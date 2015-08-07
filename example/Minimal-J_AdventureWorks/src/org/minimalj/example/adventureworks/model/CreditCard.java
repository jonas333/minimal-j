package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

public class CreditCard {
	public static final CreditCard $ = Keys.of(CreditCard.class);
	
	public Object id;
	
	@Size(50)
	public String cardType;
	@Size(25)
	public String cardNumber;

	@Size(2)
	public Integer expMonth;
	
	@Size(4)
	public Integer expYear;
	
}
