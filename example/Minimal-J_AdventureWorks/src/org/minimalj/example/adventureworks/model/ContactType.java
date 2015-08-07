package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Code;
import org.minimalj.model.annotation.Sizes;

@Sizes(AdventureWorksFormats.class)
public class ContactType implements Code {

	public Integer id;
	
	public String name;
	
}
