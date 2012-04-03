package ch.openech.mj.edit.fields;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import ch.openech.mj.autofill.DemoEnabled;
import ch.openech.mj.edit.validation.Validatable;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.toolkit.TextField.TextFieldFilter;
import ch.openech.mj.util.DateUtils;
import ch.openech.mj.util.StringUtils;

/*

Ein Datumsfeld enthält die Angaben zu Tag, Monat und Jahr. Bei Geburtstagen kann der Monat und der Tag weggelassen werden. Die Eingabe wird
so gut wie möglich ergänzt. Die Übersetzung geschieht wie folgt:

 030607 wird zu 03.06.2007 also dem 3. Juni im Jahr 2010
 2.2.02 wird zu 02.02.2002 also dem 2. Februar 2002
 1.1.99 wird zu 01.01.1999 Zweistellige Jahreszahlen werden bis 20 als 20xx, ab 21 als 19xx interpretiert

 */

public class DateField extends AbstractEditField<String> implements Validatable, DemoEnabled {
	public static final boolean REQUIRED = true;
	public static final boolean NOT_REQUIRED = !REQUIRED;
	public static final boolean PARTIAL_ALLOWED = true;
	
	private final TextField textField;
	
	private final boolean partialAllowed;
	private Format format = Format.CH;
	
	private enum Format {
		CH, US, Free
	}

	public DateField(Object key) {
		this(key, false);
	}
	
	public DateField(Object key, boolean partialAllowed) {
		this(key, partialAllowed, true);
	}
	
	public DateField(Object key, boolean partialAllowed, boolean editable) {
		super(key, editable);
		this.partialAllowed = partialAllowed;
		
		if (editable) {
			textField = ClientToolkit.getToolkit().createTextField(listener(), new DateFilter());
			
			installFocusLostListener();
		} else {
			textField = ClientToolkit.getToolkit().createReadOnlyTextField();
		}
	}

	@Override
	public Object getComponent() {
		return textField;
	}

	private void installFocusLostListener() {
        textField.setFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				// Formattierung auslösen
				String value = getObject();
				if (!StringUtils.isBlank(value)) {
					setObject(value);
				}
			}
		});
	}
	
	@Override
	public String getObject() {
		String text = textField.getText();
		if (text == null) return null;
		text = text.trim();
		if (text.length() == 0) return null;
		
		switch (format) {
		case CH: return DateUtils.parseCH(text, partialAllowed);
		case US: return DateUtils.parseUS(text);
		default: return text;
		}
	}
	
	@Override
	public void setObject(String value) {
		if (!StringUtils.isBlank(value)) {
			if (format == Format.CH) value = DateUtils.formatCH(value);
			if (!StringUtils.equals(textField.getText(), value)) {
				textField.setText(value);
			}
		} else {
			textField.setText(null);
		}
	}

	public void setEnabled(boolean enabled) {
		textField.setEnabled(enabled);
		if (!enabled) {
			setObject(null);
		}
	}
	
	@Override
	public void fillWithDemoData() {
		if ("dateOfDeath".equals(getName())) {
			if (Math.random() < 0.9) {
				setObject(null);
				return;
			}
		}
		setObject(generateRandom());
	}

	public static String generateRandom() {
		int year =(int)(Math.random() * 80) + 1930;
		int month =(int)(Math.random() * 12) + 1;
		int day;
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			day =(int)(Math.random() * 30) + 1;
		} else if (month == 2) {
			day =(int)(Math.random() * 28) + 1;
		} else {
			day =(int)(Math.random() * 31) + 1;
		}
		return DateUtils.parseCH(day + "."  + month + "." + year);
	}
	
	@Override
	public void validate(List<ValidationMessage> list) {
		String value = getObject();
		if (StringUtils.isBlank(textField.getText())) {
			return;
		}

		if (!DateUtils.isValueValidUS(value, partialAllowed)) {
			list.add(new ValidationMessage(getName(), "Fehlerhaftes Format des Datums"));
			return;
		}

		boolean complete = value != null && value.length() == 10;
		if (!partialAllowed && !complete) {
			list.add(new ValidationMessage(getName(), "Komplettes Datum erforderlich"));
			return;
		}
	}
	
	private class DateFilter implements TextFieldFilter {
		private static final int limit = 10;
		
		@Override
		public String filter(IComponent textField, String str) {
			if (str == null)
				return str;

			int pos = 0;
			while (pos < str.length()) {
				char c = str.charAt(pos);
				if (!Character.isDigit(c)) {
					if (format == Format.CH && c != '.') break;
					if (format == Format.US && c != '-') break;
				}
				pos++;
			}
			if (pos < str.length()) {
				String message = "In ein Datumsfeld kann kein \"" + str.charAt(pos) + "\" eingegeben werden";
				showBubble(textField, message);
				str = str.substring(0, pos);
			}
			
			if (str.length() <= limit) {
				return str;
			} else {
				showBubble(textField, "Eingabe auf " + limit + " Zeichen beschränkt");
				return str.substring(0, limit);
			}
		}
	}
	
}
