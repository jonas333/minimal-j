package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;
import ch.openech.mj.toolkit.GridFormLayout;
import ch.openech.mj.toolkit.IComponent;

public class AndroidGridLayout extends GridLayout implements GridFormLayout {
	
	public AndroidGridLayout(Context context, int columns) {
		super(context);
		setColumnCount(columns);
	}
	

	@Override
	public void add(IComponent component, int span) {
		View child = (View) component;
		GridLayout.LayoutParams childLP = new GridLayout.LayoutParams();
		childLP.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, span);
		child.setLayoutParams(childLP);
		addView((View) component);
	}

}
