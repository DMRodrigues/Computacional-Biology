package exercise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

public class Simulator5 {

	private String INFILENAME; // file of input
	private String OUTFILENAME; // file to output

	private int ss; // sequence size
	private int ps; // population size
	private int gen; // number of genetarions
	private int actualGen; // actual generation
	private int originalSeq; // number of original sequences to randomize

	private double mr; // mutation rate
	private double rr; // recombination rate
	private int rfl; // recombination fragment length

	private List<String> sequences; //list of sequences from input file
	private List<String> original; //list of original sequences
	private int seqSize; // number of sequences
	private int maxSize; // to avoid different size of sequences

	private double[][] hammingDistances; // Hamming distance matrix
	private double[][] jukesCantor; // Jukes-Cantor matrix;
	
	public double getMR(){
		return this.mr;
	}
	
	public double getRR(){
		return this.rr;
	}
	
	public int getRFL(){
		return this.rfl;
	}

	public int getGEN(){
		return this.gen;
	}
	
	public int getPS(){
		return this.ps;
	}
	
	public int getSS(){
		return this.ss;
	}
	
	public int getOrgSeq(){
		return this.originalSeq;
	}
	
	public void setOrgSeq(int orgSeq){
		this.originalSeq = orgSeq;
	}
	
	public void setSS(int ss){
		this.ss = ss;
	}
	
	public void setPS(int ps){
		this.ps = ps;
	}
	
	public void setGEN(int gen){
		this.gen = gen;
	}
	
	public void setActualGen(int it){
		this.actualGen = it;
	}

	public void setRFL(int rfl){
		this.rfl = rfl;
	}
	
	public void setRR(double rr){
		this.rr = rr;
	}
	
	public void setMR(double mr){
		this.mr = mr;
	}

	// Function to process input from FASTA file
	public void input() {
		String[] parsed = null;
		String[] splited = null;
		sequences = new ArrayList<String>();
		original = new ArrayList<String>();

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

		this.INFILENAME = sf.getAbsolutePath();

		try {
			FileReader reader = new FileReader(INFILENAME);
			BufferedReader bufferedreader = new BufferedReader(reader);

			// in each line at FASTA file we append new line
			while((line = bufferedreader.readLine()) != null) {
				stringBuffer.append(line).append("\n");
				parsed = (stringBuffer.toString()).split(">");
			}

			//to remove header from FASTA file 
			for(int i = 1; i < parsed.length; i++) {
				splited = parsed[i].split("\\n", 2);
				String sequence = splited[1].replaceAll("[\r\n]", "");
				this.sequences.add(sequence);
				this.original.add(sequence);
			}
			check();
			
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Mutation
	public void mutation() {

		String seq, seqMutated;
		char oldNucleotide, newNucleotide;
		double number;
		int pos;

		// 1st step
		for (int i = 0; i < this.seqSize; i++) {

			number = Math.random();

			// 2nd step
			if (number <= this.mr) { //condition to mutate
				
				seq = this.sequences.get(i);
				pos = (int) (Math.round(Math.random() * (seq.length()-1))); //choose a random position in the sequence. improve the distribution with round()

				oldNucleotide = seq.charAt(pos); //get the actual nucleotide
				newNucleotide = randomNucleotide(oldNucleotide); //choose a random nucleotide           

				seqMutated = seq.substring(0, pos) + newNucleotide + seq.substring(pos + 1); //change the actual nucleotide, in the random position, to a different one 

				// 3rd step
				this.sequences.set(i, seqMutated); //update the sequence

				System.out.println("Mutation in Sequence_" + (i + 1) + ", position: " + pos + ". Old nucleotide: " + oldNucleotide + ", New nucleotide: " + newNucleotide);
			}
		}
	}

	//Auxiliar mutation function: random nucleotide generator
	private char randomNucleotide(char nucleotide) {

		int rand;
		char newNucleotide = nucleotide;

		rand = (int) (Math.random() * 3);

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
			rand = (int) (Math.random() * 3);
		}
		return newNucleotide;
	}

	// Recombine
	public void recombine() {
		
		int max, random, randMax, copSeq;
		double rng;

		// para cada sequencia na lista
		for (int i = 0; i < this.seqSize; i++) {

			// fazer ou n recombinacao
			rng = Math.random();

			if (rng <= this.rr) {

				max = this.maxSize - this.rfl;
				
				random = (int) (Math.round(Math.random() * max)); // melhorar a distribuicao atraves de round()
				randMax = random + this.rfl; // obter a localizacao de onde substituir [random - randomMax]

				//determinar outra sequencia a ir buscar
				copSeq = (int) (Math.random() * this.seqSize);

				this.sequences.set(i,
						this.sequences.get(i).substring(0, random)
								+ this.sequences.get(copSeq).substring(random, randMax)
								+ this.sequences.get(i).substring(randMax, this.maxSize));
			}
		}
	}

	// Compute the Hamming distance for each pair of sequences (same size)
	// The distance means the smallest number of substitutions to transform seq1 in seq2
	public void hammingDistance(){

		String seq1, seq2;
		int size, dist = 0;
		this.hammingDistances = new double[seqSize][seqSize];

		for(int i = 0; i < this.seqSize; i++){
			for(int j = i + 1; j < this.seqSize; j++){ //Half of the matrix (from the diagonal upwards)   
				
				seq1 = this.sequences.get(i); 
				seq2 = this.sequences.get(j);
				
				if (seq1.length() != seq2.length()) {
    				System.out.println("Error: Input sequences should have the same length");
    	    		System.exit(1);
	   			 }

	   			 size = seq1.length(); //both sequences have same length

				for (int c = 0; c < size; c++){ 
					if(seq1.charAt(c) != seq2.charAt(c)){
						System.out.println("Seq1; Char: "+seq1.charAt(c));
						System.out.println("Seq2; Char: "+seq2.charAt(c));
						dist++; //counting when the sequences differ from each other
					}
				}
				this.hammingDistances[i][j] = (double) dist/size; // % of mismatching sites
				dist = 0;
			}
		}
	}

	//Compute the Jukes-Cantor model for each pair of sequences (same size)
	public void jukesCantorModel(){
		
		double hdist, d, p = 0.75; //p - Proportion relevant of different sites, between two sequences
		this.jukesCantor = new double[seqSize][seqSize];

		for(int i = 0; i < this.seqSize; i++){
			for(int j = i + 1; j < this.seqSize; j++){  
				
				hdist = this.hammingDistances[i][j];
				if (hdist < p) {
					d = -(3.0/4.0) * Math.log(1.0-((4.0/3.0) * hdist));
				}
				else { //the value is ignored
					d = -1.0;
				}
				this.jukesCantor[i][j] = d;
			}
		}
	}

	// Compute the average Hamming distance and Jukes-Cantor for each generation
	// Write the values in a doc 
	public void plot(){

		double hAvg, jcAvg;
		double h = 0, jc = 0;
		int num = 0, ignored = 0; //num - values counted in sum; ignored - values ignored in Jukes-Cantor

		for(int i = 0; i < this.seqSize; i++){
			for(int j = i + 1; j < this.seqSize; j++){ 
				num ++;				
				h += hammingDistances[i][j]; //Sum of all Hamming distances
				
				if(jukesCantor[i][j] >= 0)
					jc += jukesCantor[i][j]; //Sum of all Jukes-Cantor values (excepto the proportions ignored)
				else
					ignored++;
			}
		}

		hAvg = h / num; //Average of Hamming distances
		jcAvg = jc / (num - ignored); //Average of Jukes-Cantor Model

		try {

			saveInputDirectory();

			FileWriter writer = new FileWriter(this.OUTFILENAME + "/plot.doc", true);
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			bufferedWriter.write("Geracao: " + actualGen + "\n");
			bufferedWriter.write(hAvg + "\n");
			bufferedWriter.write(jcAvg + "\n");
            bufferedWriter.newLine();
			
			bufferedWriter.close();

		} catch(IOException e){ 
			e.printStackTrace();
        } 
	}

	// Write the output in a fasta file
	public void output() {
		try {
			
			saveInputDirectory();
			
			// to save output file in the same folder than input
			FileWriter writer = new FileWriter(this.OUTFILENAME + "/output.fasta");
			BufferedWriter bufferedWriter = new BufferedWriter(writer);

			// to write each sequence in FASTA file
			for (int i = 0; i < this.sequences.size(); i++) {
				if(i<original.size()){
					bufferedWriter.write(">seq" + (i+1) + "_initial" + "\n");
					bufferedWriter.write(this.original.get(i) + "\n");
				}
				bufferedWriter.write(">seq" + "_" + (i + 1) + "\n");
				bufferedWriter.write(this.sequences.get(i) + "\n");
			}
			/*
			for(int i=0; i<this.sequences.size(); i++){
				for(int j=0; j<original.size(); j++){
					this.sequences.add(original.get(i));				
				}
			}*/
			

			bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Auxilixar function: save the file in the same directory of input file 
	public void saveInputDirectory () {

		if(this.INFILENAME==null) {
			this.OUTFILENAME = System.getProperty("user.home");
			this.OUTFILENAME+="/Desktop";
			System.out.println(this.OUTFILENAME);
		}
		else{
			int endIndex = this.INFILENAME.lastIndexOf("\\");
			if (endIndex != -1) {
				this.OUTFILENAME = this.INFILENAME.substring(0, endIndex);
			}
		}
	}

	// Generate a random sequence
	public void generate(){
		sequences = new ArrayList<String>();
		original = new ArrayList<String>();
		randomSeq();
		// to make copies of random sequence until population size
		for(int i=0; i < this.ps; i++){
			this.sequences.add(original.get(0));
		}
		check();
	}

	// Auxiliar function: random nucleotide generator and build sequence
	public void randomSeq(){
		int i = 0;
		int rand;
		
		//original = new ArrayList<String>();
		StringBuffer stringBuffer = new StringBuffer();
		
		rand = (int) (Math.random() * 3);
		
		while (i < this.ss) { // to generate random sequence with sequence size desired
			switch (rand) {
			case 0:
				stringBuffer.append("A");
				i++;
				break;
			case 1:
				stringBuffer.append("C");
				i++;
				break;
			case 2:
				stringBuffer.append("T");
				i++;
				break;
			case 3:
				stringBuffer.append("G");
				i++;
				break;
			}
			rand = (int) (Math.random() * 3);
		}
		System.out.println(stringBuffer);
		this.original.add(stringBuffer.toString());
	}
	
	// Auxiliar function
	private void check(){
		this.seqSize = this.sequences.size();
		
		this.maxSize = this.sequences.get(0).length();
		for(String tmp : this.sequences) {
			if(this.maxSize > tmp.length())
				this.maxSize = tmp.length();
		}
		
		if(this.rfl > this.maxSize)
			this.rfl = this.maxSize;
	}
	
	public void genNrandoms(){
		//int i=0;
		sequences = new ArrayList<String>();
		original = new ArrayList<String>();
		for(int j=0; j<this.originalSeq; j++){
			randomSeq();
		}
		System.out.println("Sequences: "+sequences);
		// to make copies of random sequence until population size
		
		for(int i=0; i<original.size(); i++){
			for(int j=0; j<((this.ps-this.originalSeq)/this.originalSeq); j++){
				this.sequences.add(original.get(i));				
			}
		}
		/*
		for (String tmp : this.original){
			if(i < ((this.ps - this.originalSeq)/this.originalSeq)){
				this.sequences.add(tmp);
			}
			i++;
		}*/
		check();
	}
}
