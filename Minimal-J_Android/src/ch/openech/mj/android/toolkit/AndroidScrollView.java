package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.view.View;
import android.widget.ScrollView;
import ch.openech.mj.toolkit.IComponent;

public class AndroidScrollView extends ScrollView implements IComponent {

	public AndroidScrollView(Context context, IComponent component) {
		super(context);
		addView((View) component);
	}

}
