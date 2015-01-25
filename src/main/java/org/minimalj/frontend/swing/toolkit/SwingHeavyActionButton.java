package org.minimalj.frontend.swing.toolkit;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

import org.minimalj.frontend.swing.SwingTab;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ProgressListener;

public class SwingHeavyActionButton extends JButton {
	private static final long serialVersionUID = 1L;

	public SwingHeavyActionButton(Action action) {
		super(action);
	}

	public SwingHeavyActionButton(String text) {
		super(text);
	}

	@Override
    protected void fireActionPerformed(final ActionEvent event) {
		final SwingTab tab = SwingClientToolkit.findSwingTab(SwingHeavyActionButton.this);
		final ProgressListener progress = ((SwingClientToolkit) ClientToolkit.getToolkit()).showProgress("Waiting");
		SwingWorker<Object, Object> worker = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() throws Exception {
				SwingClientToolkit.updateEventTab(tab);
				SwingHeavyActionButton.super.fireActionPerformed(event);
				setProgress(100);
				return null;
			}
		};
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					progress.showProgress((Integer) evt.getNewValue(), 100);
				} else if ("state".equals(evt.getPropertyName()) && evt.getNewValue() == StateValue.DONE) {
					progress.showProgress(100, 100);
				}
			}
		});
		worker.execute();
    }
	
}