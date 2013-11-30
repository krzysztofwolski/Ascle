package com.example.asclemon;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
	PendingIntent pendingIntent;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main_menu);

		controlsView  = findViewById(R.id.fullscreen_content_controls);
		contentView = findViewById(R.id.fullscreen_content);

		
		// DATABASE INITIALIZE --------------------------------------------------------------
		// start DataBase updating routine
		db.setContext(getBaseContext());
		db.setUserLogin("test123","test123");  // <--- tutaj ustawiasz usera
		db.sendMessage(123, "jakas tam wiadomoœc testowa"); // <--- w taki sposób wysylasz wiadomoœæ pierwsze to id do któreg owysy³am a 2 parametr to treœæ, (uwaga nie jest filtrowana w ¿aden sposób wiêc wpuszczenie tam g³upiego znaku mo¿e powodowaæ blêd)
		Thread dataBase = new Thread(db, "dataBaseUpdate");
		dataBase.start();
		//example how to add new measure 
		// adding new measure --------------------------------------------------------------
	//	db.getSensor("termometr 2000").addNewMeasure(999.0f, new Date());
		//and its all.
		
		/*Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 10);
		Intent intent = new Intent(MainMenu.this, MyReceiver.class);
		PendingIntent pintent = PendingIntent.getService(MainMenu.this, 0, intent, 0);
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),30 *20 * 1000, pintent);*/
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
