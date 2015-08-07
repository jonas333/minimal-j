package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;
import org.minimalj.model.annotation.Required;

public class SalesPerson {
	public static final SalesPerson $ = Keys.of(SalesPerson.class);
	
	public Object id;
	
	public final BusinessEntity businessEntity = new BusinessEntity();

	@Reference
	public SalesTerritory territory;
	
	public BigDecimal salesQuota;

	@Required
	public BigDecimal bonus, commissionPct, salesYTD, salesLastYear;
}
