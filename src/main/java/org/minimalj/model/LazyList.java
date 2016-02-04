package org.minimalj.model;

import java.util.AbstractList;

import org.minimalj.backend.Backend;
import org.minimalj.backend.sql.SqlPersistence;
import org.minimalj.transaction.PersistenceTransaction;
import org.minimalj.transaction.persistence.ListTransaction.AddTransaction;
import org.minimalj.transaction.persistence.ListTransaction.ReadElementTransaction;
import org.minimalj.transaction.persistence.ListTransaction.RemoveTransaction;
import org.minimalj.transaction.persistence.ListTransaction.SizeTransaction;

public class LazyList<E> extends AbstractList<E> {

	private final Object listId;
	private final transient SqlPersistence persistence;
	
	public LazyList(SqlPersistence persistence, Object listId) {
		this.persistence = persistence;
		this.listId = listId;
	}
	
	private <T> T execute(PersistenceTransaction<T> transaction) {
		if (persistence != null) {
			return persistence.execute(transaction);
		} else {
			return Backend.getInstance().execute(transaction);
		}
	}
	public void load(int maxElements) {
		// TODO: es sollten im GUI nicht dauernd alle Elemente vom Backend geladen
		// werden. Ein kleiner poor-mans cache ist angebraucht. Eventuell auch paging
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		return (E) execute(new ReadElementTransaction(listId, index));
	}

	@Override
	public int size() {
		return execute(new SizeTransaction(listId));
	}
	
	@Override
	public boolean add(E element) {
		execute(new AddTransaction<E>(listId, element));
		return true;
	}

	@Override
	public E remove(int index) {
		execute(new RemoveTransaction(listId, index));
		return null;
	}
}
