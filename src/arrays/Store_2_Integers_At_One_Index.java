package arrays;

public class Store_2_Integers_At_One_Index {
	/*
	 * 
	Given a zero-based permutation nums (0-indexed), build an array ans of the same length
	 where ans[i] = nums[nums[i]] for each 0 <= i < nums.length and return it.
	
	A zero-based permutation nums is an array of distinct integers from 0 to nums.length - 1 (inclusive).
	
	 
	
	Example 1:
	
	Input: nums = [0,2,1,5,3,4]
	Output: [0,1,2,4,5,3]
	Explanation: The array ans is built as follows: 
	ans = [nums[nums[0]], nums[nums[1]], nums[nums[2]], nums[nums[3]], nums[nums[4]], nums[nums[5]]]
	    = [nums[0], nums[2], nums[1], nums[5], nums[3], nums[4]]
	    = [0,1,2,4,5,3]
	Example 2:
	
	Input: nums = [5,0,1,2,3,4]
	Output: [4,5,0,1,2,3]
	Explanation: The array ans is built as follows:
	ans = [nums[nums[0]], nums[nums[1]], nums[nums[2]], nums[nums[3]], nums[nums[4]], nums[nums[5]]]
	    = [nums[5], nums[0], nums[1], nums[2], nums[3], nums[4]]
	    = [4,5,0,1,2,3]
	 
	
	Constraints:
	
	1 <= nums.length <= 1000
	0 <= nums[i] < nums.length
	The elements in nums are distinct.
	 
	
	Follow-up: Can you solve it without using an extra space (i.e., O(1) memory)?
	 * 
	 * */
	
	public static void main(String[] args) {
		buildArray(new int[] {5,0,1,2,3,4});
	}
	public static int[] buildArray(int[] nums) {
		 // this is done to keep both old and new value together. 
        // going by the example of [5,0,1,2,3,4]
        // after this nums[0] will be 5 + 6*(4%6) = 5 + 24 = 29;
        // now for next index calulation we might need the original value of num[0] which can be obtain by num[0]%6 = 29%6 = 5;
        // if we want to get just he new value of num[0], we can get it by num[0]/6 = 29/6 = 4
        int k = nums.length;
        
        for(int i=0; i < k; i++){
            nums[i] = nums[i] + k*(nums[nums[i]]%k); // we are taking mod with k to make sure result does not overflow
        }
        for(int j=0; j < k; j++){
            nums[j] = nums[j]/k;
        }
        return nums;
    }
}
