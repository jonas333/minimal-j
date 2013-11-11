package ch.openech.mj.android;

import java.util.Arrays;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
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
import ch.openech.mj.toolkit.IAction;
import ch.openech.mj.toolkit.IComponent;
import ch.openech.mj.toolkit.IDialog;
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
		
		
		createLabel(gridLayout);
		createActionLabel(gridLayout);
		createTitle(gridLayout);
		createLink(gridLayout);
		createTextField(gridLayout);
		createReadOnlyTextfield(gridLayout);
		createComboBox(gridLayout);
		createCheckBox(gridLayout);
		createComponentAndCaption(gridLayout);
		createErrorDialog(gridLayout);
		createMessageDialog(gridLayout);
		createConfirmationDialog(gridLayout);
		createDialog(gridLayout);
		
		TextView l4 = new TextView(this);
		l4.setText("FlowField");
		gridLayout.addView(l4);
		
		FlowField flowField = ClientToolkit.getToolkit().createFlowField();
		for (int i = 0; i < 50; i++)
		{
			flowField.add(ClientToolkit.getToolkit().createLabel("Label" + i));
		}
		
		gridLayout.addView((View) flowField);
		
		
		TextView l8 = new TextView(this);
		l8.setText("HorizonalLayout");
		gridLayout.addView(l8);
		
		IComponent[] components = {new AndroidLabel(this, "Erster Eintrag"), new AndroidLabel(this, "Zweiter Eintrag")};
		
		gridLayout.addView((View) ClientToolkit.getToolkit().createHorizontalLayout(components));
		
		
		TextView l14 = new TextView(this);
		l14.setText("GridLayout");
		gridLayout.addView(l14);
		
		
		final AndroidGridLayout gl = new AndroidGridLayout(this, 2);
		for (int i = 0; i < 2; i++)
		{
			for (int j = 0; j < 4; j++) {
				gl.add(ClientToolkit.getToolkit().createTitle(AndroidHelper.createRandomString("GRID", 25)), 1);
			}
		}
		gl.add(ClientToolkit.getToolkit().createTitle(AndroidHelper.createRandomString("GRID", 50)), 2);
		
		gridLayout.addView(gl);
		
		
		TextView l16 = new TextView(this);
		l16.setText("Switchlayout");
		gridLayout.addView(l16);
		SwitchLayout switchLayout = ClientToolkit.getToolkit().createSwitchLayout();
		switchLayout.show(ClientToolkit.getToolkit().createLabel("A SwitchLayout Label"));
		
		gridLayout.addView((View) switchLayout);
		
		setContentView(gridLayout);
	}
	
	
	
	private void createComboBox(ViewGroup content) {
		TextView label = new TextView(this);
		label.setText("Combobox");
		content.addView(label);
		
		String[] names = {"Müller", "Schmidt", "Musolf", "Schneider"};
		final ComboBox<String> comboBox = ClientToolkit.getToolkit().createComboBox(null);
		comboBox.setObjects(Arrays.asList(names));
		
		LinearLayout comboLayout = new LinearLayout(this);
		comboLayout.setOrientation(LinearLayout.HORIZONTAL);
		comboLayout.addView((View) comboBox);
		Button bSelect = new Button(this);
		bSelect.setText("Set Selected Musolf");
		bSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				comboBox.setSelectedObject("Musolf");
			}
		});
        comboLayout.addView(bSelect);
		
		final Button bGetSelect = new Button(this);
		bGetSelect.setText("Get Selected");
		bGetSelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 bGetSelect.setText(comboBox.getSelectedObject());
			}
		});
		
		comboLayout.addView(bGetSelect);
		content.addView(comboLayout);
	}

	
	private void createCheckBox(ViewGroup content) {
		TextView label = new TextView(this);
		label.setText("Checkbox");
		content.addView(label);
		InputComponentListener l = new InputComponentListener() {
			
			@Override
			public void changed(IComponent checkbox) {
				CheckBox cb = (CheckBox) checkbox;
				System.out.println("checked : " + cb.isChecked());
			}
		};
		content.addView((View) ClientToolkit.getToolkit().createCheckBox(l, "Check me"));
	}
	
	
	private void createDialog(ViewGroup content) {
		TextView label = new TextView(this);
		label.setText("Dialog");
		content.addView(label);
		Button bDialog = new Button(this);
		bDialog.setText("Show/Hide Dialog");
		bDialog.setOnClickListener(new OnClickListener() {
			
			boolean visible = false;
			IDialog dlg;
			
			@Override
			public void onClick(View v) {
				if (!visible) {
					dlg = ClientToolkit.getToolkit().createDialog(null, "Ein Beispieldialog", ClientToolkit.getToolkit().createTextField(null, 10, null), (IAction[]) null);
					dlg.openDialog();
				} else {
					dlg.closeDialog();
				}
				visible = !visible;
			}
		});
		content.addView(bDialog);
	}
	
	
	private void createConfirmationDialog(ViewGroup content) {
		TextView label = new TextView(this);
		label.setText("Confirmation Dialog");
		content.addView(label);
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
		content.addView(confirmationDialog);
	}
	
	private void createMessageDialog(ViewGroup content) {
		TextView label = new TextView(this);
		label.setText("Message Dialog");
		content.addView(label);
		Button messageDialog = new Button(this);
		messageDialog.setText("Message Dialog");
		messageDialog.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	ClientToolkit.getToolkit().showMessage(this, "Hier könnte Ihre Information stehen");
		    }
		});
		content.addView(messageDialog);
	}
	
	private void createErrorDialog(ViewGroup content) {
		TextView label = new TextView(this);
		label.setText("Error Dialog");
		content.addView(label);
		Button errorDialog = new Button(this);
		errorDialog.setText("Error Dialog");
		errorDialog.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	ClientToolkit.getToolkit().showError(this, "Es ist ein Fehler aufgetreten !");
		    }
		});
		content.addView(errorDialog);
	}
	
	private void createLink(ViewGroup content) {
		TextView label = new TextView(this);
		label.setText("Link");
		content.addView(label);
		content.addView((View) ClientToolkit.getToolkit().createLink("", "http://www.google.de"));
	}
	
	
	private void createTitle(ViewGroup content) {
		TextView label = new TextView(this);
		label.setText("Ueberschrift");
		content.addView(label);
		content.addView( (View) ClientToolkit.getToolkit().createTitle("Dies ist eine Ueberschrift"));
	}
	
	
	private void createComponentAndCaption(ViewGroup content) {
		TextView label = new TextView(this);
		label.setText("Component with Caption");
		content.addView(label);
		IComponent et = ClientToolkit.getToolkit().createTextField(null, 10);
		content.addView((View) new AndroidSampleCaption(this, "Eingabe", (View) et));
	}
	
	private void createReadOnlyTextfield(ViewGroup content) {
		TextView label = new TextView(this);
		label.setText("ReadOnly Textfeld");
		content.addView(label);
		content.addView( (View) ClientToolkit.getToolkit().createReadOnlyTextField());
	}
	
	private void createActionLabel(GridLayout content) {
		TextView label = new TextView(this);
		label.setText("Action Label");
		content.addView(label);
		content.addView((View) ClientToolkit.getToolkit().createLabel(new AndroidDummyAction()));
	}

	private void createTextField(ViewGroup content) {
		TextView label = new TextView(this);
		label.setText("TextField");
		content.addView(label);
		TextField textField = ClientToolkit.getToolkit().createTextField(new InputComponentListener() {
			@Override
			public void changed(IComponent component) {
				ClientToolkit.getToolkit().showMessage(null, ((TextField) component).getInput() );
			}
		}, 2, "aAa");
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
		content.addView((View) textField);
		
	}
	
	
	private void createLabel(ViewGroup contentView) {
		TextView label = new TextView(this);
		label.setText("Label");
		contentView.addView(label);
		IComponent actionLabel = ClientToolkit.getToolkit().createLabel("Android Action Label");
		contentView.addView((View) actionLabel);
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
