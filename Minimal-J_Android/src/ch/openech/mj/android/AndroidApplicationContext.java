package ch.openech.mj.android;

import ch.openech.mj.application.ApplicationContext;

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
		// PreferencesHelper.save(Preferences.userNodeForPackage(SwingLauncher.this.getClass()),
		// preferences);
	}

	@Override
	public void loadPreferences(Object preferences) {
		// PreferencesHelper.load(Preferences.userNodeForPackage(SwingLauncher.this.getClass()),
		// preferences);
	}
}