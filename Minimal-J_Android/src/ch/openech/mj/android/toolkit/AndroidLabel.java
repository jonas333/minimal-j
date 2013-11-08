package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.TextView;
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;

public class AndroidLabel extends TextView implements IComponent {

	private IAction action = null;

	public AndroidLabel(Context context) {
		super(context);
	}

	public AndroidLabel(Context context, String caption) {
		super(context);
		setText(caption);
	}

	public AndroidLabel(Context context, IAction action) {
		this(context, action.getName());
		this.action = action;
		setTextColor(Color.BLUE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (action != null) {
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					action.action(this);
					return true;
				}
			}
		}
		return false;
	}

}
