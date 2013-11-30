package com.example.asclemon;

import java.util.Date;
import java.util.Random;

import com.example.asclemon.database.DataBase;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;   

public class MyReceiver extends Service {
	DataBase db = DataBase.INSTANCE;
	public static Date d= new Date();
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	d = new Date();
        Toast.makeText(this, "add Measure", Toast.LENGTH_LONG).show();
        Random rand = new Random();
        float final1 = rand.nextFloat() * 4 + 36;
        float final2 = rand.nextFloat() * 4 + 80;
        float final3 = rand.nextFloat() * 40 + 110;
        
        final1 *= 100; 
        final1 = Math.round(final1);
        final1 /= 100;
        
        final2 *= 100; 
        final2 = Math.round(final2);
        final2 /= 100;
        
        final3 *= 100; 
        final3 = Math.round(final2);
        final3 /= 100; 
        
        db.getSensor("termometr 2000").addNewMeasure(final1, d);
        db.getSensor("Waga 2000").addNewMeasure(final2, d);
        db.getSensor("Pulsometr 2000").addNewMeasure(final3, d);

        return START_STICKY;
    }

    public static Date aktualizacjaDaty(){
    	return d; 
    }
    
    
    @Override
    public void onDestroy() {

        super.onDestroy();
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
	
}
