package ch.openech.mj.server;

import java.util.concurrent.Callable;

import ch.openech.mj.db.Persistence;

public class DbInsert<T> implements Callable<Long> {

	private final T object;
	
	public DbInsert(T object) {
		this.object = object;
	}

	@Override
	public Long call() throws Exception {
		long id = Persistence.get(object.getClass()).insert(object);
		return id;
	}
	
}
