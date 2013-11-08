package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.graphics.Rect;
import android.widget.EditText;
import ch.openech.mj.toolkit.ClientToolkit.InputComponentListener;
import ch.openech.mj.toolkit.IFocusListener;
import ch.openech.mj.toolkit.TextField;

public class AndroidTextField extends EditText implements TextField {

	private final InputComponentListener changeListener;
	private IFocusListener focusListener;
	private Runnable commitListener;
	
	public AndroidTextField(Context context) {
		this(context, null, Integer.MAX_VALUE);
	}

	public AndroidTextField(Context ctx,   InputComponentListener changeListener, int maxLength) {
		this(ctx, changeListener, maxLength, null);
	}
	
	public AndroidTextField(Context ctx, InputComponentListener changeListener, int maxLength, String allowedCharacters) {
		super(ctx);
		this.changeListener = changeListener;
	}
	
	@Override
	public void setCommitListener(Runnable commitListener) {
		this.commitListener = commitListener;
	}

	@Override
	public void setEditable(boolean editable) {
		setEnabled(editable);

	}

	@Override
	public void setFocusListener(IFocusListener focusListener) {
		this.focusListener = focusListener;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		if (focusListener != null)
		{
			if (focused) {
				focusListener.onFocusGained();
			} else {
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
		return getText().toString();
	}

	
	
	
	

}
