package arrays;

public class StockBuySell2 {
	public static void main(String[] args) {
		/*
		 * advance prices for a stock are given
		 * find appropriate time to buy and sell
		 * so that profit is max
		 * 
		 * advanced condition is that - we can buy and sell any number of times
		 * 
		 * so here we need to buy at the local minima and sell at local maxima
		 * 
		 * a[i] is local minima if - a[i-1] > a[i] < a[i+1]
		 * a[i] is local maxima if - a[i-1] < a[i] > a[i+1]
		 * 
		 * */
		int[] a = {5,2,6,1,4,7,3,6};
		System.out.println(findMaxProfit2(a));
	}
	
	private static int findMaxProfit2(int[] A) {
		// time complexity - O(n)
		// space - O(1)
		int profit = 0;
		for (int i = 1; i < A.length; i++) {
			if(A[i] > A[i-1]) {
				profit += A[i] - A[i-1];
			}
		}
		return profit;
	}
}
