package org.minimalj.frontend.edit.fields;

import org.minimalj.frontend.toolkit.ClientToolkit;
import org.minimalj.frontend.toolkit.ClientToolkit.IComponent;
import org.minimalj.frontend.toolkit.ClientToolkit.InputType;
import org.minimalj.frontend.toolkit.IFocusListener;
import org.minimalj.frontend.toolkit.TextField;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.model.validation.InvalidValues;
import org.minimalj.model.validation.Validatable;
import org.minimalj.util.mock.Mocking;

public abstract class TextFormatField<T> extends AbstractEditField<T> implements Enable, Mocking {

	protected final TextField textField;

	public TextFormatField(PropertyInterface property) {
		this(property, false);
	}

	public TextFormatField(PropertyInterface property, boolean editable) {
		super(property, editable);
		if (editable) {
			textField = ClientToolkit.getToolkit().createTextField(false, listener(), getAllowedSize(property), getAllowedCharacters(property),
					getInputType());
			installFocusLostListener();
		} else {
			textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		}
	}

	protected abstract String getAllowedCharacters(PropertyInterface property);

	protected abstract int getAllowedSize(PropertyInterface property);

	protected InputType getInputType() {
		return InputType.FREE;
	}

	@Override
	public IComponent getComponent() {
		return textField;
	}

	public abstract T getObject();

	public abstract void setObject(T value);

	private void installFocusLostListener() {
		textField.setFocusListener(new IFocusListener() {

			@Override
			public void onFocusLost() {
				// Formattierung auslösen
				T value = getObject();
				boolean valid = true;
				valid &= !InvalidValues.isInvalid(value);
				valid &= !(value instanceof Validatable) || ((Validatable) value).validate() == null;

				if (valid) {
					setObject(value);
				}

			}
		});
	}

	public void setEnabled(boolean enabled) {
		textField.setEditable(enabled);
	}

}