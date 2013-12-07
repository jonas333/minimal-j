package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import ch.openech.mj.toolkit.IFocusListener;
import ch.openech.mj.toolkit.TextField;

public class AndroidReadonlyTextField extends TextView implements TextField {

	
	public AndroidReadonlyTextField(Context context) {
		super(context);
		setEditable(false);
		setBackgroundColor(Color.WHITE);
	}

	@Override
	public void setEditable(boolean editable) {
		
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
	public String getText() {
		return super.getText().toString();
	}

	@Override
	public void setText(String text) {
		super.setText(text);
	}

}
