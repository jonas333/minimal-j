package org.minimalj.repository.sql.codelist;

import org.minimalj.model.Code;
import org.minimalj.model.annotation.Size;

public class TestStringCode implements Code {

	public TestStringCode() {
		// needed for reflection constructor
	}

	public TestStringCode(String code, String name) {
		this.id = code;
		this.name = name;
	}

	@Size(5)
	public String id;

	@Size(30)
	public String name;
}
