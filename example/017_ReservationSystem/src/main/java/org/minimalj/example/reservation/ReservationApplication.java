package org.minimalj.example.reservation;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.application.Application;
import org.minimalj.example.reservation.model.Reservation;
import org.minimalj.example.reservation.page.ReservationTablePage;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.page.Page;

public class ReservationApplication extends Application {

	@Override
	public Class<?>[] getEntityClasses() {
		return new Class<?>[]{ Reservation.class };
	}
	
	@Override
	public Page createDefaultPage() {
		return new ReservationTablePage();
	}
	
	@Override
	public List<Action> getNavigation() {
		List<Action> actions = new ArrayList<Action>();
		actions.add(new AddTimeSlotAction());
		return actions;
	}
	
	public static void main(String[] args) {
		Application.main(args);
	}
	
}
