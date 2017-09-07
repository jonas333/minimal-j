package org.minimalj.frontend.action;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.minimalj.application.Application;
import org.minimalj.util.resources.Resources;

public abstract class MediaProvider {
	
	private static final Map<String, MediaProvider> providers = new HashMap<>();
	
	public static void addProvider(MediaProvider provider) {
		providers.put(provider.getName(), provider);
	}

	public static MediaProvider getProvider(String name) {
		return providers.get(name);
	}

	static {
		addProvider(new ResourceMediaProvider());
	}
	
	public String getName() {
		return getClass().getSimpleName();
	}

	public abstract InputStream getData(String name);
	
	public String getMimeType(String name) {
		int index = name.lastIndexOf('.');
		if (index > 0 && index < name.length() - 1) {
			String postfix = name.substring(index + 1, name.length());
			return Resources.getMimeType(postfix);
		} else {
			return null;
		}
	}

	public static final MediaProvider resourceMediaProvider = new ResourceMediaProvider();
	
	public static class ResourceMediaProvider extends MediaProvider {
		
		@Override
		public InputStream getData(String name) {
			return Application.getInstance().getClass().getResourceAsStream(name);
		}
	}
	
}
