package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class Product {
	public static final Product $ = Keys.of(Product.class);
	
	public Object id;
	
	@Size(AdventureWorksFormats.Name) @Required
	public String name;
	
	@Size(25) @Required
	public String productNumber;
	
	public Boolean make;
	
	public Boolean finishedGoods;
	
	@Size(15)
	public String color;
	
	public Integer safetyStockLelvel;

	public Integer reorderPoint;

	public ProductSubcategory productSubcategory;
	
	public final List<ProductCostHistory> costHistory = new ArrayList<>();

	public final List<ProductListPriceHistory> listPriceHistory = new ArrayList<>();

	public ProductDescription productDescription;
	
	public static class ProductCostHistory {
		@Required
		public LocalDate startDate;
		public LocalDate endDate;

		public BigDecimal standardCost;
	}
	
	public static class ProductListPriceHistory {
		@Required
		public LocalDate startDate;
		public LocalDate endDate;

		public BigDecimal listPrice;
	}
	
	public static class ProductDescription {
		@Required @Size(400)
		public String description;
	}
	
	public static class ProductModel {
		@Size(AdventureWorksFormats.Name) @Required
		public String name;
		@Size(4000) 
		public String catalogDescription, instructions;
	}
}
