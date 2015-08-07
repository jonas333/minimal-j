package org.minimalj.example.adventureworks.model;

import java.time.LocalTime;

import org.minimalj.model.Keys;

public class Shift {
	public static final Shift $ = Keys.of(Shift.class);
	
	public Object id;
	
	public String name;
	public LocalTime startTime, endTime;
}
