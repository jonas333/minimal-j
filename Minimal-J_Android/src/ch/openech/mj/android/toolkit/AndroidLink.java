package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;
import ch.openech.mj.toolkit.ILink;

public class AndroidLink extends TextView implements ILink {

	
	public AndroidLink(Context context, String text, String address) {
		super(context);
		setText(address);
		setAutoLinkMask(Linkify.ALL);
		setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public String getAddress() {
		return getText().toString();
	}

}
