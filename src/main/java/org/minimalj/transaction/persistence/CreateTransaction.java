package org.minimalj.transaction.persistence;

import org.minimalj.backend.Persistence;
import org.minimalj.transaction.Transaction;
import org.minimalj.util.SerializationContainer;

@SuppressWarnings("unchecked")
public class CreateTransaction<T> implements Transaction<T> {
	private static final long serialVersionUID = 1L;

	private final T object;
	
	public CreateTransaction(Object object) {
		this.object = (T) SerializationContainer.wrap(object);
	}

	@Override
	public T execute(Persistence persistence) {
		return (T) persistence.create(SerializationContainer.unwrap(object));
	}

}