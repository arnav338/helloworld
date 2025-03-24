package main;

import java.util.ArrayList;

public class MainClass1 {
	
	public static void main(String args[]) {
		int[] arr = new int[]{1,2,5,7,6,8,9,3,4,2};

		mergeSort(arr,0,arr.length-1);

		for(int i=0; i<arr.length; i++){
			System.out.print(arr[i]+",");
		}
	}

	private static void mergeSort(int[] arr, int low, int high) {
		if(low >= high) return;
		int mid = (low + high)/2;
		mergeSort(arr,low,mid); // left part
		mergeSort(arr,mid+1,high); // right part
		merge(arr,low,mid,high);
//		for(int i=0; i<arr.length; i++){
//			System.out.print(arr[i]+",");
//		}
//		System.out.println("");
	}

	private static void merge(int[] arr, int low, int mid, int high) {
		ArrayList<Integer> temp = new ArrayList<>(); // temporary array
		int left = low;      // starting index of left half of arr
		int right = mid + 1;   // starting index of right half of arr

		//storing elements in the temporary array in a sorted manner//

		while (left <= mid && right <= high) {
			if (arr[left] <= arr[right]) {
				temp.add(arr[left]);
				left++;
			} else {
				temp.add(arr[right]);
				right++;
			}
		}

		// if elements on the left half are still left //

		while (left <= mid) {
			temp.add(arr[left]);
			left++;
		}

		//  if elements on the right half are still left //
		while (right <= high) {
			temp.add(arr[right]);
			right++;
		}

		// transfering all elements from temporary to arr //
		for (int i = low; i <= high; i++) {
			arr[i] = temp.get(i - low); // (i-low) to make sure all elements of temporary array get copied
		}


//		int[] ar = new int[high-low+1];
//
//		int k = 0; // counter for temporary array
//		int i=low;
//		int j = mid+1;
//
//		while(i<=mid && j<=high){
//			if(arr[i] <= arr[j]){
//				ar[k] = arr[i];
//				k++;
//				i++;
//			}else {
//				ar[k] = arr[j];
//				k++;
//				j++;
//			}
//		}
//
//		while(i<=mid){
//			ar[k] = arr[i];
//			i++;
//		}
//
//		while(j<=high){
//			ar[k] = arr[j];
//			j++;
//		}
//
//		for(int l=low; l<=high; l++){
//			arr[l] = ar[l-low];
//		}


	}


}
