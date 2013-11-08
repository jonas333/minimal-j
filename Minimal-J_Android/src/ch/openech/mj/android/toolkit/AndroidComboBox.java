package ch.openech.mj.android.toolkit;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import ch.openech.mj.toolkit.ComboBox;

public class AndroidComboBox<T> extends Spinner implements ComboBox<T> {

	public AndroidComboBox(Context context) {
		super(context);
	}

	@Override
	public T getSelectedObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setObjects(List<T> objects) {
		ArrayAdapter<T> data = new ArrayAdapter<T>(getContext(), android.R.layout.simple_spinner_dropdown_item, objects);
		setAdapter(data);
	}

	@Override
	public void setSelectedObject(T arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

}
