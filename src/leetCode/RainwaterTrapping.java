package leetCode;

public class RainwaterTrapping {
    /*
    Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.
Example 1:
Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
Output: 6
Explanation: The above elevation map (black section) is represented by array [0,1,0,2,1,0,1,3,2,1,2,1]. In this case, 6 units of rain water (blue section) are being trapped.
Example 2:

Input: height = [4,2,0,3,2,5]
Output: 9
    * */

    public static void main(String[] args) {
        int[] arr = new int[]{0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println("answer - "+trap(arr));
        System.out.println("answer 1 - "+trap_(arr));
        System.out.println("answer 2 - "+trap__(arr));
    }
    /*
    this solution can further be optimized by removing the extra space
    this solution makes use of the fact that we are processing any index, only when height[left] or height[right] is provided to be bigger than current height
    hence we can make use of single index while computing
    * */

    static int trap__(int[] height) {
        int n = height.length;
        int left = 0, right = n - 1;
        int res = 0;
        int maxLeft = 0, maxRight = 0;
        while (left <= right) {
            if (height[left] <= height[right]) { // traversing smaller elements first
                // we are only taking leftMax into account as we are sure that at the right we dont have anything smaller than maxLeft (as we have used the if condition above)
                // by using the above if condition we have ensured that we have a supporting structure at the right is there,
                // that is taller or equal than the current so that water does not overflow
                res += Math.max(maxLeft - height[left], 0);
                maxLeft = Math.max(height[left],maxLeft);
                left++; // since we have processed this index we move forward
            } else {
                // we are only taking rightMax into account as we are sure that at the left we dont have anything smaller than rightMax (as we have used the if condition above)
                res += Math.max(maxRight - height[right], 0);
                maxRight = Math.max(height[right],maxRight);
                right--;
            }
        }
        return res;
    }
    /*
    LEETCODE 42 — TRAPPING RAIN WATER

Pattern:
Two Pointers + Greedy + Running Maximums

Core Idea:
For every index:

    water[i] = min(maxLeft, maxRight) - height[i]

The smaller of the two boundaries determines how much water can be trapped.

The normal approach stores maxLeft/maxRight for every index using prefix/suffix
arrays → O(n) extra space.

The cleaner approach realizes we don't need those arrays.
We only need the CURRENT maximum height from each side:

    maxLeft
    maxRight

and two pointers:

    left
    right

Key Insight:
If:

    maxLeft <= maxRight

then the LEFT side is the limiting side.

Why?
The water level at left is:

    min(maxLeft, maxRight)

Since maxLeft <= maxRight:

    min(maxLeft, maxRight) = maxLeft

Therefore the exact value of maxRight no longer matters.
We can safely calculate the water at `left` using only `maxLeft`.

Similarly:

    maxRight < maxLeft
            ↓
    right side is limiting
            ↓
    safely calculate water at right

Mental Model:
"The smaller maximum boundary decides the water level."

Thought Process:
1. At each position, water depends on the tallest wall on both sides.
2. Storing all left/right maximums works, but uses O(n) space.
3. Can we keep only the current maximum from each side?
4. If maxLeft <= maxRight, left is the smaller boundary.
5. Therefore water at `left` is already determined by `maxLeft`.
6. Process left and move `left++`.
7. Otherwise process right and move `right--`.
8. Continue until the pointers meet.

Example:
height = [4,2,0,3,2,5]

Initially:

    left = 0
    right = 5
    maxLeft = 4
    maxRight = 5

Since:

    maxLeft <= maxRight
    4 <= 5

process LEFT.

At height 2:

    water = 4 - 2 = 2

At height 0:

    water = 4 - 0 = 4

At height 3:

    water = 4 - 3 = 1

At height 2:

    water = 4 - 2 = 2

Total:

    2 + 4 + 1 + 2 = 9

Important:
The condition:

    maxLeft <= maxRight

is NOT saying the left bar itself is smaller.

It means the tallest boundary discovered from the left is currently
smaller/equal to the tallest boundary discovered from the right.

That guarantees the left side is the limiting boundary.

Why `Math.max(..., 0)`?
If:

    maxLeft - height[left] < 0

there is no water at that position.

In the standard two-pointer implementation, this can also be avoided
by updating the maximum first or by relying on the invariant, but your
current code's `Math.max(..., 0)` is perfectly safe.

Cleaner Code:

class Solution {
    public int trap(int[] height) {
        int left = 0;
        int right = height.length - 1;

        int maxLeft = 0;
        int maxRight = 0;
        int res = 0;

        while (left <= right) {

            if (maxLeft <= maxRight) {
                // Right side is already tall enough to support maxLeft.
                res += Math.max(maxLeft - height[left], 0);
                maxLeft = Math.max(maxLeft, height[left]);
                left++;
            } else {
                // Left side is already tall enough to support maxRight.
                res += Math.max(maxRight - height[right], 0);
                maxRight = Math.max(maxRight, height[right]);
                right--;
            }
        }

        return res;
    }
}

Your Code:
Your two-pointer solution is correct and achieves the optimal complexity.

One small cleanup:
You initialize:

    maxLeft = height[0]
    maxRight = height[n-1]

So you could start pointers at the ends and keep your code as-is.
Using `maxLeft = 0` and `maxRight = 0` makes the invariant slightly
easier to reason about and avoids needing special handling for small arrays.

Brute Force:
For every index, scan left to find maxLeft and scan right to find maxRight.

Time: O(n²)
Space: O(1)

Prefix/Suffix Approach:
Precompute:

    left[i]  = tallest wall to the left
    right[i] = tallest wall to the right

Then:

    water[i] = min(left[i], right[i]) - height[i]

Time: O(n)
Space: O(n)

Optimal Two-Pointer Approach:
Keep only:

    maxLeft
    maxRight
    left
    right

Time: O(n)
Space: O(1)

Why O(n)?
Each pointer only moves in one direction.
`left` moves at most n times and `right` moves at most n times.
No element is processed repeatedly.

How to Derive This in an Interview:
Start with:

    water[i] = min(maxLeft, maxRight) - height[i]

Then ask:

    "Do I really need maxLeft/maxRight for every index?"

No.

I only need the current maximum from each side.

Then ask:

    "Can I decide which side's water is determined?"

Yes:

    maxLeft <= maxRight → process left
    maxRight < maxLeft  → process right

This removes the arrays and gives O(1) extra space.

What Can You Generalize:
1. Don't immediately accept an O(n)-space solution if the stored information
   may be compressible into a few running variables.

2. Ask:
   "What information from the past do I REALLY need?"

3. A common optimization pattern is:

    Store everything → identify what actually matters → keep only that state.

4. The two-pointer technique often works when one side can be proven to be
   the limiting side.

5. This is an example of using a PROOF/INVARIANT to decide which pointer
   can safely move, rather than blindly moving pointers.

Easy to Forget:
- Water is controlled by the SMALLER boundary.
- `maxLeft <= maxRight` → safely process LEFT.
- `maxRight < maxLeft` → safely process RIGHT.
- `maxLeft` and `maxRight` mean maximum heights SEEN SO FAR, not necessarily
  the immediate neighboring bars.
- `left` and `right` move only inward.
- The answer is the SUM of trapped water at each position.

Memory Hook:
"Smaller max side controls the water.
If leftMax <= rightMax → calculate left.
Otherwise → calculate right."

Pattern Connection:
LeetCode 1695:
    Sliding Window → maintain a valid range.

LeetCode 42:
    Two Pointers → maintain enough information about both boundaries.

Both use the same broader DSA idea:

    Don't track the entire history.
    Track only the STATE that is sufficient to make the next decision.
    * */



//    static int trap__(int[] height) {
//        int n = height.length;
//        int left = 0, right = n - 1;
//        int res = 0;
//        int maxLeft = 0, maxRight = 0;
//        while (left <= right) {
//            if (height[left] <= height[right]) { // traversing smaller elements first
//                if (height[left] >= maxLeft) {
//                    maxLeft = height[left];
//                } else {
//                    res += maxLeft - height[left];
//                }
//                left++;
//            } else {
//                if (height[right] >= maxRight) {
//                    maxRight = height[right];
//                } else {
//                    res += maxRight - height[right];
//                }
//                right--;
//            }
//        }
//        return res;
//    }





    /*
    Optimization over brute force -

    we observe that we have to keep track of the left max element encountered till now
    and also we have to keep track of right max element encountered till now
    one of the iteration can be saved as we go from left to right
    as anyways we have to iterate over the array once from left to right while cumulating the answer
    so instead of left[] we can simply have a variable called left that we keep updating

    this saves us on a little time complexity but space complexity is still an issue

    TC = O(N) + O(N)
    SC = O(N)

    * */

    public static int trap_(int[] height) {
        int total = 0;
        int left=-1;
        int[] right = new int[height.length];
        for(int i=height.length-1; i>=0; i--){
            right[i] = i==height.length-1 ? -1 : Math.max(right[i+1],height[i+1]);
        }
        for(int i=0; i<height.length; i++){
            int water = Math.min(left, right[i]) - height[i];
            total += water > 0 ? water : 0;
            left = Math.max(left,height[i]);
        }
        return total;
    }


    /*
    Brute force approach
    first populate 2 diff arrays left and right which maintain the value of max height encountered for each index
    then calculate trapped water at each point by formula -> Math.min(left[i], right[i]) - arr[i]
    if this result is positive add it to the answer

    so we use prefix max and suffix max arrays to keep track

    we are taking min of left and right as the min value will define the level of water that can be stored
    so if value on left is 1 and value on right is 3, water will overflow above 1

    TC - O(N) + O(N) + O(N) =O(3*N)
    SC - O(N) + O(N)
    * */
    public static int trap(int[] height) {
        int total = 0;
        int[] left = new int[height.length];
        int[] right = new int[height.length];
        for(int i=0; i<height.length; i++){
            left[i] = i==0 ? -1 : Math.max(left[i-1],height[i-1]);
        }
        for(int i=height.length-1; i>=0; i--){
            right[i] = i==height.length-1 ? -1 : Math.max(right[i+1],height[i+1]);
        }
        for(int i=0; i<height.length; i++){
            int water = Math.min(left[i], right[i]) - height[i];
            total += water > 0 ? water : 0;
        }
        return total;
    }
}
