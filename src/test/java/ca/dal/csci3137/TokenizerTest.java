package ca.dal.csci3137;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenizerTest {
    public static Stream<Arguments> testTokenizer() {
        return Stream.of(
                Arguments.of("", Arrays.asList()),
                Arguments.of(")", Arrays.asList(")")),
                Arguments.of("(#t -10 symbol", Arrays.asList("(", "#t", "-10", "symbol")),
                Arguments.of("((((", Arrays.asList("(", "(", "(", "(")),
                Arguments.of("()", Arrays.asList("(", ")")),
                Arguments.of("'()", Arrays.asList("'", "(", ")")),
                Arguments.of("'( )", Arrays.asList("'", "(", ")")),
                Arguments.of("'  (      )", Arrays.asList("'", "(", ")")),
                Arguments.of("135", Arrays.asList("135")),
                Arguments.of("#t", Arrays.asList("#t")),
                Arguments.of("quotient", Arrays.asList("quotient")),
                Arguments.of("(+ 1 2)", Arrays.asList("(", "+", "1", "2", ")")),
                Arguments.of("'(the quick brown fox)", Arrays.asList("'", "(", "the", "quick", "brown", "fox", ")"))
                );
    }

    @ParameterizedTest
    @MethodSource
    void testTokenizer(String source, List<String> expected) {
        assertEquals(expected, Tokenizer.tokenize(source));
    }
}