package ch.openech.mj.db;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Just for logging.<p>
 * 
 * <b>note:</b> not all set methods are handled yet. So this class is only useable
 * for the things done in Minimal-J.
 */
class LoggingPreparedStatement implements PreparedStatement {

	private final PreparedStatement preparedStatement;
	private final String query;
	private final Logger logger;
	private final Map<Integer, String> parameters = new HashMap<>();
	
	public LoggingPreparedStatement(Connection connection, String query, Logger logger) throws SQLException {
		this.query = query;
		this.preparedStatement = connection.prepareStatement(query);
		this.logger = logger;
	}
	
	public LoggingPreparedStatement(Connection connection, String query, int autoGeneratedKeys, Logger logger) throws SQLException {
		this.query = query;
		this.preparedStatement = connection.prepareStatement(query, autoGeneratedKeys);
		this.logger = logger;
	}
	
	private void log() {
		StringBuilder s = new StringBuilder(query.length() + 20 * parameters.size());
		int paramPos = 0;
		for (int i = 0; i<query.length(); i++) {
			char c = query.charAt(i);
			if (c == '?') {
				paramPos++;
				if (parameters.containsKey(paramPos)) {
					s.append(parameters.get(paramPos));
				} else{
					s.append('?');
				}
			} else {
				s.append(c);
			}
		}
		logger.fine(s.toString());
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return preparedStatement.unwrap(iface);
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		log();
		return preparedStatement.executeQuery(sql);
	}

	public ResultSet executeQuery() throws SQLException {
		log();
		return preparedStatement.executeQuery();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return preparedStatement.isWrapperFor(iface);
	}

	public int executeUpdate(String sql) throws SQLException {
		log();
		return preparedStatement.executeUpdate(sql);
	}

	public int executeUpdate() throws SQLException {
		log();
		return preparedStatement.executeUpdate();
	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		preparedStatement.setNull(parameterIndex, sqlType);
		parameters.put(parameterIndex, "null");
	}

	public void close() throws SQLException {
		preparedStatement.close();
	}

	public int getMaxFieldSize() throws SQLException {
		return preparedStatement.getMaxFieldSize();
	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		preparedStatement.setBoolean(parameterIndex, x);
	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		preparedStatement.setByte(parameterIndex, x);
	}

	public void setMaxFieldSize(int max) throws SQLException {
		preparedStatement.setMaxFieldSize(max);
	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		preparedStatement.setShort(parameterIndex, x);
	}

	public int getMaxRows() throws SQLException {
		return preparedStatement.getMaxRows();
	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		preparedStatement.setInt(parameterIndex, x);
		parameters.put(parameterIndex, String.valueOf(x));
	}

	public void setMaxRows(int max) throws SQLException {
		preparedStatement.setMaxRows(max);
	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		preparedStatement.setLong(parameterIndex, x);
	}

	public void setEscapeProcessing(boolean enable) throws SQLException {
		preparedStatement.setEscapeProcessing(enable);
	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		preparedStatement.setFloat(parameterIndex, x);
	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		preparedStatement.setDouble(parameterIndex, x);
	}

	public int getQueryTimeout() throws SQLException {
		return preparedStatement.getQueryTimeout();
	}

	public void setQueryTimeout(int seconds) throws SQLException {
		preparedStatement.setQueryTimeout(seconds);
	}

	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		preparedStatement.setBigDecimal(parameterIndex, x);
	}

	public void setString(int parameterIndex, String x) throws SQLException {
		preparedStatement.setString(parameterIndex, x);
	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		preparedStatement.setBytes(parameterIndex, x);
	}

	public void cancel() throws SQLException {
		preparedStatement.cancel();
	}

	public SQLWarning getWarnings() throws SQLException {
		return preparedStatement.getWarnings();
	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		preparedStatement.setDate(parameterIndex, x);
	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		preparedStatement.setTime(parameterIndex, x);
	}

	public void clearWarnings() throws SQLException {
		preparedStatement.clearWarnings();
	}

	public void setCursorName(String name) throws SQLException {
		preparedStatement.setCursorName(name);
	}

	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		preparedStatement.setTimestamp(parameterIndex, x);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		preparedStatement.setAsciiStream(parameterIndex, x, length);
	}

	public boolean execute(String sql) throws SQLException {
		log();
		return preparedStatement.execute(sql);
	}

	@SuppressWarnings("deprecation")
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		preparedStatement.setUnicodeStream(parameterIndex, x, length);
	}

	public ResultSet getResultSet() throws SQLException {
		return preparedStatement.getResultSet();
	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		preparedStatement.setBinaryStream(parameterIndex, x, length);
	}

	public int getUpdateCount() throws SQLException {
		return preparedStatement.getUpdateCount();
	}

	public boolean getMoreResults() throws SQLException {
		return preparedStatement.getMoreResults();
	}

	public void clearParameters() throws SQLException {
		preparedStatement.clearParameters();
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		preparedStatement.setObject(parameterIndex, x, targetSqlType);
	}

	public void setFetchDirection(int direction) throws SQLException {
		preparedStatement.setFetchDirection(direction);
	}

	public int getFetchDirection() throws SQLException {
		return preparedStatement.getFetchDirection();
	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		preparedStatement.setObject(parameterIndex, x);
		if (x instanceof String) {
			parameters.put(parameterIndex, "'" + x + "'");
		} else {
			parameters.put(parameterIndex, "" + x);
		}
	}

	public void setFetchSize(int rows) throws SQLException {
		preparedStatement.setFetchSize(rows);
	}

	public int getFetchSize() throws SQLException {
		return preparedStatement.getFetchSize();
	}

	public int getResultSetConcurrency() throws SQLException {
		return preparedStatement.getResultSetConcurrency();
	}

	public boolean execute() throws SQLException {
		log();
		return preparedStatement.execute();
	}

	public int getResultSetType() throws SQLException {
		return preparedStatement.getResultSetType();
	}

	public void addBatch(String sql) throws SQLException {
		preparedStatement.addBatch(sql);
	}

	public void clearBatch() throws SQLException {
		preparedStatement.clearBatch();
	}

	public void addBatch() throws SQLException {
		preparedStatement.addBatch();
	}

	public int[] executeBatch() throws SQLException {
		log();
		return preparedStatement.executeBatch();
	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		preparedStatement.setCharacterStream(parameterIndex, reader, length);
	}

	public void setRef(int parameterIndex, Ref x) throws SQLException {
		preparedStatement.setRef(parameterIndex, x);
	}

	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		preparedStatement.setBlob(parameterIndex, x);
	}

	public void setClob(int parameterIndex, Clob x) throws SQLException {
		preparedStatement.setClob(parameterIndex, x);
	}

	public Connection getConnection() throws SQLException {
		return preparedStatement.getConnection();
	}

	public void setArray(int parameterIndex, Array x) throws SQLException {
		preparedStatement.setArray(parameterIndex, x);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return preparedStatement.getMetaData();
	}

	public boolean getMoreResults(int current) throws SQLException {
		return preparedStatement.getMoreResults(current);
	}

	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		preparedStatement.setDate(parameterIndex, x, cal);
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		return preparedStatement.getGeneratedKeys();
	}

	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		preparedStatement.setTime(parameterIndex, x, cal);
	}

	public int executeUpdate(String sql, int autoGeneratedKeys)
			throws SQLException {
		log();
		return preparedStatement.executeUpdate(sql, autoGeneratedKeys);
	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		preparedStatement.setTimestamp(parameterIndex, x, cal);
	}

	public void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException {
		preparedStatement.setNull(parameterIndex, sqlType, typeName);
	}

	public int executeUpdate(String sql, int[] columnIndexes)
			throws SQLException {
		log();
		return preparedStatement.executeUpdate(sql, columnIndexes);
	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		preparedStatement.setURL(parameterIndex, x);
	}

	public int executeUpdate(String sql, String[] columnNames)
			throws SQLException {
		log();
		return preparedStatement.executeUpdate(sql, columnNames);
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		return preparedStatement.getParameterMetaData();
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		preparedStatement.setRowId(parameterIndex, x);
	}

	public void setNString(int parameterIndex, String value)
			throws SQLException {
		preparedStatement.setNString(parameterIndex, value);
	}

	public boolean execute(String sql, int autoGeneratedKeys)
			throws SQLException {
		log();
		return preparedStatement.execute(sql, autoGeneratedKeys);
	}

	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		preparedStatement.setNCharacterStream(parameterIndex, value, length);
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		preparedStatement.setNClob(parameterIndex, value);
	}

	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		preparedStatement.setClob(parameterIndex, reader, length);
	}

	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		log();
		return preparedStatement.execute(sql, columnIndexes);
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		preparedStatement.setBlob(parameterIndex, inputStream, length);
	}

	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		preparedStatement.setNClob(parameterIndex, reader, length);
	}

	public boolean execute(String sql, String[] columnNames)
			throws SQLException {
		log();
		return preparedStatement.execute(sql, columnNames);
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		preparedStatement.setSQLXML(parameterIndex, xmlObject);
	}

	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException {
		preparedStatement.setObject(parameterIndex, x, targetSqlType,
				scaleOrLength);
	}

	public int getResultSetHoldability() throws SQLException {
		return preparedStatement.getResultSetHoldability();
	}

	public boolean isClosed() throws SQLException {
		return preparedStatement.isClosed();
	}

	public void setPoolable(boolean poolable) throws SQLException {
		preparedStatement.setPoolable(poolable);
	}

	public boolean isPoolable() throws SQLException {
		return preparedStatement.isPoolable();
	}

	public void closeOnCompletion() throws SQLException {
		preparedStatement.closeOnCompletion();
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		preparedStatement.setAsciiStream(parameterIndex, x, length);
	}

	public boolean isCloseOnCompletion() throws SQLException {
		return preparedStatement.isCloseOnCompletion();
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		preparedStatement.setBinaryStream(parameterIndex, x, length);
	}

	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		preparedStatement.setCharacterStream(parameterIndex, reader, length);
	}

	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		preparedStatement.setAsciiStream(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		preparedStatement.setBinaryStream(parameterIndex, x);
	}

	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		preparedStatement.setCharacterStream(parameterIndex, reader);
	}

	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		preparedStatement.setNCharacterStream(parameterIndex, value);
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		preparedStatement.setClob(parameterIndex, reader);
	}

	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		preparedStatement.setBlob(parameterIndex, inputStream);
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		preparedStatement.setNClob(parameterIndex, reader);
	}
	
}
