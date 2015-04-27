package org.minimalj.backend.db;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;

public class L {


	public static final L $ = Keys.of(L.class);
	
	public L() {
		// needed for reflection constructor
	}

	public Object id;
	
	public final List<MView> mviews = new ArrayList<>();
}

