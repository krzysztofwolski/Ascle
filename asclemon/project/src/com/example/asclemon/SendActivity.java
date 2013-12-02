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

public class SendActivity extends Activity implements OnClickListener{
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_message_activity);
		// Show the Up button in the action bar.
		Button btn1 = (Button) this.findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		Button btn2 = (Button) this.findViewById(R.id.button2);
		btn2.setOnClickListener(this);		
	}
	
	public void onClick(View v){
		
		if(v.getId() == R.id.button1)
		{
			DataBase db = DataBase.INSTANCE;

			EditText getMessage = (EditText)findViewById(R.id.editText1);
			EditText getID = (EditText)findViewById(R.id.editText2);
			if(getMessage.getText().length() != 0 && getID.getText().length() != 0){
				int id =  Integer.valueOf(getID.getText().toString()); 

				db.sendMessage(id, getMessage.getText().toString());
			}
			super.onBackPressed();
		}
		if(v.getId() == R.id.button2)
		{
			super.onBackPressed();
		}
	}
	


}
