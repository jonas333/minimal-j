package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class SalesOrderDetail {
	public static final SalesOrderDetail $ = Keys.of(SalesOrderDetail.class);
	
	public Object id;
	
	@Reference
	public SalesOrderHeader salesOrder;
	
	@Size(25)
	public String carrierTrackingNumber;

	@Size(5)
	public Integer orderQty;
	
	@Reference @Required
	public Product product;
	
	@Reference @Required
	public SpecialOffer specialOffer;
	
	@Required
	public BigDecimal unitPrice, unitPriceDiscount;

}
