package arrays;

import java.util.HashMap;
import java.util.Map;

public class ChocolatePickup_Hard {
    /*
 Problem statement
Ninja has a 'GRID' of size 'R' X 'C'. Each cell of the grid contains some chocolates. Ninja has two friends Alice and Bob, and he wants to collect as many chocolates as possible with the help of his friends.

Initially, Alice is in the top-left position i.e. (0, 0), and Bob is in the top-right place i.e. (0, ‘C’ - 1) in the grid. Each of them can move from their current cell to the cells just below them. When anyone passes from any cell, he will pick all chocolates in it, and then the number of chocolates in that cell will become zero. If both stay in the same cell, only one of them will pick the chocolates in it.

If Alice or Bob is at (i, j) then they can move to (i + 1, j), (i + 1, j - 1) or (i + 1, j + 1). They will always stay inside the ‘GRID’.

Your task is to find the maximum number of chocolates Ninja can collect with the help of his friends by following the above rules.

Example:
Input: ‘R’ = 3, ‘C’ = 4
       ‘GRID’ = [[2, 3, 1, 2], [3, 4, 2, 2], [5, 6, 3, 5]]
Output: 21

Initially Alice is at the position (0,0) he can follow the path (0,0) -> (1,1) -> (2,1) and will collect 2 + 4 + 6 = 12 chocolates.

Initially Bob is at the position (0, 3) and he can follow the path (0, 3) -> (1,3) -> (2, 3) and will colllect 2 + 2 + 5 = 9 chocolates.

Hence the total number of chocolates collected will be 12 + 9 = 21. there is no other possible way to collect a greater number of chocolates than 21.
Detailed explanation ( Input/output format, Notes, Images )
Constraints :
1 <= ‘T’ <= 10
2 <= 'R', 'C' <= 50
0 <= 'GRID[i][j]'<= 10^2
Time Limit: 1sec
Sample Input 1 :
2
3 4
2 3 1 2
3 4 2 2
5 6 3 5
2 2
1 1
1 2
Sample Output 1 :
21
5
Explanation Of Sample Input 1 :
For the first test case, Initially Alice is at the position (0, 0) he can follow the path (0, 0) -> (1, 1) -> (2, 1) and will collect 2 + 4 + 6 = 12 chocolates.

Initially Bob is at the position (0, 3) and he can follow the path (0, 3) -> (1, 3) -> (2, 3) and will collect 2 + 2 + 5 = 9 chocolates.

Hence the total number of chocolates collected will be 12 + 9 = 21.

For the second test case, Alice will follow the path (0, 0) -> (1, 0) and Bob will follow the path (0, 1) -> (1, 1). total number of chocolates collected will be 1 + 1 + 1 + 2 = 5
Sample Input 2 :
2
2 2
3 7
7 6
3 2
4 5
3 7
4 2
Sample Output 2 :
23
25
    * */

    public static void main(String[] args) {
        int[][] grid = new int[][]{
                {2,3,1,2},
                {3,4,2,2},
                {5,6,3,5}
        };

        System.out.println("max choc , with basic recursion - "+maximumChocolates(3,4,grid));

        System.out.println("max choc , with memo - "+maximumChocolates_memo(3,4,grid));
    }

    public static int maximumChocolates(int r, int c, int[][] grid) {
        int r1=0,c1=0; // initial alice position
        int r2=0,c2=c-1; // initial bob position
        return calculateMax(r1,c1,r2,c2,r,c,grid);
    }

    public static int calculateMax(int r1, int c1, int r2, int c2,int r, int c, int[][] grid) {
        if(r1>r-1 || r2>r-1 || c1<0 || c2<0 || c1>c-1 || c2>c-1){ // check boundary condition
            //System.out.println("boundary");
            return 0;
        }

        int maxAlice=0;
        int maxBob=0;

        if(r1==r2 && c1==c2){
            //System.out.println("same cell");
            maxAlice += grid[r1][c1]; // if on same cell, only one of them gets the chocolates
        }else{
            //System.out.println("diff cell");
            maxAlice += grid[r1][c1];
            maxBob += grid[r2][c2];
        }

        int total = maxAlice + maxBob;
        //System.out.println("total - "+total);


        int[] arr = new int[9];
        // alice moves left, bob moves left
        arr[0] = calculateMax(r1+1, c1-1, r2+1, c2-1, r, c, grid);
        // alice moves left, bob moves down
        arr[1] = calculateMax(r1+1, c1-1, r2+1, c2, r, c, grid);
        // alice moves left, bob moves right
        arr[2] = calculateMax(r1+1, c1-1, r2+1, c2+1, r, c, grid);


        // alice moves down, bob moves left
        arr[3] = calculateMax(r1+1, c1, r2+1, c2-1, r, c, grid);
        // alice moves down, bob moves down
        arr[4] = calculateMax(r1+1, c1, r2+1, c2, r, c, grid);
        // alice moves down, bob moves right
        arr[5] = calculateMax(r1+1, c1, r2+1, c2+1, r, c, grid);

        // alice moves right, bob moves left
        arr[6] = calculateMax(r1+1, c1+1, r2+1, c2-1, r, c, grid);
        // alice moves right, bob moves down
        arr[7] = calculateMax(r1+1, c1+1, r2+1, c2, r, c, grid);
        // alice moves right, bob moves right
        arr[8] = calculateMax(r1+1, c1+1, r2+1, c2+1, r, c, grid);

        int totalMax = 0;
        for(int i=0; i<arr.length; i++){
            totalMax = Math.max(totalMax, arr[i]);
        }
        //System.out.println("returning - "+(total + totalMax));
        return total + totalMax;
    }

    /*memoization*/

    public static int maximumChocolates_memo(int r, int c, int[][] grid) {
        int r1=0,c1=0; // initial alice position
        int c2=c-1; // initial bob position
        Object[][] obj = new Object[r][c];
        return calculateMax_memo(r1,c1,c2,r,c,grid,obj);
    }

    public static int calculateMax_memo(int r1, int c1, int c2, int r, int c, int[][] grid, Object[][] obj) {
        if(r1>r-1 || c1<0 || c2<0 || c1>c-1 || c2>c-1){ // check boundary condition
            //System.out.println("boundary");
            return 0;
        }

        if(obj[c1][c2]!=null && ((Map<Integer,Integer>)obj[c1][c2])!=null && ((Map<Integer,Integer>)obj[c1][c2]).get(r1)!=null){
            return ((Map<Integer,Integer>)obj[c1][c2]).get(r1);
        }

        int maxAlice=0;
        int maxBob=0;

        if(c1==c2){
            //System.out.println("same cell");
            maxAlice += grid[r1][c1]; // if on same cell, only one of them gets the chocolates
        }else{
            //System.out.println("diff cell");
            maxAlice += grid[r1][c1];
            maxBob += grid[r1][c2];
        }

        int total = maxAlice + maxBob;
        //System.out.println("total - "+total);


        int[] arr = new int[9];
        // alice moves left, bob moves left
        arr[0] = calculateMax_memo(r1+1, c1-1,  c2-1, r, c, grid, obj);
        // alice moves left, bob moves down
        arr[1] = calculateMax_memo(r1+1, c1-1,  c2, r, c, grid, obj);
        // alice moves left, bob moves right
        arr[2] = calculateMax_memo(r1+1, c1-1,  c2+1, r, c, grid, obj);


        // alice moves down, bob moves left
        arr[3] = calculateMax_memo(r1+1, c1,  c2-1, r, c, grid, obj);
        // alice moves down, bob moves down
        arr[4] = calculateMax_memo(r1+1, c1,  c2, r, c, grid, obj);
        // alice moves down, bob moves right
        arr[5] = calculateMax_memo(r1+1, c1,  c2+1, r, c, grid, obj);

        // alice moves right, bob moves left
        arr[6] = calculateMax_memo(r1+1, c1+1,  c2-1, r, c, grid, obj);
        // alice moves right, bob moves down
        arr[7] = calculateMax_memo(r1+1, c1+1,  c2, r, c, grid, obj);
        // alice moves right, bob moves right
        arr[8] = calculateMax_memo(r1+1, c1+1,  c2+1, r, c, grid, obj);

        int totalMax = 0;
        for(int i=0; i<arr.length; i++){
            totalMax = Math.max(totalMax, arr[i]);
        }
        //System.out.println("returning - "+(total + totalMax));
        if(obj[c1][c2]==null){
            Map<Integer,Integer> m = new HashMap<>();
            m.put(r1,total + totalMax);
            obj[c1][c2] = m;
        }else{
            if(((Map<Integer,Integer>)obj[c1][c2]).get(r1) != null){
                Map<Integer,Integer> m = (Map<Integer,Integer>)obj[c1][c2];
                m.put(r1,Math.max(total + totalMax,m.get(r1)));
                obj[c1][c2] = m;
            }
        }
        return total + totalMax;
    }
}
