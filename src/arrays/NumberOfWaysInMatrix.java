package arrays;

import java.util.ArrayList;
import java.util.List;

public class NumberOfWaysInMatrix {
	public static void main(String[] args) {
		/*
		 * Find number of ways we can go from 0,0 to m,n in a M*N matrix
		 * */
		int r = 3;
		int c= 3;
		int DP[][] = new int[r][c];
		for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                DP[i][j] = 0;
            }
        }
		System.out.println(numberOfWays(r,c));
		System.out.println(numberOfWaysDP(r-1,c-1,DP));
		int mat[][] = new int[r][c];
		int d = 1;
		for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                mat[i][j] = d;
                d++;
            }
        }
		List<Integer> list = new ArrayList<Integer>();
		printAllPossiblePaths(mat, r-1, c-1, 0, 0, list);
	}
	private static int numberOfWaysDP(int n, int m, int[][] DP) {
		if (n == 0 || m == 0)
            return DP[n][m] = 1;
 
        // Add the element in the DP table If it is was not computed before
        if (DP[n][m] == 0)
            DP[n][m] = numberOfWaysDP(n - 1, m, DP) + numberOfWaysDP(n, m - 1, DP);
 
        return DP[n][m];
	}
	private static int numberOfWays(int n, int m) {
		if(n==1 || m==1) return 1;
		// If diagonal movements are allowed then this last addition is required -> + numberOfPaths(m-1, n-1);
		return numberOfWays(n-1, m) + numberOfWays(n, m-1);
	}
	 private static void printAllPossiblePaths(int mat[][], int m, int n, int i, int j, List<Integer> list){
		//return if i or j crosses matrix size
		// m,n is destination
		// i,j represents current position
		if(i > m || j > n)
			return;
		list.add(mat[i][j]);
		if(i == m && j == n){
			System.out.println(list); // prints list when destination is reached
		}
		printAllPossiblePaths(mat, m, n, i+1, j, list);
		printAllPossiblePaths(mat, m, n, i, j+1, list);
		list.remove(list.size()-1); // removes last treaded element, sort of backtracking
	 }
}
