package arrays;

public class KMP_pattern {

	public static void main(String[] args) {
		/*
		 * The KMP matching algorithm uses degenerating property (pattern having same
		 * sub-patterns appearing more than once in the pattern) of the pattern and
		 * improves the worst case complexity to O(n). The basic idea behind KMP’s
		 * algorithm is: whenever we detect a mismatch (after some matches), we already
		 * know some of the characters in the text of the next window. We take advantage
		 * of this information to avoid matching the characters that we know will anyway
		 * match
		 * 
		 * KMP algorithm preprocesses pat[] and constructs an auxiliary lps[] of size m
		 * (same as size of pattern) which is used to skip characters while matching.
		 * name lps indicates longest proper prefix which is also suffix.. A proper
		 * prefix is prefix with whole string not allowed. For example, prefixes of
		 * “ABC” are “”, “A”, “AB” and “ABC”. Proper prefixes are “”, “A” and “AB”.
		 * Suffixes of the string are “”, “C”, “BC” and “ABC”. We search for lps in
		 * sub-patterns. More clearly we focus on sub-strings of patterns that are
		 * either prefix and suffix.
		 * 
		 * How to use lps[] to decide next positions (or to know a number of characters
		 * to be skipped)?
		 * 
		 * We start comparison of pat[j] with j = 0 with characters of current window of
		 * text. We keep matching characters txt[i] and pat[j] and keep incrementing i
		 * and j while pat[j] and txt[i] keep matching. When we see a mismatch We know
		 * that characters pat[0..j-1] match with txt[i-j…i-1] (Note that j starts with
		 * 0 and increment it only when there is a match). We also know (from above
		 * definition) that lps[j-1] is count of characters of pat[0…j-1] that are both
		 * proper prefix and suffix. From above two points, we can conclude that we do
		 * not need to match these lps[j-1] characters with txt[i-j…i-1] because we know
		 * that these characters will anyway match. Let us consider above example to
		 * understand this.
		 * 
		 */
		String txt = "ABABDABACDABABCABAB";
        String pat = "ABABCABAB";
		//String txt = "ABABABCBCABAB";
		//String pat = "ABCBC";
		KMPSearch(pat, txt);
	}
	static void KMPSearch(String pat, String txt)
    {
        int M = pat.length();
        int N = txt.length();
  
        // create lps[] that will hold the longest prefix suffix values for pattern
        int lps[] = new int[M];
  
        // Preprocess the pattern (calculate lps[] array)
        computeLPSArray(pat, M, lps);
        
        int i = 0; // index for txt[]
        int j = 0; // index for pat[]
        while (i < N) {
            if (pat.charAt(j) == txt.charAt(i)) {
                j++;
                i++;
            }
            if (j == M) {
                System.out.println("Found pattern " + "at index " + (i - j));
                j = lps[j - 1];
            }
            // mismatch after j matches
            else if (i < N && pat.charAt(j) != txt.charAt(i)) {
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                if (j != 0)
                    j = lps[j - 1];
                else
                    i = i + 1;
            }
        }
    }
  
    static void computeLPSArray(String pat, int M, int lps[])
    {
        // length of the previous longest prefix suffix
        int len = 0; // this counter denotes the character to be matched with
        int i = 1;
        lps[0] = 0; // lps[0] is always 0
        // the loop calculates lps[i] for i = 1 to M-1
        while (i < M) {
        	System.out.println("char : "+pat.charAt(i)+" || "+len);
            if (pat.charAt(i) == pat.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            }
            else // (pat[i] != pat[len])
            {
                // This is tricky. Consider the example.
                // AAACAAAA and i = 7. The idea is similar
                // to search step.
                if (len != 0) {
                    len = lps[len - 1]; // in case of mismatch, we fallback to last matching value
                    
                    // Also, note that we do not increment i here
                }
                else // if (len == 0)
                {
                    lps[i] = len;
                    i++;
                }
            }
        }
    }
}
