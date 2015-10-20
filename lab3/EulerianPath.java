import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class EulerianPath extends Graph {

	private int num = -1;
	private String text = null;

	public static void main(String[] args) 
    {
    	EulerianPath eulerPath = new EulerianPath();
		eulerPath.processInput();
		//eulerPath.compute();
        //eulerPath.printResult();
	}

	private void processInput() 
    {	
        String pattern = null;
        int size = -1;
        String lastTemp = null;
        String recentTemp = null;
		
        Scanner input = new Scanner(System.in);

		System.out.println("How many patterns? ");
		num = input.nextInt();
		
		Graph adjlist = new Graph();
		
		System.out.println("Enter the patterns:");
		
		for (int i = 0; i < num; i++) {
			pattern = input.next();
            size = pattern.length();
		
		   for (int j=0; j < (size-1); j++) { //Divide cada string em sequencias de 2
            	recentTemp = pattern.substring(j, j+2);
            	adjlist.add(recentTemp);

            	if (j > 0) { //Se nao for a primeira sub-sequencia, e necessario criar os edges entre as sub-sequencias
            		adjlist.add(lastTemp, recentTemp);
            	} 
            	
            	lastTemp = recentTemp; //Actualiza a var para na iteracao seguinte, saber a origem do edge
            }
		}

        System.out.println("Adjacency List: ");
        System.out.println(adjlist.toString());

	}

	private void printResult() 
    {
	//	System.out.println("Text: ", text);
	//System.out.println("It took: " + totalTime + " miliseconds\n");
	}


}