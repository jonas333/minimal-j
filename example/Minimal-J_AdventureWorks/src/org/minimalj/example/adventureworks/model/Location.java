package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

public class Location {
	public static final Location $ = Keys.of(Location.class);
	
	public Object id;
	
	@Size(AdventureWorksFormats.Name)
	public String name;
	
	public BigDecimal costRate;
	
	public BigDecimal availability;
}
