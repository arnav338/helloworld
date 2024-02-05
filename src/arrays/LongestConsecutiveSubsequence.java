package arrays;

import java.util.Arrays;
import java.util.HashSet;

public class LongestConsecutiveSubsequence {
	/*
	 * Given an array of integers, find the length of the longest sub-sequence such
	 * that elements in the subsequence are consecutive integers, the consecutive
	 * numbers can be in any order.
	 * 
	 * Examples:
	 * 
	 * Input: 
	 * arr[] = {1, 9, 3, 10, 4, 20, 2} 
	 *         {1, 2, 3, 4, 9, 10, 20} 
	 * Output: 4 
	 * Explanation: The subsequence 1, 3, 4, 2 is the longest subsequence of consecutive elements
	 */
	public static void main(String[] args) {
		//int[] a = { 1, 9, 3, 10, 4, 20, 2 };
		int[] a = {36, 41, 56, 35, 44, 33, 34, 92, 43, 32, 42};
		System.out.println("longest length : " + longestLength(a));
		System.out.println("longest length : " + findLongestConseqSubseq(a,a.length));
	}
	static int findLongestConseqSubseq(int arr[], int n)
    {	// time complexity - O(n)
        HashSet<Integer> S = new HashSet<Integer>();
        int ans = 0;
        
        // Hash all the array elements
        for (int i = 0; i < n; ++i)
            S.add(arr[i]);
 
        // check each possible sequence from the start
        // then update optimal length
        for (int i = 0; i < n; ++i)
        {
            // if current element is the starting element of a sequence
            if (!S.contains(arr[i] - 1))
            {
                // Then check for next elements in the sequence
                int j = arr[i];
                while (S.contains(j))  // we keep on incrementing j till the value of incremented j is found in set
                    j++;
                /*
                 * if list has 9, 10,11 
                 * then final value of j after while loop will be 12,
                 * so length is only 12 - 9 = 3, i.e., j - arr[i] * */
                // update  optimal length if this length is more
                ans = Math.max(ans, j - arr[i]);
            }
        }
        return ans;
    }
	private static int longestLength(int[] a) {
		// Time complexity: O(nLogn). 
		int max = 0, temp=0;
		Arrays.sort(a);
		for (int i = 0; i < a.length-1; i++) {
			if(a[i+1] - a[i] == 1) {
				temp++;
			}
			else if(a[i+1] - a[i] != 1 && temp!=0) {
				temp++;
				max = Math.max(max, temp);
				temp=0;
			}
		}
		return max;
	}
}
