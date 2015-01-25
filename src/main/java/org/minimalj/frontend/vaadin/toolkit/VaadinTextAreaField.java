package org.minimalj.frontend.vaadin.toolkit;

import org.minimalj.frontend.toolkit.ClientToolkit.InputComponentListener;
import org.minimalj.frontend.toolkit.IFocusListener;
import org.minimalj.frontend.toolkit.TextField;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;

/**
 * Nearly identical with TextField but extends from TextArea.
 * Maybe some code could be deduplicated.
 * 
 */
public class VaadinTextAreaField extends com.vaadin.ui.TextArea implements TextField {
	private static final long serialVersionUID = 1L;

	private TextChangeEvent event;
	private Runnable commitListener;
	
	public VaadinTextAreaField(InputComponentListener changeListener, int maxLength) {
		this(changeListener, maxLength, null);
	}
	
	public VaadinTextAreaField(InputComponentListener changeListener, int maxLength, String allowedCharacters) {
		setMaxLength(maxLength);
		setNullRepresentation("");
		setImmediate(true);
		if (changeListener != null) {
			addListener(new VaadinTextFieldTextChangeListener(changeListener));
			addShortcutListener(new ShortcutListener("Commit", ShortcutAction.KeyCode.ENTER, null) {
				private static final long serialVersionUID = 1L;

				@Override
				public void handleAction(Object sender, Object target) {
					if (target == VaadinTextAreaField.this) {
						if (commitListener != null) {
							commitListener.run();
						}
					}
				}
			});
		} else {
			setReadOnly(true);
		}
	}


	@Override
	public void setCommitListener(Runnable commitListener) {
		this.commitListener = commitListener;
	}
	
	private class VaadinTextFieldTextChangeListener implements TextChangeListener {
		private static final long serialVersionUID = 1L;
		private final InputComponentListener changeListener;
		
		public VaadinTextFieldTextChangeListener(InputComponentListener changeListener) {
			this.changeListener = changeListener;
		}

		@Override
		public void textChange(TextChangeEvent event) {
			VaadinTextAreaField.this.event = event;
			changeListener.changed(VaadinTextAreaField.this);
			VaadinTextAreaField.this.event = null;
		}
	}

	@Override
	public void setEditable(boolean editable) {
		setReadOnly(!editable);
	}

	@Override
	public void setText(String text) {
		boolean readOnly = isReadOnly();
		if (readOnly) {
			setReadOnly(false);
			setValue(text);
			setReadOnly(true);
		} else {
			setValue(text);
		}		
	}

	@Override
	public String getText() {
		if (event != null) {
			return event.getText();
		} else {
			return (String) getValue();
		}
	}

	@Override
	public void setFocusListener(IFocusListener focusListener) {
		// TODO Auto-generated method stub
	}
	
}
