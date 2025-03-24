package leetCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwoSum {

	public static void main(String[] args) {
		int N=1;
		//int startingNumber = N==1 ? 0 : (int) Math.pow(10,N-1);
		
		int[] input = new int[]{2,7,11,15};
		int target = 9;
		for(int a:twoSum(input,target)) {
			System.out.print(a+",");
		}

	}

	private static int[] twoSum(int[] nums, int target) {
		Map<Integer, Integer> source = new HashMap<>();
		for(int a=0;a<nums.length;a++) {
            if(source.containsKey(target-nums[a]) && source.get(target-nums[a])!=a) {
				return new int[]{a,source.get(target-nums[a])};
			}
            source.put(nums[a],a);
		}
		
		return null;
	}

}
