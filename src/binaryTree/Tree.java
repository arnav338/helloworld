package binaryTree;

import java.util.Optional;

public class Tree {

	public class BinaryTree {

		int data;
		BinaryTree left, right;

		public BinaryTree(int data) {
			this.data = data;
			this.left = this.right = null;
		}

		public void addLeft(BinaryTree parent, int data) {
			parent.left = new BinaryTree(data);
		}

		public void addRight(BinaryTree parent, int data) {
			parent.right = new BinaryTree(data);
		}
	}

	public static void main(String[] args) {

		BinaryTree bt = new Tree().new BinaryTree(10);
		bt.addLeft(bt, 11);
		bt.addRight(bt, 9);
		bt.left.addLeft(bt.left, 7);
		bt.right.addLeft(bt.right, 15);
		bt.right.addRight(bt.right, 8);

		int k = 12;
		addToNextAvailablePosition(bt, k);
		System.out.println();
	}

	private static void addToNextAvailablePosition(BinaryTree bt, int k) {
		Optional<BinaryTree> left = Optional.ofNullable(bt.left);
		Optional<BinaryTree> right = Optional.ofNullable(bt.right);
		if(!left.isPresent() && !right.isPresent()) {
			
		}
		if (!left.isPresent()) {
			System.out.println("Left is not empty. "+bt);
			bt.addLeft(bt, k);
			return;
			//addToNextAvailablePosition(bt.left, k);
		}
		if (!right.isPresent()) {
			System.out.println("Right is empty. "+bt);
			bt.addRight(bt, k);
			return;
			//addToNextAvailablePosition(bt.right, k);
		}
		addToNextAvailablePosition(bt.left, k);
	}

}
