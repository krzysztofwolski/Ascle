package com.example.asclemon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.asclemon.database.DataBase;

public class LogActivity extends Activity implements OnClickListener{
    
	public static String login = "testuj5";
	public static String pass = "asdf";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		// Show the Up button in the action bar.
		Button btn1 = (Button) this.findViewById(R.id.button1);
		btn1.setOnClickListener(this);
	
	}
	
	public void onClick(View v){
		
		if(v.getId() == R.id.button1)
		{
			EditText loginA = (EditText)findViewById(R.id.editText1);
			EditText hasloA = (EditText)findViewById(R.id.editText2);
			String log =loginA.getText().toString();
			String pass2 =  hasloA.getText().toString();
			//if(log.equals(login) || hasloA.getText().toString().equals(pass))
			if(loginA.getText().length()!=0 && hasloA.getText().length()!=0){
				Context context = getApplicationContext();

				login =log;
				pass = hasloA.getText().toString();
			//	DataBase db = DataBase.INSTANCE;
			//	db.setUserLogin(loginA.getText().toString(),hasloA.getText().toString());
			
			//	db.setContext(getBaseContext());
			//	Thread dataBase = new Thread(db, "dataBaseUpdate");
			//	dataBase.start();
			
				CharSequence text = "Witaj " + login;
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				Intent intent = new Intent(this, MainMenu.class);
				startActivity(intent);

			}
			else{
				Context context = getApplicationContext();

				CharSequence text ="b³êdne has³o";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				
			}

			
		}
	}
	


}
