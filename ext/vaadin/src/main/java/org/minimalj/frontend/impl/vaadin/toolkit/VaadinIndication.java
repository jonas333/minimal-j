package org.minimalj.frontend.impl.vaadin.toolkit;

import java.util.List;

import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;

public class VaadinIndication {

	public static void setValidationMessages(List<String> validationMessages, AbstractComponent component) {
		if (validationMessages.isEmpty()) {
			component.setComponentError(null);
		} else if (validationMessages.size() == 1) {
			component.setComponentError(new UserError(validationMessages.get(0)));
		} else {
			ErrorMessage[] errorMessages = new ErrorMessage[validationMessages.size()];
			for (int i = 0; i<validationMessages.size(); i++) {
				errorMessages[i] = new UserError(validationMessages.get(i));
			}
			CompositeErrorMessage compositeErrorMessage = new CompositeErrorMessage(errorMessages);
			component.setComponentError(compositeErrorMessage);
		}
	}

}
