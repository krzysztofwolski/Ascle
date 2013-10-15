import com.google.gson.*;
import com.google.gson.reflect.*;
import java.util.Map;

//for test only
import java.io.FileReader;
import java.io.BufferedReader;
//End tets imports

//Encog
import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
//end Encog


import java.io.IOException;


public class ANN {
	private BasicNetwork network;
	public ANN(){
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true,100/*TODO: Set proper value of neurons*/));						//INPUT LAYER
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,100/*TODO: Set proper value of neurons*/));	//HIDDEN LAYER
		network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1/*TODO: Set proper value of neurons*/));	//OUTPUT LAYER
		network.getStructure().finalizeStructure();
		network.reset();
	};
	
	public void run(boolean maintenance_mode){
		loadData();
		if(maintenance_mode){
			/*
			 * Trainig in this mode
			 */
		}
		else{
			
		}
	};
	
	private void loadData(){
		Gson gson = new Gson();
		/*TEST PURPOSE AGIN!*/String path = "/home/tomasz/Documents/Projekty_uczelnia/HeartDiseaseDetection/jsonEx2.json";
		try{
			BufferedReader read = new BufferedReader(new FileReader(path));
			Map<String,String> out = gson.fromJson(read, new TypeToken<Map<String, String>>() {}.getType());
			System.out.println(out);
		}catch(IOException e){
			e.printStackTrace();
		}
	};
	
	private void prepareDataSet(){
		
	};
	
	private void trainNetwork(){
		
	};
	
}
