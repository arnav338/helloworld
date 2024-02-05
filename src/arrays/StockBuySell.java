package arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class StockBuySell {
	public static void main(String[] args) {
		/*
		 * advance prices for a stock are given
		 * find appropriate time to buy and sell
		 * so that profit is max
		 * */
		//int[] a = {100,180,260,310,40,535,695};
		//int[] a = {11,42,49,96,23,20,49,26,26,18,73,2,53,59,34,99,25,2};
		int[] a = {3,1,4,8,7,2,5};
		System.out.println(findMaxProfit2(a));
	}
	
	private static int findMaxProfit2(int[] A) {
		int min=Integer.MAX_VALUE;
		int max=Integer.MIN_VALUE;
		int temp_profit=0;
		int max_profit=0;
		for (int i = 0; i < A.length; i++) {
			if(A[i]<min) {
				if(max>min) {
					max_profit += temp_profit;
					temp_profit = 0;
					max = A[i];
				}
				min = A[i]; // buy stock here
			}
			if(A[i]>max) {
				max = A[i];
			}
			if(max - min > temp_profit) {
				System.out.println("--"+A[i]);
				temp_profit = max-min;
				System.out.println("max profit : "+temp_profit+" || Buy: "+min+"|| Sell : "+max);
			}
		}
		return max_profit+temp_profit;
	}
	
	private static int findMaxProfit1(int[] A) {
		// space and time - O(n)
		// Valley peak approach
		int[] temp = new int[A.length];
		int max = Integer.MIN_VALUE;
		int maxProfit=0;
		for (int i = A.length-1; i >= 0; i--) {
			max = Math.max(A[i], max); // we iterate from last element and keep track of highest price for all elements
			temp[i] = max;
		}
		for (int i = 0; i < A.length; i++) {
			int curr = temp[i]-A[i]; // temp represents highest price ahead from this element
			maxProfit = Math.max(curr, maxProfit);
		}
		return maxProfit;
	}
	
	private static int findMaxProfit(int[] A) {
		int min=Integer.MAX_VALUE;
		int max=Integer.MIN_VALUE;
		int min_index=0;
		int max_index=0;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		int temp_profit=0;
		int max_profit=0;
		ArrayList<ArrayList<Integer>> res = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < A.length; i++) {
			if(A[i]<min) {
				if(max>min) {
					max_profit += temp_profit;
					temp_profit = 0;
					ArrayList<Integer> t = new ArrayList<Integer>();
					t.add(max_index);
					t.add(min_index);
					Collections.sort(t);
					System.out.println("-->"+t);
					res.add(t);
					max = A[i];
				}
				min = A[i];
				min_index = i;
			}
			if(A[i]>max) {
				max = A[i];
				max_index = i;
			}
			if(max - min > temp_profit) {
				System.out.println("--"+A[i]);
				temp_profit = max-min;
				temp.clear();
				temp.add(min_index);
				temp.add(max_index);
				Collections.sort(temp);
				System.out.println("max profit : "+temp_profit+" || Buy: "+min+"|| Sell : "+max);
			}
		}
		res.add(temp);
		res.stream().forEach(System.out::println);
		return max_profit+temp_profit;
	}
}
