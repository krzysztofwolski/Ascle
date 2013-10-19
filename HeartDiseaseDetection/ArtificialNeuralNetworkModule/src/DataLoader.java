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
		String addr;
		for(Table t : Table.values()){
			addr = "";
			switch (t){
			case sensor:
				addr = this.srvAddr + "sensor/" + this.patient_id;
				this.responses.add(new Gson().fromJson(this.getJson(addr),SensorResponse.class));
				break;
			case sensortype:
				int sensorTypeID = this.responses.get(0).getForeginKey();	/*TODO: this is lame solution, but it is sufficient*/
				addr = this.srvAddr + "sensortype/" + sensorTypeID;
				this.responses.add(new Gson().fromJson(this.getJson(addr),SensorTypesResponse.class));
				break;
			case measure:
				addr = this.srvAddr + "measure/" + this.patient_id;
				this.responses.add(new Gson().fromJson(this.getJson(addr),MeasuresResponse.class));
				break;
			}
		}	
	}	
}


abstract class Response{
	 Integer id;
	 public abstract Integer getForeginKey();
	 public abstract Integer getPrimaryKey();
}

class SensorResponse extends Response{
	
	Integer sensor_type_id;
	Integer user_id;
	Float alert_min;
	Float alert_max;
	Float warning_min;
    Float warning_max;
    
    @Override
    /*TODO: This method returns only ONE FK, need to change this to all FK just for correctness*/
    public Integer getForeginKey(){
    	return this.sensor_type_id;
    }
    
	@Override
	public Integer getPrimaryKey() {
		return this.id;
	}
}

class SensorTypesResponse extends Response{
	String name;
	String unit;
	Boolean automatic;
	
	@Override
    public Integer getForeginKey(){
		throw new RuntimeException("No foregin key in this table! This table should not even exist!!!");
	}

	@Override
	public Integer getPrimaryKey() {
		return this.id;
	}
}

class MeasuresResponse extends Response{
	Integer sensor_id;
	Float value;
	String timestamp;
	
	@Override
    public Integer getForeginKey(){
		return this.sensor_id;
	}
	
	@Override
	public Integer getPrimaryKey() {
		return this.id;
	}
}


