package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFS {

	public static void main(String[] args) {
		/*
		 * Task is to traverse a single component and multi component graph using BFS
		 * input will be an adjacency list
		 * 
		 * Single component graph : 
		 * 
		 * 				3
		 * 			    | 
		 * 		    0 - 1 - 2 - 4
		 * 					| 
		 * 					5 - 6 
		 *   				|
		 *   				7
		 *   
		 *   BFS traversal will be 0,1,3,2,4,5,6,7
		 * 
		 * */
		ArrayList<ArrayList<Integer>> singleComponentGraph = new ArrayList<>(8);
		singleComponentGraph.add(0, new ArrayList<>(Arrays.asList(1))); // 0 is connected to 1 only
		singleComponentGraph.add(1, new ArrayList<>(Arrays.asList(0,3,2))); // 1 is connected to 0,3,2 only
		singleComponentGraph.add(2, new ArrayList<>(Arrays.asList(1,5,4))); // 2 is connected to 1,5,4 only
		singleComponentGraph.add(3, new ArrayList<>(Arrays.asList(1))); // 3 is connected to 1 only
		singleComponentGraph.add(4, new ArrayList<>(Arrays.asList(2))); // 4 is connected to 2 only
		singleComponentGraph.add(5, new ArrayList<>(Arrays.asList(2,6,7))); // 5 is connected to 2,6,7 only
		singleComponentGraph.add(6, new ArrayList<>(Arrays.asList(5))); // 6 is connected to 5 only
		singleComponentGraph.add(7, new ArrayList<>(Arrays.asList(5))); // 7 is connected to 5 only
		ArrayList<Integer> result = traverse(singleComponentGraph);
		result.stream().forEach(System.out::println);
		
		/*  Multi component graph : 
			 *   
			 *   0 - 1 - 2		5 - 6
			 *   	 |   |
			 *       3 - 4
			 *   
			 * 
			 * */
		ArrayList<ArrayList<Integer>> multiComponentGraph = new ArrayList<>(7);
		multiComponentGraph.add(0, new ArrayList<>(Arrays.asList(1))); // 0 is connected to 1 only
		multiComponentGraph.add(1, new ArrayList<>(Arrays.asList(0,2,3))); // 1 is connected to 0,2,3 only
		multiComponentGraph.add(2, new ArrayList<>(Arrays.asList(4))); // 2 is connected to 4 only
		multiComponentGraph.add(3, new ArrayList<>(Arrays.asList(1,4))); // 3 is connected to 1,4 only
		multiComponentGraph.add(4, new ArrayList<>(Arrays.asList(2,3))); // 4 is connected to 2,3 only
		multiComponentGraph.add(5, new ArrayList<>(Arrays.asList(6))); // 5 is connected to 6 only
		multiComponentGraph.add(6, new ArrayList<>(Arrays.asList(5))); // 6 is connected to 5 only
//		ArrayList<Integer> res = traverse(multiComponentGraph);
//		res.stream().forEach(System.out::println);
		
	}

	private static ArrayList<Integer> traverse(ArrayList<ArrayList<Integer>> graph) {
		boolean[] visited = new boolean[graph.size()];
		Queue<Integer> q = new LinkedList<Integer>();
		ArrayList<Integer> result = new ArrayList<Integer>();
		// take out first element and add its neighbors to the queue
		for(int i=0; i<graph.size();i++) {
			// the exterior for loop is for the case when we have a disconnected graph
			// if its a single component graph/connected graph, we dont need the outer loop
			if(!visited[i]) {
				q.add(i);
				while(!q.isEmpty()) {
					int c = q.poll();	
					if(!visited[c]) {
							System.out.println("Encountered : "+c);
							result.add(c);
							visited[c] = true;
							ArrayList<Integer> adjacentNodes = graph.get(c);
							adjacentNodes.stream().forEach(e -> q.add(e));// adding all adjacent nodes of this node into the queue
					}
				}
			}
		}
		return result;
	}
}