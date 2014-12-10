package org.minimalj.util.resources;

import java.net.URL;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.minimalj.util.StringUtils;

public class ResourceHelper {
	private static final String ICONS_DIRECTORY = "icons";
	private static Logger logger = Logger.getLogger(ResourceHelper.class.getName());
	private static Set<String> loggedMissings = new HashSet<>();
	
	/*
	 * Init all of the Action properties for the @Action named
	 * actionName.
	 */
	public static Action initProperties(Action action, ResourceBundle resourceBundle, String baseName) {
		action.putValue("actionName", baseName);
		
		// Action => Action.NAME,MNEMONIC_KEY,DISPLAYED_MNEMONIC_INDEX_KEY
		String text = getString(resourceBundle, baseName);
		if (text != null) {
			MnemonicText.configure(action, text);
		}
		
		// Action.mnemonic => Action.MNEMONIC_KEY
		Integer mnemonic = getKeyCode(resourceBundle, baseName + ".mnemonic");
		if (mnemonic != null) {
			action.putValue(Action.MNEMONIC_KEY, mnemonic);
		}
		
		// Action.mnemonic => Action.DISPLAYED_MNEMONIC_INDEX_KEY
		Integer index = getInteger(resourceBundle, baseName + ".displayedMnemonicIndex");
		if (index != null) {
			action.putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, index);
		}
		
		// Action.accelerator => Action.ACCELERATOR_KEY
		KeyStroke key = getKeyStroke(resourceBundle, baseName + ".accelerator");
		if (key != null) {
			action.putValue(Action.ACCELERATOR_KEY, key);
		}
		
		// Action.icon => Action.SMALL_ICON,LARGE_ICON_KEY
		Icon icon = getIcon(resourceBundle, baseName + ".icon");
		if (icon != null) {
			action.putValue(Action.SMALL_ICON, icon);
			action.putValue(Action.LARGE_ICON_KEY, icon);
		}
		
		// Action.smallIcon => Action.SMALL_ICON
		Icon smallIcon = getIcon(resourceBundle, baseName + ".smallIcon");
		if (smallIcon != null) {
			action.putValue(Action.SMALL_ICON, smallIcon);
		}
		
		// Action.largeIcon => Action.LARGE_ICON
		Icon largeIcon = getIcon(resourceBundle, baseName + ".largeIcon");
		if (largeIcon != null) {
			action.putValue(Action.LARGE_ICON_KEY, largeIcon);
		}
		
		addActionValue(action, Action.SHORT_DESCRIPTION, resourceBundle, baseName + ".shortDescription");
		addActionValue(action, Action.LONG_DESCRIPTION, resourceBundle, baseName + ".longDescription");
		addActionValue(action, Action.ACTION_COMMAND_KEY, resourceBundle, baseName + ".command");
		
		return action;
	}
	
	private static void addActionValue(Action action, String actionKey, ResourceBundle resourceBundle,
			String property) {
		if (resourceBundle.containsKey(property)) {
			action.putValue(actionKey, resourceBundle.getString(property));
		}
	}

	public static Icon getIcon(ResourceBundle resourceBundle, String key) {
		String filename = getStringOptional(resourceBundle, key);
		if (!StringUtils.isBlank(filename)) {
			URL url = ResourceHelper.class.getResource(ICONS_DIRECTORY + "/" + filename);
			if (url != null) {
				return new ImageIcon(url);
			}
		}
		return null;
	}
	
	public static Icon getIcon(String filename) {
		filename = ICONS_DIRECTORY + "/" + filename;
		URL url = ResourceHelper.class.getResource(filename);
		if (url != null) {
			return new ImageIcon(url);
		} else {
			return null;
		}
	}
	
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
	
	public static KeyStroke getKeyStroke(ResourceBundle resourceBundle, String key) {
		String keyStrokeString = getStringOptional(resourceBundle, key);
		if (!StringUtils.isBlank(keyStrokeString)) {
			return KeyStroke.getKeyStroke(keyStrokeString);
		}
		return null;
	}
	
	public static Integer getKeyCode(ResourceBundle resourceBundle, String key) {
		String keyStrokeString = getStringOptional(resourceBundle, key);
		if (!StringUtils.isBlank(keyStrokeString)) {
			KeyStroke keyStroke = KeyStroke.getKeyStroke(keyStrokeString);
			if (keyStroke != null) {
				return keyStroke.getKeyCode();
			}
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
			return "'" + key + "'";
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

}
