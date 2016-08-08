package org.minimalj.example.currencies.model;

import java.math.BigDecimal;

import org.minimalj.model.Keys;

public class CurrencyCalculation {

	public static final CurrencyCalculation $ = Keys.of(CurrencyCalculation.class);
	
	public Currency from, to;
	
	public BigDecimal fromValue;
	
	public BigDecimal getToValue() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "toValue", BigDecimal.class);
		
		return BigDecimal.valueOf(Math.random() * 300);
	}
}
