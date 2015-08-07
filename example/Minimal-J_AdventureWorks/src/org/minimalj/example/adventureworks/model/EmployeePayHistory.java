package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class EmployeePayHistory {
	public static final EmployeePayHistory $ = Keys.of(EmployeePayHistory.class);
	
	public Object id;
	
	@Reference @Required
	public Employee employee;
	
	@Required
	public LocalDate rateChangeDate;

	@Required
	public BigDecimal rate;
	
	@Size(3)
	public Integer payFrequency;
}
