package arrays;

public class FindPivotIndex {
    /*
    # Find Pivot Index — LeetCode 724

**Pattern:** Prefix Sum / Cumulative Sum

## Problem
Find the leftmost index where the sum of elements strictly to the left equals the sum of elements strictly to the right. Return `-1` if no pivot exists.

Example: `[1, 7, 3, 6, 5, 6]` → `3`

Left sum = `1 + 7 + 3 = 11`
Right sum = `5 + 6 = 11`

## Core Intuition
Calculate the total sum of the array once. While traversing, maintain `leftSum` and derive `rightSum` without another loop.

**Key formula:**

`rightSum = totalSum - leftSum - nums[i]`

Why? The total sum consists of three parts:

`totalSum = leftSum + nums[i] + rightSum`

Therefore, a pivot exists when:

`leftSum == rightSum`

## Optimal Java Solution

```java
class Solution {
    public int pivotIndex(int[] nums) {
        int total = 0;

        for (int num : nums) {
            total += num;
        }

        int leftSum = 0;

        for (int i = 0; i < nums.length; i++) {
            int rightSum = total - leftSum - nums[i];

            if (leftSum == rightSum) {
                return i;
            }

            leftSum += nums[i];
        }

        return -1;
    }
}
```

## Dry Run

Input: `[1, 7, 3, 6, 5, 6]`

Total sum = `28`

| Index | Value | Left sum | Right sum | Pivot? |
|---:|---:|---:|---:|---|
| 0 | 1 | 0 | 27 | No |
| 1 | 7 | 1 | 20 | No |
| 2 | 3 | 8 | 17 | No |
| 3 | 6 | 11 | 11 | Yes |

Return `3`, the leftmost pivot index.

## Important Details
- `leftSum` must contain only elements strictly before the current index.
- Check the pivot condition **before** adding `nums[i]` to `leftSum`.
- The leftmost pivot is guaranteed by traversing from left to right and returning immediately when a match is found.
- Edge indices work naturally: the missing side has sum `0`.
- Negative numbers are allowed; the formula still works.

## Complexity
- **Time:** `O(n)` — two passes through the array.
- **Auxiliary space:** `O(1)` — only a few variables.

## Interview Approach
1. A brute-force solution would calculate left and right sums at every index, taking `O(n²)` time.
2. Improve it by calculating the total sum once.
3. Maintain the left sum while traversing.
4. Derive the right sum using `totalSum - leftSum - nums[i]`.
5. Return the first index where both sums are equal.

**Memory hook:** Total − left − current = right.

**Connection to Running Sum:** Both problems reuse cumulative information instead of repeatedly summing array elements. Pivot Index adds the idea of deriving one side's sum from the total.
    * */
}
