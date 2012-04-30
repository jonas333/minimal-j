package ch.openech.mj.edit;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.edit.validation.Indicator;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.edit.value.CloneHelper;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.resources.ResourceHelper;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ConfirmDialogListener;

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
 * 
 * @author Bruno
 * 
 * @param <T>
 *            Class of the edited Object
 */
public abstract class Editor<T> {

	private T original;
	protected final Action saveAction;
	protected final Action cancelAction;
	protected final Action demoDataAction;
	private IForm<T> form;
	private EditorFinishedListener editorFinishedListener;
	private Indicator indicator;
	private boolean saveable = true;
	private boolean userEdited;
	private String followLink;
	protected PageContext context;
	
	// what to implement

	protected abstract IForm<T> createForm();

	protected T load() {
		return null;
	}

	protected T newInstance() {
		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) ch.openech.mj.util.GenericUtils.getGenericClass(Editor.this.getClass());
		if (clazz == null) {
			throw new RuntimeException("TODO");
		}
		T newInstance = CloneHelper.newInstance(clazz);
		return newInstance;
	}
	
	protected abstract void validate(T object, List<ValidationMessage> resultList);

	protected abstract boolean save(T object);

	public String getTitle() {
		String resourceName = getClass().getSimpleName() + ".text";
		return Resources.getString(resourceName);
	}
	
	public Action[] getActions() {
		return new Action[]{demoDataAction, cancelAction, saveAction};
	}

	// /////

	protected Editor() {
		saveAction = createSaveAction();
		cancelAction = createCancelAction();
		demoDataAction = createDemoDataAction();
	}
	
	public IForm<T> startEditor(PageContext context) {
		if (form != null) {
			throw new IllegalStateException();
		}
		this.context = context;
		form = createForm();
		
		original = load();
		if (original != null) {
			T copy = CloneHelper.clone(original);
			form.setObject(copy);
		} else {
			T newInstance = newInstance();
			form.setObject(newInstance);
		}
		
		form.setSaveAction(saveAction);
		userEdited = false;
		form.setChangeListener(new EditorChangeListener());
		
		return form;
	}
	
	public void setEditorFinishedListener(EditorFinishedListener editorFinishedListener) {
		this.editorFinishedListener = editorFinishedListener;
	}
	
	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	/**
	 * 
	 * Disposes the editor and calls the editorFinished Listener
	 */
	void finish() {
		form = null;
		fireEditorFinished();
	}
	
	public boolean isFinished() {
		return form == null;
	}
	
	private void fireEditorFinished() {
		if (editorFinishedListener != null) {
			editorFinishedListener.finished(followLink);
		}
	}
	
	protected void setFollowLink(String followLink) {
		this.followLink = followLink;
	}
	
	protected T getObject() {
		return form.getObject();
	}
	
	protected final void save() {
		if (saveable) {
			if (isSaveSynchron()) {
				doSave();
			} else {
				progress(0, 100);
				new Thread(new Runnable() {
					@Override
					public void run() {
						doSave();
					}
				}).start();
			}
		} else {
			ClientToolkit.getToolkit().showNotification(form, "Abschluss nicht möglich.\n\nBitte Eingaben überprüfen.");
		}
	}

	private void doSave() {
		T objectToSave;
		if (original != null) {
			objectToSave = original;
			CloneHelper.deepCopy(getObject(), original);
		} else {
			objectToSave = getObject();
		}
		if (save(objectToSave)) {
			finish();
		}
	}
	
	public void progress(int value, int maximum) {
		if (editorFinishedListener != null) {
			editorFinishedListener.progress(value, maximum);
		}
	}
	
	private class EditorChangeListener implements ChangeListener {

		public EditorChangeListener() {
			update(getObject());
		}
		
		@Override
		public void stateChanged(ChangeEvent e) {
			userEdited = true;
			update(getObject());
		}
	}

	private void update(T object) {
		List<ValidationMessage> validationResult = validate(object);
		indicate(validationResult);
	}
	
	private List<ValidationMessage> validate(T object) {
		List<ValidationMessage> validationResult = new ArrayList<ValidationMessage>();
		if (object instanceof Validatable) {
			Validatable validatable = (Validatable) object;
			validatable.validate(validationResult);
		}
		form.validate(validationResult);
		validate(object, validationResult);
		return validationResult;
	}
	
	final void indicate(List<ValidationMessage> validationResult) {
		saveable = validationResult.isEmpty();
		form.setValidationMessages(validationResult);
		if (saveAction instanceof Indicator) {
			((Indicator) saveAction).setValidationMessages(validationResult);
		}
		if (indicator != null) {
			indicator.setValidationMessages(validationResult);
		}
	}
	
	protected boolean isSaveable() {
		return saveable;
	}
	
	public void checkedClose() {
		if (!userEdited) {
			finish();
		} else if (isSaveable()) {
			ConfirmDialogListener listener = new ConfirmDialogListener() {
				@Override
				public void onClose(int answer) {
					if (answer == JOptionPane.YES_OPTION) {
						// finish will be called at the end of save
						save();
					} else if (answer == JOptionPane.NO_OPTION) {
						finish();
					} else { // Cancel or Close
						// do nothing
					}
				}
			};
			ClientToolkit.getToolkit().showConfirmDialog(form, "Sollen die aktuellen Eingaben gespeichert werden?", "Schliessen",
					JOptionPane.YES_NO_CANCEL_OPTION, listener);

		} else {
			ConfirmDialogListener listener = new ConfirmDialogListener() {
				@Override
				public void onClose(int answer) {
					if (answer == JOptionPane.YES_OPTION) {
						finish();
					} else { // No or Close
						// do nothing
					}
				}
			};
			
			ClientToolkit.getToolkit().showConfirmDialog(form, "Die momentanen Eingaben sind nicht gültig\nund können daher nicht gespeichert werden.\n\nSollen sie verworfen werden?",
					"Schliessen", JOptionPane.YES_NO_OPTION, listener);
		}
	}

	protected boolean isSaveSynchron() {
		return true;
	}
	
	protected Action createSaveAction() {
		return new SaveAction("OkAction") {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		};
	}
	
	protected Action createCancelAction() {
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				finish();
			}
		};
		ResourceHelper.initProperties(action, Resources.getResourceBundle(), "CancelAction");
		return action;
	}
	
	protected Action createDemoDataAction() {
		Action action = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fillWithDemoData();
			}
		};
		ResourceHelper.initProperties(action, Resources.getResourceBundle(), "FillWithDemoDataAction");
		return action;
	}
	
	//
	
	public interface EditorFinishedListener {
		
		public void progress(int value, int maximum);
		
		public void finished(String followLink);
		
	}
	
	public void fillWithDemoData() {
		if (form instanceof DemoEnabled) {
			((DemoEnabled) form).fillWithDemoData();
		}
	}
	
}
