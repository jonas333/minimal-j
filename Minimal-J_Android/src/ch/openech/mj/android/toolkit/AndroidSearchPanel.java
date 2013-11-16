package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import ch.openech.mj.search.Search;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.ITable.TableActionListener;

public class AndroidSearchPanel<T> extends GridLayout implements IComponent {

	private EditText editText;
	private Button searchButton;
	private AndroidTable<T> table;
	
	
	public AndroidSearchPanel(Context context, final Search<T> search, Object[] keys, TableActionListener listener) {
		super(context);
		init(search, keys, listener);
	}
	
	private void init(final Search<T> search, Object[] keys, TableActionListener listener) {
		setColumnCount(1);
		setRowCount(2);
		LinearLayout top = new LinearLayout(getContext());
		
		editText = new EditText(getContext());
		top.addView(editText);

		searchButton = new Button(getContext());
		searchButton.setText("Suchen");
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				table.setIds(search.search(editText.getText().toString()));
			}
		});
		
		top.addView(searchButton);
		addView(top);
		
		table = new AndroidTable<T>(getContext(), search, keys);
		table.setClickListener(listener);
		addView(table);
		
	}
	

}
