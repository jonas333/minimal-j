package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class SalesTerritory {
	public static final SalesTerritory $ = Keys.of(SalesTerritory.class);
	
	public Object id;

	@Required @Size(AdventureWorksFormats.Name);
	public String name;
	
	@Required
	public CountryRegion countryRegion;
	
	@Size(50)
	public String group;
	
	@Required
	public BigDecimal salesYTD, salesLastYear, costYTD, costLastYear;
}
