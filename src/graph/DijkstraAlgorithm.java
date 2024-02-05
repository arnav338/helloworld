package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class DijkstraAlgorithm {
	/*
	 * Dijkstra -> (j silent) (Di-ekstra algorithm)
	 * 
	 * Used to find single source shortest path algorithm
	 * 
	 * we create an array for all values having array value as infinity
	 * and array index representing value of that node
	 * 
	 * when we start from a node, we mark all nodes connected to this node as active nodes
	 * and update the value of all active nodes
	 * the value we set at this step are tentative, we might find a better value for it
	 * From all the active nodes we find the node having min value
	 * and again mark active nodes for this current node.
	 * 
	 * The node to which we have moved we will mark that as final min value, indicating
	 * that this min value will not be updated futher
	 * 
	 * repeat this process until no active node is left
	 * time complexity - O((V+E)LogV) ; E - edges, V - vertices
	 * */
	
	static class AdjListNode {
        int vertex, weight;
 
        AdjListNode(int v, int w)
        {
            vertex = v;
            weight = w;
        }
        int getVertex() { return vertex; }
        int getWeight() { return weight; }
    }
	
	// Function to find the shortest distance of all the
    // vertices from the source vertex S.
    public static int[] dijkstra(int V, ArrayList<ArrayList<AdjListNode> > graph, int source)
    {
    	/*
    	 * The time complexity of the above code/algorithm looks O(V^2) as there are two nested while loops. 
    	 * If we take a closer look, we can observe that the statements in inner loop are executed O(V+E) times (similar to BFS)
    	 * . The inner loop has decreaseKey() operation which takes O(LogV) time. 
    	 * So overall time complexity is O(E+V)*O(LogV) which is O((E+V)*LogV) = O(ELogV) 
    	 * V : no of vertices
    	 * E : number of edges in the graph.
    	 * */
        int[] distance = new int[V];
        for (int i = 0; i < V; i++)
            distance[i] = Integer.MAX_VALUE;
        Arrays.fill(distance, 0);
        
        // implementing a min heap using priority queue to store the vertices 
        PriorityQueue<AdjListNode> pq = new PriorityQueue<>((v1, v2) -> v1.getWeight() - v2.getWeight()); 
        pq.add(new AdjListNode(source, 0));
 
        while (pq.size() > 0) {
            AdjListNode current = pq.poll();
 
            for (AdjListNode n :graph.get(current.getVertex())) {
            	// applying relaxation technique and checking if we can find a lesser distance
                if (distance[current.getVertex()] + n.getWeight() < distance[n.getVertex()]) {
                	// updating distance if found min
                    distance[n.getVertex()] = n.getWeight() + distance[current.getVertex()];
                    
                    pq.add(new AdjListNode( n.getVertex(), distance[n.getVertex()]));
                }
            }
        }
        // If you want to calculate distance from source to
        // a particular target, you can return
        // distance[target]
        return distance;
    }
	
	public static void main(String[] args) {
		int V = 9;
        ArrayList<ArrayList<AdjListNode> > graph
            = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            graph.add(new ArrayList<>());
        }
        int source = 0;
        graph.get(0).add(new AdjListNode(1, 4));
        graph.get(0).add(new AdjListNode(7, 8));
        graph.get(1).add(new AdjListNode(2, 8));
        graph.get(1).add(new AdjListNode(7, 11));
        graph.get(1).add(new AdjListNode(0, 7));
        graph.get(2).add(new AdjListNode(1, 8));
        graph.get(2).add(new AdjListNode(3, 7));
        graph.get(2).add(new AdjListNode(8, 2));
        graph.get(2).add(new AdjListNode(5, 4));
        graph.get(3).add(new AdjListNode(2, 7));
        graph.get(3).add(new AdjListNode(4, 9));
        graph.get(3).add(new AdjListNode(5, 14));
        graph.get(4).add(new AdjListNode(3, 9));
        graph.get(4).add(new AdjListNode(5, 10));
        graph.get(5).add(new AdjListNode(4, 10));
        graph.get(5).add(new AdjListNode(6, 2));
        graph.get(6).add(new AdjListNode(5, 2));
        graph.get(6).add(new AdjListNode(7, 1));
        graph.get(6).add(new AdjListNode(8, 6));
        graph.get(7).add(new AdjListNode(0, 8));
        graph.get(7).add(new AdjListNode(1, 11));
        graph.get(7).add(new AdjListNode(6, 1));
        graph.get(7).add(new AdjListNode(8, 7));
        graph.get(8).add(new AdjListNode(2, 2));
        graph.get(8).add(new AdjListNode(6, 6));
        graph.get(8).add(new AdjListNode(7, 1));
 
        int[] distance = dijkstra(V, graph, source);
        // Printing the Output
        System.out.println("Vertex  "
                           + "  Distance from Source");
        for (int i = 0; i < V; i++) {
            System.out.println(i + "             "
                               + distance[i]);
        }
	}

}
