package exercise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Simulator {
	
	private static String FILENAME = ""; // file of input
	private static int ss = -1; // sequence size
	static int ps = -1; // population size
	static int generations; // number of genetarions
	static double mr; // mutation rate
	static double rr; // recombination rate
	static int rfl; // recombination fragment length
	
	public static void main(String[] args) {
		input();
	}
	
	// function to process input from FASTA file
	private static void input(){		
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;
		
		System.out.println("Welcome to Simulator!");
		Scanner in = new Scanner(System.in);
		
		System.out.println("Introduce FASTA file ->");
		FILENAME = in.nextLine();
		
		System.out.println("Introduce number of sequence size ->");
		String text = in.nextLine();
		ss = Integer.parseInt(text);
		
		System.out.println("Introduce number of population size ->");
		text = in.nextLine();
		ps = Integer.parseInt(text);
		
		try {
			FileReader reader = new FileReader(FILENAME);
			BufferedReader bufferedreader = new BufferedReader(reader);
			
			// in each line at FASTA file we append new line
			while((line = bufferedreader.readLine())!=null){
				stringBuffer.append(line).append("\n");
				}
			
			// to remove initial header
			String test[] = (stringBuffer.toString()).split("\\n",2);
			//System.out.println(test[1]);
			
			// to remove \n from original genome in FASTA file
			test[1] = test[1].replaceAll("[\r\n]", "");
			
			// to construct each sequences from genome at FASTA file
			List<String> sequences = new ArrayList<String>();
			int index = 0;
			while (index < test[1].length()) {
			    sequences.add(test[1].substring(index, Math.min(index + ss,test[1].length())));
			    index += ss;
			}
			
			for(int i = 0; i<ps; i++)
			System.out.println(">sequence_"+(i+1)+"\n"+sequences.get(i)+"\n");
			
			//for(int i=0; i<ps; i++){

			//while((line=bufferedreader.readLine()) != null){
		/*
				for(int j=0; j<ss; j++){
					if((chars=bufferedreader.read()) != -1)
					str.append((char)chars);
				}
				//str.append("\n");
				String sequence = str.toString();
				System.out.println(sequence);
				output(sequence);*/
			//}
			

				
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void output(String sequence){
		try {
			FileWriter writer = new FileWriter("teste.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			bufferedWriter.write(sequence);
			bufferedWriter.newLine();
			
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
