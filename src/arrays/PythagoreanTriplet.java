package arrays;

import java.util.HashSet;

public class PythagoreanTriplet {
	public static void main(String[] args) {
		/*
		 * Given an array arr of N integers, write a function that returns true 
		 * if there is a triplet (a, b, c) that satisfies a2 + b2 = c2, otherwise false.
		 * */
		int a[] = {3, 2, 4, 6, 5};
		System.out.println(hasTriplet(a,a.length));
	}

	private static boolean hasTriplet(int[] arr, int n) {
		HashSet<Integer> set = new HashSet<>();
        for(int i=0; i<n; i++){
            arr[i] *= arr[i]; 
            set.add(arr[i]);
        }
        for(int i = 0; i< n; i++){
            for(int j = 0; j!=i && j< n; j++){
                if(set.contains(arr[i]+arr[j])){
                    return true;
                }
            }
        }
        return false;
	}
}
