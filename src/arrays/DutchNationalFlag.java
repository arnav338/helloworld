package arrays;

public class DutchNationalFlag {
	public static void main(String[] args) {
		/*// time complexity - O(n)
		 * Aim is to sort an array containing 0,1,2
		 * we will keep 3 pointers - low mid and high
		 * and work acc to following algorithm
		 * 
		 * check arr[mid]
		 * 
		 * if 0 , swap arr[low] and arr[mid], low++, mid++
		 * if 1, mid ++
		 * if 2, swap arr[mid] and arr[high] , high--
		 * 
		 * the pointer mid represents unknown area
		 * when mid reaches high,i.e., mid == high
		 * the algorithm ends
		 * 
		 * mid pointer represents our partitioning
		 * low represents the index where 0's will end
		 * high represents the index where 2 will start
		 * */
		int arr[] = {1,0,2,1,1,0,0,1,2,1};
		int low = 0, mid = 0, high = arr.length-1;
		while(mid<high) {
			if(arr[mid] == 0) {
				swap(arr,low,mid);
				low++;
				mid++;
			}
			else if(arr[mid] == 2) {
				swap(arr,high,mid);
				high--;
			}
			else { mid++;
			}
		}
		for(int a : arr) System.out.print(a+"  ");
	}

	private static void swap(int[] arr, int low, int mid) {
		arr[low] = arr[low] ^ arr[mid];
		arr[mid] = arr[low] ^ arr[mid];
		arr[low] = arr[low] ^ arr[mid];
	}
}