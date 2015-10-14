package org.minimalj.frontend.impl.swing.toolkit;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.FocusTraversalPolicy;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.FocusManager;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.text.JTextComponent;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.action.Action;
import org.minimalj.frontend.action.Action.ActionChangeListener;
import org.minimalj.frontend.impl.swing.SwingFrame;
import org.minimalj.frontend.impl.swing.SwingTab;
import org.minimalj.frontend.impl.swing.component.SwingHtmlContent;
import org.minimalj.frontend.page.IDialog;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.page.PageBrowser;
import org.minimalj.model.Rendering;
import org.minimalj.model.Rendering.RenderType;
import org.minimalj.security.Subject;

public class SwingFrontend extends Frontend {

	public SwingFrontend() {
		AWTEventListener listener = new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				if (event.getID() == FocusEvent.FOCUS_GAINED && event.getSource() instanceof Component) {
					Component focusedPageComponent = findPageComponent((Component) event.getSource());
					if (focusedPageComponent != null) {
						SwingTab swingTab = findSwingTab(focusedPageComponent);
						swingTab.setFocusedPage(getPageProperty(focusedPageComponent));
					}
				}
			}
		};
		Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.FOCUS_EVENT_MASK);
	}
	
	@Override
	public IComponent createLabel(String string) {
		return new SwingText(string);
	}
	
	@Override
	public IComponent createLabel(Action action) {
		return new SwingActionLabel(action);
	}

	@Override
	public IComponent createText(Rendering rendering) {
		return new SwingText(rendering);
	}
	
	public static class SwingActionLabel extends JLabel implements IComponent {
		private static final long serialVersionUID = 1L;

		public SwingActionLabel(final Action action) {
			setText(action.getName());
			setToolTipText(action.getDescription());
			
			setForeground(Color.BLUE);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					SwingFrontend.executeActionInSwingWorker(action, (Component) e.getSource());
				}
			});
		}
	}
	
	public static SwingTab findSwingTab(Component c) {
		while (c != null && !(c instanceof SwingFrame)) {
			if (c instanceof JPopupMenu) {
				c = ((JPopupMenu) c).getInvoker();
			} else {
				c = c.getParent();
			}
		}
		return c != null ? ((SwingFrame) c).getVisibleTab() : null;
	}
	
//	public static Page findPage(Component c) {
//		while (c != null && getPageProperty(c) == null) {
//			if (c instanceof JPopupMenu) {
//				c = ((JPopupMenu) c).getInvoker();
//			} else {
//				c = c.getParent();
//			}
//		}
//		return getPageProperty(c);
//	}
	
	public static Component findPageComponent(Component c) {
		while (c != null && getPageProperty(c) == null) {
			if (c instanceof JPopupMenu) {
				c = ((JPopupMenu) c).getInvoker();
			} else {
				c = c.getParent();
			}
		}
		return c;
	}
	
	private static Page getPageProperty(Component c) {
		if (c instanceof JComponent) {
			JComponent jcomponent = (JComponent) c;
			return (Page) jcomponent.getClientProperty("page");
		} else {
			return null;
		}
	}
	
//	public static void updateEventTab(Component c) {
//		SwingTab swingTab = findSwingTab(c);
//		Frontend.setBrowser(swingTab);
//		if (swingTab != null) {
//			Page focusedPage = findPage(c);
//			swingTab.setFocusedPage(focusedPage);
//		}
//	}

	@Override
	public IComponent createTitle(String string) {
		return new SwingTitle(string);

	}

	@Override
	public Input<String> createReadOnlyTextField() {
		return new SwingReadOnlyTextField();
	}

	@Override
	public Input<String> createTextField(int maxLength, String allowedCharacters, InputType inputType, Search<String> suggestionSearch,
			InputComponentListener changeListener) {
		if (suggestionSearch == null) {
			return new SwingTextField(changeListener, maxLength, allowedCharacters);
		} else {
			return new SwingTextFieldAutocomplete(changeListener, suggestionSearch);
		}
	}

	@Override
	public PasswordField createPasswordField(InputComponentListener changeListener, int maxLength) {
		return new SwingPasswordField(changeListener, maxLength);
	}
	
	@Override
	public Input<String> createAreaField(int maxLength, String allowedCharacters, InputComponentListener changeListener) {
		return new SwingTextAreaField(changeListener, maxLength, null);
	}

	@Override
	public Input<byte[]> createImage(Size size, InputComponentListener changeListener) {
		return new SwingImage(size, changeListener);
	}
	
	@Override
	public IList createList(Action... actions) {
		return new SwingList(actions);
	}

	@Override
	public <T> Input<T> createComboBox(List<T> objects, InputComponentListener changeListener) {
		return new SwingComboBox<T>(objects, changeListener);
	}

	@Override
	public Input<Boolean> createCheckBox(InputComponentListener changeListener, String text) {
		return new SwingCheckBox(changeListener, text);
	}

	@Override
	public IComponent createComponentGroup(IComponent... components) {
		return new SwingHorizontalLayout(components);
	}

	@Override
	public SwitchContent createSwitchContent() {
		return new SwingSwitchContent();
	}

	@Override
	public FormContent createFormContent(int columns, int columnWidthPercentage) {
		return new SwingGridFormLayout(columns, columnWidthPercentage);
	}

	public static void focusFirstComponent(JComponent jComponent) {
		if (jComponent.isShowing()) {
			focusFirstComponentNow(jComponent);
		} else {
			focusFirstComponentLater(jComponent);
		}
	}

	private static void focusFirstComponentNow(JComponent component) {
		FocusTraversalPolicy focusPolicy = component.getFocusTraversalPolicy();
		if (component instanceof JTextComponent || component instanceof JComboBox || component instanceof JCheckBox) {
			component.requestFocus();
		} else if (focusPolicy != null && focusPolicy.getFirstComponent(component) != null) {
			focusPolicy.getFirstComponent(component).requestFocus();
		} else {
			FocusManager.getCurrentManager().focusNextComponent(component);
		}
	}

	private static void focusFirstComponentLater(final JComponent component) {
		component.addHierarchyListener(new HierarchyListener() {
			@Override
			public void hierarchyChanged(HierarchyEvent e) {
				component.removeHierarchyListener(this);
				focusFirstComponent(component);
			}
		});
	}

	@Override
	public <T> ITable<T> createTable(Object[] keys, TableActionListener<T> listener) {
		return new SwingTable<T>(keys, listener);
	}
	
	@Override
	public IContent createHtmlContent(String htmlOrUrl) {
		return new SwingHtmlContent(htmlOrUrl);
	}
	
	@Override
	public <T> Input<T> createLookup(InputComponentListener changeListener, Search<T> index, Object[] keys) {
		return new SwingLookup<T>(changeListener, index, keys);
	}
	
	public File showFileDialog(String title, String approveButtonText) {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(false);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setDialogTitle(title);
		if (JFileChooser.APPROVE_OPTION == chooser.showDialog((Component) getBrowser(), approveButtonText)) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}
	
	private static class SwingLookup<T> extends JPanel implements Input<T> {
		private static final long serialVersionUID = 1L;
		
		private final InputComponentListener changeListener;
		private final Search<T> search;
		private final Object[] keys;
		private final SwingLookupLabel actionLabel;
		private final SwingRemoveLabel removeLabel;
		private IDialog dialog;
		private T selectedObject;
		
		public SwingLookup(InputComponentListener changeListener, Search<T> search, Object[] keys) {
			super(new BorderLayout());
			
			this.changeListener = changeListener;
			this.search = search;
			this.keys = keys;
			
			this.actionLabel = new SwingLookupLabel();
			this.removeLabel = new SwingRemoveLabel();
			add(actionLabel, BorderLayout.CENTER);
			add(removeLabel, BorderLayout.LINE_END);
		}

		@Override
		public void setValue(T value) {
			this.selectedObject = value;
			display();
		}
		
		protected void display() {
			if (selectedObject instanceof Rendering) {
				Rendering rendering = (Rendering) selectedObject;
				actionLabel.setText(rendering.render(RenderType.PLAIN_TEXT));
			} else if (selectedObject != null) {
				actionLabel.setText(selectedObject.toString());
			} else {
				actionLabel.setText("[+]");
			}
		}

		@Override
		public T getValue() {
			return selectedObject;
		}
		
		@Override
		public void setEditable(boolean editable) {
			actionLabel.setEnabled(editable);
			removeLabel.setEnabled(editable);
		}
		
		private class SwingLookupLabel extends JLabel {
			private static final long serialVersionUID = 1L;

			public SwingLookupLabel() {
				setForeground(Color.BLUE);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						dialog = Frontend.getBrowser().showSearchDialog(search, keys, new LookupClickListener());
					}
				});
			}
		}
		
		private class SwingRemoveLabel extends JLabel {
			private static final long serialVersionUID = 1L;

			public SwingRemoveLabel() {
				super("[x]");
				setForeground(Color.BLUE);
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						SwingLookup.this.selectedObject = null;
						changeListener.changed(SwingLookup.this);
					}
				});
			}
		}
		
		private class LookupClickListener implements TableActionListener<T> {
			@Override
			public void action(T selectedObject) {
				SwingLookup.this.selectedObject = selectedObject;
				dialog.closeDialog();
				changeListener.changed(SwingLookup.this);
			}
		}
	}

	public static boolean verticallyGrowing(Component component) {
		if (component instanceof SwingList || component instanceof JTable || component instanceof SwingTextAreaField || component instanceof SwingImage) {
			return true;
		}
		if (component instanceof Container) {
			Container container = (Container) component;
			for (Component c : container.getComponents()) {
				if (verticallyGrowing(c)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static javax.swing.Action[] adaptActions(Action[] actions) {
		javax.swing.Action[] swingActions = new javax.swing.Action[actions.length];
		for (int i = 0; i<actions.length; i++) {
			swingActions[i] = adaptAction(actions[i]);
		}
		return swingActions;
	}

	public static javax.swing.Action adaptAction(final Action action) {
		final javax.swing.Action swingAction = new AbstractAction(action.getName()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				executeActionInSwingWorker(action, (Component) e.getSource());
			}
		};
		action.setChangeListener(new ActionChangeListener() {
			{
				update();
			}
			
			@Override
			public void change() {
				update();
			}

			protected void update() {
				swingAction.setEnabled(action.isEnabled());
				swingAction.putValue(javax.swing.Action.SHORT_DESCRIPTION, action.getDescription());
			}
		});
		return swingAction;
	}
	
	public static void executeActionInSwingWorker(Action action, Component source) {
		SwingTab pageBrowser = findSwingTab(source);
		pageBrowser.lock();
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				Frontend.setBrowser(new EventThreadPageBrowser(pageBrowser));
				action.action();
				Thread.sleep(3000);
				return null;
			}
		};
		worker.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
//				if ("progress".equals(evt.getPropertyName())) {
//					// progress.showProgress((Integer) evt.getNewValue(), 100);
//				} else 
				if ("state".equals(evt.getPropertyName()) && evt.getNewValue() == StateValue.DONE) {
					pageBrowser.unlock();
				}
			}
		});
		worker.execute();
	}
	
	public static void installAccelerator(javax.swing.Action action, final JButton button) {
		if (action.getValue(javax.swing.Action.ACCELERATOR_KEY) instanceof KeyStroke) {
			KeyStroke keyStroke = (KeyStroke)action.getValue(javax.swing.Action.ACCELERATOR_KEY);
			InputMap windowInputMap = SwingUtilities.getUIInputMap(button, JComponent.WHEN_IN_FOCUSED_WINDOW);
			if (windowInputMap == null) {
				windowInputMap = new ComponentInputMapUIResource(button);
				SwingUtilities.replaceUIInputMap(button, JComponent.WHEN_IN_FOCUSED_WINDOW, windowInputMap);
			}
			windowInputMap.put(keyStroke, keyStroke.toString());
			button.getActionMap().put(keyStroke.toString(), action);
		}
	}
	
	private static class EventThreadPageBrowser implements PageBrowser {
		private final PageBrowser delegate;
		
		public EventThreadPageBrowser(PageBrowser delegate) {
			this.delegate = delegate;
		}

		public Subject getSubject() {
			return delegate.getSubject();
		}

		public void setSubject(Subject subject) {
			delegate.setSubject(subject);
		}

		public boolean hasPermission(String... accessRoles) {
			return delegate.hasPermission(accessRoles);
		}

		public void show(Page page) {
			SwingUtilities.invokeLater(() -> delegate.show(page));
		}

		public void showDetail(Page page) {
			delegate.showDetail(page);
		}

		public void hideDetail(Page page) {
			delegate.hideDetail(page);
		}

		public boolean isDetailShown(Page page) {
			return delegate.isDetailShown(page);
		}

		public IDialog showDialog(String title, IContent content, Action saveAction, Action closeAction, Action... actions) {
			return delegate.showDialog(title, content, saveAction, closeAction, actions);
		}

		public <T> IDialog showSearchDialog(Search<T> index, Object[] keys, TableActionListener<T> listener) {
			return delegate.showSearchDialog(index, keys, listener);
		}

		public void showMessage(String text) {
			delegate.showMessage(text);
		}

		public void showError(String text) {
			delegate.showError(text);
		}
	}
}
