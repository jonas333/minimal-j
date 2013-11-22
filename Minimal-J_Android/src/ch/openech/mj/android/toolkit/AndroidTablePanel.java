package ch.openech.mj.android.toolkit;

import java.util.List;

import android.R.color;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import ch.openech.mj.android.AndroidHelper;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.model.properties.Properties;
import ch.openech.mj.search.Lookup;
import ch.openech.mj.toolkit.ITable;

public class AndroidTablePanel<T> extends LinearLayout implements ITable<T> {
	
	AndroidTable<T> table;
	private final List<PropertyInterface> properties;

	public AndroidTablePanel(Context context, Lookup<T> lookup, Object[] keys) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);
		properties = Properties.convert(keys);
		table = new AndroidTable<T>(context, lookup, keys, properties);
		setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(beforeTableHeader());
		addView(createTableHeader());
		addView(table);
	}

	
	@Override
	public void setClickListener(
			ch.openech.mj.toolkit.ITable.TableActionListener clickListener) {
		table.setClickListener(clickListener);
	}

	@Override
	public void setDeleteListener(
			ch.openech.mj.toolkit.ITable.TableActionListener deleteListener) {
		table.setDeleteListener(deleteListener);
	}

	@Override
	public void setFunctionListener(int function,
			ch.openech.mj.toolkit.ITable.TableActionListener listener) {
		table.setFunctionListener(function, listener);
	}

	@Override
	public void setIds(List<Integer> ids) {
		table.setIds(ids);
	}

	@Override
	public void setInsertListener(
			ch.openech.mj.toolkit.ITable.InsertListener insertListener) {
		table.setInsertListener(insertListener);
	}
	
	protected View beforeTableHeader() {
		return new Space(getContext());
	}
	
	
	private View createTableHeader() {
		LinearLayout headerPanel = new LinearLayout(getContext());
		headerPanel.setOrientation(HORIZONTAL);
		headerPanel.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		for (PropertyInterface property : properties) {
			headerPanel.addView(createHeaderCol(property));
		}
		return headerPanel;
	}

	private View createHeaderCol(PropertyInterface property) {
		TextView headerCol = new TextView(getContext());
		LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
		lp.weight = 1.0f;
		headerCol.setLayoutParams(lp);
		headerCol.setText(AndroidHelper.camelCase(property.getFieldName()));
		headerCol.setTypeface(null, Typeface.BOLD);
		headerCol.setBackgroundColor(color.darker_gray);
		return headerCol;
	}
}
