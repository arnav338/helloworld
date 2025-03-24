import java.util.HashMap;
import java.util.Map;

public class Main11 {

    public static void main(String[] args) {
        String s = "00110";

        int count_of_zero = 0;
        int count_of_one = 0;

        for(int i=0; i<s.length()-1; i++){
            if(s.charAt(i)=='0'){
                count_of_zero++;
            }
        }

        count_of_one = 0;

        int number_of_flips = s.length() - count_of_zero;

        for(int i=0; i<s.length(); i++){
            if(s.charAt(i)=='0'){
                count_of_zero--;
            }else{
                number_of_flips = Math.min(number_of_flips,count_of_one+count_of_zero);
                count_of_one++;
            }
        }

        System.out.println("max flips : "+number_of_flips);

    }



//    public static void main(String[] args) {
//        int[] arr = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4};
//
//        int maxSum = findMaxSumArray(arr);
//        System.out.println("max sum is "+maxSum);
//    }

    private static int findMaxSumArray(int[] arr) {
        int result = Integer.MIN_VALUE;
        int start = 0;
        int end = 0;
        int j=0;
        int tempSum = 0;
        for(int i=0; i<arr.length; i++){
            tempSum += arr[i];
            if(result < tempSum){
                result = tempSum;
                start = j;
                end = i;
            }
            if(result<0){
                result = 0;
                j = i+1;
                start = j;
            }
        }
        int maxSum = 0;
        System.out.println("start + "+start+", end : "+end);
        for(int k=start; k<=end; k++){
            maxSum += arr[k];
        }
        return maxSum;
    }

}
