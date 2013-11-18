import java.util.ArrayList;
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

public class DataLoader {
	private String cookie;
	//private String cookieName;
	//private String cookieValue;
	private int patient_id;
	private final String srvAddr = "http://kuchnia.mooo.com:5000/api/";
	private List<Response> responses;
	private enum Table{
		user,sensor,measure,sensortype
	}
	
	DataLoader(int id) throws IOException{
		this.logIn();
		this.patient_id = id;
		responses = new ArrayList<Response>();
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
					this.responses.add(new Gson().fromJson(this.getJson(addr),MeasuresResponse.class));
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
		//user response
		UserResponse uResp = (UserResponse) this.responses.get(0);
		int age = uResp.age;
		Boolean sex = uResp.sex;
		
		//Sensor and values
		int cp = 0;
		float trestbps = 0;
		float chol = 0;
		float fbs = 0;
		float restecg = 0;
		float thalach = 0;
		float oldpeak = 0;
		float slope = 0;
		float ca = 0;
		float thal = 0;
		
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
    	throw new RuntimeException("Do not invoke it for this class!!");
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
