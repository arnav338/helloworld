package graph;

public class PrimMST {
	public static void main(String[] args) {
		/*
		 * It starts with an empty spanning tree. The idea is to maintain two sets of vertices. 
		 * The first set contains the vertices already included in the MST,
		 * the other set contains the vertices not yet included. 
		 * At every step, it considers all the edges that connect the two sets, 
		 * and picks the minimum weight edge from these edges. 
		 * After picking the edge, it moves the other endpoint of the edge to the set 
		 * containing MST. 
		 * Algorithm 
			1) Create a set mstSet that keeps track of vertices already included in MST. 
			2) Assign a key value to all vertices in the input graph. Initialize all key values as INFINITE. 
			Assign key value as 0 for the first vertex so that it is picked first. 
			3) While mstSet doesn’t include all vertices 
				….a) Pick a vertex u which is not there in mstSet and has minimum key value. 
				….b) Include u to mstSet. 
				….c) Update key value of all adjacent vertices of u. To update the key values, iterate through all adjacent vertices. 
				For every adjacent vertex v, if weight of edge u-v is less than the previous key value of v, 
				update the key value as weight of u-v. The idea of using key values is to pick the minimum weight edge from cut. 
				The key values are used only for vertices which are not yet included in MST,
				 the key value for these vertices indicate the minimum weight edges connecting them to the set of vertices included in MST.
				 
			We use a boolean array mstSet[] to represent the set of vertices included in MST. 
			If a value mstSet[v] is true, then vertex v is included in MST, otherwise not. 
			Array key[] is used to store key values of all vertices. Another array parent[] to store indexes of parent nodes in MST. 
			The parent array is the output array which is used to show the constructed MST. 	  
		 * */
		/* Let us create the following graph
        2 3
        (0)--(1)--(2)
        | / \ |
        6| 8/ \5 |7
        | /     \ |
        (3)-------(4)
            9         */
        int graph[][] = new int[][] { { 0, 2, 0, 6, 0 },
                                      { 2, 0, 3, 8, 5 },
                                      { 0, 3, 0, 0, 7 },
                                      { 6, 8, 0, 0, 9 },
                                      { 0, 5, 7, 9, 0 } };
 
        // Print the solution
        primMST(graph);
	}
	
	private static final int V = 5;
	
	static void primMST(int graph[][])
    {
        // Array to store constructed MST
        int parent[] = new int[V];
 
        // Key values used to pick minimum weight edge in cut
        int key[] = new int[V];
 
        // To represent set of vertices included in MST
        Boolean mstSet[] = new Boolean[V];
 
        // Initialize all keys as INFINITE
        for (int i = 0; i < V; i++) {
            key[i] = Integer.MAX_VALUE;
            mstSet[i] = false;
        }
 
        // Always include first 1st vertex in MST.
        key[0] = 0; // Make key 0 so that this vertex is picked as first vertex
        parent[0] = -1; // First node is always root of MST
 
        // The MST will have V vertices
        for (int count = 0; count < V - 1; count++) {
            // Pick the minimum key vertex from the set of vertices not yet included in MST
            int u = minKey(key, mstSet);
 
            // Add the picked vertex to the MST Set
            mstSet[u] = true;
 
            // Update key value and parent index of the adjacent
            // vertices of the picked vertex. Consider only those
            // vertices which are not yet included in MST
            for (int v = 0; v < V; v++)
 
                // graph[u][v] is non zero only for adjacent vertices of m
                // mstSet[v] is false for vertices not yet included in MST
                // Update the key only if graph[u][v] is smaller than key[v]
                if (graph[u][v] != 0 && mstSet[v] == false && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
        }
        
        // print the constructed MST
        //printMST(parent, graph);
    }
    // A utility function to find the vertex with minimum key value, from the set of vertices not yet included in MST
    static int minKey(int key[], Boolean mstSet[])
    {
        // Initialize min value
        int min = Integer.MAX_VALUE; 
        int min_index = -1;
        
        for (int v = 0; v < V; v++)
            if (mstSet[v] == false && key[v] < min) {
                min = key[v];
                min_index = v;
            }
        return min_index;
    }
}
