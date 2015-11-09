import java.io.InputStreamReader;
import java.util.Scanner;

public class Viterbi {

	//HMM model

	//Start probabilities
	private double[] start = {0.33, 0.33, 0.33};

	//Transition probabilities
	private double[][] transition = {
			{0.6, 0.4, 0},
			{0.25, 0.5, 0.25},
			{0.25, 0.25, 0.5}
	};
	
	private int tranSize = transition.length;

	//Emission probabilities: A T G C
	private double[][] emission = {
			{0.4, 0.3, 0.3, 0}, //s1
			{0.1, 0.1, 0.4, 0.4}, //s2
			{0.4, 0.3, 0, 0.3} //s3
	};

	//End probabilities 
	private double[] end = {0.33, 0.33, 0.33};

	/*
	//Start probabilities
	private double[] start;

	//Transition probabilities
	private double[][] transition;
	private int tranSize;

	//Emission probabilities: A T G C
	private double[][] emission;

	//End probabilities
	private double[] end;

	//Number of states
	private int tranSize;
	*/

	//Result matrix
	private double[][] matrix;

	//Matrix with simplified arrows
	private int[][] pointers;

	//Print matrix or not
	private boolean seeMatrix;

	//DNA sequence
	private String seq;
	private int seqSize;

	//Most likely path
	private String path;

	private long begin, totalTime;
	
	//Convert sequence char to number (column in matrix)
	public int getLetter(char c){
		
		switch(c){
		
			case 'A':
				return 0;
			case 'T':
				return 1;
			case 'G':
				return 2;
			case 'C':
				return 3;
		}
		
		return 0;
	}

	public static void main(String[] args) {

		Viterbi vit = new Viterbi();

		vit.processInput();
		vit.init();
		vit.compute();
		vit.backtrace();
		vit.printResults();
		
	}

	private void processInput() {

		Scanner in = new Scanner(System.in); 

		/*System.out.println("-- Detail the HMM model --");
		
		System.out.println("How many states? ");
		this.tranSize = in.nextInt();
		in.nextLine();	

        System.out.println("Enter the start probabilities:");
		System.out.println("Ex: Listagem de valores na vertical - s11, s12, etc.");

		this.start = new double[tranSize];
		for(int i = 0; i < this.tranSize; i++){ 
            start[i] = in.nextDouble();
        } 

        System.out.println("Enter the transition probabilities: ");
		System.out.println("Ex: Listagem de valores na vertical - a11 a12, etc.");

		this.transition = new double[tranSize][tranSize];
		for(int i = 0; i < tranSize; i++){ 
            for(int j = 0; j < tranSize; j++){ 
            	transition[i][j] = in.nextDouble();
        	}
        } 

		System.out.println("Enter the emission probabilities: ");
		System.out.println("Ex: Listagem de valores na vertical - eA, eT, eG e eC para cada estado.");

		this.emission = new double[tranSize][4];
		for(int i = 0; i < tranSize; i++){ 
            for(int j = 0; j < 4; j++){ 
            	emission[i][j] = in.nextDouble();
        	}
        }  

		System.out.println("Enter the end probabilities: ");
		System.out.println("Ex: Listagem de valores na vertical - e1 e2, etc.");

		this.end = new double[tranSize];
		for(int i = 0; i < tranSize; i++){ 
            end[i] = in.nextDouble();
        } 
        in.nextLine();*/
		
		System.out.println("Enter the DNA sequence: ");
		this.seq = in.nextLine();
		
		if (this.seq == null || this.seq.equals("")) {
			System.out.println("Error exiting");
			System.exit(1);
		}

		this.seq.toUpperCase();
		this.seqSize = this.seq.length();
	
		System.out.print("btw wish to see the final matrix? [true / false]: ");
		try {
			this.seeMatrix = Boolean.valueOf(in.nextLine().toLowerCase());
		} catch (Exception e) {
			System.out.println("Error exiting");
			System.exit(1);
		}

		in.close();

	}


	private void init() {

		int columnEm;

		this.matrix = new double[this.tranSize][this.seqSize];
		this.pointers = new int[this.tranSize][this.seqSize];

		/*for(int i = 0; i < tranSize; i++){
			for(int j = 0; j < seqSize; j++){
				pointers[i][j] = new ArrayList(); 
			}
		}*/

		//Detecta qual e o char q se esta a observar
		columnEm = getLetter(seq.charAt(0));

		//Preenche a 1a coluna da matriz
		for(int i = 0; i < tranSize; i++) {
			this.matrix[i][0] = this.start[i] * this.emission[i][columnEm];
		}

	}

	public void compute(){
		
		int columnEm, maxIndex;
		double[] score = new double[this.tranSize];
		double max;

		for(int i = 1; i < seqSize; i++){ //Percorre cada char da sequencia		
			
			columnEm = getLetter(seq.charAt(i));
			
			for(int j = 0; j < tranSize; j++){ //Percorre cada cj estado/sequencia

				for(int s = 0; s < tranSize; s++){ //Calcula o score vindo de cada estado
					score[s] = emission[j][columnEm] * matrix[s][i-1] * transition[s][j];
				}

				max = score[0];
				maxIndex = 0;
				
				for(int m = 0; m < score.length; m++) { //Calcula o max e o seu indice de todos os scores obtidos
					if(score[m] > max) {
						max = score[m];
						maxIndex = m;
					}
				}
				matrix[j][i] = max;
				pointers[j][i] = maxIndex; //Guarda o estado q deu origem ao score
			}
		} 
	}

	public void backtrace(){

		StringBuilder temPath = new StringBuilder();

		int next, prev, maxIndex = tranSize-1;
		double max = matrix[tranSize-1][seqSize-1];
		
		for(int m = 0; m < tranSize; m++) { //Calcula o max e o seu indice dos valores finais
			if(matrix[m][seqSize-1] > max) {
				max = matrix[m][seqSize-1];
				maxIndex = m;
			}
		}
		
		prev = pointers[maxIndex][seqSize-1];
		temPath.append((prev + 1) + "S ");
		
		for(int i = seqSize-1; i > 0 ; i--){ //Acede ao valor da matriz pointers que contem o proximo estado a ir
			
			next = pointers[prev][i];
			temPath.append((next + 1) + "S ");
			prev = next;
		}

		this.path = new String(temPath.reverse().toString());
	}

	private void printResults() {
		if (this.seeMatrix) {
			System.out.print("\n\nFinal matrix result:");
			//printMatrix();
		}

		System.out.println("\n\nMost likely path: " + this.path);
	}


	private void printMatrix() {

		
	}

}
