package ca.dal.csci3137;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static ca.dal.csci3137.SchemeCore.*;
import static java.math.BigInteger.*;
import static org.junit.jupiter.api.Assertions.*;

public class Integration06Test {
    public static @NotNull Stream<Arguments> testEval() {
        return Stream.of(
                Arguments.of("(range 1 10 1)", list(BigInteger.valueOf(1), BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(4), BigInteger.valueOf(5), BigInteger.valueOf(6), BigInteger.valueOf(7), BigInteger.valueOf(8), BigInteger.valueOf(9))),
                Arguments.of("(range 1 20 3)", list(BigInteger.valueOf(1), BigInteger.valueOf(4), BigInteger.valueOf(7), BigInteger.valueOf(10), BigInteger.valueOf(13), BigInteger.valueOf(16), BigInteger.valueOf(19))),
                Arguments.of("(range 21 20 1)", list()),
                Arguments.of("(range 10 -10 -5)", list(BigInteger.valueOf(10), BigInteger.valueOf(5), BigInteger.valueOf(0), BigInteger.valueOf(-5))),

                Arguments.of("(filter list? '(no (yes) 1 ()))", list(list(symbol("yes")), list())),
                Arguments.of("(filter (lambda (x) #t) '(the quick brown fox))", list(symbol("the"), symbol("quick"), symbol("brown"), symbol("fox"))),
                Arguments.of("(filter (lambda (x) #f) '(jumps over the lazy dog))", list()),
                Arguments.of("(filter (lambda (x) x) '(1 z #f #t))", list(BigInteger.valueOf(1), symbol("z"), true)),
                Arguments.of("(filter (lambda (x) (= (remainder x 3) 0)) (range 1 10 1))", list(BigInteger.valueOf(3), BigInteger.valueOf(6), BigInteger.valueOf(9))),

                Arguments.of("(foldl + 0 '(1 2 3 4))", BigInteger.valueOf(10)),
                Arguments.of("(foldl cons '() '(1 2 3 4))", list(BigInteger.valueOf(4), BigInteger.valueOf(3), BigInteger.valueOf(2), BigInteger.valueOf(1)))
        );
    }

    @ParameterizedTest
    @MethodSource
    void testEval(String source, Object expectedValue) {
        Map<Symbol, Object> referencingEnvironment = new HashMap<>(Main.GLOBAL_REFERENCING_ENVIRONMENT);

        try {
            String text = Files.readString(Paths.get(Objects.requireNonNull(Main.class.getResource("/core.scm")).toURI()), StandardCharsets.UTF_8);
            List<String> tokens = Tokenizer.tokenize(text);
            while(!tokens.isEmpty()) {
                Object abstractSyntaxTree = Parser.parse(tokens);
                eval(abstractSyntaxTree, referencingEnvironment);
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        List<String> tokens = Tokenizer.tokenize(source);
        Object value;
        do {
            value = eval(Parser.parse(tokens), referencingEnvironment);
        } while (!tokens.isEmpty());
        assertEquals(expectedValue, value);
    }
}
