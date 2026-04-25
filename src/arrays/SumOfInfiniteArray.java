package arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SumOfInfiniteArray {
    /*
    Given an array “A” of N integers and you have also defined the new array “B” as a concatenation of array “A” for an infinite number of times.

For example, if the given array “A” is [1,2,3] then, infinite array “B” is [1,2,3,1,2,3,1,2,3,.......].

Now you are given Q queries, each query consists of two integers “L“ and “R”(1-based indexing). Your task is to find the sum of the subarray from index “L” to “R” (both inclusive) in the infinite array “B” for each query.

Note :

The value of the sum can be very large, return the answer as modulus 10^9+7.
Detailed explanation ( Input/output format, Notes, Images )
Constraints :
1 <= T <= 100
1 <= N <= 10^4
1 <= A[i] <= 10^9
1 <= Q <= 10^4
1 <= L <= R <= 10^18

Time Limit: 1sec
Sample Input 1 :
1
3
1 2 3
2
1 3
1 5
Sample Output 1 :
6 9
Explanation to Sample Input 1 :
For the first test case, the given array A is [1,2,3] therefore the infinite array “B” will be [1,2,3,1,2,3,1,2,3,.......]. So the answer for the given first query is 6 because the sum of the subarray from index 1 to 3 of infinite array “B” i.e. (B[1]+B[2]+B[3]) is 6.

For the given second query is 9 because the sum of the subarray from index 1 to 5 of array “B” .ie (B[1]+B[2]+B[3]+B[4]+B[5]) is 9.
Sample Input 2 :
1
4
5 2 6 9
3
1 5
10 13
7 11
Sample Output 2 :
27 22 28
    * */

    public static void main(String[] args) {
//        int[] arr = new int[]{1,2,3};
//        List<List<Long>> query = new ArrayList<>();
//        query.add(new ArrayList<>(Arrays.asList(1L,3L)));
//        query.add(new ArrayList<>(Arrays.asList(1L,5L)));

        int[] arr = new int[]{5,2,6,9};
        List<List<Long>> query = new ArrayList<>();
        query.add(new ArrayList<>(Arrays.asList(1L,5L)));
        query.add(new ArrayList<>(Arrays.asList(10L,13L)));
        query.add(new ArrayList<>(Arrays.asList(7L,11L)));


        //List<Integer> integers = sumInRanges(arr, arr.length, query, query.size());
        List<Integer> integers_ = sumInRanges_(arr, arr.length, query, query.size());

        System.out.println(integers_);
    }

    private static int mod = (int) 1e9 + 7;

    public static List<Integer> sumInRanges_(int[] arr, int n, List<List<Long>> queries, int q) {

        List<Integer> ans = new ArrayList<Integer>();

        long[] sumArray = new long[n + 1];

        for (int i = 1; i <= n; i++) {

            sumArray[i] = (sumArray[i - 1] + arr[i - 1]) % mod;

        }



        for (List<Long> range : queries) {

            long l = range.get(0);

            long r = range.get(1);



            long rsum = func(sumArray, r, n);

            long lsum = func(sumArray, l - 1, n);

            ans.add((int) ((rsum - lsum + mod) % mod));

        }

        return ans;

    }

    private static long func(long[] sumArray, long x, long n) {

        long count = (x / n) % mod;

        long res = (count * sumArray[(int) n] % mod);

        res = (res + sumArray[(int) (x % n)]) % mod;

        return res;

    }

    public static List<Integer> sumInRanges(int[] arr, int n, List<List<Long>> queries, int q) {
        List<Integer> res = new ArrayList<>();
        long totalSum = 0;
        for(int i=0; i<arr.length; i++){
            totalSum += arr[i];
        }
        for(List<Long> query : queries){
            Integer sum = 0;
            Long start = query.get(0);
            Long end = query.get(1);
            Long range = end - start + 1;
            if((end-start+1)>arr.length){
                long j = start;
                for(;;j++){
                    sum += arr[((int)j)-1];
                    range--;
                    if(j%arr.length==0){
                        break;
                    }
                }
                long k = end;
                System.out.println("k - "+k);
                for(;;k--){
                    if(k%arr.length==0){
                        break;
                    }
                    sum += arr[(((int)k)%arr.length)-1];
                    range--;
                }
                long l = range/arr.length;
                sum = (int)(sum + ((int)l)*(totalSum));
            }else{
                long i = start - 1;
                while(range > 0){
                    if(i>=arr.length){
                        //System.out.println("i is "+i);
                        i = i%arr.length;
                    }
                    //System.out.println("adding = "+arr[(int)i]);
                    sum = sum + arr[(int)i];
                    range--;
                    i++;
                }
            }
            res.add(sum);
            //System.out.println("----------");
        }
        return res;
    }
}

