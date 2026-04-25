package arrays;

import java.util.ArrayList;
import java.util.List;

public class NQueens {

    public static void main(String[] args) {
        solveNQueens(4);
    }


    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        //List<String> init = new ArrayList<>();
        char[][] init = new char[n][n];
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                init[i][j] = '.';
            }
        }
        //System.out.println(init);
        solve(n,init,res);
        return res;
    }

    public static void solve(int n, char[][] init, List<List<String>> res){
        int j = 0;
        for(int k = 0; k<n; k++){
            placeQueen(k,j,n,init,res);
        }
        System.out.println("");
    }

    public static boolean placeQueen(int row,int column, int n, char[][] init, List<List<String>> res){
        if(column == n){
            // copy answer to res
            List<String> l = new ArrayList<>();
            for(int i=0; i<init.length; i++){
                String s = "";
                for(int j=0; j<init[0].length; j++){
                    s += init[i][j];
                }
                l.add(s);
            }
            res.add(l);
            return false;
        }
        if(isFeasible(row,column,n,init)){
            init[row][column] = 'Q';
            if(!placeQueen(0,column+1,n,init,res)){
                init[row][column] = '.';
                return false;
            }
        }else{
            if(row == n-1){
                return false;
            }
            else{
                return placeQueen(row+1,column,n,init,res);
            }
        }
        return true;
    }

    /** tells if it is safe to place a queen at (x,y) position */
    public static boolean isFeasible(int row, int column, int n, char[][] init){
        for(int i=column; i>=0; i--){
            // checks row occupancy
            if(init[row][i] == 'Q') return false;
        }
        int r = row;
        int c = column;
        while(r>=0 && c>=0){
            // checks upward left occupancy
            if(init[r][c] == 'Q') return false;
            r--;
            c--;
        }
        r = row;
        c = column;
        while(r<n && c>=0){
            // checks downward left occupancy
            if(init[r][c] == 'Q') return false;
            r++;
            c--;
        }
        return true;
    }
}
