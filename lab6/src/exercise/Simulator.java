package exercise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFileChooser;

public class Simulator {
	
	private String INFILENAME; // file of input
	private String OUTFILENAME; // file to output
	private int ss; // sequence size
	private int ps; // population size
	private int generations; // number of genetarions
	private double mr; // mutation rate
	private double rr; // recombination rate
	private int rfl; // recombination fragment length
	private List<String> sequences; //list of sequences from input file
	
	public static void main(String[] args) {
		
		Simulator sim = new Simulator();
		
		sim.input();
		sim.mutation();
		//sim.recombination();
		sim.output();
	}
		
	// function to process input from FASTA file
	private void input(){
		String[] parsed = null;
		String[] splited = null;
		sequences = new ArrayList <String>();		
		
		// to create interactive window to input FASTA file
		File sf = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(fileChooser);
		if (result == JFileChooser.APPROVE_OPTION) {
		    sf = fileChooser.getSelectedFile();		    
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;
		
		//System.out.println("Welcome to Simulator!");
		Scanner in = new Scanner(System.in);
		
		//System.out.println("Introduce FASTA file ->");
		//this.INFILENAME = in.nextLine();
		this.INFILENAME = sf.getAbsolutePath();
		//System.out.println(INFILENAME);
		
		//System.out.print("Introduce number of sequence size ->");
		//this.ss = in.nextInt();
		
		//System.out.print("Introduce number of population size ->");
		//this.ps = in.nextInt();
		
		try {
			FileReader reader = new FileReader(INFILENAME);
			BufferedReader bufferedreader = new BufferedReader(reader);
			
			// in each line at FASTA file we append new line
			while((line = bufferedreader.readLine())!=null){
				stringBuffer.append(line).append("\n");
				parsed = (stringBuffer.toString()).split(">");
			}
			
			//to remove header from FASTA file 
			for(int i = 1; i<parsed.length; i++){
				splited = parsed[i].split("\\n",2);
				String sequence = splited[1].replaceAll("[\r\n]", "");
				this.sequences.add(sequence);
				//System.out.println("lista: "+ this.sequences.get(i-1));
			}
			
			reader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Mutation
		private void mutation(){
			
			String seq;
			String seqMutated;
			int pos;
			char oldNucleotide;
			char newNucleotide;

			Random rand = new Random();

			//1st step
			for(int i = 0; i < sequences.size(); i++) {
		
				double number = rand.nextDouble(); 

				//2nd step
				if(number <= mr){ //condition to mutate
					seq = sequences.get(i);
					pos = rand.nextInt(seq.length()-1); //choose a random position in the sequence
					
					oldNucleotide = seq.charAt(pos); //get the actual nucleotide
					newNucleotide = randomNucleotide(oldNucleotide); //choose a random nucleotide           
					
					seqMutated = seq.substring(0,pos) + newNucleotide + seq.substring(pos+1); //change the actual nucleotide, in the random position, to a different one 
					
					//3rd step
					sequences.set(i, seqMutated); //update the sequence
				} 
			}
		}

		//Random nucleotide generator
		public char randomNucleotide(char nucleotide){ 
			
			int rand;
			char newNucleotide = nucleotide;

			Random generator = new Random();
			rand = generator.nextInt(3);

			while(newNucleotide == nucleotide){ //guarantees that the new nucleotide is different from the original
				switch (rand) {
					case 0 : newNucleotide = 'A';
							 break;
					case 1 : newNucleotide = 'C';
							 break;
					case 2 : newNucleotide = 'T';
							 break;
					case 3 : newNucleotide = 'G';
							 break;
				}
				rand = generator.nextInt(3);
			}
			return newNucleotide;
		}

	
	private void output(){
		System.out.println(this.sequences);
		System.out.println(this.sequences.get(0));
		try {
			
			int endIndex = this.INFILENAME.lastIndexOf("\\");
		    if (endIndex != -1)  
		    {
		       this.OUTFILENAME = this.INFILENAME.substring(0, endIndex); // not forgot to put check if(endIndex != -1)
		    }
			
		    // to save output file in the same folder than input
			FileWriter writer = new FileWriter(this.OUTFILENAME + "/output5.fasta");
			BufferedWriter bufferedWriter = new BufferedWriter(writer);
			
			// to write each sequence in FASTA file
			for(int i = 0; i<this.sequences.size(); i++){
				bufferedWriter.write(">Sequence"+"_"+(i+1)+"\n");
				bufferedWriter.write(this.sequences.get(i));
				bufferedWriter.write("\n");
			}
				
			bufferedWriter.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}


