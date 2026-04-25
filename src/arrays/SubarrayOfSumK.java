package arrays;

import java.util.HashMap;

public class SubarrayOfSumK {

    /*
    Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.

A subarray is a contiguous non-empty sequence of elements within an array.



Example 1:

Input: nums = [1,1,1], k = 2
Output: 2
Example 2:

Input: nums = [1,2,3], k = 3
Output: 2
    * */

    /*
    The problem can be solved using the concept of prefix sums. The idea is to count how many subarrays sum to k by leveraging the difference between the cumulative sum at two indices. If the difference between two prefix sums equals k, then the subarray between these indices sums to k.

Approach
Use a HashMap:

Store the cumulative sum (prefix sum) as the key and the number of times this sum occurs as the value.
Initialize the map with (0, 1) to account for subarrays that start from index 0.
Iterate through the array:

Add the current element to the cumulative sum.
Check if sum - k exists in the map. If it does, it means there are subarrays ending at the current index that sum to k. Add the frequency of sum - k to the count.
Update the map with the current cumulative sum.
Return the count:

After iterating through the array, count will hold the total number of subarrays with a sum equal to k.
    * */
    public static void main(String[] args) {
        int[] arr = new int[]{1,1,1};
        System.out.println("res - "+subarraySum(arr,2));
    }
    public static int subarraySum(int[] nums, int k) {
        int count = 0, sum = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        /*
        let's say we do not add above value in pair in map
        [1,1,1] k = 2

        when we reach i=1, 2nd element
        we will check the condition map.contains(2-0),
        if we havent put the entry (0,1) in map, this condition will become false
        hence the pair (0,1) is there to account for subarrays that start from index 0

Add the current element to the cumulative sum.
Check if sum - k exists in the map. If it does, it means there are subarrays ending at the current index that sum to k.
Add the frequency of sum - k to the count.
Update the map with the current cumulative sum.
        * */
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (map.containsKey(sum - k))
                count += map.get(sum - k);
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        return count;
    }
}
