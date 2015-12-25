package org.minimalj.backend.db.container;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.minimalj.backend.sql.SqlPersistence;

public class SqlContainerCrudTest {

private static SqlPersistence persistence;
	
	@BeforeClass
	public static void setupPersistence() {
		persistence = new SqlPersistence(SqlPersistence.embeddedDataSource(), A.class);
	}
	
	@AfterClass
	public static void shutdownPersistence() {
	}
	
	@Test
	public void testInsertAndDelete() {
		A a = new A("A");
		
		B b = new B("B");
		a.b.add(b);
		
		C c = new C("C");
		b.c.add(c);
		
		Object id = persistence.insert(a);
		
		A a2 = persistence.read(A.class, id);
		Assert.assertNotNull(a2);
		Assert.assertEquals(1, a2.b.size());
		Assert.assertEquals(1, a2.b.get(0).c.size());
		
	}
}
