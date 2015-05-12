package org.minimalj.frontend.lanterna.toolkit;

import org.minimalj.frontend.toolkit.Action;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.IList;

import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.layout.VerticalLayout;

public class LanternaList extends Panel implements IList {
	
	private final int actionCount;
	
	public LanternaList(Action... actions) {
		setLayoutManager(new VerticalLayout());
		if (actions != null) {
			for (Action action : actions) {
				addComponent((Component) ClientToolkit.getToolkit().createLabel(action));
			}
			actionCount = actions.length;
		} else {
			actionCount = 0;
		}
	}
	
	@Override
	public void clear() {
		for (int i = getComponentCount() - actionCount - 1; i>=0; i--) {
			removeComponent(getComponentAt(i));
		}
	}

	@Override
	public void setEnabled(boolean enabled) {	
		// TODO
		
	}

	@Override
	public void add(Object object, Action... actions) {
		Component label = object instanceof Action ? (Component) ClientToolkit.getToolkit().createLabel((Action) object) : new LanternaLabel(object);
		super.addComponent(label);
		
		// TODO actions
	}

}