package org.minimalj.example.currencies.frontend;

import java.math.BigDecimal;

import org.minimalj.example.currencies.model.CurrencyCalculation3;
import org.minimalj.frontend.form.Form;
import org.minimalj.frontend.form.element.TextFormElement;

public class CalculationEditor3 extends Calculator<CurrencyCalculation3> {

	@Override
	protected Form<CurrencyCalculation3> createForm() {
		Form<CurrencyCalculation3> form = new Form<>(2);
		form.line(CurrencyCalculation3.$.from, CurrencyCalculation3.$.to);
		TextFormElement toValueElement = new TextFormElement(CurrencyCalculation3.$.toValue);
		form.line(CurrencyCalculation3.$.fromValue, toValueElement);

		return form;
	}
	
	@Override
	protected void calculate(CurrencyCalculation3 cc) {
		cc.toValue = BigDecimal.valueOf(Math.random() * 300);
	}
}
