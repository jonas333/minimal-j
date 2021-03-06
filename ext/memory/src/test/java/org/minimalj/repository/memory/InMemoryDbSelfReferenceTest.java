package org.minimalj.repository.memory;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore // InMemoryDb could live with self references. But Minimal-J doesn't
		// want it
public class InMemoryDbSelfReferenceTest {

	private static InMemoryRepository repository;
	
	@BeforeClass
	public static void setupRepository() {
		repository = new InMemoryRepository(TestEntity.class);
	}

	@Test
	public void testSelfReferencingEntity() {
		TestEntity e = new TestEntity();
		e.reference = e;
		Object id = repository.insert(e);
		e = repository.read(TestEntity.class, id);
		Assert.assertEquals(e, e.reference);
	}
	
	@Test
	public void testCycleWithOneInserts() {
		TestEntity e1 = new TestEntity();
		TestEntity e2 = new TestEntity();
		e1.reference = e2;
		e2.reference = e1;
		Object id = repository.insert(e1);
		e1 = repository.read(TestEntity.class, id);
		Assert.assertEquals(e1, e1.reference.reference);
	}
	
	@Test
	public void testCycleWithSeparateInserts() {
		TestEntity e1 = new TestEntity();
		e1.id = repository.insert(e1);
		TestEntity e2 = new TestEntity();
		e2.id = repository.insert(e2);
		
		e1.reference = e2;
		repository.update(e1);
		
		e2.reference = e1;
		repository.update(e2);

		e1 = repository.read(TestEntity.class, e1.id);
		Assert.assertEquals(e1, e1.reference.reference);
	}
	
	public static class TestEntity {
		public Object id;
		
		public TestEntity reference;
		
	}
	
}
