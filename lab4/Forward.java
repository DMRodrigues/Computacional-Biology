import java.io.InputStreamReader;
import java.util.Scanner;

public class Forward {


	//Start probabilities
	private double[] start;

	//Transition probabilities
	private double[][] transition;
	
	//Number of states
	private int tranSize;

	//Emission probabilities: A T G C
	private double[][] emission;

	//End probabilities
	private double[] end;
	
	//Result matrix
	private double[][] matrix;

	//Print matrix or not
	private boolean seeMatrix;

	//DNA sequence
	private String seq;
	private int seqSize;

	//Probability P(X)
	private double probability;

	private long begin, totalTime;


	public static void main(String[] args) {

		Forward fwd = new Forward();

		fwd.processInput();
		fwd.init();
		fwd.compute();
		fwd.printResults();
	}

	private void processInput() {

		Scanner in = new Scanner(System.in); 

		System.out.println("-- Detail the HMM model --");
		
		System.out.println("\nHow many states? ");
		this.tranSize = in.nextInt();
		in.nextLine();	

		System.out.println("\nATENCAO: Nos valores seguintes, usar virgula nos numeros decimais.");

        System.out.println("\nEnter the start probabilities:");
		System.out.println("Ex: Listagem de valores na vertical - s11, s12, etc.");

		this.start = new double[tranSize];
		for(int i = 0; i < this.tranSize; i++){ 
            start[i] = in.nextDouble();
        } 

        System.out.println("\nEnter the transition probabilities: ");
		System.out.println("Ex: Listagem de valores na vertical - a11 a12, etc.");

		this.transition = new double[tranSize][tranSize];
		for(int i = 0; i < tranSize; i++){ 
            for(int j = 0; j < tranSize; j++){ 
            	transition[i][j] = in.nextDouble();
        	}
        } 

		System.out.println("\nEnter the emission probabilities: ");
		System.out.println("Ex: Listagem de valores na vertical - eA, eT, eG e eC para cada estado.");

		this.emission = new double[tranSize][4];
		for(int i = 0; i < tranSize; i++){ 
            for(int j = 0; j < 4; j++){ 
            	emission[i][j] = in.nextDouble();
        	}
        }  

		System.out.println("\nEnter the end probabilities: ");
		System.out.println("Ex: Listagem de valores na vertical - e1 e2, etc.");

		this.end = new double[tranSize];
		for(int i = 0; i < tranSize; i++){ 
            end[i] = in.nextDouble();
        } 
        in.nextLine();
		
		System.out.println("\nEnter the DNA sequence: ");
		this.seq = in.nextLine();
		
		if (this.seq == null || this.seq.equals("")) {
			System.out.println("Error exiting");
			System.exit(1);
		}

		this.seq.toUpperCase();
		this.seqSize = this.seq.length();
	
		System.out.print("\nWish to see the final matrix? [true / false]: ");
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

		this.begin = System.currentTimeMillis();
		this.matrix = new double[this.tranSize][this.seqSize];
	
		//Detecta qual e o char q se esta a observar
		columnEm = getLetter(seq.charAt(0));

		//Preenche a 1a coluna da matriz
		for(int i = 0; i < tranSize; i++) {
			this.matrix[i][0] = this.start[i] * this.emission[i][columnEm];
		}

	}

	public void compute(){
		
		int columnEm, maxIndex;
		double max, sum = 0.0;
		probability = 0.0;

		for(int i = 1; i < seqSize; i++){ //Percorre cada char da sequencia		
			
			columnEm = getLetter(seq.charAt(i));
			
			for(int j = 0; j < tranSize; j++){ //Percorre cada cj estado/sequencia
	
				for(int s = 0; s < tranSize; s++){ //Calcula a soma de scores vindos de cada estado
					sum += emission[j][columnEm] * matrix[s][i-1] * transition[s][j];
				}
				
				matrix[j][i] = sum;
				sum = 0.0;
			}
		}

		for(int i = 0; i < tranSize; i++){
			probability += (matrix[i][seqSize-1]) * end[i];
		}

		this.totalTime = System.currentTimeMillis() - this.begin;
	}

	private void printResults() {
		if (this.seeMatrix) {
			System.out.print("\n\nFinal matrix result:");
		//	printMatrix();
		}

		System.out.println("\nProbability P(X): " + this.probability);

		System.out.println("\nIt took: " + this.totalTime + " miliseconds\n");
	}

	//Auxiliar

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

	private void printMatrix() {

		for(int i = 0; i < tranSize; i++){
			for(int j = 0; j < seqSize; j++){
				System.out.println(i + "  " + j + "  " + matrix[i][j]);
			}
		}
		
	}

}