package org.minimalj.example.adventureworks.model;

import org.minimalj.example.adventureworks.model.Product.ProductModel;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;

public class ProductModelIllustration {
	public static final ProductModelIllustration $ = Keys.of(ProductModelIllustration.class);
	
	public Object id;
	
	@Reference
	public ProductModel productModel;
	
	@Reference
	public Illustration illustration;

}
