package leetCode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LengthOfLongestSubstring {
	/*
	 * Given a string s, find the length of the longest substring without repeating characters.

		Example 1:
		
		Input: s = "abcabcbb"
		Output: 3
		Explanation: The answer is "abc", with the length of 3.
		Example 2:
		
		Input: s = "bbbbb"
		Output: 1
		Explanation: The answer is "b", with the length of 1.
		Example 3:
		
		Input: s = "pwwkew"
		Output: 3
		Explanation: The answer is "wke", with the length of 3.
		Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
	 * */
	public static void main(String[] args) {
		System.out.println("-> "+getLengthOfLongestSubstring("abba"));

//		var a = "a";
//		if(a instanceof String){
//			System.out.println("true");
//		}
	}
	public static int getLengthOfLongestSubstring(String input) {
		int len=0;
		char[] temp = input.toCharArray();
		
		int left=0;
		int right=0;
		HashMap<Character,Integer> map = new HashMap<>();
		while(right < temp.length) {
			if(map.getOrDefault(temp[right], -1)==-1) { // not appeared previously
				map.put(temp[right], right);
			}
			else {
				left = Math.max(map.get(temp[right])+1,left);// move the left pointer such that the repeating element's index is passed and hence we avoid the repeating character
				map.put(temp[right], right);
			}
			len = Math.max(len, (right-left+1));
			right++;
		}
		return len;
	}

	public int lengthOfLongestSubstring(String s) {
		if(s.length()==0 || s.length()==1) return s.length();
		int left = 0, right = 0;
		int len = 0;
		Set<Character> set = new HashSet<>();
		while(right<s.length()){
			if(set.contains(s.charAt(right))){
				// duplicate element encountered
				while(set.contains(s.charAt(right))){
					// remove elements till the duplicate character is removed from the set
					set.remove(s.charAt(left));
					left++;
				}
			}else{
				len = Math.max(len,(right-left+1));
			}
			set.add(s.charAt(right));
			right++;
		}
		return len;
	}
	/*
	LEETCODE 3 — LONGEST SUBSTRING WITHOUT REPEATING CHARACTERS

Pattern:
Sliding Window + HashSet

Core Idea:
Find the longest contiguous substring containing only unique characters.
Instead of checking every substring from scratch, maintain a valid window
using two pointers: left and right.

Thought Process:
1. "Substring" means the answer is contiguous.
2. "Longest" contiguous range + a condition → think Sliding Window.
3. Condition here = no character should repeat.
4. HashSet can tell us whether a character already exists in the window.
5. Expand using right.
6. If duplicate appears, move left forward and remove characters until valid.
7. Once valid, update the maximum window length.

Brute Force:
Generate every possible substring and check whether it contains duplicates.
There are O(n²) substrings, and checking each can take O(n).
Time: O(n³)
Space: O(n)

Key Mental Model / Invariant:
At all times, [left...right] must contain only unique characters.
The HashSet represents exactly the characters currently inside the window.

Sliding Window Template:
left = 0

for right:
    add/process right

    while window is invalid:
        remove/process left
        left++

    update answer

For this problem:
Data structure → HashSet
Invalid condition → duplicate character
Shrink → remove from left
Answer → maximum window length

Important:
The nested while loop does NOT make this O(n²).
Both left and right only move forward.
right moves at most n times and left moves at most n times.
Therefore total time is O(n) — amortized analysis.

HashSet vs HashMap:
HashSet → "Does this value exist?"
HashMap → "What value is associated with this key?"

Clean Code:

class Solution {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();

        int left = 0;
        int maxLength = 0;

        for (int right = 0; right < s.length(); right++) {

            // Shrink until the current window becomes valid.
            while (set.contains(s.charAt(right))) {
                set.remove(s.charAt(left));
                left++;
            }

            // Add the current character to the valid window.
            set.add(s.charAt(right));

            // Current window contains only unique characters.
            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }
}

Complexity:
Time  → O(n)
Space → O(n)

What Can You Generalize:
Whenever you see:
"longest/shortest" + "substring/subarray" + "contiguous" + "condition"

Ask:
"Can I maintain a valid sliding window instead of recalculating everything?"

Memory Hook:
Expand → violation → shrink → valid → update answer.
	* */
}
