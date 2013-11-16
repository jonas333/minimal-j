package ch.openech.mj.android.toolkit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IDialog;

public class AndroidDialog extends DialogFragment implements IDialog {

	private String title;
	private IComponent content;
	private FragmentManager fragmentManager;
	private CloseListener listener;

	
	public AndroidDialog() {
		super();
	}
	
	public void setFragmentManager(FragmentManager fragmentManager) {
		this.fragmentManager = fragmentManager;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		builder.setView((View) content);
		builder.setPositiveButton("OK", null);
		builder.setNegativeButton("Abbrechen", null);
		Dialog dlg = builder.create();
		dlg.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (listener != null)
				{
					listener.close();
				}
			}
		});
		return builder.create();
		
	}

	@Override
	public void closeDialog() {
		dismiss();
	}

	@Override
	public void openDialog() {
		show(fragmentManager, "RANDOM");
	}

	@Override
	public void setCloseListener(CloseListener listener) {
		this.listener = listener;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setContent(IComponent content) {
		this.content = content;
	}

}
