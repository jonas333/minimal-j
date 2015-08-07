package org.minimalj.example.adventureworks.model;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.minimalj.model.Code;

public class CountryRegion implements Code {

	public String id;
	
	public String name;
	
	public List<CountryRegionCurrency> currencies = new ArrayList<>();
	
	public static class CountryRegionCurrency {
		public Currency currency;
	}
}
