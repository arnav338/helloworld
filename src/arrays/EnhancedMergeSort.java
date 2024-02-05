package arrays;

import java.util.Arrays;

public class EnhancedMergeSort {
	/*
	 * Given an array of integers. Find the Inversion Count in the array.
	 * 
	 * Inversion Count: For an array, inversion count indicates how far (or close)
	 * the array is from being sorted. If array is already sorted then the inversion
	 * count is 0. If an array is sorted in the reverse order then the inversion
	 * count is the maximum. Formally, two elements a[i] and a[j] form an inversion
	 * if a[i] > a[j] and i < j.
	 * 
	 * 
	 * Example 1:
	 * 
	 * Input: 
	 * N = 5, 
	 * arr[] = {2, 4, 1, 3, 5} 
	 * Output: 3 
	 * Explanation: 
	 * The sequence 2, 4, 1, 3, 5 has three inversions (2, 1), (4, 1), (4, 3). 
	 * 
	 * Example 2:
	 * 
	 * Input: N = 5 
	 * arr[] = {2, 3, 4, 5, 6} 
	 * Output: 0 
	 * Explanation: As the sequence is already sorted so there is no inversion count.
	 */
	// Function to count the number of inversions
    // during the merge process
    private static int mergeAndCount(int[] arr, int l,
                                     int m, int r)
    {
 
        // Left subarray
        int[] left = Arrays.copyOfRange(arr, l, m + 1);
        // Right subarray
        int[] right = Arrays.copyOfRange(arr, m + 1, r + 1);
 
        int i = 0, j = 0, k = l, swaps = 0;
 
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) 
            	// if left is lower or equal than right no inversion,  
            	// hence don't increment swaps
                arr[k++] = left[i++];
            else {
            	// if left is greater than right there is inversion,  
            	// hence increment swaps
                arr[k++] = right[j++];
                swaps += (m + 1) - (l + i);
            }
        }
        //copying remaining elements to array
        while (i < left.length)
            arr[k++] = left[i++];
        while (j < right.length)
            arr[k++] = right[j++];
        return swaps;
    }
 
    // Merge sort function
    private static int mergeSortAndCount(int[] arr, int l,
                                         int r)
    {
 
        // Keeps track of the inversion count at a particular node of the recursion tree
        int count = 0;
 
        if (l < r) {
            int m = (l + r) / 2;
 
            // Total inversion count = left subarray count + right subarray count + merge count
 
            // Left subarray count
            count += mergeSortAndCount(arr, l, m);
 
            // Right subarray count
            count += mergeSortAndCount(arr, m + 1, r);
 
            // Merge count
            // as the inversions in left and right subarray have already been counted
            // the left ones are only the ones which are there in the merge step
            count += mergeAndCount(arr, l, m, r);
        }
 
        return count;
    }
    public static void main(String[] args)
    {
        int[] arr = { 1, 20, 6, 4, 5 };
 
        System.out.println(mergeSortAndCount(arr, 0, arr.length - 1));
    }
}
