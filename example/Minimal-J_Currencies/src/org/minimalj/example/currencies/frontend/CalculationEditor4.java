package org.minimalj.example.currencies.frontend;

import java.math.BigDecimal;

import org.minimalj.example.currencies.model.CurrencyCalculation3;
import org.minimalj.frontend.editor.Editor.NewObjectEditor;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.TextFormElement;

public class CalculationEditor4 extends NewObjectEditor<CurrencyCalculation3> {

	private Form<CurrencyCalculation3> form;
	
	@Override
	protected Form<CurrencyCalculation3> createForm() {
		form = new Form<>(2);
		form.line(CurrencyCalculation3.$.from, CurrencyCalculation3.$.to);
		TextFormElement toValueElement = new TextFormElement(CurrencyCalculation3.$.toValue);
		form.line(CurrencyCalculation3.$.fromValue, toValueElement);

		return form;
	}

	@Override
	protected CurrencyCalculation3 save(CurrencyCalculation3 cc) {
		cc.toValue = BigDecimal.valueOf(Math.random() * 300);
		return cc;
	}
	
	@Override
	protected boolean closeWith(CurrencyCalculation3 result) {
		form.setObject(result);
		return false;
	}
}
