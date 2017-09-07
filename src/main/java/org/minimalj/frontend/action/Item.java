package org.minimalj.frontend.action;

import java.util.Objects;

import org.minimalj.frontend.editor.Editor;
import org.minimalj.frontend.page.Page;
import org.minimalj.frontend.page.PageAction;
import org.minimalj.model.Keys;
import org.minimalj.util.resources.Resources;

public class Item {
	public static final Item $ = Keys.of(Item.class);
	
	private final Action action;
	private final MediaProvider mediaProvider;
	private final String mediaName;

	// only to allow $ construction
	public Item() {
		this.action = null;
		this.mediaProvider = null;
		this.mediaName = null;
	}
	
	public Item(Page page) {
		this(new PageAction(page), page.getClass());
	}

	public Item(Editor<?, ?> editor) {
		this(editor, editor.getClass());
	}

	private Item(Action action, Class<?> resouceClass) {
		this.action = action;

		String mediaName = Resources.getString(resouceClass.getName() + ".tile", Resources.OPTIONAL);
		if (mediaName == null) {
			mediaName = Resources.getString(resouceClass.getSimpleName() + ".tile");
		}
		this.mediaName = mediaName;
		this.mediaProvider = MediaProvider.resourceMediaProvider;
	}
	
	public Item(Action action, MediaProvider mediaProvider, String name) {
		Objects.nonNull(action);
		
		this.action = action;
		this.mediaProvider = mediaProvider;
		this.mediaName = name;
	}
	
	public Item(Action action, String mediaName) {
		Objects.nonNull(action);
		
		this.action = action;
		this.mediaName = mediaName;
		this.mediaProvider = MediaProvider.resourceMediaProvider;
	}

	public Item(Page page, String mediaName) {
		this(new PageAction(page), mediaName);
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

	public String getMediaProviderName() {
		return mediaProvider.getName();
	}
	
	public String getMediaName() {
		return mediaName;
	}
	
	public String getMimeType() {
		return mediaProvider.getMimeType(mediaName);
	}
	
	public void action() {
		action.action();
	}
}
