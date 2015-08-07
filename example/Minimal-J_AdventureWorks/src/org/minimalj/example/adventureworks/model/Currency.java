package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Code;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.annotation.Sizes;

@Sizes(AdventureWorksFormats.class)
public class Currency implements Code {

	@Size(3)
	public String iso;
	
	public String name;
	
}
