package org.minimalj.example.adventureworks.model;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class Employee {
	public static final Employee $ = Keys.of(Employee.class);
	
	public Object id;
	
	public Person person;
	
	@Size(256) @Required
	public String login;
	
	public Employee lineManager;
	
	@Size(50) @Required
	public String jobTitle;
	
	@Required
	public LocalDate birthDate;
	
	@Size(1) @Required
	public String maritalStatus, gender;

	@Required
	public LocalDate hireDate;
	
	public Boolean salaried;
	
	@Size(5) @Required
	public Integer vacationHours, sickLeaveHours;
	
	public Boolean current;
}
