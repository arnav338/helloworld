package AnonymousClass;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        String a = "mobile";
        String b = "bomile";
//        for(char c : a.toCharArray()){
//            int d = c;
//            System.out.println("int value "+d);
//        }
        //System.out.println(isAnagram(a,b)? "is anagram" : "not an anagram");

        // ===== //
        int[] c = new int[]{16,17,4,3,5,2};
        findLeaders(c);

        Test t = new Test();
        Student s = new Student();
        s.setA("old");
        t.modify(s);
        System.out.println("res - "+s.getA());

        Set<List<Integer>> set = new HashSet<>();
        List<Integer> l1 = new ArrayList<>(Arrays.asList(1));
        List<Integer> l2 = new ArrayList<>(Arrays.asList(1));
        set.add(l1);
        set.add(l2);
        System.out.println(
                "set is - "+set
        );



    }

    public static int modify(Student student){
        student = new Student();
        student.setA("new");
        return 1;
    }

    public static class Student{
        private String a;
        public String getA() {return a;}
        public void setA(String a) {this.a = a;}
    }

    private static void findLeaders(int[] c) {
        List<Integer> leadersList = new ArrayList<>();
        int[] max = new int[c.length];
        max[max.length-1] = 0;
        for(int j= c.length-2; j>=0; j++){
            
        }
    }

    public static boolean isAnagram(String a, String b) {
        if(a.length()==0 || b.length() ==0) return false;
        int[] arr_a = new int[26];
        int[] arr_b = new int[26];
        for(char c : a.toCharArray()){
            arr_a[c-'a']++;
        }
        for(char c : b.toCharArray()){
            arr_b[c-'a']++;
        }
        for(int i=0; i<26; i++){
            if (arr_a[i] != arr_b[i]){
                return false;
            }
        }
        return true;
    }

}
