package org.minimalj.frontend.html.toolkit;

import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;

public class HtmlLabel extends HtmlComponent implements IComponent {

	private final String label;

	public HtmlLabel(String label) {
		this.label = label;
	}

	@Override
	public String text() {
		return label;
	}
	
}
