package stack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaintCoordinated {
	/*
	 * Given a horizontal line, you have to paint coordinates and tell the amount of paint required at every step.
		Ex - [(4, 10), (7, 13), (20, 30)]
		ans - [6, 3, 10]
		
		4					10
				7					13
				
					4					10	
			2									12
	 * */
	public static void main(String[] args) {
		List<ArrayList<Integer>> input = new ArrayList<>();
		input.add((ArrayList<Integer>) Arrays.asList(4,10));
		input.add((ArrayList<Integer>) Arrays.asList(7,13));
		input.add((ArrayList<Integer>) Arrays.asList(20,30));
		System.out.println("-> "+getPaintAmount(input));
	}

	private static int getPaintAmount(List<ArrayList<Integer>> input) {
		int paint=0;
		int leftPainted = Integer.MAX_VALUE;
		int rightPainted = Integer.MIN_VALUE;
		for(ArrayList<Integer> step : input) {
			if(step.get(0)>rightPainted) { // no intersection
				paint += step.get(1)-step.get(0);
				leftPainted = Math.min(leftPainted, step.get(0));
				rightPainted = Math.max(rightPainted, step.get(1));
			}
			else if(!(step.get(0)>=leftPainted && step.get(1)<=rightPainted)) { // partial intersection
				paint += step.get(0)>=leftPainted ? 0 : leftPainted-step.get(0);
				paint += step.get(1)>=rightPainted ? step.get(1)-rightPainted : 0;
				leftPainted = Math.min(leftPainted, step.get(0));
				rightPainted = Math.max(rightPainted, step.get(1));
			}
		}
		return paint;
	}
}
