package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class PurchaseOrderHeader {
	public static final PurchaseOrderHeader $ = Keys.of(PurchaseOrderHeader.class);
	
	public Object id;
	
	@Size(3) @Required
	public Integer revisionNumber;

	public PurchaseOrderHeaderStatus status;
	
	@Reference @Required
	public Employee employee;
	
	@Reference @Required
	public Vendor vendor;
	
	@Reference @Required
	public ShipMethod shipMethod;

	@Required
	public LocalDate orderDate;
	public LocalDate shipDate;
	
	public BigDecimal subTotal;
	public BigDecimal taxAmt;
	public BigDecimal freight;
	
	public static enum PurchaseOrderHeaderStatus implements Code {
		NA, Pending, Approved, Rejected, Complete;
	}
			   
}
