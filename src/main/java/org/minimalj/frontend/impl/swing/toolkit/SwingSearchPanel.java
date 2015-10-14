package org.minimalj.frontend.impl.swing.toolkit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.minimalj.frontend.Frontend.Search;
import org.minimalj.frontend.Frontend.TableActionListener;
import org.minimalj.frontend.action.Action;

public class SwingSearchPanel<T> extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JTextField text;
	private final JButton searchButton;
	private final SwingTable<T> table;
	
	public SwingSearchPanel(final Search<T> search, Object[] keys, TableActionListener<T> listener) {
		super(new BorderLayout());
		
		text = new JTextField();
		searchButton = new JButton("Search");
		table = new SwingTable<T>(keys, listener);

		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(text, BorderLayout.CENTER);
		northPanel.add(searchButton, BorderLayout.EAST);
				
		add(border(northPanel, 5, 5, 5, 5), BorderLayout.NORTH);
		add(table, BorderLayout.CENTER);

		Action action = new Action() {
			@Override
			public void action() {
				List<T> objects = search.search(text.getText());
				table.setObjects(objects);
			}
		};
		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SwingFrontend.executeActionInSwingWorker(action, searchButton);
			}
		};
		text.addActionListener(actionListener);
		searchButton.addActionListener(actionListener);
	}

	private static Component border(Component component, int top, int left, int bottom, int right) {
		JComponent jComponent;
		if (component instanceof JComponent) {
			jComponent = (JComponent) component;
		} else {
			jComponent = new JPanel(new BorderLayout());
			jComponent.add(component, BorderLayout.CENTER);
		}
		jComponent.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
		return jComponent;
	}

}
