import java.util.ArrayList;
import java.util.List;

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
		user,sensor,sensortype,measure
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
//		this.cookieName = tmp_cookie.substring(0, tmp_cookie.indexOf("="));
//		this.cookie = tmp_cookie.substring(tmp_cookie.indexOf("=")+1,tmp_cookie.length());
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
	
	@SuppressWarnings("incomplete-switch")
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
				addr = this.srvAddr + "sensor/" + this.patient_id;
				this.responses.add(new Gson().fromJson(this.getJson(addr),SensorResponse.class));
				break;
			case sensortype:
				int sensorTypeID = this.responses.get(1).getPrimaryKey();	/*TODO: this is lame solution, but it is sufficient*/
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
	
	public List<Response> getData(){
		 return this.responses;
	}
}


abstract class Response{
	 Integer id;
	 public abstract Integer getForeginKey();
	 public abstract Integer getPrimaryKey();
}

class SensorResponse extends Response{
	
	String name;
	Integer sensor_type_id;
	Float alert_min;
	Float alert_max;
	Float warning_min;
    Float warning_max;
    Integer user_id;
    
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getPrimaryKey() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

