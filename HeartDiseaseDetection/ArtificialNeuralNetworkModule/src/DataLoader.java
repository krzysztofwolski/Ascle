import java.util.List;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.*;
public class DataLoader {
	private Gson jsonstream;
	private int patient_id;
	private final String srvAddr = "";
	DataLoader(int id)
	{
		this.jsonstream = new Gson();
		this.patient_id = id;
		//TODO:GET HERE
		
	}
}

class SensorResponse{
	private Integer totalNum;
	private List<SensorData> data;
}

class SensorData{
	private String id_pk;
	private String id_SensorType;
	private String id_user;
	private Integer alert_min;
	private Integer alert_max;
	private Integer warn_min;
	private Integer warn_max;
}

class SensorTypesResponse{
	private Integer totalNum;
	private List<SensorTypeData> data;
}

class SensorTypeData{
	private String id_pk;
	private String name;
	private String unit;
	private String automatic;
}

class MeasuresResponse{
	private Integer totalNum;
	private List<MeasuresData> data;
}

class MeasuresData{
	private String id_pk;
	private String id_Sesnor;
	private Float value;
	private String timeStamp;
}