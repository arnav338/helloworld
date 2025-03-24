package arrays;

import java.util.ArrayList;
import java.util.List;

public class PrintSubarrayWithGivenSum {

	public static void main(String[] args) {

		/*
		in array [1,2,1], k = 2
		we have to print subsequences with sum 2, i.e., [1,1] and [2]
		* */

		//int[] arr = new int[]{1,2,1};
		int[] arr = new int[]{1,5,4,3,3};
		int k = 6;
		List<Integer> l = new ArrayList<>();
		int sumTillNow = 0;
		int current = 0;
		printSubsequence(arr,k,l,sumTillNow,current);

	}

	private static void printSubsequence(int[] arr, int k, List<Integer> l, int sumTillNow, int current) {
		if(sumTillNow == k){
			System.out.println("subsequence with sum k -> "+l);
		}
		if(current >= arr.length) return;
		l.add(arr[current]);
		printSubsequence(arr,k,l,sumTillNow+arr[current],current+1);
		l.remove((Object) arr[current]);
		printSubsequence(arr,k,l,sumTillNow,current+1);
	}
}
