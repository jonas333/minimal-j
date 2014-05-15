package ch.openech.mj.server;

import java.util.concurrent.Callable;

import ch.openech.mj.db.Persistence;
import ch.openech.mj.util.StringUtils;

public class DbReadHistory<T> implements Callable<T> {

	private final Class<T> clazz;
	private final long id;
	private final Integer time;
	
	public DbReadHistory(Class<T> clazz, String id, String time) {
		this(clazz, Long.valueOf(id), StringUtils.isEmpty(time) ? null : Integer.valueOf(time));
	}

	public DbReadHistory(Class<T> clazz, long id, Integer time) {
		this.clazz = clazz;
		this.id = id;
		this.time = time;
	}

	@Override
	public T call() throws Exception {
		T result = Persistence.get(clazz).read(clazz, id, time);
		return result;
	}
	
}
