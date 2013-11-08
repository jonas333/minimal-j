package ch.openech.mj.android.toolkit;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ch.openech.mj.edit.validation.ValidationMessage;
import ch.openech.mj.toolkit.Caption;
import ch.openech.mj.toolkit.IComponent;

public class AndroidCaption extends LinearLayout implements Caption, IComponent {

	
	private final TextView captionLabel;
	
	public AndroidCaption(Context context, String text, View view) {
		super(context);
		setOrientation(VERTICAL);
		captionLabel = new TextView(context);
		captionLabel.setText(text);
		addView(captionLabel);
		addView(view);
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
			Toast.makeText(getContext(), validationMessage, Toast.LENGTH_LONG).show();
		} else {
			captionLabel.setTextColor(Color.BLACK);
		}
	}

}
