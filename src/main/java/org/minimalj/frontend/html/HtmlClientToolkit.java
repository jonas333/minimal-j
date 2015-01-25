package org.minimalj.frontend.html;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.minimalj.application.ApplicationContext;
import org.minimalj.frontend.html.toolkit.HtmlCheckBox;
import org.minimalj.frontend.html.toolkit.HtmlFlowField;
import org.minimalj.frontend.html.toolkit.HtmlFormContent;
import org.minimalj.frontend.html.toolkit.HtmlLabel;
import org.minimalj.frontend.html.toolkit.HtmlReadOnlyTextField;
import org.minimalj.frontend.html.toolkit.HtmlTable;
import org.minimalj.frontend.toolkit.CheckBox;
import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ComboBox;
import org.minimalj.frontend.toolkit.FlowField;
import org.minimalj.frontend.toolkit.FormContent;
import org.minimalj.frontend.toolkit.IAction;
import org.minimalj.frontend.toolkit.IDialog;
import org.minimalj.frontend.toolkit.ITable;
import org.minimalj.frontend.toolkit.ITable.TableActionListener;
import org.minimalj.frontend.toolkit.SwitchComponent;
import org.minimalj.frontend.toolkit.TextField;

public class HtmlClientToolkit extends ClientToolkit {

	@Override
	public FormContent createFormContent(int columns, int columnWidth) {
		return new HtmlFormContent(columns, columnWidth);
	}

	@Override
	public IComponent createLabel(String string) {
		return new HtmlLabel(string);
	}

	@Override
	public IComponent createLabel(IAction action) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IComponent createTitle(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextField createReadOnlyTextField() {
		return new HtmlReadOnlyTextField();
	}

	@Override
	public TextField createTextField(InputComponentListener changeListener, int maxLength) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextField createTextField(Boolean multiLine, InputComponentListener changeListener, int maxLength, String allowedCharacters,
			InputType inputType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlowField createFlowField() {
		return new HtmlFlowField();
	}

	@Override
	public <T> ComboBox<T> createComboBox(InputComponentListener changeListener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CheckBox createCheckBox(InputComponentListener changeListener, String text) {
		return new HtmlCheckBox();
	}

	@Override	
	public IComponent createLink(String text, String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ILookup<T> createLookup(InputComponentListener changeListener, Search<T> index, Object[] keys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IComponent createHorizontalLayout(IComponent... components) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SwitchComponent createSwitchComponent(IComponent... components) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SwitchContent createSwitchContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> ITable<T> createTable(Object[] fields) {
		return new HtmlTable<T>(fields);
	}

	@Override
	public void show(String pageLink) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show(List<String> pageLinks, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ApplicationContext getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDialog createDialog(String title, IContent content, IAction... actions) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> IDialog createSearchDialog(Search<T> index, Object[] keys, TableActionListener<T> listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showMessage(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showError(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showConfirmDialog(String message, String title, ConfirmDialogType type, DialogListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OutputStream store(String buttonText) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream load(String buttonText) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
