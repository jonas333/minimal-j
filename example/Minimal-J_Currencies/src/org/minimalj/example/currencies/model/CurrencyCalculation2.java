package org.minimalj.example.currencies.model;

import java.math.BigDecimal;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;

public class CurrencyCalculation2 {

	public static final CurrencyCalculation2 $ = Keys.of(CurrencyCalculation2.class);
	
	@NotEmpty
	public Currency from, to;
	
	@NotEmpty
	public BigDecimal fromValue;
}
