package ch.openech.mj.swing.toolkit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentInputMapUIResource;

import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.util.StringUtils;

public class SwingEditorLayout extends JPanel implements IComponent {

	public SwingEditorLayout(String information, IComponent content, Action[] actions) {
		super(new BorderLayout());
		addInformation(information);
		JPanel centerPanel = new JPanel(new SwingFormAlignLayoutManager());
		centerPanel.add(SwingClientToolkit.getComponent(content));
		add(centerPanel, BorderLayout.CENTER);
		add(createButtonBar(actions), BorderLayout.SOUTH);
	}

	protected void addInformation(String information) {
		if (!StringUtils.isBlank(information)) {
			JLabel help = new JLabel(information);
			help.setBorder(BorderFactory.createEmptyBorder(7, 10, 10, 7));
			add(help, BorderLayout.NORTH);
		}
	}

	protected JPanel createButtonBar(Action... actions) {
		JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0)); // align, hgap, vgap (also used for top/bottom insets)
		setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		addButtons(buttonBar, actions);
		return buttonBar;
	}
	
	protected void addButtons(JPanel buttonBar, Action... actions) {
		for (Action action: actions) {
			addActionButton(buttonBar, action);
		}
	}

	protected void addActionButton(JPanel buttonBar, Action action) {
		JButton button = new JButton(action);
		buttonBar.add(button);
		installAccelerator(action);
		installAdditionalActionListener(action, button);
	}

	private static void installAdditionalActionListener(Action action, final JButton button) {
		action.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("visible".equals(evt.getPropertyName()) && (evt.getNewValue() instanceof Boolean)) {
					button.setVisible((Boolean) evt.getNewValue());
				} else if ("foreground".equals(evt.getPropertyName()) && (evt.getNewValue() instanceof Color)) {
					button.setForeground((Color) evt.getNewValue());
				}
			}
		});
	}

	protected void installAccelerator(Action action) {
		if (action.getValue(Action.ACCELERATOR_KEY) instanceof KeyStroke) {
			KeyStroke keyStroke = (KeyStroke)action.getValue(Action.ACCELERATOR_KEY);
			InputMap windowInputMap = SwingUtilities.getUIInputMap(this, JComponent.WHEN_IN_FOCUSED_WINDOW);
			if (windowInputMap == null) {
				windowInputMap = new ComponentInputMapUIResource(this);
				SwingUtilities.replaceUIInputMap(this, JComponent.WHEN_IN_FOCUSED_WINDOW, windowInputMap);
			}
			windowInputMap.put(keyStroke, keyStroke.toString());
			this.getActionMap().put(keyStroke.toString(), action);
		}
	}
}
