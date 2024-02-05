package arrays;

public class RabinKarp {
	
	// d is the number of characters in the input alphabet
	// 256 doesnt only represent the 26 english alphabets but other alphabets and characters too
	// like %, /, ^, etc.. ( whole of the ascii table )
    public final static int d = 256;
	
    /* pat -> pattern
    txt -> text
    q -> A prime number
     */
    
    static void search(String pat, String txt, int q)
    {
        int M = pat.length();
        int N = txt.length();
        int i, j;
        int p = 0; // hash value for pattern
        int t = 0; // hash value for txt
        int h = 1;
        // The value of h would be "pow(d, M-1)%q"
        for (i = 0; i < M-1; i++)
            h = (h*d)%q;
        // Calculate the hash value of pattern and first window of text
        for (i = 0; i < M; i++)
        {
            p = (d*p + pat.charAt(i))%q;
            t = (d*t + txt.charAt(i))%q;
        }
        // Slide the pattern over text one by one
        for (i = 0; i <= N - M; i++)
        {
            // Check the hash values of current window of text
            // and pattern. If the hash values match then only
            // check for characters on by one
            if ( p == t )
            {
                /* Check for characters one by one */
                for (j = 0; j < M; j++)
                {
                    if (txt.charAt(i+j) != pat.charAt(j))
                        break;
                }
     
                // if p == t and pat[0...M-1] = txt[i, i+1, ...i+M-1]
                if (j == M)
                    System.out.println("Pattern found at index " + i);
            }
            // Calculate hash value for next window of text: Remove leading digit, add trailing digit
            if ( i < N-M )
            {
                t = (d*(t - txt.charAt(i)*h) + txt.charAt(i+M))%q;
                // We might get negative value of t, converting it to positive
                if (t < 0)
                t = (t + q);
            }
        }
    }
    
	public static void main(String[] args) {
		/*
		 * Rabin karp is an algorithm for pattern searching
		 * Conventionally if we have to find a string m in parent string n
		 * we will create a sliding window of size m and slide through the window
		 * checking each window of size m against the string that we need to find
		 * For each prospective match we iterate through whole of the window 
		 * and see if it exactly matches the string
		 * 
		 * But in rabin karp , in each window, before iterating for all possible windows
		 * we have a preliminary check
		 * we create a hash of the string that we have to find and we create a hash of the
		 * current window using the same hash function, if the hash value of both are same, 
		 * only then we iterate over whole string otherwise not
		 * Input:  
		 * 	txt[] =  "AABAACAADAABAABA"
           	pat[] =  "AABA"
			Output: 
			Pattern found at index 0
        	Pattern found at index 9
        	Pattern found at index 12
        	
        	The average and best-case running time of the Rabin-Karp algorithm is O(n+m), 
        	but its worst-case time is O(nm). Worst case of Rabin-Karp algorithm occurs 
        	when all characters of pattern and text are same as the hash values of all 
        	the substrings of txt[] match with hash value of pat[]. 
        	For example pat[] = “AAA” and txt[] = “AAAAAAA”.
		 * */
		String txt = "GEEKS FOR GEEKS";
        String pat = "GEEK";
           
          // A prime number
        int q = 101;
       
          // Function Call
        search(pat, txt, q);
	}
}
