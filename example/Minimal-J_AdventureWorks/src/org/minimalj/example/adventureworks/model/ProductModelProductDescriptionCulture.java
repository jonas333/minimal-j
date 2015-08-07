package org.minimalj.example.adventureworks.model;

import org.minimalj.example.adventureworks.model.Product.ProductDescription;
import org.minimalj.example.adventureworks.model.Product.ProductModel;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;

public class ProductModelProductDescriptionCulture {
	public static final ProductModelProductDescriptionCulture $ = Keys.of(ProductModelProductDescriptionCulture.class);
	
	public Object id;
	
	@Reference
	public ProductModel productModel;
	
	@Reference
	public ProductDescription productDescription;

	@Reference
	public Culture culture;

}
