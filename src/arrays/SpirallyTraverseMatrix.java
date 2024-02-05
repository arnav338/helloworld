package arrays;

import java.util.ArrayList;

public class SpirallyTraverseMatrix {
	public static void main(String[] args) {
		/*
		Given a matrix of size r*c. Traverse the matrix in spiral form.

		Example 1:

		Input:
		r = 4, c = 4
		matrix[][] = {  {1, 2, 3, 4},
		           		{5, 6, 7, 8},
		           		{9, 10, 11, 12},
		           		{13, 14, 15,16}
		           	}
		Output: 
		1 2 3 4 8 12 16 15 14 13 9 5 6 7 11 10
		*/
		int[][] input = {   {1, 2, 3, 4},
							{5, 6, 7, 8},
							{9, 10, 11, 12},
							{13, 14, 15,16}
		};
		/*
		int[][] input = {		{1, 2, 3, 4},
		           				{5, 6, 7, 8},
		           				{9, 10, 11, 12}
		           		};
		
		int[][] input = {   {1, 2, 3, 4},
           					{5, 6, 7, 8},
           					{9, 10, 11, 12},
           					{13, 14, 15,16}
           	};
		int[][] input = {   {5, 11, 30},
							{5, 19, 19}
		};*/
		ArrayList<Integer> result = new ArrayList<Integer>();
		traverse(input,result,4,4);
		result.stream().forEach(a -> System.out.print(a+"||"));
	}
	
	private static void traverse(int[][] input, ArrayList<Integer> result, int r, int c) {
		if(input == null) return;
		int i = 0;
		int j = 0;
		boolean[][] seen = new boolean[r][c];
		trav(input,result,i,j,r,c,false,seen);
	}
	
	private static void trav(int[][] input, ArrayList<Integer> result, int i, int j, int r, int c, boolean continueUp, boolean[][] seen) {
		int curr = input[i][j];
		result.add(input[i][j]);
		seen[i][j] = true;
		if(i-1 >=0 && result.contains(input[i-1][j])) {
			continueUp = false;
		}
		if( j+1 <= c-1 && !continueUp) { // trying to move right
			if(!seen[i][j+1]) {
				trav(input, result, i, j+1, r, c,continueUp, seen);
			}
		}
		if(i+1 <= r-1 && !continueUp) {// trying to move down
			if(!seen[i+1][j]) {
				trav(input, result, i+1, j, r, c,continueUp, seen);
			}
		}
		if(j-1 >= 0 && !continueUp) {// trying to move left
			if(!seen[i][j-1]) {
				trav(input, result, i, j-1, r, c,continueUp, seen);
			}
		}
		
		if(i-1 >= 0 ) {// trying to move up
			if(!seen[i-1][j]) {
				continueUp = true;
				trav(input, result, i-1, j, r, c,continueUp, seen);
			}
		}
		
	}
	
}
