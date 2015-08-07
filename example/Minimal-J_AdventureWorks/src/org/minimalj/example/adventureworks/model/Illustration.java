package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class Illustration {
	public static final Illustration $ = Keys.of(Illustration.class);
	
	public Object id;
	
	@Required @Size(4000) // TODO
	public String diagram;
}
