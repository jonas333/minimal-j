package org.minimalj.example.reservation;

import org.minimalj.backend.Backend;
import org.minimalj.example.reservation.model.TimeSlot;
import org.minimalj.example.reservation.page.ReservationTablePage;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.form.Form;

public class AddTimeSlotAction extends Editor.NewObjectEditor<TimeSlot> {

	@Override
	protected Form<TimeSlot> createForm() {
		Form<TimeSlot> form = new Form<TimeSlot>();
		form.line(TimeSlot.$.date);
		form.line(TimeSlot.$.start);
		form.line(TimeSlot.$.end);
		return form;
	}
	
	@Override
	protected TimeSlot save(TimeSlot timeSlot) {
		return Backend.save(timeSlot);
	}
	
	@Override
	protected void finished(TimeSlot result) {
		super.finished(result);
		Frontend.show(new ReservationTablePage());
	}

}
