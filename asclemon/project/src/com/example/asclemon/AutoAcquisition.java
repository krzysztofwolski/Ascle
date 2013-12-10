package com.example.asclemon;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.asclemon.database.DataBase;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AutoAcquisition extends Activity implements OnClickListener{
		
	TextView text;
	DataBase db = DataBase.INSTANCE;
	Date d;
	SimpleDateFormat f= new SimpleDateFormat("E yyyy.MM.dd \n'godz.' HH:mm:ss");
	PendingIntent pendingIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_new_one);
		text = (TextView) findViewById(R.id.textView1);		

		d = MyReceiver.aktualizacjaDaty();
		text.setText("data ostatniej aktualizacji: \n" +(f.format(d)).toString());

		Button btn1 = (Button) this.findViewById(R.id.button3);
		btn1.setOnClickListener(this);
		Button btn2 = (Button) this.findViewById(R.id.button2);
		btn2.setOnClickListener(this);
		Button btn3 = (Button) this.findViewById(R.id.button1);
		btn3.setOnClickListener(this);
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.auto_acquisition, menu);
		return true;
	}
	
	public void backButton(View view) {
		Intent intent = new Intent(this, MainMenu.class);
	    startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button3)
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, 10);
			Intent intent = new Intent(AutoAcquisition.this, MyReceiver.class);
			PendingIntent pintent = PendingIntent.getService(AutoAcquisition.this, 0, intent, 0);
			AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),30 *20 * 1000, pintent);
			startService(new Intent(getBaseContext(), MyReceiver.class));
		}
		if(v.getId() == R.id.button2)
		{
			 stopService(new Intent(getBaseContext(), MyReceiver.class));
		}
		if(v.getId() == R.id.button1)
		{
			super.onBackPressed();
		}
	}

}
