package arrays;

import java.util.ArrayList;
import java.util.List;

public class FindAllPermutations_03_09 {
    /*
    Given an array nums of distinct integers, return all the possible permutations. You can return the answer in any order.



Example 1:

Input: nums = [1,2,3]
Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
Example 2:

Input: nums = [0,1]
Output: [[0,1],[1,0]]
Example 3:

Input: nums = [1]
Output: [[1]]

    * */

    public static void main(String[] args) {
        int[] arr = new int[]{1,2,3};
        List<List<Integer>> permute = permute(arr);
        System.out.println(permute.size()+" , answer -> "+permute);
        String s = "abc";
        char[][] ini = new char[2][2];
        s += ini[0][0];
        String j = s.replace(s.charAt(0), 'j');
    }

    /*
    at every step we try to swap the current index element with all elements to its right including itself

    when we reach the end of array, we add to result list
    * */
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        calculatePermutations(0,nums,res);
        return res;
    }

    public static void calculatePermutations(int currentIndex, int[] nums, List<List<Integer>> res){
        if(currentIndex == nums.length-1){
            List<Integer> l = new ArrayList<>();
            for(int j=0; j<nums.length; j++){
                l.add(nums[j]);
            }
            res.add(l);
        }
        for(int i=currentIndex; i<nums.length; i++){
            swap(nums,i,currentIndex);
            calculatePermutations(currentIndex+1,nums,res);
            swap(nums,i,currentIndex);
        }
    }

    public static void swap(int[] nums, int i, int j){
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
