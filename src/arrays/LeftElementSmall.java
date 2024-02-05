package arrays;

public class LeftElementSmall {
	public static void main(String[] args) {
		int[] a = {4, 2, 5, 7};
		// find such element, on the left of which lies all elements smaller than it
		System.out.println(findElement(a,a.length));	
	}
	
	private static int findElement(int[] arr, int n) {
        int[] small = new int[n];
        // 4 4 2 2
        int[] large = new int[n];
        // 7  7   7   7
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for(int i=1;i<n;i++){
        	min = Math.min(min,arr[i-1]);
            small[i] = min;
        }
        small[0] = arr[0];
        for(int i=n-2;i>=0;i--){
        	max = Math.max(max,arr[i+1]);
            large[i] = max;
        }
        large[n-1] = arr[n-1];
        for(int i=1;i<n-1;i++){
        	int curr = arr[i];
            if(curr >= small[i] && curr <= large[i] ){
                return arr[i];
            }
        }
        return -1;
	}
}
