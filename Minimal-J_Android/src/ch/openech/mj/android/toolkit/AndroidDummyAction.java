package ch.openech.mj.android.toolkit;

import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;

public class AndroidDummyAction implements IAction {

	@Override
	public void action(IComponent arg0) {
		System.out.println( getClass().getSimpleName()  +  " performing action");
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getName() {
		return "DummyAction";
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void setChangeListener(ActionChangeListener arg0) {

	}

}
