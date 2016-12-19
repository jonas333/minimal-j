package org.minimalj.example.reservation.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;

public class Room {
	public static final Room $ = Keys.of(Room.class);
	
	public Object id;

	@Size(255)
	public String name;
	
}
