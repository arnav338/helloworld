package leetCode;

import java.util.HashMap;

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
}
