package com.example.asclemon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

public class Read extends Activity implements OnClickListener{
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_received_mess);
		// Show the Up button in the action bar.
        DataBase db = DataBase.INSTANCE;
		TextView txt = (TextView) findViewById(R.id.textView1);
		
		List<String> lista = new ArrayList<String>();
		for (int i = 0; i< db.received.size();i++){
			lista.add("Od: " +db.received.get(i).SenderId + "  ,Treœæ: " +db.received.get(i).text+ "\n");
		}
		txt.setText(lista.toString());     
		Button btn1 = (Button) this.findViewById(R.id.button1);
		btn1.setOnClickListener(this);

	}
	
	public void onClick(View v){
		
		if(v.getId() == R.id.button1)
		{
			super.onBackPressed();
		}

	}
	


}
