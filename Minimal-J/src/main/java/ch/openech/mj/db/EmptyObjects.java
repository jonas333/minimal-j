package ch.openech.mj.db;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.LocalDate;
import org.joda.time.ReadablePartial;

import ch.openech.mj.edit.value.EqualsHelper;
import ch.openech.mj.model.EnumUtils;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.model.properties.FlatProperties;

@SuppressWarnings("unchecked")
public class EmptyObjects {
	private static final String EMPTY_STRING = "";
	private static Map<Class<?>, Object> emptyObjects = new HashMap<Class<?>, Object>();

	public static <T> boolean isEmpty(T object) {
		if (object != null && !object.equals(EMPTY_STRING)) {
			Class<T> clazz = (Class<T>) object.getClass();
			if (clazz.getName().startsWith("java") || clazz.getName().startsWith("org.joda") || //
					Enum.class.isAssignableFrom(clazz)) return false;
			
			T emptyObject = getEmptyObject(clazz);
			return EqualsHelper.equals(emptyObject, object);
		} else {
			return true;
		}
	}
	
	public static <T> T getEmptyObject(Class<T> clazz) {
		if (!emptyObjects.containsKey(clazz)) {
			try {
				Object emptyObject = clazz.newInstance();
				fill(emptyObject);
				emptyObjects.put(clazz, emptyObject);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return (T) emptyObjects.get(clazz);
	}

	private static void fill(Object emptyObject) {
		for (PropertyInterface property : FlatProperties.getProperties(emptyObject.getClass()).values()) {
			if (property.getFieldClazz() == String.class) {
				property.setValue(emptyObject, EMPTY_STRING);
			} else if (Enum.class.isAssignableFrom(property.getFieldClazz())) {
				property.setValue(emptyObject, EnumUtils.getDefault((Class<Enum>) property.getFieldClazz())); 
			} else if (property.getFieldClazz() == LocalDate.class || property.getFieldClazz() == ReadablePartial.class) {
				property.setValue(emptyObject, new LocalDate());
			} 
			if (isReference(property)) {
				if (property.getFieldClazz() == emptyObject.getClass()) {
					property.setValue(emptyObject, emptyObject);
				} else {
					property.setValue(emptyObject, getEmptyObject(property.getFieldClazz()));
				}
			}
		}
	}
	
	public static boolean isReference(PropertyInterface property) {
		if (property.getFieldClazz().getName().startsWith("java")) return false;
		if (property.getFieldClazz().getName().startsWith("org.joda")) return false;
		if (Enum.class.isAssignableFrom(property.getFieldClazz())) return false;
		return true;
	}

}
