package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.widget.EditText;
import ch.openech.mj.toolkit.IFocusListener;
import ch.openech.mj.toolkit.TextField;

public class AndroidReadonlyTextField extends EditText implements TextField {

	
	private boolean isFirst = true;
	
	public AndroidReadonlyTextField(Context context) {
		super(context);
		setEditable(false);
	}

	@Override
	public void setEditable(boolean editable) {
		if (isFirst) {
			isFirst = !isFirst;
			setEnabled(editable);
		}
	}
	
	@Override
	public void setFocusListener(IFocusListener focusListener) {
		// read only field cannot be focused
	}

	@Override
	public void setCommitListener(Runnable runnable) {
		// read only field cannot get commit command
	}

	@Override
	public String getInput() {
		return getText().toString();
	}

	@Override
	public void setInput(String text) {
		setText(text);
	}

}
