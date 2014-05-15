package ch.openech.mj.db;

import ch.openech.mj.application.MjApplication;

/**
 * TODO:
 * 
 * - je nach entityClass sollte eine unterschiedliche persistence zurückgegeben werden
 * - pro unterschiedliche persistence sollten die properties beachtet werden
 * - zu DbPersistence sollte ein interface gemacht werden
 * 
 * @author bruno
 *
 */
public class Persistence {

	private static DbPersistence persistence;
	
	public static DbPersistence get(Class<?> entityClass) {
		if (persistence == null) {
			persistence = new DbPersistence(DbPersistence.embeddedDataSource(), MjApplication.getApplication().getEntityClasses());
		}
		return persistence;
	}
	
}
