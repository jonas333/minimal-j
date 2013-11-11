package ch.openech.mj.android.toolkit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IDialog;

public class AndroidDialog extends DialogFragment implements IDialog {

	private String title;
	private IComponent content;
	private FragmentManager fragmentManager;

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
		// TODO Auto-generated method stub

	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setContent(IComponent content) {
		this.content = content;
	}

}
