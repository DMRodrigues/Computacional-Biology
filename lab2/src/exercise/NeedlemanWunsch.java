package exercise;

import java.io.InputStreamReader;
import java.util.Scanner;

import org.biojava.nbio.alignment.SimpleSubstitutionMatrix;
import org.biojava.nbio.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;

/* TODO
 * 		update backtrack to find all sequence's
 * 		update backtrack on compare to NOT calculate the values again
*/
public class NeedlemanWunsch {

	// sequencia 1 info
	private String seq1 = null;
	private int seq1Size = -1;

	// sequencia 2 info
	private String seq2 = null;
	private int seq2Size = -1;

	private String guideLine;

	private int penalty; // sera bom ter valor default?

	// matriz de BLOSUM ou PAM a usar
	private SubstitutionMatrix<AminoAcidCompound> matrix;

	// matriz que ira ter o resultado do algoritmo
	private int[][] matrixRes;

	// conjunto de dados finais obtidos
	private int score;

	// se quiser usar matriz ou nao; se quiser ver matriz no fim
	private boolean isMatrix, seeMatrix;

	// valores caso n qeira usar matriz
	private short match, mismatch;

	private int total = 0;

	// quanto tempo demora algoritmo
	private long begin, totalTime;

	public static void main(String[] args) {

		NeedlemanWunsch nw = new NeedlemanWunsch();
		nw.processInput();
		nw.init();
		nw.compute();
		nw.backtrack();
		nw.printScoreAndAlignment();

		System.out.println("All done, thank you!!!");
	}

	// processar as informacoes de input
	private void processInput() {

		System.out.println("Welcome!!!");

		Scanner in = new Scanner(System.in); // pa ler do input as sequencias e cenas assim

		System.out.print("Enter sequence 1: ");
		this.seq1 = in.nextLine();

		// simples verificacao
		if (this.seq1 == null || this.seq1.equals("")) {
			System.out.println("Error exiting =(");
			System.exit(1);
		}

		// SAFE TO UPDATE
		this.seq1.toUpperCase();
		this.seq1Size = this.seq1.length();

		System.out.print("Enter sequence 2: ");
		this.seq2 = in.nextLine();

		// simples verificacao
		if (this.seq2 == null || this.seq2.equals("")) {
			System.out.println("Error exiting =(");
			System.exit(1);
		}

		// SAFE TO UPDATE
		this.seq2.toUpperCase();
		this.seq2Size = this.seq2.length();

		System.out.print("Enter gap penalty: ");
		try {
			this.penalty = new Integer(in.nextLine()); // verificar valor de penalty ???
		} catch (Exception e) {
			System.out.println("Error exiting =(");
			System.exit(1);
		}

		// processar pa saber q tipo de matriz qer
		System.out.print("Wish scoring matrix(or match/mismatch)? [true / false] : ");
		try {
			this.isMatrix = Boolean.valueOf(in.nextLine().toLowerCase());
		} catch (Exception e) {
			System.out.println("Error exiting =(");
			System.exit(1);
		}

		if (!this.isMatrix) {
			System.out.print("Enter match value: ");
			try {
				this.match = Short.parseShort(in.nextLine().toLowerCase());
			} catch (Exception e) {
				System.out.println("Error exiting =(");
				System.exit(1);
			}
			System.out.print("Enter mismatch value: ");
			try {
				this.mismatch = Short.parseShort(in.nextLine().toLowerCase());
			} catch (Exception e) {
				System.out.println("Error exiting =(");
				System.exit(1);
			}

		} else {
			String tempMatrixString = null;

			// processar pa saber q tipo de matriz qer
			System.out.print("Enter scoring matrix name: ");
			tempMatrixString = in.nextLine().toLowerCase();

			// WARNING n se verifica se matriz existe (+ facil)
			try {
				// criar uma matriz BLOSUM ou PAM conforme a escolha
				// o 2 argumento da SimpleSubstitutionMatrix vai procurar na biblioteca biojava
				this.matrix = new SimpleSubstitutionMatrix<AminoAcidCompound>(
						AminoAcidCompoundSet.getAminoAcidCompoundSet(),
						new InputStreamReader(SimpleSubstitutionMatrix.class
								.getResourceAsStream(new String("/" + tempMatrixString + ".txt"))),
						tempMatrixString);

			} catch (Exception e) {
				System.out.println("Wrong matrix name!!!");
				System.out.println("Exiting =(");
				System.exit(1);
			}

		}

		System.out.print("btw wish to see the final matrix? [true / false]: ");
		try {
			this.seeMatrix = Boolean.valueOf(in.nextLine().toLowerCase());
		} catch (Exception e) {
			System.out.println("Error exiting =(");
			System.exit(1);
		}

		in.close();

		System.out.print("Thank you, now let's do some hard work...");
	}

	// inicializar a matriz de resultados a zero
	private void init() {

		this.begin = System.currentTimeMillis();

		this.matrixRes = new int[this.seq1Size + 1][this.seq2Size + 1]; // contar com gaps

		for (int i = 0; i <= this.seq1Size; i++) {
			this.matrixRes[i][0] = (i * this.penalty); // penalty e linear logo multiplica
		}
		for (int j = 0; j <= this.seq2Size; j++) {
			this.matrixRes[0][j] = (j * this.penalty);
		}
	}

	// imprimir matriz resultado so pa confirmar
	private void printMatrix() {

		System.out.print("\n | _ |");
		for (int i = 0; i < this.seq1.length(); i++) {
			System.out.print(" " + this.seq1.charAt(i) + " |");
		}
		System.out.print("\n_|");
		for (int i = 0; i <= this.seq2Size; i++) {
			for (int j = 0; j <= this.seq1Size; j++) {
				if (this.matrixRes[j][i] < -9)
					System.out.print(matrixRes[j][i] + "|");
				else if (this.matrixRes[j][i] < 0)
					System.out.print(" " + this.matrixRes[j][i] + "|");
				else if (this.matrixRes[j][i] < 10)
					System.out.print("  " + this.matrixRes[j][i] + "|");
				else
					System.out.print(" " + this.matrixRes[j][i] + "|");
			}
			System.out.println();
			if (i != this.seq2Size) // ficou engatado por causa dos gaps
				System.out.print(seq2.charAt(i) + "|");
		}
	}

	// fazer o processamento da matriz dos resultados com base na matriz dada
	private void compute() {

		int diagonalValue, leftValue, upValue;

		for (int i = 1; i <= this.seq1Size; i++) {
			for (int j = 1; j <= this.seq2Size; j++) {

				// se for this.match/mistach com base na tabela ou valor dado
				diagonalValue = this.matrixRes[i - 1][j - 1] + convertCompoundToValue(
						Character.toString(this.seq1.charAt(i - 1)), Character.toString(this.seq2.charAt(j - 1)));

				// se gap horizontal
				leftValue = this.matrixRes[i][j - 1] + this.penalty;

				// se gap vertical
				upValue = this.matrixRes[i - 1][j] + this.penalty;

				// ir buscar o melhor resultado dos 3
				this.matrixRes[i][j] = Math.max(Math.max(diagonalValue, leftValue), upValue);
			}
		}
	}

	// efectuar o processamento traceback e saber qual a sequencia obtida
	private void backtrack() {

		boolean flag = false;

		this.score = this.matrixRes[this.seq1Size][this.seq2Size]; // resultado na ultima posicao

		StringBuilder a = new StringBuilder(), b = new StringBuilder(), gl = new StringBuilder();

		int i = seq1Size, j = seq2Size;

		while ((i > 0) || (j > 0)) {

			// e match/mistach investigar melhor
			if (i > 0 && j > 0 && this.matrixRes[i][j] == this.matrixRes[i - 1][j - 1] + convertCompoundToValue(
					Character.toString(this.seq1.charAt(i - 1)), Character.toString(this.seq2.charAt(j - 1)))) {

				a.append(this.seq1.charAt(--i));
				b.append(this.seq2.charAt(--j));

				if (this.seq1.charAt(i) == this.seq2.charAt(j)) // se for match
					gl.append("|");
				else
					gl.append(" "); // se for mismatch

				flag = true;
			}
			// gap horizontal
			if (i > 0 && this.matrixRes[i][j] == this.matrixRes[i - 1][j] + this.penalty) {

				if (flag)
					this.total++;
				else {
					a.append(this.seq1.charAt(--i));
					b.append("-"); // se for gap
					gl.append(" ");
				}
			}
			// gap vertical
			if (j > 0 && this.matrixRes[i][j] == this.matrixRes[i][j - 1] + this.penalty) {

				if (flag)
					this.total++;
				else {
					b.append(this.seq2.charAt(--j));
					a.append("-"); // se for gap
					gl.append(" ");
				}
			}
			flag = false;
		}

		// update seqs
		seq1 = new String(a.reverse().toString());
		seq2 = new String(b.reverse().toString());
		guideLine = new String("            " + gl.reverse().toString());

		this.totalTime = System.currentTimeMillis() - this.begin;
	}

	private void printScoreAndAlignment() {
		if (this.seeMatrix) {
			System.out.print("\n\nFinal matrix result:");
			printMatrix();
		}
		System.out.println("\n\nOptimal score: " + this.score);
		System.out.println("\nTotal sequences: " + this.total);
		System.out.println("\nSolution");
		System.out.println("Sequence 1: " + this.seq1);
		System.out.println(this.guideLine);
		System.out.println("Sequence 2: " + this.seq2);
		System.out.println();
		System.out.println("It took: " + this.totalTime + " miliseconds\n");
	}

	// obter o valor do this.match/this.mismatch tendo conta a entrada na tabela
	private short convertCompoundToValue(String s1, String s2) {

		// se tiver usar matriz blosum ir buscar valor tendo conta os aminoacid "A", "B", etc
		if (this.isMatrix)
			return this.matrix.getValue(AminoAcidCompoundSet.getAminoAcidCompoundSet().getCompoundForString(s1),
					AminoAcidCompoundSet.getAminoAcidCompoundSet().getCompoundForString(s2));

		// se usar valor this.match/this.mismatch
		else {
			if (s1.equals(s2))
				return this.match;
			else
				return this.mismatch;
		}
	}
}
