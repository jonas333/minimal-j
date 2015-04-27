package org.minimalj.backend.db;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.minimalj.model.ViewUtil;
import org.minimalj.transaction.criteria.Criteria;

public class DbCriteriaTest {
	
	private static DbPersistence persistence;
	
	@BeforeClass
	public static void setupDb() throws SQLException {
		persistence = new DbPersistence(DbPersistence.embeddedDataSource(), A.class, G.class, H.class, L.class, M.class);
	}
	
	@AfterClass
	public static void shutdownDb() throws SQLException {
	}
	
	@Test // if fields of class reference are correctly written and read
	public void testReferenceField() throws SQLException {
		G g = new G("g1");
		Object id = persistence.insert(g);
		g = persistence.read(G.class, id);
		
		H h = new H();
		h.g = g;
		Object idH = persistence.insert(h);

		h = persistence.read(H.class, idH);
		
		Assert.assertEquals(id, h.g.id);
	}

	@Test // if read by a reference works correctly
	public void testSimpleCriteria() throws SQLException {
		G g = new G("g2");
		Object id = persistence.insert(g);
		g = persistence.read(G.class, id);
		
		H h = new H();
		h.g = g;
		persistence.insert(h);

		h = new H();
		h.g = g;
		persistence.insert(h);
		
		List<H> hList = persistence.getTable(H.class).read(new Criteria.SimpleCriteria(H.$.g, g), 3);
		Assert.assertEquals("Read by reference", 2, hList.size());

		hList = persistence.getTable(H.class).read(new Criteria.SimpleCriteria(H.$.g.id, g.id), 3);
		Assert.assertEquals("Read by references id", 2, hList.size());

		hList = persistence.getTable(H.class).read(new Criteria.SimpleCriteria(H.$.g.g, "g2"), 3);
		Assert.assertEquals("Read by references field", 2, hList.size());
	}

	@Test // if read by a foreign key works correctly if the reference is in a dependable
	public void testForeignKeyCriteriaInDependable() throws SQLException {
		G g = new G("g3");
		Object id = persistence.insert(g);
		g = persistence.read(G.class, id);
		
		H h = new H();
		h.i.rG = g;
		persistence.insert(h);

		h = new H();
		h.i.rG = g;
		persistence.insert(h);
		
		List<H> hList = persistence.getTable(H.class).read(new Criteria.SimpleCriteria(H.$.i.rG, g), 3);
		
		Assert.assertEquals(2, hList.size());
	}
	
	@Test // if list can contain Views of Model classes
	public void testViewAsListContent() throws SQLException {
		L l = new L();
		
		M m1 = new M("text1_1", "text1_2");
		persistence.insert(m1);
		MView mview1 = new MView();
		ViewUtil.view(m1, mview1);
		
		M m2 = new M("text2_1", "text2_2");
		persistence.insert(m2);
		MView mview2 = new MView();
		ViewUtil.view(m2, mview2);
		
		l.mviews.add(mview1);
		l.mviews.add(mview2);
		
		
	}
	
}
