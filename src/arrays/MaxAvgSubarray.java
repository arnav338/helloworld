package arrays;

public class MaxAvgSubarray {
    /*
    You are given an integer array nums consisting of n elements, and an integer k.

Find a contiguous subarray whose length is equal to k that has the maximum average value and return this value. Any answer with a calculation error less than 10-5 will be accepted.



Example 1:

Input: nums = [1,12,-5,-6,50,3], k = 4
Output: 12.75000
Explanation: Maximum average is (12 - 5 - 6 + 50) / 4 = 51 / 4 = 12.75
Example 2:

Input: nums = [5], k = 1
Output: 5.00000


Constraints:

n == nums.length
1 <= k <= n <= 105
-104 <= nums[i] <= 104
    * */

    public static void main(String[] args) {
        int[] arr = new int[]{1,12,-5,-6,50,3};
        int k=4;
        double d = findMaxAverage(arr,k);
        System.out.println("result - "+d);
    }




    /*
     Intuition
To find the contiguous subarray of length k with the maximum average, we can use the sliding window technique. Instead of recalculating the sum for every subarray, we maintain a running sum of size k, updating it by subtracting the element leaving the window and adding the new element entering the window. This efficiently tracks the maximum sum.
🛠️ Approach
Initialize the sum of the first k elements as the initial window sum.
Set maxSum = sum.
Slide the window through the array from index k to n-1:
Subtract nums[i - k] (element leaving the window)
Add nums[i] (element entering the window)
Update maxSum if the current sum is greater.
After processing all windows, return maxSum / k as the maximum average.
📊 Complexity
Time complexity: O(n) → Single pass through the array using the sliding window.
Space complexity: O(1) → Only a few variables are used; no extra data structures.
    * */
    public static double findMaxAverage(int[] nums, int k) {
        int n = nums.length;
        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += nums[i];
        }
        int maxSum = sum;
        for (int i = k; i < n; i++) {
            sum = sum - nums[i - k] + nums[i];
            if (sum > maxSum) {
                maxSum = sum;
            }
        }
        return (double) maxSum / k;
    }

}
