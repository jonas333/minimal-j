package org.minimalj.example.adventureworks.model;

import java.math.BigDecimal;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.Size;
import org.minimalj.model.validation.Validation;
import org.minimalj.model.validation.ValidationMessage;

public class Address {
	public static final Address $ = Keys.of(Address.class);
	
	public Object id;
	
	@Size(60)
	public String addressLine1, addressLine2;
	@Size(30)
	public String city;
	public StateProvince stateProvince;
	@Size(15)
	public String postalCode;
	public final Geography spatialLocation = new Geography();

	public static class Geography implements Validation {
		public static final Geography $ = Keys.of(Geography.class);
		
		public static final BigDecimal MAX = new BigDecimal(180.0);
		public BigDecimal latitude, longitude;

		@Override
		public void validate(List<ValidationMessage> validationResult) {
			if (latitude != null) {
				if (latitude.abs().compareTo(MAX) > 0) {
					validationResult.add(new ValidationMessage($.latitude, "Must be <= 180"));
				}
			}
			if (longitude != null) {
				if (longitude.abs().compareTo(MAX) > 0) {
					validationResult.add(new ValidationMessage($.longitude, "Must be <= 180"));
				}
			}
		}
	}

}
