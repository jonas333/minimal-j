package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

public class Department {
	public static final Department $ = Keys.of(Department.class);
	
	public Object id;
	
	@Size(AdventureWorksFormats.Name)
	public String name, groupName;
}
