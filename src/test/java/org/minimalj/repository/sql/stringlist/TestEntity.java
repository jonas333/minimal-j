package org.minimalj.repository.sql.stringlist;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

public class TestEntity {

	public static final TestEntity $ = Keys.of(TestEntity.class);

	public TestEntity() {
		// needed for reflection constructor
	}

	public TestEntity(String name) {
		this.name = name;
	}

	public Object id;

	@Size(30)
	public String name;

	@Size(20)
	public List<String> list = new ArrayList<>();
	
	public List<Integer> listInteger = new ArrayList<>();
	public List<Long> listLong = new ArrayList<>();
	public List<BigDecimal> listBigDecimal = new ArrayList<>();
	public List<LocalDate> listDate = new ArrayList<>();
	

}
