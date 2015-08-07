package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.minimalj.model.Keys;

public class CurrencyRate {
	public static final CurrencyRate $ = Keys.of(CurrencyRate.class);
	
	public Object id;
	
	public LocalDate currencyRateDate;
	public Currency fromCurrency, toCurrency;
	public BigDecimal averageRate, endOfDayRate;

}
