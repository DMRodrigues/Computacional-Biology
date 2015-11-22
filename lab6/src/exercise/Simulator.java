package exercise;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Simulator {
	
	static String FILENAME = "genome.txt";
	int generations; // number of genetarions
	double mr; // mutation rate
	double rr; // recombination rate
	int rfl; // recombination fragment length
	
	public static void main(String[] args) {
		input();
	}
	
	private static void input(){
		try {
			FileReader reader = new FileReader(FILENAME);
			BufferedReader bufferedreader = new BufferedReader(reader);
			
			String line;
			
			while ((line = bufferedreader.readLine()) != null){
				//System.out.println(line);
				output(line);
			}
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void output(String input){
		try {
			FileWriter writer = new FileWriter("teste.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			bufferedWriter.write(input);
			bufferedWriter.newLine();
			
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
