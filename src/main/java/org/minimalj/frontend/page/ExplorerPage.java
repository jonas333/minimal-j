package org.minimalj.frontend.page;

import java.util.List;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IContent;
import org.minimalj.frontend.action.Item;

public class ExplorerPage extends Page {

	private final List<Item> items;
	private final Frontend.EXPLORER_DISPLAY display;
	
	public ExplorerPage(List<Item> items) {
		this(items, Frontend.EXPLORER_DISPLAY.TILES);
	}

	public ExplorerPage(List<Item> items, Frontend.EXPLORER_DISPLAY display) {
		this.items = items;
		this.display = display;
	}

	@Override
	public IContent getContent() {
		return Frontend.getInstance().createExplorerContent(items, display);
	}

}
