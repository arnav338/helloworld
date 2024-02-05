package dynamic;

import java.util.Arrays;

public class MinCoins {
	public static void main(String[] args) {
		// coin value to be achieved
		int n = 18;
		// available coins
		int a[] = { 5, 7, 1 };
		// initializing array for memoization
		int[] dp = new int[n + 1];
		Arrays.fill(dp, -1);
		dp[0] = 0;
		System.out.println(minCoins(a, n, dp));
		for (int i = 0; i < dp.length; i++) {
			System.out.print(dp[i]+"||");
		}
	}
	// time complexity is O(m*n)
	// m is no. of coins available (as we have the for loop running for all coins) 
	// n is the value to be achieved (n=18)
	// space complexity : O(n)
	private static int minCoins(int[] a, int target, int[] dp) {
		if(target==0) return 0;
		int res = Integer.MAX_VALUE;
		for(int i = 0 ; i< a.length; i++) {
			int temp=0;
			if(target-a[i]>=0) { // subtracting a[i] as we are using coin at index i
				if(dp[target-a[i]]!=-1) {
						temp = dp[target-a[i]]; // using stored value
				}
				else {
					temp = minCoins(a, target-a[i], dp); // changing target value
				}
				if(temp+1 < res) { // adding 1 to count the current coin
					res = temp+1;
				}
			}
		}
		// saving value of min coins reqd for diff values
		// eg : value of min coins reqd to make 6
		dp[target] = res;
		return res;
	}

}
