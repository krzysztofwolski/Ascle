import java.io.IOException;

import com.google.gson.JsonSyntaxException;
//Java version od srv
//java version "1.7.0_40"
//Java(TM) SE Runtime Environment (build 1.7.0_40-b43)
//Java HotSpot(TM) 64-Bit Server VM (build 24.0-b56, mixed mode)
public class ArtificialNeuralNetworkModule {

	private static ANN a;

	public static void main(String[] args) throws JsonSyntaxException, IOException {

		DataLoader d = new DataLoader(8/*ID ARG HERE*/);
		d.load();
		AnnDataContainer actualData = d.getData();
	}

}