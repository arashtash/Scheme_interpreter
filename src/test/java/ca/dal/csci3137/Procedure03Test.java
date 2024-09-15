package ca.dal.csci3137;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.*;

import static ca.dal.csci3137.SchemeCore.*;
import static java.math.BigInteger.*;
import static org.junit.jupiter.api.Assertions.*;

public class Procedure03Test {
    @Test
    void testQuoteThrowsExceptionWhenGivenTheWrongNumberOfArguments() {
        assertThrows(Throwable.class, () -> Procedure.QUOTE.apply(list(), Collections.emptyMap()));
        assertThrows(Throwable.class, () -> Procedure.QUOTE.apply(list(nil, nil), Collections.emptyMap()));
    }

    @Test
    void testQuoteReturnsItsFirstArgumentWithoutEvaluatingIt() {
        Symbol symbol = symbol("symbol");
        assertEquals(symbol, Procedure.QUOTE.apply(list(symbol), Collections.singletonMap(symbol, new Object())));

        Pair expression = list(Procedure.ADD, ONE, TWO);
        assertEquals(expression, Procedure.QUOTE.apply(list(expression), Collections.emptyMap()));
    }

    @Test
    void testDefineThrowsExceptionIfGivenTheWrongNumberOfArguments() {
        assertThrows(Throwable.class, () -> Procedure.DEFINE.apply(list(nil), Collections.emptyMap()));
        assertThrows(Throwable.class, () -> Procedure.DEFINE.apply(list(nil, nil, nil), Collections.emptyMap()));
    }

    @Test
    void testDefineThrowsExceptionIfFirstArgumentIsNotSymbol() {
        assertThrows(Throwable.class, () -> Procedure.DEFINE.apply(list(ZERO, ONE), Collections.emptyMap()));
    }

    @Test
    void testDefineThrowsExceptionIfSymbolAlreadyDefined() {
        Symbol symbol = symbol("symbol");
        assertThrows(Throwable.class, () -> Procedure.DEFINE.apply(list(symbol, ONE), Collections.singletonMap(symbol, ZERO)));
    }

    @Test
    void testDefineBindsSymbolToValueInReferencingEnvironment() {
        Symbol symbol = symbol("symbol");
        Map<Symbol, Object> referencingEnvironment = new HashMap<>();
        Procedure.DEFINE.apply(list(symbol, ONE), referencingEnvironment);
        assertEquals(ONE, referencingEnvironment.get(symbol));
    }

    @Test
    void testDefineEvaluatesValue() {
        Symbol symbol = symbol("symbol");
        Map<Symbol, Object> referencingEnvironment = new HashMap<>();
        Procedure.DEFINE.apply(list(symbol, list(Procedure.LIST, ZERO)), referencingEnvironment);
        assertEquals(list(ZERO), referencingEnvironment.get(symbol));
    }

    @Test
    void testUnaryProcedureThrowsExceptionWhenGivenTheWrongNumberOfArguments() {
        for(Procedure unaryProcedure : List.of(Procedure.CAR, Procedure.CDR, Procedure.LENGTH)) {
            assertThrows(Throwable.class, () -> unaryProcedure.apply(list(), Collections.emptyMap()));
            assertThrows(Throwable.class, () -> unaryProcedure.apply(list(nil, nil), Collections.emptyMap()));
        }
    }

    @Test
    void testBinaryProcedureThrowsExceptionWhenGivenTheWrongNumberOfArguments() {
        for(Procedure binaryProcedure : List.of(Procedure.CONS, Procedure.ADD, Procedure.SUBTRACT, Procedure.MULTIPLY, Procedure.QUOTIENT, Procedure.REMAINDER)) {
            assertThrows(Throwable.class, () -> binaryProcedure.apply(list(nil), Collections.emptyMap()));
            assertThrows(Throwable.class, () -> binaryProcedure.apply(list(nil, nil, nil), Collections.emptyMap()));
        }
    }

    @Test
    void testCar() {
        assertEquals(ZERO, Procedure.CAR.apply(list(list(symbol("quote"), list(ZERO, ONE))), Collections.singletonMap(symbol("quote"), Procedure.QUOTE)));
    }

    @Test
    void testCdr() {
        assertEquals(list(ONE), Procedure.CDR.apply(list(list(symbol("quote"), list(ZERO, ONE))), Collections.singletonMap(symbol("quote"), Procedure.QUOTE)));
    }

    @Test
    void testLength() {
        assertEquals(TWO, Procedure.LENGTH.apply(list(list(symbol("quote"), list(ZERO, ONE))), Collections.singletonMap(symbol("quote"), Procedure.QUOTE)));
    }

    @Test
    void testCons() {
        assertEquals(cons(ZERO, TWO), Procedure.CONS.apply(list(ZERO, TWO), Collections.emptyMap()));
    }

    @Test
    void testAdd() {
        assertEquals(TWO, Procedure.ADD.apply(list(ONE, ONE), Collections.emptyMap()));
    }

    @Test
    void testSubtract() {
        assertEquals(ONE, Procedure.SUBTRACT.apply(list(TWO, ONE), Collections.emptyMap()));
    }

    @Test
    void testMultiply() {
        assertEquals(BigInteger.valueOf(20), Procedure.MULTIPLY.apply(list(TWO, TEN), Collections.emptyMap()));
    }

    @Test
    void testQuotient() {
        assertEquals(BigInteger.valueOf(5), Procedure.QUOTIENT.apply(list(TEN, TWO), Collections.emptyMap()));
    }

    @Test
    void testRemainder() {
        assertEquals(ZERO, Procedure.REMAINDER.apply(list(TEN, TWO), Collections.emptyMap()));
    }

    @Test
    void testListReturnsTheEmptyListWhenGivenNoArguments() {
        assertEquals(nil, Procedure.LIST.apply(nil, Collections.emptyMap()));
    }

    @Test
    void testListReturnsSingletonListWhenGivenOneArguments() {
        assertEquals(list(ONE), Procedure.LIST.apply(list(ONE), Collections.emptyMap()));
    }

    @Test
    void testListReturnsDoubletonListWhenGivenTwoArguments() {
        assertEquals(list(ONE, TWO), Procedure.LIST.apply(list(ONE, TWO), Collections.emptyMap()));
    }

    @Test
    void testListEvaluatesItsArguments() {
        Symbol symbol = symbol("symbol");
        assertEquals(list(ZERO), Procedure.LIST.apply(list(symbol), Collections.singletonMap(symbol, ZERO)));
    }
}
