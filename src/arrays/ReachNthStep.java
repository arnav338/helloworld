package arrays;

public class ReachNthStep {
	/*
	 * There are n stairs, a person standing at the bottom wants to reach the top. 
	 * The person can climb either 1 stair or 2 stairs at a time. 
	 * Count the number of ways, the person can reach the top.
	 * 
	 * We create a table res[] in bottom up manner using the following relation: 

		res[i] = res[i] + res[i-j] for every (i-j) >= 0
		such that the ith index of the array will contain the number of ways required to 
		reach the ith step considering all the possibilities of climbing (i.e. from 1 to i).
	 * */
	public static void main(String[] args) {
		int s = 4, m = 2;
        System.out.println("Number of ways = "  + countWays(s, m));
	}
	// A recursive function used by countWays
    static int countWaysUtil(int n, int m)
    {
        int res[] = new int[n];
        res[0] = 1; // to go till 0th stair only 1 way is possible ,i.e., take no step
        res[1] = 1; // to go till 1st stair only 1 way is possible ,i.e., take 1 step
        for (int i = 2; i < n; i++) {
            res[i] = 0;
            //for (int j = 1; j <= m && j <= i; j++) 
            // m is the max steps we can take and i is the destination step we are trying to reach to
            // we are taking min of m and i as we cant take more than m steps in this iteration
            // and we are taking i as we cant reach more than the destination and i represents the destination step
            for (int j = 1; j <= Math.min(m, i); j++)
            	res[i] += res[i - j];
            // to handle large numbers 
            // res[i] = (res[i] % ( (int) Math.pow(10,9)+7) + res[i-j] % ( (int) Math.pow(10,9)+7)) %  ( (int) Math.pow(10,9)+7);
        }
        return res[n - 1];
    }
    
    // Returns number of ways to reach s'th stair
    static int countWays(int s, int m)
    {
    	// we pass s+1 as we are also considering the case of 0th stair
    	// ,i.e., to go till 0th stair only 1 way is possible
        return countWaysUtil(s + 1, m); 
    }
    /*
    int countWays(int n)
    {
        int res = findWays(n+1, 2);
        res = res % ( (int) Math.pow(10,9)+7);
        return res;
    }
    
    public int findWays(int n, int m){
        int[] res = new int[n];
        res[0] = 1;
        res[1] = 1;
        for(int i=2; i<n; i++){
            res[i] = 0;
            for(int j=0; j<=m && j<=i ; j++){
                res[i] = (res[i] % ( (int) Math.pow(10,9)+7) + res[i-j] % ( (int) Math.pow(10,9)+7)) %  ( (int) Math.pow(10,9)+7);
            }
        }
        return res[n-1];
    }*/
}
