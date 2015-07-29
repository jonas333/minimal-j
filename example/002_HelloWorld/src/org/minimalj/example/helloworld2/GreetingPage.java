package org.minimalj.example.helloworld2;

import java.util.Collections;
import java.util.List;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.FormContent;
import org.minimalj.frontend.Frontend.IContent;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.page.Page;

public class GreetingPage extends Page {

	private final User user;
	
	public GreetingPage(User user) {
		this.user = user;
	}
	
	@Override
	public String getTitle() {
		return "Hello " + user.name;
	}

	@Override
	public IContent getContent() {
		FormContent form = Frontend.getInstance().createFormContent(1, 100);
		form.add(Frontend.getInstance().createLabel("Hello " + user.name));
		return form;
	}
	
	@Override
	public List<Action> getActions() {
		return Collections.singletonList(new UserNameEditor());
	}

}
