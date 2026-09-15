package leetCode;

public class MedianOf2SortedArrays {
    /*
    Approach 1: Merge and Sort
Create a new array with a size equal to the total number of elements in both input arrays.
Insert elements from both input arrays into the new array.
Sort the new array.
Find and return the median of the sorted array.
Time Complexity

In the worst case TC is O((n + m) * log(n + m)).
Space Complexity

O(n + m), where ‘n’ and ‘m’ are the sizes of the arrays.
Approach 2: Two-Pointer Method
Initialize two pointers, i and j, both initially set to 0.
Move the pointer that corresponds to the smaller value forward at each step.
Continue moving the pointers until you have processed half of the total number of elements.
Calculate and return the median based on the values pointed to by i and j.
Time Complexity

O(n + m), where ‘n’ & ‘m’ are the sizes of the two arrays.
Space Complexity

O(1).
Approach 3: Binary Search
Use binary search to partition the smaller of the two input arrays into two parts.
Find the partition of the larger array such that the sum of elements on the left side of the partition in both arrays is half of the total elements.
Check if this partition is valid by verifying if the largest number on the left side is smaller than the smallest number on the right side.
If the partition is valid, calculate and return the median.
Time Complexity

O(logm/logn)
Space Complexity

O(1)
    * */



    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        // Binary-search the smaller array so that our search space is minimum.
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int n1 = nums1.length;
        int n2 = nums2.length;
        int total = n1 + n2;

        // We want the conceptual combined array to be split into:
        //
        // [ LEFT HALF | RIGHT HALF ]
        //
        // For odd total, put the median in the LEFT half.
        // Example: 7 elements -> 4 on left, 3 on right.
        int leftSize = (total + 1) / 2;

        // We don't know how many elements nums1 should contribute
        // to the left half, so binary-search all possible partitions.
        //
        // Example nums1 = [1, 3, 8]:
        // | 1 3 8       -> take 0
        // 1 | 3 8       -> take 1
        // 1 3 | 8       -> take 2
        // 1 3 8 |       -> take 3
        int low = 0;
        int high = n1;

        while (low <= high) {

            // mid1 = number of elements taken from nums1 into LEFT.
            int mid1 = low + (high - low) / 2;

            // Total elements on LEFT must be leftSize.
            // Therefore, nums2 must contribute whatever is remaining.
            //
            // mid1 + mid2 = leftSize
            int mid2 = leftSize - mid1;

            /*
             * We only care about the elements immediately around
             * the two partitions:
             *
             * nums1: [ .... l1 | r1 .... ]
             * nums2: [ .... l2 | r2 .... ]
             *
             * l1/l2 = largest elements on the LEFT
             * r1/r2 = smallest elements on the RIGHT
             *
             * If the partition is at an edge, one side has no element.
             * MIN_VALUE / MAX_VALUE act like -∞ / +∞ so we don't
             * need separate special cases.
             */
            int l1 = (mid1 == 0) ? Integer.MIN_VALUE : nums1[mid1 - 1];
            int r1 = (mid1 == n1) ? Integer.MAX_VALUE : nums1[mid1];

            int l2 = (mid2 == 0) ? Integer.MIN_VALUE : nums2[mid2 - 1];
            int r2 = (mid2 == n2) ? Integer.MAX_VALUE : nums2[mid2];

            /*
             * Is our partition actually valid?
             *
             * Each individual array is already sorted, so we already know:
             *
             * l1 <= r1
             * l2 <= r2
             *
             * We only need to check the CROSS-boundaries:
             *
             * nums1 LEFT  <= nums2 RIGHT
             * nums2 LEFT  <= nums1 RIGHT
             *
             * i.e.
             *
             * l1 <= r2
             * l2 <= r1
             *
             * If both are true:
             *
             * [ everything on LEFT | everything on RIGHT ]
             *
             * is a valid partition of the combined sorted array.
             */
            if (l1 <= r2 && l2 <= r1) {

                // Odd total:
                //
                // Example:
                // [1, 2, 3, 7 | 8, 10, 11]
                //          ↑
                //        median
                //
                // Since we deliberately put the median in LEFT,
                // the median is simply the largest element on LEFT.
                if (total % 2 == 1) {
                    return Math.max(l1, l2);
                }

                // Even total:
                //
                // [1, 2 | 3, 4]
                //      ↑   ↑
                //   left  right
                //
                // Median = average of:
                // largest element on LEFT
                // smallest element on RIGHT
                return (Math.max(l1, l2) + (double) Math.min(r1, r2)) / 2.0;
            }

            /*
             * Partition is invalid.
             *
             * If l1 > r2:
             *
             * nums1 has put TOO MANY elements on the LEFT.
             *
             * Example:
             * nums1: [1, 3, 8 | ...]
             * nums2: [... 7 | ...]
             *
             * 8 > 7 -> nums1 partition is too far RIGHT.
             * Move it LEFT.
             */
            if (l1 > r2) {
                high = mid1 - 1;
            }

            /*
             * Otherwise l2 > r1:
             *
             * nums1 has put TOO FEW elements on the LEFT.
             *
             * We need more elements from nums1 on the LEFT,
             * so move the partition RIGHT.
             */
            else {
                low = mid1 + 1;
            }
        }

        // Valid sorted input should always find a partition.
        throw new IllegalArgumentException("Input arrays must be sorted.");
    }

    /*
    LEETCODE 4 — MEDIAN OF TWO SORTED ARRAYS

PATTERN
Binary Search on Partition

CORE IDEA
Don't merge the two sorted arrays.

Instead, imagine a wall dividing the combined sorted array:

        LEFT | RIGHT

For odd total:
        [1, 2, 3, 7 | 8, 10, 11]
                 ↑
               median

We want exactly (total + 1) / 2 elements on LEFT.

--------------------------------------------------

HOW DO WE FIND THE WALL?

Binary-search the smaller array.

Suppose:

A = [1, 3, 8]
B = [2, 7, 10, 11]

Try a partition:

A: [1, 3 | 8]
B: [2, 7 | 10, 11]

If A contributes `mid1` elements to LEFT,
then B must contribute:

mid2 = leftSize - mid1

because:

mid1 + mid2 = leftSize

So we only need to binary-search the partition in A.

--------------------------------------------------

ONLY 4 VALUES MATTER

A: [ .... l1 | r1 .... ]
B: [ .... l2 | r2 .... ]

l1/l2 = largest elements on LEFT
r1/r2 = smallest elements on RIGHT

Because each individual array is already sorted,
we already know:

l1 <= r1
l2 <= r2

Therefore, we only need to check the CROSS-boundaries:

l1 <= r2
l2 <= r1

If both are true:

        [ EVERYTHING LEFT | EVERYTHING RIGHT ]

is a valid partition.

--------------------------------------------------

HOW TO MOVE THE BINARY SEARCH?

If:

l1 > r2

then nums1 has TOO MANY elements on LEFT.

        → partition in nums1 is too far RIGHT
        → move LEFT
        → high = mid1 - 1


If:

l2 > r1

then nums1 has TOO FEW elements on LEFT.

        → partition in nums1 is too far LEFT
        → move RIGHT
        → low = mid1 + 1

--------------------------------------------------

ONCE PARTITION IS VALID

ODD:

[1, 2, 3, 7 | 8, 10, 11]

Median = largest element on LEFT

        = max(l1, l2)


EVEN:

[1, 2 | 3, 4]

Median = average of:

largest LEFT  = max(l1, l2)
smallest RIGHT = min(r1, r2)

--------------------------------------------------

EDGE CASES

If partition is at the beginning:

    l = -∞

If partition is at the end:

    r = +∞

Use:

Integer.MIN_VALUE
Integer.MAX_VALUE

This avoids separate special-case logic.

--------------------------------------------------

WHY BINARY SEARCH WORKS

We're not binary-searching for the median.

We're binary-searching for the CORRECT WALL.

At every wall, sortedness tells us the direction:

    too far RIGHT → move LEFT
    too far LEFT  → move RIGHT
    correct       → found median

--------------------------------------------------

COMPLEXITY

Time:  O(log(min(m, n)))
Space: O(1)

--------------------------------------------------

MEMORY HOOK

"Find the wall, not the median."

1. Put a wall in both arrays.
2. Left side must contain half the total elements.
3. Only check the 4 boundary values.
4. If l1 > r2 → move A wall LEFT.
5. If l2 > r1 → move A wall RIGHT.
6. Valid wall → median is at the boundary.
    * */
}
