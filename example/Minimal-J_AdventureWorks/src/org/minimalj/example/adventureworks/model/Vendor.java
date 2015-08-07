package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class Vendor {
	public static final Vendor $ = Keys.of(Vendor.class);
	
	public Object id;
	
	public final BusinessEntity businessEntity = new BusinessEntity();
	
	@Required @Size(AdventureWorksFormats.Name)
	public String name;

	public String demographics;

}
