package trie;

public class WordBreak {
	/*
	 * Given an input string and a dictionary of words, 
	 * find out if the input string can be segmented into a space-separated 
	 * sequence of dictionary words. 
	
	Consider the following dictionary 
	{ i, like, sam, sung, samsung, mobile, ice, cream, icecream, man, go, mango}
	
	Input:  ilike
	Output: Yes 
	The string can be segmented as "i like".
	
	Input:  ilikesamsung
	Output: Yes
	The string can be segmented as "i like samsung" 
	or "i like sam sung".
	 * */
	
	/* A utility function to check whether a word is present in dictionary or not.
	  An array of Strings is used for dictionary.  Using array of Strings for
	  dictionary is definitely not a good idea. We have used for simplicity of
	  the program*/
	static boolean dictionaryContains(String word)
	{
	    String dictionary[] = {"mobile","samsung","sam","sung","man","mango",  "icecream","and","go","i","like","ice","cream"};
	    int size = dictionary.length;
	    for (int i = 0; i < size; i++)
	        if (dictionary[i].compareTo(word) == 0)
	           return true;
	    return false;
	}
	 
	// Returns true if String can be segmented into space separated words, otherwise returns false
	static boolean wordBreak(String str)
	{
	    int size = str.length();
	    if (size == 0)   return true;
	 
	    // Create the DP table to store results of subproblems. The value wb[i]
	    // will be true if str[0..i-1] can be segmented into dictionary words, otherwise false.
	    boolean []wb = new boolean[size+1];
	    
	    for (int i=1; i<=size; i++)
	    {
	        // if wb[i] is false, then check if current prefix can make it true.
	        // Current prefix is "str.substring(0, i)"
	    	String a = str.substring(0, i);
	        if (wb[i] == false && dictionaryContains( str.substring(0, i) )) {
	        	wb[i] = true;// this is the sign of partitioning
	        	// if this step is success then only we proceed ahead
	        }
	            
	 
	        // wb[i] is true, then check for all subStrings starting from
	        // (i+1)th character and store their results.
	        if (wb[i] == true)
	        {
	            // If we reached the last prefix
	            if (i == size)
	                return true;
	 
	            for (int j = i+1; j <= size; j++)
	            {
	                // Update wb[j] if it is false and can be updated
	                // Note the parameter passed to dictionaryContains() is
	                // subString starting from index 'i' and length 'j-i'
	            	String b = str.substring(i, j) ;
	                if (wb[j] == false && dictionaryContains( str.substring(i, j) ))
	                    wb[j] = true;
	 
	                // If we reached the last character
	                if (j == size && wb[j] == true)
	                    return true;
	            }
	        }
	    }
	 
	    // Uncomment these lines to print DP table "wb[]"
	     for (int i = 1; i <= size; i++)
	        System.out.print(" " +  wb[i]); 
	 
	    // If we have tried all prefixes and none of them worked
	    return false;
	}
	 
	// Driver program to test above functions
	public static void main(String[] args)
	{
	    if(wordBreak("ilikesamsung"))
	        System.out.print("Yes\n");
	    else
	        System.out.print("No\n");
	    if(wordBreak("iiiiiiii"))
	        System.out.print("Yes\n");
	    else
	        System.out.print("No\n");
	    if(wordBreak(""))
	        System.out.print("Yes\n");
	    else
	        System.out.print("No\n");
	    if(wordBreak("ilikelikeimangoiii"))
	        System.out.print("Yes\n");
	    else
	        System.out.print("No\n");
	    if(wordBreak("samsungandmango"))
	        System.out.print("Yes\n");
	    else
	        System.out.print("No\n");
	    if(wordBreak("samsungandmangok"))
	        System.out.print("Yes\n");
	    else
	        System.out.print("No\n");
	   }
}
