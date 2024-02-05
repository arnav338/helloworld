package leetCode;

public class BinarySearch {

	public static void main(String[] args) {
		
		//int[] arr = new int[] {4,5,6,7,8,1,2,3};
		int[] arr = new int[] {1,2,3,4,5,6,7,8};
		int k = 8;
		System.out.println(binarySearch(arr,k,0,arr.length-1));
		
	}
	// time complexity - O(log N)
	public static int binarySearch(int[] arr, int key, int start, int end) {
		System.out.println("Start "+start+" end "+end);
		int mid = (start+end)/2;
		if(key==arr[mid]) {
			return mid;
		}
		else if(key<arr[mid]) {
			return binarySearch(arr, key, start, mid-1);
		}
		else {
			return binarySearch(arr, key, mid+1, end);
		}
		
	}

}
