package org.minimalj.repository.query;

public class Limit implements Query {
	private static final long serialVersionUID = 1L;

	private final Query query;
	private final Integer offset;
	private final int rows;
	
	public Limit(Query query, int rows) {
		this(query, rows, null);
	}
	
	public Limit(Query query, int rows, Integer offset) {
		this.query = query;
		this.offset = offset;
		this.rows = rows;
	}

	public Query getQuery() {
		return query;
	}
	
	public int getRows() {
		return rows;
	}
	
	public Integer getOffset() {
		return offset;
	}
}