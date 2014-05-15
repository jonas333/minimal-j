package ch.openech.mj.page;

import java.util.concurrent.Callable;

import ch.openech.mj.edit.form.IForm;
import ch.openech.mj.server.DbRead;
import ch.openech.mj.server.DbReadHistory;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.IComponent;

public abstract class ObjectViewPage<T> extends AbstractPage implements RefreshablePage {

	private final Callable<T> read;
	private IForm<T> objectPanel;
	private IComponent alignLayout;
	private T object;
	
	public ObjectViewPage(PageContext pageContext, Class<T> clazz, String id) {
		this(pageContext, new DbRead<T>(clazz, id));
	}

	public ObjectViewPage(PageContext pageContext, Class<T> clazz, String id, String time) {
		this(pageContext, time != null ? new DbReadHistory<T>(clazz, id, time) : new DbRead<T>(clazz, id));
	}
	
	public ObjectViewPage(PageContext pageContext, Callable<T> read) {
		super(pageContext);
		this.read = read;
	}


	protected abstract IForm<T> createForm();

	private void readObject() {
		object = ClientToolkit.getToolkit().execute(getPageContext(), read);
	}
	
	protected T getObject() {
		if (object == null) {
			readObject();
		}
		return object;
	}
	
	@Override
	public IComponent getComponent() {
		if (alignLayout == null) {
			objectPanel = createForm();
			objectPanel.setObject(getObject());
			alignLayout = ClientToolkit.getToolkit().createFormAlignLayout(objectPanel.getComponent());
		}
		return alignLayout;
	}
	
	@Override
	public void refresh() {
		objectPanel.setObject(getObject());
	}
	
}
