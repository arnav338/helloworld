package arrays;

public class EquilibriumPoint {
	public static void main(String[] args) {
		/*
		 * Given an array A of n positive numbers. The task is to find the first
		 * Equilibrium Point in the array. Equilibrium Point in an array is a position
		 * such that the sum of elements before it is equal to the sum of elements after
		 * it.
		 * 
		 * Example 1:
		 * 
		 * Input: 
		 * n = 5
		 * A[] = {1,3,5,2,2} 
		 * Output: 3 
		 * Explanation: For second test case equilibrium point is at position 3 as 
		 * elements before it (1+3) = elements after it (2+2).
		 */
		int a[] = {1,3,5,2,2} ;
		System.out.println("Element at position : "+positionOfEquilibriumPoint(a,a.length)+" is equilibrium point");
	}

	private static int positionOfEquilibriumPoint(int[] a, int length) {
		int leftSum = 0;
        int result = -1;
        if(length==0 || length==2) return result;
        if(length==1) return 1;
        long sum = 0;
        for(long b : a){ // take total sum
            sum += b;
        }
        for(int i=0;i<length-1;i++){ 
        	// at every index, decrease the total sum by the current element and the elements to left of current
        	// at every index i, leftSum = a[k] , k = 0 to i
        	// 					rightSum = sum - a[k]
        	// if leftSum == rightSum ,we found equilibrim point
            leftSum += a[i]; // 4
            long curr = sum - leftSum - a[i+1]; // 4
            if(leftSum == curr){
                return i+2;
            }
        }
		return result;
	}
}
