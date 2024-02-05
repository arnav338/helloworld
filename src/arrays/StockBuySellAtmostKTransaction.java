package arrays;

public class StockBuySellAtmostKTransaction {
	public static void main(String[] args) {
		/*
		 * we use memoization and dynamic programming to store all possible values
		 * 
		 * */
		int k = 3;
        int price[] = {12, 14, 17, 10, 14, 13, 12, 15};
 
        int n = price.length;
 
        System.out.println("Maximum profit is: " + maxProfit(price, n, k));
	}
	static int maxProfit(int price[], int n, int k)
		{
		//The time complexity of the solution is O(k*n) and space complexity is O(n*k).
		// table to store results of subproblems profit[t][i] stores maximum profit
		// using atmost t transactions up to day i (including day i)
		int profit[][] = new int[k + 1][ n + 1];
		
		// For day 0, you can't earn money irrespective of how many times you trade
		for (int i = 0; i <= k; i++)
		profit[i][0] = 0;
		
		// profit is 0 if we don't do any transaction (i.e. k =0)
		for (int j = 0; j <= n; j++)
		profit[0][j] = 0;
		
		// fill the table in bottom-up fashion
		for (int i = 1; i <= k; i++)
		{
			int prevDiff = Integer.MIN_VALUE;
			for (int j = 1; j < n; j++)
			{
			   //profit[i - 1][j - 1] refers to the previous diagonal element in table
			   // prevDiff = (profit earned by making 1 transaction in 1st 4 days) – p4 (profit at day 4)
			   prevDiff = Math.max(prevDiff, profit[i - 1][j - 1] - price[j - 1]);
			   
			   
			   profit[i][j] = Math.max(profit[i][j - 1], price[j] + prevDiff);
			}
		}
		return profit[k][n - 1];
		}
}
