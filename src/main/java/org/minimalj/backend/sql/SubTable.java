package org.minimalj.backend.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.minimalj.model.properties.PropertyInterface;

/**
 * Minimal-J internal
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SubTable extends AbstractTable {
	private static final int[] EMPTY_PATH = new int[0];
	
	protected final String selectByIdQuery;
	protected final String updateQuery;
	protected final String deleteQuery;
	
	private Integer level;
	
	public SubTable(SqlPersistence sqlPersistence, String prefix, Class clazz, PropertyInterface idProperty) {
		super(sqlPersistence, prefix, clazz, idProperty);
		
		selectByIdQuery = selectByIdQuery();
		updateQuery = updateQuery();
		deleteQuery = deleteQuery();
	}

	private int getLevel() {
		// getLevel is indirectly called by super constructor
		// this is why the level has to be derived from the table name
		if (level == null) {
			level = getTableName().split("__").length - 1;
		}
		return level;
	}
	
	public void insert(Object parentId, List objects) throws SQLException {
		insert(parentId, EMPTY_PATH, objects);
	}
	
	private void insert(Object parentId, int[] parentPositionPath, List objects) throws SQLException {
		checkPath(parentPositionPath);
		try (PreparedStatement insertStatement = createStatement(sqlPersistence.getConnection(), insertQuery, false)) {
			for (int position = 0; position<objects.size(); position++) {
				Object object = objects.get(position);
				int parameterPos = setParameters(insertStatement, object, false, ParameterMode.INSERT, parentId);
				for (int l = 0; l<parentPositionPath.length; l++) {
					insertStatement.setInt(parameterPos++, parentPositionPath[l]);
				}
				insertStatement.setInt(parameterPos++, position);
				insertStatement.execute();
				//
				// Myst: why don't generics work here or entrySet??
				for (Object o : subTables.entrySet()) {
					Map.Entry subTableEntry = (Map.Entry) o;
					SubTable subTable = (SubTable) subTableEntry.getValue();
					List list;
					try {
						PropertyInterface key = (PropertyInterface) getLists().get(subTableEntry.getKey());
						list = (List) key.getValue(object);
						if (list != null && !list.isEmpty()) {
							int[] myPath = addPostionToPath(parentPositionPath, position);
							subTable.insert(parentId, myPath, list);
						}
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}

	private int[] addPostionToPath(int[] positionPath, int position) {
		if (positionPath.length == 0) {
			return new int[]{position};
		} else {
			int[] newPositionPath = new int[positionPath.length+1];
			System.arraycopy(positionPath, 0, newPositionPath, 0, positionPath.length);
			newPositionPath[newPositionPath.length-1] = position;
			return newPositionPath;
		}
	}

	protected void update(Object parentId, List objects) throws SQLException {
		update(parentId, EMPTY_PATH, objects);
	}
	
	private void update(Object parentId, int[] parentPositionPath, List objects) throws SQLException {
		checkPath(parentPositionPath);
		List objectsInDb = read(parentId, parentPositionPath);
		int position = 0;
		while (position < Math.max(objects.size(), objectsInDb.size())) {
			if (position < objectsInDb.size() && position < objects.size()) {
				update(parentId, parentPositionPath, position, objects.get(position));
			} else if (position < objectsInDb.size()) {
				// delete all beginning from this position with one delete statement
				delete(parentId, parentPositionPath, position);
				break; 
			} else /* if (position < objects.size()) */ {
				insert(parentId, parentPositionPath, position, objects.get(position));
			}
			position++;
		}
	}

	protected void update(Object parentId, int[] parentPositionPath, int position, Object object) throws SQLException {
		try (PreparedStatement updateStatement = createStatement(sqlPersistence.getConnection(), updateQuery, false)) {
			int parameterPos = setParameters(updateStatement, object, false, ParameterMode.UPDATE, parentId);
			for (int parentPosition : parentPositionPath) {
				updateStatement.setInt(parameterPos++, parentPosition);
			}
			updateStatement.execute();
		}
	}

	protected void insert(Object parentId, int[] parentPositionPath, int position, Object object) throws SQLException {
		try (PreparedStatement insertStatement = createStatement(sqlPersistence.getConnection(), insertQuery, false)) {
			int parameterPos = setParameters(insertStatement, object, false, ParameterMode.INSERT, parentId);
			for (int parentPosition : parentPositionPath) {
				insertStatement.setInt(parameterPos++, parentPosition);
			}
			insertStatement.execute();
		}
	}
	
	protected void delete(Object parentId, int[] parentPositionPath, int position) throws SQLException {
		try (PreparedStatement deleteStatement = createStatement(sqlPersistence.getConnection(), deleteQuery, false)) {
			int parameterPos = 1;
			deleteStatement.setObject(parameterPos++, parentId);
			for (int parentPosition : parentPositionPath) {
				deleteStatement.setInt(parameterPos++, parentPosition);
			}
			deleteStatement.execute();
		}
	}

	public List read(Object parentId) throws SQLException {
		return read(parentId, EMPTY_PATH);
	}
	
	private List read(Object parentId, int[] parentPositionPath) throws SQLException {
		try (PreparedStatement selectByIdStatement = createStatement(sqlPersistence.getConnection(), selectByIdQuery, false)) {
			int parameterPos = 1;
			selectByIdStatement.setObject(parameterPos++, parentId);
			for (int parentPosition : parentPositionPath) {
				selectByIdStatement.setInt(parameterPos++, parentPosition);
			}
			List list = executeSelectAll(selectByIdStatement);
			
			int position = 0;
			for (Object object : list) {
				int[] positionPath = addPostionToPath(parentPositionPath, position++);
				loadRelations(object, parentId, positionPath);
			}
			return list;
		}
	}

	protected void loadRelations(Object object, Object id, int[] positionPath) throws SQLException {
		for (Object o : subTables.entrySet()) {
			Map.Entry subTableEntry = (Map.Entry) o;
			SubTable subTable = (SubTable) subTableEntry.getValue();
			
			PropertyInterface listProperty = (PropertyInterface) getLists().get(subTableEntry.getKey());
			List list = (List) listProperty.getValue(object);
			list.addAll(subTable.read(id));
		}
	}
	
	// Queries
	
	@Override
	protected String selectByIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM "); query.append(getTableName()); query.append(" WHERE id = ? ORDER BY position");
		return query.toString();
	}
	
	@Override
	protected String insertQuery() {
		StringBuilder s = new StringBuilder();
		
		s.append("INSERT INTO "); s.append(getTableName()); s.append(" (");
		for (Object columnNameObject : getColumns().keySet()) {
			// myst, direkt auf columnNames zugreiffen funktionert hier nicht
			String columnName = (String) columnNameObject;
			s.append(columnName);
			s.append(", ");
		}
		s.append("id, position");
		for (int i = 2; i<=getLevel(); i++) {
			s.append(", position").append(i);
		}
		s.append(") VALUES (");
		for (int i = 0; i<getColumns().size(); i++) {
			s.append("?, ");
		}
		for (int i = 2; i<=getLevel(); i++) {
			s.append("?, ");
		}
		s.append("?, ?)");

		return s.toString();
	}

	protected String updateQuery() {
		StringBuilder s = new StringBuilder();
		
		s.append("UPDATE "); s.append(getTableName()); s.append(" SET ");
		for (Object columnNameObject : getColumns().keySet()) {
			s.append((String) columnNameObject);
			s.append("= ?, ");
		}
		s.delete(s.length()-2, s.length());
		s.append(" WHERE id = ? AND position = ? ");
		for (int i = 2; i<=getLevel(); i++) {
			s.append("AND position").append(i).append(" = ? ");
		}

		// position = ?
		
		return s.toString();
	}
	
	protected String deleteQuery() {
		StringBuilder s = new StringBuilder();
		s.append("DELETE FROM " + getTableName() + " WHERE id = ? AND position = ?");
		for (int i = 2; i<=getLevel(); i++) {
			s.append("AND position").append(i).append(" = ?");
		}
		return s.toString();
	}
	
	@Override
	protected void addSpecialColumns(SqlSyntax syntax, StringBuilder s) {
		s.append(" id ");
		syntax.addColumnDefinition(s, idProperty);
		s.append(",\n position INTEGER NOT NULL");
		for (int i = 2; i<=getLevel(); i++) {
			s.append(",\n position").append(i).append(" INTEGER NOT NULL");
		}
	}
	
	@Override
	protected void addPrimaryKey(SqlSyntax syntax, StringBuilder s) {
		StringBuilder keys = new StringBuilder("id, position");
		for (int i = 2; i<=getLevel(); i++) {
			keys.append(",\n position").append(i).append(" INTEGER NOT NULL");
		}
		syntax.addPrimaryKey(s, keys.toString());
	}
	
	private void checkPath(int[] parentPositionPath) {
		if (parentPositionPath.length != getLevel() - 1) {
			throw new IllegalArgumentException("ParentPath has length " + parentPositionPath.length + " but expected is " + (getLevel()-1));
		}
	}
}
