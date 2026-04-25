package arrays;

import java.util.Arrays;

public class Ninja_Training {
    /*
    Ninja is planing this ‘N’ days-long training schedule. Each day, he can perform any one of these three activities. (Running, Fighting Practice or Learning New Moves). Each activity has some merit points on each day. As Ninja has to improve all his skills, he can’t do the same activity in two consecutive days. Can you help Ninja find out the maximum merit points Ninja can earn?

You are given a 2D array of size N*3 ‘POINTS’ with the points corresponding to each day and activity. Your task is to calculate the maximum number of merit points that Ninja can earn.

For Example
If the given ‘POINTS’ array is [[1,2,5], [3 ,1 ,1] ,[3,3,3] ],the answer will be 11 as 5 + 3 + 3.
Detailed explanation ( Input/output format, Notes, Images )
Constraints:
1 <= T <= 10
1 <= N <= 100000.
1 <= values of POINTS arrays <= 100 .

Time limit: 1 sec
Sample Input 1:
2
3
1 2 5
3 1 1
3 3 3
3
10 40 70
20 50 80
30 60 90
Sample Output 1:
11
210
Explanation of sample input 1:
For the first test case,
One of the answers can be:
On the first day, Ninja will learn new moves and earn 5 merit points.
On the second day, Ninja will do running and earn 3 merit points.
On the third day, Ninja will do fighting and earn 3 merit points.
The total merit point is 11 which is the maximum.
Hence, the answer is 11.

For the second test case:
One of the answers can be:
On the first day, Ninja will learn new moves and earn 70 merit points.
On the second day, Ninja will do fighting and earn 50 merit points.
On the third day, Ninja will learn new moves and earn 90 merit points.
The total merit point is 210 which is the maximum.
Hence, the answer is 210.
Sample Input 2:
2
3
18 11 19
4 13 7
1 8 13
2
10 50 1
5 100 11
Sample Output 2:
45
110
    * */

    public static void main(String[] args) {
        int[][] arr = new int[][]{
                                    {1,2,5},
                                    {3,1,1},
                                    {3,3,3}
                                };

//        int[][] arr = new int[][]{
//                {83, 66, 76 },
//                {40, 15, 72 },
//                {14, 61, 88 },
//                {63, 35, 21 },
//                {58, 24, 61 },
//                {91, 10, 33 },
//                {14, 3 ,10 },
//                {54, 84, 39 },
//                {53, 32, 54 },
//                {66, 85, 70 },
//                {77, 2 ,22 },
//                {56, 56 ,80 },
//                {59, 17 ,99 },
//                {5 ,43 ,38 },
//                {65, 67, 7 },
//                {72, 46 ,81 },
//                {45, 78 ,74 },
//                {8, 41 ,56 },
//                {50, 25 ,55 },
//                {8, 47 ,57 },
//                {93, 36, 36 },
//                {98, 51, 99 },
//                {16, 90, 14 },
//                {64, 60, 14 },
//                {94, 66, 59 },
//                {45, 78, 19 },
//                {18, 90, 15 },
//                {95, 90, 79 },
//                {64, 26, 5 },
//                {13, 80, 67 }
//        };


//        int i = ninjaTraining(arr.length, arr);
//        System.out.println("bottom up - "+i);

//        int j = ninjaTraining_topDown(arr.length, arr);
//        System.out.println("top down without memo - "+j);

//        int k = ninjaTraining_topDown_withMemo(arr.length, arr);
//        System.out.println("top down with memo - "+k);

        int m = ninjaTraining_topDown_withTabulation(arr.length, arr);
        System.out.println("top down with tabulation - "+m);


    }

    public static int ninjaTraining(int n, int points[][]) {
        int res;
        int i=0, k=0; // starting row
        int last = -1;
        res = findMaxPoints(i,k,points, last);
        return res;
    }
    public static int findMaxPoints(int i, int k, int points[][], int last) {
        //System.out.println("i - "+i+", k - "+k);
        if(i>=points.length || k>=points[0].length){
            return 0;
        }
        int max;
        int take = last!=k ? points[i][k] + findMaxPoints(i+1, 0, points, k) : Integer.MIN_VALUE;
        int notTake = findMaxPoints(i, k+1,  points, last);
        max = Math.max(take, notTake);
        return max;
    }

    public static int ninjaTraining_topDown(int n, int points[][]) {
        int res;
        int i=points.length-1, k=points[0].length-1; // starting position
        int last = -1;
        res = findMaxPoints_topDown(i,k,points, last);
        return res;
    }

    public static int findMaxPoints_topDown(int i, int k, int points[][], int last) {
        //System.out.println("row : "+i+", column : "+k);
        if(i<0 || k < 0){
            return 0;
        }
        if(i==0 && k==0){
            return points[i][k];
        }
        int max;
        int take = Integer.MIN_VALUE, notTake = Integer.MIN_VALUE;
        if(last!=k){
            take = points[i][k] + findMaxPoints_topDown(i-1, points[0].length-1, points, k);
        }
        notTake = findMaxPoints_topDown(i, k-1,  points, last);
        max = Math.max(take, notTake);
        return max;
    }

    public static int ninjaTraining_topDown_withMemo(int n, int points[][]) {
        int res;
        int i=points.length-1, k=points[0].length-1; // starting position
        int last = 3;
        int[][] dp = new int[points.length][points[0].length+1];
        for(int l = 0; l<points.length; l++){
            Arrays.fill(dp[l], -1);
        }
        res = findMaxPoints_topDown_withMemo(i,points, last,dp);
        return res;
    }

    public static int findMaxPoints_topDown_withMemo(int i, int[][] points, int last, int[][] dp) {
        //System.out.println("row : "+i+", column : "+k);
        if(i<0){
            return 0;
        }
        if(dp[i][last] != -1){
            return dp[i][last];
        }
        if(i==0){
            int maxPpoint = 0;
            for(int p = 0; p<points[0].length; p++){
                if(p!=last){
                    maxPpoint = Math.max(maxPpoint,points[i][p]);
                }
            }
            return maxPpoint;
        }
        int max = Integer.MIN_VALUE;
        for(int q=0; q<points[0].length; q++){
            if(q!=last){
                int point = points[i][q] + findMaxPoints_topDown_withMemo(i-1,points,q,dp);
                max = Math.max(max,point);
            }
        }
        dp[i][last] = max;
        return max;
    }

    public static int ninjaTraining_topDown_withTabulation(int n, int[][] points) {
        int[][] dp = new int[points.length][points[0].length+1];
        for(int l = 0; l<points.length; l++){
            Arrays.fill(dp[l], -1);
        }
        dp[0][0] = Math.max(points[0][1],points[0][2]);
        dp[0][1] = Math.max(points[0][0],points[0][2]);
        dp[0][2] = Math.max(points[0][1],points[0][0]);
        dp[0][3] = Math.max(points[0][0],Math.max(points[0][1],points[0][2]));
        for (int day = 1; day<points.length; day++){ // iterating for each day
            for (int last = 0; last<4; last++){ // possible values of which activity was undertaken on previous day
                dp[day][last] = 0;
                for (int task = 0; task<3; task++) { // for each possible activity undertaken on the last day, we can have these activities on current day
                    if(task != last){
                        int curr = points[day][task] + dp[day-1][task];
                        /*
                        value in dp array depicts that what is the maximum points we can obtain on current day provided that a certain task has been done on the previous day
                        eg : value of dp[1][2] depicts what is the maximum points we can achieve on day 1 provided that task 2 has already been done on previous day

                        task is the variable that ensures that we do not take into consideration the activity that was undertaken on the previous day
                        hence we calculate curr as
                        int curr = points[day][task] + dp[day-1][task];
                        instead of
                        int curr = points[day][task] + dp[day-1][last];

                        since if calculate it as -> int curr = points[day][task] + dp[day-1][last];
                        then those cases will also be considered where previous day task is same as the current task in consideration which is wrong

                        * */
                        dp[day][last] = Math.max(dp[day][last],curr);
                        System.out.println("day : "+day+", last : "+last+", task : "+task+", curr : "+curr);
                    }
                }
            }
        }
        return dp[points.length-1][3];
    }
}
