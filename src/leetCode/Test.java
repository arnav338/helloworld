package leetCode;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Test {
	static HashMap<Integer, String> map;
	static HashMap<Integer, Integer> temp;
	
	public static void main(String[] args) {
		map = new HashMap<Integer, String>();
		map.put(1, "One");
		map.put(2, "Two");
	 	map.put(3, "Three");
		map.put(4, "Four");
		map.put(5, "Five");
		int a = 1122;
		//translate(a);
		System.out.println("=============");
		HashSet<Integer> b = new HashSet<Integer>();
		b.add(1);
		b.add(17);
		b.add(2);
		b.add(23);
		b.add(50);
		b.add(65);
		b.add(11);
		Integer[] d = b.toArray(new Integer[0]);
		for(int c : d) {
			System.out.println(c);
		}
		System.out.println("=============");
		
		/*
		Frame f = new Frame();
		TextField tf = new TextField();
		f.add(tf);
		f.setSize(200, 100);
		f.setVisible(true);*/
		System.out.println("=============");
		int ar[] = {1, 2, 3, 6, 5, 4};
		nextPermutation(6, ar);
		System.out.println("=============");
		System.out.println(checkPalindrome(234)); 
		HashMap<Integer, Integer> m = new HashMap<Integer, Integer>();
		
		
	}
	
	public static void test() {
		
	}
	
	private static void translate(int a) {
		temp = new HashMap<Integer, Integer>();
		while(a!=0) {
			int t = a%10;
			if(temp.containsKey(t)) {
				temp.put(t, temp.get(t)+1);
			}
			else {
				temp.put(t, 1);
			}
			a = a/10;
		}
		for(int b : temp.keySet()) {
			System.out.println(map.get(temp.get(b))+" "+b+"'s");
		}
		
	}
	static List<Integer> nextPermutation(int N, int arr[]){
        ArrayList<Integer> list = new ArrayList<>();
        int j = 0;
        Integer l = 0;
        list.add(arr[N-1]);
        for(int i = N-2; i>=0; i--){
            list.add(arr[i]);
            if(arr[i] < arr[i+1]){
                j=i;
                break;
            }
        }
        Collections.sort(list);
        for(int k : list){
            if(k > arr[j]){
                arr[j] = k; // replacing the found digit with the next possible bigger digit
                l = k; // storing the elemnt that has been replaced with for removal
                break;
            }
        }
        list.remove(l);
        int t = j+1;
        for(int k : list) {
        	arr[t] = k; // reset rest of the list with sorted numbers
        	t++;
        }
        list.clear();
        for(int k = 0; k < N; k++){
            list.add(arr[k]);
        }
        return list;
    }
	public static boolean checkPalindrome(int a){
		int temp = 0;
		int b = a;
		ArrayList<Integer> list = new ArrayList<>();
		while(a > 0){
			temp = a%10;
			System.out.println("temp : "+temp);
			list.add(temp);
			a = a/10;
		}
		int right = list.size()-1;
		int copy = 0,j=0;
		for(int i=right; i>=0; i--){
		    copy += list.get(j) * Math.pow(10,i);
		    j++;
		}
		System.out.println("copy : "+copy);
		return b==copy ? true : false;
	    }
}
