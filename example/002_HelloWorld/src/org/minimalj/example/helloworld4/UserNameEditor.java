package org.minimalj.example.helloworld4;

import org.minimalj.frontend.editor.Editor.SimpleEditor;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.TextFormElement;

public class UserNameEditor extends SimpleEditor<User> {

	public UserNameEditor() {
		super("Greet");
	}

	@Override
	protected User createObject() {
		return GreetingApplication.user;
	}
	
	@Override
	protected Form<User> createForm() {
		Form<User> form = new Form<>();
		form.line(User.$.name);
		form.line(new TextFormElement(User.$.name));
		form.addDependecy(User.$.name, User.$.name);
		return form;
	}

	@Override
	protected User save(User user) {
		return user;
	}

}
