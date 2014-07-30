package org.minimalj.frontend.page;

import org.minimalj.frontend.toolkit.ClientToolkit.IContent;

public class EmptyPage extends AbstractPage {

	public EmptyPage() {
	}
	
	@Override
	public IContent getContent() {
		return null;
	}

	@Override
	public void refresh() {
		// nothing to do
	}

}
