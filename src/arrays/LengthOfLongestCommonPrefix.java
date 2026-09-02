package arrays;

import java.util.HashSet;

public class LengthOfLongestCommonPrefix {
    /*
    3043. Find the Length of the Longest Common Prefix
Attempted
Medium
Topics
premium lock icon
Companies
Hint
You are given two arrays with positive integers arr1 and arr2.

A prefix of a positive integer is an integer formed by one or more of its digits, starting from its leftmost digit. For example, 123 is a prefix of the integer 12345, while 234 is not.

A common prefix of two integers a and b is an integer c, such that c is a prefix of both a and b. For example, 5655359 and 56554 have common prefixes 565 and 5655 while 1223 and 43456 do not have a common prefix.

You need to find the length of the longest common prefix between all pairs of integers (x, y) such that x belongs to arr1 and y belongs to arr2.

Return the length of the longest common prefix among all pairs. If no common prefix exists among them, return 0.



Example 1:

Input: arr1 = [1,10,100], arr2 = [1000]
Output: 3
Explanation: There are 3 pairs (arr1[i], arr2[j]):
- The longest common prefix of (1, 1000) is 1.
- The longest common prefix of (10, 1000) is 10.
- The longest common prefix of (100, 1000) is 100.
The longest common prefix is 100 with a length of 3.
Example 2:

Input: arr1 = [1,2,3], arr2 = [4,4,4]
Output: 0
Explanation: There exists no common prefix for any pair (arr1[i], arr2[j]), hence we return 0.
Note that common prefixes between elements of the same array do not count.


Constraints:

1 <= arr1.length, arr2.length <= 5 * 104
1 <= arr1[i], arr2[i] <= 108
    * */
    public int longestCommonPrefix(int[] arr1, int[] arr2) {
        int res = 0;
        HashSet<Integer> set = new HashSet<>();
        for(int i=0; i<arr1.length; i++){
            int r = arr1[i];
            set.add(r);
            while(r > 0){
                r = r/10;
                if(r != 0) set.add(r);
            }
        }
        System.out.println(set);
        for(int j = 0; j<arr2.length; j++){
            int r = arr2[j];
            if(set.contains(r)){
                res = Math.max(res, digits(r));
                continue;
            }
            while(r > 0){
                r = r/10;
                if(set.contains(r)){
                    res = Math.max(res, digits(r));
                    break;
                }
            }
        }
        return res;
    }

    public int digits(int d){
        int i = 0;
        while(d>0){
            i++;
            d = d/10;
        }
        return i;
    }
    /*
    ╔════════════════════════════════════════════════════════════╗
║       FIND LENGTH OF LONGEST COMMON PREFIX                 ║
╚════════════════════════════════════════════════════════════╝

PATTERN
─────────────────────────────────────────────────────────────
Preprocessing + HashSet + Prefix Generation + Greedy Search

PROBLEM
─────────────────────────────────────────────────────────────
Given two arrays of integers, find the maximum length of a
prefix shared by at least one number from arr1 and one number
from arr2.

Important:

    We need the best PAIR across the two arrays.

We do NOT care about prefixes shared by numbers within the
same array.


FIRST THOUGHT
─────────────────────────────────────────────────────────────
Naive approach:

    for every x in arr1
        for every y in arr2
            calculate common prefix

This checks every possible pair.

If:

    arr1.length = 50,000
    arr2.length = 50,000

then:

    50,000 × 50,000
    = 2.5 billion pairs

Too expensive.

The important question becomes:

    "Can I preprocess one array so I don't have to
     compare every possible pair?"


CORE INTUITION
─────────────────────────────────────────────────────────────
Instead of thinking:

    "Which pair has the longest common prefix?"

Think:

    "What prefixes exist anywhere in arr1?"

For:

    12345

the prefixes are:

    1
    12
    123
    1234
    12345

Store all prefixes from arr1 in a HashSet.

Then process numbers from arr2 and ask:

    "Does this prefix exist in arr1?"

This changes the problem from:

    PAIR COMPARISON

to:

    PREFIX EXISTENCE LOOKUP


KEY TRANSFORMATION
─────────────────────────────────────────────────────────────
Original problem:

    arr1 × arr2
        ↓
    compare every pair

Better:

    arr1
      ↓
    generate prefixes
      ↓
    HashSet

Then:

    arr2
      ↓
    generate prefixes
      ↓
    HashSet lookup


WHY HASHSET?
─────────────────────────────────────────────────────────────
We only need to know:

    "Does this prefix exist?"

We don't need to associate extra information with it.

Therefore:

    HashSet → correct data structure

General rule:

    Need existence?
        → Set

    Need key → associated value?
        → Map


GENERATING PREFIXES FROM AN INTEGER
─────────────────────────────────────────────────────────────
For:

    12345

repeatedly remove the last digit:

    12345
      ↓ /10
    1234
      ↓ /10
    123
      ↓ /10
    12
      ↓ /10
    1

These are exactly the prefixes, generated from:

    LONGEST → SHORTEST

This connects directly to the digit-manipulation pattern:

    x % 10 → extract last digit
    x / 10 → remove last digit


IMPORTANT GREEDY IDEA
─────────────────────────────────────────────────────────────
Suppose we're processing:

    12399

Possible prefixes:

    12399   ← length 5
    1239    ← length 4
    123     ← length 3
    12      ← length 2
    1       ← length 1

Check them in this order.

If:

    12399 → not found
    1239  → not found
    123   → FOUND

STOP.

Why?

Because we are checking:

    longest → shortest

Therefore the FIRST match is automatically the
LONGEST possible match.

This is the key greedy insight.


GREEDY MENTAL MODEL
─────────────────────────────────────────────────────────────
When looking for the maximum/best valid candidate:

    Generate candidates:
        BEST → WORST

    Check each candidate.

    FIRST VALID CANDIDATE
            ↓
         ANSWER

General question to ask in DSA:

    "Can I generate candidates in an order where
     the first valid one is automatically optimal?"


EXAMPLE
─────────────────────────────────────────────────────────────
arr1:

    [12345, 987]

Stored prefixes include:

    1
    12
    123
    1234
    12345
    9
    98
    987

Now:

    arr2 = [12399]

Candidates:

    12399 → not found
    1239  → not found
    123   → FOUND

Therefore:

    answer = 3


CLEAN OPTIMAL CODE
─────────────────────────────────────────────────────────────
class Solution {
    public int longestCommonPrefix(int[] arr1, int[] arr2) {

        // Store every prefix that appears in arr1.
        Set<Integer> prefixes = new HashSet<>();

        for (int num : arr1) {
            while (num > 0) {
                prefixes.add(num);
                num /= 10;
            }
        }

        int answer = 0;

        // For each number in arr2, generate prefixes
        // from longest to shortest.
        for (int num : arr2) {

            while (num > 0) {

                // First match is the longest possible
                // prefix for this number.
                if (prefixes.contains(num)) {
                    answer = Math.max(answer, digitCount(num));
                    break;
                }

                num /= 10;
            }
        }

        return answer;
    }

    private int digitCount(int num) {
        int count = 0;

        while (num > 0) {
            count++;
            num /= 10;
        }

        return count;
    }
}


CLEANER VERSION — AVOID RECOUNTING DIGITS
─────────────────────────────────────────────────────────────
A useful improvement is to carry the prefix length while
removing digits.

class Solution {
    public int longestCommonPrefix(int[] arr1, int[] arr2) {

        Set<Integer> prefixes = new HashSet<>();

        // Preprocess all prefixes from arr1.
        for (int num : arr1) {
            while (num > 0) {
                prefixes.add(num);
                num /= 10;
            }
        }

        int answer = 0;

        // Generate prefixes of arr2 from longest to shortest.
        for (int num : arr2) {

            int length = String.valueOf(num).length();

            while (num > 0) {

                // First match is the longest prefix.
                if (prefixes.contains(num)) {
                    answer = Math.max(answer, length);
                    break;
                }

                num /= 10;
                length--;
            }
        }

        return answer;
    }
}


NOTE ON IMPLEMENTATION
─────────────────────────────────────────────────────────────
The String.valueOf(num).length() is only used to obtain
the initial number of digits.

The actual prefix generation and comparison are still
mathematical.

You can also calculate the digit count mathematically if
you want to avoid String completely.


BRUTE FORCE
─────────────────────────────────────────────────────────────
For every pair:

    x ∈ arr1
    y ∈ arr2

convert/compare their digits.

If:

    N = arr1.length
    M = arr2.length
    D = maximum number of digits

Time:

    O(N × M × D)

With 50,000 elements in each array, this is far too large.

Space:

    O(1) auxiliary space
    apart from temporary variables.


OPTIMAL APPROACH
─────────────────────────────────────────────────────────────
Preprocess arr1:

    Each number has at most D prefixes.

Therefore:

    O(N × D)

Then process arr2:

    O(M × D)

HashSet lookup is approximately:

    O(1)

Total:

    O((N + M) × D)

Since the constraint gives numbers ≤ 10^8,
D is at most about 9 digits.

So practically this is close to:

    O(N + M)

with a small constant factor.

Space:

    O(N × D)

for the stored prefixes.


TRICKY PARTS
─────────────────────────────────────────────────────────────
1. Prefix ≠ suffix.

For:

    12345

prefixes:

    1, 12, 123, 1234, 12345

NOT:

    5, 45, 345...


2. Prefix must start from the LEFTMOST digit.

That's why:

    12345 / 10 → 1234

is useful.


3. The first match is the longest.

Only because we check:

    longest → shortest


4. We need prefixes from BOTH arrays conceptually,
   but we only need to STORE one side.

The other side can be used to query the stored prefixes.


5. Common prefixes inside arr1 don't count.

We only check:

    arr1 prefix ↔ arr2 number/prefix


6. No need to sort.

The previous Longest Common Prefix problem used sorting,
but this problem's main optimization is:

    PREPROCESS + HASHSET


INTERVIEW DERIVATION
─────────────────────────────────────────────────────────────
Step 1:

    "Brute force would compare every pair, which is
     O(N × M). With 50k × 50k, that's too expensive."


Step 2:

    "What information am I repeatedly calculating?"

Answer:

    Prefixes.


Step 3:

    "Can I preprocess all prefixes from one array?"

Yes.


Step 4:

    "I only need to know whether a prefix exists."

Therefore:

    HashSet.


Step 5:

    "How do I generate integer prefixes?"

Repeatedly:

    num /= 10


Step 6:

    "How do I search for the longest prefix?"

Generate prefixes from:

    longest → shortest.


Step 7:

    "Can I stop at the first match?"

Yes.

Because the first match is the longest possible.


Step 8:

    "Keep the maximum answer across all numbers in arr2."


IMPORTANT MENTAL MODELS
─────────────────────────────────────────────────────────────
MODEL 1 — PREPROCESSING

    Expensive repeated calculation
            ↓
       preprocess once
            ↓
       answer queries quickly


MODEL 2 — EXISTENCE QUERY

    "Have I seen X?"

        ↓

    HashSet


MODEL 3 — BEST → WORST

    If candidates can be generated in descending
    quality:

        best
        ↓
        ...
        ↓
        worst

    then:

        first valid candidate = optimal


MODEL 4 — INTEGER PREFIX

    Don't automatically convert to String.

    Think:

        num
        ↓ /10
        prefix
        ↓ /10
        shorter prefix
        ↓
        ...


MODEL 5 — DON'T CONFUSE "POSSIBILITIES" WITH "COMBINATIONS"

Generating prefixes does increase the number of items
you process.

But:

    N numbers × D prefixes

is fundamentally different from:

    N × M pairs.

This is the important complexity transformation.


WHAT CAN I GENERALIZE?
─────────────────────────────────────────────────────────────
1. When nested loops look huge, ask:

       "Can I preprocess one side?"

2. When the repeated operation is:

       "Does X exist?"

   think:

       HashSet.

3. When searching for maximum:

       "Can I generate candidates from best to worst?"

4. When dealing with numbers:

       % 10 → extract digit
       / 10 → remove digit

5. When many objects share a property:

       "Can I store the property instead of repeatedly
        comparing the objects themselves?"

This last idea is extremely important for harder DSA.


EASY THINGS TO FORGET
─────────────────────────────────────────────────────────────
☐ Don't compare every arr1 × arr2 pair.

☐ Store ALL prefixes from one array.

☐ HashSet is enough; Map isn't necessary.

☐ Generate prefixes using /10.

☐ Query from longest prefix → shortest prefix.

☐ First match = longest match for that number.

☐ Take max across all numbers in arr2.

☐ Prefix means LEFT side, not right side.

☐ Sorting is not the main idea here.


2–3 MONTH RECALL
─────────────────────────────────────────────────────────────
If you forget the solution, ask:

    1. Why can't I compare every pair?
       → N × M is too large.

    2. What am I repeatedly calculating?
       → Common prefixes.

    3. Can I preprocess prefixes from one array?
       → Yes.

    4. What data structure?
       → HashSet.

    5. How do I generate integer prefixes?
       → Repeatedly divide by 10.

    6. In what order?
       → Longest → shortest.

    7. When do I stop?
       → First HashSet match.

    8. Why can I stop?
       → First match is the longest possible.

    9. What do I do across all numbers?
       → Maintain global maximum.


ONE-LINE MEMORY HOOK
─────────────────────────────────────────────────────────────
    TOO MANY PAIRS
          ↓
    PREPROCESS PREFIXES
          ↓
    HASHSET
          ↓
    LONGEST → SHORTEST
          ↓
    FIRST MATCH = BEST
    * */
}
