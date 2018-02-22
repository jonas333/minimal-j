package org.minimalj.frontend.impl.cheerpj;

import org.minimalj.application.Application;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.impl.json.JsonFrontend;
import org.minimalj.frontend.impl.json.JsonSessionManager;
import org.minimalj.model.test.ModelTest;

public class Cheerpj {

	private static JsonSessionManager sessionManager = new JsonSessionManager();
	
	public static String receiveMessage(String json) {
		String result = sessionManager.handle(json);
		return result;
	}
	
	public static void start(Application application) {
		Application.setInstance(application);
		
		ModelTest.exitIfProblems();
		Frontend.setInstance(new JsonFrontend());
	}
}
