package graph;

import java.util.ArrayList;
import java.util.Arrays;

public class CycleDetection {
	/*
	 * The aim is to find if there exists a cycle in the graph or not
	 * for eg : 
		 * 0 - 1 - 2      3 - 6
		 * 	   |   |
		 * 	   5 - 4	 
		  A cycle exists around 1-2-4-5
	 * */
	public static void main(String[] args) {
		int size = 7;
		ArrayList<ArrayList<Integer>> multiComponentGraph = new ArrayList<>(size);
		multiComponentGraph.add(0, new ArrayList<>(Arrays.asList(1))); // 0 is connected to 1 only
		multiComponentGraph.add(1, new ArrayList<>(Arrays.asList(0,2,5))); // 1 is connected to 0,2,5 only
		multiComponentGraph.add(2, new ArrayList<>(Arrays.asList(1,4))); // 2 is connected to 1,4 only
		multiComponentGraph.add(3, new ArrayList<>(Arrays.asList(6))); // 3 is connected to 6 only
		multiComponentGraph.add(4, new ArrayList<>(Arrays.asList(2,5))); // 4 is connected to 2,5 only
		multiComponentGraph.add(5, new ArrayList<>(Arrays.asList(1,4))); // 5 is connected to 1,4 only
		multiComponentGraph.add(6, new ArrayList<>(Arrays.asList(3))); // 6 is connected to 3 only
		boolean cycle = doesCycleExist(multiComponentGraph);
		System.out.println("Does cycle exist ? "+cycle);
	}

	private static boolean doesCycleExist(ArrayList<ArrayList<Integer>> multiComponentGraph) {
		int source = 0;
		int parent = 0;
		boolean[] visited = new boolean[multiComponentGraph.size()];
		boolean res = dfs(multiComponentGraph,visited,source,parent);
		return res;
	}
	
	private static boolean dfs(ArrayList<ArrayList<Integer>> multiComponentGraph, boolean[] visited, 
								int source, int parent) {
		if(!visited[source]) { // if not visited
			visited[source] = true;// mark it as visited
			for(Integer neighbor : multiComponentGraph.get(source)) { // fetch neighbors of current node
				if(visited[neighbor] && neighbor!=parent) {
					return true;
				}
				if(dfs(multiComponentGraph, visited, neighbor, source)) {
					return true;
				}
			}
		}
		return false;
	}
}