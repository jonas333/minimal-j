package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Code;
import org.minimalj.model.annotation.Size;

public class ProductCategory implements Code {

	public Integer id;
	
	@Size(AdventureWorksFormats.Name)
	public String name;
	
}
