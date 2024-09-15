package ca.dal.csci3137;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.stream.Stream;

import static ca.dal.csci3137.SchemeCore.nil;
import static ca.dal.csci3137.SchemeCore.list;
import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    public static Stream<Arguments> testParser() {
        return Stream.of(
                Arguments.of("10", BigInteger.TEN),
                Arguments.of("-1", BigInteger.ONE.negate()),
                Arguments.of("#f", Boolean.FALSE),
                Arguments.of("#t", Boolean.TRUE),
                Arguments.of("atom", SchemeCore.symbol("atom")),
                Arguments.of("()", nil),
                Arguments.of("'()", list(SchemeCore.symbol("quote"), nil)),
                Arguments.of("(atom)", list(SchemeCore.symbol("atom"))),
                Arguments.of("'(atom)", list(SchemeCore.symbol("quote"), list(SchemeCore.symbol("atom")))),
                Arguments.of("'atom", list(SchemeCore.symbol("quote"), SchemeCore.symbol("atom"))),
                Arguments.of("((lambda (n) (+ n n)) 1)", list(list(SchemeCore.symbol("lambda"), list(SchemeCore.symbol("n")), list(SchemeCore.symbol("+"), SchemeCore.symbol("n"), SchemeCore.symbol("n"))), BigInteger.ONE))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testParser(String source, Object expected) {
        ArrayList<String> tokens = new ArrayList<>(Tokenizer.tokenize(source));
        assertEquals(expected, Parser.parse(tokens));
        assertTrue(tokens.isEmpty());
    }

    @Test
    void testParserThrowsExceptionWhenParsingOpeningParenthesisWithoutClosingParenthesis() {
        assertThrows(Throwable.class, () -> Parser.parse(Tokenizer.tokenize("(")));
    }

    @Test
    void testParserThrowsExceptionWhenParsingClosingParenthesisWithoutOpeningParenthesis() {
        assertThrows(Throwable.class, () -> Parser.parse(Tokenizer.tokenize(")")));
    }
}