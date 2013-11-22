package ch.openech.mj.android.toolkit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import ch.openech.mj.android.R;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IDialog;

public class AndroidDialog extends Dialog implements IDialog {

	private IComponent content;
	private CloseListener listener;

	
	public AndroidDialog(Context context, String title, IComponent content) {
		super(context);
		this.content = content;
		setTitle(title);
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				fireCloseEvent();
			}
		});
		setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				fireCloseEvent();
			}
		});
	}
	

	@Override
	public void closeDialog() {
		dismiss();
	}

	@Override
	public void openDialog() {
		show();
	}

	@Override
	public void setCloseListener(CloseListener listener) {
		this.listener = listener;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);
        Button buttonOk = (Button) findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});			
        Button buttonCancel = (Button) findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
        ViewGroup dialogContent = (ViewGroup) findViewById(R.id.dialog_content);
        dialogContent.addView((View) content);
	}
	
	private void fireCloseEvent() {
		if (listener != null) {
			listener.close();
		}
	}
	
	
}
