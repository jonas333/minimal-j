package org.minimalj.frontend.impl.swing.lookAndFeel;

public class TerminalLargeFontLookAndFeel extends TerminalLookAndFeel {
	private static final long serialVersionUID = 1L;

	@Override
	public MetalTheme getMetalTheme() {
		return new TerminalLookAndFeel.MetalTheme(true);
	}

}
