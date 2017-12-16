package org.minimalj.repository.sql.codelist;

import org.minimalj.model.Code;
import org.minimalj.model.annotation.Size;

public class TestIntegerCode implements Code {

	public TestIntegerCode() {
		// needed for reflection constructor
	}

	public TestIntegerCode(Integer code, String name) {
		this.id = code;
		this.name = name;
	}

	public Integer id;

	@Size(30)
	public String name;
}
