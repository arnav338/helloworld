package arrays;

import java.util.ArrayList;
import java.util.List;

public class PrintSubarrayWithGivenSum_22_07_24 {

	public static void main(String[] args) {

		/*
		in array [1,2,1], k = 2
		we have to print subsequences with sum 2, i.e., [1,1] and [2]

TODO:
		Tip : whenever only one solution is required instead of all in recursion (eg  - print only one subsequence of sum K)
		try to return a boolean flag by the recursive function instead of calling void recursive function
		so that on the basis of the flag value we can decide whether to proceed further recursive calls

		similarly if we just want the count of subsequences, we can simply return 1 if our condition satisfies
		and 0 if it doesnt and make the return type of our function as int
		and for left and right recursive calls, we can return the addition of left and right

		time complexity - 2^n as for every array element we have option of choosing and not choosing
		* */

		//int[] arr = new int[]{1,2,1};
		int[] arr = new int[]{1,5,4,3,3};
		int k = 6;
		List<Integer> l = new ArrayList<>();
		int sumTillNow = 0;
		int current = 0;
		printSubsequence(arr,k,l,sumTillNow,current);
		System.out.println("--------");
		//printAnyOneSubsequence(arr,k,l,sumTillNow,current);
		System.out.println("--------");
		int counter = 	countAllSubsequence(arr,k,l,sumTillNow,current);
		System.out.println("count - "+counter);

	}

	private static void printSubsequence(int[] inputArray, int desiredSum, List<Integer> temporaryList, int sumTillNow, int current) {
		// the last parameter current represents the element under inspection, i.e., we will try to get our desired
		if(sumTillNow == desiredSum){
			System.out.println("subsequence with sum desiredSum -> "+temporaryList);
		}
		if(current >= inputArray.length) return;
		temporaryList.add(inputArray[current]);
		printSubsequence(inputArray,desiredSum,temporaryList,sumTillNow+inputArray[current],current+1);
		temporaryList.remove((Object) inputArray[current]);
		printSubsequence(inputArray,desiredSum,temporaryList,sumTillNow,current+1);
	}

	private static boolean printAnyOneSubsequence(int[] arr, int k, List<Integer> l, int sumTillNow, int current) {
		if(sumTillNow == k){
			System.out.println("subsequence with sum k -> "+l);
			return true;
		}
		if(current >= arr.length) return false;
		l.add(arr[current]);
		if(printAnyOneSubsequence(arr,k,l,sumTillNow+arr[current],current+1)){
			return true;
		}
		l.remove((Object) arr[current]);
		if(printAnyOneSubsequence(arr,k,l,sumTillNow,current+1)){
			return true;
		}
		return false;
	}

	private static int countAllSubsequence(int[] arr, int k, List<Integer> l, int sumTillNow, int current) {
		if(sumTillNow == k){
			//System.out.println("subsequence with sum k -> "+l);
			return 1;
		}
		if(current >= arr.length) return 0;
		l.add(arr[current]);
		int left = countAllSubsequence(arr,k,l,sumTillNow+arr[current],current+1);
		l.remove((Object) arr[current]);
		int right = countAllSubsequence(arr,k,l,sumTillNow,current+1);
		return left+right;
	}
}
