package org.minimalj.backend.sql;

public class SqlListId {

	private final Object parentId;
	private final String subTableName;
	
	public SqlListId(Object parentId, String subTableName) {
		this.parentId = parentId;
		this.subTableName = subTableName;
	}

	public Object getParentId() {
		return parentId;
	}
	
	public String getSubTableName() {
		return subTableName;
	}
	
}
