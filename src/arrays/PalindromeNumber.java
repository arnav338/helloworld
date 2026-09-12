package arrays;

public class PalindromeNumber {

    /*
    Given an integer x, return true if x is a palindrome, and false otherwise.



Example 1:

Input: x = 121
Output: true
Explanation: 121 reads as 121 from left to right and from right to left.
Example 2:

Input: x = -121
Output: false
Explanation: From left to right, it reads -121. From right to left, it becomes 121-. Therefore it is not a palindrome.
Example 3:

Input: x = 10
Output: false
Explanation: Reads 01 from right to left. Therefore it is not a palindrome.


Constraints:

-231 <= x <= 231 - 1


Follow up: Could you solve it without converting the integer to a string?
    * */


    public boolean isPalindrome(int x) {
        if(x < 0) return false;
        int t = x;
        int rev = 0;
        while(t>0){
            int d = t%10;
            rev = (rev * 10) + d;
            t = t/10;
        }
        return (x-rev)==0;
    }

    /*
    ╔════════════════════════════════════════════════════════════╗
║                  PALINDROME NUMBER                         ║
╚════════════════════════════════════════════════════════════╝

PATTERN
─────────────────────────────────────────────────────────────
Number Manipulation + Digit Extraction

PROBLEM
─────────────────────────────────────────────────────────────
Check whether an integer reads the same forward and backward.

Examples:
    121  → true
    1221 → true
    123  → false
    -121 → false
    10   → false


FIRST THOUGHT
─────────────────────────────────────────────────────────────
Two obvious approaches:

1. Convert number → String
   Check characters from both ends.

   Time  → O(n)
   Space → O(n)

2. Don't use String.
   Reverse the digits mathematically and compare
   the reversed number with the original.

The second approach satisfies the follow-up.


CORE INTUITION
─────────────────────────────────────────────────────────────
Extract digits from right → left using:

    x % 10 → last digit
    x / 10 → remove last digit

Build the reversed number:

    rev = rev * 10 + digit

Example:

    x = 121

    digit = 1 → rev = 1
    digit = 2 → rev = 12
    digit = 1 → rev = 121

Then:

    original == reversed
    121 == 121 → true


KEY DIGIT MANIPULATION PATTERN
─────────────────────────────────────────────────────────────
Given:

    x = 1234

Last digit:

    x % 10 = 4

Remove last digit:

    x / 10 = 123

Therefore:

    digit = x % 10
    x = x / 10

This is a fundamental pattern for integer/digit problems.


HOW TO BUILD A REVERSED NUMBER
─────────────────────────────────────────────────────────────
Suppose:

    digits = 1 → 2 → 3

Start:

    rev = 0

Take 1:

    rev = 0 * 10 + 1
        = 1

Take 2:

    rev = 1 * 10 + 2
        = 12

Take 3:

    rev = 12 * 10 + 3
        = 123

Therefore:

    rev = rev * 10 + digit


STATE
─────────────────────────────────────────────────────────────
We need:

    t   → remaining part of the number
    rev → number constructed so far

Original x must be preserved because t is modified.

    int t = x;


IMPORTANT EDGE CASES
─────────────────────────────────────────────────────────────
1. Negative numbers

    -121

Cannot be a palindrome because the '-' sign
doesn't have a matching '-' at the other end.

Therefore:

    if (x < 0) return false;


2. Numbers ending in 0

    10

Reverse is:

    01 → effectively 1

So:

    10 != 1

false.

More generally, a positive number ending in 0 cannot
be a palindrome unless the number is 0.


3. x = 0

Your current code handles this correctly:

    rev = 0
    loop doesn't execute
    x == rev

    0 == 0 → true


CLEAN OPTIMAL CODE
─────────────────────────────────────────────────────────────
class Solution {
    public boolean isPalindrome(int x) {

        // Negative numbers cannot be palindromes.
        if (x < 0) {
            return false;
        }

        // Keep the original number because we modify t.
        int original = x;
        int reversed = 0;

        // Extract digits from right to left.
        while (x > 0) {

            int digit = x % 10;

            // Append digit to the reversed number.
            reversed = reversed * 10 + digit;

            // Remove the last digit.
            x = x / 10;
        }

        // Palindrome if original and reversed are identical.
        return original == reversed;
    }
}


WHY YOUR CODE WORKS
─────────────────────────────────────────────────────────────
Your approach:

    t = x

keeps the original safe.

Then:

    d = t % 10

extracts the last digit.

Then:

    rev = rev * 10 + d

builds the reversed number.

Then:

    t = t / 10

removes the processed digit.

Finally:

    x == rev

would be the cleanest comparison.

Your:

    return (x - rev) == 0;

is logically correct, but:

    return x == rev;

is clearer and directly expresses the intention.


BRUTE FORCE VS OPTIMAL
─────────────────────────────────────────────────────────────
String approach:

    Integer → String → compare characters

    Time  → O(n)
    Space → O(n)

Mathematical reversal:

    Integer → reverse digits → compare

    Time  → O(n)
    Space → O(1)

where n = number of digits.


INTERVIEW DERIVATION
─────────────────────────────────────────────────────────────
Step 1:
    "I need to check whether the number reads the same
     forward and backward."

Step 2:
    "The straightforward solution is converting it to a
     string, but the follow-up asks me not to."

Step 3:
    "I can reverse the number using digit extraction."

Step 4:
    "x % 10 gives me the last digit."

Step 5:
    "x / 10 removes that digit."

Step 6:
    "I can construct the reversed number using:

        reversed = reversed * 10 + digit"

Step 7:
    "Then compare the reversed number with the original."

Step 8:
    "I need to handle negative numbers separately."


EASY THINGS TO FORGET
─────────────────────────────────────────────────────────────
☐ Preserve the original number before modifying it.

☐ `% 10` → extract last digit.

☐ `/ 10` → remove last digit.

☐ `rev * 10 + digit` → append digit to reversed number.

☐ Negative numbers → false.

☐ `0` → true.

☐ Positive numbers ending in `0` → false.

☐ Compare using:
      original == reversed


IMPORTANT MENTAL MODELS
─────────────────────────────────────────────────────────────
MODEL 1 — DIGIT EXTRACTION

    % 10 → take last digit
    / 10 → remove last digit

This should become automatic for integer problems.


MODEL 2 — BUILDING A NUMBER

When processing digits one at a time:

    result = result * 10 + digit

This means:
    shift existing digits left
    then add the new digit.


MODEL 3 — PRESERVE WHAT YOU NEED

If a variable is going to be destroyed during processing,
keep a copy of the original.

    original = x
    x = modified


MODEL 4 — FOLLOW-UP QUESTIONS OFTEN CHANGE THE TECHNIQUE

Without the follow-up:

    String conversion is easiest.

With:
    "without converting to String"

Think:

    What mathematical property am I checking?

Palindrome means:

    number == reverse(number)

So digit manipulation becomes natural.


WHAT CAN I GENERALIZE?
─────────────────────────────────────────────────────────────
1. Integer → digit-by-digit processing

Whenever you see:

    integer
    digits
    reverse
    last digit
    digit sum
    digit count

immediately think:

    % 10
    / 10


2. Reversing digits

    rev = rev * 10 + digit


3. Destroying a copy

If processing modifies the input:

    original = input
    temp = input

Use temp for traversal.


4. Mathematical reformulation

Instead of asking:

    "How do I check this string?"

Ask:

    "What mathematical property defines the answer?"

Palindrome:

    original == reverse


COMPLEXITY
─────────────────────────────────────────────────────────────
Let n = number of digits.

Time:
    O(n)

Space:
    O(1)

Only a few integer variables are used.


2–3 MONTH RECALL
─────────────────────────────────────────────────────────────
If you forget the solution, ask yourself:

    1. What does palindrome mean?
       → Same forward and backward.

    2. Follow-up says no String. What can I do?
       → Reverse the integer mathematically.

    3. How do I get the last digit?
       → x % 10

    4. How do I remove the last digit?
       → x / 10

    5. How do I construct the reverse?
       → rev = rev * 10 + digit

    6. What do I compare?
       → original == reversed

    7. What edge case comes first?
       → negative → false


ONE-LINE MEMORY HOOK
─────────────────────────────────────────────────────────────
    PALINDROME NUMBER
          ↓
    original == reverse
          ↓
    digit = x % 10
    x = x / 10
    rev = rev * 10 + digit
    * */
}
