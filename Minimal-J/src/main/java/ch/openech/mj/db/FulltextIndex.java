package ch.openech.mj.db;//

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import ch.openech.mj.model.Keys;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.util.StringUtils;

public class FulltextIndex<T> implements Index<T> {
	private static final Logger logger = Logger.getLogger(FulltextIndex.class.getName());
	
	private static final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_45);
	private RAMDirectory directory;
	private IndexWriter iwriter;                                                                 
	private final Table<T> table;
	
	private final Object[] keys;
	private final PropertyInterface[] properties;
	
	public FulltextIndex(Table<T> table, Object[] keys) {
		this.table = table;
		this.keys = keys;
		this.properties = Keys.getProperties(keys);
	}
	
	private static String[] getPropertyPaths(PropertyInterface[] properties) {
		String[] paths = new String[properties.length];
		for (int i = 0; i<properties.length; i++) {
			paths[i] = properties[i].getFieldPath();
		}
		return paths;
	}
	
	private synchronized void initializeIndex() {
		if (directory == null) {
			directory = new RAMDirectory();
			try {
				IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_45, analyzer);
				iwriter = new IndexWriter(directory, config);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Initialize index failed", e);
				throw new RuntimeException("Initialize index failed");
			}
		}
	}
	
	public void insert(int id, T object) {
		initializeIndex();
		writeInIndex(id, object);

		try (IndexReader ireader = DirectoryReader.open(directory)) {
			IndexSearcher isearcher = new IndexSearcher(ireader);
			Query query = queryById(id);
			ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;
			if (hits.length != 1) throw new IllegalStateException("Id : " + id);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Couldn't search after insert");
			throw new RuntimeException("Couldn't search after insert");
		} 	
	}

	public void update(int id, T object) {
		try (IndexReader ireader = DirectoryReader.open(directory)) {
			IndexSearcher isearcher = new IndexSearcher(ireader);
			Query query = queryById(id);
			ScoreDoc[] hits = isearcher.search(query, 10).scoreDocs;
			if (hits.length != 1) throw new IllegalStateException("Id : " + id);

			iwriter.deleteDocuments(query);
			writeInIndex(id, object);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Couldn't search after update");
			throw new RuntimeException("Couldn't search after update");
		} 	
	}

	private Query queryById(int id) {
		return NumericRangeQuery.newIntRange("id", id, id, true, true);
	}
	
	public void clear() {
		try {
			if (iwriter != null) {
				iwriter.deleteAll();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Couldn't clear index of table " + table.getTableName(), e);
			throw new RuntimeException("Couldn't clear index of table " + table.getTableName());
		}	
	}

	protected Field getField(PropertyInterface property, T object, boolean indexed) {
		String fieldName = property.getFieldPath();
		
		if (property.getFieldClazz() == String.class) {
			String string = (String) property.getValue(object);
			if (string != null) {
				return new TextField(fieldName, string,	Field.Store.YES);
			}
		} 
		return null;
	}
	
	protected Field createIdField(int id) {
		return new IntField("id", id, Field.Store.YES);
	}
	
	private void writeInIndex(int id, T object) {
		try {
			Document doc = new Document();
			doc.add(createIdField(id));
			for (PropertyInterface property : properties) {
				Field field = getField(property, object, true);
				if (field != null) {
					doc.add(field);
				}
			}
		    iwriter.addDocument(doc);
		    iwriter.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public List<T> find(String text) {
		PropertyInterface[] searchProperties = Keys.getProperties(keys);
		String[] searchPaths = getPropertyPaths(searchProperties);
		
		List<T> result = new ArrayList<>();
		initializeIndex();
		if (directory.listAll().length == 0 || StringUtils.isBlank(text)) return Collections.emptyList();

		try (IndexReader ireader = DirectoryReader.open(directory)) {
			IndexSearcher isearcher = new IndexSearcher(ireader);
			QueryParser parser;
			if (searchPaths.length > 1) {
				parser = new MultiFieldQueryParser(Version.LUCENE_45, searchPaths, analyzer);
			} else {
				parser = new QueryParser(Version.LUCENE_45, searchPaths[0], analyzer);
			}
			Query query = parser.parse(text);
			ScoreDoc[] hits = isearcher.search(query, null, 50).scoreDocs;
			
			for (ScoreDoc hit : hits) {
				Document hitDoc = isearcher.doc(hit.doc);
				Integer id = Integer.parseInt(hitDoc.get("id"));
				T object = table.read(id);
				result.add(object);
			}
		} catch (ParseException x) {
			// user entered something like "*" which is not allowed
			logger.info(x.getLocalizedMessage());
		} catch (Exception x) {
			logger.log(Level.SEVERE, x.getLocalizedMessage(), x);
			x.printStackTrace();
		}
		return result;
	}

	public List<Integer> findIds(String text) {
		PropertyInterface[] searchProperties = Keys.getProperties(keys);
		String[] searchPaths = getPropertyPaths(searchProperties);
		List<Integer> result = new ArrayList<>();

		initializeIndex();
		if (directory.listAll().length == 0 || StringUtils.isBlank(text)) return Collections.emptyList();

		try (IndexReader ireader = DirectoryReader.open(directory)) {
			IndexSearcher isearcher = new IndexSearcher(ireader);
			QueryParser parser;
			if (searchPaths.length > 1) {
				parser = new MultiFieldQueryParser(Version.LUCENE_45, searchPaths, analyzer);
			} else {
				parser = new QueryParser(Version.LUCENE_45, searchPaths[0], analyzer);
			}
			Query query = parser.parse(text);
			ScoreDoc[] hits = isearcher.search(query, null, 50).scoreDocs;
			
			for (ScoreDoc hit : hits) {
				Document hitDoc = isearcher.doc(hit.doc);
				Integer id = Integer.parseInt(hitDoc.get("id"));
				result.add(id);
			}
		} catch (ParseException x) {
			// user entered something like "*" which is not allowed
			logger.info(x.getLocalizedMessage());
		} catch (Exception x) {
			logger.log(Level.SEVERE, x.getLocalizedMessage(), x);
			x.printStackTrace();
		}
		return result;
	}
	
	public T lookup(int id) {
		return table.read(id); 
	}

}
