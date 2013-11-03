import java.io.IOException;

import com.google.gson.JsonSyntaxException;


public class ArtificialNeuralNetworkModule {

	public static void main(String[] args) throws JsonSyntaxException, IOException {
		// TODO Auto-generated method stub
//		ANN ann = new ANN();
//		ann.run();
		DataLoader d = new DataLoader(5);
		d.load();
		
	}

}