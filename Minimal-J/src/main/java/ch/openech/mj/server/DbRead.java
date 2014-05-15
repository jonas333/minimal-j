package ch.openech.mj.server;

import java.util.concurrent.Callable;

import ch.openech.mj.db.Persistence;

public class DbRead<T> implements Callable<T> {

	private final Class<T> clazz;
	private final long id;
	
	public DbRead(Class<T> clazz, String id) {
		this(clazz, Long.valueOf(id));
	}

	public DbRead(Class<T> clazz, long id) {
		this.clazz = clazz;
		this.id = id;
	}

	@Override
	public T call() throws Exception {
		T result = Persistence.get(clazz).read(clazz, id);
		return result;
	}
	
}
