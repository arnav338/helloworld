package arrays;

import java.util.Arrays;

public class MinimumPlatforms {
	/*
	 * Given arrival and departure times of all trains that reach a railway station.
	 * Find the minimum number of platforms required for the railway station so that
	 * no train is kept waiting. Consider that all the trains arrive on the same day
	 * and leave on the same day. Arrival and departure time can never be the same
	 * for a train but we can have arrival time of one train equal to departure time
	 * of the other. At any given instance of time, same platform can not be used
	 * for both departure of a train and arrival of another train. In such cases, we
	 * need different platforms.
	 * 
	 * 
	 * Example 1:
	 * 
	 * Input: 
	 * n = 6 
	 * arr[] = {0900, 0940, 0950, 1100, 1500, 1800} 
	 * dep[] = {0910,1200, 1120, 1130, 1900, 2000} 
	 * Output: 3 
	 * Explanation: Minimum 3 platforms are required to safely arrive and depart all trains.
	 */
	public static void main(String[] args) {
		int arr[] = { 900, 940, 950, 1100, 1500, 1800 };
		int dep[] = { 910, 1200, 1120, 1130, 1900, 2000 };
		System.out.println(findPlatform(arr, dep));
	}

	private static int findPlatform(int[] arr, int[] dep) {
		// time - O(n*log N)
		Arrays.sort(arr);
		Arrays.sort(dep);
		int platform = 0;
		int ar = 0;
		int de = 0;
		while (ar < arr.length) {
			if (ar == 0) { // if its the 1st train we need to update number of platform irrespective of anything
				ar++;
				platform++;
				continue;
			} else {
				if (arr[ar] > dep[de]) { 
					// if train arrives after departure of previous, then no need to update number of platforms
					// just increment arrival and departure index as this departure time we have already chacked
					de++;
					ar++;
				} else {
					// if train arrives before departure of previous, then we need to update number of platforms
					// at the same time we will only update the arrival index and not the departure one
					// as the current departure time has still not passed
					platform++;
					ar++;
				}
			}

		}
		return platform;
	}
}
