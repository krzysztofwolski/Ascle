package com.example.asclemon;

import java.util.ArrayList;
import java.util.List;

import com.example.asclemon.database.DataBase;
import com.example.asclemon.database.Measure;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Stats extends Activity  implements OnClickListener  {
	
	Context context;
	List<Float> listaTemperatur = new ArrayList<Float>();
	List<Float> listaWag = new ArrayList<Float>();
	List<Float> listaPulsu = new ArrayList<Float>();
	List<Float> listaMiernik1 = new ArrayList<Float>();
	List<Float> listaMiernik2 = new ArrayList<Float>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		context = this.getApplicationContext();
		
		Button btn1 = (Button) this.findViewById(R.id.button1);
		btn1.setOnClickListener(this);
		DataBase db = DataBase.INSTANCE;
		ArrayList<Measure> lista = new ArrayList();
		lista = db.getSensor("termometr 2000").getMeasures();
		for (int i = 0; i< lista.size();i++){
			listaTemperatur.add((lista.get(i).value));
		}
		lista = db.getSensor("Waga 2000").getMeasures();
		for (int i = 0; i< lista.size();i++){
			listaWag.add((lista.get(i).value));
		}
		lista = db.getSensor("Miernik 2000").getMeasures();
		for (int i = 0; i< lista.size();i++){
			listaMiernik1.add((lista.get(i).value));
		}
		lista = db.getSensor("Miernik 2000 (rozkurczowe)").getMeasures();
		for (int i = 0; i< lista.size();i++){
			listaMiernik2.add((lista.get(i).value));
		}
		lista = db.getSensor("Pulsometr 2000").getMeasures();
		for (int i = 0; i< lista.size();i++){
			listaPulsu.add((lista.get(i).value));
		}
		
		TextView txt = (TextView) findViewById(R.id.textView2);
		TextView txt2 = (TextView) findViewById(R.id.textView4);
		TextView txt3 = (TextView) findViewById(R.id.textView6 );
		TextView txt4 = (TextView) findViewById(R.id.textView8);
		TextView txt5 = (TextView) findViewById(R.id.textView10);
		txt.setText(listaTemperatur.toString());
		txt2.setText(listaWag.toString());
		txt3.setText(listaMiernik1.toString());
		txt4.setText(listaMiernik2.toString());
		txt5.setText(listaPulsu.toString());
	}
	
	
	@Override
	public void onClick(View v) {	
		if(v.getId() == R.id.button1)
		{
			super.onBackPressed();
		}
	}
}