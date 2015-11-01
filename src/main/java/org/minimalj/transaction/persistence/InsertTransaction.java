package org.minimalj.transaction.persistence;

import org.minimalj.backend.Persistence;
import org.minimalj.backend.Persistence.Return;
import org.minimalj.transaction.PersistenceTransaction;
import org.minimalj.util.SerializationContainer;

@SuppressWarnings("unchecked")
public class InsertTransaction<T> implements PersistenceTransaction<Object> {
	private static final long serialVersionUID = 1L;

	private final T object;
	private final Return r;
	
	public InsertTransaction(Object object, Return r) {
		this.object = (T) SerializationContainer.wrap(object);
		this.r = r;
	}
	
	@Override
	public Class<?> getEntityClazz() {
		return SerializationContainer.unwrap(object).getClass();
	}
	
	@Override
	public Object execute(Persistence persistence) {
		return persistence.insert(SerializationContainer.unwrap(object), r);
	}

}