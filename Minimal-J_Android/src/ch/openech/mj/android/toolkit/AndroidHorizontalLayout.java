package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import ch.openech.mj.android.AndroidHelper;
import ch.openech.mj.toolkit.HorizontalLayout;
import ch.openech.mj.toolkit.IComponent;

public class AndroidHorizontalLayout extends LinearLayout implements HorizontalLayout {

	
	private static final int DEFAULT_GAP_BETWEEN_VIEWS  = 25; 
	
	
	public AndroidHorizontalLayout(Context context, IComponent... components) {
		this(context,DEFAULT_GAP_BETWEEN_VIEWS, components);
	}
	
	public AndroidHorizontalLayout(Context context, int gap, IComponent... components) {
		super(context);
		setOrientation(HORIZONTAL);
		for (IComponent component : components) {
			addView((View) component);
			addView(AndroidHelper.createSpace(context, DEFAULT_GAP_BETWEEN_VIEWS));
		}
	}
	
	
	
}
