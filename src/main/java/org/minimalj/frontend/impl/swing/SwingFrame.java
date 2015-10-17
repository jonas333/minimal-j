package org.minimalj.frontend.impl.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.minimalj.application.Application;
import org.minimalj.backend.Backend;
import org.minimalj.frontend.impl.swing.component.HideableTabbedPane;
import org.minimalj.frontend.page.Page;
import org.minimalj.security.LoginAction;
import org.minimalj.security.LogoutTransaction;
import org.minimalj.security.Subject;

public class SwingFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private static SwingFrame swingFrameInConstruction = null;
	
	private SwingMenuBar menuBar;
	private HideableTabbedPane tabbedPane;
	final Action loginAction, logoutAction, closeWindowAction, exitAction, newWindowAction, newTabAction, closeTabAction;
	
	private Subject subject;
	
	public SwingFrame() {
		swingFrameInConstruction = this;
		
		loginAction = new SwingLoginAction();
		logoutAction = new SwingLogoutAction();
		updateLoginAction();
		
		updateWindowTitle();
		
		closeWindowAction = new CloseWindowAction();
		exitAction = new ExitAction();
		newWindowAction = new NewWindowAction();
		newTabAction = new NewTabAction();
		closeTabAction = new CloseTabAction();
		
		setDefaultSize();
		setLocationRelativeTo(null);
		addWindowListener();
		createContent();
		
		getRootPane().putClientProperty(SwingFrame.class.getSimpleName(), this);
		swingFrameInConstruction = null;
	}
	
	protected void setDefaultSize() {
		Dimension screenSize = getToolkit().getScreenSize();
		if (screenSize.width < 1280 || screenSize.height < 1024) {
			setExtendedState(MAXIMIZED_BOTH);
			setSize(screenSize.width - 20, screenSize.height - 40);
		} else {
			int width = Math.min(screenSize.width - 40, 1300);
			int height = Math.min(screenSize.height - 40, 1000);
			setSize(width, height);
		}
	}
	
	private void addWindowListener() {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WindowListener listener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				FrameManager.getInstance().closeWindowPerformed(SwingFrame.this);
			}
		};
		addWindowListener(listener);
	}

	protected void createContent() {
		getContentPane().setLayout(new BorderLayout());
		menuBar = new SwingMenuBar(this);
		getContentPane().add(menuBar, BorderLayout.NORTH);
		getContentPane().add(createTabbedPane(), BorderLayout.CENTER);
	}

	private JComponent createTabbedPane() {
		tabbedPane = new HideableTabbedPane();
		addTab();
		return tabbedPane;
	}

	private void addTab() {
		SwingTab tab = new SwingTab(this);
		if (getVisibleTab() != null) {
			tab.setSubject(getVisibleTab().getSubject());
		}
		
		tabbedPane.addTab("", tab);
		tabbedPane.setSelectedComponent(tab);

		tab.show(Application.getApplication().createDefaultPage());
	}
	
	public void closeTabActionPerformed() {
		if (getVisibleTab().tryToClose()) {
			closeTab();
			if (tabbedPane.getTabCount() == 0) {
				if (FrameManager.getInstance().askBeforeCloseLastWindow(this)) {
					FrameManager.getInstance().lastTabClosed(SwingFrame.this);
				} else {
					addTab();
				}
			}
		}
	}
	
	public boolean tryToCloseWindow() {
		boolean closable = true;
		for (int i = tabbedPane.getTabCount()-1; i>=0; i--) {
			SwingTab tab = (SwingTab) tabbedPane.getTab(i);
			tabbedPane.setSelectedComponent(tab);
			closable = tab.tryToClose();
			if (!closable) return false;
		}
		closeWindow();
		return true;
	}
	
	public void closeTab(SwingTab tab) {
		tabbedPane.removeTab(tab);
	}
	
	private void closeAllTabs() {
		tabbedPane.removeAllTabs();
	}
	
	public void closeWindow() {
		closeAllTabs();
		setVisible(false);
	}

	public static SwingFrame getActiveWindow() {
		if (swingFrameInConstruction != null) {
			return swingFrameInConstruction;
		}
		for (Window w : Window.getWindows()) {
			if (w.isActive()) {
				return (SwingFrame) w;
			}
		}
		return null;
	}

	public SwingTab getVisibleTab() {
		return (SwingTab) tabbedPane.getSelectedComponent();
	}
	
	public void closeTab() {
		closeTab((SwingTab) tabbedPane.getSelectedComponent());
	}

	public Page getVisiblePage() {
		SwingTab tab = getVisibleTab();
		if (tab == null) return null;
		return tab.getVisiblePage();
	}
	
	public List<Page> getPages() {
		List<Page> result = new ArrayList<Page>();
		for (int i = 0; i<tabbedPane.getTabCount(); i++) {
			SwingTab tab = (SwingTab) tabbedPane.getComponent(i); // myst: getTabComponent returns allways null
			Page page = tab.getVisiblePage();
			if (page != null) result.add(page);
		}
		return result;
	}
	
	void onHistoryChanged() {
		updateTitle();
	}
	
	protected void updateWindowTitle() {
		String title = Application.getApplication().getName();
		if (subject != null) {
			title = title + " - " + subject.getName();
		}
		setTitle(title);
	}
	
	protected void updateTitle() {
		for (int index = 0; index<tabbedPane.getTabCount(); index++) {
			SwingTab tab = (SwingTab) tabbedPane.getTab(index);
			if (tab == null) throw new RuntimeException("Tab null");
			Page page = tab.getVisiblePage();
			if (page == null) {
				throw new RuntimeException("Page null");
			}
			tabbedPane.setTitleAt(index, page.getTitle());
		}
	}
	
	private void updateLoginAction() {
		loginAction.setEnabled(subject == null);
		logoutAction.setEnabled(subject != null);
	}
	
	private class SwingLoginAction extends SwingResourceAction {
		private static final long serialVersionUID = 1L;

		public SwingLoginAction() {
			super("LoginAction");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			new LoginAction().action();
		}
	}
	
	private class SwingLogoutAction extends SwingResourceAction {
		private static final long serialVersionUID = 1L;

		public SwingLogoutAction() {
			super("LogoutAction");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Backend.getInstance().execute(new LogoutTransaction());
			getVisibleTab().setSubject(null);

		}
	}
	
	private class CloseWindowAction extends SwingResourceAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			tryToCloseWindow();
		}
	}
	
	private class ExitAction extends SwingResourceAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			FrameManager.getInstance().exitActionPerformed(SwingFrame.this);
		}
	}

	private static class NewWindowAction extends SwingResourceAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			FrameManager.getInstance().openNavigationFrame();
		}
	}
	
	private class NewTabAction extends SwingResourceAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			addTab();
		}
	}
	
	private class CloseTabAction extends SwingResourceAction {
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			closeTab();
		}
	}

}
