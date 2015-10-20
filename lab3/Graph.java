import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class Graph {
    
    private Map<String,List<String>> neighbors = new HashMap<String,List<String>>();
    
    /**
     * String representation of graph
     */
    public String toString () 
    {
        StringBuffer s = new StringBuffer();

        for (String v: neighbors.keySet()) {
			s.append("\n    " + v + " -> " + neighbors.get(v));
		} 
        return s.toString();                
    }
    
    /**
     * Gett all vertex
     */
    public Set<String> getAllVertex() 
    {
        return neighbors.keySet();
    }

    /**
     * Get All edges
     */
    public Collection<List<String>> getAllEdges() 
    {
    	return neighbors.values();
    }

    /**
     * Get vertex edges
     */
    public List<String> getNeighbors(String vertex) 
    {
        return neighbors.get(vertex);
    }

    /**
     * Add a vertex to the graph
    */
    public void add (String vertex) 
    {
        if (neighbors.containsKey(vertex)) return;
        neighbors.put(vertex, new ArrayList<String>());
    }
    
    /**
     * Add an edge to the graph
     */
    public void add (String from, String to) 
    {
        this.add(from); this.add(to);
        neighbors.get(from).add(to);
    }

    /**
     * True if graph contains vertex
     */
    public boolean contains (String vertex) 
    {
        return neighbors.containsKey(vertex);
    }
    
    /**
     * Remove an edge from the graph
     */
    public void remove (String from, String to) 
    {
        if (!(this.contains(from) && this.contains(to)))
            throw new IllegalArgumentException("Nonexistent vertex");
        neighbors.get(from).remove(to);
    }

    /**
     * Out-degree of each vertex
     */
    public Map<String,Integer> outDegree () 
    {
        Map<String,Integer> result = new HashMap<String,Integer>();
        
        for (String v: neighbors.keySet()) {
        	result.put(v, neighbors.get(v).size());	
        } 
        return result;
    }
    
    /**
     * In-degree of each vertex
     */
    public Map<String,Integer> inDegree () 
    {
        Map<String,Integer> result = new HashMap<String,Integer>();
        
        for (String v: neighbors.keySet()) {
        	result.put(v, 0);       
        }
        for (String from: neighbors.keySet()) {
            for (String to: neighbors.get(from)) {
                result.put(to, result.get(to) + 1);           
            }
        }
        return result;
    }

    public static void main (String[] args) 
    {
    	Graph graph = new Graph();
        graph.add("A", "C"); 
        graph.add("A", "D"); 
        graph.add("C", "T");
        graph.add("G", "T"); 
        System.out.println(graph.getNeighbors("A"));
        System.out.println(graph.getAllVertex());
    }
}