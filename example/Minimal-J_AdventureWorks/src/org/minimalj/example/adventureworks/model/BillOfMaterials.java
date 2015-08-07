package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;
import org.minimalj.model.annotation.Required;

public class BillOfMaterials {
	public static final BillOfMaterials $ = Keys.of(BillOfMaterials.class);
	
	public Object id;
	
	@Reference
	public ProductAssembly productAssembly;

	@Reference @Required
	public Component component;

	@Required
	public LocalDate startDate;
	public LocalDate endDate;
	
	@Required
	public UnitMeasure unitMeasure;
	
	@Required
	public Integer BOMLevel;
	
	@Required
	public BigDecimal perAssemblyQty;
}
