package ch.openech.mj.android;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.joda.time.LocalDate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ch.openech.mj.android.toolkit.AndroidCaption;
import ch.openech.mj.android.toolkit.AndroidClientToolkit;
import ch.openech.mj.android.toolkit.AndroidDummyAction;
import ch.openech.mj.example.model.Book;
import ch.openech.mj.example.page.BookTablePage;
import ch.openech.mj.search.Search;
import ch.openech.mj.toolkit.CheckBox;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ClientToolkit.ConfirmDialogType;
import ch.openech.mj.toolkit.ClientToolkit.DialogListener;
import ch.openech.mj.toolkit.ClientToolkit.InputComponentListener;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.FlowField;
import ch.openech.mj.toolkit.GridFormLayout;
import ch.openech.mj.toolkit.HorizontalLayout;
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IFocusListener;
import ch.openech.mj.toolkit.ITable;
import ch.openech.mj.toolkit.ITable.TableActionListener;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;

public class WidgetDemoActivity extends Activity {
	
	private final String[] WIDGETS = { "Label", "ActionLabel", "Title", "Link",
			"TextField", "ReadOnlyTextField", "ComboBox", "CheckBox",
			"ComponentAndCaption", "ErrorDialog", "MessageDialog",
			"ConfirmationDialog", "Dialog", "Table", "FlowLayout", "GridLayout", "HorizontalLayout",
			"SwitchLayout", "FormAlignLayout", "SearchPanel"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		((AndroidClientToolkit) ClientToolkit.getToolkit()).setCtx(this);
		setContentView(R.layout.widget_demo_activity);
		ListView listView = (ListView) findViewById(R.id.mainList);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, Arrays.asList(WIDGETS));
		listView.setAdapter(adapter);
		listView.setItemsCanFocus(false);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (view instanceof TextView) {
					String title = ((TextView) view).getText().toString();
					switch (position) {
					case 0:
						showLabel(title);
						break;
					case 1:
						showActionLabel(title);
						break;
					case 2:
						showTitle(title);
						break;
					case 3:
						showLink(title);
						break;
					case 4:
						showTextField(title);
						break;
					case 5:
						showReadOnlyTextfield(title);
						break;
					case 6:
						showComboBox(title);
						break;
					case 7:
						showCheckBox(title);
						break;
					case 8:
						showComponentAndCaption(title);
						break;
					case 9:
						showErrorDialog(title);
						break;
					case 10:
						showMessageDialog(title);
						break;
					case 11:
						showConfirmationDialog(title);
						break;
					case 13:
						showTable(title);
						break;
					case 14:
						showFlowField(title);
						break;
					case 15:
						showGrid(title);
						break;
					case 16:
						showHorizontalLayout(title);
						break;
					case 17:
						showSwitchLayout(title);
						break;
					case 18:
						showFormAlignLayout(title);
						break;
					case 19:
						showSearchPanel(title);
						break;
					}
				}
			}
		});
	}

	
	private void showSearchPanel(String title) {
		AndroidClientToolkit.getToolkit().createSearchDialog(null, new DummyLookup(), BookTablePage.FIELDS, null).openDialog();
	}
	
	
	private void showFormAlignLayout(String title) {
		FlowField hl  =ClientToolkit.getToolkit().createFlowField();
		for (int i = 0; i < 9; i++)
		{
			hl.add(ClientToolkit.getToolkit().createLabel("Eintrag" + i));
		}
		IComponent c = ClientToolkit.getToolkit().createFormAlignLayout(hl);
		showDialog(c, title);
	}
	
	private void showSwitchLayout(String title) {
		SwitchLayout sl = ClientToolkit.getToolkit().createSwitchLayout();
		sl.show(ClientToolkit.getToolkit().createTitle("A Title within a switch layout"));
		showDialog(sl, title);
	}

	private void showTable(String title) {
		Integer[] ids = { 1, 2 };
		ITable<Book> table = ClientToolkit.getToolkit().createTable(new DummyLookup(),
				BookTablePage.FIELDS);
		table.setClickListener(new TableActionListener() {
			
			@Override
			public void action(int id, List<Integer> selectedIds) {
				System.out.println("selectedId : " + id + " selectedIds : " + selectedIds);
			}
		});
		table.setIds(Arrays.asList(ids));
		showDialog(table, title);
	}

	private void showComboBox(String title) {

		String[] names = { "Müller", "Schmidt", "Musolf", "Schneider" };
		final ComboBox<String> comboBox = ClientToolkit.getToolkit()
				.createComboBox(null);
		comboBox.setObjects(Arrays.asList(names));

		AndroidLinearLayout comboLayout = new AndroidLinearLayout(this);
		comboLayout.setOrientation(LinearLayout.HORIZONTAL);
		comboLayout.addView((View) comboBox);
		Button bSelect = new Button(this);
		bSelect.setText("Set Selected Musolf");
		bSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				comboBox.setSelectedObject("Musolf");
			}
		});
		comboLayout.addView(bSelect);

		final Button bGetSelect = new Button(this);
		bGetSelect.setText("Get Selected");
		bGetSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getBaseContext(), comboBox.getSelectedObject(), Toast.LENGTH_LONG).show();
			}
		});

		comboLayout.addView(bGetSelect);
		showDialog(comboLayout, title);
	}
	
	
	private void showHorizontalLayout(String title) {
		IComponent[] components = {ClientToolkit.getToolkit().createLabel("a label"), ClientToolkit.getToolkit().createLabel("another label"), ClientToolkit.getToolkit().createLabel("once again a label")};
		HorizontalLayout hl  =ClientToolkit.getToolkit().createHorizontalLayout(components);
		showDialog(hl, title);
	}
	
	private void showGrid(String title) {
		
		GridFormLayout grid = ClientToolkit.getToolkit().createGridLayout(4, 20);
		
		grid.add(ClientToolkit.getToolkit().createLabel("1 zeile, 1 spalte"), 1);
		grid.add(ClientToolkit.getToolkit().createLabel("1 zeile, 2 spalte"), 1);
		grid.add(ClientToolkit.getToolkit().createLabel("1 zeile, 3 spalte"), 1);
		grid.add(ClientToolkit.getToolkit().createLabel("1 zeile, 4 spalte"), 1);
		
		grid.add(ClientToolkit.getToolkit().createLabel("2 zeile, span=2 blablabla"), 2);
		grid.add(ClientToolkit.getToolkit().createLabel("2 zeile, span=2 xxxxxxxxx"), 2);
		
		grid.add(ClientToolkit.getToolkit().createLabel("3 zeile, span=1"), 1);
		grid.add(ClientToolkit.getToolkit().createLabel("3 zeile, span=3 yyyyyyyyyy"), 3);
		
		
		
		showDialog(grid, title);
		
		
	}
	

	private void showCheckBox(String title) {
		InputComponentListener l = new InputComponentListener() {

			@Override
			public void changed(IComponent checkbox) {
				CheckBox cb = (CheckBox) checkbox;
				System.out.println("checked : " + cb.isChecked());
			}
		};
		IComponent checkbox = ClientToolkit.getToolkit().createCheckBox(l,
				"Check me");
		showDialog(checkbox, title);
	}


	private void showConfirmationDialog(String title) {
		ClientToolkit.getToolkit().showConfirmDialog(null, title,
				"Wollen Sie wirklich den Rechner herunterfahren ?",
				ConfirmDialogType.YES_NO_CANCEL, new DialogListener() {
					public void close(Object result) {
						System.out.println(result);
					}
				});
	}

	private void showMessageDialog(String title) {
		ClientToolkit.getToolkit().showMessage(this, title);
	}

	private void showErrorDialog(String title) {
		ClientToolkit.getToolkit().showError(this, title);
	}

	private void showLink(String title) {
		IComponent link = ClientToolkit.getToolkit().createLink("Suchen",
				"http://www.google.de");
		showDialog(link, title);

	}

	private void showTitle(String title) {
		IComponent androidTitle = ClientToolkit.getToolkit().createTitle(
				"Dies ist eine Ueberschrift");
		showDialog(androidTitle, title);

	}

	private void showComponentAndCaption(String title) {
		IComponent et = ClientToolkit.getToolkit().createTextField(null, 10);
		IComponent compWithCaption = new AndroidSampleCaption(this, "Eingabe",
				(View) et);
		showDialog(compWithCaption, title);

	}

	private void showReadOnlyTextfield(String title) {
		TextField readonlyTextField = ClientToolkit.getToolkit().createReadOnlyTextField();
		readonlyTextField.setText("Ein nicht editierbarer Text");
		showDialog(readonlyTextField, title);
	}

	private void showActionLabel(String title) {
		IComponent actionLabel = ClientToolkit.getToolkit().createLabel(
				new AndroidDummyAction());
		showDialog(actionLabel, title);

	}

	private void showTextField(String title) {

		TextField textField = ClientToolkit.getToolkit().createTextField(
				new InputComponentListener() {
					@Override
					public void changed(IComponent component) {
						ClientToolkit.getToolkit().showMessage(null,
								((TextField) component).getText());
					}
				}, 2, "aABb");
		textField.setFocusListener(new IFocusListener() {

			@Override
			public void onFocusGained() {
				System.out.println("onFocusGained()");
			}

			@Override
			public void onFocusLost() {
				System.out.println("onFocusLost()");
			}

		});
		showDialog(textField, title);
	}
	
	
	private void showFlowField(String title) {
		FlowField ff = ClientToolkit.getToolkit().createFlowField();
		ff.add(ClientToolkit.getToolkit().createLabel("A label in a flow field"));
		ff.add(ClientToolkit.getToolkit().createLabel("Another label in a flow field"));
		ff.addGap();
		ff.add(ClientToolkit.getToolkit().createLabel("A label after a gap in a flow field"));
		showDialog(ff, title);
	}

	private void showLabel(String title) {
		IComponent actionLabel = ClientToolkit.getToolkit().createLabel(
				"Android Label");
		showDialog(actionLabel, title);
	}

	private void showDialog(IComponent component, String title) {
		ClientToolkit.getToolkit()
				.createDialog(null, title, component, (IAction[]) null)
				.openDialog();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
        
		
		return true;
	}
	

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		return super.onMenuItemSelected(featureId, item);
	}


	private static class AndroidSampleCaption extends AndroidCaption {

		private static final String[] validationMsg = {
				"Sie haben eine falsche Eingabe gemacht !", "Bitte korrigieren" };
		private boolean toggle = true;

		public AndroidSampleCaption(Context context, String text, View view) {
			super(context, text, view);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			if (toggle) {
				setValidationMessages(Arrays.asList(validationMsg));
			} else {
				setValidationMessages(Collections.<String> emptyList());
			}
			toggle = !toggle;
			return super.onTouchEvent(event);
		}

	}

	private static class AndroidLinearLayout extends LinearLayout implements
			IComponent {

		public AndroidLinearLayout(Context context) {
			super(context);
		}
	}
	
	public class DummyLookup implements Search<Book> {

		@Override
		public Book lookup(int id) {
			if (id == 1) {
				Book book = new Book();
				book.title = "Der dunkle Turm";
				book.author = "Stephen King";
				book.pages = 999;
				book.date = new LocalDate(2009, 1, 1);
				return book;
			} else {
				Book book = new Book();
				book.title = "Nichts ausgelassen";
				book.author = "Heiner Lauterbach";
				book.pages = 234;
				book.date = new LocalDate(2009, 1, 1);
				return book;
			}			
		}

		@Override
		public List<Integer> search(String searchString) {
			Integer[] ids = {1,2};
			return Arrays.asList(ids);
		}
		
	}

	
	

}
