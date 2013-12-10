package com.example.asclemon;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.asclemon.database.DataBase;
import com.example.asclemon.database.Measure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.content.Context;

public class GraphsActivity extends Activity implements OnClickListener  {
	/*private String[] mMonth = new String[] {
				"Jan", "Feb" , "Mar", "Apr", "May", "Jun",
				"Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"
			};*/
	Context context;
	CheckBox box1,box2,box3,box4,box5;
	List<Float> listaTemperatur = new ArrayList<Float>();
	List<Float> listaWag = new ArrayList<Float>();
	List<Float> listaPuls = new ArrayList<Float>();
	List<Float> listaMiernik1 = new ArrayList<Float>();
	List<Float> listaMiernik2 = new ArrayList<Float>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
		context = this.getApplicationContext();
		
		box1 = (CheckBox) findViewById(R.id.checkBox1);
		box2= (CheckBox) findViewById(R.id.checkBox2);
		box3 = (CheckBox) findViewById(R.id.checkBox3);
		box4 = (CheckBox) findViewById(R.id.checkBox4);
		box5 = (CheckBox) findViewById(R.id.checkBox5);	 
		
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
		
		lista = db.getSensor("Pulsometr 2000").getMeasures();
		for (int i = 0; i< lista.size();i++){
			listaPuls.add((lista.get(i).value));
		}
		lista = db.getSensor("Miernik 2000 (rozkurczowe)").getMeasures();
		for (int i = 0; i< lista.size();i++){
			listaMiernik2.add((lista.get(i).value));
		}
		
		lista = db.getSensor("Miernik 2000").getMeasures();
		for (int i = 0; i< lista.size();i++){
			listaMiernik1.add((lista.get(i).value));
		}
		Button btn1 = (Button) this.findViewById(R.id.btn_chart);
		btn1.setOnClickListener(this);
		Button btn2 = (Button) this.findViewById(R.id.button1);
		btn2.setOnClickListener(this);
		Button btn3 = (Button) this.findViewById(R.id.button2);
		btn3.setOnClickListener(this);
    }
    private void openStat(){
    	Intent intent = new Intent(context, Stats.class);
		startActivity(intent);
    }
    
    
    private void openChart(){
    	XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();

    	if(box1.isChecked()){
	    	int[] temperatury = new int [listaTemperatur.size()];
	    	float[] temperatura = new float[listaTemperatur.size()];	
	    	for(int i =0;i<listaTemperatur.size();i++){
	    		temperatura[i]=listaTemperatur.get(i);
	    	}
	    	XYSeries pomiaryTemperatury = new XYSeries("Temperatura");
	    	for(int i=0;i<temperatury.length;i++){
	    		pomiaryTemperatury.add(temperatury[i],temperatura[i]);
	    	}
	    	dataset.addSeries(pomiaryTemperatury);    	
	    	
	    	XYSeriesRenderer tempRenderer = new XYSeriesRenderer();
	    	tempRenderer.setColor(Color.BLUE);
	    	tempRenderer.setPointStyle(PointStyle.CIRCLE);
	    	tempRenderer.setFillPoints(true);
	    	tempRenderer.setLineWidth(2);
	    	tempRenderer.setDisplayChartValues(true);
	    	multiRenderer.addSeriesRenderer(tempRenderer);

    	}
    	/////////////////////////////////
    	if(box2.isChecked()){
	    	int[] waga = new int [listaWag.size()];
	    	float[] wagi = new float[listaWag.size()];
	    	for(int i =0;i<listaWag.size();i++){
	    		wagi[i]=listaWag.get(i);
	    	}
	    	XYSeries pomiaryWaga = new XYSeries("Waga");
	    	for(int i=0;i<waga.length;i++){
	    		pomiaryWaga.add(waga[i], wagi[i]);
	    	}
	    	dataset.addSeries(pomiaryWaga);
	    	
	    	XYSeriesRenderer wagaRenderer = new XYSeriesRenderer();
	    	wagaRenderer.setColor(Color.RED);
	    	wagaRenderer.setPointStyle(PointStyle.CIRCLE);
	    	wagaRenderer.setFillPoints(true);
	    	wagaRenderer.setLineWidth(2);
	    	wagaRenderer.setDisplayChartValues(true);
	    	multiRenderer.addSeriesRenderer(wagaRenderer);

    	}
    	/////////////////////////////////
    	if(box5.isChecked()){
	      	int[] pulsSize = new int [listaPuls.size()];
	    	float[] pulsValue = new float[listaPuls.size()];
	    	for(int i =0;i<listaPuls.size();i++){
	    		pulsValue[i]=listaPuls.get(i);
	    	}
	    	XYSeries pomiarPuls = new XYSeries("Puls");
	    	for(int i=0;i<pulsSize.length;i++){
	    		pomiarPuls.add(pulsSize[i], pulsValue[i]);
	    	}
	    	dataset.addSeries(pomiarPuls);
	    	
	    	XYSeriesRenderer pulsRenderer = new XYSeriesRenderer();
	    	pulsRenderer.setColor(Color.GREEN);
	    	pulsRenderer.setPointStyle(PointStyle.CIRCLE);
	    	pulsRenderer.setFillPoints(true);
	    	pulsRenderer.setLineWidth(2);
	    	pulsRenderer.setDisplayChartValues(true);
	    	multiRenderer.addSeriesRenderer(pulsRenderer);

    	}
    	/////////////////////////////////
    	if(box3.isChecked()){
	      	int[] miernik1Size = new int [listaMiernik1.size()];
	    	float[] miernik1Value = new float[listaMiernik1.size()];
	    	for(int i =0;i<listaMiernik1.size();i++){
	    		miernik1Value[i]=listaMiernik1.get(i);
	    	}
	    	XYSeries pomiaryMiernik1 = new XYSeries("Miernik1");
	    	for(int i=0;i<miernik1Size.length;i++){
	    		pomiaryMiernik1.add(miernik1Size[i], miernik1Value[i]);
	    	}
	    	dataset.addSeries(pomiaryMiernik1);
	    	
	    	XYSeriesRenderer miernik1Renderer = new XYSeriesRenderer();
	    	miernik1Renderer.setColor(Color.YELLOW);
	    	miernik1Renderer.setPointStyle(PointStyle.CIRCLE);
	    	miernik1Renderer.setFillPoints(true);
	    	miernik1Renderer.setLineWidth(2);
	    	miernik1Renderer.setDisplayChartValues(true);
	    	multiRenderer.addSeriesRenderer(miernik1Renderer);

    	}
    	/////////////////////////////////
    	if(box4.isChecked()){
	      	int[] miernik2Size = new int [listaMiernik2.size()];
	    	float[] miernik2Value = new float[listaMiernik2.size()];
	    	for(int i =0;i<listaMiernik2.size();i++){
	    		miernik2Value[i]=listaMiernik2.get(i);
	    	}
	    	XYSeries pomiaryMiernik2 = new XYSeries("Miernik2");
	    	for(int i=0;i<miernik2Size.length;i++){
	    		pomiaryMiernik2.add(miernik2Size[i], miernik2Value[i]);
	    	}
	    	dataset.addSeries(pomiaryMiernik2);  
	    	
	    	XYSeriesRenderer miernik2Renderer = new XYSeriesRenderer();
	    	miernik2Renderer.setColor(Color.CYAN);
	    	miernik2Renderer.setPointStyle(PointStyle.CIRCLE);
	    	miernik2Renderer.setFillPoints(true);
	    	miernik2Renderer.setLineWidth(2);
	    	miernik2Renderer.setDisplayChartValues(true);
	    	multiRenderer.addSeriesRenderer(miernik2Renderer);

    	}

    	multiRenderer.setXLabels(0);
    	multiRenderer.setChartTitle("Wykres pomiarow");
    	multiRenderer.setXTitle("Próbki");
    	multiRenderer.setYTitle("Jednostka");
    	multiRenderer.setZoomButtonsVisible(true);    	    	
    	//for(int i=0;i<x.length;i++){
    		//multiRenderer.addXTextLabel(i+1, mMonth[i]);    		
    	//}    	
    	
    	Intent intent = ChartFactory.getLineChartIntent(getBaseContext(), dataset, multiRenderer);
    	startActivity(intent);
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.graphs, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btn_chart)
		{
			openChart();
		}
		if(v.getId() == R.id.button1)
		{
			openStat();	
		}
		if(v.getId() == R.id.button2)
		{
			super.onBackPressed();
		}	
	}


}