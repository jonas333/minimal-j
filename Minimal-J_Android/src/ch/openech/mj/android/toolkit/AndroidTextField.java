package ch.openech.mj.android.toolkit;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import ch.openech.mj.toolkit.ClientToolkit.InputComponentListener;
import ch.openech.mj.toolkit.IFocusListener;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;

public class AndroidTextField extends LinearLayout implements TextField {

	private IFocusListener focusListener;
	private Runnable commitListener;
	private EditText editText;

	public AndroidTextField(Context context) {
		this(context, null, Integer.MAX_VALUE);
	}

	public AndroidTextField(Context ctx, InputComponentListener changeListener,
			int maxLength) {
		this(ctx, changeListener, maxLength, null);
	}

	public AndroidTextField(Context ctx, InputComponentListener changeListener,
			int maxLength, String allowedCharacters) {
		super(ctx);
		editText = new EditText(ctx);
		editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editText.addTextChangedListener(new AndroidTextWatcher(changeListener));
		editText.setFilters(createFilters(maxLength, allowedCharacters));
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (focusListener != null)
				{
					if (hasFocus) {
						focusListener.onFocusGained();
					} else {
						focusListener.onFocusLost();
					}
				}
			}
		});
		addView(editText);
	}

	private InputFilter[] createFilters(int maxLength, String allowedChars) {
		List<InputFilter> filters = new ArrayList<InputFilter>();
		filters.add(new InputFilter.LengthFilter(maxLength));
		if (!StringUtils.isEmpty(allowedChars)) {
			filters.add(new TextFilter(allowedChars));
		}
		return filters.toArray(new InputFilter[filters.size()]);
	}

	@Override
	public void setCommitListener(Runnable commitListener) {
		this.commitListener = commitListener;
	}

	@Override
	public void setEditable(boolean editable) {
		editText.setEnabled(editable);
	}

	@Override
	public void setFocusListener(IFocusListener focusListener) {
		this.focusListener = focusListener;
	}

	@Override
	public void setText(String text) {
		editText.setText(text);
	}

	@Override
	public String getText() {
		return editText.getText().toString();
	}
	
	public void setHint(String hint) {
		editText.setHint(hint);
	}
	

	public class AndroidTextWatcher implements TextWatcher {

		InputComponentListener componentListener;

		public AndroidTextWatcher(InputComponentListener componentListener) {
			super();
			this.componentListener = componentListener;
		}

		@Override
		public void afterTextChanged(Editable s) {
			// we are not interested
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// we are not interested
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (componentListener != null) {
				componentListener.changed(AndroidTextField.this);
			}
		}
	}
	
	public class TextFilter implements InputFilter {

		private final String allowedCharacters;
		
		public TextFilter(String allowedCharacters) {
			super();
			this.allowedCharacters = allowedCharacters;
		}


		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			
			if (!StringUtils.isEmpty(source.toString())) {
				StringBuilder result = new StringBuilder("");
				for (int i = 0; i < source.length(); i++) {
					String subSequence = source.subSequence(i, i+1).toString();
					if ( allowedCharacters.indexOf(  subSequence) != -1) {
						result.append(subSequence);
					}
				}
				return result.toString();
			}
			return source;
		}
		
	}

}
