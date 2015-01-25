package org.minimalj.frontend.swing;

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
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.page.PageLink;
import org.minimalj.frontend.swing.component.HideableTabbedPane;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.util.StringUtils;

public class SwingFrame extends JFrame implements IComponent {
	private static final long serialVersionUID = 1L;
	
	private HideableTabbedPane tabbedPane;
	final Action closeWindowAction, exitAction, newWindowAction, newTabAction;
	
	public SwingFrame() {
		super();

		closeWindowAction = new CloseWindowAction();
		exitAction = new ExitAction();
		newWindowAction = new NewWindowAction();
		newTabAction = new NewTabAction();
		
		setDefaultSize();
		setLocationRelativeTo(null);
		addWindowListener();
		createContent();
		getRootPane().putClientProperty(SwingFrame.class.getSimpleName(), this);
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
		getContentPane().add(createTabbedPane(), BorderLayout.CENTER);
	}

	private JComponent createTabbedPane() {
		tabbedPane = new HideableTabbedPane();
		addTab();
		return tabbedPane;
	}

	public SwingTab addTab() {
		SwingTab tab = new SwingTab(this);
		
		tabbedPane.addTab("", tab);
		tabbedPane.setSelectedComponent(tab);

		tab.show(PageLink.DEFAULT);

		return tab;
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
	
	public void closeWindow() {
		for (int i = tabbedPane.getTabCount()-1; i>=0; i--) {
			SwingTab tab = (SwingTab)tabbedPane.getTab(i);
			closeTab(tab);
		}
		
		setVisible(false);
	}

	public static Window getActiveWindow() {
		for (Window w : Window.getWindows()) {
			if (w.isActive()) {
				return w;
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
		updateWindowTitle();
		updateTitle();
	}
	
	protected void updateWindowTitle() {
		Page visiblePage = getVisiblePage();
		String title = Application.getApplication().getName();
		if (visiblePage != null) {
			String pageTitle = visiblePage.getTitle();
			if (!StringUtils.isBlank(pageTitle)) {
				title = title + " - " + pageTitle;
			}
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

}