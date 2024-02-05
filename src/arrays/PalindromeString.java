package arrays;

public class PalindromeString {

	public static void main(String[] args) {
		//String a = "aba";
		String a = "abcdxyz";
		System.out.println(isPalindrome(a,0,a.length()-1));
		System.out.println("==========");
		String b = reverseString(a);
		System.out.println("reversed : "+b);
		
	}
	
	private static String reverseString(String a) {
		char[] b = a.toCharArray();
		int left = 0;
		int right = b.length - 1;
		while(left < right) {
			char t = b[left];
			b[left] = b[right];
			b[right] = t;
			left++;
			right--;
		}
		return String.valueOf(b);
	}

	private static boolean isPalindrome(String a, int i, int j) {
		/*
		 * loop ends when both pointers reach same index
		 * if length is 1, then both pointers will be equal hence loop will return true
		 * as a single character is also palindrome
		 * */
		// time complexity - O(n)
		if(i>=j) return true;
		if(a.charAt(i)!=a.charAt(j)) return false;
		return isPalindrome(a, i+1, j-1);
	}
}
