import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataLoader {
	private int patient_id;
	private final String srvAddr = "http://kuchnia.mooo.com:5000/api/";
	private List<Response> responses;
	private enum Table{
		sensor,sensortype,measure
	}
	
	DataLoader(int id){
		this.patient_id = id;
		responses = new ArrayList<Response>();
	}
	
	private String getJson(String addr) throws IOException{
		URL url = new URL(addr);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line;
		StringBuffer output = new StringBuffer();
		while((line = in.readLine())!= null){
			output.append(line);
		}
		
		return output.toString();
	}
	
	@SuppressWarnings("incomplete-switch")
	public void load() throws JsonSyntaxException, IOException{
		List<Response> responses = new ArrayList<Response>();
		String addr;
		for(Table t : Table.values()){
			addr = "";
			switch (t){
			case sensor:
				addr = this.srvAddr + "sensor/" + this.patient_id;
				responses.add(new Gson().fromJson(this.getJson(addr),SensorResponse.class));
				break;
			case sensortype:
				addr = this.srvAddr + "sensortype/" + this.patient_id;
				responses.add(new Gson().fromJson(this.getJson(addr),SensorTypesResponse.class));
				break;
			case measure:
				addr = this.srvAddr + "measure/" + this.patient_id;
				responses.add(new Gson().fromJson(this.getJson(addr),MeasuresResponse.class));
				break;
			}
		}	
	}
	
}


abstract class Response{
	 Integer id;
}

class SensorResponse extends Response{
	Integer sensor_type_id;
	Integer user_id;
	Float alert_min;
	Float alert_max;
	Float warning_min;
    Float warning_max;
}

class SensorTypesResponse extends Response{
	String name;
	String unit;
	Boolean automatic;
}

class MeasuresResponse extends Response{
	Integer sensor_id;
	Float value;
	String timestamp;
}

