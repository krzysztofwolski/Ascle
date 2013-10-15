import org.encog.Encog;
import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

public class test_ANNXor {
	
	public static double XOR_in[][] = {
		{0.0,0.0},
		{1.0,0.0},	
		{0.0,1.0},
		{1.0,1.0}
	};
	public static double XOR_out_ideal[][] = {
		{0.0},
		{1.0},
		{1.0},
		{0.0}
	};

	public void runANN(){
		BasicNetwork network = new BasicNetwork();
		
		network.addLayer(new BasicLayer(null,true,2));	//input layer - 2 neurons + bias
		network.addLayer(new BasicLayer(new ActivationSigmoid(),true,2)); //hidden layer - 2 neurons + bias
		network.addLayer(new BasicLayer(new ActivationSigmoid(),false,1)); // output
		network.getStructure().finalizeStructure();
		network.reset();
		
		//Data set preprocess
		MLDataSet tSet = new BasicMLDataSet(XOR_in,XOR_out_ideal);
		final ResilientPropagation train  = new ResilientPropagation(network,tSet);
		int epoch = 1;
		do{
			train.iteration();
			System.out.println("Epoch:#"+epoch+" Error is: "+train.getError());
			epoch++;
		}while(train.getError()>0.01);
		train.finishTraining();
		System.out.println("Results:");
		for(MLDataPair pair: tSet){
			final MLData out = network.compute(pair.getInput());
			System.out.println(pair.getInput().getData(0) + "," + pair.getInput().getData(1)
					+ ", actual=" + out.getData(0) + ",ideal=" + pair.getIdeal().getData(0));
			
		}
		Encog.getInstance().shutdown();
	};
}
