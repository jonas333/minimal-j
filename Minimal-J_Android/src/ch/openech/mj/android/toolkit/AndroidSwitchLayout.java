package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.SwitchLayout;

public class AndroidSwitchLayout extends LinearLayout implements SwitchLayout {

	IComponent shownComponent;
	
	public AndroidSwitchLayout(Context context) {
		super(context);
	}

	@Override
	public IComponent getShownComponent() {
		return shownComponent;
	}

	@Override
	public void show(IComponent component ) {
		if (shownComponent != component)
		{
			removeAllViews();
			addView((View) component);
			shownComponent = component;
		}
	}


}
