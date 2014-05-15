package ch.openech.mj.server;

import java.util.List;

import ch.openech.mj.db.DbPersistenceHelper;
import ch.openech.mj.db.Persistence;
import ch.openech.mj.db.Table;
import ch.openech.mj.model.annotation.ViewOf;
import ch.openech.mj.toolkit.ClientToolkit.Search;

public class DbSearch<T> implements Search<T> {

	private final Class<?> entityClass;
	private final Class<T> resultClass;
	private final Object[] fields;
	
	public DbSearch(Class<T> resultClass) {
		this(resultClass, null);
	}
	
	public DbSearch(Class<T> resultClass, Object[] fields) {
		if (ViewOf.class.isAssignableFrom(resultClass)) {
			this.entityClass = DbPersistenceHelper.getViewedClass(resultClass);
		} else {
			this.entityClass = (Class<T>) resultClass;
		}
		this.resultClass = (Class<T>) resultClass;
		this.fields = fields;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<T> search(String query) {
		if (resultClass == entityClass) {
			if (fields == null) {
				return ((Table) Persistence.get(entityClass).getTable(entityClass)).search(query, 100);
			} else {
				return ((Table) Persistence.get(entityClass).getTable(entityClass)).search(fields, query, 100);
			}
		} else {
			if (fields == null) {
				return ((Table) Persistence.get(entityClass).getTable(entityClass)).search(resultClass, query, 100);
			} else {
				return ((Table) Persistence.get(entityClass).getTable(entityClass)).search(resultClass, fields, query, 100);
			}
		}
	}
	
}
