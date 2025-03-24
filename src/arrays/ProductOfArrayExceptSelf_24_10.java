package arrays;

import java.util.Arrays;

public class ProductOfArrayExceptSelf {
    /*
    Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].

The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.

You must write an algorithm that runs in O(n) time and without using the division operation.



Example 1:

Input: nums = [1,2,3,4]
Output: [24,12,8,6]
Example 2:

Input: nums = [-1,1,0,-3,3]
Output: [0,0,9,0,0]

brute force approach can be to take product of whole array elements and in a single loop divide the whole product by the current element to get the product except self
but this approach will not work if any element is 0, since division by 0 is not possible

Hence we use conecpt of prefix product and suffix product
basically we compute the product of all elements to the left and to the right in separate arrays
and then for each element, product except self will be prefix[i-1] * suffix[i+1]
    * */

    public static void main(String[] args) {
        int[] ans = new int[]{1,2,3,4};
        //int[] ans = new int[]{-1,1,0,-3,3};
        //int[] res = productExceptSelf(ans);
        int[] res = productExceptSelf_(ans);
        for(int i=0; i<res.length; i++){
            System.out.print(res[i]+",");
        }
        System.out.println("");

    }

    public static int[] productExceptSelf(int[] nums) {
        //1,2,3,4
        // TC - O(n),  SC - O(n)
        int[] res = new int[nums.length];
        int[] pre = new int[nums.length];
        int[] suf = new int[nums.length];
        pre[0] = 1;
        suf[suf.length-1] = 1;
        for (int i=1; i<nums.length; i++){
            pre[i] = pre[i-1] * nums[i-1];
        }
        for (int i=nums.length-2; i>=0; i--){
            suf[i] = suf[i+1] * nums[i+1];
        }
        for (int i=0; i<nums.length; i++){
            res[i] = pre[i] * suf[i];
        }
        return res;
    }

    public static int[] productExceptSelf_(int[] nums) {
        //1,2,3,4
        // TC - O(n),  SC - O(n)
        int[] res = new int[nums.length];
        Arrays.fill(res,1);
        int curr = 1;
        for (int i=1; i<nums.length; i++){
            res[i] = curr * res[i];
            curr = curr * nums[i]; // carrying forward product encountered till now, this variable absorbs the current element so that it can be multiplied in next iteration
        }
        curr = 1;
        for (int i=nums.length-1; i>=0; i--){
            res[i] = curr * res[i];
            curr = curr * nums[i];
        }
        return res;
    }
}
