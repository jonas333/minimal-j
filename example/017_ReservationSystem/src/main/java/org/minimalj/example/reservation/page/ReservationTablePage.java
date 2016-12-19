package org.minimalj.example.reservation.page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.minimalj.backend.Backend;
import org.minimalj.example.reservation.model.Reservation;
import org.minimalj.example.reservation.model.TimeSlot;
import org.minimalj.frontend.page.TablePage;
import org.minimalj.persistence.criteria.By;

public class ReservationTablePage extends TablePage<Reservation> {

	public ReservationTablePage() {
		super(new Object[]{ Reservation.$.timeSlot.start, Reservation.$.timeSlot.end, Reservation.$.user.name });
	}
	
	@Override
	protected List<Reservation> load() {
		List<TimeSlot> timeSlots = Backend.read(TimeSlot.class, By.ALL, Integer.MAX_VALUE);
		Collections.sort(timeSlots);
		
		List<Reservation> reservations = new ArrayList<>();
		for (TimeSlot timeSlot : timeSlots) {
			List<Reservation> reservation = Backend.read(Reservation.class, By.field(Reservation.$.timeSlot, timeSlot), 1);
			if (reservation.isEmpty()) {
				Reservation placeHolder = new Reservation();
				placeHolder.timeSlot = timeSlot;
				reservations.add(placeHolder);
			} else {
				reservations.add(reservation.get(0));
			}
			
		}
		
		return reservations;
		
//		List<Reservation> reservations = timeSlots.stream().map(timeSlot -> new Reservation()).collect(Collectors.toList());
		// List<Reservation> reservations = Backend.read(Reservation.class, By.ALL, Integer.MAX_VALUE);
	}

}
