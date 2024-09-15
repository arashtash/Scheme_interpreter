/**
 * This class implements token parsing for Scheme.
 * @author Arash Tashakori - B00872075 - ArTash@dal.ca
 */
package ca.dal.csci3137;

import java.math.BigInteger;
import java.util.List;

public class Parser {
    /**
     * This method implements the parsing of a given Scheme tokens list and turns it into and returns an abstract
     * syntax tree
     * @param tokens the tokenized List of Strings
     * @return  the abstract syntax tree represented as a nested object of Pairs, Symbols, and other sorts of objects.
     */
    public static Object parse(List<String> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            throw new IllegalArgumentException("Token list is null or empty");
        }

        String token = tokens.remove(0);

        if (token.equals("'")) {    //return a list with ' and the literal after it
            Object afterSingleQuote = parse(tokens);
            return SchemeCore.list(SchemeCore.symbol("quote"), afterSingleQuote);
        } else if (token.equals("(")) {
            return parseList(tokens);
        } else if (token.equals(")")) {
            throw new IllegalArgumentException("there is a ) with no corresponding (!");
        } else {
            return parseLiteralToken(token);
        }
    }

    //This helper method recursively handles the parsing of parenthesis, whether it's nested or not
    private static Object parseList(List<String> tokens) {
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("there is a ( with no closure!");
        }

        //Handling parenthesis closure
        if (tokens.get(0).equals(")")) {
            tokens.remove(0);
            return SchemeCore.nil;
        }

        //Recursively deal with inside each (potentially nested) parenthesis
        Object ar = parse(tokens);
        Object dr = parseList(tokens);
        return new Pair(ar, dr);
    }

    //This helper method converts a literal token into its appropriate corresponding Object value
    private static Object parseLiteralToken(String token) {
        if (token.equals("#t")) {
            return Boolean.TRUE;
        } else if (token.equals("#f")) {
            return Boolean.FALSE;
        } else if (isInteger(token)) {
            return new BigInteger(token);
        } else {
            return SchemeCore.symbol(token);
        }
    }

    //This method checks if the given token is an integer or not
    private static boolean isInteger(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        int startIdx = 0;
        //Only allowing the first digit to be possibly - or +
        if (token.charAt(0) == '+' || token.charAt(0) == '-') {
            startIdx = 1;
        }

        //Check each char position to see if it's a digit or not
        for (int i = startIdx; i < token.length(); i++) {
            if (!Character.isDigit(token.charAt(i))) {
                return false;
            }
        }

        // check to see if it's not just a + or - sign without digits
        return startIdx < token.length();
    }
}
