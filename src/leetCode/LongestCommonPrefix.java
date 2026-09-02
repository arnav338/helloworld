package leetCode;

import java.util.Arrays;

public class LongestCommonPrefix {
    /*
    Write a function to find the longest common prefix string amongst an array of strings.

If there is no common prefix, return an empty string "".



Example 1:

Input: strs = ["flower","flow","flight"]
Output: "fl"
Example 2:

Input: strs = ["dog","racecar","car"]
Output: ""
Explanation: There is no common prefix among the input strings.
    * */

    public static void main(String[] args) {
        String[] st = new String[]{"flower","flow","flight"};
        System.out.println("res - "+longestCommonPrefix(st));
    }

    /*
    Approach
This code is used to find the longest common prefix of an array of strings, which is defined as the longest string that is a prefix of all the strings in the array. By sorting the array and then comparing the first and last elements, the code is able to find the common prefix that would be shared by all strings in the array.

Sort the elements of an array of strings called "strs" in lexicographic (alphabetical) order using the Arrays.sort(strs) method.
Assign the first element of the sorted array (the lexicographically smallest string) to a string variable s1.
Assign the last element of the sorted array (the lexicographically largest string) to a string variable s2.
Initialize an integer variable idx to 0.
Start a while loop that continues while idx is less than the length of s1 and s2.
Within the while loop, check if the character at the current index in s1 is equal to the character at the same index in s2. If the characters are equal, increment the value of idx by 1.
If the characters are not equal, exit the while loop.
Return the substring of s1 that starts from the first character and ends at the idxth character (exclusive).
Complexity
Time complexity:
Sorting the array of strings takes O(Nlog(N)) time. This is because most of the common sorting algorithms like quicksort, mergesort, and heapsort have an average time complexity of O(Nlog(N)).
Iterating over the characters of the first and last strings takes O(M) time. This is because the code compares the characters of the two strings until it finds the first mismatch.
Therefore, the total time complexity is O(Nlog(N) + M).

Space complexity:
The space used by the two string variables s1 and s2 is proportional to the length of the longest string in the array. Therefore, the space complexity is O(1) as it does not depend on the size of the input array.

Reason for Sorting
The reason why we sort the input array of strings and compare the first and last strings is that
the longest common prefix of all the strings must be a prefix of the first string and a prefix of the last string in the sorted array.
This is because strings are ordered based on their alphabetical order (Lexicographical order).
For example, consider the input array of strings {"flower", "flow", "flight"}. After sorting the array, we get {"flight", "flow", "flower"}.
The longest common prefix of all the strings is "fl", which is located at the beginning of the first string "flight"
and the second string "flow".
Therefore, by comparing the first and last strings of the sorted array, we can easily find the longest common prefix.
    * */
    public static String longestCommonPrefix(String[] strs) {
        Arrays.sort(strs);
        String s1 = strs[0];
        String s2 = strs[strs.length-1];
        int idx = 0;
        while(idx < s1.length() && idx < s2.length()){
            if(s1.charAt(idx) == s2.charAt(idx)){
                idx++;
            } else {
                break;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.reverse();
        sb = new StringBuilder();
        sb.toString().trim();
        return s1.substring(0, idx);
    }
    /*
    ╔════════════════════════════════════════════════════════════╗
║                 LONGEST COMMON PREFIX                      ║
╚════════════════════════════════════════════════════════════╝

PATTERN
─────────────────────────────────────────────────────────────
String Comparison + Sorting + Character Traversal

PROBLEM
─────────────────────────────────────────────────────────────
Find the longest prefix shared by ALL strings.

Example:

    ["flower", "flow", "flight"]

Common:
    flower
    flow
    flight
    ↓
    "fl"


FIRST THOUGHTS
─────────────────────────────────────────────────────────────
The key word is:

    PREFIX

A prefix always starts from index 0.

So we only care about:

    strs[i].charAt(0)
    strs[i].charAt(1)
    strs[i].charAt(2)
    ...

A simple approach is:

    Take the first string as the reference.
    Compare its characters with every other string.

This works in:

    Time  → O(N × M)
    Space → O(1)

where:
    N = number of strings
    M = length of the shortest string.


BRUTE FORCE / DIRECT APPROACH
─────────────────────────────────────────────────────────────
Take:

    prefix = strs[0]

For every other string:

    Keep comparing characters while they match.

If mismatch occurs:
    shorten prefix.

Example:

    flower
    flow

Common:
    flow

Then:

    flow
    flight

Common:
    fl

Final:
    fl

Complexity:

    Time  → O(N × M)
    Space → O(1)

This is already a perfectly good solution.

However, there is an interesting sorting observation
that gives a very clean solution.


CORE INTUITION — SORTING
─────────────────────────────────────────────────────────────
Sort the strings lexicographically.

Example:

    ["flower", "flow", "flight"]

After sorting:

    ["flight", "flow", "flower"]

Now look at:

    FIRST string
    LAST string

    flight
    flower

Why is this enough?

Because lexicographical sorting places strings with similar
prefixes next to each other.

The longest common prefix of ALL strings must also be a
common prefix of the lexicographically smallest and largest
strings.

Therefore:

    LCP(all strings)
          =
    LCP(first string, last string after sorting)


KEY MENTAL MODEL
─────────────────────────────────────────────────────────────
Think of sorting as putting strings on a line:

    car
    cat
    dog
    door
    dolphin

The strings at the two extremes determine how far the
entire group can agree from the beginning.

If the first and last strings disagree at some position,
the entire collection cannot possibly share that character.


WHY ONLY FIRST AND LAST?
─────────────────────────────────────────────────────────────
Suppose after sorting:

    [smallest ... middle ... largest]

If smallest and largest agree on:

    characters 0 → k

then every string between them must also have that same
prefix.

If smallest and largest differ at position k:

    smallest[k] != largest[k]

then there is definitely no common prefix beyond k.

Therefore we only need:

    s1 = sorted[0]
    s2 = sorted[n - 1]


IMPORTANT OBSERVATION
─────────────────────────────────────────────────────────────
The problem is NOT:

    "Find which characters appear in all strings."

It is:

    "Find the longest sequence of characters,
     starting at index 0, that is identical everywhere."

That's why character comparison stops at the FIRST mismatch.


ALGORITHM
─────────────────────────────────────────────────────────────
1. Sort the array.

2. Take:
       s1 = first string
       s2 = last string

3. Compare:
       s1[0] with s2[0]
       s1[1] with s2[1]
       ...

4. Stop when:
       - characters differ, OR
       - one string ends.

5. Return the substring from:
       0 → mismatch index.


YOUR SOLUTION
─────────────────────────────────────────────────────────────
Arrays.sort(strs);

String s1 = strs[0];
String s2 = strs[strs.length - 1];

int idx = 0;

while (idx < s1.length() && idx < s2.length()) {

    if (s1.charAt(idx) == s2.charAt(idx)) {
        idx++;
    } else {
        break;
    }
}

return s1.substring(0, idx);


This is correct.


CLEAN OPTIMAL CODE
─────────────────────────────────────────────────────────────
class Solution {
    public String longestCommonPrefix(String[] strs) {

        // Sort lexicographically.
        // The common prefix of all strings is determined
        // by the smallest and largest strings.
        Arrays.sort(strs);

        String first = strs[0];
        String last = strs[strs.length - 1];

        int i = 0;

        // Compare characters until the first mismatch
        // or until one string ends.
        while (i < first.length()
                && i < last.length()
                && first.charAt(i) == last.charAt(i)) {

            i++;
        }

        return first.substring(0, i);
    }
}


WHY THIS CODE IS CLEANER
─────────────────────────────────────────────────────────────
Instead of:

    if (...)
        i++;
    else
        break;

we put the stopping condition directly into the while loop:

    while (
        still within first &&
        still within last &&
        characters match
    )

This makes the loop read almost like English:

    "Keep going while both strings have characters
     and those characters are equal."


TRICKY PARTS
─────────────────────────────────────────────────────────────
1. Empty string

Example:

    ["", "abc"]

After sorting:

    ""
    "abc"

The comparison loop doesn't execute.

Answer:

    ""

Correct.


2. Single string

Example:

    ["flower"]

After sorting:

    first = "flower"
    last  = "flower"

Everything matches.

Answer:

    "flower"


3. No common prefix

    ["dog", "racecar", "car"]

After sorting:

    ["car", "dog", "racecar"]

Compare:

    c vs r

Mismatch immediately.

Answer:

    ""


4. One string is a prefix of another

    ["flower", "flow"]

Compare:

    f = f
    l = l
    o = o
    w = w

Then "flow" ends.

Return:

    "flow"

This is why the length check is important.


EASY THINGS TO FORGET
─────────────────────────────────────────────────────────────
☐ Prefix always starts at index 0.

☐ Stop at the FIRST mismatch.

☐ If one string ends, stop.

☐ Empty string immediately means answer is "".

☐ Sorting changes the array order.

☐ Only first and last strings are needed AFTER sorting.

☐ substring(0, idx) does NOT include idx.

☐ Don't confuse prefix with subsequence:
      prefix → contiguous + starts at index 0
      subsequence → can skip characters.


INTERVIEW DERIVATION
─────────────────────────────────────────────────────────────
Step 1:

    "Since this is a prefix, comparison always starts
     from index 0."


Step 2:

    "I could compare the first string against every other
     string, but I can simplify this using sorting."


Step 3:

    "After lexicographically sorting, the smallest and
     largest strings determine the common prefix of the
     entire collection."


Step 4:

    "So I'll compare only those two strings character
     by character."


Step 5:

    "I'll stop at the first mismatch or when either string
     ends."


Step 6:

    "The matched portion from index 0 is the answer."


TIME COMPLEXITY
─────────────────────────────────────────────────────────────
Sorting:

    O(N log N)

String comparisons during sorting can involve characters,
so more precisely it can depend on string lengths.

After sorting:

    O(M)

for comparing first and last strings.

For typical interview analysis:

    Time → O(N log N × M)

where:
    N = number of strings
    M = maximum string length.

Space:
    O(log N) auxiliary stack/implementation-dependent
    for Java's sorting implementation, plus the array itself
    being sorted in place.


ALTERNATIVE DIRECT SOLUTION
─────────────────────────────────────────────────────────────
There is no requirement to sort.

We can simply compare the first string with every other string.

Conceptually:

    prefix = strs[0]

    for every string:
        compare characters from index 0
        stop at mismatch

Complexity:

    Time  → O(N × M)
    Space → O(1)

This is arguably the more direct algorithm because it
doesn't introduce sorting just to identify two extremes.

The sorting solution is valuable because it teaches an
important general technique:

    SORT → EXTREMES CAN SOMETIMES REPRESENT THE WHOLE SET


WHAT CAN I GENERALIZE?
─────────────────────────────────────────────────────────────
1. SORTING AS A PROBLEM-SOLVING TOOL

Don't think of sorting only as:

    "I need the elements in order."

Sometimes sorting exposes structure.

Here:

    unsorted strings
          ↓
       sort
          ↓
    smallest + largest
          ↓
    determine common prefix


2. EXTREMES CAN REPRESENT A GROUP

Whenever a problem involves:

    minimum ↔ maximum
    lexicographically smallest ↔ largest
    earliest ↔ latest

ask:

    "Can the two extremes tell me something about
     everything in between?"


3. PREFIX PROBLEMS START AT ZERO

If the problem says:

    prefix

immediately think:

    index 0 → 1 → 2 → ...

and stop at the first mismatch.


4. STOP CONDITIONS ARE PART OF THE ALGORITHM

The answer isn't found by continuing indefinitely.

We stop when:

    mismatch
    OR
    string ends

Learning to identify the exact stopping condition
is a core skill in string/array problems.


5. SIMPLE PROBLEMS CAN HIDE A USEFUL PATTERN

The actual implementation is tiny.

The important learning isn't the code.

It's:

    SORT
      ↓
    identify useful extremes
      ↓
    compare only what matters


2–3 MONTH RECALL
─────────────────────────────────────────────────────────────
If you forget the solution, ask:

    1. What does "prefix" mean?
       → Starts at index 0.

    2. What's the simplest approach?
       → Compare characters across all strings.

    3. Can sorting simplify the problem?
       → Yes.

    4. After sorting, which strings matter?
       → First and last.

    5. Why?
       → They are lexicographical extremes;
         their common prefix bounds the common prefix
         of the entire set.

    6. How do I compare?
       → Character-by-character from index 0.

    7. When do I stop?
       → First mismatch or either string ends.

    8. What do I return?
       → first.substring(0, mismatchIndex).


ONE-LINE MEMORY HOOK
─────────────────────────────────────────────────────────────
    LONGEST COMMON PREFIX

    SORT
      ↓
    FIRST + LAST
      ↓
    COMPARE FROM INDEX 0
      ↓
    STOP AT MISMATCH
      ↓
    substring(0, index)
    * */
}
