package main;

public class RatInAMaze extends Child6 {

	public static void main(String[] args) {
		/*
		int[][] arr = { { 1, 0, 0, 1 }, 
						{ 1, 1, 1, 0 }, 
						{ 1, 1, 0, 0 }, 
						{ 0, 1, 1, 1 } 
						};
		*/
		int[][] arr = { { 1, 1, 1, 1 }, 
						{ 0, 0, 0, 1 }, 
						{ 0, 0, 0, 1 }, 
						{ 0, 0, 0, 1 } 
				};
		solveMaze(arr);
	}

	private static void solveMaze(int[][] arr) {
		int[][] solution = new int[arr.length][arr.length];
		int x = 0;
		int y = 0;
		solve(solution, arr, x, y);
		for (int i = 0; i < solution.length; i++) {
			for (int j = 0; j < solution.length; j++) {
				System.out.print(solution[i][j]+" ");
			}
			System.out.println("");
		}
	}
	private static int solve(int[][] solution, int[][] input, int x, int y) {
		if(solution[input.length - 1][input.length - 1]==0) {
			int c = input[x][y];
			System.out.println(x + "||" + y + " value : " + c);
			if (checkBoundaries(x,y,input)) {
				return 0;
			}
			if (input[x][y] == 0) {
				System.out.println("GO back !!!");
				return 0;
			}
			if (x == input.length - 1 && y == input.length - 1) {
				System.out.println("Destination reached");
				solution[input.length - 1][input.length - 1] = 1;
				System.out.println("------------");
				return 1;
			}
			if(solution[input.length - 1][input.length - 1] == 0) {
				if (input[x][y] == 1) {
					solution[x][y] = 1;
				}
				// move right
				if (y + 1 <= input.length - 1 && solution[x][y + 1] == 0  && solution[input.length - 1][input.length - 1] == 0) {
					System.out.println("trying to move right");
					if (solve(solution, input, x, y + 1) == 0 && solution[input.length - 1][input.length - 1] == 0) {
						solution[x][y + 1] = 0;
					}
				}
				// move down
				if (y+1<input.length-1  && x + 1 <= input.length - 1 && solution[x + 1][y] == 0  && solution[input.length - 1][input.length - 1] == 0) {
					System.out.println("trying to move down");
					if(solution[x][y+1]==1 && solution[input.length - 1][input.length - 1] == 0) {
						solution[x][y+1] = 0;
					}
					if (solve(solution, input, x + 1, y) == 0 && solution[input.length - 1][input.length - 1] == 0) {
						solution[x + 1][y] = 0;
					}
				}
				// move left
				if (y - 1 >= 0  && solution[input.length - 1][input.length - 1] == 0) {
					System.out.println("trying to move left");
					if (solve(solution, input, x, y - 1) == 0 && solution[input.length - 1][input.length - 1] == 0) {
						solution[x][y - 1] = 0;
					}
				}
			}
			
		}
	return 0;

}
	
	/*
	 * working for 1st input
	private static int solve(int[][] solution, int[][] input, int x, int y) {
			if(solution[input.length - 1][input.length - 1]==0) {
				int c = input[x][y];
				System.out.println(x + "||" + y + " value : " + c);
				if (checkBoundaries(x,y,input)) {
					return 0;
				}
				if (input[x][y] == 0) {
					System.out.println("GO back !!!");
					return 0;
				}
				if (x == input.length - 1 && y == input.length - 1) {
					System.out.println("Destination reached");
					solution[input.length - 1][input.length - 1] = 1;
					System.out.println("------------");
					return 1;
				}
				if(solution[input.length - 1][input.length - 1] == 0) {
					if (input[x][y] == 1) {
						solution[x][y] = 1;
					}
					// move right
					if (y + 1 <= input.length - 1 && solution[x][y + 1] == 0  && solution[input.length - 1][input.length - 1] == 0) {
						System.out.println("trying to move right");
						if (solve(solution, input, x, y + 1) == 0 && solution[input.length - 1][input.length - 1] == 0) {
							solution[x][y + 1] = 0;
						}
					}
					// move down
					if (y+1<input.length-1  && x + 1 <= input.length - 1 && solution[x + 1][y] == 0  && solution[input.length - 1][input.length - 1] == 0) {
						System.out.println("trying to move down");
						if(solution[x][y+1]==1 && solution[input.length - 1][input.length - 1] == 0) {
							solution[x][y+1] = 0;
						}
						if (solve(solution, input, x + 1, y) == 0 && solution[input.length - 1][input.length - 1] == 0) {
							solution[x + 1][y] = 0;
						}
					}
					// move left
					if (y - 1 >= 0  && solution[input.length - 1][input.length - 1] == 0) {
						System.out.println("trying to move left");
						if (solve(solution, input, x, y - 1) == 0 && solution[input.length - 1][input.length - 1] == 0) {
							solution[x][y - 1] = 0;
						}
					}
				}
				
			}
		return 0;

	}
	*/
	private static boolean checkBoundaries(int x, int y, int[][] input) {
		return (x > input.length - 1 || y > input.length - 1 || x < 0 || y < 0)==true;
	}
	
}
