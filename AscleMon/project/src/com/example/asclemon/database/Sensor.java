package com.example.asclemon.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Sensor {
	
String name;
int id;
int sensor_type_id;
float alert_min;
float alert_max;
float warning_min;
float warning_max;

private ArrayList<Measure> measures;

String unit;
Boolean automatic;

Date lastUpdateTime;

	public Sensor(){
		measures = new ArrayList<Measure>();
		
	}

	public void addNewMeasure(float value, Date time){
		Measure measure = new Measure();
		measure.value = value;
		measure.time = time;
		measures.add(measure);	
		
		 Collections.sort(measures,new Comparator<Measure>(){

			    @Override
			    public int compare(Measure d1, Measure d2) {
			        if(d1.time.after(d2.time)){
			            return 1;
			        } else {
			            return -1;
			        }
			    }
			 
		 });
	}

	public ArrayList<Measure> getMeasures() {
		return measures;
	}
	
	

	

}
