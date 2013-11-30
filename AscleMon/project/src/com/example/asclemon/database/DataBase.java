package com.example.asclemon.database;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.RunnableScheduledFuture;

import org.json.*;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import com.example.asclemon.ManualAcquisition;
import com.loopj.android.http.*;

import android.R;
import android.database.sqlite.*;
import android.widget.EditText;
import android.content.*;


public enum DataBase implements Runnable {
	INSTANCE;

	public String message = "mój ma³y singleton";
	private String host_adres =  "http://kuchnia.mooo.com:5000/";
	private String api_adres = host_adres + "api/";
	private int timeout = 1000; // ms
	private int measureUpdateTimeout = 10000;
	
	SimpleDateFormat inputTimeFormat = new SimpleDateFormat ("yyyy-MM-ddHH:mm:ss.SSS"); 
	SimpleDateFormat outputTimeFormat = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSS'000'"); 
	
	
	User user = new User();
	ArrayList<Sensor> sensors = new ArrayList<Sensor>();
	ArrayList<Message> received = new ArrayList<Message>();
	ArrayList<Message> sent = new ArrayList<Message>();
	ArrayList<Message> toSend = new ArrayList<Message>();
	
	AsyncHttpClient client = new AsyncHttpClient();	
	PersistentCookieStore myCookieStore;
	Context context;
	Thread currentTread;
	
	enum State{
		UpdateAll,
		UpdateMeasure,
		Closing,
	}
	
	State currentState;
	Boolean running = true;
	
	public ArrayList<Integer> getSensorTypeIDs(){
		ArrayList<Integer> sensorTypesIDs = new ArrayList<Integer>(5);
		
		sensorTypesIDs.add(12); // automatyczne 
		sensorTypesIDs.add(14);
		sensorTypesIDs.add(20);
		sensorTypesIDs.add(16);
		sensorTypesIDs.add(18);
		
		return sensorTypesIDs;
	}
	
	public Sensor getSensor(int sensorTypeID){
		Sensor retVal = null;
		
		for(Sensor sens : sensors){
			if(sens.sensor_type_id == sensorTypeID) return sens;
		}
		
		return retVal;
	}
	
	public Sensor getSensor(String name){
		Sensor retVal = null;
		
		for(Sensor sens : sensors){
			if(sens.name.compareTo(name) == 0) return sens;
		}
		
		return retVal;
	}
	
	public void addSensor(String name, int sensor_type_id, String unit, Boolean automatic){
		Sensor sensor = new Sensor();
		
		sensor.name = name;
		sensor.sensor_type_id = sensor_type_id;
		sensor.unit = unit;
		sensor.automatic = automatic;
		sensor.lastUpdateTime = new Date();
		//sensor.measures = new ArrayList<Measure>();
		sensors.add(sensor);
	}
	
	
	private DataBase(){
		user.id = 1;
		currentState = State.UpdateAll;
		
		ArrayList<Integer> sensorTypesIDs = getSensorTypeIDs();		
		addSensor("termometr 2000", sensorTypesIDs.get(0), "C", true);
		addSensor("Miernik 2000", sensorTypesIDs.get(1), "mm/Hg", true);
		addSensor("Miernik 2000 (rozkurczowe)", sensorTypesIDs.get(2), "mm/Hg", true);
		addSensor("Pulsometr 2000", sensorTypesIDs.get(3), "1/s", true);
		addSensor("Waga 2000", sensorTypesIDs.get(4), "kG", true);
	}
	
	public void updateData() throws InterruptedException{
		login(user.login,user.password);
		updateStats("");
		updateUser(Integer.toString(user.id));
		//updateSensors("");
		
		System.out.println("updating file complete");
	}
	
	private void updateUser(String filters) {
		String name = "user";
		String request = api_adres + name;
		if(filters.length() != 0){
			request += "/" + filters;
		}
		 
		client.get(request,  new AsyncHttpResponseHandler() {
			@Override
		    public void onSuccess(String response) {
				//System.out.println(response);
		    	JSONObject json = new JSONObject();
		    	try {
					json = new JSONObject(response);
					
					int id = json.optInt("id");
					
					if(id == user.id){
						user.first_name = json.getString("first_name");
						user.last_name = json.getString("last_name");
						user.sex = json.optBoolean("sex");
						//SimpleDateFormat inputDa = new SimpleDateFormat ("yyyy-MM-ddHH:mm:ss.SSS"); 
						
						String buf = json.getString("birth_date");
						String date = buf.substring(0, 10) + buf.substring(11,23);
						//System.out.println(date);
						
						
						
						user.birth_date = inputTimeFormat.parse(date);
						//System.out.println(ft.format(user.birth_date));
						System.out.println(outputTimeFormat.format(user.birth_date ));
						
					}else{
						//  wywal error (pobrano zle dane)
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	currentTread.notify();
		    }
			
		});
		

	}


	private void updateSensorsTypes(String filters, ArrayList<Integer> sensorTypeIds) {
	String name = "sensortype";
		
	if(sensorTypeIds.size() > 0){
		filters = "?q=%7B%22filters%22:[";
		for(int i = 0 ; i < sensorTypeIds.size() ; i++){
			filters += "%7B%22name%22:%22id%22,%22op%22:%22eq%22,%22val%22:" + sensorTypeIds.get(i) + "%7D,";		
		}		
		filters = filters.substring(0,filters.length()-1);
		filters += "],%22disjunction%22:true%7D";
	}
	
	//System.out.println(filters);
	//filters = "?q=%7B%22filters%22:[";
	//filters += "%7B%22name%22:%22id%22,%22op%22:%22eq%22,%22val%22:1%7D]%7D";		
	
		String request = api_adres + name + filters;
		
		client.get(request,  new AsyncHttpResponseHandler() {
			@Override
		    public void onSuccess(String response) {
				synchronized(client){
					//System.out.println(response);
			    	JSONObject json = new JSONObject();
			    	try {
						json = new JSONObject(response);
						JSONArray users = json.getJSONArray("objects");
						System.out.println("objects Amount: " + users.length());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	client.notify();
				}
		    }
			
		});
		
	}

	private  void login(String login, String password) {
		myCookieStore.clear();
		String name = "login";
		String request = host_adres + name;
		
		user.login = login;
		user.password = password;
		
		RequestParams params = new RequestParams();
		params.put("username", login);
		params.put("password", password);
		
		client.post(request, params , new AsyncHttpResponseHandler() {
			@Override
		    public  void onSuccess(String response) {
				synchronized(client){
				System.out.println(response);
		    	JSONObject json = new JSONObject();
		    	try {
					json = new JSONObject(response);
					
					String message = json.getString("message");
					if(message.contains("Success") == true){
					
						
					}
					else{
					//wywal b³ad o niezalogowaniu	
						//this.notify();
					}
					//System.out.println("CT:" + currentTread.getName());
					//synchronized (user){
						client.notify();
					//}
					//JSONArray users = json.getJSONArray("objects");
					//System.out.println(json.getJSONArray("objects").get(0));
					//System.out.println("users Amount: " + users.length());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
		    }
			
		});
	}

	private void updateStats(String filters){
		String name = "stats";
		
		String request = api_adres + name + filters;
		
		client.get(request,  new AsyncHttpResponseHandler() {
			@Override
		    public void onSuccess(String response) {
				synchronized(client){
					System.out.println(response);
			    	JSONObject json = new JSONObject();
			    	try {
						json = new JSONObject(response);
						
						boolean logged_in = json.getBoolean("logged_in");
						
						if(logged_in == true){
							user.login = json.getString("username");
							user.id = json.getInt("user_id");
							
						}else{
							// nie zalogowano wywal error
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    	client.notify();
				}
		    }
			
		});	
	}
	
	public void setUserLogin(String login, String password){
		user.login = login;
		user.password = password;
		currentState = State.UpdateAll;
	}
	


	public void setContext(Context baseContext) {
		context = baseContext;
		
		myCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(myCookieStore);
	}


	@Override
	public void run() {
		currentTread = Thread.currentThread();
		synchronized (client) {
			try {
				while(running){
					switch(currentState){
					case UpdateAll:
						login(user.login,user.password);
						client.wait(timeout);
						updateStats("");
						client.wait(timeout);
						updateUser(Integer.toString(user.id));
						client.wait(timeout);
						downloadReceivedMessage("");
						client.wait(timeout);
						downloadSendMessage("");
						client.wait(timeout);
						updateSensorsTypes("",getSensorTypeIDs());
						client.wait(timeout);
						addMySensors(getSensorTypeIDs());
						client.wait(timeout);
						updateSensors("",getSensorTypeIDs());
						client.wait(timeout);
						downloadMeasure("",getSensorTypeIDs());
						currentState = State.UpdateMeasure;
						client.wait(timeout);
						break;
					case UpdateMeasure:
						sendNewMeasures("",getSensorTypeIDs());
						client.wait(measureUpdateTimeout);
						downloadReceivedMessage("");
						client.wait(timeout);
						sendMessages("");
						client.wait(timeout);
						break;
					case Closing:
						running = false;
						user = new User();
						sensors.clear();
						
						client = new AsyncHttpClient();	
						myCookieStore.clear();
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void sendNewMeasure(String filters, Measure measure, final Sensor sensor){
		String name = "measure";
		String request = api_adres + name;
		
		String time = outputTimeFormat.format(measure.time);
		
		String req = "{\"sensor_id\": \"" + sensor.id + "\",\"value\": "+ measure.value +", \"timestamp\": \""+ time +"\"}";
		
		System.out.println(req);
		
		StringEntity entity = null;
		try {
			entity = new StringEntity(req);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entity.setContentType("application/json");
		//System.out.println(entity.);
		
		client.post(this.context , request, entity ,"application/json",new AsyncHttpResponseHandler() {
			@Override
		    public void onSuccess(String response) {				
				synchronized(client){
					System.out.println(response);
					
					sensor.lastUpdateTime = new Date();
	
			    	client.notify();
				}
		    }
			public void onFinish(){
				System.out.println("finish");
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content)
		    {
				System.out.println(content);
		    }
			
		});	
	}
	
	private void sendNewMeasures(String filters,final Sensor sensor){
		ArrayList<Measure> toSend = new ArrayList<Measure>();
		
		for(Measure mes: sensor.getMeasures()){
			if(mes.time.after(sensor.lastUpdateTime) == true){
				toSend.add(mes);
			}	
		}
		
		for(Measure mes: toSend){
			sendNewMeasure("",mes,sensor);
		}
		
	}
	
	private void sendNewMeasures(String filters, final ArrayList<Integer> sensorTypeIDs) {
		for(int sensorNR = 0 ; sensorNR < sensorTypeIDs.size() ; sensorNR++){
			sendNewMeasures(filters,getSensor(sensorTypeIDs.get(sensorNR)));
		}
			
	}
	
	private void  downloadMeasure(String filters, final Sensor sensor){
		String name = "sensor";

		if(filters.length() == 0){
			filters = "?q=%7B%22filters%22:[";
			filters += "%7B%22name%22:%22sensor_id%22,%22op%22:%22eq%22,%22val%22:" + sensor.id + "%7D";	
			filters += "],%22disjunction%22:true%7D";
		}
		
		//System.out.println(filters);
		//filters = "?q=%7B%22filters%22:[";
		//filters += "%7B%22name%22:%22id%22,%22op%22:%22eq%22,%22val%22:1%7D]%7D";		
		
			String request = api_adres + name + filters + "?results_per_page=100";
			
			System.out.println(request);
			
			client.get(request,  new AsyncHttpResponseHandler() {
				@Override
			    public void onSuccess(String response) {
					synchronized(client){
						System.out.println(response);
				    	JSONObject json = new JSONObject();
				    	try {
							json = new JSONObject(response);
							JSONArray objects = json.getJSONArray("objects");
							
							for(int objectNr = 0 ; objectNr <  objects.length() ; objectNr++ ){
								JSONObject object = objects.getJSONObject(objectNr);
								
								float value =(float) object.getDouble("value");
								
								
								String buf = json.getString("birth_date");
								String date = buf.substring(0, 10) + buf.substring(11,23);
								System.out.println(date);
								
								Date time = inputTimeFormat.parse(date);
								
								sensor.addNewMeasure(value, time);		
							}
							
							System.out.println("objects Amount: " + objects.length());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	client.notify();
					}
			    }
				
			});
	}
	
	private void downloadMeasure(String filters, final ArrayList<Integer> sensorTypeIDs) {
		for(int sensorNR = 0 ; sensorNR < sensorTypeIDs.size() ; sensorNR++){
			downloadMeasure(filters,getSensor(sensorTypeIDs.get(sensorNR)));
		}
			
	}
	
	private void  downloadReceivedMessage(String filters){
		String name = "message";

		if(filters.length() == 0){
			filters = "?q=%7B%22filters%22%3A%5B%7B%22name%22%3A%22receiver_user_id%22%2C%22op%22%3A%22eq%22%2C%22val%22%3A"+user.id+"%7D%5D%7D";
		}
		
		//System.out.println(filters);
		//filters = "?q=%7B%22filters%22:[";
		//filters += "%7B%22name%22:%22id%22,%22op%22:%22eq%22,%22val%22:1%7D]%7D";		
		
			String request = api_adres + name + filters;
			
			System.out.println(request);
			
			client.get(request,  new AsyncHttpResponseHandler() {
				@Override
			    public void onSuccess(String response) {
					synchronized(client){
						System.out.println(response);
				    	JSONObject json = new JSONObject();
				    	try {
							json = new JSONObject(response);
							JSONArray objects = json.getJSONArray("objects");
							
							for(int objectNr = 0 ; objectNr <  objects.length() ; objectNr++ ){
								JSONObject object = objects.getJSONObject(objectNr);
								
								int id = object.getInt("id");
								int senderId = object.getInt("sender_user_id");
								int receiverId = object.getInt("receiver_user_id");
								
								String text = object.getString("text");
								boolean isNew = object.getBoolean("new");
								
								Message message = new Message(id, senderId, receiverId, text, isNew);
								boolean exist = false;
								for(int i = 0; i < received.size(); i++){
									if(received.get(i).Id == id) exist = true;
								}
								if(exist == false)
									received.add(message);	
								
							}
							
							System.out.println("objects Amount: " + objects.length());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	client.notify();
					}
			    }
				
			});
	}
	private void  downloadSendMessage(String filters){
		String name = "message";

		if(filters.length() == 0){
			filters = "?q=%7B%22filters%22%3A%5B%7B%22name%22%3A%22sender_user_id%22%2C%22op%22%3A%22eq%22%2C%22val%22%3A"+user.id+"%7D%5D%7D";
		}
		
		
			String request = api_adres + name + filters;
			
			System.out.println(request);
			
			client.get(request,  new AsyncHttpResponseHandler() {
				@Override
			    public void onSuccess(String response) {
					synchronized(client){
						System.out.println(response);
				    	JSONObject json = new JSONObject();
				    	try {
							json = new JSONObject(response);
							JSONArray objects = json.getJSONArray("objects");
							
							for(int objectNr = 0 ; objectNr <  objects.length() ; objectNr++ ){
								JSONObject object = objects.getJSONObject(objectNr);
								
								int id = object.getInt("id");
								int senderId = object.getInt("sender_user_id");
								int receiverId = object.getInt("receiver_user_id");
								
								String text = object.getString("text");
								boolean isNew = object.getBoolean("new");
								
								Message message = new Message(id, senderId, receiverId, text, isNew);
								
								boolean exist = false;
								for(int i = 0; i < sent.size(); i++){
									if(sent.get(i).Id == id) exist = true;
								}
								if(exist == false)
									sent.add(message);									
							}
							
							System.out.println("objects Amount: " + objects.length());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	client.notify();
					}
			    }
				
			});
	}
	
	private void sendMessages(String filters){
		for(int i = 0 ; i < toSend.size(); i++){
			sendMessage("",toSend.get(i));
			sent.add(toSend.get(i));
		}
		
		
		toSend.clear();
	}
	
	private void sendMessage(String filters, Message message){
		
		String name = "message";
		String request = api_adres + name;
		
		
		String req = "{\"sender_user_id\": \"" + user.id + "\",\"receiver_user_id\": "+ message.ReceiverId +", \"text\": \""+ message.text  +"\"}";
		
		System.out.println(req);
		
		StringEntity entity = null;
		try {
			entity = new StringEntity(req);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		entity.setContentType("application/json");
		//System.out.println(entity.);
		
		client.post(this.context , request, entity ,"application/json",new AsyncHttpResponseHandler() {
			@Override
		    public void onSuccess(String response) {				
				synchronized(client){
					System.out.println(response);
	
			    	client.notify();
				}
		    }
			public void onFinish(){
				System.out.println("finish");
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content)
		    {
				System.out.println(content);
		    }
			
		});	
	}

	private void addMySensors(final ArrayList<Integer> sensorTypeIDs){
/*		 {
	            "name": "testy",
	            "sensor_type_id": 11,
	            "user_id": 31
	}*/
		
		String name = "sensor";
		String request = api_adres + name;
		for(int sensNR = 0 ; sensNR < sensorTypeIDs.size() ; sensNR++){
			Sensor sensor = getSensor(sensorTypeIDs.get(sensNR));
			String req = "{\"name\": \"" + sensor.name +user.id + "\",\"sensor_type_id\": "+ sensor.sensor_type_id +", \"user_id\": "+ user.id +"}";
			//RequestParams params = new RequestParams();
			//params.put("","{\"name\": \"testy\",\"sensor_type_id\": 10, \"user_id\": 32}");
			//client.addHeader("Content-Type", "application/json");
			
			
			//params.
			//System.out.println(req);
		//	System.out.println(params.toString());
			StringEntity entity = null;
			try {
				entity = new StringEntity(req);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			entity.setContentType("application/json");
			//System.out.println(entity.);
			
			client.post(this.context , request, entity ,"application/json",new AsyncHttpResponseHandler() {
				@Override
			    public void onSuccess(String response) {
					System.out.println(response);
					synchronized(client){
						System.out.println(response);
		
				    	client.notify();
					}
			    }
				public void onFinish(){
					System.out.println("finish");
					
				}
				@Override
				public void onFailure(java.lang.Throwable error, java.lang.String content)
			    {
					System.out.println(content);
			    }
				
			});
		}
	}
	
	public void sendMessage(int toId, String text){
		Message message = new Message(-1,user.id,toId,text,false);
		toSend.add(message);
	}
	
	
	private void updateSensors(String filters, final ArrayList<Integer> sensorTypeIDs) {
		String name = "sensor";

		if(filters.length() == 0){
			filters = "?q=%7B%22filters%22:[";
			filters += "%7B%22name%22:%22user_id%22,%22op%22:%22eq%22,%22val%22:" + user.id + "%7D";		
			filters += "],%22disjunction%22:true%7D";
		}
		
		//System.out.println(filters);
		//filters = "?q=%7B%22filters%22:[";
		//filters += "%7B%22name%22:%22id%22,%22op%22:%22eq%22,%22val%22:1%7D]%7D";		
		
			String request = api_adres + name + filters;
			
			System.out.println(request);
			
			client.get(request,  new AsyncHttpResponseHandler() {
				@Override
			    public void onSuccess(String response) {
					synchronized(client){
						System.out.println(response);
				    	JSONObject json = new JSONObject();
				    	try {
							json = new JSONObject(response);
							JSONArray objects = json.getJSONArray("objects");
							
							for(int objectNr = 0 ; objectNr <  objects.length() ; objectNr++ ){
								JSONObject object = objects.getJSONObject(objectNr);
								
								Integer sensorTypeId = object.getInt("sensor_type_id");
								
								if(sensorTypeIDs.contains(sensorTypeId) != true) continue;
								else{
									Sensor sensor = getSensor(sensorTypeId);
									sensor.id = object.getInt("id");
									sensor.sensor_type_id = sensorTypeId;
									sensor.alert_max = (float) object.optDouble("alert_max");
									sensor.alert_min = (float) object.optDouble("alert_min");
									
									//////sensor.name = object.optString("name");
									
									sensor.warning_max = (float) object.optDouble("warning_max");
									sensor.warning_min = (float) object.optDouble("warning_min");
									sensor.lastUpdateTime = new Date();
								}	
							}
							
							System.out.println("objects Amount: " + objects.length());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    	client.notify();
					}
			    }
				
			});
			
	}

	public void logout() {
		currentState = State.Closing;

	}
}
