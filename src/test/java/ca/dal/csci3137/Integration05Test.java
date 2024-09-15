package ca.dal.csci3137;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.math.BigInteger;

import static ca.dal.csci3137.SchemeCore.*;
import static java.math.BigInteger.*;
import static org.junit.jupiter.api.Assertions.*;

public class Integration05Test {
    public static Stream<Arguments> testEval() {
        return Stream.of(
                Arguments.of("(let ((x 2) (y 2) (z 3)) (* x (+ y z)))", TEN),
                Arguments.of("((lambda (x) (+ x x)) 5)", TEN),
                Arguments.of("(let ((x 7)) ((let ((x 6)) (lambda (y) (+ x y))) 4))", TEN),
                Arguments.of("(define fib (lambda (n) (if (< n 2) n (+ (fib (- n 1)) (fib (- n 2)))))) (fib 0)", ZERO),
                Arguments.of("(define fib (lambda (n) (if (< n 2) n (+ (fib (- n 1)) (fib (- n 2)))))) (fib 1)", ONE),
                Arguments.of("(define fib (lambda (n) (if (< n 2) n (+ (fib (- n 1)) (fib (- n 2)))))) (fib 2)", ONE),
                Arguments.of("(define fib (lambda (n) (if (< n 2) n (+ (fib (- n 1)) (fib (- n 2)))))) (fib 3)", TWO),
                Arguments.of("(define fib (lambda (n) (if (< n 2) n (+ (fib (- n 1)) (fib (- n 2)))))) (fib 4)", BigInteger.valueOf(3))
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
    }
}
