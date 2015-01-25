package org.minimalj.frontend.swing.toolkit;

import javax.swing.JLabel;

import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;

public class SwingLabel extends JLabel implements IComponent {
	private static final long serialVersionUID = 1L;

	public SwingLabel(String string) {
		super();
		if (string != null && string.contains("<")) {
			setText("<html><body>" + string + "</body></html>");
		} else {
			setText(string);
		}
	}

}