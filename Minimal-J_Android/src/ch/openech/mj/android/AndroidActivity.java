package ch.openech.mj.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import ch.openech.mj.resources.Resources;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IDialog;
import ch.openech.mj.toolkit.IDialog.CloseListener;

public class AndroidActivity extends Activity implements PageContext {

	private static final String SEARCH_KEY_PREFIX = "Search.";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((AndroidClientToolkit) ClientToolkit.getToolkit()).setCtx(this);
		setTitle(MjApplication.getApplication().getWindowTitle(this));
		setContentView(R.layout.main_activity);

		final Spinner searchComboBox = (Spinner) findViewById(R.id.main_search_combo);
		List<SearchEntity> searchEntities = createSearchEntities(MjApplication
				.getApplication().getSearchClasses());
		searchComboBox.setAdapter(new ArrayAdapter<SearchEntity>(this,
				android.R.layout.simple_dropdown_item_1line, searchEntities));
		Button buttonSearch = (Button) findViewById(R.id.main_button_search);
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

	}

	public void search(Class<? extends Page> searchClass, String text) {
		show(PageLink.link(searchClass, text));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		Menu newMenu = menu.findItem(R.id.menu_new).getSubMenu();
		newMenu.clear();
		for (final IAction action : MjApplication.getApplication()
				.getActionsNew(this)) {
			MenuItem newItem = newMenu.add(action.getName());
			newItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					action.action(AndroidActivity.this);
					return false;
				}
			});
		}

		return true;
	}

	@Override
	public ApplicationContext getAppContext() {
		return ((AndroidApplication) getApplication()).getAppContext();
	}

	@Override
	public void show(String pageLink) {
		Page page = PageLink.createPage(this, pageLink);
		clearMainPanel();
		getMainPanel().addView((View) page.getComponent());
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

}
