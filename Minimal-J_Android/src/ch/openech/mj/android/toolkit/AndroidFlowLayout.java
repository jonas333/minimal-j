package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import ch.openech.mj.toolkit.FlowField;
import ch.openech.mj.toolkit.IComponent;

public class AndroidFlowLayout extends RelativeLayout implements FlowField {

	public AndroidFlowLayout(Context context) {
		super(context);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// need to call super.onMeasure(...) otherwise get some funny behaviour
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		// increment the x position as we progress through a line
		int xpos = getPaddingLeft();
		// increment the y position as we progress through the lines
		int ypos = getPaddingTop();
		// the height of the current line
		int line_height = 0;

		// go through children
		// to work out the height required for this view

		// call to measure size of children not needed I think?!
		// getting child's measured height/width seems to work okay without it
		// measureChildren(widthMeasureSpec, heightMeasureSpec);

		View child;
		MarginLayoutParams childMarginLayoutParams;
		int childWidth, childHeight, childMarginLeft, childMarginRight, childMarginTop, childMarginBottom;

		for (int i = 0; i < getChildCount(); i++) {
			child = getChildAt(i);

			if (child.getVisibility() != GONE) {
				childWidth = child.getMeasuredWidth();
				childHeight = child.getMeasuredHeight();

				if (child.getLayoutParams() != null
						&& child.getLayoutParams() instanceof MarginLayoutParams) {
					childMarginLayoutParams = (MarginLayoutParams) child
							.getLayoutParams();

					childMarginLeft = childMarginLayoutParams.leftMargin;
					childMarginRight = childMarginLayoutParams.rightMargin;
					childMarginTop = childMarginLayoutParams.topMargin;
					childMarginBottom = childMarginLayoutParams.bottomMargin;
				} else {
					childMarginLeft = 0;
					childMarginRight = 0;
					childMarginTop = 0;
					childMarginBottom = 0;
				}

				if (xpos + childMarginLeft + childWidth + childMarginRight
						+ getPaddingRight() > width) {
					// this child will need to go on a new line

					xpos = getPaddingLeft();
					ypos += line_height;

					line_height = childMarginTop + childHeight
							+ childMarginBottom;
				} else
					// enough space for this child on the current line
					line_height = Math.max(line_height, childMarginTop
							+ childHeight + childMarginBottom);

				xpos += childMarginLeft + childWidth + childMarginRight;
			}
		}

		ypos += line_height + getPaddingBottom();

		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED)
			// set height as measured since there's no height restrictions
			height = ypos;
		else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST
				&& ypos < height)
			// set height as measured since it's less than the maximum allowed
			height = ypos;

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// increment the x position as we progress through a line
		int xpos = getPaddingLeft();
		// increment the y position as we progress through the lines
		int ypos = getPaddingTop();
		// the height of the current line
		int line_height = 0;

		View child;
		MarginLayoutParams childMarginLayoutParams;
		int childWidth, childHeight, childMarginLeft, childMarginRight, childMarginTop, childMarginBottom;

		for (int i = 0; i < getChildCount(); i++) {
			child = getChildAt(i);

			if (child.getVisibility() != GONE) {
				childWidth = child.getMeasuredWidth();
				childHeight = child.getMeasuredHeight();

				if (child.getLayoutParams() != null
						&& child.getLayoutParams() instanceof MarginLayoutParams) {
					childMarginLayoutParams = (MarginLayoutParams) child
							.getLayoutParams();

					childMarginLeft = childMarginLayoutParams.leftMargin;
					childMarginRight = childMarginLayoutParams.rightMargin;
					childMarginTop = childMarginLayoutParams.topMargin;
					childMarginBottom = childMarginLayoutParams.bottomMargin;
				} else {
					childMarginLeft = 0;
					childMarginRight = 0;
					childMarginTop = 0;
					childMarginBottom = 0;
				}

				if (xpos + childMarginLeft + childWidth + childMarginRight
						+ getPaddingRight() > r - l) {
					// this child will need to go on a new line

					xpos = getPaddingLeft();
					ypos += line_height;

					line_height = childHeight + childMarginTop
							+ childMarginBottom;
				} else
					// enough space for this child on the current line
					line_height = Math.max(line_height, childMarginTop
							+ childHeight + childMarginBottom);

				child.layout(xpos + childMarginLeft, ypos + childMarginTop,
						xpos + childMarginLeft + childWidth, ypos
								+ childMarginTop + childHeight);

				xpos += childMarginLeft + childWidth + childMarginRight;
			}
		}
	}

	@Override
	public void add(IComponent component) {
		addView((View) component);
	}

	@Override
	public void addGap() {

	}

	@Override
	public void clear() {
		removeAllViews();
	}

}
