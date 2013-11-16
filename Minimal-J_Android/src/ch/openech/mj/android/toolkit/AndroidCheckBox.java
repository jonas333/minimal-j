package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import ch.openech.mj.toolkit.ClientToolkit.InputComponentListener;

public class AndroidCheckBox extends CheckBox implements ch.openech.mj.toolkit.CheckBox , OnCheckedChangeListener {

	private final InputComponentListener listener;
	
	public AndroidCheckBox(Context context, InputComponentListener inputComponentListener, String text) {
		super(context);
		this.listener = inputComponentListener;
		setText(text);
		setOnCheckedChangeListener(this);			
	}

	@Override
	public void setEditable(boolean editable) {
		setEnabled(editable);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (listener != null) {
			listener.changed(this);
		}
	}

}
