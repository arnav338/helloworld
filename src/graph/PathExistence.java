package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathExistence {
	/*
 		Find if Path Exists in Graph

		There is a bi-directional graph with n vertices, where each vertex is labeled from 0 to n - 1 (inclusive). The edges in the graph are represented as a 2D integer array edges, where each edges[i] = [ui, vi] denotes a bi-directional edge between vertex ui and vertex vi. Every vertex pair is connected by at most one edge, and no vertex has an edge to itself.
		
		You want to determine if there is a valid path that exists from vertex source to vertex destination.
		
		Given edges and the integers n, source, and destination, return true if there is a valid path from source to destination, or false otherwise.
		
		 
		
		Example 1:
		
		
		Input: n = 3, edges = [[0,1],[1,2],[2,0]], source = 0, destination = 2
		Output: true
		Explanation: There are two paths from vertex 0 to vertex 2:
		- 0 - 1 - 2
		- 0 - 2
		Example 2:
		
		
		Input: n = 6, edges = [[0,1],[0,2],[3,5],[5,4],[4,3]], source = 0, destination = 5
		Output: false
		Explanation: There is no path from vertex 0 to vertex 5.
	 * 
	 * */
	public static void main(String[] args) {
		int[][] edges = new int[][] {
			{0,1},
			{0,2},
			{3,5},
			{5,4},
			{4,3}
		};
		boolean res = validPath(6, edges, 0, 5);
//		int[][] edges = new int[][] {
//			{0,1},
//			{1,2},
//			{2,0}
//		};
//		boolean res = validPath(3, edges, 0, 2);
		System.out.println("-> "+res);
	}
	public static boolean validPath(int n, int[][] edges, int source, int destination) {
		if(edges.length == 0 && source==destination) return true;
        
        Map<Integer,List<Integer>> map = new HashMap<>();
        for(int i=0; i<edges.length; i++){
            if(map.get(edges[i][0])==null) map.put(edges[i][0],new ArrayList<Integer>());
            if(map.get(edges[i][1])==null) map.put(edges[i][1],new ArrayList<Integer>());
            map.get(edges[i][0]).add(edges[i][1]);
            map.get(edges[i][1]).add(edges[i][0]);
        }

        int currentPos = destination;
        boolean[] visited = new boolean[n];
        boolean pathExists = traverse(currentPos,map,visited,source);
        
        return pathExists;
    }
    public static boolean traverse(int currentPos,Map<Integer,List<Integer>> map,boolean[] visited,int source){
    	// This is the DFS implementation as we are going through all the neighbors of the current node in one for loop itself
        if(currentPos == source) return true;
    	if(visited[currentPos]) return false;
        visited[currentPos] = true;
        for(int pos : map.get(currentPos)) {
        	if(traverse(pos,map,visited,source)) {
        		return true;
        	}
        }
        return false;
    }
}
