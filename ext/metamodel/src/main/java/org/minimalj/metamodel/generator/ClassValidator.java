package org.minimalj.metamodel.generator;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.minimalj.metamodel.model.MjEntity;
import org.minimalj.metamodel.model.MjModel;
import org.minimalj.metamodel.model.MjProperty;
import org.minimalj.metamodel.model.MjProperty.MjPropertyType;
import org.minimalj.model.EnumUtils;
import org.minimalj.model.annotation.NotEmpty;
import org.minimalj.model.properties.Properties;
import org.minimalj.model.properties.PropertyInterface;

public class ClassValidator {

	private Function<MjEntity, String> classNameGenerator;
	
	public void validate(MjModel model) {
		validate(model.entities);
	}
	
	public void setClassNameGenerator(Function<MjEntity, String> classNameGenerator) {
		this.classNameGenerator = classNameGenerator;
	}
	
	private String createClassName(MjEntity entity) {
		if (classNameGenerator != null) {
			return classNameGenerator.apply(entity);
		} else {
			return entity.getClassName();
		}
	}
	
	public void validate(Collection<MjEntity> entities) {
		for (MjEntity entity : entities) {
			if (!entity.isPrimitiv() ||entity.isEnumeration()) {
				validateEntity(entity);
			}
		}
	}

	private void validateEntity(MjEntity entity) {
		String className = createClassName(entity);
		try {
			Class<?> clazz = Class.forName(entity.packageName + "." + className);
			if (entity.isEnumeration()) {
				validateEnum(clazz, entity);
			} else {
				validate(clazz, entity);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
	}

	private void validateEnum(Class<?> clazz, MjEntity entity) {
		boolean startsWithDigit = entity.values.stream().anyMatch(element -> Character.isDigit(element.charAt(0)));
		List existingValues = (List) EnumUtils.valueList((Class<Enum>) clazz).stream().map(e -> e.toString()).collect(Collectors.toList());
		
		for (String element : entity.values) {
			if (startsWithDigit) element = "_" + ClassGenerator.toEnum(element);
			if (!existingValues.contains(element)) {
				throw new RuntimeException("Missing enum: " + element + " on " + clazz.getName());
			}
		}
	}

	public void validate(Class<?> clazz, MjEntity entity) {
		Set<String> forbiddenNames = new TreeSet<>();
		forbiddenNames.add(clazz.getSimpleName());
		for (MjProperty property : entity.properties) {
			validate(clazz, entity, property, forbiddenNames);
		}
	}

	public void validate(Class<?> clazz, MjEntity entity, MjProperty property, Set<String> forbiddenNames) {
		// System.out.println("validate: " + entity.name + " " + property.name + " against " + clazz.getName());
		PropertyInterface p = Properties.getProperty(clazz, property.name);
		if (p == null) {
			throw new RuntimeException("Missing property: " + property.name + " on " + clazz.getName());
		}
		// note: it's allowed to omit an NotEmpty
		// but it's not allowed to have one if the value is not required in the model
		if (p.getAnnotation(NotEmpty.class) != null) {
			if (!Boolean.TRUE.equals(property.notEmpty)) {
				throw new RuntimeException("Property should be marked as NotEmpty: " + property.name + " on " + entity.getClassName());
			}
		}
		if (property.propertyType == MjPropertyType.LIST || property.propertyType == MjPropertyType.ENUM_SET) {
			validate(p.getGenericClass(), property.type);
		} else {
			validate(p.getClazz(), property.type);
		}
	}

	
}
