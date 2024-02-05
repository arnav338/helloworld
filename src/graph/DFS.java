package graph;

import java.util.ArrayList;
import java.util.Arrays;

public class DFS {
	/*
		 * Task is to traverse a single component and multi component graph using DFS
		 * input will be an adjacency list
		 * 
		 * Single component graph : 
		 * 
		 * 				1 - 2 - 4 
		 * 					|   |
		 * 					7 - 6
		 *   
		 *   DFS traversal will be 1,2,4,6,7 or 1,2,7,6,4
		 *   Had this been a BFS traversal the result would have been 1,2,4,7,6
		 * 
	 * */
	public static void main(String[] args) {
		/*
		 * 0 - 1 - 2      3 - 6
		 * 	   |   |
		 * 	   5 - 4	
		 * 
		 * */
		ArrayList<ArrayList<Integer>> multiComponentGraph = new ArrayList<>(8);
		multiComponentGraph.add(0, new ArrayList<>(Arrays.asList(1))); // 0 is connected to 1 only
		multiComponentGraph.add(1, new ArrayList<>(Arrays.asList(0,2,4))); // 1 is connected to 0,2,4 only
		multiComponentGraph.add(2, new ArrayList<>(Arrays.asList(4))); // 2 is connected to 4 only
		multiComponentGraph.add(3, new ArrayList<>(Arrays.asList(6))); // 3 is connected to 6 only
		multiComponentGraph.add(4, new ArrayList<>(Arrays.asList(2,5))); // 4 is connected to 2,5 only
		multiComponentGraph.add(5, new ArrayList<>(Arrays.asList(1,4))); // 5 is connected to 1,4 only
		multiComponentGraph.add(6, new ArrayList<>(Arrays.asList(3))); // 6 is connected to 3 only
		ArrayList<Integer> result = new ArrayList<Integer>();
		boolean[] visited = new boolean[7];
		dfs(multiComponentGraph,result,visited);
		result.stream().forEach(System.out::println);
	}

	private static void dfs(ArrayList<ArrayList<Integer>> multiComponentGraph, ArrayList<Integer> result,boolean[] visited) {
		for(int i = 0; i<multiComponentGraph.size(); i++) {
			// this for loop is there to ensure that DFS traversal is done for each node
			// assuming the graph has multiple nodes (some nodes may be disconnected too)
			// it works for single component graph too
			traverse(i, multiComponentGraph, result, visited);
		}
	}

	private static void traverse(int start, ArrayList<ArrayList<Integer>> multiComponentGraph, ArrayList<Integer> result, boolean[] visited) {
		if(!visited[start]) {
			result.add(start);
			visited[start] = true;
			multiComponentGraph.get(start).stream().forEach(e -> traverse(e, multiComponentGraph, result, visited));
		}
	}
}