package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

public class Password {
	public static final Password $ = Keys.of(Password.class);
	
	public Object id;
	
	@Size(AdventureWorksFormats.Name)
	public String name;
	
	public BigDecimal costRate;
	
	public BigDecimal availability;
}
