package org.minimalj.model.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.logging.Logger;

import org.minimalj.model.Keys;
import org.minimalj.model.properties.PropertyInterface;
import org.minimalj.util.FieldUtils;

public class AnnotationUtil {
	private static final Logger logger = Logger.getLogger(AnnotationUtil.class.getName());
	
	public static final boolean OPTIONAL = true;
	
	public static int getSize(PropertyInterface property) {
		return getSize(property, !OPTIONAL);
	}
	
	public static int getSize(PropertyInterface property, boolean optional) {
		Size size = property.getAnnotation(Size.class);
		if (size != null) {
			return size.value();
		}
		
		Sizes sizes = null;
		Class<?> declaringClass = property.getDeclaringClass();
		while (sizes == null && declaringClass != null) {
			sizes = declaringClass.getAnnotation(Sizes.class);
			declaringClass = declaringClass.getEnclosingClass();
		}
		if (sizes == null) {
			sizes = property.getDeclaringClass().getPackage().getAnnotation(Sizes.class);
		}
		if (sizes != null) {
			Class<?> sizeClass = sizes.value();
			for (Field field : sizeClass.getFields()) {
				if (field.getType() != Integer.TYPE) continue;
				if (!FieldUtils.isStatic(field)) continue;
				if (!field.getName().equals(property.getName())) continue;
				try {
					return field.getInt(null);
				} catch (Exception x) {
					throw new RuntimeException(x);
				}
			}
		}
		
		if (property.getClazz() == BigDecimal.class) {
			return Size.BIG_DECIMAL_DEFAULT;
		} else if (property.getClazz() == Integer.class) {
			return Size.INTEGER;
		} else if (property.getClazz() == Long.class) {
			return Size.LONG;
		}
		
		if (optional) {
			return -1;
		} else {
			logger.fine("You must annotate the fields with a @size or the entire class or package with @sizes");
			throw new IllegalArgumentException("Size not specified for " + property.getName() + " on " + property.getDeclaringClass());
		}
	}
		
	public static int getDecimal(PropertyInterface property) {
		Decimal decimal = property.getAnnotation(Decimal.class);
		if (decimal != null) {
			return decimal.value();
		} else {
			return 0;
		}
	}

	public static <T extends Annotation> T get(Class<T> annotationClass, Class<?> clazz, Object key) {
		PropertyInterface property = Keys.getProperty(key);
		return property.getAnnotation(annotationClass);
	}
		
	public static boolean isSigned(PropertyInterface property) {
		return property.getAnnotation(Signed.class) != null;
	}
	

}
