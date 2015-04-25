package org.minimalj.frontend.editor;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.minimalj.application.DevMode;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.toolkit.Action;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IContent;
import org.minimalj.model.validation.ValidationMessage;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.GenericUtils;
import org.minimalj.util.mock.Mocking;
import org.minimalj.util.resources.Resources;

/**
 * An <code>Editor</code> knows
 * <UL>
 * <LI>How to build the FormPanel containing FormField s
 * <LI>How to load an Object
 * <LI>How to validate the object
 * <LI>How to save the object
 * <LI>What additional Actions the Editor provides
 * <LI>Its name (for Window Title oder Tab Title)
 * </UL>
 * @author Bruno
 * 
 * @param <T>
 *            Class of the edited Object
 */
public abstract class Editor<T> {

	private static final Logger logger = Logger.getLogger(Editor.class.getName());
	protected static final Object SAVE_SUCCESSFUL = new Object();
	protected static final Object SAVE_FAILED = null;
	
	private T original, editedObject;
	private Form<T> form;
	protected SaveAction saveAction;
	protected CancelAction cancelAction;
	protected FillWithDemoDataAction demoAction;
	private EditorListener editorListener;
	private boolean userEdited;
	
	// what to implement

	protected abstract Form<T> createForm();

	/**
	 * Should load the object to be edited. Note: The object will be copied before
	 * changed by the editor
	 * 
	 * @return null if newInstance() should be used
	 */
	protected T load() {
		return null;
	}

	protected abstract Object save(T object) throws Exception;

	public String getTitle() {
		// specific name of editor
		if (Resources.isAvailable(getClass().getName())) {
			return Resources.getString(getClass().getName());
		} 

		// specific name of edited class
		Class<?> clazz = GenericUtils.getGenericClass(getClass());
		if (clazz != null && Resources.isAvailable(clazz.getName())) {
			return Resources.getString(getClass().getName());
		}
		
		// simple name of editor
		if (clazz == null || Resources.isAvailable(getClass().getSimpleName())) {
			return Resources.getString(getClass().getSimpleName());
		}
		
		// simple name of edited class or default
		return Resources.getString(clazz);
	}

	public Action[] getActions() {
		if (DevMode.isActive()) {
			return new Action[] { demoAction, cancelAction, saveAction };
		} else {
			return new Action[] { cancelAction, saveAction };
		}
	}
	
	public boolean isUserEdited() {
		return userEdited;
	}
	
	// /////

	protected Editor() {
	}
	
	public void startEditor() {
		if (!isFinished()) {
			throw new IllegalStateException("Editor already started");
		}
		if (editorListener == null) {
			throw new IllegalStateException("EditorListener must be set before start Editor");
		}
		
		initActions();
		
		original = load();
		editedObject = createEditedObject(original);
		
		form = createForm();
		if (form != null) {
			form.setChangeListener(new EditorChangeListener());
			form.setObject(editedObject);
		}

		userEdited = false;
	}
	
	private void initActions() {
		saveAction = new SaveAction();
		cancelAction = new CancelAction();
		demoAction = new FillWithDemoDataAction();
	}
	
	public IContent getContent() {
		return form.getContent();
	}
	
	private T createEditedObject(T original) {
		if (original != null) {
			return CloneHelper.clone(original);
		} else {
			return newInstance();
		}
	}
	
	/**
	 * Override this method to preset values for the editor
	 * 
	 * @return The object this editor should edit.
	 */
	protected T newInstance() {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) org.minimalj.util.GenericUtils.getGenericClass(Editor.this.getClass());
		T newInstance = CloneHelper.newInstance(clazz);
		return newInstance;
	}
	
	public final void setEditorListener(EditorListener savedListener) {
		this.editorListener = savedListener;
	}
	
	protected void finish() {
		editedObject = null;
	}
	
	public void cancel() {
		fireCanceled();
		finish();
	}

	private void fireCanceled() {
		try {
			editorListener.canceled();
		} catch (Exception x) {
			logger.log(Level.SEVERE, x.getLocalizedMessage(), x);
		}
	}

	public final boolean isFinished() {
		return editedObject == null;
	}
	
	private void fireSaved(Object saveResult) {
		try {
			editorListener.saved(saveResult);
		} catch (Exception x) {
			logger.log(Level.SEVERE, x.getLocalizedMessage(), x);
		}
	}

	protected final T getObject() {
		return editedObject;
	}
	
	public void save() {
		if (isSaveable()) {
			try {
				Object saveResult = save(editedObject);
				fireSaved(saveResult);
				finish();
			} catch (Exception x) {
				String message = x.getMessage() != null ? x.getMessage() : x.getClass().getSimpleName();
				logger.log(Level.SEVERE, message, x);
				ClientToolkit.getToolkit().showError("Technical problems: " + message);
			}
		} else {
			ClientToolkit.getToolkit().showError("Save is not possible because input is not valid");
		}
	}

	private class EditorChangeListener implements Form.FormChangeListener<T> {

		public void changed() {
			userEdited = true;
		}

		@Override
		public void commit() {
			if (isSaveable()) {
				save();
			}
		}

		@Override
		public void validate(T object, List<ValidationMessage> validationResult) {
			Editor.this.validate(object, validationResult);
		}

		@Override
		public void indicate(List<ValidationMessage> validationMessages, boolean allUsedElementsValid) {
			saveAction.setEnabled(allUsedElementsValid);
			saveAction.setValidationMessages(validationMessages);
			editorListener.setValidationMessages(validationMessages);
		}
	}		

	protected void validate(T object, List<ValidationMessage> validationMessages) {
		// overwrite this method to add Editor specific validation
	}
	
	protected final boolean isSaveable() {
		return saveAction.isEnabled();
	}
	
	protected final class SaveAction extends Action {
		private String description;
		
		@Override
		public void action() {
			save();
		}
		
		public void setValidationMessages(List<ValidationMessage> validationMessages) {
//			String iconKey;
			String description;
			boolean valid = validationMessages == null || validationMessages.isEmpty();
			if (valid) {
//				iconKey = getClass().getSimpleName() + ".icon.Ok";
				description = "Eingaben speichern";
			} else {
//				iconKey = getClass().getSimpleName() + ".icon.Error";
				description = ValidationMessage.formatHtml(validationMessages);
			}
			
//			Icon icon = ResourceHelper.getIcon(Resources.getResourceBundle(), iconKey);
//			putValue(LARGE_ICON_KEY, icon);
			this.description = description;
			fireChange();
		}

		@Override
		public String getDescription() {
			return description;
		}
	}
	
	private class CancelAction extends Action {
		@Override
		public void action() {
			cancel();
		}
	}
	
	private class FillWithDemoDataAction extends Action {
		public void action() {
			fillWithDemoData();
		}
	}
	
	//
	
	public interface EditorListener {
		
		public void setValidationMessages(List<ValidationMessage> validationMessages);

		public void saved(Object savedResult);
		
		public void canceled();
	}
	
	public void fillWithDemoData() {
		if (editedObject instanceof Mocking) {
			((Mocking) editedObject).mock();
			// re-set the object to update the FormFields
			form.setObject(editedObject);
		} else {
			form.mock();
		}
	}
	
}