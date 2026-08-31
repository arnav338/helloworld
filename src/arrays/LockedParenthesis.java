package arrays;

public class LockedParenthesis {
    /*
 A parentheses string is a non-empty string consisting only of '(' and ')'. It is valid if any of the following conditions is true:

It is ().
It can be written as AB (A concatenated with B), where A and B are valid parentheses strings.
It can be written as (A), where A is a valid parentheses string.
You are given a parentheses string s and a string locked, both of length n. locked is a binary string consisting only of '0's and '1's. For each index i of locked,

If locked[i] is '1', you cannot change s[i].
But if locked[i] is '0', you can change s[i] to either '(' or ')'.
Return true if you can make s a valid parentheses string. Otherwise, return false.



Example 1:


Input: s = "))()))", locked = "010100"
Output: true
Explanation: locked[1] == '1' and locked[3] == '1', so we cannot change s[1] or s[3].
We change s[0] and s[4] to '(' while leaving s[2] and s[5] unchanged to make s valid.
Example 2:

Input: s = "()()", locked = "0000"
Output: true
Explanation: We do not need to make any changes because s is already valid.
Example 3:

Input: s = ")", locked = "0"
Output: false
Explanation: locked permits us to change s[0].
Changing s[0] to either '(' or ')' will not make s valid.
Example 4:

Input: s = "(((())(((())", locked = "111111010111"
Output: true
Explanation: locked permits us to change s[6] and s[8].
We change s[6] and s[8] to ')' to make s valid.


Constraints:

n == s.length == locked.length
1 <= n <= 105
s[i] is either '(' or ')'.
locked[i] is either '0' or '1'.
    * */

    public boolean canBeValid(String s, String locked) {
        if(s.length() % 2 != 0) return false;
        // Minimum and maximum possible balance
        // after processing the current prefix.
        int minBalance = 0;
        int maxBalance = 0;

        for (int i = 0; i < s.length(); i++) {

            if (locked.charAt(i) == '1') {

                // Character cannot be changed.
                if (s.charAt(i) == '(') {
                    minBalance++;
                    maxBalance++;
                } else {
                    minBalance--;
                    maxBalance--;
                }

            } else {

                // Character can become either '(' or ')'.
                //
                // To get the minimum balance, choose ')'.
                minBalance--;

                // To get the maximum balance, choose '('.
                maxBalance++;
            }

            // If even the minimum possible balance is negative,
            // every possible prefix has become invalid.
            if (maxBalance < 0) {
                return false;
            }

            // Negative balances represent invalid paths.
            // We only keep the minimum VALID balance.
            minBalance = Math.max(0, minBalance);
        }

        // At the end, we need some valid configuration
        // whose balance is exactly 0.
        return minBalance == 0;

    }
    /*
    LOCKED PARENTHESES — KEY IDEA

Pattern:
    Greedy / State Compression / Range of possible states

Don't track:
    Exact sequence of choices

Track:
    Possible balance range [minBalance, maxBalance]

Balance:
    '(' → +1
    ')' → -1

Rules:
    balance can never be negative
    final balance must be 0

Locked '(':
    min++, max++

Locked ')':
    min--, max--

Unlocked:
    min--     // choose ')' for minimum
    max++     // choose '(' for maximum

If maxBalance < 0:
    impossible → false

After each position:
    minBalance = max(0, minBalance)

At end:
    minBalance == 0 → true
    otherwise → false

Core lesson:
    Don't track every possible decision.
    Find the smallest state that represents
    all relevant possibilities.

Mental trigger:
    "Many choices → many states → can I
     compress those states?"
    * */
}
