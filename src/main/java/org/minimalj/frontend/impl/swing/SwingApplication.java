package org.minimalj.frontend.impl.swing;

import javax.swing.SwingUtilities;

import org.minimalj.application.Application;
import org.minimalj.backend.Backend;
import org.minimalj.frontend.impl.swing.toolkit.SwingFrontend;

public class SwingApplication implements Runnable {

	private SwingApplication() {
		// private
	}

	/**
	 * Initializes application and opens a new frame
	 * 
	 */
	@Override
	public void run() {
		FrameManager.setSystemLookAndFeel();
		Application.getInstance().setFrontend(new SwingFrontend());
		Application.getInstance().setBackend(new SwingBackend(Backend.create()));
		
		FrameManager.getInstance().openNavigationFrame(null);
	}

	public static void main(final String[] args) throws Exception {
		Application.initApplication(args);

		SwingUtilities.invokeAndWait(new SwingApplication());
	}
	
}
