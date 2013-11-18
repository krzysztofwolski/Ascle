import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;



/*
 * cp - Ból klatki piersiowej
 * trestbps - Ci śnienie krwi - spoczynek
 * chol - Cholesterol
 * fbs - Wysoki poziom cukru
 * restecg - Elektrokardiograf
 * thalach - Max. puls
 * exang - Przebyta angina
 * slope - Obnizenie ST
 * ca - Naczynek na flurorosopii
 * thal - Thal
 */
public class DataLoader {
	private String cookie;
	//private String cookieName;
	//private String cookieValue;
	private int patient_id;
	private final String srvAddr = "http://kuchnia.mooo.com:5000/api/";
	private List<Response> responses;
	private List<MeasuresResponse> measuresResponses;
	private enum Table{
		user,sensor,measure,sensortype
	}
	
	DataLoader(int id) throws IOException{
		this.logIn();
		this.patient_id = id;
		responses = new ArrayList<Response>();
		measuresResponses = new ArrayList<MeasuresResponse>();
	}
	private void logIn() throws IOException{
		String charset = "UTF-8";
		String username = "admin";
		String pass = "admin";
		String urlParams = String.format("username=%s&password=%s",URLEncoder.encode(username,charset),URLEncoder.encode(pass,charset));
		URL url = new URL("http://kuchnia.mooo.com:5000/login");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		OutputStream writer = con.getOutputStream();
		writer.write(urlParams.getBytes(charset));
		writer.flush();
		String headerName = null;
		for(int i=1;(headerName = con.getHeaderFieldKey(i))!=null;i++){
			if(headerName.equals("Content-Length")){
				int contentLen = Integer.parseInt(con.getHeaderField(i));
				if(contentLen != 34){
					throw new RuntimeException("Invlid Login");
				}else{
					break;
				}
			}
		}
		headerName = null;
		String tmp_cookie = null;
		for(int i=1;(headerName = con.getHeaderFieldKey(i))!=null;i++){
			if(headerName.equals("Set-Cookie")){
				tmp_cookie = con.getHeaderField(i);
			}
		}
		tmp_cookie = tmp_cookie.substring(0,tmp_cookie.indexOf(";"));
		this.cookie = tmp_cookie.substring(0);
		writer.close();
		con.disconnect();
	}
	private String getJson(String addr) throws IOException{
		URL url = new URL(addr);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Cookie", this.cookie);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line;
		StringBuffer output = new StringBuffer();
		while((line = in.readLine())!= null){
			output.append(line);
		}
		con.disconnect();
		return output.toString();
	}
	//http://kuchnia.mooo.com:5000/api/sensor?q={"filters":[ {"name":"user_id","op":"eq","val":8}]}
	public void load() throws JsonSyntaxException, IOException{
		String addr;
		for(Table t : Table.values()){
			addr = "";
			switch (t){
			case user:
				addr = this.srvAddr + "user/" + this.patient_id;
				UserResponse tmp = new Gson().fromJson(this.getJson(addr),UserResponse.class);
				tmp.setProperAge();
				this.responses.add(tmp);
				break;
			case sensor:
				String resultsPerPage = "&results_per_page=10000";
				String filter = String.format("?q={\"filters\":[{\"name\":\"user_id\",\"op\":\"eq\",\"val\":%d}]}", this.patient_id);
				addr = this.srvAddr + "sensor" + filter+resultsPerPage;
				this.responses.add(new Gson().fromJson(this.getJson(addr),SensorResponse.class));
				break;
			case measure:
				int numSensors = this.responses.get(1).getNumResults();
				SensorResponse tmpSensorResponse = (SensorResponse) this.responses.get(1);
				List<Sensor> sensors = tmpSensorResponse.getSensors();
				for(int i = 0;i<numSensors;i++){
					addr = this.srvAddr + "measure/" + sensors.get(i).getId();
					this.measuresResponses.add(new Gson().fromJson(this.getJson(addr),MeasuresResponse.class));
				}
				break;
			case sensortype:
				addr = this.srvAddr + "sensortype?results_per_page=10000";
				this.responses.add(new Gson().fromJson(this.getJson(addr), SensorTypeResponse.class));
				break;
			default:
				throw new RuntimeException("Wooops, something gone wrong during loading data");
			}
		}
	}
//	AnnDataContainer(Integer vAge,Boolean vSex,Integer vCp,Float vTrestbps,Float vChol,Float vFbs,Float vRestecg,
//			Float vExang,Float vOldpeak,Float vSlope,Float vCa,Float vThal)
	public AnnDataContainer getData(){
		/*
		 * cp - Ból klatki piersiowej
		 * trestbps - Ciśnienie krwi - spoczynek
		 * chol - Cholesterol
		 * fbs - Wysoki poziom cukru
		 * restecg - Elektrokardiograf
		 * thalach - Max. puls
		 * exang - Przebyta angina
		 * slope - Obnizenie ST
		 * ca - Naczynek na flurorosopii
		 * thal - Thal
		 */
		//user response
		UserResponse uResp = (UserResponse) this.responses.get(0);
		int age = uResp.age;
		Boolean sex = uResp.sex;
		String []wantedNames = {"Ból klatki piersiowej","Ciśnienie krwi - spoczynek","Cholesterol","Wysoki poziom cukru","Elektrokardiograf","Max. puls","Przebyta angina","Obnizenie ST","Naczynek na flurorosopii","Thal"};
		//Sensor and values
		float cp = 0;			//0
		float trestbps = 0; 	//1
		float chol = 0;			//2
		float fbs = 0;			//3
		float restecg = 0;		//4
		float thalach = 0;		//5
		float oldpeak = 0;		//6
		float slope = 0;		//7
		float ca = 0;			//8
		float thal = 0;			//9
		Map<String,Float> vals = new HashMap<String,Float>();
		SensorTypeResponse typeResponse = (SensorTypeResponse) this.responses.get(this.responses.size()-1);
		List<SensorType> types = typeResponse.getObjects();
		for(String name : wantedNames){
			for(SensorType typeSensor : types){
				if(typeSensor.getName().equals(name)){
						int id = typeSensor.id;
						SensorResponse tmpSensorResponse = (SensorResponse) this.responses.get(1);
						List<Sensor> sensors = tmpSensorResponse.getSensors();
						for(Sensor sens : sensors){
							if(sens.sensor_type_id == id){
								int idOfSensor = sens.id;
								for(MeasuresResponse measure : this.measuresResponses){
									if(idOfSensor == measure.sensor_id){
										vals.put(name, measure.value);
									}
								}
							}
						}
				}
			}
		}
		for(String name : wantedNames){
			if(name == "Ból klatki piersiowej"){
				cp = vals.get(name);
			}else if(name == "Ciśnienie krwi - spoczynek"){
				trestbps = vals.get(name);
			}else if(name == "Cholesterol"){
				chol = vals.get(name);
			}else if(name == "Wysoki poziom cukru"){
				fbs = vals.get(name);
			}else if(name == "Elektrokardiograf"){
				restecg = vals.get(name);
			}else if(name == "Max. puls"){
				thalach = vals.get(name);
			}else if(name == "Przebyta angina"){
				oldpeak = vals.get(name);
			}else if(name == "Obnizenie ST"){
				slope = vals.get(name);
			}else if(name == "Naczynek na flurorosopii"){
				ca = vals.get(name);
			}else if(name == "Thal"){
				thal = vals.get(name);
			}
		}
		return new AnnDataContainer(age,sex,cp,trestbps,chol,fbs,restecg,thalach,oldpeak,slope,ca,thal);
	}
}


abstract class Response{
	 Integer id;
	 Integer num_results;
	 public abstract Integer getForeginKey();
	 public abstract Integer getPrimaryKey();
	 public Integer getNumResults(){
	    	return this.num_results;
	    } 
}

class SensorResponse extends Response{
	List<Sensor> objects;
   
    SensorResponse(){
    	this.id= null;
    }
    public Integer getForeginKey(){
    	throw new RuntimeException("Do not invoke it form this class!!");
    }
    List<Sensor> getSensors(){
    	return this.objects;
    }
	@Override
	public Integer getPrimaryKey() {
		throw new RuntimeException("This response does ont have own id. Use List<Sensor> for Primary keys");
	}
}

class Sensor {
	Integer id;
	String name;
	Integer sensor_type_id;
	Float alert_min;
	Float alert_max;
	Float warning_min;
    Float warning_max;
    Integer user_id;
    
    public int getId(){
    	return this.id;
    }
}


class MeasuresResponse extends Response{
	Integer sensor_id;
	Float value;
//	String timestamp;
	
	@Override
    public Integer getForeginKey(){
		return this.sensor_id;
	}
	
	@Override
	public Integer getPrimaryKey() {
		return this.id;
	}
}

class UserResponse extends Response{
	String login;
	String password;
	String last_name;
	String first_name;
	Boolean sex;
	String birth_date;
	Integer type;
	int age;
	public void setProperAge(){
		String []year = this.birth_date.split("-");
		this.age = 2013 - Integer.parseInt(year[0]); // /*TODO*/need to fix to get current system YEAR
	}
	@Override
	public Integer getForeginKey() {
		return null;
	}
	@Override
	public Integer getPrimaryKey() {
		return null;
	}
	
}

class SensorTypeResponse extends Response{
	List<SensorType> objects;
	SensorTypeResponse(){
		this.id = null;
	}
	List<SensorType> getObjects(){
		return this.objects;
	}
	@Override
	public Integer getForeginKey() {
		return null;
	}

	@Override
	public Integer getPrimaryKey() {
		return null;
	}
	
}
class SensorType{
	Boolean automatic;
	Integer id;
	String name;
	String unit;
	
	String getName(){
		return this.name;
	}
}
