package org.minimalj.example.library.model;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.View;

public class CustomerView implements View<Customer>, Rendering {
	public static final CustomerView $ = Keys.of(CustomerView.class);

	public Object id;
	public String firstName, name;

	@Override
	public String render(RenderType renderType) {
		return firstName + " " + name;
	}
}
