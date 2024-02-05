package dynamic;

import java.util.Arrays;

public class KnapSack {
	public static void main(String[] args) {
		/*
		 * 
		 * Consider a thief gets into a home to rob and he carries a knapsack. 
		 * There are fixed number of items in the home — each with its own weight and value — 
		 * Jewelry, with less weight and highest value vs tables, 
		 * with less value but a lot heavy. To add fuel to the fire, 
		 * the thief has an old knapsack which has limited capacity. 
		 * Obviously, he can’t split the table into half or jewelry into 3/4ths. 
		 * He either takes it or leaves it.
		 * Aim is to maximise the values of objects that he can carry in bag
		 * 
		 * Suppose we have a knapsack which can hold int w = 10 weight units. 
		 * We have a total of int n = 4 items to choose from, 
		 * whose values are represented by an array int[] val = {10, 40, 30, 50} 
		 * and weights represented by an array int[] wt = {5, 4, 6, 3}.
		 * 
		 * */
		
		int w = 10;
        int n = 4;
        int[] val = {10, 40, 30, 50};
        int[] wt = {5, 4, 6, 3};

        
        /*
        First, we create a 2-dimensional array (i.e. a table) of n + 1 rows and w + 1 columns.
        
        A row number i represents the set of all the items from rows 1— i. 
        For instance, the values in row 3 assumes that we only have items 1, 2, and 3.
        A column number j represents the weight capacity of our knapsack. 
        Therefore, the values in column 5, for example, assumes that our knapsack can hold 5 weight units.
        Putting everything together, an entry in row i, column j represents the maximum value that 
        can be obtained with items 1, 2, 3 … i, in a knapsack that can hold j weight units.
        */
        int[][] mat = new int[n + 1][w + 1];
        
        // Populate base cases
        /*
         * We can immediately begin filling some entries in our table: the base cases, 
         * for which the solution is trivial. 
         * For instance, at row 0, when we have no items to pick from, 
         * the maximum value that can be stored in any knapsack must be 0. 
         * Similarly, at column 0, for a knapsack which can hold 0 weight units, 
         * the maximum value that can be stored in it is 0. 
         * */
        for (int r = 0; r < w + 1; r++) {
            mat[0][r] = 0;
        }
        for (int c = 0; c < n + 1; c++) {
            mat[c][0] = 0;
        }
        
        // Main logic
        /*
         * The maximum value that we can obtain without item i can be found at row i-1, column j. This part’s easy. The reasoning is straightforward: whatever maximum value we can obtain with items 1, 2, 3 … i must obviously be the same maximum value we can obtain with items 1, 2, 3 … i - 1, if we choose not to include item i.
         * 
         * To calculate the maximum value that we can obtain with item i, we first need to compare the weight of item i with the knapsack’s weight capacity. Obviously, if item i weighs more than what the knapsack can hold, we can’t include it, so it does not make sense to perform the calculation. In that case, the solution to this problem is simply the maximum value that we can obtain without item i (i.e. the value in the row above, at the same column).
         * 
         * However, suppose that item i weighs less than the knapsack’s capacity. We thus have the option to include it, if it potentially increases the maximum obtainable value. The maximum obtainable value by including item i is thus = the value of item i itself + the maximum value that can be obtained with the remaining capacity of the knapsack. We obviously want to make full use of the capacity of our knapsack, and not let any remaining capacity go to waste.
Therefore, at row i and column j (which represents the maximum value we can obtain there), we would pick either the maximum value that we can obtain without item i, or the maximum value that we can obtain with item i, whichever is larger.
         * 
         * */
        for (int item = 1; item <= n; item++) {
            for (int capacity = 1; capacity <= w; capacity++) {
                int maxValWithoutCurr = mat[item - 1][capacity]; // This is guaranteed to exist, as we have populated the base cases initially only
                int maxValWithCurr = 0; // We initialize this value to 0
                
                int weightOfCurr = wt[item - 1]; // We use item -1 to account for the extra row at the top
                if (capacity >= weightOfCurr) { // We check if the knapsack can fit the current item
                    maxValWithCurr = val[item - 1]; // If so, maxValWithCurr is at least the value of the current item
                    
                    int remainingCapacity = capacity - weightOfCurr; // remainingCapacity must be at least 0
                    maxValWithCurr += mat[item - 1][remainingCapacity]; // Add the maximum value obtainable with the remaining capacity
                }
                
                mat[item][capacity] = Math.max(maxValWithoutCurr, maxValWithCurr); // Pick the larger of the two
            }
        }
        
        System.out.println(mat[n][w]); // Final answer
        System.out.println(Arrays.deepToString(mat)); // Visualization of the table
		
	}
}
