package ch.openech.mj.android.toolkit;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;
import ch.openech.mj.toolkit.ClientToolkit.InputComponentListener;
import ch.openech.mj.toolkit.IFocusListener;
import ch.openech.mj.toolkit.TextField;
import ch.openech.mj.util.StringUtils;

public class AndroidTextField extends EditText implements TextField {

	private IFocusListener focusListener;
	private Runnable commitListener;

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
		addTextChangedListener(new AndroidTextWatcher(changeListener));
		setFilters(createFilters(maxLength, allowedCharacters));
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
		setEnabled(editable);

	}

	@Override
	public void setFocusListener(IFocusListener focusListener) {
		this.focusListener = focusListener;
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
		if (focusListener != null) {
			if (focused) {
				focusListener.onFocusGained();
			} else {
				focusListener.onFocusLost();
			}
		}
	}

	@Override
	public void setInput(String text) {
		setText(text);
	}

	@Override
	public String getInput() {
		return getText().toString();
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
