package arrays;

public class RainWaterTrapping {
	public static void main(String[] args) {
		/*
		 * array is given where values represent height of blocks
		 * we have to find how many blocks of water will be stored in between blocks
		 * 
		 *      &
		 *   &  &  &
		 *   & &&  &&
		 *   &&&& &&&
		 *   
		 *   answer : 3 + 5 = 8
		 * */
		int a[] = {3,1,2,4,0,1,3,2};
		System.out.println("->->"+findAmountOfWater(a));
		System.out.println("->->"+findAmountOfWater1(a));
	}
	
	private static int findAmountOfWater1(int[] a) {
		// time complexity - O(n)
		// space complexity - O(1)
		int storage=0;
		int maxR=a[a.length-1],maxL=a[0];
		int l=1, r=a.length-2;
		while(l<=r) {
			if(maxL<maxR) {
				if(a[l]>=maxL) {// this block cant store water
					maxL = a[l];
				}
				else {
					storage += maxL - a[l];
				}
				l++;
			}
			else {
				if(a[r]>maxR) { // this block cant store water
					maxR = a[r];
				}
				else {
					storage += maxR - a[r];
				}
				r--;
			}
		}
		return storage;
	}
	
	private static int findAmountOfWater(int[] a) {
		// time complexity - O(n)
		// space complexity - O(n)
		int storage = 0;
		int[] maxLeft = new int[a.length];
		maxLeft[0] = 0;
		int[] maxRight = new int[a.length];
		maxRight[a.length-1] = 0;
		int maxR=0,maxL=0;
		for (int i = 1; i < a.length; i++) {
			maxL = Math.max(maxL, a[i-1]);
			maxLeft[i] = maxL;
		}
		for (int i = a.length-2; i >=0 ; i--) {
			maxR = Math.max(maxR, a[i+1]);
			maxRight[i] = maxR;
		}
		for (int i = 0; i < a.length; i++) {
			int height = Math.min(maxLeft[i], maxRight[i]);
			int curr = height - a[i];
			if(curr>0) {
				storage += curr;
			}
		}
		return storage;
	}
}
