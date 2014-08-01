package org.minimalj.frontend.vaadin.toolkit;

import org.minimalj.frontend.toolkit.ClientToolkit.IContent;
import org.minimalj.frontend.toolkit.ClientToolkit.WizardContent;
import org.minimalj.frontend.toolkit.FormContent;

import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;

public class VaadinSwitchContent extends GridLayout implements WizardContent {
	private static final long serialVersionUID = 1L;

	private IContent showContent;
	
	public VaadinSwitchContent() {
	}
	
	public void requestFocus() {
		if (showContent instanceof Focusable) {
			((Focusable) showContent).focus();
		}
	}

	@Override
	public void show(FormContent c) {
		if (showContent != null) {
			Component component = (Component) showContent;
			removeComponent(component);
		}

		if (c != null) {
			Component component = (Component) c;
			component.setWidth("100%");
			addComponent(component);
			VaadinClientToolkit.focusFirstComponent(component);
		}
		this.showContent = c;
	}

}