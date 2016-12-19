package org.minimalj.example.reservation.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.annotation.Size;

public class TimeSlot implements Comparable<TimeSlot> {
	public static final TimeSlot $ = Keys.of(TimeSlot.class);
	
	public Object id;
	
	@NotEmpty
	public LocalDate date;
	
	@Size(Size.TIME_HH_MM) @NotEmpty
	public LocalTime start, end;
	
	public int compareTo(TimeSlot o) {
		int result = date.compareTo(o.date);
		if (result != 0) {
			return result;
		} else {
			return start.compareTo(o.start);
		}
	}
}
