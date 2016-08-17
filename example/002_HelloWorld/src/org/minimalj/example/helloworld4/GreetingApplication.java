package org.minimalj.example.helloworld4;

import java.util.Collections;
import java.util.List;

import org.minimalj.application.Application;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.impl.nanoserver.NanoWebServer;

public class GreetingApplication extends Application {

	static User user = new User();
	
	@Override
	public List<Action> getNavigation() {
		return Collections.singletonList(new UserNameEditor());
	}
	
	public static void main(String[] args) {
		NanoWebServer.main(GreetingApplication.class.getName());
	}
	
}
