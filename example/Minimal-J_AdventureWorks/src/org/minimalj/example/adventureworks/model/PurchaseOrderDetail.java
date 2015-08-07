package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Reference;

public class PurchaseOrderDetail {
	public static final PurchaseOrderDetail $ = Keys.of(PurchaseOrderDetail.class);
	
	public Object id;
	
	@Reference
	public PurchaseOrderHeader purchaseOrder;


}
