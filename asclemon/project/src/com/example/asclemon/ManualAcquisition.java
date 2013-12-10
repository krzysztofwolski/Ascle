package com.example.asclemon;

import java.util.Date;

import com.example.asclemon.database.DataBase;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class ManualAcquisition extends Activity implements OnClickListener{
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manual_new);
		// Show the Up button in the action bar.
		setupActionBar();	    
		Button btn1 = (Button) this.findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		Button btn2 = (Button) this.findViewById(R.id.button2);
		btn2.setOnClickListener(this);		
	}
	
	public void onClick(View v){
		
		if(v.getId() == R.id.button1)
		{
			DataBase db = DataBase.INSTANCE;

			EditText getValue = (EditText)findViewById(R.id.editText1);
			if(getValue.getText().length() != 0){
				float value =  Float.valueOf(getValue.getText().toString()); 
				db.getSensor("termometr 2000").addNewMeasure(value, new Date());
			}
			
			EditText getValue2 = (EditText)findViewById(R.id.editText3);
			if(getValue2.getText().length() != 0){
				float value =  Float.valueOf(getValue2.getText().toString()); 
					db.getSensor("Miernik 2000").addNewMeasure(value, new Date());
			}
			EditText getValue3 = (EditText)findViewById(R.id.editText4);
			if(getValue3.getText().length() != 0){
				float value =  Float.valueOf(getValue3.getText().toString()); 
					db.getSensor("Miernik 2000 (rozkurczowe)").addNewMeasure(value, new Date());
			}
			EditText getValue4 = (EditText)findViewById(R.id.editText5);
			if(getValue4.getText().length() != 0){
				float value =  Float.valueOf(getValue4.getText().toString()); 
					db.getSensor("Pulsometr 2000").addNewMeasure(value, new Date());
			}
			EditText getValue5 = (EditText)findViewById(R.id.editText2);
			if(getValue5.getText().length() != 0){
				float value =  Float.valueOf(getValue5.getText().toString()); 
					db.getSensor("Waga 2000").addNewMeasure(value, new Date());

			}
			super.onBackPressed();
		}
		if(v.getId() == R.id.button2)
		{
			super.onBackPressed();
		}
	}
	
	public void wyswietl(String string){
		TextView txt = (TextView) findViewById(R.id.textView1);
		txt.setText(string);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manual_acquisition, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void backButton(View view) {
		Intent intent = new Intent(this, MainMenu.class);
	    startActivity(intent);
	}
	

}
