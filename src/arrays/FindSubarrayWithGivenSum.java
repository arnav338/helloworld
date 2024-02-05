package arrays;

import java.util.HashMap;

public class FindSubarrayWithGivenSum {

	public static void main(String[] args) {
		/*
		 * we have to find the position of the subarray with given sum
		 * we can use hashmap to store the values and solve it in O(n) time and O(n) space
		 * */
		int[] a = {10,15,-5,15,-10,5};
		//int[] a = {1,1,-1,1,1,-1,-1};
		int sum = 5;
		boolean res = subArraySum(a,sum);
		
		/*
		 * Question can be modified to ask what is the largest subarray with equal no of 1 and 0
		 * {1,1,0,1,1,0,0}
		 * ans -> 1,0,1,1,0,0
		 * convert all 0 to -1 and find subarray with sum as 0
		 * 
		 * */
		equalNoOfZeroAndOne(a);
	}
	private static boolean subArraySum(int[] a, int sum) {
		int start = 0;
		int end = -1;
		int currSum = 0;
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(); // key is the sum, value is the index
		for (int i = 0; i < a.length; i++) {
			currSum += a[i];
			if(currSum - sum == 0) {// to handle the case where the first element is the result itself
				start = 0;
				end = i;
				break;
			}
			if(map.containsKey(currSum-sum)) { 
				// we are checking at every index, that if we remove some elements
				// from the beginning can we achive the desired sum, if yes inc the index
				start = map.get(currSum-sum) +1;
				end = i;
				break;
			}
			map.put(currSum, i);
		}
		if(end != -1) {
			System.out.println("Start : "+start+"||End:"+end);
		}
		return false;
	}
	private static void equalNoOfZeroAndOne(int[] a) {
		int start = 0;
		int end = -1;
		int currSum = 0;
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			if(a[i] ==0) {
				a[i] = -1;
			}
		}
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>(); // key is the sum, value is the index
		for (int i = 0; i < a.length; i++) {
			currSum += a[i];
			if(currSum - sum == 0) {
				// to handle the case where the first element is the result itself
				start = 0;
				end = i;
				break;
			}
			if(map.containsKey(currSum-sum)) {
				
				start = map.get(currSum-sum) +1;
				end = i;
				break;
			}
			map.put(currSum, i);
		}
		if(end != -1) {
			System.out.println("Start : "+start+"||End:"+end);
		}
	}
}
