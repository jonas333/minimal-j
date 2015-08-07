package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Code;
import org.minimalj.model.annotation.Size;

public class ProductSubcategory implements Code {

	public Integer id;
	
	public ProductCategory productCategory; 
	
	@Size(AdventureWorksFormats.Name)
	public String name;
	
}
