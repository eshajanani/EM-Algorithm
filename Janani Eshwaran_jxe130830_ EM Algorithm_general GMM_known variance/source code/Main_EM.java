import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Main_EM {

	// main method
	public static ArrayList<Double> EM_Data = new ArrayList<Double>();
	public static void main(String[] args) throws IOException
	{
		
		readData(args[0]);
		
		System.out.println("Read Data");
		EM_algo em= new EM_algo(EM_Data,Integer.parseInt(args[1]));
		em.start_EM();
	}
	
	public static void readData(String fileName) throws IOException
	{
		String line =null;
		FileReader file = new FileReader(fileName);
		BufferedReader br = new BufferedReader(file);
		while((line = br.readLine())!= null)
		{
			EM_Data.add(Double.parseDouble(line));
			
		}
	}
	
}
