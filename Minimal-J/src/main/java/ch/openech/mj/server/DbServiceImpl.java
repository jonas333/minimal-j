package ch.openech.mj.server;

import java.util.ArrayList;
import java.util.List;

import ch.openech.mj.db.AbstractTable;
import ch.openech.mj.db.DbPersistence;
import ch.openech.mj.db.HistorizedTable;
import ch.openech.mj.util.IdUtils;

/**
 * Access is defined as annotations on the model classes
 * 
 * @author bruno
 */
public class DbServiceImpl implements DbService {

	private static DbPersistence persistence;
	


	@Override
	public <T> T loadHistory(T object, int time) {
		@SuppressWarnings("unchecked")
		AbstractTable<T> abstractTable = (AbstractTable<T>) persistence.getTable(object.getClass());
		if (abstractTable instanceof HistorizedTable) {
			long id = IdUtils.getId(object);
			return ((HistorizedTable<T>) abstractTable).read(id, time);
		} else {
			throw new IllegalArgumentException(object.getClass() + " is not historized");
		}
	}

	@Override
	public <T> List<T> loadHistory(T object) {
		@SuppressWarnings("unchecked")
		AbstractTable<T> abstractTable = (AbstractTable<T>) persistence.getTable(object.getClass());
		if (abstractTable instanceof HistorizedTable) {
			long id = IdUtils.getId(object);
			List<Integer> times = ((HistorizedTable<T>) abstractTable).readVersions(id);
			List<T> result = new ArrayList<>();
			for (int time : times) {	
				result.add(((HistorizedTable<T>) abstractTable).read(id, time));
			}
			return result;
		} else {
			throw new IllegalArgumentException(object.getClass() + " is not historized");
		}
	}


}
