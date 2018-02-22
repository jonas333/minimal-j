package org.minimalj.example.helloworld;

import org.minimalj.application.Application;
import org.minimalj.frontend.impl.cheerpj.Cheerpj;
import org.minimalj.frontend.page.Page;

public class HelloWorldApplication extends Application {

	@Override
	public Page createDefaultPage() {
		return new HelloWorldPage();
	}

	public static void main(String... args) {
		Cheerpj.start(new HelloWorldApplication());
	}
	
}
