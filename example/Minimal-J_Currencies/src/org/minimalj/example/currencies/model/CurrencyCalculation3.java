package org.minimalj.example.currencies.model;

import java.math.BigDecimal;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

public class CurrencyCalculation3 {

	public static final CurrencyCalculation3 $ = Keys.of(CurrencyCalculation3.class);
	
	public Currency from, to;
	
	@NotEmpty
	public BigDecimal fromValue;

	public BigDecimal toValue;
}
