package org.minimalj.backend.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.IdUtils;

/**
 * Minimal-J internal
 * 
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class LazyListTable extends Table implements ListTable {

	protected final String selectByParentQuery;
	protected final String selectByParentAndPositionQuery;
	protected final String updateQuery;
	protected final String deleteQuery;
	protected final String shiftQuery;
	protected final String sizeQuery;
	protected final PropertyInterface parentIdProperty;

	public LazyListTable(SqlPersistence sqlPersistence, String prefix, Class clazz, PropertyInterface parentIdProperty) {
		super(sqlPersistence, clazz);
		this.parentIdProperty = parentIdProperty;
		
		selectByParentQuery = selectByParentQuery();
		selectByParentAndPositionQuery = selectByParentAndPositionQuery();
		updateQuery = updateQuery();
		deleteQuery = deleteQuery();
		shiftQuery = shiftQuery();
		sizeQuery = sizeQuery();
	}
	
	@Override
	public List read(Object parentId) {
		try (PreparedStatement statement = createStatement(sqlPersistence.getConnection(), selectByParentQuery, false)) {
			statement.setObject(1, parentId);
			return executeSelectAll(statement);
		} catch (SQLException x) {
			throw new RuntimeException(x.getMessage());
		}	
	}
	
	@Override
	public void insert(Object parentId, List objects) {
		try (PreparedStatement insertStatement = createStatement(sqlPersistence.getConnection(), insertQuery, false)) {
			for (int position = 0; position<objects.size(); position++) {
				Object object = objects.get(position);
				Object id = IdUtils.createId();
				IdUtils.setId(object, id);
				int parameterPos = setParameters(insertStatement, object, false, ParameterMode.INSERT, parentId);
				insertStatement.setObject(parameterPos++, id);
				insertStatement.setInt(parameterPos++, position);
				insertStatement.execute();
				insertLists(object, id);
			}
		} catch (SQLException x) {
			throw new RuntimeException(x.getMessage());
		}
	}

	@Override
	public void update(Object parentId, List objects) {
		List objectsInDb = read(parentId);
		int position = 0;
		while (position < Math.max(objects.size(), objectsInDb.size())) {
			if (position < objectsInDb.size() && position < objects.size()) {
				update(parentId, position, objects.get(position));
			} else if (position < objectsInDb.size()) {
				// delete all beginning from this position with one delete statement
				delete(parentId, position);
				break; 
			} else /* if (position < objects.size()) */ {
				insert(parentId, position, objects.get(position));
			}
			position++;
		}
	}
	
	// operations for ListTransaction

	public Object elementAt(Object parentId, int position) {
		try (PreparedStatement statement = createStatement(sqlPersistence.getConnection(), selectByParentAndPositionQuery, false)) {
			statement.setObject(1, parentId);
			statement.setInt(2, position);
			Object object = executeSelect(statement);
			if (object != null) {
				loadLists(object, IdUtils.getId(object));
			}
			return object;			
		} catch (SQLException x) {
			throw new RuntimeException(x.getMessage());
		}	
	}
	
	public void add(Object parentId, Object element) {
		int position = size(parentId);
		insert(parentId, position, element);
	}

	public void set(Object parentId, Object element, int position) {
		shift(parentId, position, true);
		insert(parentId, position, element);
	}
	
	public void remove(Object parentId, int position) {
		delete(parentId, position);
		shift(parentId, position, false);
	}

	public int size(Object parentId) {
		try (PreparedStatement sizeStatement = createStatement(sqlPersistence.getConnection(), sizeQuery, false)) {
			sizeStatement.setObject(1, parentId);
			try (ResultSet resultSet = sizeStatement.executeQuery()) {
				resultSet.next();
				return resultSet.getInt(1);
			}
		} catch (SQLException x) {
			throw new RuntimeException(x.getMessage());
		}
	}
	
	protected void shift(Object parentId, int position, boolean up) {
		try (PreparedStatement shiftStatement = createStatement(sqlPersistence.getConnection(), shiftQuery, false)) {
			shiftStatement.setInt(1, up ? 1 : -1);
			shiftStatement.setObject(2, parentId);
			shiftStatement.setInt(3, position);
			shiftStatement.execute();
		} catch (SQLException x) {
			throw new RuntimeException(x.getMessage());
		}
	}
	
	//
	
	protected void update(Object parentId, int position, Object object) {
		try (PreparedStatement updateStatement = createStatement(sqlPersistence.getConnection(), updateQuery, false)) {
			int parameterPos = setParameters(updateStatement, object, false, ParameterMode.UPDATE, parentId);
			updateStatement.setInt(parameterPos++, position);
			updateStatement.execute();
		} catch (SQLException x) {
			throw new RuntimeException(x.getMessage());
		}
	}

	protected void insert(Object parentId, int position, Object object) {
		try (PreparedStatement insertStatement = createStatement(sqlPersistence.getConnection(), insertQuery, false)) {
			int parameterPos = setParameters(insertStatement, object, false, ParameterMode.INSERT, parentId);
			insertStatement.setInt(parameterPos++, position);
			insertStatement.execute();
		} catch (SQLException x) {
			throw new RuntimeException(x.getMessage());
		}
	}
	
	protected void delete(Object parentId, int position) {
		try (PreparedStatement deleteStatement = createStatement(sqlPersistence.getConnection(), deleteQuery, false)) {
			deleteStatement.setObject(1, parentId);
			deleteStatement.setInt(2, position);
			deleteStatement.execute();
		} catch (SQLException x) {
			throw new RuntimeException(x.getMessage());
		}
	}


	// Queries
	
	protected String selectByParentQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ").append(getTableName()).append(" WHERE parentId = ? ORDER BY position");
		return query.toString();
	}

	protected String selectByParentAndPositionQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ").append(getTableName()).append(" WHERE parentId = ? and position = ? ORDER BY position");
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
		s.append("parentId, id, position) VALUES (");
		for (int i = 0; i<getColumns().size(); i++) {
			s.append("?, ");
		}
		s.append("?, ?, ?)");

		return s.toString();
	}

	@Override
	protected String deleteQuery() {
		return "DELETE FROM " + getTableName() + " WHERE id = ? AND position >= ?";
	}

	protected String sizeQuery() {
		StringBuilder s = new StringBuilder();
		s.append("SELECT COUNT(*) FROM ").append(getTableName()).append(" WHERE parentId = ?");
		return s.toString();
	}

	protected String shiftQuery() {
		StringBuilder s = new StringBuilder();
		s.append("UPDATE ").append(getTableName()).append(" SET position = position + ? WHERE parentId = ? AND position >= ?");
		return s.toString();
	}
	
	@Override
	protected void addSpecialColumns(SqlSyntax syntax, StringBuilder s) {
		s.append(" parentId ");
		syntax.addColumnDefinition(s, parentIdProperty);
		s.append(", ");
		syntax.addIdColumn(s, Object.class, 0);
		s.append(",\n position INTEGER NOT NULL");
	}
	
//	@Override
//	protected void addPrimaryKey(SqlSyntax syntax, StringBuilder s) {
//		syntax.addPrimaryKey(s, "id, position");
//	}

}
