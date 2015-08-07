package org.minimalj.example.adventureworks.model;

import java.util.ArrayList;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;
import org.minimalj.model.annotation.Required;
import org.minimalj.model.annotation.Size;

public class ShoppingCart {
	public static final ShoppingCart $ = Keys.of(ShoppingCart.class);
	
	public Object id;
	
	public final List<ShoppingCartItem> items = new ArrayList<>();


	public static class ShoppingCartItem {
		public static final ShoppingCartItem $ = Keys.of(ShoppingCartItem.class);
		
		@Required @Size(5)
		public Integer quantity;
		
		@Required @Reference
		public Product product;
	}
}
