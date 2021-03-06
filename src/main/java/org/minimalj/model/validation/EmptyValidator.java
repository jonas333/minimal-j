package org.minimalj.model.validation;

import java.text.MessageFormat;
import java.util.List;

import org.minimalj.model.Keys;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.properties.Properties;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.repository.sql.EmptyObjects;
import org.minimalj.util.StringUtils;
import org.minimalj.util.resources.Resources;

public class EmptyValidator {

	public static void validate(Object object, List<ValidationMessage> resultList) {
		for (PropertyInterface property : Properties.getProperties(object.getClass()).values()) {
			boolean required = property.getAnnotation(NotEmpty.class) != null;
			if (required) {
				validate(resultList, object, property);
			}
		}
		
	}

	public static void validate(List<ValidationMessage> resultList, Object object, Object key) {
		validate(resultList, object, Keys.getProperty(key));
	}
	
	public static void validate(List<ValidationMessage> resultList, Object object, PropertyInterface property) {
		Object value = property.getValue(object);
		if (EmptyObjects.isEmpty(value)) {
			resultList.add(new ValidationMessage(property, createMessage(property)));
		}
	}
	
	public static String createMessage(PropertyInterface property) {
		String caption = Resources.getPropertyName(property);
		if (StringUtils.isEmpty(caption)) {
			return Resources.getString("EmptyValidator.messageNoCaption");
		} else {
			return MessageFormat.format(Resources.getString("EmptyValidator.message"), caption);
		}
	}
}
