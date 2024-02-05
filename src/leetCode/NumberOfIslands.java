package leetCode;

public class NumberOfIslands {
	/*
	 * Given an m x n 2D binary grid grid which represents a map of 
	 * '1's (land) and '0's (water), return the number of islands.
	 * An island is surrounded by water and is formed by connecting adjacent lands 
	 * horizontally or vertically. 
	 * You may assume all four edges of the grid are all surrounded by water.
		Example 1:

			Input: grid = [
  								["1","1","1","1","0"],
  								["1","1","0","1","0"],
  								["1","1","0","0","0"],
  								["0","0","0","0","0"]
							]
		Output: 1
		
		Example 2:

			Input: grid = [
  								["1","1","0","0","0"],
  								["1","1","0","0","0"],
  								["0","0","1","0","0"],
  								["0","0","0","1","1"]
							]
			Output: 3
	 * */
	public static void main(String[] args) {
		/*char[][] arr = {
							{'1','1','1','1','0'},
							{'1','1','0','1','0'},
							{'1','1','0','0','0'},
							{'0','0','0','0','0'}
				};*/
		char[][] arr = {
				{'1','1','0','0','0'},
				{'1','1','0','0','0'},
				{'0','0','1','0','0'},
				{'0','0','0','1','1'}
		};
		//System.out.println("Found "+countIslands(arr)+" islands");
		countIslands(arr);
		System.out.println(islands+" islands found");
	}
	static int islands = 0;
	static int noOfIslands = 0;
	static int r = 4;
	static int c = 5;
	static boolean[][] visited;
	private static void countIslands(char[][] grid) {
		// avg time complexity will be O(ROW x COL)
		visited = new boolean[r][c];
		for (int f = 0; f < r; f++) {
			for (int d = 0; d < c; d++) {
				//System.out.println("======= "+noOfIslands);
				//System.out.println("-> f: "+f+" d : "+d);
				count(grid,f,d);
				//System.out.println("+++++++++ "+noOfIslands);
				if(noOfIslands > 0) {
					islands++;
					//System.out.println(islands+" islands for-> f: "+f+" d : "+d);
					noOfIslands = 0;
				}
			}
		}
	}
	private static int count(char[][] arr, int i, int j) {
		if(arr[i][j] == '1' && !visited[i][j]) {
			visited[i][j] = true;
		}
		else return 0;
		if(j+1 <= c-1 && !visited[i][j+1]) { // check right
			count(arr, i, j+1);
		}
		if(i+1 <= r-1 && !visited[i+1][j]) { // check down
			count(arr, i+1, j);
		}
		if(j-1 >= 0 && !visited[i][j-1]) { // check left
			count(arr, i, j-1);
		}
		if(i-1 >= 0 && !visited[i-1][j]) { // check up
			count(arr, i-1, j);
		}
		noOfIslands++;
		return 0;
	}
}
