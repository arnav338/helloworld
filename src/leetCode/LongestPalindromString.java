package leetCode;

public class LongestPalindromString {
	/*
	 * Given a string s, return the longest palindromic substring in s.
	 * 
	Example 1:
	
	Input: s = "babad"
	Output: "bab"
	Explanation: "aba" is also a valid answer.
	Example 2:
	
	Input: s = "cbbd"
	Output: "bb"
	 * */
	public static void main(String[] args) {
		System.out.println("result : "+getLongestPalindrome("abba"));
	}
	
	public static String getLongestPalindrome(String input) {
		int maxLen = 0;
		int startIndex = 0;
		int temp=0;
		for(int i=0; i<input.length(); i++) {
			//expandPalindrome(input,i,i,maxLen);
			int start = i, end=i;
			while(start >=0 && end<input.length() && input.charAt(start)==input.charAt(end)) {
				start--;
				end++;
			}
			temp = end - start - 1;
			if(maxLen < temp) {
				maxLen = temp;
				startIndex = start+1;
			}
			//expandPalindrome(input,i,i+1,maxLen);
			start = i; 
			end=i+1;// to incorporate the case of even length palindrome, eg : abba , we need have start = index-1 and end = index-2 to explore the possibility of even palindrome
			while(start >=0 && end<input.length() && input.charAt(start)==input.charAt(end)) { 
				start--;
				end++;
			}
			temp = end - start - 1;
			if(maxLen < temp) {
				maxLen = temp;
				startIndex = start+1;
			}
		}
		
		return input.substring(startIndex, startIndex+maxLen);
	}
	
}
