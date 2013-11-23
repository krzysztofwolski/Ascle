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
import org.encog.util.obj.SerializeObject;


//end Encog
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



public class ANN {
	private BasicNetwork network;

//Trainging purpose fields
//*************************************
	private final int[] attrIndex = {3,4,9,10,12,16,19,32,38,40,41,44,51};
	private final int desiredOutputIndex = 58;
	double [][]selectedAttr;
	double[][] desiredOutput;
//*************************************	
	
//Run prepared ANN
//*************************************
	private AnnDataContainer data;	
//*************************************
	public ANN(){
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true,13));	//INPUT LAYER
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,15));	//HIDDEN LAYER
		network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1));	//OUTPUT LAYER
		network.getStructure().finalizeStructure();
		network.reset();
		System.out.println("New network assembled");
	};
	public ANN(AnnDataContainer a,String fileName) throws IOException, ClassNotFoundException{
		File file = new File(fileName);
		this.network = (BasicNetwork) SerializeObject.load(file);
		this.data = a;
		System.out.println("Network ready to perform diagnosis");
	}
	public void runTraining() throws IOException{
		this.prepareDataSet();
		this.trainNetwork();
		this.saveNetwork();
	};
	
	public double runDiagnosis(double [][]attr){
		double[] result = {-1};
		this.network.compute(this.data.toArray(), result);
		this.denormalize(result[0], 0, 4);
		
		return result[0];
	}
	
	private String loadDataFile(String fileName) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String temp;
		StringBuilder sb = new StringBuilder();
		while( (temp = reader.readLine()) != null){
			temp.replace("\\.","");
			sb.append(temp + " ");
		}
		reader.close();
		return sb.toString();
	};
	
	private double normalize(double val,double min,double max){
		if( max == min ){
			return 0.5;
		}
		return ((val - min) / (max - min))* (1 - 0) + 0;
		
	}
	
	private double denormalize(double val,double min,double max){
		return ((min- max) * val - 1 * min + max * 0)/ (0 - 1);
	}
	
	private void prepareDataSet(){
		String fileName = "/home/tomasz/Documents/Projekty_uczelnia/HeartDiseaseDetection/dataSet/hungarian.data";
		String data_tmp = "";
		try{
			data_tmp = this.loadDataFile(fileName);
		}catch (IOException e){
			e.printStackTrace();
		}
		String []data = data_tmp.split(" ");
		final int numPatientData = 76;
		final int numPatients = data.length/numPatientData;
		this.selectedAttr = new double[numPatients][numPatientData];
		this.desiredOutput = new double[numPatients][1];
		for(int i=0;i<numPatients;i++){
			for(int j=0;j<numPatientData;j++){
				if(j+1==this.desiredOutputIndex){
					double value = this.normalize(Double.parseDouble(data[i*numPatientData+j]), 0, 4);
					this.desiredOutput[i][0] = value;
				}
				else if(this.contains(j+1)){
					this.selectedAttr[i][j] = Double.parseDouble(data[i*numPatientData+j]);
				}
			}
		}
		//min and max form selected attrs
		for(int i=0;i<this.attrIndex.length;i++){
			double max = Integer.MIN_VALUE;
			double min = Integer.MAX_VALUE;
			for(int j=0;j<numPatients;j++){
				if(this.selectedAttr[j][i]>max){
					max = this.selectedAttr[j][i];
				}
				if(this.selectedAttr[j][i]<min){
					min = this.selectedAttr[j][i];
				}
			}
			for(int j=0;j<numPatients;j++){
				this.selectedAttr[j][i] = this.normalize(this.selectedAttr[j][i], min, max);
			}
		}
	};
	
	private void trainNetwork(){
		MLDataSet tSet = new BasicMLDataSet(this.selectedAttr,this.desiredOutput);
		final ResilientPropagation trainer = new ResilientPropagation(this.network,tSet);
		int epoch = 1;
		do{
			trainer.iteration();
			System.out.println("Epoch: #"+epoch+" Error is: "+trainer.getError());
			epoch++;
		}while(trainer.getError()>0.033);
		trainer.finishTraining();
		for(MLDataPair pair: tSet){
			final MLData out = network.compute(pair.getInput());
			System.out.println("actual=" + this.denormalize(out.getData(0),0,4) + " ideal=" + this.denormalize(pair.getIdeal().getData(0),0,4));
			
		}
	};
	
	private void saveNetwork() throws IOException{
		final String filename = "/home/tomasz/Documents/Projekty_uczelnia/Network.ser";
		System.out.println("Network is ready to work! Saving to file: "+filename);
		SerializeObject.save(new File(filename), this.network);
	};
	
	private boolean contains(int val){
		for(int i=0;i<this.attrIndex.length;i++){
			if(val == this.attrIndex[i]){
				return true;
			}
		}
		return false;
	}

}