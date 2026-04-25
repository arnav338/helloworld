package arrays;

import java.util.*;

public class SubSetSum_1__28_08_24 {

    /*
    Given a list arr of n integers, return sums of all subsets in it. Output sums can be printed in any order.

Examples:

Input: n = 2, arr[] = {2, 3}
Output: 0 2 3 5
Explanation: When no elements is taken then Sum = 0. When only 2 is taken then Sum = 2. When only 3 is taken then Sum = 3. When element 2 and 3 are taken then Sum = 2+3 = 5.
Example 2:

Input: n = 3, arr = {1, 2, 1}
Output: 0 1 1 2 2 3 3 4

    * */

    public static void main(String[] args) {
        ArrayList<Integer> l = new ArrayList<>(Arrays.asList(1,2,1));
        ArrayList<Integer> integers = subsetSums(l, l.size());
        //integers.forEach(i -> System.out.print(i+","));
    }

    static ArrayList<Integer> subsetSums(ArrayList<Integer> arr, int n) {
        ArrayList<Integer> res = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        calculateSumUnique(0,0,arr,set);
        //set.forEach(i -> System.out.print(i+","));
        System.out.println("======");

        // TC - O(2^n)
        int currentIndex = 0;
        calculateSum(currentIndex,0,arr,res);
        calculateSum(currentIndex,0 + arr.get(currentIndex),arr,res);
        System.out.println(res.size()+ " elements found ");
        res.forEach(i -> System.out.print(i+","));
        return res;
    }

    private static void calculateSumUnique(int currentIndex, int currentSum, ArrayList<Integer> arr, Set<Integer> res) {
        if(currentIndex>=arr.size()){
            return;
        }
        res.add(currentSum);
        res.add(currentSum + arr.get(currentIndex));
        calculateSumUnique(currentIndex+1,currentSum,arr,res);
        calculateSumUnique(currentIndex+1,currentSum + arr.get(currentIndex),arr,res);
    }

    private static void calculateSum(int currentIndex, int currentSum, ArrayList<Integer> arr, ArrayList<Integer> res) {
        if(currentIndex == arr.size()-1){
            res.add(currentSum);
        }
        if(currentIndex+1 >= arr.size()) return;
        System.out.println("curr index "+currentIndex+", curr sum - "+currentSum);
        calculateSum(currentIndex+1,currentSum,arr,res);
        calculateSum(currentIndex+1,currentSum + arr.get(currentIndex+1),arr,res);
    }
}
