package org.minimalj.frontend.impl.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.minimalj.frontend.Frontend.IContent;
import org.minimalj.frontend.action.Item;

public class JsonExplorerContent extends JsonComponent implements IContent {

	public JsonExplorerContent(List<Item> items) {
		super("Explorer");
		put("items", convert(items));
	}

	private List<Map<String, String>> convert(List<Item> items) {
		List<Map<String, String>> converted = new ArrayList<>();
		for (Item item : items) {
			Map<String, String> m = new HashMap<>();
			m.put("name", item.getName());
			m.put("description", item.getDescription());
			m.put("mediaProvider", item.getMediaProviderName());
			m.put("mediaName", item.getMediaName());
			m.put("mimetype", item.getMimeType());
			converted.add(m);
		}
		return converted;
	}
}
