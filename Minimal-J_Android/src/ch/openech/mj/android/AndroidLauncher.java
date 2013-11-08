package ch.openech.mj.android;

import android.content.Context;
import android.os.AsyncTask;
import ch.openech.mj.android.toolkit.AndroidClientToolkit;
import ch.openech.mj.application.ApplicationContext;
import ch.openech.mj.application.MjApplication;
import ch.openech.mj.toolkit.ClientToolkit;

public class AndroidLauncher extends AsyncTask<Context, Void, Void>
{
	
	private static final String APPLICATION_NAME = "ch.openech.mj.example.MjExampleApplication";
	private static ApplicationContext applicationContext;
	

	@Override
	protected Void doInBackground(Context... params) {
		
		try {
			
			Class<? extends MjApplication> applicationClass = (Class<? extends MjApplication>) Class.forName(APPLICATION_NAME);
			MjApplication application = applicationClass.newInstance();
			
			ClientToolkit.setToolkit(new AndroidClientToolkit(params[0]));
			applicationContext = new AndroidApplicationContext();
			application.init();
			
			System.out.println("successfully created instance");
		
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	

	
	
	public class AndroidApplicationContext extends ApplicationContext {
		private String user;

		

		@Override
		public void setUser(String user) {
			this.user = user;
		}

		@Override
		public String getUser() {
			return user;
		}
		
		@Override
		public void savePreferences(Object preferences) {
			//PreferencesHelper.save(Preferences.userNodeForPackage(SwingLauncher.this.getClass()), preferences);
		}

		@Override
		public void loadPreferences(Object preferences) {
			//PreferencesHelper.load(Preferences.userNodeForPackage(SwingLauncher.this.getClass()), preferences);
		}
	}


	
	
	
}

	

