package arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class OverlappingIntervals {
	/*
	 * Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input.

 

Example 1:

Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
Output: [[1,6],[8,10],[15,18]]
Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
Example 2:

Input: intervals = [[1,4],[4,5]]
Output: [[1,5]]
Explanation: Intervals [1,4] and [4,5] are considered overlapping.

An efficient approach is to first sort the intervals according to the starting time. 
Once we have the sorted intervals, we can combine all intervals in a linear traversal. 
The idea is, in sorted array of intervals, if interval[i] doesn’t overlap with interval[i-1], 
then interval[i+1] cannot overlap with interval[i-1] because 
starting time of interval[i+1] must be greater than or equal to interval[i].
 

1. Sort the intervals based on increasing order of 
    starting time.
2. Push the first interval on to a stack.
3. For each interval do the following
   a. If the current interval does not overlap with the stack 
       top, push it. (starting time of current interval > ending time of interval on top of stack)
   b. If the current interval overlaps with stack top and ending
       time of current interval is more than that of stack top, 
       update stack top with the ending  time of current interval.
4. At the end stack contains the merged intervals. 
	 * */
	public static void main(String[] args) {
		/*
		int[][] a = {
				{1,3},
				{2,6},
				{8,10},
				{15,18}
		};
		*/
		int[][] a = {
				{1,4},
				{4,5}
		};
		int[][] res = merge(a);
		for(int j=0; j<res.length; j++) {
			System.out.println("-> "+res[j][0]+" || "+res[j][1]);
		}
	}
	
	public static int[][] merge(int[][] intervals) {
        int left = Integer.MAX_VALUE;
        int right = Integer.MIN_VALUE;
        List<List<Integer>> temp = new ArrayList<>();
        int r = 0;
        for(int i=0; i < intervals.length; i++){
            if(i!=0){
            	if(intervals[i][0] == intervals[i-1][1]){
                    right = intervals[i][1];
                    System.out.println("setting "+left+" "+right);
                    if(i == intervals.length-1) {
                    	temp.add(Arrays.asList(left,right));
                    }
                    continue;
                }
                if(intervals[i][0] > intervals[i-1][1]){
                	System.out.println("adding "+left+" "+right);
                	temp.add(Arrays.asList(left,right));
                    left = intervals[i][0];
                    right = intervals[i][1];
                    System.out.println("setting "+left+" "+right);
                    if(i == intervals.length-1) {
                    	temp.add(Arrays.asList(left,right));
                    }
                    continue;
                }
            }
            if(intervals[i][0] < left){
                left = intervals[i][0];
            }
            if(intervals[i][1] > right){
                right = intervals[i][1];
            }
        }
        int[][] result = new int[temp.size()][2];
        for(int j = 0; j < temp.size(); j++) {
        	List<Integer> l = temp.get(j);
        	result[r][0] = l.get(0);
        	result[r][1] = l.get(1);
        	r++;
        }
        return result;
    }
	static class Interval
	{
	    int start,end;
	    Interval(int start, int end)
	    {
	        this.start=start;
	        this.end=end;
	    }
	}
	
	 public static void mergeIntervals(Interval arr[])
	    {
	        // Test if the given set has at least one interval
	        if (arr.length <= 0)
	            return;
	        // Create an empty stack of intervals
	        Stack<Interval> stack=new Stack<>();
	        // sort the intervals in increasing order of start time
	        Arrays.sort(arr,new Comparator<Interval>(){
	            public int compare(Interval i1,Interval i2)
	            {
	                return i1.start-i2.start;
	            }
	        });
	        // push the first interval to stack
	        stack.push(arr[0]);
	   
	        // Start from the next interval and merge if necessary
	        for (int i = 1 ; i < arr.length; i++)
	        {
	            // get interval from stack top
	            Interval top = stack.peek();
	   
	            // if current interval is not overlapping with stack top,push it to the stack
	            if (top.end < arr[i].start)
	                stack.push(arr[i]);
	   
	            // Otherwise update the ending time of top if ending of current interval is more
	            else if (top.end < arr[i].end)
	            {
	                top.end = arr[i].end;
	                stack.pop();
	                stack.push(top);
	            }
	        }
	        // Print contents of stack
	        System.out.print("The Merged Intervals are: ");
	        while (!stack.isEmpty())
	        {
	            Interval t = stack.pop();
	            System.out.print("["+t.start+","+t.end+"] ");
	        } 
	    }
}
