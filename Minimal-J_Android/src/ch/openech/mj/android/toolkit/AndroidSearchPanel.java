package ch.openech.mj.android.toolkit;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import ch.openech.mj.resources.ResourceHelper;
import ch.openech.mj.resources.Resources;
import ch.openech.mj.search.Search;

public class AndroidSearchPanel<T> extends AndroidTablePanel<T>  {

	private final Search<T> search;
	
	public AndroidSearchPanel(Context context, final Search<T> search, Object[] keys, TableActionListener listener) {
		super(context, search, keys);
		this.search = search;
	}


	@Override
	protected View beforeTableHeader() {
		LinearLayout searchPanel = new LinearLayout(getContext());
		searchPanel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		final EditText searchText = new EditText(getContext());
	    searchText.setHint(ResourceHelper.getString(Resources.getResourceBundle(), "search.hint"));
	    searchText.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	    searchPanel.addView(searchText);
		
        Button searchButton = new Button(getContext());
        searchButton.setText(ResourceHelper.getString(Resources.getResourceBundle(), "search"));
        searchButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setIds(search.search(searchText.getText().toString()));
			}
		});
        searchPanel.addView(searchButton);
        
        return searchPanel;
	}

}
