package ch.openech.mj.toolkit;



public interface TextField extends IComponent {
	
	public void setInput(String text);

	public String getInput();

	public void setEditable(boolean editable);
	
	public void setFocusListener(IFocusListener focusListener);
	
	public void setCommitListener(Runnable runnable);

}
