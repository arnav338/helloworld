package arrays;

public class CanPlaceFlower {
    /*
    You have a long flowerbed in which some of the plots are planted, and some are not. However, flowers cannot be planted in adjacent plots.

Given an integer array flowerbed containing 0's and 1's, where 0 means empty and 1 means not empty, and an integer n, return true if n new flowers can be planted in the flowerbed without violating the no-adjacent-flowers rule and false otherwise.



Example 1:

Input: flowerbed = [1,0,0,0,1], n = 1
Output: true
Example 2:

Input: flowerbed = [1,0,0,0,1], n = 2
Output: false
    * */

    public static void main(String[] args) {

        System.out.println("can place flower ? "+canPlaceFlowers(new int[]{1,0,0,0,1},1));

        System.out.println("can place flower ? "+canPlaceFlowers(new int[]{1,0,0,0,1},2));

        System.out.println("can place flower ? "+canPlaceFlowers(new int[]{1,0,0,0,0,1},2));
    }

    /*
    Intuition

    at each index check if
    1. array is empty at this index
    2. left place is empty (handle index == 0)
    3. right place is empty (handle index == n-1)
        * */

    public static boolean canPlaceFlowers(int[] flowerbed, int n) {
        for(int i=0; i<flowerbed.length; i++){
            if(n<=0) break;
            if(flowerbed[i] == 0){
                boolean left = i==0 ? true : (flowerbed[i-1] == 0 ? true : false);
                boolean right = i==flowerbed.length-1 ? true : (flowerbed[i+1] == 0 ? true : false);
                if(left && right){
                    flowerbed[i] = 1;
                    n--;
                }
            }
        }
        return n==0 ? true : false;
    }

}
