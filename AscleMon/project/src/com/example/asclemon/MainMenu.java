package com.example.asclemon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.asclemon.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainMenu extends Activity {
	
	 public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	View controlsView;
	View contentView;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_menu);

		controlsView  = findViewById(R.id.fullscreen_content_controls);
		contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		
	}
	
	
	public void goToAutoAcquisitionMenu(View view){
		Intent intent = new Intent(this, AutoAcquisition.class);
	    startActivity(intent);
	}
	
	public void goToManualAcquisitionMenu(View view){
		Intent intent = new Intent(this, ManualAcquisition.class);
	    startActivity(intent);
	}
	
	public void goToGraphsMenu(View view){
		Intent intent = new Intent(this, GraphsActivity.class);
	    startActivity(intent);
	}
	
	public void goToDrugsMenu(View view){
		Intent intent = new Intent(this, DrugsActivity.class);
	    startActivity(intent);
	}
	
}
