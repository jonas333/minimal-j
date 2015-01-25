package org.minimalj.frontend.html.toolkit;

import org.minimalj.frontend.toolkit.IFocusListener;
import org.minimalj.frontend.toolkit.TextField;

public class HtmlReadOnlyTextField extends HtmlComponent implements TextField {

	private String text;
	
	@Override
	public String getText() {
		return text;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public void setEditable(boolean editable) {
		if (editable) throw new IllegalStateException("Readonly Field cannot be set editable");
	}

	@Override
	public void setFocusListener(IFocusListener focusListener) {
		// not possible
	}

	@Override
	public void setCommitListener(Runnable runnable) {
		// not possible
	}

	@Override
	public String text() {
		return text;
	}

}
