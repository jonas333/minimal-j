package org.minimalj.frontend.html.toolkit;

import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;

public abstract class HtmlComponent implements IComponent {

	private String caption;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public abstract String text();

}
