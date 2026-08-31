package graph;

public class NumberOfIslands {
    /*
    Given an m x n 2D binary grid grid which represents a map of '1's (land) and '0's (water), return the number of islands.

An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid are all surrounded by water.



Example 1:

Input: grid = [
  ["1","1","1","1","0"],
  ["1","1","0","1","0"],
  ["1","1","0","0","0"],
  ["0","0","0","0","0"]
]
Output: 1
Example 2:

Input: grid = [
  ["1","1","0","0","0"],
  ["1","1","0","0","0"],
  ["0","0","1","0","0"],
  ["0","0","0","1","1"]
]
Output: 3
    * */

    public int numIslands(char[][] grid) {
        boolean[][] vis = new boolean[grid.length][grid[0].length];
        int res = 0;
        for(int i=0; i<grid.length; i++){
            for(int j=0; j<grid[0].length; j++){
                if(grid[i][j] == '1' && !vis[i][j]){
                    res++;
                    mark(i,j,vis,grid);
                }
            }
        }
        return res;
    }

    public void mark(int i, int j, boolean[][] vis, char[][] grid){
        if(grid[i][j] == '0' || vis[i][j]) {
            return;
        }
        vis[i][j] = true;
        if(i<=grid.length-2) {
            mark(i+1,j,vis,grid);
        }
        if(j<=grid[i].length-2) {
            mark(i,j+1,vis,grid);
        }
        if(i>0) {
            mark(i-1,j,vis,grid);
        }
        if(j>0) {
            mark(i,j-1,vis,grid);
        }
    }
}
