package org.minimalj.frontend.html.toolkit;

import org.minimalj.frontend.toolkit.CheckBox;

public class HtmlCheckBox extends HtmlComponent implements CheckBox {

	private boolean selected;
//	private boolean editable;
	
	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setEditable(boolean editable) {
		if (editable) throw new RuntimeException("Not yet implemented");
	}

	@Override
	public String text() {
		return selected ? "x" : "-";
	}

}
