package arrays;

import java.util.ArrayList;
import java.util.List;

public class MajorityElement2 {
    /*
    Given an integer array of size n, find all elements that appear more than ⌊ n/3 ⌋ times.



Example 1:

Input: nums = [3,2,3]
Output: [3]
Example 2:

Input: nums = [1]
Output: [1]
Example 3:

Input: nums = [1,2]
Output: [1,2]
    * */


    /*
    Let's start with the simplest case.

[1,2,3]
In this case, n/3 is 1 and occurence of all numbers are 1, so we don't find majority numbers.

[1,2,1]
In this case, n/3 is 1 and occurence of 1 is 2, so we find 1 majority number.

[1,2,1,2]
In this case, n/3 is 1.333... and occurence of 1 and 2 are both 2, so we find 2 majority numbers.

[1,2,1,2,3]
In this case, n/3 is 1.666... occurence of 1 and 2 are both are 2, so we find 2 majority numbers.

[1,2,1,2,3,3]
In this case, n/3 is 2 all numbers are 2, so we don't find majority numbers.

⭐️ Points

From exmaples above, We can find 2 majority numbers at most.

To prove that, we can calculate like this.

From [1,2,3] and [1,2,1] cases, to make some number majority element, we need n/3 + 1 of some number. If we multiply 3 with n/3 + 1, the answer is definitely longer than length of original input array. That's why we can't create 3 majority elements.

(n/3 + 1) * 3 > input array
n = 6

case * 3
(6/3 + 1) * 3 > 6

case * 2 , both sides are equal.
(6/3 + 1) * 2 == 6
That's why we can find 2 majority numbers at most.


Boyer-Moore Majority Vote Algorithm
The algorithm above is known as Boyer-Moore Majority Vote Algorithm.

The Boyer-Moore Majority Vote Algorithm is an efficient algorithm used to find the majority element (an element that appears more than half of the time) in a given array. This algorithm operates in linear time and requires O(1) additional space.

Here are the basic steps of the Boyer-Moore Majority Vote Algorithm:

Candidate Element Selection: Choose the first element of the array as the candidate element and initialize a counter.

Element Counting: Traverse through the array. If the current element matches the candidate element, increment the counter. If they don't match, decrement the counter.

Check Counter: If the counter becomes zero, choose the current element as the new candidate element and reset the counter to one.

Final Candidate Verification: After these steps, verify if the selected candidate is indeed the majority element.

By following these steps, the algorithm efficiently identifies the most frequently occurring element, if one exists, in linear time O(n) in the worst case. This efficiency is achieved by carefully selecting candidate elements and comparing them against other elements.

We use this idea for two majority candidates. Let's see real algorithm!

Algorithm Overview:
Initialize majority1, majority2, count1, and count2 to track the majority elements and their counts.
Iterate through the input list nums to find the two majority elements.
Iterate through nums again to verify the counts of the two majority elements.
Return a list of majority elements that appear more than len(nums) // 3 times.
Detailed Explanation:
Initialization:

Initialize majority1, majority2, count1, and count2 to 0. These variables will be used to keep track of the majority elements and their counts.
First Iteration to Find Majority Elements:

Iterate through the input list nums.
If the current number is equal to majority1, increment count1.
If the current number is equal to majority2, increment count2.
If count1 is 0, update majority1 to the current number and increment count1.
If count2 is 0, update majority2 to the current number and increment count2.
If both count1 and count2 are non-zero and the current number is not equal to either majority, decrement count1 and count2.
Second Iteration to Verify Counts:

Reset count1 and count2 to 0.
Iterate through nums again.
Count the occurrences of majority1 and majority2 by incrementing count1 and count2 accordingly.
Result Generation:

Create an empty list res to store the majority elements.
If count1 is greater than len(nums) // 3, add majority1 to res.
If count2 is greater than len(nums) // 3, add majority2 to res.
Return the list res containing the majority elements that appear more than len(nums) // 3 times.
The algorithm uses the Boyer-Moore Majority Vote algorithm to find the majority elements in nums efficiently and then verifies their counts to ensure they appear more than len(nums) // 3 times before returning the result.


    * */

    public List<Integer> majorityElement(int[] nums) {
        Integer majority1 = 0;
        Integer majority2 = 0;
        int count1 = 0;
        int count2 = 0;

        for (int num : nums) {
            if (num == majority1) {
                count1++;
            } else if (num == majority2) {
                count2++;
            } else if (count1 == 0) {
                majority1 = num;
                count1++;
            } else if (count2 == 0) {
                majority2 = num;
                count2++;
            } else {
                count1--;
                count2--;
            }
        }

        count1 = 0;
        count2 = 0;

        for (int num : nums) {
            if (num == majority1) {
                count1++;
            } else if (num == majority2) {
                count2++;
            }
        }

        List<Integer> res = new ArrayList<>();
        int n = nums.length;

        if (count1 > n / 3) {
            res.add(majority1);
        }
        if (count2 > n / 3) {
            res.add(majority2);
        }

        return res;
    }
}
