package org.minimalj.backend.db.viewlist;

import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;
import org.minimalj.backend.sql.SqlPersistence;

import junit.framework.Assert;

public class SqlLazyListTest {
	
	private static SqlPersistence persistence;
	
	@BeforeClass
	public static void setupPersistence() {
		persistence = new SqlPersistence(SqlPersistence.embeddedDataSource(), A.class);
	}
	
	@Test 
	public void testInsertWithNewReferencedObject() {
		A a = new A("A1");
		
		B b = new B("B1");
		a.b = Collections.singletonList(b);
		
		C c = new C("C1");
		b.c = Collections.singletonList(c);
		
		Object aId = persistence.insert(a);
		
		A a2 = persistence.read(A.class, aId);
		Assert.assertEquals("B1", a2.b.get(0).bName);
		
		// lists of c is not loaded
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInsertWithExistingReferencedObject() {
		B b = new B("B1");
		Object bId = persistence.insert(b); // will throw exception as B is not a main entity
		b = persistence.read(B.class, bId);
		
		A a = new A("A1");
		a.b = Collections.singletonList(b);
		
		Object aId = persistence.insert(a);
		
		A a2 = persistence.read(A.class, aId);
		Assert.assertEquals("B1", a2.b.get(0).bName);
	}
	
	@Test
	public void testListInElement() {
		A a = new A("A1");
		
		B b = new B("B1");
		a.b = Collections.singletonList(b);
		
		C c = new C("C1");
		b.c = Collections.singletonList(c);
		
		Object aId = persistence.insert(a);
		
		A a2 = persistence.read(A.class, aId);
		
		Assert.assertEquals(1, a2.b.size());
		Assert.assertEquals(a2.b.get(0).c.size(), 1);
	}

}
