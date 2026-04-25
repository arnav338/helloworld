package arrays;

public class FIndMinimumInRotatedSortedArray {
    /*
    Suppose an array of length n sorted in ascending order is rotated between 1 and n times. For example, the array nums = [0,1,2,4,5,6,7] might become:

[4,5,6,7,0,1,2] if it was rotated 4 times.
[0,1,2,4,5,6,7] if it was rotated 7 times.
Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].

Given the sorted rotated array nums of unique elements, return the minimum element of this array.

You must write an algorithm that runs in O(log n) time.



Example 1:

Input: nums = [3,4,5,1,2]
Output: 1
Explanation: The original array was [1,2,3,4,5] rotated 3 times.
Example 2:

Input: nums = [4,5,6,7,0,1,2]
Output: 0
Explanation: The original array was [0,1,2,4,5,6,7] and it was rotated 4 times.
Example 3:

Input: nums = [11,13,15,17]
Output: 11
Explanation: The original array was [11,13,15,17] and it was rotated 4 times.
    * */

    public static void main(String[] args) {
        //int[] nums = new int[]{3,4,5,1,2};
        //int[] nums = new int[]{4,5,6,7,0,1,2};
        //int[] nums = new int[]{11,13,15,17};
        //int[] nums = new int[]{2,3,1};
        //int[] nums = new int[]{2,3,4,5,1};
        //int[] nums = new int[]{3,4,5,1,2};
        int[] nums = new int[]{9,10,24,56,76,54,1,8};
        int min = findMin(nums);
        System.out.println("min is "+min);
    }
    public static int findMin(int[] nums) {
        int n=nums.length;
        int left=0;
        int right=n-1;

        while(left<right){
            int mid= left+(right-left)/2;
            /*
            the key lies in the fact that looking at the end element we are deciding whether to move in left or right,
            rather than deciding on the basis of immediate left and right of the middle number everytime

            the solution works because it is guaranteed that the original array was sorted
            so if we are at mid and see that the last element is higher then it only means that it is in increasing fashion from mid to end,
            so the min element will be found most probably to the left of mid element

             if we are at mid and see that the last element is lower then it only means that the array was rotated in such a way that some lesser
             elements are left at the end and some are at the start, so min element will be found in the right
            * */
            if(nums[mid]>nums[right]){
                left=mid+1;
            }else{
                right=mid;
            }
        }
        return nums[left];
    }
}
