package leetCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaxProfitTrader {
	/*
	 * In share trading, a buyer buys shares and sells on a future date. Given the
	 * stock price of n days, the trader is allowed to make at most k transactions,
	 * where a new transaction can only start after the previous transaction is
	 * complete, find out the maximum profit that a share trader could have made.
	 * 
	 * Input: Price = [10, 22, 5, 75, 65, 80] 
	 * K = 2 
	 * Output: 87 
	 * Trader earns 87 as
	 * sum of 12 and 75 
	 * Buy at price 10, sell at 22, 
	 * buy at 5 and sell at 80
	 */
	public static void main(String[] args) {
		int[] arr = new int[] { 10, 22, 5, 75, 65, 80 };
		int K = 2;
		System.out.println(findMaxProfit(arr,K));
	}

	private static int findMaxProfit(int[] arr, int k) {
		int profit =0;
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < arr.length; i++) {
			list.add(arr[i]);
		}
		
		return profit;
	}

}
