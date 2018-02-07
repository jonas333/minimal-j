package org.minimalj.frontend.impl.cheerpj;

import org.minimalj.application.Application;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.impl.json.JsonFrontend;
import org.minimalj.frontend.impl.json.JsonSessionManager;
import org.minimalj.model.test.ModelTest;
import org.minimalj.util.StringUtils;

public class Cheerpj {

	private static JsonSessionManager sessionManager = new JsonSessionManager();
	
	public static native void sendMessage(String json);
	
	public static void receiveMessage(String json) {
		String result = sessionManager.handle(json);
		if (StringUtils.isEmpty(result)) {
			sendMessage(json);
		}
	}
	
	public static void start(Application application) {
		Application.setInstance(application);
		
		ModelTest.exitIfProblems();
		Frontend.setInstance(new JsonFrontend());
	}
}
