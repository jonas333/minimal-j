package ch.openech.mj.resources;

import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.openech.mj.util.StringUtils;

public class ResourceHelper {
	private static Logger logger = Logger.getLogger(ResourceHelper.class.getName());
	private static Set<String> loggedMissings = new HashSet<String>();
	
	
	public static Integer getInteger(ResourceBundle resourceBundle, String key) {
		try {
			String integerString = resourceBundle.getString(key);
			if (!StringUtils.isBlank(integerString)) {
				return Integer.parseInt(integerString);
			}
		} catch (MissingResourceException e) {
			// silent
		}
		return null;
	}
	
	
	
	public static String getString(ResourceBundle resourceBundle, String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			if (!loggedMissings.contains(key)) {
				logger.log(Level.CONFIG, key + " missing", e);
				loggedMissings.add(key);
			}
			return "!" + key + "!";
		}
	}

	public static String getStringOptional(ResourceBundle resourceBundle, String key) {
		if (resourceBundle.containsKey(key)) {
			return resourceBundle.getString(key);
		} else {
			return null;
		}
	}

	public static String getApplicationTitle() {
		return ResourceHelper.getString(Resources.getResourceBundle(), "Application.title");
	}

	public static String getApplicationHomepage() {
		return ResourceHelper.getString(Resources.getResourceBundle(), "Application.homepage");
	}

	public static String getApplicationVendor() {
		return ResourceHelper.getString(Resources.getResourceBundle(), "Application.vendor");
	}

	public static String getApplicationVersion() {
		return ResourceHelper.getString(Resources.getResourceBundle(), "Application.version");
	}
	
	public static ClassLoader getClassLoader(Class<?> clazz) {
		ClassLoader cl = clazz.getClassLoader();
		if (cl == null) {
			cl = ClassLoader.getSystemClassLoader();
		}
		return cl;
	}
	
	

}
