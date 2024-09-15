/**
 * This class implements a tokenizer for Scheme
 * @author Arash Tashakori - B00872075 - ArTash@dal.ca
 */
package ca.dal.csci3137;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Tokenizer {
    /**
     * This method tokenizes a given String and returns a List of tokens. The tokens are separated by whitespaces,
     * parenthesis and single quote symbol. Parenthesis and single quote symbol are considered their own tokens.
     * @param source The given String that is supposed to be tokenized
     * @return A list of tokens
     */
    public static List<String> tokenize(String source) {

        List <String> tokens = new ArrayList<>();
        String delimiter = " \t\n\r\f()'"; //a.k.a. "Whitespaces"
        StringTokenizer tokenizer = new StringTokenizer(source, delimiter, true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();

            // Ignore whitespace tokens
            if (token.trim().isEmpty()) {
                continue;
            }

            // take (, ) and ' as their own token
            tokens.add(token);
        }

        return tokens;
    }
}
