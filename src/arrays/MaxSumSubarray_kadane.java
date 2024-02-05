package arrays;

public class MaxSumSubarray_kadane {
	public static void main(String[] args) {
		/*
		 * aim is to find the maximum sum of any subarray present in an array
		 * 
		 * {1,15,-20,25,600}
		 * 
		 * max sum - 625 {3,4}
		 * 
		 * time complexity using kadane's - O(n)
		 * */
		int[] a = {5,-4,-2,6,-1};
		int[] b = {-5,-4,-2,-6,-1};
		System.out.println("Max sum : "+findMaxSumSubarray(a));
		System.out.println("Max sum : "+findMaxSumSubarrayNegative(b));
	}

	private static int findMaxSumSubarrayNegative(int[] a) {
		int curr = 0, max = Integer.MIN_VALUE; 
		for (int i = 0; i < a.length; i++) {
			curr += a[i];
			if(curr > max) {
				max = curr;
			}
			if(curr < max) {
				curr=0;
			}
		}
		return max;
	}

	private static int findMaxSumSubarray(int[] a) {
		int curr = 0, max = 0;
		for (int i = 0; i < a.length; i++) {
			curr += a[i];
			if(curr>max) {
				max = curr;
			}
			if(curr<0) {
				curr=0;
			}
		}
		return max;
	}
}