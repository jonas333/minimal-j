package ch.openech.mj.toolkit;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;


public interface TextField extends IComponent, Focusable {
	
	public void setText(String text);

	public String getText();

	public void setEnabled(boolean editable);
	
	public void setFocusListener(FocusListener focusListener);
	
	public void setCommitListener(ActionListener listener);

}
