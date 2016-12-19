package org.minimalj.example.reservation.model;

import org.minimalj.model.Keys;
import org.minimalj.security.model.User;

public class Reservation {
	public static final Reservation $ = Keys.of(Reservation.class);
	
	public Object id;
	
	public TimeSlot timeSlot;
	
	public User user;
	
}
