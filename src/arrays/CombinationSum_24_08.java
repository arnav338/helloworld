package arrays;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinationSum {

    /*
    Given an array of distinct integers candidates and a target integer target, return a list of all unique combinations of candidates where the chosen numbers sum to target. You may return the combinations in any order.

The same number may be chosen from candidates an unlimited number of times. Two combinations are unique if the
frequency
 of at least one of the chosen numbers is different.

The test cases are generated such that the number of unique combinations that sum up to target is less than 150 combinations for the given input.



Example 1:

Input: candidates = [2,3,6,7], target = 7
Output: [[2,2,3],[7]]
Explanation:
2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
7 is a candidate, and 7 = 7.
These are the only two combinations.
Example 2:

Input: candidates = [2,3,5], target = 8
Output: [[2,2,2,2],[2,3,3],[3,5]]
Example 3:

Input: candidates = [2], target = 1
Output: []
    * */

    public static void main(String[] args) {
        int[] arr = new int[]{2,3,6,7};
        combinationSum(arr,7);
    }

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Set<List<Integer>> listSet = new HashSet<>();
        List<Integer> temp = new ArrayList<>();
        checkSum(0,target, candidates,temp,listSet);
        listSet.forEach(l -> {
            System.out.print("list is - ");
            l.forEach(a -> System.out.print(a+","));
            System.out.println();
        });
        listSet.forEach(s -> res.add(s));
        return res;
    }

    public static void checkSum(int currentIndex, int target, int[] candidates, List<Integer> temp, Set<List<Integer>> listSet){
        if(currentIndex >= candidates.length || target < 0){
            return;
        }
        if(target == 0){
            listSet.add(new ArrayList<>(temp));
            //temp.clear();
//            System.out.println("found a list");
//            temp.forEach(a -> System.out.println(a+","));
//            System.out.println("");
        }
        //System.out.println("Adding "+candidates[currentIndex]);
        temp.add(candidates[currentIndex]);
        checkSum(currentIndex, target-candidates[currentIndex], candidates,temp,listSet);
        temp.remove(temp.size()-1);
//        if(temp.size()>0){
//            System.out.println("Removing "+temp.get(temp.size()-1));
//            temp.remove(temp.size()-1);
//        }
        checkSum(currentIndex+1, target, candidates,temp,listSet);
    }

    /*

    working solution -

    time complexity - 2^n * k - as for every array element we are checking the possiblity


    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Set<List<Integer>> listSet = new HashSet<>();
        List<Integer> temp = new ArrayList<>();
        checkSum(0,target, candidates,temp,listSet);
        listSet.forEach(s -> res.add(s));
        return res;
    }

    public static void checkSum(int currentIndex, int target, int[] candidates, List<Integer> temp, Set<List<Integer>> listSet){
        if(currentIndex >= candidates.length || target < 0){
            return;
        }
        if(target == 0){
            listSet.add(new ArrayList<>(temp));
        temp.add(candidates[currentIndex]);
        checkSum(currentIndex, target-candidates[currentIndex], candidates,temp,listSet);
        temp.remove(temp.size()-1);
        checkSum(currentIndex+1, target, candidates,temp,listSet);
    }
    * */

}
