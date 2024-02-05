package main;

import java.util.HashMap;

public class Child12 {	
	public static void main(String args[]) {
		
		long[] a = {0,5,1,2,4,3};
		shuffle(a);
	}

	private static void shuffle(long[] a) {
		HashMap<Long, Integer> map = new HashMap<Long, Integer>();
		long[] temp = new long[a.length];
		for (int i = 0; i < a.length; i++) {
			map.put(a[i], i);
			temp[i] = a[i];
		}
		for (int i = 0; i < a.length; i++) {
			int index = map.get((long) i);
			a[index] = temp[i];
			
		}
	}
}
