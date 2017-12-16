package org.minimalj.repository.sql;

import java.lang.annotation.Annotation;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.properties.VirtualProperty;
import org.minimalj.util.GenericUtils;
import org.minimalj.util.IdUtils;
import org.minimalj.util.LoggingRuntimeException;

/**
 * Specialization of SubTable to store a list of String, Integer, Long, BigDecimal.
 */
public class SubPrimitiveTable<PARENT, ELEMENT> extends SubTable<PARENT, ELEMENT> implements ListTable<PARENT, ELEMENT> {

	private final PropertyInterface property;

	public SubPrimitiveTable(SqlRepository sqlRepository, String name, Class<ELEMENT> clazz, PropertyInterface property,
			PropertyInterface parentIdProperty) {
		super(sqlRepository, name, clazz, parentIdProperty);

		this.property = property;
	}

	@Override
	public void addList(PARENT parent, List<ELEMENT> objects) {
		try (PreparedStatement insertStatement = createStatement(sqlRepository.getConnection(), insertQuery, false)) {
			Object parentId = IdUtils.getId(parent);
			for (int position = 0; position < objects.size(); position++) {
				Object object = objects.get(position);
				insertStatement.setObject(1, parentId);
				insertStatement.setInt(2, position);
				insertStatement.setObject(3, object);
				insertStatement.execute();
			}
		} catch (SQLException x) {
			throw new LoggingRuntimeException(x, sqlLogger, "addList failed");
		}
	}

	protected void update(Object parentId, int position, Object object) throws SQLException {
		try (PreparedStatement updateStatement = createStatement(sqlRepository.getConnection(), updateQuery, false)) {
			updateStatement.setObject(1, object);
			updateStatement.setObject(2, parentId);
			updateStatement.setInt(3, position);
			updateStatement.execute();
		}
	}

	protected void insert(Object parentId, int position, Object object) throws SQLException {
		try (PreparedStatement insertStatement = createStatement(sqlRepository.getConnection(), insertQuery, false)) {
			insertStatement.setObject(1, parentId);
			insertStatement.setInt(2, position);
			insertStatement.setObject(3, object);
			insertStatement.execute();
		}
	}

	protected void delete(Object parentId, int position) throws SQLException {
		try (PreparedStatement deleteStatement = createStatement(sqlRepository.getConnection(), deleteQuery, false)) {
			deleteStatement.setObject(1, parentId);
			deleteStatement.setInt(2, position);
			deleteStatement.execute();
		}
	}

	@Override
	public List<ELEMENT> getList(PARENT parent) {
		try (PreparedStatement selectByIdStatement = createStatement(sqlRepository.getConnection(), selectByIdQuery,
				false)) {
			selectByIdStatement.setObject(1, IdUtils.getId(parent));
			List<ELEMENT> result = new ArrayList<>();
			try (ResultSet resultSet = selectByIdStatement.executeQuery()) {
				while (resultSet.next()) {
					ELEMENT object = sqlRepository.readResultSetRowPrimitive(clazz,  resultSet);
					result.add(object);
				}
			}
			return result;
		} catch (SQLException x) {
			throw new LoggingRuntimeException(x, sqlLogger, "getList failed");
		}
	}
	
	// Queries

	@Override
	protected String selectByIdQuery() {
		StringBuilder query = new StringBuilder();
		query.append("SELECT value FROM ").append(getTableName()).append(" WHERE id = ? ORDER BY position");
		return query.toString();
	}

	@Override
	protected String insertQuery() {
		StringBuilder s = new StringBuilder();
		s.append("INSERT INTO ").append(getTableName()).append(" (id, position, value) VALUES (?, ? ,?)");
		return s.toString();
	}

	@Override
	protected String updateQuery() {
		StringBuilder s = new StringBuilder();
		s.append("UPDATE ").append(getTableName()).append(" SET value = ? WHERE id = ? AND position = ?");
		return s.toString();
	}

	@Override
	protected void addFieldColumns(SqlDialect dialect, StringBuilder s) {
		s.append(",\n value ");
		PropertyInterface property = new DelegateProperty();
		dialect.addColumnDefinition(s, property);
		s.append(" NOT NULL");
	}

	/*
	 * for column definition the SqlDialect has to know about class of the elements
	 * and maybe about the size of the strings. This class delegates the information
	 * to the list field. Internal only.
	 */
	private class DelegateProperty extends VirtualProperty {

		@Override
		public Class<?> getClazz() {
			return GenericUtils.getGenericClass(property.getType());
		}

		@Override
		public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
			return property.getAnnotation(annotationClass);
		}
		
		@Override
		public Class<?> getDeclaringClass() {
			return property.getDeclaringClass();
		}
		
		@Override
		public String getName() {
			return null; // not used
		}

		@Override
		public Object getValue(Object object) {
			return null; // not used
		}

		@Override
		public void setValue(Object object, Object value) {
			// not used
		}
	}

}