import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.JsonSyntaxException;
//Java version od srv
//java version "1.7.0_40"
//Java(TM) SE Runtime Environment (build 1.7.0_40-b43)
//Java HotSpot(TM) 64-Bit Server VM (build 24.0-b56, mixed mode)
public class ArtificialNeuralNetworkModule {
	
	public static void main(String[] args) throws JsonSyntaxException, IOException, ClassNotFoundException {

		int patientID = -1;
		if(args.length<=0){
			throw new RuntimeException("Please specify command-Line arguments");
		}
		
		if(args[0] == "train"){
			ANN network = new ANN();
			network.runTraining();
			
		}else if(args[0].equals("run")){
			//Run trained ANN
			try{
				patientID = Integer.parseInt(args[1]);
			}catch(NumberFormatException e){
				System.out.println("1st argument must be an integer!");
				System.exit(1);
			}
			DataLoader loader = new DataLoader(patientID);
			loader.load();
			AnnDataContainer actualData = loader.getData();
			ANN network = new ANN(actualData,"/home/tomasz/Documents/Projekty_uczelnia/Network.ser");
			double testResult = network.runDiagnosis();
			System.out.print(testResult);
			//Put result to DB
			loader.putData(testResult);
			
		}
		else if(args[0].equals("test")){
			//connection test
			if(testCon()){
				//System.out.println("Connection test Passed");
				System.exit(0);
			}
			else{
				//System.out.println("Connection test Failed");
				System.exit(-1);				
			}
		}
		else{
			throw new RuntimeException("Unrecognized command-line argument");
		}
	}
	private static Boolean testCon() throws IOException{
		URL url = new URL("http://kuchnia.mooo.com:5000/api/stats");
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		
		if(con.getResponseCode() == 200){
			con.disconnect();
			return true;
		}
		con.disconnect();
		return false;
	}
		
}


