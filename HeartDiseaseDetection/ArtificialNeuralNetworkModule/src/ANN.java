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


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ANN {
	private BasicNetwork network;
	private final int[] attrIndex = {3,4,9,10,12,16,19,32,38,40,41,44,51};
	private final int desiredOutputIndex = 58;
	public ANN(){
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true,13));	//INPUT LAYER
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,15));	//HIDDEN LAYER
		network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1));	//OUTPUT LAYER
		network.getStructure().finalizeStructure();
		network.reset();
	};
	
	public void run(){

	};
	
	private String loadDataFile(String fileName) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String temp;
		StringBuilder sb = new StringBuilder();
		while( (temp = reader.readLine()) != null){
			temp.replace(".","");
			sb.append(temp + " ");
		}
		reader.close();
		return sb.toString();
	};
	
	private float normalize(int val,int min,int max){
		return ( (val - min)/(max - min) ) * (1 - (-1)) + (-1);
	}
	
	private void prepareDataSet(){
		String fileName = "";/*TODO:FILE NAME HERE*/
		String data_tmp = "";
		try{
			data_tmp = this.loadDataFile(fileName);
		}catch (IOException e){
			e.printStackTrace();
		}
		String []data = data_tmp.split(" ");
		
	};
	
	private void trainNetwork(){
		
	};
	
	private void saveSynapses(){
		
	};
	
}