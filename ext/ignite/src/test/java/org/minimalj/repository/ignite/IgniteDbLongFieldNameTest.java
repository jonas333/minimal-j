package org.minimalj.repository.ignite;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.repository.ignite.IgniteRepository;

public class IgniteDbLongFieldNameTest {
	
	private static IgniteRepository repository;
	
	@BeforeClass
	public static void setupRepository() {
		repository = new IgniteRepository(TestEntity.class);
	}
	
	@AfterClass
	public static void shutdownRepository() {
	}
	
	@Test
	public void testInsertAndDelete() {
		TestEntity entity = new TestEntity();
		Object id = repository.insert(entity);
		
		TestEntity l2 = repository.read(TestEntity.class, id);
		Assert.assertNotNull(l2);
		
		repository.delete(l2);
		
		TestEntity l3 = repository.read(TestEntity.class, id);
		Assert.assertNull(l3);
	}

	public static class TestEntity {

		public static final TestEntity $ = Keys.of(TestEntity.class);
		
		public Object id;
		public int version;

		@Size(30)
		public String aVeryLongFieldNameAbcdefghijklmnopqrstuvwyzAbcdefghijklmnopqrstuvwyzAbcdefghijklmnopqrstuvwyzAbcdefghijklmnopqrstuvwyzAbcdefghijklmnopqrstuvwyz;

		@Size(30)
		public String aVeryLongFieldNameAbcdefghijklmnopqrstuvwyzAbcdefghijklmnopqrstuvwyzAbcdefghijklmnopqrstuvwyzAbcdefghijklmnopqrstuvwyzAbcdefghijklmnopqrstuvwyz2;

	}

}
