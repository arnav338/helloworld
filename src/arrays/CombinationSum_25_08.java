package arrays;

import java.util.*;

public class CombinationSum_25_08 {

    /*
Given a collection of candidate numbers (candidates) and a target number (target), find all unique combinations in candidates where the candidate numbers sum to target.

Each number in candidates may only be used once in the combination.

Note: The solution set must not contain duplicate combinations.



Example 1:

Input: candidates = [10,1,2,7,6,1,5], target = 8
Output:
[
[1,1,6],
[1,2,5],
[1,7],
[2,6]
]
Example 2:

Input: candidates = [2,5,2,1,2], target = 5
Output:
[
[1,2,2],
[5]
]
    * */

    public static void main(String[] args) {
        int[] arr = new int[]{10,1,2,7,6,1,5};
        int target = 8;
//        int[] arr = new int[]{2,5,2,1,2};
//        int target = 5;
        combinationSum(arr,target);
    }

    /* optimised approach
    the difference that this problem (as compared to combination sum 1) has is that we are checking at every step if the current element can be taken or not, as it may so happen that at the same level of iteration,
   we have multiple occurences of the same element but we obviously dont want to pick the same combination twice
   so we should have a check that the element we are choosing is not the same as previous one

    * */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List < List < Integer >> ans = new ArrayList < > ();
        Arrays.sort(candidates);
        findCombinations(0, candidates, target, ans, new ArrayList < > ());
        return ans;
    }
    static void findCombinations(int ind, int[] arr, int target, List < List < Integer >> ans, List < Integer > ds) {
        if (target == 0) {
            ans.add(new ArrayList < > (ds));
            return;
        }

        for (int i = ind; i < arr.length; i++) {
            // i>ind ensures that the first element is always picked as when index is 0, we dont have any comparison to make
            // arr[i] == arr[i - 1] ensures that the same element is not picked at the same level of iteration
            if (i > ind && arr[i] == arr[i - 1]) continue;
            //  arr[i] > target ensures that if the adding the current element to the list exceeds the target value then all the elements to the right of current element are also not picked
            // as if adding current element exceeds the target, the adding any element to the right of current element will obviously exceed the target as all elements to the right are bigger than the current element
            if (arr[i] > target) break;

            ds.add(arr[i]);
            findCombinations(i + 1, arr, target - arr[i], ans, ds);
            ds.remove(ds.size() - 1);
        }
    }

    /* brute force approach */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Set<List<Integer>> listSet = new HashSet<>();
        List<Integer> temp = new ArrayList<>();
        Arrays.sort(candidates);
        checkSum(0,target, candidates,temp,res);
        //res.forEach(s -> res.add(s));
        res.forEach(l -> {
            System.out.print("list is - ");
            l.forEach(a -> System.out.print(a+","));
            System.out.println();
        });
        return res;
    }

    public static void checkSum(int currentIndex, int target, int[] candidates, List<Integer> temp, List<List<Integer>> listSet){
        //System.out.println("temp is "+temp+" | target - "+target+" | currentIndex - "+currentIndex);
        if(target == 0){
            listSet.add(new ArrayList<>(temp));
        }
        if(currentIndex >= candidates.length || target < 0){
            return;
        }
        temp.add(candidates[currentIndex]);
        checkSum(currentIndex+1, target-candidates[currentIndex], candidates,temp,listSet);
        temp.remove(temp.size()-1);
        checkSum(currentIndex+1, target, candidates,temp,listSet);
    }

}
