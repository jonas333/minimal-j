package org.minimalj.backend.db.container;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.annotation.Size;


public class B {

	public B() {
		// needed for reflection constructor
	}
	
	public B(String bName) {
		this.bName = bName;
	}
	
	@Size(30)
	public String bName;
	
	public final List<C> c = new ArrayList<>();
}
