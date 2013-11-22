package ch.openech.mj.android.toolkit;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ch.openech.mj.android.R;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.Caption;
import ch.openech.mj.toolkit.IComponent;

public class AndroidCaption extends LinearLayout implements Caption, IComponent {

	private TextView captionLabel;
	private ImageView icon;
	
	public AndroidCaption(Context context, String text, View view) {
		super(context);
		setOrientation(VERTICAL);
		addView(createCaption(context, text));
		if (view instanceof AndroidTextField) {
			((EditText) view).setHint(text);
		}
		addView(view);
	}
	
	private View createCaption(Context context, String text) {
		LinearLayout captionLayout = new LinearLayout(context);
		captionLayout.setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		icon = new ImageView(context);
		icon.setImageDrawable(context.getResources().getDrawable(R.drawable.field_error));
		icon.setVisibility(INVISIBLE);
		captionLayout.addView(icon);
		captionLabel = new TextView(context);
		captionLabel.setText(text);
		captionLayout.addView(captionLabel);
		return captionLayout;
	}
	

	@Override
	public IComponent getComponent() {
		return this;
	}

	@Override
	public void setValidationMessages(List<String> validationMessages) {
		
		if (!validationMessages.isEmpty()) {
			captionLabel.setTextColor(Color.RED);
			String validationMessage = ValidationMessage.formatText(validationMessages);
			icon.setVisibility(VISIBLE);
			invalidate();
			Toast.makeText(getContext(), validationMessage, Toast.LENGTH_LONG).show();
		} else {
			captionLabel.setTextColor(Color.BLACK);
			icon.setVisibility(INVISIBLE);
			invalidate();
		}
	}

}
