package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AmazonFoodItems extends Child6 {
	
	public static void main(String args[]) {
		
		List<List<Integer>> allLocations = new ArrayList<>();
		allLocations.add(Arrays.asList(1,2));
		allLocations.add(Arrays.asList(5,1));
		allLocations.add(Arrays.asList(2,6));
		allLocations.add(Arrays.asList(1,2));
		allLocations.add(Arrays.asList(3,1));
		allLocations.add(Arrays.asList(4,6));
		allLocations.add(Arrays.asList(5,1));
		allLocations.add(Arrays.asList(3,2));
		
		int numLocations = 5;
		List<List<Integer>> output = findNearbyRestraunts(allLocations,numLocations);
		System.out.println("+++++"+output);
	}
	
	public class Location implements Comparable<Location>{
		int x;
		int y;
		double distance;
		public Location(int x, int y) {
			this.x = x;
			this.y = y;
			this.distance = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public double getDistance() {
			return distance;
		}
		@Override
		public int compareTo(Location o) {
			return Double.compare(getDistance(), o.getDistance());
		}
	}
	
	public static List<Location> convertToLocationList(List<List<Integer>> allLocations){
		List<Location> output = new ArrayList<AmazonFoodItems.Location>();
		for(List<Integer> l : allLocations) {
			output.add(new AmazonFoodItems().new Location(l.get(0), l.get(1)));
		}
		Collections.sort(output);
		return output;
	}

	private static List<List<Integer>> findNearbyRestraunts(List<List<Integer>> allLocations, int numLocations) {
		List<List<Integer>> output = new ArrayList<>();
		
		if(allLocations.size()>0) {
			List<Location> outputList = convertToLocationList(allLocations);
			if(outputList.size()>=numLocations) {
				for(int i=0;i<numLocations;i++) {
					output.add(Arrays.asList(outputList.get(i).getX(),outputList.get(i).getY()));
				}
			}
		}
		return output;
	}
	
}
