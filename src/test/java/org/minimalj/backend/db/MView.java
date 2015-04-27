package org.minimalj.backend.db;

import org.minimalj.model.View;
import org.minimalj.model.annotation.Size;

public class MView implements View<M> {


	public MView() {
		// needed for reflection constructor
	}

	public Object id;

	@Size(32)
	public String text;
	
}

