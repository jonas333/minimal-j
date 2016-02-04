package org.minimalj.transaction.persistence;

import org.minimalj.backend.Persistence;
import org.minimalj.backend.sql.SqlPersistence;
import org.minimalj.transaction.PersistenceTransaction;
import org.minimalj.util.SerializationContainer;

public abstract class ListTransaction<T> implements PersistenceTransaction<T> {
	private static final long serialVersionUID = 1L;
	
	protected final Object listId;

	protected ListTransaction(Object listId) {
		this.listId = listId;
	}

	@Override
	public Class<?> getEntityClazz() {
		return null;
	}
	
	public static class ReadElementTransaction extends ListTransaction<Object> {
		private static final long serialVersionUID = 1L;
		private final int position;
		
		public ReadElementTransaction(Object listId, int position) {
			super(listId);
			this.position = position;
		}

		@Override
		public Object execute(Persistence persistence) {
			return ((SqlPersistence) persistence).elementAt(listId, position);
		}
	}
	
	public static class AddTransaction<T> extends ListTransaction<T> {
		private static final long serialVersionUID = 1L;
		protected final Object element;

		public AddTransaction(Object listId, T element) {
			super(listId);
			this.element = SerializationContainer.wrap(element);
		}

		@Override
		public T execute(Persistence persistence) {
			T unwrapped = (T) SerializationContainer.unwrap(element);
			((SqlPersistence) persistence).add(listId, unwrapped);
			return null;
		}
	}
	
	public static class SetTransaction<T> extends AddTransaction<T> {
		private static final long serialVersionUID = 1L;
		private final int position;

		public SetTransaction(Object listId, T element, int position) {
			super(listId, element);
			this.position = position;
		}

		@Override
		public T execute(Persistence persistence) {
			T unwrapped = (T) SerializationContainer.unwrap(element);
			((SqlPersistence) persistence).set(listId, unwrapped, position);
			return null;
		}
	}

	public static class RemoveTransaction extends ListTransaction<Integer> {
		private static final long serialVersionUID = 1L;
		private final int position;
		
		public RemoveTransaction(Object listId, int position) {
			super(listId);
			this.position = position;
		}

		@Override
		public Integer execute(Persistence persistence) {
			((SqlPersistence) persistence).remove(listId, position);
			return null;
		}
	}

	public static class SizeTransaction extends ListTransaction<Integer> {
		private static final long serialVersionUID = 1L;
		
		public SizeTransaction(Object listId) {
			super(listId);
		}

		@Override
		public Integer execute(Persistence persistence) {
			return ((SqlPersistence) persistence).size(listId);
		}
	}

}