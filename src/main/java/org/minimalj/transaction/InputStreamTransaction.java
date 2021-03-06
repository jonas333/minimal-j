package org.minimalj.transaction;

import java.io.InputStream;
import java.io.Serializable;

public abstract class InputStreamTransaction<T extends Serializable> implements Transaction<T> {
	private static final long serialVersionUID = 1L;
	
	private transient InputStream stream;
	
	public InputStreamTransaction(InputStream inputStream) {
		this.stream = inputStream;
	}

	public void setStream(InputStream stream) {
		this.stream = stream;
	}
	
	public InputStream getStream() {
		return stream;
	}
}
