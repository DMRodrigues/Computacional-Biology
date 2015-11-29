package exercise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
	private int seqSize; // number of sequences

	public static void main(String[] args) {

		Simulator sim = new Simulator();

		sim.input();
		sim.mutation();
		sim.recombine();
		sim.output();
	}

	// function to process input from FASTA file
	private void input() {
		String[] parsed = null;
		String[] splited = null;
		sequences = new ArrayList<String>();

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
		in.close(); // ?????????????????????????

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
			while ((line = bufferedreader.readLine()) != null) {
				stringBuffer.append(line).append("\n");
				parsed = (stringBuffer.toString()).split(">");
			}

			//to remove header from FASTA file 
			for (int i = 1; i < parsed.length; i++) {
				splited = parsed[i].split("\\n", 2);
				String sequence = splited[1].replaceAll("[\r\n]", "");
				this.sequences.add(sequence);
				//System.out.println("lista: "+ this.sequences.get(i-1));
			}

			// seguro guardar o tamanho
			this.seqSize = this.sequences.size();

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Mutation
	private void mutation() {

		String seq, seqMutated;
		char oldNucleotide, newNucleotide;
		double number;
		int pos;

		//1st step
		for (int i = 0; i < this.seqSize; i++) {

			number = Math.random();

			//2nd step
			if (number <= this.mr) { //condition to mutate
				
				seq = this.sequences.get(i);
				pos = (int) (Math.round(Math.random() * seq.length())); //choose a random position in the sequence. improve the distribution with round()

				oldNucleotide = seq.charAt(pos); //get the actual nucleotide
				newNucleotide = randomNucleotide(oldNucleotide); //choose a random nucleotide           

				seqMutated = seq.substring(0, pos) + newNucleotide + seq.substring(pos + 1); //change the actual nucleotide, in the random position, to a different one 

				//3rd step
				this.sequences.set(i, seqMutated); //update the sequence

				System.out.println("Mutation in Sequence_" + (i + 1) + ", position: " + pos + ". Old nucleotide: " + oldNucleotide + ", New nucleotide: " + newNucleotide);
			}
		}
	}

	//Random nucleotide generator
	private char randomNucleotide(char nucleotide) {

		int rand;
		char newNucleotide = nucleotide;

		rand = (int) (Math.random() * 4);

		while (newNucleotide == nucleotide) { //guarantees that the new nucleotide is different from the original
			switch (rand) {
			case 0:
				newNucleotide = 'A';
				break;
			case 1:
				newNucleotide = 'C';
				break;
			case 2:
				newNucleotide = 'T';
				break;
			case 3:
				newNucleotide = 'G';
				break;
			}
			rand = (int) (Math.random() * 4);
		}
		return newNucleotide;
	}

	private void recombine() {
		
		int mySize, max, random, randMax, copSeq;
		double rng;
		
		// para cada sequencia na lista
		for (int i = 0; i < this.seqSize; i++) {

			// fazer ou n recombinacao
			rng = Math.random();

			if (rng <= this.rr) {

				max = (mySize = this.sequences.get(i).length()) - this.rfl;
				random = (int) (Math.round(Math.random() * max)); // melhorar a distribuicao atraves de round()
				randMax = random + this.rfl; // obter a localizacao de onde substituir [random - randomMax]

				//determinar outra sequencia a ir buscar
				copSeq = (int) (Math.random() * this.seqSize);

				this.sequences.set(i,
						this.sequences.get(i).substring(0, random)
								+ this.sequences.get(copSeq).substring(random, randMax)
								+ this.sequences.get(i).substring(randMax, mySize));
			}
		}
	}

	// Compute the Hamming distance between all the sequences (same size)
	// The distance means the smallest number of substitutions to transform seq1 in seq2

	public double[][] hammingDistance(){

		String seq1, seq2;
		int size;
		double dist = 0;
		double[][] hammingDistances = new double[seqSize][seqSize];

		for(int i = 0; i < this.seqSize; i++){
			for(int j = 0; j < this.seqSize; j++){  
				
				seq1 = this.sequences.get(i); 
				seq2 = this.sequences.get(j);
				
				if (seq1.length() != seq2.length()) {
    				System.out.println("Error: Input sequences should have the same length");
    	    		System.exit(1);
	   			 }

	   			 size = seq1.length(); //both sequences have same length

				for (int c = 0; c < size; c++){ 
					if(seq1.charAt(c) != seq2.charAt(c)){
						dist++; //counting when the sequences differ from each other
					}
				}
				hammingDistances[i][j] = dist/size; // % of mismatching sites
			}
		}
		return hammingDistances;
	}

	private void output() {
		
		try {

			int endIndex = this.INFILENAME.lastIndexOf("\\");
			if (endIndex != -1) {
				this.OUTFILENAME = this.INFILENAME.substring(0, endIndex);
			}

			// to save output file in the same folder than input
			FileWriter writer = new FileWriter(this.OUTFILENAME + "/output.fasta");
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			// to write each sequence in FASTA file
			for (int i = 0; i < this.sequences.size(); i++) {
				bufferedWriter.write(">Sequence" + "_" + (i + 1) + "\n");
				bufferedWriter.write(this.sequences.get(i));
				bufferedWriter.write("\n");
			}

			bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
