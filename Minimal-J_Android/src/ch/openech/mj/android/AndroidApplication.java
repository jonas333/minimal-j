package ch.openech.mj.android;

import android.app.Application;
import ch.openech.mj.android.toolkit.AndroidClientToolkit;
import ch.openech.mj.application.ApplicationContext;
import ch.openech.mj.application.MjApplication;
import ch.openech.mj.toolkit.ClientToolkit;

public final class AndroidApplication extends Application {
	
	private ApplicationContext applicationContext;
	
	@Override
	public void onCreate() {
		super.onCreate();
		initApplication();
	}

	@SuppressWarnings("unchecked")
	private  void initApplication() {
		Class<? extends MjApplication> applicationClass;
		try {
			String appName = getResources().getString(R.string.minimalj_application);
			applicationClass = (Class<? extends MjApplication>) Class.forName(appName);
			MjApplication application = applicationClass.newInstance();
			ClientToolkit.setToolkit(new AndroidClientToolkit());
			application.init();
			applicationContext = new AndroidApplicationContext();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public ApplicationContext getAppContext() {
		return applicationContext;
	}

	
}