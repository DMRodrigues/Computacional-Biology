package exercise;

import java.io.InputStreamReader;
import java.util.Scanner;

import org.biojava.nbio.alignment.SimpleSubstitutionMatrix;
import org.biojava.nbio.alignment.template.SubstitutionMatrix;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;

public class NeedlemanWunsch {

	// sequencia 1 info
	private String seq1 = null;
	private int seq1Size = -1;

	// sequencia 2 info
	private String seq2 = null;
	private int seq2Size = -1;

	private int penalty; // sera bom ter valor default?

	// matriz de BLOSUM ou PAM a usar
	private SubstitutionMatrix<AminoAcidCompound> matrix;

	// matriz que ira ter o resultado do algoritmo
	private int[][] matrixRes;

	// conjunto de dados finais obtidos
	private int score;
	private String finalSeq1 = new String();
	private String finalSeq2 = new String();
	private String guideLine = new String("            ");

	// se quiser usar matriz ou nao; se quiser ver matriz no fim
	private boolean isMatrix, seeMatrix;

	// valores caso n qeira usar matriz
	private short match, mismatch;

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
		seq1 = in.nextLine();

		// simples verificacao
		if (seq1 == null || seq1.equals("")) {
			System.out.println("Error exiting =(");
			System.exit(1);
		}

		// SAFE TO UPDATE
		seq1Size = seq1.length();

		System.out.print("Enter sequence 2: ");
		seq2 = in.nextLine();

		// simples verificacao
		if (seq2 == null || seq2.equals("")) {
			System.out.println("Error exiting =(");
			System.exit(1);
		}

		// SAFE TO UPDATE
		seq2Size = seq2.length();

		System.out.print("Enter gap penalty: ");
		try {
			penalty = new Integer(in.nextLine()); // verificar valor de penalty ???
		} catch (Exception e) {
			System.out.println("Error exiting =(");
			System.exit(1);
		}

		// processar pa saber q tipo de matriz qer
		System.out.print("Wish scoring matrix(or match/mismatch)? [true / false] : ");
		try {
			isMatrix = Boolean.valueOf(in.nextLine().toLowerCase());
		} catch (Exception e) {
			System.out.println("Error exiting =(");
			System.exit(1);
		}

		if (!isMatrix) {
			System.out.print("Enter match value: ");
			try {
				match = Short.parseShort(in.nextLine().toLowerCase());
			} catch (Exception e) {
				System.out.println("Error exiting =(");
				System.exit(1);
			}
			System.out.print("Enter mismatch value: ");
			try {
				mismatch = Short.parseShort(in.nextLine().toLowerCase());
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
				matrix = new SimpleSubstitutionMatrix<AminoAcidCompound>(AminoAcidCompoundSet.getAminoAcidCompoundSet(),
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
			seeMatrix = Boolean.valueOf(in.nextLine().toLowerCase());
		} catch (Exception e) {
			System.out.println("Error exiting =(");
			System.exit(1);
		}

		in.close();

		System.out.print("Thank you, now let's do some hard work...");
	}

	// inicializar a matriz de resultados a zero
	private void init() {
		matrixRes = new int[seq1Size + 1][seq2Size + 1];

		for (int i = 0; i <= seq1Size; i++) {
			for (int j = 0; j <= seq2Size; j++) {
				if (i == 0) {
					matrixRes[i][j] = (j * penalty); // penalty e linear logo multiplica
				} else if (j == 0) {
					matrixRes[i][j] = (i * penalty);
				} else {
					matrixRes[i][j] = 0;
				}
			}
		}
	}

	// imprimir matriz resultado so pa confirmar
	private void printMatrix() {
		for (int i = 0; i <= seq2Size; i++) {
			for (int j = 0; j <= seq1Size; j++) {
				if (matrixRes[j][i] < -9)
					System.out.print(matrixRes[j][i] + "|");
				else if (matrixRes[j][i] < 0)
					System.out.print(" " + matrixRes[j][i] + "|");
				else if (matrixRes[j][i] < 10)
					System.out.print("  " + matrixRes[j][i] + "|");
				else
					System.out.print(" " + matrixRes[j][i] + "|");
			}
			System.out.println();
		}
	}

	// fazer o processamento da matriz dos resultados com base na matriz dada
	private void compute() {
		for (int i = 1; i <= seq1Size; i++) {
			for (int j = 1; j <= seq2Size; j++) {

				// se for match/mistach com base na tabela ou valor dado
				int diagonalValue = matrixRes[i - 1][j - 1] + convertCompoundToValue(
						Character.toString(seq1.charAt(i - 1)), Character.toString(seq2.charAt(j - 1)));

				// se gap horizontal
				int leftValue = matrixRes[i][j - 1] + penalty;

				// se gap vertical
				int upValue = matrixRes[i - 1][j] + penalty;

				// ir buscar o melhor resultado dos 3
				matrixRes[i][j] = Math.max(Math.max(diagonalValue, leftValue), upValue);
			}
		}
	}

	// obter o valor do match/mismatch tendo conta a entrada na tabela
	private short convertCompoundToValue(String s1, String s2) {

		// se tiver usar matriz blosum ir buscar valor tendo conta os aminoacid "A", "B", etc
		if (isMatrix)
			return matrix.getValue(AminoAcidCompoundSet.getAminoAcidCompoundSet().getCompoundForString(s1),
					AminoAcidCompoundSet.getAminoAcidCompoundSet().getCompoundForString(s2));

		// se usar valor match/mismatch
		else {
			if (s1.equals(s2))
				return match;
			else
				return mismatch;
		}
	}

	// efectuar o processamento traceback e saber qual a sequencia obtida
	private void backtrack() {

		score = matrixRes[seq1Size][seq2Size]; // resultado na ultima posicao

		// strings temp pa guardar uma dos resultados optimos
		StringBuilder a = new StringBuilder(), b = new StringBuilder(), gl = new StringBuilder();

		for (int i = seq1Size, j = seq2Size; i > 0 || j > 0;) {

			// e match/mistach investigar melhor
			if (i > 0 && j > 0 && matrixRes[i][j] == matrixRes[i - 1][j - 1] + convertCompoundToValue(
					Character.toString(seq1.charAt(i - 1)), Character.toString(seq2.charAt(j - 1)))) {

				a.append(seq1.charAt(--i));
				b.append(seq2.charAt(--j));

				if (seq1.charAt(i) == seq2.charAt(j)) // se for match
					gl.append("|");
				else
					gl.append(" "); // se for mismatch
			}
			// gap horizontal
			else if (i > 0 && matrixRes[i][j] == matrixRes[i - 1][j] + penalty) {
				a.append(seq1.charAt(--i));
				b.append("-"); // se for gap
				gl.append(" ");
			}
			// gap vertical
			else if (j > 0 && matrixRes[i][j] == matrixRes[i][j - 1] + penalty) {
				b.append(seq2.charAt(--j));
				a.append("-"); // se for gap
				gl.append(" ");
			}
		}

		/* Com while n trabalha la mt bem n sei prq =( 
		while ((i > 0) || (j > 0)) {

			System.out.println(i + " " + j + " " + matrixRes[i][j]);

			if ((i > 0) && (j > 0) && matrixRes[i][j] == matrixRes[i - 1][j - 1] + convertCompoundToInt(
					Character.toString(seq1.charAt(i - 1)), Character.toString(seq2.charAt(j - 1)))) {
				finalA += seq1.charAt(i - 1);
				finalB += seq2.charAt(j - 1);
				i--;
				j--;
				System.out.println("1");
			} else if (matrixRes[i][j] == matrixRes[i][j - 1] + penalty) {
				if (j == 0)
					finalA += new String("_");
				else {
					finalA += new String("_");
					finalB += seq2.charAt(j - 1);
					j--;
				}
				System.out.println("2");
			} else {
				finalA += seq1.charAt(i - 1);
				finalB += new String("_");
				i--;
				System.out.println("3"); // continue;
			}
		}
		*/

		finalSeq1 = new String(a.reverse().toString());
		guideLine = guideLine + new String(gl.reverse().toString());
		finalSeq2 = new String(b.reverse().toString());
	}

	private void printScoreAndAlignment() {
		if (seeMatrix) {
			System.out.println("\n\nFinal matrix result: ");
			printMatrix();
		}
		System.out.println("\nOptimal score: " + score);
		System.out.println("\nAlignment");
		System.out.println("Sequence 1: " + finalSeq1);
		System.out.println(guideLine);
		System.out.println("Sequence 2: " + finalSeq2);
		System.out.println();
	}
}
