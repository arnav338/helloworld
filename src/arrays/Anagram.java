package arrays;

import java.util.HashMap;

public class Anagram {
	public static void main(String[] args) {
		String a = "dnsjbvhsvdshbvbshdbvsd";
		String b = "vjbdshvsdkbvhsdkvcdsv";
		int res = remAnagrams(a, b);
		System.out.println("-->"+res);
	}
	public static int remAnagrams(String s,String s1)
    {
        int res = 0;
            if(s.length() == s1.length()){
                return -1;
            }
            String big = s1.length() > s.length() ? s1 : s;
            String small = s1.length() > s.length() ? s : s1;
            HashMap<String,Integer> map = new HashMap<>();
            HashMap<String,Integer> bigmap = new HashMap<>();
            for(int i=0; i<small.length(); i++){
                map.put(small.substring(i,i+1),map.getOrDefault(small.substring(i,i+1),0)+1);
            }
            for(int i=0; i<big.length(); i++){
                if(!map.containsKey(big.substring(i,i+1))){
                    System.out.println("-->"+big.substring(i,i+1));
                    res++; // needs to be removed
                }
                else{
                    bigmap.put(big.substring(i,i+1),bigmap.getOrDefault(big.substring(i,i+1),0)+1);
                    if(map.getOrDefault(big.substring(i,i+1),-1) != bigmap.get(big.substring(i,i+1))){
                        System.out.println("mismatch -> "+big.substring(i,i+1));
                        System.out.println(map.get(big.substring(i,i+1))+" || "+bigmap.get(big.substring(i,i+1)));
                        res++;
                    }
                }
            }
        return res;    
    }
}
