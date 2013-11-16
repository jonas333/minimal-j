package ch.openech.mj.android.toolkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;
import ch.openech.mj.android.AndroidHelper;
import ch.openech.mj.model.PropertyInterface;
import ch.openech.mj.model.properties.Properties;
import ch.openech.mj.search.Lookup;
import ch.openech.mj.toolkit.ITable;

public class AndroidTable<T> extends ListView implements ITable<T>, android.widget.AdapterView.OnItemClickListener {

	
	private static final float COLUMN_WEIGHT = 1.0f;
	
	private final Lookup<T> lookup;
	private final Object[] keys;
	
	private TableActionListener clickListener;
	private TableActionListener deleteListener;
	private InsertListener insertListener;
	private TableActionListener functionListener;
	private final List<PropertyInterface> properties;
	private final AndroidTableAdapter adapter;
	
	
	
	public AndroidTable(Context context, Lookup<T> lookup, Object[] keys) {
		super(context);
		this.lookup = lookup;
		this.keys = keys;
		properties = Properties.convert(keys);
		setChoiceMode(CHOICE_MODE_SINGLE);
		setDividerHeight(1);
		adapter = new AndroidTableAdapter(context, android.R.layout.simple_list_item_1);
		addHeaderView(createHeader(), properties, false);
		
		setAdapter(adapter);
		setOnItemClickListener(this);
	}

	@Override
	public void setClickListener(
			ch.openech.mj.toolkit.ITable.TableActionListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	public void setDeleteListener(
			ch.openech.mj.toolkit.ITable.TableActionListener deleteListener) {
		this.deleteListener = deleteListener;
	}

	@Override
	public void setFunctionListener(int function,
			ch.openech.mj.toolkit.ITable.TableActionListener functionListener) {
		
	}

	@Override
	public void setIds(List<Integer> ids) {
		adapter.setIds(ids);
	}

	@Override
	public void setInsertListener(
			ch.openech.mj.toolkit.ITable.InsertListener insertListener) {
		this.insertListener = insertListener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (clickListener != null)
		{
			clickListener.action(getId(position), getSelectedIds());
		}	
	}
	
	private List<Integer> getSelectedIds() {
		List<Integer> selectedIds = new ArrayList<Integer>();
		SparseBooleanArray checkedItems = getCheckedItemPositions();
		for (int i = 0; i < checkedItems.size(); i++) {
			if (checkedItems.valueAt(i) == true )
			{
				selectedIds.add(adapter.getId(checkedItems.keyAt(i)));
			}
		}
		return selectedIds;
	}
	
	private int getId(int position) {
		return adapter.getId(position);
	}
	
	private View createHeader() {
		ViewGroup header = createRowLayout(getContext());
		for (PropertyInterface prop : properties)
		{
			header.addView(createTableHeader(getContext(), prop.getFieldName()));
		}
		return header;
	}
	
	public static ViewGroup createRowLayout(Context context) {
		LinearLayout rowLayout = new LinearLayout(context);
		ListView.LayoutParams lp = new ListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		rowLayout.setLayoutParams(lp);
		rowLayout.setPadding(0, 6, 0, 4);
		return rowLayout;
	}
	
	public static View createTableHeader(Context context, Object value) {
		TextView header = createTableColumn(context, value);
		header.setText( AndroidHelper.camelCase(header.getText().toString()));
		header.setTypeface(null, Typeface.BOLD);
		return header;
	}
	
	
	private static TextView createTableColumn(Context context, Object value)
	{
		TextView column = new TextView(context);
		column.setText(value == null ? "NULL" : value.toString());
		android.widget.LinearLayout.LayoutParams  lp = new android.widget.LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.weight = COLUMN_WEIGHT;
		column.setLayoutParams(lp);
		return column;
	}
	

	public class AndroidTableAdapter extends ArrayAdapter<T> {
		
		private static final int COLUMN_GAP = 5;
		private List<Integer> ids = Collections.emptyList();
		
		public AndroidTableAdapter(Context context, int resource) {
			super(context, resource);
		}

		@Override
		public int getCount() {
			return ids == null ? 0 :  ids.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (position > -1)
			{
			ViewGroup layout = createRowLayout(getContext());
			for (PropertyInterface property : properties) {
				layout.addView(createColumn(position, property));
				layout.addView(createSpace(COLUMN_GAP));
			}
			layout.removeViewAt(layout.getChildCount()-1);
			return layout;
			}
			else {
				return null;
			}
		}
	
		
		
		private View createColumn(int position, PropertyInterface property)
		{
			T row = lookup.lookup(ids.get(position));
			return createTableColumn(getContext(), property.getValue(row));
		}
		
		
		private View createSpace(int width) {
			Space space = new Space(getContext());
			space.setLayoutParams(new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT));
			return space;
		}
		
		public void setIds(List<Integer> ids) {
			this.ids = ids;
			notifyDataSetChanged();
		}

		public int getId(int position) {
			if (position >= 0) {
				return ids.get(position-1);
			} else {
				return -1;
			}
		}
	}
	
}
