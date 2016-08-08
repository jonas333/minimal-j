package org.minimalj.example.currencies.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.editor.ObjectValidator;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.page.IDialog;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.ChangeListener;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.ExceptionUtils;
import org.minimalj.util.GenericUtils;
import org.minimalj.util.resources.Resources;

public abstract class Calculator<T> extends Action {
	private static final Logger logger = Logger.getLogger(Calculator.class.getName());

	private T object;
	private Form<T> form;
	private final List<ValidationMessage> validationMessages = new ArrayList<>();
	private CalculateAction calculateAction;
	private IDialog dialog;
	
	public Calculator() {
		super();
	}

	public Calculator(String actionName) {
		super(actionName);
	}
	
	@Override
	protected Object[] getNameArguments() {
		Class<?> editedClass = GenericUtils.getGenericClass(getClass());
		if (editedClass != null) {
			String resourceName = Resources.getResourceName(editedClass);
			return new Object[]{Resources.getString(resourceName)};
		} else {
			return null;
		}
	}

	public String getTitle() {
		return getName();
	}

	@Override
	public void action() {
		object = createObject();
		form = createForm();
		
		calculateAction = new CalculateAction();
		
		validate();

		form.setChangeListener(new EditorChangeListener());
		form.setObject(object);
		
		dialog = Frontend.showDialog(getTitle(), form.getContent(), calculateAction, new CancelAction(), createActions());
	}
	
	private Action[] createActions() {
		Action[] actions = new Action[2];
		int index = 0;
		actions[index++] = new CancelAction();
		actions[index++] = calculateAction;
		return actions;
	}
	
	protected T createObject() {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) GenericUtils.getGenericClass(getClass());
		T newInstance = CloneHelper.newInstance(clazz);
		return newInstance;
	}
	
	protected T getObject() {
		return object;
	}
	
	protected abstract Form<T> createForm();
	
	private void validate() {
		validationMessages.clear();
		if (object instanceof Validation) {
			((Validation) object).validate(validationMessages);
		}
		ObjectValidator.validate(object, validationMessages, form.getProperties());
		validate(object, validationMessages);
		form.indicate(validationMessages);
		calculateAction.setValidationMessages(validationMessages);
	}
	
	protected void validate(T object, List<ValidationMessage> validationMessages) {
		// 
	}
	
	private void save() {
		try {
			calculate(object);
			form.setObject(object);
		} catch (Exception x) {
			ExceptionUtils.logReducedStackTrace(logger, x);
			Frontend.showError(x.getLocalizedMessage());
			return;
		}
	}

	protected abstract void calculate(T object);
	
	private class EditorChangeListener implements ChangeListener<Form<?>> {

		@Override
		public void changed(Form<?> form) {
			validate();
		}
	}	
	
	protected final class CalculateAction extends Action {
		private String description;
		private boolean valid = false;

		@Override
		public void action() {
			save();
		}
		
		public void setValidationMessages(List<ValidationMessage> validationMessages) {
			valid = validationMessages == null || validationMessages.isEmpty();
			description = ValidationMessage.formatHtml(validationMessages);
			fireChange();
		}

		@Override
		public boolean isEnabled() {
			return valid;
		}
		
		@Override
		public String getDescription() {
			return description != null ? description : super.getDescription();
		}
	}
	
	private class CancelAction extends Action {
		@Override
		public void action() {
			cancel();
		}
	}
	
	public void cancel() {
		dialog.closeDialog();
	}

}
