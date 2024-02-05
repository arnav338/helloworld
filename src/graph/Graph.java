package graph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Graph {
	
	public static LinkedList<Integer> list[];
	
	@SuppressWarnings("unchecked")
	public Graph(int numberOfVertices) {
		list = new LinkedList[numberOfVertices];
		for (int i = 0; i < numberOfVertices; i++) {
			list[i] = new LinkedList<Integer>();
		}
	}
	
	public void addEdge(int source, int destination){
		list[source].add(destination);
		list[destination].add(source);
	}
	
	public static void main(String[] args) {
		/*
		  * Graphs are represented by adjacency list
		 * 
		 *  1 2
		 * 	 \| 
		 *  0-4
		 *   \/
		 *    3 
		 * */
		
		Graph graph = new Graph(5);
	 	graph.addEdge(0, 3);
		graph.addEdge(0, 4);
		graph.addEdge(4, 3);
		graph.addEdge(1, 4);
		graph.addEdge(2, 4);
		System.out.println(bfs(1,3));
		System.out.println("+++++++");
		boolean visited[] = new boolean[list.length];
		System.out.println(dfs(2,0,visited));
		System.out.println("+++++++");
		boolean visit[] = new boolean[list.length];
		System.out.println(dfsStack(2,0,visit));
	}
	
	private static boolean dfsStack(int source, int destination, boolean[] visit) {
		Stack<Integer> stack = new Stack<>();
		stack.push(source);
		visit[source] = true;
		while(!stack.isEmpty()) {
			int cur = stack.pop();
			
			if(cur==destination) return true;
			
			for(int neighbor : list[cur]) {
				if(!visit[neighbor]) {
					visit[neighbor] = true;
					stack.push(neighbor);
				}
			}
			
		}
		return false;
	}

	private static boolean dfs(int source, int destination, boolean[] visited) {
		// DFS does not guarantee shortest path but guarantees that if there is a path it will return true
		// it is better in memory optimization
		// Time complexity: O(V + E), where V is the number of vertices and E is the number of edges in the graph.
		/*
		 * Depth-first search is an algorithm for traversing or searching tree or graph data structures. 
		 * The algorithm starts at the root node (selecting some arbitrary node as the root node in the case of a graph) 
		 * and explores as far as possible along each branch before backtracking. 
		 * So the basic idea is to start from the root or any arbitrary node and mark the node and 
		 * move to the adjacent unmarked node and continue this loop until there is no unmarked adjacent node. 
		 * Then backtrack and check for other unmarked nodes and traverse them. Finally, print the nodes in the path.
		 * */
		if(source==destination) return true;
		for(int neighbor : list[source]) {
		 	if(!visited[neighbor]) {
				visited[neighbor] = true;
				boolean isConnected = dfs(neighbor, destination, visited);// after checking for source we check for neighbor
				if(isConnected) return true;
			}
		}
		return false;
	}
	
	

	public static int bfs(int source, int destination) {
		// Use of BFS is that it can tell the shortest path between 2 nodes
		// But BFS consumes lot of memory
		// Time Complexity: O(V+E) where V is a number of vertices in the graph and E is a number of edges in the graph.
		boolean visited[] = new boolean[list.length]; // to avoid checking elements more than once
		int parent[] = new int[list.length];
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(source);
		visited[source]=true;
		parent[source] = -1;
		System.out.println("BFS traversal starts");
		while(!q.isEmpty()) {
			int current = q.poll();
			System.out.println("==== "+current);
			LinkedList<Integer> neighbors = list[current];
			for(int neighbor : neighbors) {
				if(!visited[neighbor]) {
					visited[neighbor] = true;
					// adding current element to queue
					q.add(neighbor);
					
					// parent of node whose value is i will be parent[i]
					// means parent of 3 will be parent[3]
					// means we visited 3 after we visited parent[3]
					parent[neighbor] = current;
				}
			}
		}
		int cur = destination;
		int distance=0;
		while(parent[cur]!=-1) {
			cur = parent[cur];
			distance++;
		}
		return distance;
	}
	
}
