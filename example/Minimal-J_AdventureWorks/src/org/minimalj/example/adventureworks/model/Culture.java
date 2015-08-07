package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Code;
import org.minimalj.model.annotation.Sizes;

@Sizes(AdventureWorksFormats.class)
public class Culture implements Code {

	public String id;
	
	public String name;
	
}
