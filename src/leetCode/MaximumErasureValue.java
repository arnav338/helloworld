package leetCode;

import java.util.HashSet;
import java.util.Set;

public class MaximumErasureValue {
    /*
    1695. Maximum Erasure Value
Solved
Medium
Topics
premium lock icon
Companies
Hint
You are given an array of positive integers nums and want to erase a subarray containing unique elements. The score you get by erasing the subarray is equal to the sum of its elements.

Return the maximum score you can get by erasing exactly one subarray.

An array b is called to be a subarray of a if it forms a contiguous subsequence of a, that is, if it is equal to a[l],a[l+1],...,a[r] for some (l,r).



Example 1:

Input: nums = [4,2,4,5,6]
Output: 17
Explanation: The optimal subarray here is [2,4,5,6].
Example 2:

Input: nums = [5,2,1,2,5,2,1,2,5]
Output: 8
Explanation: The optimal subarray here is [5,2,1] or [1,2,5].


Constraints:

1 <= nums.length <= 105
1 <= nums[i] <= 104
    * */

    public int maximumUniqueSubarray(int[] nums) {
        int sum = 0;
        int res = 0;
        int left = 0;
        int right = 0;
        Set<Integer> set = new HashSet<>();
        for(right=0; right<nums.length; right++){
            while(set.contains(nums[right])){
                set.remove(nums[left]);
                sum -= nums[left];
                left++;
            }
            set.add(nums[right]);
            sum += nums[right];
            res = Math.max(sum,res);
        }
        return res;
    }
    /*
    LEETCODE 1695 — MAXIMUM ERASURE VALUE

Pattern:
Sliding Window + HashSet

Core Idea:
Find the contiguous subarray containing only unique elements whose sum is maximum.

The key observation:
We can maintain a valid window [left...right] where every element is unique.
When a duplicate appears, move left forward until the duplicate is removed.

Thought Process:
1. The answer must be a subarray → it must be contiguous.
2. We need the maximum sum of a subarray.
3. The subarray must contain unique elements.
4. This is a perfect Sliding Window problem.
5. Use a HashSet to track which values are currently in the window.
6. Use a running sum instead of recalculating the sum every time.
7. When a duplicate appears:
      - remove nums[left] from the Set
      - subtract nums[left] from sum
      - move left forward
8. Once the window is valid, update the maximum sum.

Brute Force:
Generate every possible subarray and check whether all elements are unique,
then calculate its sum.

There are O(n²) subarrays, and checking uniqueness can take O(n).
Time: O(n³)
A better brute force using a Set can reduce this to O(n²).

Key Mental Model / Invariant:
At every point:

    [left ... right]

contains only unique elements.

The Set represents exactly the elements inside the current window.
The variable sum represents exactly the sum of the current window.

When the window changes:
    add right  → sum += nums[right]
    remove left → sum -= nums[left]

Sliding Window Template:
left = 0
sum = 0

for right:
    while window is invalid:
        remove left
        left++

    add right
    update answer

For this problem:
Data structure → HashSet
Invalid condition → duplicate element
Shrink → remove elements from left
Window state → current sum
Answer → maximum sum

Important:
Because nums contains positive integers, extending a valid window always
increases the sum. Therefore, once the window is valid, its current sum
is the best score for that right boundary.

Example:
nums = [4,2,4,5,6]

right = 0 → [4]       sum = 4
right = 1 → [4,2]     sum = 6
right = 2 → duplicate 4

Remove from left:
[4,2,4]
  ↓ remove 4
[2,4]                sum = 6

Continue:
[2,4,5]              sum = 11
[2,4,5,6]            sum = 17

Answer = 17

Why O(n), despite the nested while:
right moves forward at most n times.
left also moves forward at most n times.
No element is added/removed indefinitely.

Therefore:
Time → O(n)
Space → O(n)

Clean Code:

class Solution {
    public int maximumUniqueSubarray(int[] nums) {
        Set<Integer> set = new HashSet<>();

        int left = 0;
        int sum = 0;
        int maxSum = 0;

        for (int right = 0; right < nums.length; right++) {

            // Remove elements until the window becomes unique.
            while (set.contains(nums[right])) {
                set.remove(nums[left]);
                sum -= nums[left];
                left++;
            }

            // Add the new element to the valid window.
            set.add(nums[right]);
            sum += nums[right];

            // Current window is unique, so update maximum score.
            maxSum = Math.max(maxSum, sum);
        }

        return maxSum;
    }
}

What Can You Generalize:
This is the same Sliding Window pattern as
"Longest Substring Without Repeating Characters".

The difference is only what we track:

LeetCode 3:
    Window condition → unique characters
    Answer → maximum LENGTH

LeetCode 1695:
    Window condition → unique numbers
    Answer → maximum SUM

Memory Hook:
"Unique contiguous range → HashSet + Sliding Window.
Maintain the window sum while expanding/shrinking."

Very important pattern:
    Expand → violation → shrink → valid → calculate/update answer
    * */
}
