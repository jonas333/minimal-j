package org.minimalj.example.adventureworks.model;

import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;
import org.minimalj.model.annotation.Required;

public class EmployeeDepartmentHistory {
	public static final EmployeeDepartmentHistory $ = Keys.of(EmployeeDepartmentHistory.class);
	
	public Object id;
	
	@Reference @Required
	public Employee employee;
	
	@Reference @Required
	public Shift shift;
	
	@Required
	public LocalDate startDate;
	public LocalDate endDate;
}
