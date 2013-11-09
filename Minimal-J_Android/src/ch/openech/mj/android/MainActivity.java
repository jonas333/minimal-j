package ch.openech.mj.android;

import java.util.Arrays;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import ch.openech.mj.android.toolkit.AndroidCaption;
import ch.openech.mj.android.toolkit.AndroidClientToolkit;
import ch.openech.mj.android.toolkit.AndroidDummyAction;
import ch.openech.mj.android.toolkit.AndroidGridLayout;
import ch.openech.mj.android.toolkit.AndroidLabel;
import ch.openech.mj.application.ApplicationContext;
import ch.openech.mj.application.MjApplication;
import ch.openech.mj.toolkit.CheckBox;
import ch.openech.mj.toolkit.ClientToolkit;
import ch.openech.mj.toolkit.ClientToolkit.ConfirmDialogType;
import ch.openech.mj.toolkit.ClientToolkit.DialogListener;
import ch.openech.mj.toolkit.ClientToolkit.InputComponentListener;
import ch.openech.mj.toolkit.ComboBox;
import ch.openech.mj.toolkit.FlowField;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IFocusListener;
import ch.openech.mj.toolkit.SwitchLayout;
import ch.openech.mj.toolkit.TextField;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initApplication();
		initActivity();
		
	}

	private void initActivity() {
		GridLayout gridLayout = new GridLayout(this);
		gridLayout.setColumnCount(2);
		gridLayout.setRowCount(5);
		
		//gridLayout.setPadding(10, 10, 10, 10);
		
		
		TextView l1 = new TextView(this);
		l1.setText("Label");
		gridLayout.addView(l1);
		gridLayout.addView((View) ClientToolkit.getToolkit().createLabel("Android Launcher"));
		
		
		
		
		TextView l2 = new TextView(this);
		l2.setText("Action Label");
		gridLayout.addView(l2);
		gridLayout.addView((View) ClientToolkit.getToolkit().createLabel(new AndroidDummyAction()));
		
		
		
		TextField textField = ClientToolkit.getToolkit().createTextField(new InputComponentListener() {
			@Override
			public void changed(IComponent component) {
				ClientToolkit.getToolkit().showMessage(null, ((TextField) component).getInput() );
			}
		}, 0);
		textField.setFocusListener(new IFocusListener() {

			@Override
			public void onFocusGained() {
				System.out.println("onFocusGained()");
			}

			@Override
			public void onFocusLost() {
				System.out.println("onFocusLost()");
			}
			
		});
		
		TextView l3 = new TextView(this);
		l3.setText("TextField");
		gridLayout.addView(l3);
		
		gridLayout.addView((View ) textField);
		
		
		TextView l4 = new TextView(this);
		l4.setText("FlowField");
		gridLayout.addView(l4);
		
		FlowField flowField = ClientToolkit.getToolkit().createFlowField();
		for (int i = 0; i < 50; i++)
		{
			flowField.add(ClientToolkit.getToolkit().createLabel("Label" + i));
		}
		
		gridLayout.addView((View) flowField);
		
		TextView l5 = new TextView(this);
		l5.setText("Combobox");
		gridLayout.addView(l5);
		
		String[] names = {"Müller", "Schmidt", "Musolf", "Schneider"};
		ComboBox<String> comboBox = ClientToolkit.getToolkit().createComboBox(null);
		comboBox.setObjects(Arrays.asList(names));
		gridLayout.addView((View) comboBox  );
		
		TextView l6 = new TextView(this);
		l6.setText("Checkbox");
		gridLayout.addView(l6);
		InputComponentListener l = new InputComponentListener() {
			
			@Override
			public void changed(IComponent checkbox) {
				CheckBox cb = (CheckBox) checkbox;
				System.out.println("checked : " + cb.isChecked());
			}
		};
		gridLayout.addView((View) ClientToolkit.getToolkit().createCheckBox(l, "Check me"));
		
		
		TextView l7 = new TextView(this);
		l7.setText("Component with Caption");
		gridLayout.addView(l7);
		
		IComponent et = ClientToolkit.getToolkit().createTextField(null, 10);
		gridLayout.addView((View) new AndroidSampleCaption(this, "Eingabe", (View) et));
		
		TextView l8 = new TextView(this);
		l8.setText("HorizonalLayout");
		gridLayout.addView(l8);
		
		IComponent[] components = {new AndroidLabel(this, "Erster Eintrag"), new AndroidLabel(this, "Zweiter Eintrag")};
		
		gridLayout.addView((View) ClientToolkit.getToolkit().createHorizontalLayout(components));
		
		TextView l9 = new TextView(this);
		l9.setText("Ueberschrift");
		gridLayout.addView(l9);
		gridLayout.addView( (View) ClientToolkit.getToolkit().createTitle("Dies ist eine Ueberschrift"));
		
		
		TextView l10 = new TextView(this);
		l10.setText("ReadOnly Textfeld");
		gridLayout.addView(l10);
		gridLayout.addView( (View) ClientToolkit.getToolkit().createReadOnlyTextField());
		
		
		TextView l14 = new TextView(this);
		l14.setText("GridLayout");
		gridLayout.addView(l14);
		
		
		AndroidGridLayout gl = new AndroidGridLayout(this, 2);
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 4; j++) {
				gl.add(ClientToolkit.getToolkit().createTitle(AndroidHelper.createRandomString("GRID", 25)), 1);
			}
		}
		gl.add(ClientToolkit.getToolkit().createTitle(AndroidHelper.createRandomString("GRID", 50)), 2);
		
		gridLayout.addView(gl);
		
		

		TextView l15 = new TextView(this);
		l15.setText("Link");
		gridLayout.addView(l15);
		gridLayout.addView((View) ClientToolkit.getToolkit().createLink("", "http://www.google.de"));
		
		
		TextView l16 = new TextView(this);
		l16.setText("Switchlayout");
		gridLayout.addView(l16);
		SwitchLayout switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayout.show(ClientToolkit.getToolkit().createLabel("A SwitchLayout Label"));
		
		gridLayout.addView((View) switchLayout);
		
		
		TextView l11 = new TextView(this);
		l11.setText("Error Dialog");
		gridLayout.addView(l11);
		Button errorDialog = new Button(this);
		errorDialog.setText("Error Dialog");
		errorDialog.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	ClientToolkit.getToolkit().showError(this, "Es ist ein Fehler aufgetreten !");
		    }
		});
		gridLayout.addView(errorDialog);
		
		
		TextView l12 = new TextView(this);
		l12.setText("Message Dialog");
		gridLayout.addView(l12);
		Button messageDialog = new Button(this);
		messageDialog.setText("Message Dialog");
		messageDialog.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	ClientToolkit.getToolkit().showMessage(this, "Hier könnte Ihre Information stehen");
		    }
		});
		gridLayout.addView(messageDialog);
		
		
		TextView l13 = new TextView(this);
		l13.setText("Confirmation Dialog");
		gridLayout.addView(l13);
		Button confirmationDialog = new Button(this);
		confirmationDialog.setText("Confirmation Dialog");
		confirmationDialog.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	ClientToolkit.getToolkit().showConfirmDialog(null, "Bestätigen", "Wollen Sie wirklich den Rechner herunterfahren ?", ConfirmDialogType.YES_NO_CANCEL, new DialogListener() {
		    		public void close(Object result) {
		    			System.out.println(result);
		    		}
		    	});
		    }
		});
		gridLayout.addView(confirmationDialog);
		
		
		
		
		
		setContentView(gridLayout);
	}

	@SuppressWarnings("unchecked")
	private void initApplication() {
		Class<? extends MjApplication> applicationClass;
		try {
			applicationClass = (Class<? extends MjApplication>) Class.forName("ch.openech.mj.example.MjExampleApplication");
			MjApplication application = applicationClass.newInstance();
			ClientToolkit.setToolkit(new AndroidClientToolkit(this));
			ApplicationContext applicationContext = new AndroidApplicationContext();
			application.init();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	
	private static class AndroidSampleCaption extends AndroidCaption {

		
		private static final String[] validationMsg = {"Sie haben eine falsche Eingabe gemacht !", "Bitte korrigieren"};
		private boolean toggle = true;
		
		public AndroidSampleCaption(Context context, String text, View view) {
			super(context, text, view);
		}


		@Override
		public boolean onTouchEvent(MotionEvent event) {
			toggle = !toggle;
			if (toggle) {
				setValidationMessages(Arrays.asList(validationMsg));
			} else {
				setValidationMessages(Collections.<String>emptyList());
			}
			return super.onTouchEvent(event);
		}
		
	}
	
}
