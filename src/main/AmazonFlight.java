package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AmazonFlight extends Child6 {
	
	public static void main(String args[]) {
		
		List<List<Integer>> fwd = new ArrayList<>();
		fwd.add(Arrays.asList(1,3000));
		fwd.add(Arrays.asList(3,7000));
		fwd.add(Arrays.asList(2,5000));
		fwd.add(Arrays.asList(4,10000));
		
		List<List<Integer>> bkd = new ArrayList<>();
		bkd.add(Arrays.asList(1,2000));
		bkd.add(Arrays.asList(4,5000));
		bkd.add(Arrays.asList(2,3000));
		bkd.add(Arrays.asList(3,4000));
		
		int maxD = 10000;
		List<List<Integer>> output = findOptimalPair(maxD,fwd,bkd);
		System.out.println("+++++"+output);
	}
	
	class Route implements Comparator<Route> {
		int id;
		int dist;
		public int getDist() {
			return dist;
		}
		public int getId() {
			return id;
		}
		public Route() {}
		public Route(int id, int dist) {
			super();
			this.id = id;
			this.dist = dist;
		}
		@Override
		public String toString() {
			return id + "," + dist;
		}
		@Override
		public int compare(Route o1, Route o2) {
			return o1.getDist()>o2.dist ? 1 : 
					o1.getDist()==o2.getDist() ? 0 : -1;
		}
		
	}
	
	public static List<Route> convertToRouteList(List<List<Integer>> list, List<Integer> b){
		List<Route> routeList = new ArrayList<AmazonFlight.Route>();
		for(List<Integer> l:list) {
			routeList.add(new AmazonFlight().new Route(l.get(0), l.get(1)));
			b.add(l.get(1));
		}
		return routeList;
	}

	public static List<List<Integer>> findOptimalPair(int maxD, List<List<Integer>> fwd, List<List<Integer>> bkd) {
		List<List<Integer>> output = new ArrayList<List<Integer>>();
		List<Integer> f = new ArrayList<Integer>();
		List<Integer> b = new ArrayList<Integer>();
		List<Route> fwdrouteList = convertToRouteList(fwd,f);
		List<Route> bkdrouteList = convertToRouteList(bkd,b);
		
		Collections.sort(fwdrouteList, new AmazonFlight().new Route());
		Collections.sort(bkdrouteList, new AmazonFlight().new Route());
		Collections.sort(f);
		Collections.sort(b);
		System.out.println("-->"+fwdrouteList);
		System.out.println("-->"+bkdrouteList);
		
		int max=Integer.MAX_VALUE;
		int fwdCounter = 0;
		int bkdCounter = b.size()-1;
		
		while(true) {
			int distance = (maxD-( f.get(fwdCounter) + b.get(bkdCounter) ));
			if(distance<=max) {
				if(distance<max) {
					output.clear();
				}
				max = distance;
				output.add(Arrays.asList(fwdCounter+1,bkdCounter+1));
			}
			if((fwdCounter+1)>f.size()-1 || (bkdCounter-1)<0 ) {
				break;
			}
			if( (f.get(fwdCounter+1)) > b.get(bkdCounter-1) ) {
				if( (f.get(fwdCounter+1) + b.get(bkdCounter)) > maxD ) {
					bkdCounter--;
				}
				else {
					fwdCounter++;
				}
			}
			else {
				bkdCounter--;
			}
		}

		return output;		
	}
	
}
