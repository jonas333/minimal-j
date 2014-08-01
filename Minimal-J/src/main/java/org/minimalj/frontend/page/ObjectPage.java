package org.minimalj.frontend.page;

import org.minimalj.frontend.edit.form.Form;
import org.minimalj.frontend.toolkit.ClientToolkit.IContent;

public abstract class ObjectPage<T> extends AbstractPage {

	private Form<T> form;
	private T object;
	
	public ObjectPage() {
	}

	public ActionGroup getMenu() {
		return null;
	}

	protected abstract Form<T> createForm();

	protected abstract T loadObject();
	
	protected T getObject() {
		return object;
	}
	
	@Override
	public IContent getContent() {
		if (form == null) {
			form = createForm();
			refresh();
		}
		return form.getContent();
	}
	
	@Override
	public void refresh() {
		object = loadObject();
		form.setObject(object);
	}
	
}