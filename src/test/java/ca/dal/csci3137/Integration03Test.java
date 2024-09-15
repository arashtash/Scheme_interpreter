package ca.dal.csci3137;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static ca.dal.csci3137.SchemeCore.*;
import static java.math.BigInteger.*;
import static org.junit.jupiter.api.Assertions.*;

public class Integration03Test {
    public static Stream<Arguments> testEval() {
        return Stream.of(
                Arguments.of("2", TWO),
                Arguments.of("(+ 1 1)", TWO),
                Arguments.of("(- 1 1)", ZERO),
                Arguments.of("(* 2 5)", TEN),
                Arguments.of("(quotient 5 2)", TWO),
                Arguments.of("(remainder 5 2)", ONE),
                Arguments.of("(+ (* 1 1) (* 1 1))", TWO),
                Arguments.of("'()", nil),
                Arguments.of("(cons 0 '())", list(ZERO)),
                Arguments.of("(car '(1 2))", ONE),
                Arguments.of("(cdr '(1 2))", list(TWO)),
                Arguments.of("(length '(0 1))", TWO),
                Arguments.of("(list 0 1 2 10)", list(ZERO, ONE, TWO, TEN)),
                Arguments.of("(list (- 1 1) (remainder 5 2) (+ 1 1) (* 2 5))", list(ZERO, ONE, TWO, TEN)),
                Arguments.of("(define x 1) (+ x x)", TWO),
                Arguments.of("(define plus +) (plus 1 1)", TWO)
        );
    }

    @ParameterizedTest
    @MethodSource
    void testEval(String source, Object expectedValue) {
        Map<Symbol, Object> referencingEnvironment = new HashMap<>(Main.GLOBAL_REFERENCING_ENVIRONMENT);
        List<String> tokens = Tokenizer.tokenize(source);
        Object value;
        do {
            value = eval(Parser.parse(tokens), referencingEnvironment);
        } while (!tokens.isEmpty());
        assertEquals(expectedValue, value);
    }}
