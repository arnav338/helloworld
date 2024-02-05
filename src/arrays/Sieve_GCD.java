package arrays;

import java.util.ArrayList;

public class Sieve_GCD {
	public static void main(String[] args) {
		
		ArrayList<Integer> list = sieve(125);
		for(int d : list) {
			System.out.print(" "+d);
		}
		System.out.println("=====");
		System.out.println(gcd(27,15));
	}

	private static int gcd(int i, int j) {
		if(j==0) return i;
		return gcd(j, i%j);
	}

	private static ArrayList<Integer> sieve(int k) {
		boolean[] bool = new boolean[k];
		bool[0] = false;
		// we will go from 1 to square root of n
		for(int i=2;i*i<=k;i++) {
			// j is incremented in multiples of current number
			// 2 -> 4 -> 6
			for(int j=2*i;j<=k;j+=i) {
				if(j%i==0 && j!=i) {
					bool[j-1] = true;
				}
			}
		}
		ArrayList<Integer> output = new ArrayList<Integer>();
		for(int c = 0; c<bool.length-1; c++) {
			if(!bool[c]) {
				output.add(c+1);
			}
		}
		return output;
	}
}
