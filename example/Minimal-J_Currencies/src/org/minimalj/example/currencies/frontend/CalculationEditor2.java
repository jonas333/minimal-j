package org.minimalj.example.currencies.frontend;

import java.math.BigDecimal;

import org.minimalj.example.currencies.model.CurrencyCalculation2;
import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.form.Form;

public class CalculationEditor2 extends Editor<CurrencyCalculation2, BigDecimal> {

	@Override
	protected CurrencyCalculation2 createObject() {
		return new CurrencyCalculation2();
	}
	
	@Override
	protected Form<CurrencyCalculation2> createForm() {
		Form<CurrencyCalculation2> form = new Form<>(2);
		form.line(CurrencyCalculation2.$.from, CurrencyCalculation2.$.to);
		form.line(CurrencyCalculation2.$.fromValue);

		return form;
	}
	
	@Override
	protected BigDecimal save(CurrencyCalculation2 cc) {
		// get exchange rate and calculate!
		return BigDecimal.valueOf(Math.random() * 300);
	}
	
	@Override
	protected void finished(BigDecimal result) {
		Frontend.showMessage("Wert " + result.toPlainString());
	}
}
