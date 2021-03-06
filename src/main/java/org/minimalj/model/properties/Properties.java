package org.minimalj.model.properties;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import org.minimalj.model.Keys;
import org.minimalj.repository.sql.EmptyObjects;
import org.minimalj.util.CloneHelper;
import org.minimalj.util.FieldUtils;

public class Properties {
	private static final Logger logger = Logger.getLogger(Properties.class.getName());

	private static final Map<Class<?>, Map<String, PropertyInterface>> properties = 
			new HashMap<Class<?>, Map<String, PropertyInterface>>();
	
	public static PropertyInterface getProperty(Class<?> clazz, String propertyName) {
		Objects.requireNonNull(propertyName);

		Map<String, PropertyInterface> propertiesForClass = getProperties(clazz);
		PropertyInterface property = propertiesForClass.get(propertyName);

		if (property == null) {
			property = Keys.getMethodProperty(clazz, propertyName);
		}
		
		if (property != null) {
			return property;
		} else {
			logger.fine("No field/access methods for " + propertyName + " in Class " + clazz.getName());
			return null;
		}
	}

	public static PropertyInterface getPropertyByPath(Class<?> clazz, String propertyName) {
		int pos = propertyName.indexOf('.');
		if (pos < 0) {
			return getProperty(clazz, propertyName);
		} else {
			PropertyInterface property1 = getProperty(clazz, propertyName.substring(0, pos));
			PropertyInterface property2 = getPropertyByPath(property1.getClazz(), propertyName.substring(pos + 1));
			return new ChainedProperty(property1, property2);
		}
	}
	
	public static PropertyInterface getProperty(Field field) {
		return getProperty(field.getDeclaringClass(), field.getName());
	}
	
	public static Map<String, PropertyInterface> getProperties(Class<?> clazz) {
		if (!properties.containsKey(clazz)) {
			properties.put(clazz, Collections.unmodifiableMap(properties(clazz)));
		}
		Map<String, PropertyInterface> propertiesForClass = properties.get(clazz);
		return propertiesForClass;
	}
	
	private static Map<String, PropertyInterface> properties(Class<?> clazz) {
		Map<String, PropertyInterface> properties = new LinkedHashMap<String, PropertyInterface>();
		
		for (Field field : clazz.getFields()) {
			if (!FieldUtils.isStatic(field)) {
				properties.put(field.getName(), new FieldProperty(field));
			} 
		}
		return properties; 
	}

	public static void setAndRestructure(PropertyInterface property, Object object, Object value) {
		boolean empty = EmptyObjects.isEmpty(value);
		String path = property.getPath();
		if (!empty) {
			int index = path.indexOf('.');
			if (index > -1) {
				PropertyInterface p1 = Properties.getProperty(object.getClass(), path.substring(0, index));
				Object fieldObject = populate(object, p1);

				PropertyInterface p2 = Properties.getPropertyByPath(p1.getClazz(), path.substring(index + 1, path.length()));
				setAndRestructure(p2, fieldObject, value);
			} else {
				property.setValue(object, value);
			}
		} else {
			int index = path.lastIndexOf('.');
			if (index > -1) {
				String parentPath = path.substring(0, index);
				PropertyInterface p1 = Properties.getPropertyByPath(object.getClass(), parentPath);
				Object fieldObject = p1.getValue(object);
				if (fieldObject == null) {
					return;
				}
				
				PropertyInterface p2 = Properties.getProperty(p1.getClazz(), path.substring(index + 1));
				clear(fieldObject, p2);
				
				if (EmptyObjects.isEmpty(fieldObject)) {
					setAndRestructure(p1, object, null);
				}
			} else {
				clear(object, property);
			}
		}
	}

	private static Object populate(Object object, PropertyInterface property) {
		Object value = property.getValue(object);
		if (value == null) {
			value = CloneHelper.newInstance(property.getClazz());
			property.setValue(object, value);
		}
		return value;
	}
	
	private static void clear(Object object, PropertyInterface property) {
		if (property.isFinal()) {
			CloneHelper.deepCopy(EmptyObjects.getEmptyObject(property.getClazz()), property.getValue(object));
		} else {
			property.setValue(object, null);
		}
	}
	
}
