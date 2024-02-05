package arrays;

import java.util.ArrayList;
import java.util.Collections;

public class CountTriplets {

	public static void main(String[] args) {
		/*
		 * 
		 * Given an array of distinct integers. The task is to count all the triplets
		 * such that sum of two elements equals the third element.
		 * 
		 * Example 1:
		 * 
		 * Input: N = 4 arr[] = {1, 5, 3, 2} 
		 * Output: 2 
		 * Explanation: There are 2
		 * triplets: 1 + 2 = 3 and 3 +2 = 5 
		 * 
		 * Example 2:
		 * 
		 * Input: 
		 * N = 3 
		 * arr[] = {2, 3, 4} 
		 * Output: 0 
		 * Explanation: No such triplet exits
		 * 
		 * Expected Time Complexity: O(N^2) Expected Auxiliary Space: O(1)
		 * 
		 */
		
		int[] arr = new int[] {1, 5, 3, 2};
		System.out.println(twoPointer(arr)+" pairs exist");
	}

	private static int twoPointer(int[] arr) {
		int count=0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int a : arr) { // add all elements to list
			list.add(a);
		}
		Collections.sort(list);
		list.stream().forEach(System.out::print);
		System.out.println("=====");
		for(int i=arr.length-1;i>=0;i--) {
			int left = 0;
			int right = arr.length-1;
			while(left<right) {
				if(list.get(left)+list.get(right)==list.get(i)) {
					count++;
					// to check if more than one triplets exist for the current element
					left++;
					right--;
				}
				else if(list.get(left)+list.get(right)>list.get(i)) {
					right--;
				}
				else {
					left++;
				}
			}
		}
		return count;
	}

}
