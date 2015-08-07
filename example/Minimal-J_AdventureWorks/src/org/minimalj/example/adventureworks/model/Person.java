package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Keys;

public class Person {
	public static final Person $ = Keys.of(Person.class);
	
	public Object id;
	
	public final BusinessEntity businessEntity = new BusinessEntity();
	
	public String title;
	public String firstName, middleName, lastName;
	public String suffix;

	public static class PersonPhone {
		public static final PersonPhone $ = Keys.of(PersonPhone.class);
		
		public String phoneNumber;
		public PhoneNumberType type;
	}	
	
	public static class EmailAddress {
		public static final EmailAddress $ = Keys.of(EmailAddress.class);
		
	}
}
