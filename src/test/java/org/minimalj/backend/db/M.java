package org.minimalj.backend.db;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

public class M {

	public static final M $ = Keys.of(M.class);
	
	public M() {
		// needed for reflection constructor
	}
	
	public M(String text, String text2) {
		this.text = text;
		this.text2 = text2;
	}

	public Object id;

	@Size(32)
	public String text;

	@Size(32)
	public String text2;

}

