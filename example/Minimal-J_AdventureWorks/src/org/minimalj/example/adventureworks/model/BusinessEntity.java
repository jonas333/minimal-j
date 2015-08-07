package org.minimalj.example.adventureworks.model;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

import jdk.nashorn.internal.ir.annotations.Reference;

public class BusinessEntity {

	public final List<BusinessEntityContact> contacts = new ArrayList<>();
	
	public final List<BusinessEntityContact> addresses = new ArrayList<>();
	
	public Password password;
	
	public static class BusinessEntityContact {
		
		@Reference
		public Person person;
		
		public ContactType contactType;
	}
	
	public static class BusinessEntityAddress {
		
		@Reference
		public Address address;
		
		public AddressType addressType;
	}

	public static class Password {
		
		@Size(128) @Required
		public String passwordHash;

		@Size(10) @Required
		public String passwordSalt;

	}
}
