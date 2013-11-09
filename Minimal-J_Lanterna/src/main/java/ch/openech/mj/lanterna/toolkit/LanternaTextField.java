package ch.openech.mj.lanterna.toolkit;

import ch.openech.mj.toolkit.ClientToolkit.InputComponentListener;
import ch.openech.mj.toolkit.IFocusListener;
import ch.openech.mj.toolkit.TextField;

import com.googlecode.lanterna.gui.component.InteractableComponent;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.listener.ComponentAdapter;
import com.googlecode.lanterna.input.Key;

public class LanternaTextField extends TextBox implements TextField {

	private final InputComponentListener changeListener;
	private IFocusListener focusListener;
	
	public LanternaTextField(InputComponentListener changeListener) {
		this.changeListener = changeListener;
		addComponentListener(new TextFieldComponentListener());
	}

	@Override
	public void setEditable(boolean editable) {
		super.setVisible(editable);
	}

	@Override
	public void setFocusListener(IFocusListener focusListener) {
		this.focusListener = focusListener;
	}

	@Override
	public void setCommitListener(Runnable listener) {
		// ignored at the moment
	}

	@Override
	public String getText() {
		String text = super.getText();
		if (text.length() == 0) return null;
		return text;
	}

	@Override
	public void setText(String text) {
		if (text == null) {
			text = "";
		}
		super.setText(text);
	}
	
	private void fireChangeEvent() {
		changeListener.changed(LanternaTextField.this);
	}
	
	@Override
	public Result keyboardInteraction(Key key) {
		Result result = super.keyboardInteraction(key);
		if (result != Result.EVENT_NOT_HANDLED) {
			fireChangeEvent();
		}
		return result;
	}

	private class TextFieldComponentListener extends ComponentAdapter {

		@Override
		public void onComponentReceivedFocus(InteractableComponent interactableComponent) {
			if (focusListener != null) {
				focusListener.onFocusGained();
			}
		}
		
		@Override
		public void onComponentLostFocus(InteractableComponent interactableComponent) {
			if (focusListener != null) {
				focusListener.onFocusLost();
			}
		}
		
	}

	@Override
	public void setInput(String text) {
		setText(text);
	}

	@Override
	public String getInput() {
		return getText();
	}
	
}
