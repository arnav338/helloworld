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
		//int[] a = {5,-4,-2,6,-1};
		int[] a = {1,15,-20,25,600};
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
	/*
	LEETCODE 53 — MAXIMUM SUBARRAY

PATTERN
Kadane's Algorithm / Dynamic Programming

--------------------------------------------------
CORE PROBLEM

Find the contiguous subarray with the maximum sum.

Example:

[-2, 1, -3, 4, -1, 2, 1, -5, 4]

Best subarray:

[4, -1, 2, 1] = 6

--------------------------------------------------
FIRST THOUGHT

Brute force:
Try every possible subarray and calculate its sum.

O(n²) possible subarrays.

Can we avoid reconsidering all previous elements?

Yes.

--------------------------------------------------
KEY INTUITION

At every index, ask:

"Is my previous subarray helping me or hurting me?"

Suppose:

previous sum = -5
current number = 4

Continuing:

-5 + 4 = -1

Starting fresh:

4

So a NEGATIVE running sum can never help a future
maximum subarray.

Therefore:

if curr < 0
    discard it
    curr = 0

This means:

"Start a new subarray from the next element."

--------------------------------------------------
STATE

curr = maximum sum of a subarray
       that ENDS at the current position.

sum = maximum subarray sum seen so far.

At every number:

curr += nums[i]

Then:

sum = max(sum, curr)

If curr becomes negative:

curr = 0

--------------------------------------------------
WHY RESET TO ZERO?

Suppose:

[4, -10, 5]

After 4:

curr = 4

After -10:

curr = -6

Now consider 5.

Continuing gives:

-6 + 5 = -1

Starting fresh gives:

5

So the -6 prefix is useless.

Discard it:

curr = 0

Then next element 5 starts a new candidate.

--------------------------------------------------
IMPORTANT ORDER

Your code does:

curr += nums[i]

sum = max(curr, sum)

if (curr < 0)
    curr = 0;

The MAX MUST be updated BEFORE resetting.

Why?

Because the current subarray itself may be the best answer,
even if its sum is negative.

Example:

[-3, -2, -5]

At -3:

curr = -3
sum = -3
curr < 0 → reset

At -2:

curr = -2
sum = max(-3, -2) = -2

Therefore answer = -2.

This is why initializing:

sum = Integer.MIN_VALUE

is important.

--------------------------------------------------
DRY RUN

nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]

Start:

curr = 0
sum = -∞


i=0, -2:

curr = -2
sum = -2
curr < 0 → curr = 0


i=1, 1:

curr = 1
sum = 1


i=2, -3:

curr = -2
sum = 1
curr < 0 → curr = 0


i=3, 4:

curr = 4
sum = 4


i=4, -1:

curr = 3
sum = 4


i=5, 2:

curr = 5
sum = 5


i=6, 1:

curr = 6
sum = 6


i=7, -5:

curr = 1
sum = 6


i=8, 4:

curr = 5
sum = 6

Answer = 6

The running sum of 6 corresponds to:

[4, -1, 2, 1]

--------------------------------------------------
YOUR CODE

class Solution {
    public int maxSubArray(int[] nums) {

        int sum = Integer.MIN_VALUE;
        int curr = 0;

        for (int i = 0; i < nums.length; i++) {

            // Best subarray ending at i
            curr += nums[i];

            // Best subarray seen anywhere so far
            sum = Math.max(curr, sum);

            // Negative prefix can only hurt future subarrays.
            if (curr < 0) {
                curr = 0;
            }
        }

        return sum;
    }
}

--------------------------------------------------
COMPLEXITY

Time:  O(n)
Space: O(1)

--------------------------------------------------
GENERALIZATION

This is a useful DP pattern:

At every position, maintain the BEST state
that ends exactly here.

Then decide:

    extend previous state
          OR
    start fresh

For Maximum Subarray:

    curr = max(nums[i], curr + nums[i])

Your implementation expresses the same idea
using curr = 0 after a negative prefix.

--------------------------------------------------
MEMORY HOOK

"Negative baggage never helps."

Keep extending while the previous sum is useful.

If the running sum becomes negative:

    DROP IT → START FRESH

And always:

    update answer BEFORE resetting.

--------------------------------------------------
INTERVIEW ONE-LINER

"I maintain the maximum sum of a subarray ending at the
current index. If that running sum becomes negative, I discard
it because adding a negative prefix can only reduce any future
subarray sum. I keep the global maximum separately."
	* */
}