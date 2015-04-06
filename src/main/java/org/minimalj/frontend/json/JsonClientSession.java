package org.minimalj.frontend.json;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.minimalj.application.Application;
import org.minimalj.application.ApplicationContext;
import org.minimalj.frontend.json.JsonClientToolkit.JsonActionLabel;
import org.minimalj.frontend.json.JsonComponent.JsonComponentListener;
import org.minimalj.frontend.page.Page;

public class JsonClientSession {

	private static final Map<String, JsonClientSession> sessions = new HashMap<>();
	private final ApplicationContext applicatonContext;
	private Page visiblePage;
	private Map<String, JsonComponent> componentById = new HashMap<>();
	private Map<String, Page> pageById = new HashMap<>();
	private final JsonOutputComponentListener listener = new JsonOutputComponentListener();
	private JsonOutput output;

	public JsonClientSession(ApplicationContext context) {
		this.applicatonContext = context;
		
	}
	
	public static JsonClientSession getSession(String sessionId) {
		return sessions.get(sessionId);
	}
	
	public static String createSession() {
		String sessionId = UUID.randomUUID().toString();
		JsonClientSession session = new JsonClientSession(null);
		sessions.put(sessionId, session);
		return sessionId;
	}
	
	public void setPage(Page page) {
		JsonComponent content = (JsonComponent) page.getContent();
		registerComponent(content);
	}

	private void registerComponent(JsonComponent component) {
		component.setListener(listener);
		componentById.put(component.getId(), component);
		for (JsonComponent child : component.getComponents()) {
			registerComponent(child);
		}
	}
	
	private class JsonOutputComponentListener implements JsonComponentListener {
		@Override
		public void changed(String id, String property, Object value) {
			output.propertyChange(id, property, value);
		}
	}

	public JsonOutput handle(JsonInput input) {
		output = new JsonOutput();

		if (input.containsObject(JsonInput.SHOW_PAGE)) {
			String pageId = (String) input.getObject(JsonInput.SHOW_PAGE);
			Page page;
			if (pageId != null) {
				page = pageById.get(pageId);
			} else {
				page = Application.getApplication().createDefaultPage();
			}
			JsonComponent content = (JsonComponent) page.getContent();
			output.add("content", content.getValues());
		}
		
		Map<String, Object> changedValue = input.get(JsonInput.CHANGED_VALUE);
		for (Map.Entry<String, Object> entry : changedValue.entrySet()) {
			String componentId = entry.getKey();
			String newValue = (String) entry.getValue();
			
			JsonComponent component = componentById.get(componentId);
			((JsonValueComponent) component).setValue((String) newValue); 
		}
		
		String actionId = (String) input.getObject(JsonInput.ACTIVATED_ACTION);
		if (actionId != null) {
			JsonComponent component = componentById.get(actionId);
			if (component instanceof JsonActionLabel) {
				JsonActionLabel actionLabel = (JsonActionLabel) component;
				actionLabel.action();
			}
		}
		
		return output;
	}
}
