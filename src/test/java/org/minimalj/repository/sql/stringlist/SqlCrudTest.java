package org.minimalj.repository.sql.stringlist;

import java.math.BigDecimal;
import java.time.LocalDate;

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
	}
	
	private TestEntity insertAndRead() {
		TestEntity entity = new TestEntity("aName");
		entity.list.add("string1");
		entity.list.add("string1");
		entity.list.add("string2");

		entity.listInteger.add(42);
		entity.listInteger.add(43);

		entity.listLong.add(42L);
		entity.listLong.add(2L^34L);

		entity.listBigDecimal.add(BigDecimal.ONE);

		entity.listDate.add(LocalDate.now().minusDays(100));
		entity.listDate.add(LocalDate.now());

		
		Object id = repository.insert(entity);
		return repository.read(TestEntity.class, id);
	}

	@Test
	public void testInsertAndRead() {
		TestEntity entity = insertAndRead();
		Assert.assertEquals(3, entity.list.size());
		Assert.assertEquals("string1", entity.list.get(0));
		Assert.assertEquals("string1", entity.list.get(1));
		Assert.assertEquals("string2", entity.list.get(2));

		Assert.assertEquals(BigDecimal.class, entity.listBigDecimal.get(0).getClass());

	}

	@Test
	public void testAddElement() {
		TestEntity entity = insertAndRead();
		
		entity.list.add("string3");
		repository.update(entity);
		
		entity = repository.read(TestEntity.class, IdUtils.getId(entity));

		Assert.assertEquals(4, entity.list.size());
		Assert.assertEquals("string1", entity.list.get(0));
		Assert.assertEquals("string1", entity.list.get(1));
		Assert.assertEquals("string2", entity.list.get(2));
		Assert.assertEquals("string3", entity.list.get(3));
	}
	
	@Test
	public void testRemoveElement() {
		TestEntity entity = insertAndRead();
		
		entity.list.remove(1);
		repository.update(entity);
		
		entity = repository.read(TestEntity.class, IdUtils.getId(entity));

		Assert.assertEquals(2, entity.list.size());
		Assert.assertEquals("string1", entity.list.get(0));
		Assert.assertEquals("string2", entity.list.get(1));
	}

}