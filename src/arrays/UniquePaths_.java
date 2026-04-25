package arrays;

import java.util.Arrays;

public class UniquePaths_ {
    /*
    You are present at point ‘A’ which is the top-left cell of an M X N matrix, your destination is point ‘B’, which is the bottom-right cell of the same matrix. Your task is to find the total number of unique paths from point ‘A’ to point ‘B’.In other words, you will be given the dimensions of the matrix as integers ‘M’ and ‘N’, your task is to find the total number of unique paths from the cell MATRIX[0][0] to MATRIX['M' - 1]['N' - 1].

To traverse in the matrix, you can either move Right or Down at each step. For example in a given point MATRIX[i] [j], you can move to either MATRIX[i + 1][j] or MATRIX[i][j + 1].

Detailed explanation ( Input/output format, Notes, Images )
Constraints:
1 ≤ T ≤ 100
1 ≤ M ≤ 15
1 ≤ N ≤ 15

Where ‘M’ is the number of rows and ‘N’ is the number of columns in the matrix.

Time limit: 1 sec
Sample Input 1:
2
2 2
1 1
Sample Output 1:
2
1
Explanation of Sample Output 1:
In test case 1, we are given a 2 x 2 matrix, to move from matrix[0][0] to matrix[1][1] we have the following possible paths.

Path 1 = (0, 0) -> (0, 1) -> (1, 1)
Path 2 = (0, 0) -> (1, 0) -> (1, 1)

Hence a total of 2 paths are available, so the output is 2.

In test case 2, we are given a 1 x 1 matrix, hence we just have a single cell which is both the starting and ending point. Hence the output is 1.
Sample Input 2:
2
3 2
1 6
Sample Output 2:
3
1
Explanation of Sample Output 2:
In test case 1, we are given a 3 x 2 matrix, to move from matrix[0][0] to matrix[2][1] we have the following possible paths.

Path 1 = (0, 0) -> (0, 1) -> (1, 1) -> (2, 1)
Path 2 = (0, 0) -> (1, 0) -> (2, 0) -> (2, 1)
Path 3 =  (0, 0) -> (1, 0) -> (1, 1) -> (2, 1)

Hence a total of 3 paths are available, so the output is 3.

In test case 2, we are given a 1 x 6 matrix, hence we just have a single row to traverse and thus total path will be 1.
    * */

    public static void main(String[] args) {

        //System.out.println("unique paths - "+uniquePaths(2,2));
        long start = System.currentTimeMillis();
        System.out.println("unique paths - "+uniquePaths(4,3));
        //System.out.println("took - "+(System.currentTimeMillis()-start));

        start = System.currentTimeMillis();
        System.out.println("unique paths memo - "+uniquePaths_memo(4,3));
        //System.out.println("took - "+(System.currentTimeMillis()-start));

        start = System.currentTimeMillis();
        System.out.println("unique paths tabulation - "+uniquePaths_tabulation(4,3));

        start = System.currentTimeMillis();
        System.out.println("unique paths tabulation with space optimization - "+uniquePaths_tabulation_with_space_optimization(4,3));


    }


    public static int uniquePaths(int m, int n) {
        if(m==1) return 1;
        int r=0, c=0; // starting position
        int paths = countPaths(r,c,m,n);
        return paths;
    }

    public static int countPaths(int r, int c,int m, int n) {
        if(r==m-1 && c == n-1){ // we reach last cell
            return 1;
        }
        if(invalidCell(r,c,m,n)){
            return 0;// we go out of boundary
        }
        return countPaths(r, c+1, m, n) + countPaths(r+1, c, m, n);
    }

    public static boolean invalidCell(int r, int c,int m, int n) {
        if(r>m-1 || c>n-1){
            return true;
        }
        return false;
    }

    /* in above approach we see that we may traverse similar subproblems since
        we can traverse 1 path more than once since there is backtracking involved
        hence we can pre calculate sum using memoization
    *  */

    /*this is a recursive bottom up approach as we are starting from initial position
    and combining subproblems to find solution

    TC - O(n*m)
    SC - (n*m + n*m)

    * */
    public static int uniquePaths_memo(int m, int n) {
        if(m==1) return 1;
        int r=0, c=0; // starting position
        int[][] dp = new int[m][n];
        for(int[] ar: dp){
            Arrays.fill(ar,-1);
        }
        return countPaths_memo(r,c,m,n,dp);
    }

    public static int countPaths_memo(int r, int c, int m, int n, int[][] dp) {
        if(r==m-1 && c == n-1){ // we reach last cell
            return 1;
        }
        if(invalidCell(r,c,m,n)){
            return 0;// we go out of boundary
        }
        if(dp[r][c]!=-1){
            return dp[r][c];
        }
        dp[r][c] = countPaths_memo(r, c+1, m, n, dp) + countPaths_memo(r+1, c, m, n, dp);
        return dp[r][c];
    }

    /*
    tabulation approach
    * */

    public static int uniquePaths_tabulation(int m, int n) {
        if(m==1) return 1;
        int[][] dp = new int[m][n];
        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                if(i==0 && j==0){
                    dp[0][0] = 1;
                }else{
                    int up = 0;
                    if (i>0) up = dp[i-1][j];
                    int left = 0;
                    if (j>0) left = dp[i][j-1];
                    dp[i][j] = up + left;
                }
            }
        }
        return dp[m-1][n-1];
    }

    public static int uniquePaths_tabulation_with_space_optimization(int m, int n) {
        if(m==1) return 1;
        //int[][] dp = new int[m][n];
        int[] prev = new int[n];
        //Arrays.fill(prev,1);
        for(int i=0; i<m; i++){
            int[] temp = new int[n];
            for(int j=0; j<n; j++){
                if(i==0 && j==0){
                    temp[0] = 1;
                }else{
                    int up = 0;
                    if (i>0) up = prev[j];
                    int left = 0;
                    if (j>0) left = temp[j-1];
                    temp[j] = up + left;
                }
            }
            prev = temp;
        }
        return prev[n-1];
    }


}
