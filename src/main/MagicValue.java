package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MagicValue {

	public static void main(String[] args) {
		int[] a = new int[] { 0, 4, 0, 0, 1, 3, 4, 1, 0, 2 };
		// int[] a = new int[] {0,2,3,4,5};
		System.out.println("Magic value : " + magicValue(a));

	}

	private static int magicValue(int[] a) {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < a.length; i++) {
			list.add(a[i]);
		}

		System.out.println("--" + list.stream().reduce((d, b) -> d < b ? d : b).get());

		List<Integer> temp = new ArrayList<>(list);
		Collections.sort(temp);

		int sumOfBadNumbers = 0;
		for (int b = 0; b < list.size(); b++) {
			if (list.get(b) - temp.get(b) != 0) {
				sumOfBadNumbers += list.get(b);
			}
		}
		return list.stream().mapToInt(i -> i).sum() - 2 * sumOfBadNumbers;
	}

}
