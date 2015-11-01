package org.minimalj.backend;

import java.util.List;

import org.minimalj.transaction.criteria.Criteria;

/**
 * The common interface of all types of persistences. Note that specific implementations
 * can have more methods. See for example the <code>execute</code> methods in SqlPersistence
 *
 */
public interface Persistence {

	public <T> T read(Class<T> clazz, Object id);

	public <T> List<T> read(Class<T> clazz, Criteria criteria, int maxResults);

	public static enum Return { VOID, ID, COMPLETE }

	public default <T> T insert(T object) {
		return insert(object, Return.ID);
	}
	
	public <T> T insert(T object, Return r);

	public default <T> T update(T object) {
		return update(object, Return.COMPLETE);
	}

	public <T> T update(T object, Return r);

	public <T> void delete(Class<T> clazz, Object id);
	
}
