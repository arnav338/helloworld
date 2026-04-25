package arrays;

import java.util.ArrayList;
import java.util.Arrays;

public class HouseRobber {
    /*
    Mr. X is a professional robber planning to rob houses along a street. Each house has a certain amount of money hidden.



All houses along this street are arranged in a circle. That means the first house is the neighbour of the last one. Meanwhile, adjacent houses have a security system connected, and it will automatically contact the police if two adjacent houses are broken into on the same night.



You are given an array/list of non-negative integers 'ARR' representing the amount of money of each house. Your task is to return the maximum amount of money Mr. X can rob tonight without alerting the police.



Note:
It is possible for Mr. X to rob the same amount of money by looting two different sets of houses. Just print the maximum possible robbed amount, irrespective of sets of houses robbed.


For example:
(i) Given the input array arr[] = {2, 3, 2} the output will be 3 because Mr X cannot rob house 1 (money = 2) and then rob house 3 (money = 2), because they are adjacent houses. So, he’ll rob only house 2 (money = 3)

(ii) Given the input array arr[] = {1, 2, 3, 1} the output will be 4 because Mr X rob house 1 (money = 1) and then rob house 3 (money = 3).

(iii) Given the input array arr[] = {0} the output will be 0 because Mr. X has got nothing to rob.
Detailed explanation ( Input/output format, Notes, Images )
Constraints:
1 <= T <= 10
1 <= N <= 5 x 10 ^ 3
1 <= ARR[i] <= 10 ^ 9

Time limit: 1 sec.
Sample Input 1:
3
1
0
3
2 3 2
4
1 3 2 1
Sample Output 1:
0
3
4
Explanation of Input 1:
(i) Mr. X has only one house to rob, but with no money.

(ii) Mr. X cannot rob house 1 (money = 2) and then rob house 3 (money = 2), because they are adjacent houses (remember, it’s a circular street). So, he’ll rob only house 2 (money = 3) with a maximum value

(iii) Mr. X will get maximum value when he robs house 2 (money = 3) and then robs house 4 (money = 1) i.e. 4 units of money.
Sample Input 2:
3
5
1 5 1 2 6
3
2 3 5
4
1 3 2 0
Sample Output 2:
11
5
3
    * */

    public static void main(String[] args) {
        int[] arr = new int[]{6, 5, 4, 3, 2, 1, 7};
//        int[] arr = new int[]{1, 7, 14, 21, 13};

//        int[] arr = new int[]{0, 8, 4, 12, 2, 10, 6, 14, 1, 9, 5, 13, 30, 11, 7, 15};
//        int[] arr = new int[]{1, 7, 14, 21, 13};

//        long l = houseRobber(arr);
//        System.out.println("recursion = "+l);

        long l = houseRobber_topDown(arr);
        System.out.println("recursion top down = "+l);

        long k = houseRobber_topDown_withMemo(arr);
        System.out.println("recursion top down with memoization = "+k);

//        int[] a1 = new int[arr.length-1];
//        int[] a2 = new int[arr.length-1];


        long m = houseRobber_maxNonAdjacentSum(arr);
        System.out.println("recursion maxNonAdjacentSum = "+m);
    }

    private static long houseRobber_maxNonAdjacentSum(int[] valueInHouse) {
        ArrayList<Integer> a1 = new ArrayList<>();
        ArrayList<Integer> a2 = new ArrayList<>();

        for(int i=0; i<valueInHouse.length; i++){
            if(i!=valueInHouse.length-1){
                a1.add(valueInHouse[i]);
            }
            if(i!=0){
                a2.add(valueInHouse[i]);
            }
        }
        int i = maximumNonAdjacentSum_dp_topDown(a1);
        int j = maximumNonAdjacentSum_dp_topDown(a2);
        return Math.max(i,j);
    }

    public static int maximumNonAdjacentSum_dp_topDown(ArrayList<Integer> nums) {
        int sum = 0;
        int i= nums.size()-1;
        int[] dp = new int[nums.size()];
        Arrays.fill(dp,-1);
        sum = findMaxSum_dp_topDown(i,nums,sum,dp);
        return sum;
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

    private static long houseRobber_topDown_withMemo(int[] valueInHouse) {
        if(valueInHouse.length==0) return 0;
        if(valueInHouse.length==1) return valueInHouse[0];
        long res = 0L;
        int i=valueInHouse.length-1;
        boolean first = false; // depicts if 1st house is robbed
        long sum = 0;
        long[] dp = new long[valueInHouse.length];
        Arrays.fill(dp,-1);
        res = findMax_topDown_withmemo(i, valueInHouse,first,sum,dp);
        return res;
    }

    public static long findMax_topDown_withmemo(int i,int[] valueInHouse,boolean last,long sum, long[] dp){
        if(i<0) {
            return sum;
        }
        if(dp[i]!=-1){
            return dp[i];
        }
        if(i==valueInHouse.length-1){
            last = true;
        }
        if(i==0){ // check if we reach 1st element
            if(last){
                return sum;
            }else{
                return sum+valueInHouse[0];
            }
        }
        long add = findMax_topDown_withmemo(i-2,valueInHouse,last,sum+valueInHouse[i],dp);
        if(last && i==valueInHouse.length-1){
            last = false;
        }
        long notAdd = findMax_topDown_withmemo(i-1,valueInHouse,last,sum,dp);
        return dp[i] = Math.max(add,notAdd);
    }

    public static long houseRobber_topDown(int[] valueInHouse) {
        if(valueInHouse.length==0) return 0;
        if(valueInHouse.length==1) return valueInHouse[0];
        long res = 0L;
        int i=valueInHouse.length-1;
        boolean first = false; // depicts if 1st house is robbed
        long sum = 0;
        res = findMax_topDown(i, valueInHouse,first,sum);
        return res;
    }

    public static long findMax_topDown(int i,int[] valueInHouse,boolean last,long sum){
        if(i<0) {
            return sum;
        }
        if(i==valueInHouse.length-1){
            last = true;
        }
        if(i==0){ // check if we reach 1st element
            if(last){
                return sum;
            }else{
                return sum+valueInHouse[0];
            }
        }
        long add = findMax_topDown(i-2,valueInHouse,last,sum+valueInHouse[i]);
        if(last && i==valueInHouse.length-1){
            last = false;
        }
        long notAdd = findMax_topDown(i-1,valueInHouse,last,sum);

        return Math.max(add,notAdd);
    }
}
