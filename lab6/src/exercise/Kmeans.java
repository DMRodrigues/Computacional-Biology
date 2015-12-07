package exercise;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

public class Kmeans {

	private String INFILENAME; // input file
	private double[][] matrix; 
    private int rows; // n observations
	private int col; // variables
	private List<String> labels; // label of each observation
	
	public void input() {
		
		// to create interactive window to input file
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
					
					this.rows =  Integer.parseInt(bufferedreader.readLine());
					this.col = Integer.parseInt(bufferedreader.readLine());
					
					this.matrix = new double[this.rows][this.col];
					this.labels = new ArrayList<String>();
					String[] obs = null;
					
					for(int r = 0; r < this.rows; r++){  // for each line in the file
						obs = bufferedreader.readLine().split(" ");  // split the observation ->  variable fields and label
						for(int v = 0; v < this.col; v++){ // for each variable
							this.matrix[r][v] = Double.parseDouble(obs[v]);						
						}
						labels.add(obs[obs.length-1]);					
					}
					reader.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
	}
	
}
