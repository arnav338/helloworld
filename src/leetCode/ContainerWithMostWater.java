package leetCode;

public class ContainerWithMostWater {
    /*
    You are given an integer array height of length n. There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).

Find two lines that together with the x-axis form a container, such that the container contains the most water.

Return the maximum amount of water a container can store.

Notice that you may not slant the container.



Example 1:


Input: height = [1,8,6,2,5,4,8,3,7]
Output: 49
Explanation: The above vertical lines are represented by array [1,8,6,2,5,4,8,3,7]. In this case, the max area of water (blue section) the container can contain is 49.
Example 2:

Input: height = [1,1]
Output: 1
    * */

    /*
    Time complexity: O(n)
    Space complexity: O(1)
    * */
    public int maxArea(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;

        while (left < right) {
            maxArea = Math.max(maxArea, (right - left) * Math.min(height[left], height[right]));

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }

    /*
    Simply, we calculate max area of rectangle then return it. Formula is

width * height
Let's think about width and height with this example.

Input: height = [1,8,6,2,5,4,8,3,7]
Each number is height, so we can easily get height. But how about width?

width is also simple. It's just distance between two heights. That's why it's good idea to have two pointers left and right.

 0,1,2,3,4,5,6,7,8 (= index)
[1,8,6,2,5,4,8,3,7]
 L               R

Left pointer starts from index 0
Right pointer starts from the last index
Let's see how it works!

width = right - left = 8 - 0 = 8
For height, we take smaller height between left and right, because if we calculate based on the taller height, the water would overflow from the container.

height = min(height[left], height[right]) = min(1, 7) = 1
Current max area should be

8 * 1 = 8
After that, we want to move one of the pointers. How can we judge it? It's simple. We want to keep taller height between left and right because there is a possibility that we will get max area with the taller height.

In this case,

left vs right = 1 vs 7
So we move the left pointer to next.

 0,1,2,3,4,5,6,7,8 (= index)
[1,8,6,2,5,4,8,3,7]
   L             R

max_area = 8
We will repeat the same process until we break left < right condition. I'll speed up.

 0,1,2,3,4,5,6,7,8 (= index)
[1,8,6,2,5,4,8,3,7]
   L             R

max_area = 8
current_area = 7 * 7 = 49
max_area = max(8, 49) = 49
Move R to next
 0,1,2,3,4,5,6,7,8 (= index)
[1,8,6,2,5,4,8,3,7]
   L           R

max_area = 49
current_area = 6 * 3 = 18
max_area = max(49, 18) = 49
Move R to next
 0,1,2,3,4,5,6,7,8 (= index)
[1,8,6,2,5,4,8,3,7]
   L         R

max_area = 49
current_area = 5 * 8 = 40
max_area = max(49, 40) = 49
Move L to next
In the above case, we can also move R instead of L because L and Rare the same(= 8).

 0,1,2,3,4,5,6,7,8 (= index)
[1,8,6,2,5,4,8,3,7]
     L       R

max_area = 49
current_area = 4 * 6 = 14
max_area = max(49, 24) = 49
Move L to next
 0,1,2,3,4,5,6,7,8 (= index)
[1,8,6,2,5,4,8,3,7]
       L     R

max_area = 49
current_area = 3 * 2 = 6
max_area = max(49, 6) = 49
Move L to next
 0,1,2,3,4,5,6,7,8 (= index)
[1,8,6,2,5,4,8,3,7]
         L   R

max_area = 49
current_area = 2 * 5 = 10
max_area = max(49, 10) = 49
Move L to next
 0,1,2,3,4,5,6,7,8 (= index)
[1,8,6,2,5,4,8,3,7]
           L R

max_area = 49
current_area = 1 * 4 = 4
max_area = max(49, 4) = 49
Move L to next
Now L and R are the same index. We shop iteration.

 0,1,2,3,4,5,6,7,8 (= index)
[1,8,6,2,5,4,8,3,7]
             L
             R
return 49


LEETCODE 11 — CONTAINER WITH MOST WATER

Pattern:
Two Pointers + Greedy

Core Formula:
area = width × limiting height

area = (right - left) * min(height[left], height[right])


1. First Thought / Brute Force
--------------------------------
Try every pair of lines (left, right).

For every pair:
    area = (right - left) * min(height[left], height[right])

There are O(n²) pairs.

Brute Force:
Time  = O(n²)
Space = O(1)


2. Key Intuition
----------------
For any two lines:

    width = right - left
    height = min(height[left], height[right])

The SHORTER line is the limiting wall.

Example:

    5                    8
    |                    |
    |                    |
    +--------------------+

Height is limited to 5.

If we move the taller 8:
    - width decreases
    - 5 is still present
    - height cannot become > 5

So moving the taller side can NEVER improve the area.

Therefore:

    Move the SHORTER pointer.

This gives the shorter side a chance to be replaced by
a taller line, potentially increasing the limiting height.


3. Two-Pointer Strategy
-----------------------
Start with the widest possible container:

    left  = 0
    right = n - 1

At every step:

    1. Calculate current area.
    2. Update maximum area.
    3. Move the pointer at the shorter line.

Why?

    Shorter wall = bottleneck
    → discard bottleneck
    → search for a taller wall


4. Clean Optimal Code
---------------------
class Solution {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;

        while (left < right) {

            // Area is limited by the shorter wall.
            int width = right - left;
            int waterHeight = Math.min(height[left], height[right]);
            int area = width * waterHeight;

            maxArea = Math.max(maxArea, area);

            // Shorter wall is the bottleneck.
            // Moving the taller wall cannot improve the height,
            // because the shorter wall would still limit us.
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }
}


5. Complexity
-------------
Time  = O(n)
Space = O(1)

Each pointer only moves inward, so there are at most n pointer movements.


6. Interview Derivation
-----------------------
Start from:

    area = width × min(leftHeight, rightHeight)

Brute force checks all pairs → O(n²).

To optimize, start with the widest pair.

At every step, width will decrease, so the only way to
potentially improve the area is to increase the limiting height.

The shorter line is the limiting height.

Moving the taller line cannot increase the limiting height,
so discard the shorter line and move that pointer inward.

Hence:

    if leftHeight < rightHeight:
        left++
    else:
        right--


7. Memory Hook
--------------
"Width always decreases.
Only height can save us.
Shorter wall is the bottleneck.
Discard the bottleneck."

Or simply:

    SHORTER WALL → MOVE IT
    * */


}
