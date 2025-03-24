package stack;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class NextGreaterElement {
    /*
    The next greater element of some element x in an array is the first greater element that is to the right of x in the same array.

You are given two distinct 0-indexed integer arrays nums1 and nums2, where nums1 is a subset of nums2.

For each 0 <= i < nums1.length, find the index j such that nums1[i] == nums2[j] and determine the next greater element of nums2[j] in nums2. If there is no next greater element, then the answer for this query is -1.

Return an array ans of length nums1.length such that ans[i] is the next greater element as described above.



Example 1:

Input: nums1 = [4,1,2], nums2 = [1,3,4,2]
Output: [-1,3,-1]
Explanation: The next greater element for each value of nums1 is as follows:
- 4 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
- 1 is underlined in nums2 = [1,3,4,2]. The next greater element is 3.
- 2 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
Example 2:

Input: nums1 = [2,4], nums2 = [1,2,3,4]
Output: [3,-1]
Explanation: The next greater element for each value of nums1 is as follows:
- 2 is underlined in nums2 = [1,2,3,4]. The next greater element is 3.
- 4 is underlined in nums2 = [1,2,3,4]. There is no next greater element, so the answer is -1.


Constraints:

1 <= nums1.length <= nums2.length <= 1000
0 <= nums1[i], nums2[i] <= 104
All integers in nums1 and nums2 are unique.
All the integers of nums1 also appear in nums2.
    * */


    public static void main(String[] args) {
        int[] nums1 = new int[]{4,1,2};
        int[] nums2 = new int[]{1,3,4,2};
        int[] res = nextGreaterElement(nums1,nums2);
        for(int i : res){
            System.out.print(i+",");
        }
    }

    /*
    TC -> O(2*N)
    SC -> O(N) for stack + O(N) for returning the answer

    TC is O(2*N) and not O(N*N) because although the external for loop
    is running N times but the internal while loop runs only for at max N times
    because the stack.pop operation can not be performed more than N times
    We can not remove more elements from stack than we have in the array
    so O(N) for for loop and O(n) for while loop

    * */
    public static int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Stack<Integer> stack = new Stack<>();
        int[] res = new int[nums2.length];
        int[] res1 = new int[nums1.length];
        Map<Integer,Integer> map = new HashMap<>();
        for(int i=nums2.length-1; i>=0 ;i--){
            map.put(nums2[i],i);
            // System.out.println(" -> "+nums2[i]);
            if(stack.empty()){
                //System.out.println("empty");
                res[i] = -1;
                stack.add(nums2[i]);
            }
            else if(stack.peek()<nums2[i]){
                while(!stack.empty() && stack.peek() < nums2[i]){
                    stack.pop();
                }
                if(stack.empty()){
                    //System.out.println("empty stack");
                    res[i] = -1;
                    stack.add(nums2[i]);
                }else{
                    //System.out.println("- peek - "+stack.peek());
                    res[i] = stack.peek();
                    stack.add(nums2[i]);
                }
            }else{
                //System.out.println("- peek1 - "+stack.peek());
                res[i] = stack.peek();
                stack.add(nums2[i]);
            }
        }
        for(int j=0;j<nums1.length;j++){
            res1[j] = res[map.get(nums1[j])];
            //System.out.print();
        }
        return res1;
    }
}
