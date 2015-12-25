package org.minimalj.example.library.model;

import org.minimalj.model.Keys;
import org.minimalj.model.Rendering;
import org.minimalj.model.View;


public class BookView implements View<Book>, Rendering {
	public static final BookView $ = Keys.of(BookView.class);

	public Object id;
	public String title;
	public String author;

	@Override
	public String render(RenderType renderType) {
		return author + ": " + title;
	}
}
