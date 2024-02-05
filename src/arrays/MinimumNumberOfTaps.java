package arrays;

public class MinimumNumberOfTaps {

	public static void main(String[] args) {
		/*
		 * There is a one-dimensional garden on the x-axis. The garden starts at the
		 * point 0 and ends at the point n. (i.e The length of the garden is n).
		 * 
		 * There are n + 1 taps located at points [0, 1, ..., n] in the garden.
		 * 
		 * Given an integer n and an integer array ranges of length n + 1 where
		 * ranges[i] (0-indexed) means the i-th tap can water the area [i - ranges[i], i
		 * + ranges[i]] if it was open.
		 * 
		 * Return the minimum number of taps that should be open to water the whole
		 * garden, If the garden cannot be watered return -1.
		 * 
		 * Input: 
		 * n = 5, 
		 * ranges = [3,4,1,1,0,0] 
		 * Output: 1 
		 * Explanation: The tap at point 0 can cover the interval [-3,3] 
		 * The tap at point 1 can cover the interval [-3,5] 
		 * The tap at point 2 can cover the interval [1,3] 
		 * The tap at point 3 can cover the interval [2,4] 
		 * The tap at point 4 can cover the interval [4,4] 
		 * The tap at point 5 can cover the interval [5,5] 
		 * Opening Only the second tap will water the whole garden [0,5]
		 */
		int n = 9;
		//int a[] = {3,4,1,1,0,0};
		//int a[] = {0,0,0};
		
		int a[] = {0,5,0,3,3,3,1,4,0,4};
		System.out.println("->->"+minTaps(a,n));
	}

	private static int minTaps(int[] ranges, int n) {
		int left = 0;
		int right = 0;
		int tap = 0;
		for (int i = 0; i <= n; i++) {
			if(i-ranges[i]>=left && i+ranges[i]<=right) {
				System.out.println("i:"+i+"||value:"+ranges[i]);
				continue;
			}
			if(ranges[i]!=0 && i-ranges[i]<=left && i+ranges[i]>=right){
				System.out.println("Before---"+ranges[i]+"||left:"+left+"||right"+right+"||tap:"+tap);
				left = Math.min(left, i-ranges[i]);
				left = Math.max(0, left);
                right = Math.max(right, i+ranges[i]);
                right = Math.min(n, right);
                tap = 1;
                System.out.println("After---"+ranges[i]+"||left:"+left+"||right"+right+"||tap:"+tap);
            }
			else if(ranges[i]!=0){
				System.out.println("Before+++"+ranges[i]+"||left:"+left+"||right"+right+"||taps:"+tap);
				left = Math.min(left, i-ranges[i]);
				left = Math.max(0, left);
				right = Math.max(right, i+ranges[i]);
	            right = Math.min(n, right);
                tap++;
                System.out.println("After+++"+ranges[i]+"||left:"+left+"||right"+right+"||taps:"+tap);
            }
            if(left == 0 && right==n){
                break;
            }
		}
		return left==0 && right==n ? tap : -1;
	}

}
