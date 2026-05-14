package Number_Theory;

public class Add_Digits {
    /*
    258. Add Digits
Solved
Easy
Topics
premium lock icon
Companies
Hint
Given an integer num, repeatedly add all its digits until the result has only one digit, and return it.



Example 1:

Input: num = 38
Output: 2
Explanation: The process is
38 --> 3 + 8 --> 11
11 --> 1 + 1 --> 2
Since 2 has only one digit, return it.
Example 2:

Input: num = 0
Output: 0


Constraints:

0 <= num <= 231 - 1

    * */

    public static void main(String[] args) {
        System.out.println(addDigits(38));
    }

    public static int addDigits(int num) {
        int temp = 0;
        while(num>0 || temp >9){
            temp += num%10;
            num = num/10;
            if(num == 0 && temp >9){
                num = temp;
                temp = 0;
            }
        }
        return temp;
    }

    /*
    Intuition
This problem asks us to repeatedly sum digits of a number until only a single digit remains. The elegant mathematical insight is that this "digital root" follows a pattern: it equals (num - 1) % 9 + 1 for positive numbers, or equivalently num % 9 with special handling for 0 and multiples of 9. This avoids iterative summation entirely.

Approach
We'll use a mathematical pattern recognition strategy:

Zero case: If num is 0, digital root is 0
Multiple of 9: If num % 9 equals 0 (and num ≠ 0), digital root is 9
General case: For all other numbers, digital root is num % 9
Mathematical property: This works because repeatedly summing digits is equivalent to finding the remainder when divided by 9
Constant time: No loops or recursion needed
This approach leverages the mathematical relationship between digital roots and modulo 9.

Complexity
Time complexity: O(1)
We perform only constant-time modulo and comparison operations.

Space complexity: O(1)
We use only constant extra space for the calculation.

Concept -

Add Digits / Digital Root — Interview Notes
Core Concept

Repeated sum of digits of a number eventually becomes its digital root.

Example:

38 → 3+8 = 11 → 1+1 = 2
Key Mathematical Property

A number and the sum of its digits have the same remainder modulo 9.

“Digital root is basically modulo 9, except multiples of 9 become 9 instead of 0.”



    * */

    public int addDigits_(int num) {
        if (num == 0)
            return 0;
        if (num % 9 == 0)
            return 9;
        return num % 9;
    }

}
