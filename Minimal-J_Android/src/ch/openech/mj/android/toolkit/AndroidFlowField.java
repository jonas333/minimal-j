package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import ch.openech.mj.toolkit.FlowField;
import ch.openech.mj.toolkit.IComponent;

public class AndroidFlowField extends LinearLayout implements FlowField {

	
	private static int GAP = 5;
	
	public AndroidFlowField(Context context) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);
	}


	@Override
	public void add(IComponent component) {
		addView((View) component);
	}

	@Override
	public void addGap() {
		Space space = new Space(getContext());
		space.setPadding(0, GAP, 0, 0);
		addView(space);
	}

	@Override
	public void clear() {
		removeAllViews();
	}

}
