package arrays;

import java.util.SortedSet;
import java.util.TreeSet;

public class LongestIncreasingSubsequence {
	/*
	 * The Longest Increasing Subsequence (LIS) problem is to 
	 * find the length of the longest subsequence of a given sequence such that 
	 * all elements of the subsequence are sorted in increasing order. 
	 * For example, 
	 * the length of LIS for {10, 22, 9, 33, 21, 50, 41, 60, 80} is 6 
	 * and LIS is {10, 22, 33, 50, 60, 80}. 
	 * Examples: 

		Input: arr[] = {3, 10, 2, 1, 20}
		Output: Length of LIS = 3
		The longest increasing subsequence is 3, 10, 20
		
		Input: arr[] = {3, 2}
		Output: Length of LIS = 1
		The longest increasing subsequences are {3} and {2}
		
		Input: arr[] = {50, 3, 10, 7, 40, 80}
		Output: Length of LIS = 4
		The longest increasing subsequence is {3, 7, 40, 80}
		
		Idea is to create an auxiliary array such that
		each index contains length of longest increasing subsequence 
		that ends at current index
		
		At the end we will find the max of the auxillary array
	 * */
	public static void main(String[] args) {
		
	}
	/* lis() returns the length of the longest
	 * increasing subsequence in arr[] of size n */
 static int lis(int a[], int size)
 { 
	 // time complexity - O(n^2)
	 // space - O(n)
     int res = 0;
     int[] dp = new int[size];
     /* Initialize LIS values for all indexes */
     for (int i = 0; i < size; i++)
      dp[i] = 1;

     /* Compute optimized LIS values in bottom up manner */
     for (int i = 0; i < size; i++){
         int max = 0;
          for (int j = 0; j < i; j++){
           if (a[i] > a[j]){
               if(dp[j] > max){
                   max = dp[j];
               }
           }
          } 
          dp[i] = max+1;
      }

     /* Pick maximum of all LIS values */
     for(int i = 0; i < size; i++){
         res = Math.max(res,dp[i]);
     }  

     return res;
 }
 /* lis() returns the length of the longest
 increasing subsequence in arr[] of size n */
 static int lis1(int arr[], int n)
 {
     SortedSet<Integer> hs = new TreeSet<Integer>();
     // Storing and Sorting unique elements.
     for (int i = 0; i < n; i++)
         hs.add(arr[i]);
     int lis[] = new int[hs.size()];
     
     // Storing all the unique values in a sorted manner.
     int k = 0;
     for (int val : hs) {
         lis[k] = val;
         k++;
     }
     
     int m = k, i, j;
     int dp[][] = new int[m + 1][n + 1];

     // Storing -1 in dp multidimensional array.
     for (i = 0; i < m + 1; i++) {
         for (j = 0; j < n + 1; j++) {
             dp[i][j] = -1;
         }
     }

     // Finding the Longest Common Subsequence of the two
     // arrays
     for (i = 0; i < m + 1; i++) {
         for (j = 0; j < n + 1; j++) {
             if (i == 0 || j == 0) {
                 dp[i][j] = 0;
             }
             else if (arr[j - 1] == lis[i - 1]) {
                 dp[i][j] = 1 + dp[i - 1][j - 1];
             }
             else {
                 dp[i][j]
                     = Math.max(dp[i - 1][j], dp[i][j - 1]);
             }
         }
     }
     return dp[m][n];
 }
}
