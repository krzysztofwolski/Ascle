import java.io.IOException;

import com.google.gson.JsonSyntaxException;


public class ArtificialNeuralNetworkModule {

	public static void main(String[] args) throws JsonSyntaxException, IOException {
		// TODO Auto-generated method stub
		//test_ANNXor testANN = new test_ANNXor();
		//testANN.runANN();
		DataLoader loader = new DataLoader(1);
		loader.load();
	}

}