package ch.openech.mj.android.toolkit;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import ch.openech.mj.toolkit.ClientToolkit.InputComponentListener;
import ch.openech.mj.toolkit.ComboBox;

public class AndroidComboBox<T> extends Spinner implements ComboBox<T> {

	
	private final InputComponentListener listener;
	ArrayAdapter<T> data = null;
	
	public AndroidComboBox(Context context, InputComponentListener listener) {
		super(context);
		this.listener = listener;
		setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int argposition2, long id) {
				fireChangeEvent();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Not interested
			}
		});
	}
	
	private void fireChangeEvent() {
		if (listener != null) {
			listener.changed(this);
		}
	}
	

	@Override
	public T getSelectedObject() {
		if (data != null) 
		{
			return data.getItem(getSelectedItemPosition());
		}
		return null;
	}

	@Override
	public void setObjects(List<T> objects) {
		data = new ArrayAdapter<T>(getContext(), android.R.layout.simple_spinner_dropdown_item, objects);
		setAdapter(data);
	}

	@Override
	public void setSelectedObject(T object) throws IllegalArgumentException {
		if (data != null) {
			setSelection(data.getPosition(object));
		}
		
	}

}
