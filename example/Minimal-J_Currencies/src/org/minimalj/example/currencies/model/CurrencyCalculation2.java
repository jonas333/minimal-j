package org.minimalj.example.currencies.model;

import java.math.BigDecimal;

import org.minimalj.model.Keys;

public class CurrencyCalculation2 {

	public static final CurrencyCalculation2 $ = Keys.of(CurrencyCalculation2.class);
	
	public Currency from, to;
	
	public BigDecimal fromValue;
}
