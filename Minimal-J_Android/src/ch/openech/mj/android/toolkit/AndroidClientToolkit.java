package ch.openech.mj.android.toolkit;

import java.io.InputStream;

import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ScrollView;
import ch.openech.mj.android.AndroidHelper;
import ch.openech.mj.search.Lookup;
import ch.openech.mj.search.Search;
import ch.openech.mj.toolkit.Caption;
import ch.openech.mj.toolkit.CheckBox;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.ExportHandler;
import ch.openech.mj.toolkit.FlowField;
import ch.openech.mj.toolkit.GridFormLayout;
import ch.openech.mj.toolkit.HorizontalLayout;
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IDialog;
import ch.openech.mj.toolkit.ITable;
import ch.openech.mj.toolkit.ITable.TableActionListener;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;

public class AndroidClientToolkit extends ClientToolkit {
	
	private Activity ctx;
	
	public AndroidClientToolkit(Activity ctx) {
		super();
		this.ctx = ctx;
	}

	@Override
	public IComponent createLabel(String text) {
		return new AndroidLabel(ctx, text);
	}
	
	@Override
	public IComponent createLabel(IAction action) {
		return new AndroidLabel(ctx, action);
	}
	
	@Override
	public CheckBox createCheckBox(InputComponentListener listener, String text) {
		return new AndroidCheckBox(ctx, listener, text);
	}

	@Override
	public <T> ComboBox<T> createComboBox(InputComponentListener listener) {
		return new AndroidComboBox<T>(ctx, listener);
	}

	@Override
	public IDialog createDialog(IComponent parent, String title, IComponent content,
			IAction... actions) {
		AndroidDialog dlg = new AndroidDialog();
		dlg.setTitle(title);
		dlg.setContent(content);
		dlg.setFragmentManager(ctx.getFragmentManager());
		return dlg;
	}

	@Override
	public FlowField createFlowField() {
		return new AndroidFlowField(ctx);
	}

	@Override
	public IComponent createFormAlignLayout(IComponent component) {
		return new AndroidScrollView(ctx, component);
	}

	@Override
	public GridFormLayout createGridLayout(int columns, int columnWidth) {
		return new AndroidGridLayout(ctx, columns);
	}

	@Override
	public HorizontalLayout createHorizontalLayout(IComponent... components) {
		return new AndroidHorizontalLayout(ctx, components);
	}


	@Override
	public IComponent createLink(String text, String address) {
		return new AndroidLink(ctx, text, address);
	}

	@Override
	public TextField createReadOnlyTextField() {
		return new AndroidReadonlyTextField(ctx);
	}


	@Override
	public SwitchLayout createSwitchLayout() {
		return new AndroidSwitchLayout(ctx);
	}


	@Override
	public TextField createTextField(InputComponentListener changeListener, int maxLength) {
		return new AndroidTextField(ctx, changeListener, maxLength);
	}

	@Override
	public TextField createTextField(InputComponentListener changeListener, int maxLength,
			String allowedCharacters) {
		return new AndroidTextField(ctx, changeListener, maxLength, allowedCharacters);
	}

	@Override
	public IComponent createTitle(String title) {
		return new AndroidTitle(ctx, title);
	}

	@Override
	public Caption decorateWithCaption(IComponent component, String text) {
		return new AndroidCaption(ctx, text, (View) component);
	}

	@Override
	public void export(IComponent arg0, String arg1, ExportHandler arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public InputStream imprt(IComponent arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showConfirmDialog(IComponent component, String title, String message,
			ConfirmDialogType dialogType, DialogListener listener) {
		AndroidHelper.createConfirmationDialog(ctx, title, message, dialogType, listener).show();
	}

	@Override
	public void showError(Object parent, String title) {
		AndroidHelper.createAlertDialog(ctx, "Fehler", title).show();
	}

	@Override
	public void showMessage(Object parent, String message) {
		AndroidHelper.createAlertDialog(ctx, "Information", message).show();
	}

	@Override
	public <T> ITable<T> createTable(Lookup<T> lookup, Object[] keys) {
		return new AndroidTable<T>(ctx, lookup, keys);
	}

	@Override
	public <T> IDialog createSearchDialog(IComponent parent, Search<T> search,
			Object[] keys, TableActionListener listener) {
		return createDialog(null, "Suchen", new AndroidSearchPanel<T>(ctx, search, keys, listener), (IAction[]) null);
	}

}
