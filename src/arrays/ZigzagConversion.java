package arrays;

import java.util.ArrayList;
import java.util.List;

public class ZigzagConversion {
    /*
    The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this: (you may want to display this pattern in a fixed font for better legibility)

P   A   H   N
A P L S I I G
Y   I   R
And then read line by line: "PAHNAPLSIIGYIR"

Write the code that will take a string and make this conversion given a number of rows:

string convert(string s, int numRows);


Example 1:

Input: s = "PAYPALISHIRING", numRows = 3
Output: "PAHNAPLSIIGYIR"
Example 2:

Input: s = "PAYPALISHIRING", numRows = 4
Output: "PINALSIGYAHRPI"
Explanation:
P     I    N
A   L S  I G
Y A   H R
P     I
Example 3:

Input: s = "A", numRows = 1
Output: "A"
    * */

    public static void main(String[] args) {
        String convert = convert("PAYPALISHIRING", 3);
        System.out.println("converted - "+convert);
        String convert_ = convert_("PAYPALISHIRING", 3);
        System.out.println("converted - "+convert_);
        System.out.println("correct ? "+convert.equals(convert_));
    }

    public static String convert_(String s, int numRows) {
        String res = "";
        if(numRows == 1) return s;
        List<StringBuilder> l =new ArrayList<>();
        for(int p = 0; p<numRows; p++){
            l.add(new StringBuilder());
        }
        int counter = 0;
        int r=0;
        boolean diag = false;
        while(counter < s.length()){
            l.get(r).append(s.charAt(counter));
            counter++;
            if(r == numRows-1){
                diag = true;
            }
            if(r == 0){
                diag = false;
            }
            if(diag){
                r--;
            }else{
                r++;
            }
        }
        for(int q = 0; q<l.size(); q++){
            res += l.get(q);
        }
        return res;
    }



    /*
    brute force - convert the string into the desired 2-d array and then read
    * */
    public static String convert(String s, int numRows) {
        String res = "";
        if(numRows == 1) return s;
        int columns = ((s.length() / (numRows+numRows-2))+1)*(numRows-1);
        //System.out.println("columns - "+columns);
        char[][] arr = new char[numRows][columns];
        for(int p = 0; p<numRows; p++){
            for(int q=0; q < columns; q++){
                arr[p][q] = '0';
            }
        }
        int counter = 0;
        int r=0;
        int c=0;
        boolean diag = false;
        while(counter < s.length()){
            arr[r][c] = s.charAt(counter);
            counter++;
            if(r == numRows-1){
                diag = true;
            }
            if(r == 0){
                diag = false;
            }
            if(diag){
                r--;
                c++;
            }else{
                r++;
            }
        }
        for(int p = 0; p<numRows; p++){
            for(int q=0; q < columns; q++){
                //System.out.print(arr[p][q]);
                if(arr[p][q] != '0'){
                    res += arr[p][q];
                }
            }
            //System.out.println("");
        }
        return res;
    }
}
