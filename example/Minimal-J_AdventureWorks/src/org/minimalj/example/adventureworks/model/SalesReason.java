package org.minimalj.example.adventureworks.model;

import org.minimalj.model.Code;
import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Sizes;

@Sizes(AdventureWorksFormats.class)
public class SalesReason implements Code {
	public static final SalesReason $ = Keys.of(SalesReason.class);
	
	public Integer id;
	
	public String name;
	
	public SalesReasonType reasonType;

	public static enum SalesReasonType {
		Promotion, Marketing, Other;
	}
	
}
