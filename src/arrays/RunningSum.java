package arrays;

public class RunningSum {
    /*
    # Running Sum of 1D Array — Prefix Sum

**Pattern:** Prefix Sum / Cumulative Sum

## Problem
For each index `i`, calculate the sum of all elements from index `0` to `i`.

Example: `[3, 1, 2, 10, 1]` → `[3, 4, 6, 16, 17]`

## Core Intuition
Instead of recalculating the sum of all previous elements at every index, reuse the sum already calculated for the previous index.

**Formula:**
`runningSum[i] = nums[i] + runningSum[i - 1]`

The first element remains unchanged because its running sum is itself.

## Optimal Java Solution

```java
class Solution {
    public int[] runningSum(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            nums[i] += nums[i - 1];
        }
        return nums;
    }
}
```

## Dry Run

Input: `[3, 1, 2, 10, 1]`

- `i = 1`: `nums[1] = 1 + 3 = 4` → `[3, 4, 2, 10, 1]`
- `i = 2`: `nums[2] = 2 + 4 = 6` → `[3, 4, 6, 10, 1]`
- `i = 3`: `nums[3] = 10 + 6 = 16` → `[3, 4, 6, 16, 1]`
- `i = 4`: `nums[4] = 1 + 16 = 17` → `[3, 4, 6, 16, 17]`

## Why It Works
After processing index `i - 1`, `nums[i - 1]` already stores the sum of all original elements up to that index. Adding it to the untouched `nums[i]` produces the running sum at index `i`.

## Important Java Concepts

- `a = a + b` and `a += b` are equivalent for these integer variables.
- `a++` is post-increment: use the old value in the expression, then increment `a`.
- `++a` is pre-increment: increment `a` first, then use the new value in the expression.
- `nums[i] += nums[i - 1]` updates the existing array element in place.

## Complexity
- **Time:** `O(n)` — visit each element once.
- **Auxiliary space:** `O(1)` — modify the input array directly.

## Interview Approach
1. Recognize that each answer depends on the previous cumulative answer.
2. Start with the first element as the initial running sum.
3. Add the previous running sum to each next element.
4. Update the array in place to avoid an additional array.

**Memory hook:** Previous prefix sum + current original element = current prefix sum.

**Where this concept helps next:** Find Pivot Index, Subarray Sum Equals K, and other prefix-sum problems.

    * */
}
