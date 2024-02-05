package arrays;

import java.util.HashMap;

public class LongestSubArrayOfSumK {
	/*
	 *  Time Complexity: O(n). 
		Auxiliary Space: O(n)
	 * */
	public static void main(String[] args) {
		int[] arr = {10, 5, 2, 7, 1, 9};
        int n = arr.length;
        int k = 15;
        System.out.println("Length = " + lenOfLongSubarr(arr, n, k));
	}
	
	// function to find the length of longest subarray having sum k
    static int lenOfLongSubarr(int[] arr, int n, int desiredSum)
    {
           // HashMap to store (sum, index) tuples
           HashMap<Integer, Integer> map = new HashMap<>();
           
           int sum = 0, maxLen = 0;

           // traverse the given array
           for (int i = 0; i < n; i++) {
               
                // accumulate sum
                sum += arr[i];
                // when subarray starts from index '0'
                if (sum == desiredSum)
                    maxLen = i + 1;
                // make an entry for 'sum' if it is not present in 'map'
                map.putIfAbsent(sum, i);
                
                // check if 'sum-k' is present in 'map' or not
                if (map.containsKey(sum - desiredSum)) {
                    // update maxLength
                    if (maxLen < (i - map.get(sum - desiredSum)))
                        maxLen = i - map.get(sum - desiredSum);
                }
           }
            
           return maxLen;            
    }
}
