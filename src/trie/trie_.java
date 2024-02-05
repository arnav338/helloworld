package trie;

public class trie_ {
	
	static Node head;
	static final int alphabet = 26;
	
	static class Node{
		int endOfWord;
		Node[] array;
		public Node() {
			this.endOfWord = -1;
			this.array = new Node[alphabet];
		}
	}
	public static void main(String[] args) {
		String[] val = {"cat"};
		head = new Node();
		loadValues(val);
		System.out.println("===========");
		String[] target = {"cat","bat"};
		search(target);
	}
	private static void search(String[] target) {
		for(String word : target) {
			boolean res = searchValue(head,word,0);
			System.out.println(word+" "+(res ? "exists" : "doesnt exist"));
		}
	}
	private static boolean searchValue(Node head, String word,int i) {
		if(i == word.length()) {
			if(head.endOfWord != -1) {
				return true;
			}
			return false;
		}
		int j = word.charAt(i) - 'a';
		if(head.array[j] == null) {
			return false;
		}
		i++;
		return searchValue(head.array[j], word, i);
	}
	private static void loadValues(String[] val) {
		for(String word : val) {
			addValue(word);
		}
	}
	public static void addValue(String word) {
		if(word.length() > 0) {
			add(head,word,0);
		}
	}
	public static void add(Node head, String word, int i) {
		if(i == word.length()) {
			head.endOfWord = head.endOfWord == -1 ? 1 : head.endOfWord++;
			return;
		}
		int j = word.charAt(i) - 'a';
		head.array[j] = new Node();
		i++;
		add(head.array[j], word, i);
	}
}
