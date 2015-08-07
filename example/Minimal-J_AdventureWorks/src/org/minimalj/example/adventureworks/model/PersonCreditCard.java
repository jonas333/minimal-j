package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;

public class PersonCreditCard {
	public static final PersonCreditCard $ = Keys.of(PersonCreditCard.class);
	
	public Object id;
	
	@Reference
	public Person person;

	@Reference
	public CreditCard creditCard;

}
