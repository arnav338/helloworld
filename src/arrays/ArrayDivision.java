package arrays;

import java.util.HashMap;

public class ArrayDivision {
	public static void main(String[] args) {
		/*
		 * Given an array of integers and a number k, write a function that returns true
		 * if the given array can be divided into pairs such that the sum of every pair
		 * is divisible by k.
		 * 
		 * Examples:
		 * 
		 * Input: 
		 * arr[] = {9, 7, 5, 3}, 
		 * k = 6 Output: True We can divide the array into
		 * (9, 3) and (7, 5). Sum of both of these pairs is a multiple of 6.
		 * 
		 * An Efficient Solution is to use Hashing.
		 * 
		 * 1) If length of given array is odd, return false. An odd length array cannot
		 * be divided into pairs. 
		 * 2) Traverse input array and count occurrences of all
		 * remainders (use (arr[i] % k)+k)%k for handling the case of negative integers
		 * as well). freq[((arr[i] % k)+k)%k]++ 
		 * 3) Traverse input array again. 
		 * 		a) Find the remainder of the current element. 
		 * 		b) If remainder divides k into two halves, then there must be even occurrences of it as it forms pair with
		 * 			itself only. 
		 * 		c) If the remainder is 0, then there must be even occurrences.
		 * 		d) Else, number of occurrences of current the remainder must be equal to a
		 * 			number of occurrences of "k - current remainder".
		 * 
		 */
		int arr[] = { 92, 75, 65, 48, 45, 35 };
        int k = 10;
 
        // Function call
        boolean ans = canPairs(arr, k);
        if (ans)
            System.out.println("True");
        else
            System.out.println("False");
	}
	static boolean canPairs(int ar[], int k)
    {
        // An odd length array cannot be divided into pairs
        if (ar.length % 2 == 1)
            return false;
 
        // Create a frequency array to count occurrences
        // of all remainders when divided by k.
        HashMap<Integer, Integer> hm = new HashMap<>();
 
        // Count occurrences of all remainders
        // the map is between remainder and the remainder's freq
        for (int i = 0; i < ar.length; i++) {
            int rem = ((ar[i] % k) + k) % k;
            if (!hm.containsKey(rem)) {
                hm.put(rem, 0);
            }
            hm.put(rem, hm.get(rem) + 1);
        }
 
        // Traverse input array and use freq[] to decide
        // if given array can be divided in pairs
        for (int i = 0; i < ar.length; i++) {
            // Remainder of current element
            int rem = ((ar[i] % k) + k) % k;
 
            // If remainder with current element divides
            // k into two halves.
            if (2 * rem == k) {
                // Then there must be even occurrences of
                // such remainder
                if (hm.get(rem) % 2 == 1)
                    return false;
            }
 
            // If remainder is 0, then there must be two elements with 0 remainder
            else if (rem == 0) {
                // Then there must be even occurrences of such remainder
                if (hm.get(rem) % 2 == 1) // returning if occurences are false
                    return false;
            }
 
            // Else number of occurrences of remainder
            // must be equal to number of occurrences of
            // k - remainder
            else {
                if (hm.get(k - rem) != hm.get(rem))
                    return false;
            }
        }
        return true;
    }
}
