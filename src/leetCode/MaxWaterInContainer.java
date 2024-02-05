package leetCode;

public class MaxWaterInContainer {
	/*
	 * You are given an integer array height of length n. There are n vertical lines drawn such that the two endpoints of the ith line are (i, 0) and (i, height[i]).

		Find two lines that together with the x-axis form a container, such that the container contains the most water.
		
		Return the maximum amount of water a container can store.
		
		Notice that you may not slant the container.
		
		Input: height = [1,8,6,2,5,4,8,3,7]
		Output: 49
		Explanation: The above vertical lines are represented by array [1,8,6,2,5,4,8,3,7]. In this case, the max area of water (blue section) the container can contain is 49.
	 * */
	public static void main(String[] args) {
		System.out.println("max water : "+maxArea(new int[] {1,8,6,2,5,4,8,3,7}));
	}
	
	public static int maxArea(int[] height) {
        int max=Integer.MIN_VALUE;
        int i=0;
        int j=height.length-1;
        
        while(i<j){
            int min=Math.min(height[i],height[j]); //we will find the min of both height because that is the water level
            max=Math.max(max,min*(j-i)); //finding the amount of water 
            if(height[i]<height[j]) i++; //if ith is smaller then increase i, to explore the possibility that a bigger height line may exist on the right(as that is the main aim of the problem)
            else j--; // if jth is smalller then decrease j, to explore the possibility that a bigger height line may exist on the left(as that is the main aim of the problem)
        }
        return max;   
    }
}
