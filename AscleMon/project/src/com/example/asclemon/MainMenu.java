package com.example.asclemon;

import java.util.Date;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.asclemon.database.DataBase;
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
	// DATABASE INITIALIZE --------------------------------------------------------------
	DataBase db = DataBase.INSTANCE;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_menu);

		controlsView  = findViewById(R.id.fullscreen_content_controls);
		contentView = findViewById(R.id.fullscreen_content);

		
		// DATABASE INITIALIZE --------------------------------------------------------------
		// start DataBase updating routine
		db.setContext(getBaseContext());
		db.setUserLogin("test123","test123");
		Thread dataBase = new Thread(db, "dataBaseUpdate");
		dataBase.start();
		//example how to add new measure 
		// adding new measure --------------------------------------------------------------
		db.getSensor("termometr 2000").addNewMeasure(999.0f, new Date());
		//and its all.
	}
	
	
	public void goToAutoAcquisitionMenu(View view){
		System.out.println("startuje");
		Intent intent = new Intent(this, AutoAcquisition.class);

	    startActivity(intent);
	}
	
	public void goToManualAcquisitionMenu(View view){
		//db.syncUser();
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
