package arrays;

public class SubArraysWithOddSum {
    /*
    Given an array of integers arr, return the number of subarrays with an odd sum.

Since the answer can be very large, return it modulo 109 + 7.



Example 1:

Input: arr = [1,3,5]
Output: 4
Explanation: All subarrays are [[1],[1,3],[1,3,5],[3],[3,5],[5]]
All sub-arrays sum are [1,4,9,3,8,5].
Odd sums are [1,9,3,5] so the answer is 4.
Example 2:

Input: arr = [2,4,6]
Output: 0
Explanation: All subarrays are [[2],[2,4],[2,4,6],[4],[4,6],[6]]
All sub-arrays sum are [2,6,12,4,10,6].
All sub-arrays have even sum and the answer is 0.
Example 3:

Input: arr = [1,2,3,4,5,6,7]
Output: 16
    * */

    public static void main(String[] args) {
        int[] arr = new int[]{1,3,5};
        int res = numOfSubarrays(arr);
        System.out.println("res - "+res);
    }

    /*
    https://www.youtube.com/watch?v=svvIB5pPc2E



    we use concept of prefix sums in the question

    while iterating the array we keep track of the sum encountered till now
    along with that, we also keep count of 2 types of array -
    a. ones with odd sum
    b, ones with even sum

    now we know that

    1. even + odd = odd
    2. even + even = even
    3. odd + odd = even

    from equation 1, if we try to get value of odd = odd - even
    from equation 3, odd = even - odd

    from this we can conclude that while iterating the array, if the sum at any point is odd, then another odd subarray can be obtained by subtracting
    all of even subarrays found till now

                       odd                              =    odd            -       even
   (number of arrays with odd sum that can be obtained)    ( sum till now)     (even arrays found till now)


   one thing to note here is that apart from the result obtained on left side of above equation, we also have the current array whose sum is odd
   hence we add 1 additionally to the answer

   similarly if sum till now is even

                       odd                              =    even            -       odd
   (number of arrays with odd sum that can be obtained)    ( sum till now)     (odd arrays found till now)


    * */
    public static int numOfSubarrays(int[] arr) {
        long res = 0L;
        int oddSum = 0;
        int evenSum = 0;
        int sum = 0;

        for(int a : arr){
            sum += a;
            if(sum % 2 == 0){
                res += oddSum;
                evenSum++;
            }else{
                res += evenSum + 1;
                oddSum++;
            }
        }
        return (int)(res % 1000000007);
    }
}
