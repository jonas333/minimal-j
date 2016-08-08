package org.minimalj.example.currencies.frontend;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.example.currencies.model.Currency;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.form.element.AbstractFormElement;
import org.minimalj.model.Keys;
import org.minimalj.util.Codes;

public class CurrencyFormElement extends AbstractFormElement<Currency> {

	private final List<Currency> currencies;
	private final Input<Currency> comboBox;

	public CurrencyFormElement(Object key) {
		super(Keys.getProperty(key));
		currencies = new ArrayList<>(Codes.get(Currency.class));
		// currencies.sort(comparator);  HIER SORTIEREN
		comboBox = Frontend.getInstance().createComboBox(currencies, listener());
	}
	
	@Override
	public IComponent getComponent() {
		return comboBox;
	}

	@Override
	public Currency getValue() {
		return comboBox.getValue();
	}

	@Override
	public void setValue(Currency value) {
		comboBox.setValue(value);
	}
}