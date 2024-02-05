package arrays;

import java.util.ArrayList;

public class ClosestPairFromSortedArray {
	/*
	 * Given two sorted arrays and a number x, find the pair whose sum is closest to
	 * x and the pair has an element from each array. We are given two arrays
	 * ar1[0…m-1] and ar2[0..n-1] and a number x, we need to find the pair ar1[i] +
	 * ar2[j] such that absolute value of (ar1[i] + ar2[j] – x) is minimum. Example:
	 * 
	 * Input: 
	 * ar1[] = {1, 4, 5, 7}; 
	 * ar2[] = {10, 20, 30, 40}; 
	 * x = 32 
	 * Output: 1 and 30
	 * 
	 * Input: 
	 * ar1[] = {1, 4, 5, 7}; 
	 * ar2[] = {10, 20, 30, 40}; 
	 * x = 50 
	 * Output: 7 and 40
	 */
	public static void main(String[] args) {
		int[] a1 = {1, 4, 5, 7}; 
		int[] a2 = {10, 20, 30, 40}; 
		int k = 32;
		ArrayList<Integer> res = findPair(a1,a2,k);
		res.stream().forEach(System.out::println);
	}
	
	private static ArrayList<Integer> findPair(int[] a1, int[] a2, int k) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		int i = 0;
		int j = 0;
		if(a1[i]+a2[j] >= k) { // if sum of 1st 2 elements exceeds k, return from here only
			res.add(a1[i]);
			res.add(a2[j]);
			return res;
		}
		while(i < a1.length && j < a2.length) {
			int max = Math.max(a1[i+1], a2[j+1]);
			if(a1[i+1]+a2[j] > k || a1[i]+a2[j+1] > k) { // if choosing any of the next element exceeds k, return from here only
				res.add(a1[i]);
				res.add(a2[j]);
				return res;
			}
			if(a1[i+1] > a2[j+1]) {
				System.out.println(a1[i+1]+" is greater than "+a2[j+1]);
				i++;
			}
			else {
				System.out.println(a2[j+1]+" is greater than "+a1[i+1]);
				j++;
			}
		}
		return null;
	}
}
