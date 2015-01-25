package org.minimalj.frontend.html.toolkit;

import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.FlowField;

public class HtmlFlowField extends HtmlComponent implements FlowField {

	private final StringBuilder builder = new StringBuilder();
	
	@Override
	public void clear() {
		builder.delete(0, builder.length());
	}

	@Override
	public void add(IComponent component) {
		builder.append(((HtmlComponent) component).text());
		builder.append("\n");
	}

	@Override
	public void addGap() {
		builder.append("\n\n");
	}

	@Override
	public String text() {
		return builder.toString();
	}

}
