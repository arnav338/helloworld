package arrays;

public class PermutationsOfString {
	
	public static void main(String[] args) {
		
		String a = "abc";
		printAllPermutations(a,0,a.length()-1);
		
	}
	
	private static void printAllPermutations(String a, int l, int r) {
		// time complexity is O(n*n!)
		if(l==r) {
			// i==j indicates that we have reached the end of iteration and exhausted 
			// all combinations for this current iteration and we need to print it
			System.out.println(a);
			return;
		}
		for(int i=l;i<=r;i++) {
			a = swap(a,l,i);
			printAllPermutations(a, l+1, r);
			a = swap(a,l,i);
			// this swap is needed to bring the string to its original form
			// since we are using the same string throughout the code we need to maintain its form
		}
	}

	private static String swap(String a, int l, int r) {
		char[] c = a.toCharArray();
		char d = c[l];
		c[l] = c[r];
		c[r] = d;
		return String.valueOf(c);
	}
	
	
	
}
