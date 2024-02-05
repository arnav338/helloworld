package arrays;

import java.util.Arrays;

public class LongestCommonSubsequence {
	static final int maximum = 1000;
	public static void main(String[] args) {
		String X = "AGGTAB";
        String Y = "GXTXAYB";
        System.out.println(lcsUsingRecursion(X, Y, 0, 0));
        int dp[][] = new int[X.length()][maximum];
        // assign -1 to all positions
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }
        System.out.println("Length of LCS: " + lcs(X, Y, X.length(), Y.length(), dp));
	}
	// Returns length of LCS for X[0..m-1], Y[0..n-1]  memoization applied in recursive solution
	// time complexity is O(m*n), m : length of 1 string, n : length of 2nd string
	static int lcs(String X, String Y, int m, int n, int dp[][]) {
	        // base case
	        if (m == 0 || n == 0) {
	            return 0;
	        }
	        // if the same state has already been computed
	        if (dp[m - 1][n - 1] != -1) {
	            return dp[m - 1][n - 1];
	        }
	        // if equal, then we store the value of the function call
	        if (X.charAt(m - 1) == Y.charAt(n - 1)) {
	            // store it in arr to avoid further repetitive work in future function calls
	            dp[m - 1][n - 1] = 1 + lcs(X, Y, m - 1, n - 1, dp);
	            return dp[m - 1][n - 1];
	        } else {
	            // store it in arr to avoid further repetitive work in future function calls
	            dp[m - 1][n - 1] = Math.max(lcs(X, Y, m, n - 1, dp), lcs(X, Y, m - 1, n, dp));
	            return dp[m - 1][n - 1];
	        }
	 }
	
	static int lcsUsingRecursion(String X, String Y, int m, int n) {
        // base case
		// this will take exponential time. complexity - O(2^n)
        if (m > X.length()-1 || n > Y.length()-1) {
            return 0;
        }
        // No storing is being done
        if (X.charAt(m) == Y.charAt(n)) {
        	// adding 1 as current characters match so need to take them into account
            return 1 + lcsUsingRecursion(X, Y, m+1, n+1);
        } 
        else {
            return Math.max(lcsUsingRecursion(X, Y, m+1, n), lcsUsingRecursion(X, Y, m, n+1));
        }
 }
}
