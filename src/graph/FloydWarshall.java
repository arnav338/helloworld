package graph;

public class FloydWarshall {
	
	static final int high = 9999999; 

	public static void main(String[] args) {
		/*
		 * There are n cities numbered from 0 to n-1. Given the array edges where
		 * edges[i] = [fromi, toi, weighti] represents a bidirectional and weighted edge
		 * between cities fromi and toi, and given the integer distanceThreshold.
		 * 
		 * Return the city with the smallest number of cities that are reachable through
		 * some path and whose distance is at most distanceThreshold, If there are
		 * multiple such cities, return the city with the greatest number.
		 * 
		 * Notice that the distance of a path connecting cities i and j is equal to the
		 * sum of the edges' weights along that path.
		 * Example 1:
		 * 
		 * Input: 
		 * n = 4, 
		 * edges = [[0,1,3],[1,2,1],[1,3,4],[2,3,1]], 
		 * distanceThreshold =4 
		 * Output: 3 
		 * Explanation: The figure above describes the graph. The neighboring cities at a distanceThreshold = 4 for each city are: 
		 * City 0 -> [City 1, City 2] 
		 * City 1 -> [City 0, City 2, City 3] 
		 * City 2 -> [City 0, City 1, City 3] 
		 * City 3 -> [City 1, City 2] 
		 * Cities 0 and 3 have 2 neighboring cities at a distanceThreshold = 4, but we have to return city 3 since it has
		 * the greatest number.
		 * 
		 * It can be solved using Floyd Warshall algo in time O(n^3)
		 * and O(n^2) space
		 */
		int graph[][] = { 	{0,  3,  high, high},
                			{3,    0,   1,    4},
                			{high,   1, 0,   1},
                			{high,   4,    1, 0}
              };
		floydWarshall(graph,4,4);
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				if(graph[i][j] == high || graph[i][j] == -high) {
					System.out.print(0+" || ");
				}
				else {
					System.out.print(graph[i][j]+" || ");
				}
			}
			System.out.println();
		}
	}

	private static void floydWarshall(int[][] graph, int r, int c) {
		if(graph == null) return;
		for (int i = 0; i < 4; i++) { // i is the pivot
			for (int j = 0; j < 4; j++) { // j- source
				for (int k = 0; k < 4; k++) { // k - destination 
					if((graph[i][k] != high || graph[j][i] != high)) { // j=1 k =2 
						int curr = graph[j][i] + graph[i][k];
						if(curr < graph[j][k]) {
							System.out.println((j+1)+" to "+(k+1)+" || updated : "+curr+" || original :"+graph[j][k]);
							graph[j][k] = curr;
						}
					}
				}
			}
		}
		
	}

}
