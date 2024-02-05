package arrays;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class LeadersInArray {
	public static void main(String[] args) {
		/*
		 * Given an array A of positive integers. Your task is to find the leaders in
		 * the array. An element of array is leader if it is greater than or equal to
		 * all the elements to its right side. The rightmost element is always a leader.
		 * 
		 * Example 1:
		 * 
		 * Input: 
		 * n = 6 
		 * A[] = {16,17,4,3,5,2} 
		 * Output: 17 5 2 
		 * Explanation: The first leader is 17 as it is greater than all the elements to its right. 
		 * Similarly,the next leader is 5. The right most element is always a leader so it is also
		 * included.
		 */
		int arr[] = {16,17,4,3,5,2};
		ArrayList<Integer> result = new ArrayList<Integer>();
		addLeaders(arr,result );
		result.stream().forEach(System.out::println);
	}
	// time complexity - O(n)
	private static void addLeaders(int[] arr, ArrayList<Integer> result) {
        int max[] = new int[arr.length];
        max[arr.length-1] = 0;
        int k = 0;
        for(int j = arr.length-2; j >= 0; j--){  
        	// we start from the rightmost element and keep track 
        	// of the max element found for every element at its right
            k = Math.max(arr[j+1],k);
            max[j] = k;
        }
        for(int i=0; i < arr.length; i++){
            if(arr[i] > max[i]){
                result.add(arr[i]);
            }
        }
	}
}