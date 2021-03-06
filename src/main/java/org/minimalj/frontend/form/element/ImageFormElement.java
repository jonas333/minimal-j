package org.minimalj.frontend.form.element;

import org.minimalj.frontend.Frontend;
import org.minimalj.frontend.Frontend.IComponent;
import org.minimalj.frontend.Frontend.Input;
import org.minimalj.frontend.Frontend.InputComponentListener;

public class ImageFormElement extends AbstractFormElement<byte[]> {

	private final Input<byte[]> input;

	public ImageFormElement(Object key) {
		this(key, 3);
	}
	
	public ImageFormElement(Object key, int size) {
		super(key);
		input = Frontend.getInstance().createImage(size, new ImageFieldChangeListener());
	}

	@Override
	public void setValue(byte[] object) {
		input.setValue(object);
	}

	@Override
	public byte[] getValue() {
		return input.getValue();
	}

	@Override
	public IComponent getComponent() {
		return input;
	}
	
	private class ImageFieldChangeListener implements InputComponentListener {
		@Override
		public void changed(IComponent source) {
			// TODO image validation
			fireChange();
		}
	}
}