package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;

public class Customer {
	public static final Customer $ = Keys.of(Customer.class);
	
	public Object id;
	
	@Reference
	public Person person;

	@Reference
	public Store store;

	@Reference
	public SalesTerritory territory;
	
//	public String getAccountNumber() {
//	    [AccountNumber] AS ISNULL('AW' + [dbo].[ufnLeadingZeros](CustomerID), ''),
//	}
}
