package exercise;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;

public class Kmeans {

private String INFILENAME; // input file

private int rows; // n observacoes
private int col; // variaveis
private double[][] data; // info dataset (observacoes e variaveis)
private List<String> label; // label observacao (nao serve para nada)
private List<String> unknown; // localizacao dados desconhecidos

private int nCluster; // numero de clusters
private Map<Integer, List<Integer>> cluster; // clusters
private List<double[]> centroid; // centro dos clusters

private double[][] distance; // distancia das observacoes ate cada cluster
private int update; // flag alteracoes na iteracao

public int getNCluster() {
	return this.nCluster;
}

public void setNCluster(int value) {
	this.nCluster = value;
}

// Parse do ficheiro de input
public void input() {

	File sf = null;
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
	int result = fileChooser.showOpenDialog(fileChooser);
	if (result == JFileChooser.APPROVE_OPTION) {
		sf = fileChooser.getSelectedFile();
	}

	this.INFILENAME = sf.getAbsolutePath();

	try {
		FileReader reader = new FileReader(INFILENAME);
		BufferedReader bufferedreader = new BufferedReader(reader);

		this.rows = Integer.parseInt(bufferedreader.readLine()); // numero de observacoes
		this.col = Integer.parseInt(bufferedreader.readLine()); // numero de variaveis

		this.data = new double[this.rows][this.col];
		this.label = new ArrayList<String>();
		this.unknown = new ArrayList<String>();
		String[] obs = null;

		for (int r = 0; r < this.rows; r++) { // por cada observacao do dataset

			// obs = bufferedreader.readLine().split(" "); // GROUP 1, split  em variaveis e label
			obs = bufferedreader.readLine().split(","); // GROUP 2, split em variaveis e label

			for (int c = 0; c < this.col; c++) { // por cada variavel
				if (obs[c].equals("?")) { // se tiver data desconhecida
					this.unknown.add(r + ";" + c); // guarda no formato (observacao;variavel)
					this.data[r][c] = 9999; // para posicao nao ficar a null (futuros calculos)
				} else
					this.data[r][c] = Double.parseDouble(obs[c]);
			}
			label.add(obs[obs.length - 1]);
		}
		reader.close();
		updateUnknown();

	} catch (IOException e) {
		e.printStackTrace();
	}
}

// Actualiza os dados desconhecidos com a media da coluna
private void updateUnknown() {

	String[] unk;
	int row, col, rowsUnk = 0;
	double avg = 0; // media da coluna

	for (int i = 0; i < this.unknown.size(); i++) {
		unk = this.unknown.get(i).split(";");
		row = Integer.parseInt(unk[0]);
		col = Integer.parseInt(unk[1]);

		for (int j = 0; j < rows; j++) {
			if (this.data[j][col] == 9999) {
				rowsUnk++;
			} else
				avg += this.data[j][col];
		}
		avg = avg / (this.rows - rowsUnk); // retira o numero de rows com data desconhecida na coluna
		this.data[row][col] = avg;
	}
}

// 1º Passo: Inicializacoes gerais. Inicializa o centro dos clusters
public void inicialization() {

	this.distance = new double[this.rows][this.nCluster];
	this.centroid = new ArrayList<double[]>();
	this.cluster = new HashMap<Integer, List<Integer>>();
	double[] center;
	int obsRand;
	List<Integer> list;

	for (int i = 0; i < this.nCluster; i++) {
		center = new double[col];
		obsRand = (int) (Math.round(Math.random() * (this.rows - 1))); // escolhe // uma observacao para ser o centro do Cluster

		for (int j = 0; j < col; j++) {
			center[j] = this.data[obsRand][j]; // atribui ao centro do cluster, os valores da observacao escolhida
		}
		this.centroid.add(i, center);

		list = new ArrayList<Integer>();
		this.cluster.put(i, list); // inicializa lista de clusters
	}
	this.update = 1; // inicializa para comecar a execucao
}

// Ciclo principal
public void compute() {

	int i = 0;

	while (this.update != 0) { // stop quando nao ocorrer alteracoes
		i++;
		computeDistance(); // 2º Passo: calcula a distancia das observacoes ate cada cluster
		groupClusters(); // 3º Passo: agrupa as observacoes por cluster (criterio: distancia minima)
		updateCentroid(); // 4º Passo: actualiza o centro dos clusters (media das observacoes dentro do cluster)
	}

	System.out.println("------ Clusters Finais ------  ");
	printClusters();
	System.out.println("Iteracoes: " + i);
	System.out.println("Dados desconhecidos: " + this.unknown.size());
}

// 2º Passo
public void computeDistance() {

	double dist = 0;
	double[] center = new double[col];

	this.update = 0;
	for (int id = 0; id < this.nCluster; id++) {
		center = this.centroid.get(id);
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.col; j++) {
				dist += Math.pow(this.data[i][j] - center[j], 2); // eleva ao quadrado
			}
			this.distance[i][id] = Math.sqrt(dist); // distancia euclidiana: srq(sum[data - centro]^2)
			dist = 0;
		}
	}
}

// 3º Passo
public void groupClusters() {

	int minIndex;
	double minDist;

	for (int r = 0; r < this.rows; r++) {
		minDist = this.distance[r][0];
		minIndex = 0;
		for (int c = 0; c < this.nCluster; c++) {
			if (minDist > this.distance[r][c]) { // calcula o cluster corresponde a distancia minima
				minDist = this.distance[r][c];
				minIndex = c;
			}
		}
		if (!this.cluster.get(minIndex).contains(r)) { // se a observacao  ainda nao estiver no cluster
			this.cluster.get(minIndex).add(r);
			this.update = 1;

			for (int c = 0; c < this.nCluster; c++) { // verifica se a observacao esta noutro cluster
				if ((c != minIndex) && this.cluster.get(c).contains(r))
					this.cluster.get(c).remove(new Integer(r)); // remove se estiver
			}
		}
	}
}

// 4º Passo
public void updateCentroid() {

	List<Integer> clusterEl;
	double[] center;
	int obs, clusterSize;

	for (int i = 0; i < this.nCluster; i++) {

		center = new double[this.col];
		clusterEl = this.cluster.get(i);
		clusterSize = clusterEl.size();

		if (clusterSize > 1) { // se cluster vazio ou 1 elemento, o centro mantem-se
			for (int j = 0; j < clusterSize; j++) {
				obs = clusterEl.get(j);
				for (int c = 0; c < this.col; c++) {
					center[c] += this.data[obs][c];
				}
			}
			for (int ct = 0; ct < this.col; ct++) {
				center[ct] = (center[ct] / clusterSize);
			}
			this.centroid.add(i, center); // actualiza o centro
		}
	}
}

public void printClusters() {

	for (int i = 0; i < nCluster; i++) {
		System.out.println("Cluster " + (i + 1));
		Collections.sort(this.cluster.get(i));
		System.out.println(this.cluster.get(i));
		System.out.println("Nº de elementos: " + this.cluster.get(i).size() + ", " + ((100 * this.cluster.get(i).size() / this.rows) + "%"));
		}
	}

}
