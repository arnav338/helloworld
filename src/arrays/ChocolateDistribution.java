package arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ChocolateDistribution {
	public static void main(String[] args) {
		/*
		 * Given an array A[ ] of positive integers of size N, where each value
		 * represents the number of chocolates in a packet. Each packet can have a
		 * variable number of chocolates. There are M students, the task is to
		 * distribute chocolate packets among M students such that : 1. Each student
		 * gets exactly one packet. 2. The difference between maximum number of
		 * chocolates given to a student and minimum number of chocolates given to a
		 * student is minimum.
		 * 
		 * Example 1:
		 * 
		 * Input: 
		 * N = 8, 
		 * M = 5 
		 * A = {3, 4, 1, 9, 56, 7, 9, 12} 
		 * Output: 6 
		 * Explanation: The minimum difference between maximum chocolates and 
		 * minimum chocolates is 9 - 3 = 6 by choosing following M packets : {3, 4, 9, 7, 9}.
		 *An efficient solution is based on the observation that to minimize the difference, 
		 *we must choose consecutive elements from a sorted packet. We first sort the array arr[0..n-1], 
		 *then find the subarray of size m with the minimum difference between the last and first elements.
		 */
		ArrayList<Integer> a = new ArrayList<>();
		a.add(3);
		a.add(4);
		a.add(1);
		a.add(9);
		a.add(56);
		a.add(7);
		a.add(9);
		a.add(12);
		int m = 5; // no of chocolates
		System.out.println("Min diff :: "+findMinDiff(a,a.size(),m));
	}

	private static long findMinDiff(ArrayList<Integer> a, int length, int m) {
		// time complexity - O(n * log N) - as sorting takes O(N* log N) time and only 1 traversal is needed, i.e., O(N)
		int min = Integer.MAX_VALUE;
		// {1, 3, 4, 7, 9, 9, 12, 56, 9} 
        int start = 0, end = (int) m-1; // end represents window size which will be equal to no of chocolates
        Collections.sort(a);
        while(end<length){
        	int curr = a.get(end)- a.get(start);
            min = Math.min(min, curr);
            end++;
            start++;
        }
        return (long) min;
	}
}
