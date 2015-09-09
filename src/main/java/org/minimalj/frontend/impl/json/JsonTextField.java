package org.minimalj.frontend.impl.json;

import java.util.List;

import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.InputComponentListener;
import org.minimalj.frontend.Frontend.InputType;

public class JsonTextField extends JsonInputComponent<String> implements Input<String> {

	private static final String MAX_LENGTH = "maxLength";
	private static final String ALLOWED_CHARACTERS = "allowedCharacters";
	private static final String INPUT_TYPE = "inputType";
	private static final String SUGGESTIONS = "suggestions";
	
	public JsonTextField(String type) {
		super(type, null);
	}
	
	public JsonTextField(String type, InputComponentListener changeListener) {
		super(type, changeListener);
	}
	
	public JsonTextField(String type, int maxLength, String allowedCharacters, InputType inputType, List<String> suggestions,
			InputComponentListener changeListener) {
		super(type, changeListener);
		put(MAX_LENGTH, maxLength);
		put(ALLOWED_CHARACTERS, allowedCharacters);
		if (inputType != null) {
			put(INPUT_TYPE, inputType.name());
		}
		if (suggestions != null) {
			put(SUGGESTIONS, suggestions);
		}
	}
	
	@Override
	public void setValue(String text) {
		put(VALUE, text);
	}

	@Override
	public String getValue() {
		return (String) get(VALUE);
	}
}
