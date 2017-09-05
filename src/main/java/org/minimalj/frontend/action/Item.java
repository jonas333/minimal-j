package org.minimalj.frontend.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.minimalj.application.Application;
import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.page.PageAction;
import org.minimalj.model.Keys;
import org.minimalj.util.resources.Resources;

public class Item {
	public static final Item $ = Keys.of(Item.class);
	
	private final Action action;
	private String resourceName;
	private byte[] content;
	private final String mimeType;

	// only to allow $ construction
	public Item() {
		this.action = null;
		this.mimeType = null;
	}
	
	public Item(Page page) {
		this(new PageAction(page), page.getClass());
	}

	public Item(Editor<?, ?> editor) {
		this(editor, editor.getClass());
	}

	private Item(Action action, Class<?> resouceClass) {
		this.action = action;

		resourceName = Resources.getString(resouceClass.getName() + ".tile", Resources.OPTIONAL);
		if (resourceName == null) {
			resourceName = Resources.getString(resouceClass.getSimpleName() + ".tile");
		}
		
		this.mimeType = guessMimeType(resourceName);
	}
	
	public Item(Action action, byte[] content, String mimeType) {
		Objects.nonNull(action);
		
		this.action = action;
		this.content = content;
		this.mimeType = mimeType;
	}
	
	public Item(Action action, String resourceName) {
		Objects.nonNull(action);
		
		this.action = action;
		this.resourceName = resourceName;
		this.mimeType = guessMimeType(resourceName);
	}

	public Item(Page page, byte[] content, String mimeType) {
		this(new PageAction(page), content, mimeType);
	}

	public Item(Page page, String resourceName) {
		this(new PageAction(page), resourceName);
	}

	//
	
	public final String getName() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "name");
		
		return action.getName();
	}

	public final String getDescription() {
		if (Keys.isKeyObject(this)) return Keys.methodOf(this, "description");
		
		return action.getDescription();
	}

	public InputStream getContent() {
		if (resourceName != null) {
			return Application.getInstance().getClass().getResourceAsStream(resourceName);
		} else if (content != null) {
			return new ByteArrayInputStream(content);
		} else {
			return null;
		}
	}
	
	public byte[] getContentBytes() {
		if (content != null) {
			return content;
		} else if (resourceName != null) {
			InputStream is = Application.getInstance().getClass().getResourceAsStream(resourceName);
			try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				byte[] buffer = new byte[1024];
				while (true) {
					int r = is.read(buffer);
					if (r == -1) break;
					out.write(buffer, 0, r);
				}
				content = out.toByteArray();
				return content;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}

	public String getMimeType() {
		return mimeType;
	}
	
	private String guessMimeType(String resourceName) {
		int index = resourceName.lastIndexOf('.');
		if (index > 0 && index < resourceName.length() - 1) {
			String postfix = resourceName.substring(index + 1, resourceName.length());
			return Resources.getMimeType(postfix);
		} else {
			return null;
		}
		
	}
	
	public void action() {
		action.action();
	}
}
