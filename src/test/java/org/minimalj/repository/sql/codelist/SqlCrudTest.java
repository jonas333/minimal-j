package org.minimalj.repository.sql.codelist;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.minimalj.repository.DataSourceFactory;
import org.minimalj.repository.sql.SqlRepository;
import org.minimalj.util.IdUtils;

public class SqlCrudTest {
	
	private static SqlRepository repository;
	
	@BeforeClass
	public static void setupRepository() {
		repository = new SqlRepository(DataSourceFactory.embeddedDataSource(), TestEntity.class);
		TestStringCode t = new TestStringCode("c1", "code1");
		repository.insert(t);
		
		TestStringCode t2 = new TestStringCode("c2", "code2");
		repository.insert(t2);
		
		TestIntegerCode i1 = new TestIntegerCode(1, "code1");
		repository.insert(i1);
	}
	
	private TestEntity insertAndRead() {
		TestEntity entity = new TestEntity("aName");
		entity.list.add(new TestStringCode("c1", "code1"));
		entity.list.add(new TestStringCode("c1", "code1"));
		entity.list.add(new TestStringCode("c2", "code2"));

		entity.listInteger.add(new TestIntegerCode(1, "code2"));
		
		Object id = repository.insert(entity);
		return repository.read(TestEntity.class, id);
	}

	@Test
	public void testInsertAndRead() {
		TestEntity entity = insertAndRead();
		Assert.assertEquals(3, entity.list.size());
		Assert.assertEquals("c1", entity.list.get(0).id);
		Assert.assertEquals("code1", entity.list.get(0).name);
		Assert.assertEquals("c1", entity.list.get(1).id);
		Assert.assertEquals("code1", entity.list.get(1).name);
		Assert.assertEquals("c2", entity.list.get(2).id);
		Assert.assertEquals("code2", entity.list.get(2).name);

		Assert.assertEquals(1, entity.listInteger.size());
	}

	@Test
	public void testAddElement() {
		TestEntity entity = insertAndRead();
		
		entity.list.add(new TestStringCode("c2", "code2"));
		repository.update(entity);
		
		entity = repository.read(TestEntity.class, IdUtils.getId(entity));

		Assert.assertEquals("An additional element with id should be persisted when calling add method", 4, entity.list.size());
		Assert.assertEquals("c1", entity.list.get(0).id);
		Assert.assertEquals("c1", entity.list.get(1).id);
		Assert.assertEquals("c2", entity.list.get(2).id);
		Assert.assertEquals("c2", entity.list.get(3).id);
	}

}