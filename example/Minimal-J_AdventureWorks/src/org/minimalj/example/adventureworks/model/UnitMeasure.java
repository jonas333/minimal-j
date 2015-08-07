package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class UnitMeasure {
	public static final UnitMeasure $ = Keys.of(UnitMeasure.class);
	
	@Size(3)
	public String id;
	
	@Required @Size(AdventureWorksFormats.Name)
	public String name;

}
