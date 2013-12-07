package ch.openech.mj.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import ch.openech.mj.android.toolkit.AndroidClientToolkit;
import ch.openech.mj.application.ApplicationContext;
import ch.openech.mj.application.MjApplication;
import ch.openech.mj.edit.Editor;
import ch.openech.mj.edit.Editor.EditorListener;
import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.page.Page;
import ch.openech.mj.page.PageContext;
import ch.openech.mj.page.PageLink;
import ch.openech.mj.resources.ResourceHelper;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IDialog;
import ch.openech.mj.toolkit.IDialog.CloseListener;

public class AndroidActivity extends Activity {

	private static final String KEY_MENU_NEW_TEXT = "Menu.new.text";
	private static final String SEARCH_KEY_PREFIX = "Search.";
	private PageContextDelegate pageContext = new PageContextDelegate();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
<<<<<<< HEAD
		setContentView(R.layout.main_activity);
		init();
	}

	private void init() {
=======
		
		StrictMode.ThreadPolicy policy = 
		        new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
>>>>>>> refs/remotes/origin/android
		((AndroidClientToolkit) ClientToolkit.getToolkit()).setCtx(this);
<<<<<<< HEAD
		setTitle(MjApplication.getApplication().getWindowTitle(this));
=======
		setTitle(MjApplication.getApplication().getWindowTitle(pageContext));
		setContentView(R.layout.main_activity);
>>>>>>> refs/remotes/origin/android

		final Spinner searchComboBox = (Spinner) findViewById(R.id.main_search_combo);
		List<SearchEntity> searchEntities = createSearchEntities(MjApplication
				.getApplication().getSearchClasses());
		searchComboBox.setAdapter(new ArrayAdapter<SearchEntity>(this,
				android.R.layout.simple_dropdown_item_1line, searchEntities));
		Button buttonSearch = (Button) findViewById(R.id.main_button_search);
		buttonSearch.setText(ResourceHelper.getString(Resources.getResourceBundle(), AndroidHelper.KEY_SEARCH));
		buttonSearch.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				SearchEntity searchEntity = (SearchEntity) searchComboBox
						.getSelectedItem();
				String searchText = ((TextView) findViewById(R.id.main_search_text))
						.getText().toString();
				search((Class<? extends Page>) searchEntity.getClazz(),
						searchText);
			}
		});		
		EditText searchText = (EditText) findViewById(R.id.main_search_text);
		searchText.setHint(ResourceHelper.getString(Resources.getResourceBundle(), AndroidHelper.KEY_SEARCH_HINT));
	}
	
	
	public void search(Class<? extends Page> searchClass, String text) {
		pageContext.show(PageLink.link(searchClass, text));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		
		MenuItem newMenu = menu.findItem(R.id.menu_new);
		newMenu.setTitle(ResourceHelper.getString(Resources.getResourceBundle(), KEY_MENU_NEW_TEXT));
		Menu subMenu = newMenu.getSubMenu();
		subMenu.clear();
		for (final IAction action : MjApplication.getApplication()
<<<<<<< HEAD
				.getActionsNew(null)) {
			MenuItem newItem = subMenu.add(action.getName());
=======
				.getActionsNew(pageContext)) {
			MenuItem newItem = newMenu.add(action.getName());
>>>>>>> refs/remotes/origin/android
			newItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					action.action(pageContext);
					return false;
				}
			});
		}

		return true;
	}

	

	private void clearMainPanel() {
		ViewGroup mainPanel = getMainPanel();
		if (mainPanel.getChildCount() > 1) {
			mainPanel.removeViewAt(1);
		}
	}

	private ViewGroup getMainPanel() {
		return (ViewGroup) findViewById(R.id.main_panel);
	}


	private List<SearchEntity> createSearchEntities(Class<?>[] classes) {
		List<SearchEntity> result = new ArrayList<SearchEntity>();
		for (Class<?> clazz : classes) {
			result.add(new SearchEntity(clazz, Resources
					.getString(SEARCH_KEY_PREFIX + clazz.getSimpleName())));
		}
		return result;
	}

	private static class SearchEntity {
		private Class<?> clazz;
		private String name;

		public SearchEntity(Class<?> clazz, String name) {
			this.clazz = clazz;
			this.name = name;
		}

		public Class<?> getClazz() {
			return clazz;
		}

		@Override
		public String toString() {
			return name;
		}
	}
	
	private class PageContextDelegate implements PageContext {
		
		
		private ApplicationContext applicationContext = new AndroidApplicationContext();

		@Override
		public ApplicationContext getApplicationContext() {
			return applicationContext;
		}

		@Override
		public void show(String pageLink) {
			Page page = PageLink.createPage(this, pageLink);
			clearMainPanel();
			getMainPanel().addView((View) page.getComponent());			
		}

		@Override
		public void show(final Editor<?> editor) {
			IForm<?> form = editor.startEditor();
			IComponent component = form.getComponent();
			final IDialog dlg = ClientToolkit.getToolkit().createDialog(null,
					editor.getTitle(), component, editor.getActions());
			dlg.setCloseListener(new CloseListener() {

				@Override
				public boolean close() {
					editor.checkedClose();
					return editor.isFinished();
				}
			});
			editor.setEditorListener(new EditorListener() {

				@Override
				public void canceled() {
					dlg.closeDialog();
				}

				@Override
				public void saved(Object saveResult) {
					dlg.closeDialog();
					if (saveResult instanceof String) {
						show((String) saveResult);
					}
				}
			});
			dlg.openDialog();			
		}

		@Override
		public void show(List<String> arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	

}
