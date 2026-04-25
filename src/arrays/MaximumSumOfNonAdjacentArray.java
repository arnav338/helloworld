package arrays;

import java.util.ArrayList;
import java.util.Arrays;

public class MaximumSumOfNonAdjacentArray {
    /*
    You are given an array/list of ‘N’ integers. You are supposed to return the maximum sum of the subsequence with the constraint that no two elements are adjacent in the given array/list.

Note:
A subsequence of an array/list is obtained by deleting some number of elements (can be zero) from the array/list, leaving the remaining elements in their original order.
Detailed explanation ( Input/output format, Notes, Images )
Constraints:
1 <= T <= 500
1 <= N <= 1000
0 <= ARR[i] <= 10^5

Where 'ARR[i]' denotes the 'i-th' element in the array/list.

Time Limit: 1 sec.
Sample Input 1:
2
3
1 2 4
4
2 1 4 9
Sample Output 1:
5
11
Explanation to Sample Output 1:
In test case 1, the sum of 'ARR[0]' & 'ARR[2]' is 5 which is greater than 'ARR[1]' which is 2 so the answer is 5.

In test case 2, the sum of 'ARR[0]' and 'ARR[2]' is 6, the sum of 'ARR[1]' and 'ARR[3]' is 10, and the sum of 'ARR[0]' and 'ARR[3]' is 11. So if we take the sum of 'ARR[0]' and 'ARR[3]', it will give the maximum sum of sequence in which no elements are adjacent in the given array/list.
Sample Input 2:
2
5
1 2 3 5 4
9
1 2 3 1 3 5 8 1 9
Sample Output 2:
8
24
Explanation to Sample Output 2:
In test case 1, out of all the possibilities, if we take the sum of 'ARR[0]', 'ARR[2]' and 'ARR[4]', i.e. 8, it will give the maximum sum of sequence in which no elements are adjacent in the given array/list.

In test case 2, out of all the possibilities, if we take the sum of 'ARR[0]', 'ARR[2]', 'ARR[4]', 'ARR[6]' and 'ARR[8]', i.e. 24 so, it will give the maximum sum of sequence in which no elements are adjacent in the given array/list.
    * */

    public static void main(String[] args) {
        //ArrayList<Integer> list = new ArrayList<>(Arrays.asList(9,9,8,2));
        //ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1,2,3));
        //ArrayList<Integer> list = new ArrayList<>(Arrays.asList(5,8,7,2));
        //ArrayList<Integer> list = new ArrayList<>(Arrays.asList(11, 55, 17, 23, 97, 11, 3, 64, 45, 25));
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(2,1,4,9));

        int i = maximumNonAdjacentSum(list);
        System.out.println("res - "+i);

        /*
        this approach does not work. since what we are trying to do is use a bottom up approach with memoization
        whereas memoization is pre-dominantly a top down approach
        and tabulation is more suited for a bottom up approach
        so whenever we want to move in a bottom up manner, use tabulation
        and whenever we want to move in a top down manner, use memoization
        * */
        int j = maximumNonAdjacentSum_dp(list);
        System.out.println("res with dp - "+j);

        int k = maximumNonAdjacentSum_dp_topDown(list);
        System.out.println("res with dp top down - "+k);

        int l = maximumNonAdjacentSum_dp_bottomUp(list);
        System.out.println("res with dp top down - "+l);
    }

    private static int maximumNonAdjacentSum_dp_bottomUp(ArrayList<Integer> nums) {
        int sum = 0;
        int i= nums.size()-1;
        int[] dp = new int[nums.size()];
        Arrays.fill(dp,-1);
        dp[0] = nums.get(0);
        for(int m=1; m<nums.size(); m++){

            int take = nums.get(m);
            if(m>1){
                take += dp[m-2];
            }
            int notTake = 0 + dp[m-1];
            dp[m] = Math.max(take,notTake);
        }
        return dp[nums.size()-1];
    }

    public static int maximumNonAdjacentSum(ArrayList<Integer> nums) {
        int sum = 0;
        int i=0;
        sum = findMaxSum(i,nums,sum);
        return sum;
    }

    public static int maximumNonAdjacentSum_dp(ArrayList<Integer> nums) {
        int sum = 0;
        int i=0;
        int[] dp = new int[nums.size()];
        Arrays.fill(dp,-1);
        sum = findMaxSum_dp(i,nums,sum,dp);
        return sum;
    }

    public static int maximumNonAdjacentSum_dp_topDown(ArrayList<Integer> nums) {
        int sum = 0;
        int i= nums.size()-1;
        int[] dp = new int[nums.size()];
        Arrays.fill(dp,-1);
        sum = findMaxSum_dp_topDown(i,nums,sum,dp);
        return sum;
    }

    public static int findMaxSum(int i,ArrayList<Integer> nums,int sum){
        if(i>=nums.size()){
            return sum;
        }
        if(i==nums.size()-1){
            return sum + nums.get(nums.size()-1);
        }
        int add = findMaxSum(i+2, nums, sum+nums.get(i));
        int notAdd = findMaxSum(i+1, nums, sum);
//        if(i==0){
//            System.out.println("max - "+add);
//        }
        return Math.max(add, notAdd);
    }

    public static int findMaxSum_dp(int i, ArrayList<Integer> nums, int sum, int[] dp){
        if(i>=nums.size()){
            return sum;
        }
        if(i==nums.size()-1){
            return sum + nums.get(nums.size()-1);
        }
        if(dp[i]!=-1){
            return dp[i];
        }
        int add = findMaxSum_dp(i+2, nums, sum+nums.get(i),dp);
        int notAdd = findMaxSum_dp(i+1, nums, sum,dp);
//        if(i==0){
//            System.out.println("max - "+add);
//        }
        dp[i] = Math.max(add, notAdd);
        return Math.max(add, notAdd);
    }

    public static int findMaxSum_dp_topDown(int i, ArrayList<Integer> nums, int sum, int[] dp){
        if(i<0){
            return 0;
        }
        if(dp[i]!=-1) {
            return dp[i];
        }
        if(i==0){
            return nums.get(0);
        }
        int add = nums.get(i) + findMaxSum_dp_topDown(i-2,nums,sum+nums.get(i),dp);
        int notAdd = findMaxSum_dp_topDown(i-1,nums,sum,dp);
        dp[i] = Math.max(add, notAdd);
        return Math.max(add, notAdd);
    }



}
