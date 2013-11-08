package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.widget.TextView;
import ch.openech.mj.toolkit.IComponent;

public class AndroidTitle extends TextView implements IComponent {

	private Paint paint = new Paint();
	public static final int BORDER_TOP = 0x00000001;
	public static final int BORDER_RIGHT = 0x00000002;
	public static final int BORDER_BOTTOM = 0x00000004;
	public static final int BORDER_LEFT = 0x00000008;

	public AndroidTitle(Context context, String title) {
		super(context);
		setTypeface(getTypeface(), Typeface.BOLD);
		setText(title);
		init();
	}

	private void init() {
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(4);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(1);
		canvas.drawLine(0, 0, getWidth(), 0, paint);
		canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), paint);
		canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
		canvas.drawLine(0, 0, 0, getHeight(), paint);
	}

}
