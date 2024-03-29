package arrays;

public class NumberOfWaysToReachDestination {
	/*
	 * Given a maze with obstacles, count the number of paths to reach the rightmost-bottommost 
	 * cell from the topmost-leftmost cell. A cell in the given maze has a value of -1 
	 * if it is a blockage or dead-end, else 0. From a given cell, 
	 * we are allowed to move to cells (i+1, j) and (i, j+1) only.
	 * 
	 * The idea is to modify the given grid[][] so that grid[i][j] 
	 * contains count of paths to reach (i, j) from (0, 0) if (i, j) 
	 * is not a blockage, else grid[i][j] remains -1. 

		We can recursively compute grid[i][j] using below 
		formula and finally return grid[R-1][C-1]
		
		  // If current cell is a blockage
		  if (maze[i][j] == -1)
		      maze[i][j] = -1; //  Do not change
		
		  // If we can reach maze[i][j] from maze[i-1][j] then increment count.
		  else if (maze[i-1][j] > 0)
		      maze[i][j] = (maze[i][j] + maze[i-1][j]);
		
		  // If we can reach maze[i][j] from maze[i][j-1] then increment count.
		  else if (maze[i][j-1] > 0)
		      maze[i][j] = (maze[i][j] + maze[i][j-1]);
			 * 
	 * */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	static int R = 4;
    static int C = 4;
     
    // Returns count of possible paths in a maze[R][C] from (0,0) to (R-1,C-1)
    static int countPaths(int maze[][])
    {
        // If the initial cell is blocked, there is no way of moving anywhere
        if (maze[0][0]==-1)
            return 0;
     
        // Initializing the leftmost column
        for (int i = 0; i < R; i++)
        {
            if (maze[i][0] == 0)
                maze[i][0] = 1;
            // If we encounter a blocked cell in leftmost row, there is no way
            // of visiting any cell directly below it.
            else
                break;
        }
     
        // Similarly initialize the topmost row
        for (int i =1 ; i< C ; i++)
        {
            if (maze[0][i] == 0)
                maze[0][i] = 1;
            // If we encounter a blocked cell in bottommost row, there is no way of
            // visiting any cell directly below it.
            else
                break;
        }
     
        // The only difference is that if a cell is -1, simply ignore it else recursively
        // compute count value maze[i][j]
        for (int i = 1; i < R; i++)
        {
            for (int j = 1; j <C ; j++)
            {
                // If blockage is found, ignore this cell
                if (maze[i][j] == -1)
                    continue;
     
                // If we can reach maze[i][j] from maze[i-1][j] then increment count.
                if (maze[i - 1][j] > 0)
                    maze[i][j] = (maze[i][j] +  maze[i - 1][j]);
                // If we can reach maze[i][j] from  maze[i][j-1] then increment count.
                if (maze[i][j - 1] > 0)
                    maze[i][j] = (maze[i][j] +    maze[i][j - 1]);
            }
        }
     
        // If the final cell is blocked, output 0, otherwise the answer
        return (maze[R - 1][C - 1] > 0) ? maze[R - 1][C - 1] : 0;
    }
}
