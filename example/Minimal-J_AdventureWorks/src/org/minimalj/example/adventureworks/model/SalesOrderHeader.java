package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class SalesOrderHeader {
	public static final SalesOrderHeader $ = Keys.of(SalesOrderHeader.class);
	
	public Object id;
	
	@Size(3) @Required
	public Integer revisionNumber;

	@Required
	public LocalDate orderDate, dueDate;
	public LocalDate shipDate;
	
	public SalesOrderHeaderStatus status;
	
	@Required
	public Boolean onlineOrder;
	
	@Reference
	public PurchaseOrderHeader purchaseOrder;
	
	public String accountNumber;
	
	@Reference @Required
	public Customer customer;

	@Reference
	public SalesPerson salesPerson;

	@Reference
	public SalesTerritory territory;
	
	@Reference @Required
	public Address billToAddress, shipToAddress;
	
	@Reference @Required
	public ShipMethod shipMethod;
	
	@Reference
	public CreditCard creditCard;
	@Size(15)
	public String creditCardApprovalCode;
	
	@Reference
	public CurrencyRate currencyRate;

	@Required
	public BigDecimal subTotal, taxAmt, freight;

	@Size(128)
	public String comment;
	
	public static enum SalesOrderHeaderStatus implements Code {
		NA, Pending, Approved, Rejected, Complete, Unknown5, Unknown6, Unknown7, Unknown8;
	}
			   
}
