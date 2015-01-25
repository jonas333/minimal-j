package org.minimalj.frontend.html;

import java.util.List;

import javax.swing.JMenuBar;

import org.minimalj.application.Application;
import org.minimalj.frontend.page.ActionGroup;
import org.minimalj.frontend.page.ObjectPage;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.page.Separator;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.IAction;
import org.minimalj.util.resources.Resources;

public class HtmlMenuBar extends JMenuBar implements IComponent {
	private static final long serialVersionUID = 1L;
	
	private final StringBuilder s = new StringBuilder(1000);
	private final Page page;
	
	public HtmlMenuBar(Page page) {
		this.page = page;
		
		s.append("<ul id=\"nav\">");
		createFileMenu();
		createObjectMenu();
		s.append("</ul>");
	}
	
	private void createFileMenu() {
		startMenu("file");
		
		List<IAction> actionsNew = Application.getApplication().getActionsNew();
		if (!actionsNew.isEmpty()) {
			addActions("new", actionsNew);
			addSeparator();
		}
		List<IAction> actionsImport = Application.getApplication().getActionsImport();
		if (!actionsImport.isEmpty()) addActions("import", actionsImport);
		List<IAction> actionsExport = Application.getApplication().getActionsExport();
		if (!actionsExport.isEmpty()) addActions("export", actionsExport);
		if (!actionsImport.isEmpty() || !actionsExport.isEmpty()) addSeparator();
		
		endMenu();
	}
	
	private void createObjectMenu() {
		if (page instanceof ObjectPage) {
			ActionGroup actionGroup = ((ObjectPage<?>) page).getMenu();
			if (actionGroup != null && actionGroup.getItems() != null) {
				s.append("<ul>");
				s.append(actionGroup.getName());
				addActions(actionGroup.getItems());
				s.append("</ul>");
			}
		}
	}
	
	//
	
	private void startMenu(String resourceName) {
		String text = Resources.getString("Menu." + resourceName);
		s.append("<li><a href=\"a\">");
		s.append(text);
		s.append("</a><ul>");
	}
	
	private void endMenu() {
		s.append("</ul>");
		s.append("</li>");
	}
	
	private void addActions(String type, List<IAction> actions) {
		startMenu(type);
		addActions(actions);
		endMenu();
	}
	
	private void addActions(List<IAction> actions) {
		for (IAction action : actions) {
			if (action instanceof org.minimalj.frontend.page.ActionGroup) {
				org.minimalj.frontend.page.ActionGroup actionGroup = (org.minimalj.frontend.page.ActionGroup) action;
				s.append("<li><a href=\"a\">");
				s.append(action.getName());
				s.append("</a><ul>");
				addActions(actionGroup.getItems());
				s.append("</ul>");
				s.append("</li>");
			} else if (action instanceof Separator) {
				addSeparator();
			} else {
				s.append("<li><a href=\"a\">");
				s.append(action.getName());
				s.append("</a></li>");
			}
		}
	}

	protected void addSeparator() {
//		s.append("<hr>");
	}
	
	public String toHtml() {
		return s.toString();
	}
}	
