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
	
	private String INFILENAME; // file of input
	private String OUTFILENAME; // file to output
	private int ss; // sequence size
	private int ps; // population size
	private int generations; // number of genetarions
	private double mr; // mutation rate
	private double rr; // recombination rate
	private int rfl; // recombination fragment length
	private List<String> sequences;
	
	public static void main(String[] args) {
		
		Simulator sim = new Simulator();
		
		sim.input();
		//sim.mutation();
		sim.recombine();
		sim.output();
	}
	
	// function to process input from FASTA file
	private void input(){		
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;
		
		System.out.println("Welcome to Simulator!");
		Scanner in = new Scanner(System.in);
		
		System.out.println("Introduce FASTA file ->");
		this.INFILENAME = in.nextLine();
		
		System.out.println("Introduce number of sequence size ->");
		this.ss = in.nextInt();
		
		System.out.println("Introduce number of population size ->");
		this.ps = in.nextInt();
		
		try {
			FileReader reader = new FileReader(INFILENAME);
			BufferedReader bufferedreader = new BufferedReader(reader);
			
			// in each line at FASTA file we append new line
			while((line = bufferedreader.readLine())!=null){
				stringBuffer.append(line).append("\n");
				}
			
			// to remove initial header
			String splited[] = (stringBuffer.toString()).split("\\n",2);
			
			// to remove \n from original genome in FASTA file
			splited[1] = splited[1].replaceAll("[\r\n]", "");
			
			// to construct each sequences from genome at FASTA file
			this.sequences = new ArrayList<String>();
			int index = 0;
			while (index < splited[1].length()) {
			    this.sequences.add(splited[1].substring(index, Math.min(index + this.ss,splited[1].length())));
			    index += this.ss;
			}
			/*
			for(int i = 0; i<this.ps; i++)
			System.out.println(">sequence_"+(i+1)+"\n"+this.sequences.get(i)+"\n");
			*/
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void output(){
		try {
			
			// create a FASTA file with output on desired folder
			Scanner out = new Scanner(System.in);
			System.out.println("Where you want to save the FASTA file? ->");
			this.OUTFILENAME = out.nextLine();
			
			FileWriter writer = new FileWriter(OUTFILENAME + "/output.fasta");
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			bufferedWriter.write("Dummy header fix it later ... or not...\n");
			
			// to write each sequence in a different line on FASTA file
			for(int i = 0; i<this.ps; i++){
				bufferedWriter.write(this.sequences.get(i));
				bufferedWriter.write("\n");
			}
				
			bufferedWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void recombine() {
		double rng = Math.random();
		System.out.println(rng);
	}
	
}
