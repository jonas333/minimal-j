package org.minimalj.example.currencies.frontend;

import org.minimalj.example.currencies.model.CurrencyCalculation;
import org.minimalj.frontend.editor.Editor.SimpleEditor;
import org.minimalj.frontend.form.Form;

public class CalculationEditor extends SimpleEditor<CurrencyCalculation> {

	@Override
	protected CurrencyCalculation createObject() {
		return new CurrencyCalculation();
	}

	@Override
	protected Form<CurrencyCalculation> createForm() {
		Form<CurrencyCalculation> form = new Form<>(2);
		form.line(CurrencyCalculation.$.from, CurrencyCalculation.$.to);
		form.line(CurrencyCalculation.$.fromValue, CurrencyCalculation.$.getToValue());
		
		form.addDependecy(CurrencyCalculation.$.from, CurrencyCalculation.$.getToValue());
		form.addDependecy(CurrencyCalculation.$.to, CurrencyCalculation.$.getToValue());
		form.addDependecy(CurrencyCalculation.$.fromValue, CurrencyCalculation.$.getToValue());

		return form;
	}
}
