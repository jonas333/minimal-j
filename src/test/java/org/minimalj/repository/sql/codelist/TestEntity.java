package org.minimalj.repository.sql.codelist;

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

	public List<TestStringCode> list = new ArrayList<>();

	public List<TestIntegerCode> listInteger = new ArrayList<>();

}
