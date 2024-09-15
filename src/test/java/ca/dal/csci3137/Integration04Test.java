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

public class Integration04Test {
    public static Stream<Arguments> testEval() {
        return Stream.of(
                Arguments.of("(null? -7)", Boolean.FALSE),
                Arguments.of("(list? -7)", Boolean.FALSE),
                Arguments.of("(pair? -7)", Boolean.FALSE),
                Arguments.of("(integer? -7)", Boolean.TRUE),
                Arguments.of("(null? '())", Boolean.TRUE),
                Arguments.of("(list? '())", Boolean.TRUE),
                Arguments.of("(pair? '())", Boolean.FALSE),
                Arguments.of("(integer? '())", Boolean.FALSE),
                Arguments.of("(null? '(0))", Boolean.FALSE),
                Arguments.of("(list? '(0))", Boolean.TRUE),
                Arguments.of("(pair? '(0))", Boolean.TRUE),
                Arguments.of("(integer? '(0))", Boolean.FALSE),
                Arguments.of("(null? (cons 1 2))", Boolean.FALSE),
                Arguments.of("(list? (cons 1 2))", Boolean.FALSE),
                Arguments.of("(pair? (cons 1 2))", Boolean.TRUE),
                Arguments.of("(integer? (cons 1 2))", Boolean.FALSE),
                Arguments.of("(null? (+ 1 2))", Boolean.FALSE),
                Arguments.of("(list? (+ 1 2))", Boolean.FALSE),
                Arguments.of("(pair? (+ 1 2))", Boolean.FALSE),
                Arguments.of("(integer? (+ 1 2))", Boolean.TRUE),

                Arguments.of("(not #t)", Boolean.FALSE),
                Arguments.of("(not #f)", Boolean.TRUE),
                Arguments.of("(not (null? '()))", Boolean.FALSE),
                Arguments.of("(and #f #f)", Boolean.FALSE),
                Arguments.of("(and #f #t)", Boolean.FALSE),
                Arguments.of("(and #t #f)", Boolean.FALSE),
                Arguments.of("(and #t #t)", Boolean.TRUE),
                Arguments.of("(define x '()) (and (pair? x) (car x))", Boolean.FALSE),
                Arguments.of("(define x '(0)) (and (pair? x) (car x))", ZERO),
                Arguments.of("(or #f #f)", Boolean.FALSE),
                Arguments.of("(or #f #t)", Boolean.TRUE),
                Arguments.of("(or #t #f)", Boolean.TRUE),
                Arguments.of("(or #t #t)", Boolean.TRUE),
                Arguments.of("(define x '()) (or (null? x) (car x))", Boolean.TRUE),
                Arguments.of("(define x '(0)) (or (null? x) (car x))", ZERO),

                Arguments.of("(= (- 0 1) (+ 0 0))", Boolean.FALSE),
                Arguments.of("(= (+ 0 0) (+ 0 0))", Boolean.TRUE),
                Arguments.of("(= (+ 0 1) (+ 0 0))", Boolean.FALSE),
                Arguments.of("(< (- 0 1) (+ 0 0))", Boolean.TRUE),
                Arguments.of("(< (+ 0 0) (+ 0 0))", Boolean.FALSE),
                Arguments.of("(< (+ 0 1) (+ 0 0))", Boolean.FALSE),
                Arguments.of("(> (- 0 1) (+ 0 0))", Boolean.FALSE),
                Arguments.of("(> (+ 0 0) (+ 0 0))", Boolean.FALSE),
                Arguments.of("(> (+ 0 1) (+ 0 0))", Boolean.TRUE),
                Arguments.of("(<= (- 0 1) (+ 0 0))", Boolean.TRUE),
                Arguments.of("(<= (+ 0 0) (+ 0 0))", Boolean.TRUE),
                Arguments.of("(<= (+ 0 1) (+ 0 0))", Boolean.FALSE),
                Arguments.of("(>= (- 0 1) (+ 0 0))", Boolean.FALSE),
                Arguments.of("(>= (+ 0 0) (+ 0 0))", Boolean.TRUE),
                Arguments.of("(>= (+ 0 1) (+ 0 0))", Boolean.TRUE),

                Arguments.of("(equal? '(1 2 3) (cons 1 '(2 3)))", Boolean.TRUE),
                Arguments.of("(equal? '(1 2 3) (cons 1 (cons 2 3)))", Boolean.FALSE),

                Arguments.of("(if (< 1 2) 'less fail)", symbol("less")),
                Arguments.of("(if (> 1 2) fail 'not-less)", symbol("not-less"))
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
