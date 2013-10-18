import java.util.Map;


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
		network.addLayer(new BasicLayer(null,true,100/*TODO: Set proper value of number of neurons*/));						//INPUT LAYER
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,100/*TODO: Set proper value of number of neurons*/));	//HIDDEN LAYER
		network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1/*TODO: Set proper value of number of neurons*/));	//OUTPUT LAYER
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
		
	};
	
	private void prepareDataSet(){
		
	};
	
	private void trainNetwork(){
		
	};
	
	private void saveSynapses(){
		
	};
	
}