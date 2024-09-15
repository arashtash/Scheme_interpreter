package ca.dal.csci3137;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static ca.dal.csci3137.Procedure.*;
import static ca.dal.csci3137.SchemeCore.*;
import static java.math.BigInteger.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Procedure05Test {

    @Test
    void testLetThrowsExceptionWhenGivenWrongNumberOfArguments() {
        assertThrows(Throwable.class, () -> LET.apply(list(), Collections.emptyMap()));
    }

    @Test
    void testLetEvaluatesExpression() {
        Procedure procedure = mock(Procedure.class);

        LET.apply(list(list(), list(procedure)), Collections.emptyMap());

        verify(procedure).apply(eq(nil), any());
    }

    @Test
    void testLetThrowsExceptionIfLocalVariableAssignmentHasWrongNumberOfElements() {
        assertThrows(Throwable.class, () -> LET.apply(list(list(list(symbol("symbol"), ZERO, ONE)), ZERO), Collections.emptyMap()));
    }

    @Test
    void testLetEvaluatesFirstLocalVariableValue() {
        Procedure procedure = mock(Procedure.class);

        LET.apply(list(list(list(symbol("symbol"), list(procedure))), ZERO), Collections.emptyMap());

        verify(procedure).apply(eq(nil), any());
    }

    @Test
    void testLetEvaluatesSecondLocalVariableValue() {
        Procedure procedure = mock(Procedure.class);

        LET.apply(list(list(list(symbol("symbol1"), ZERO), list(symbol("symbol2"), list(procedure))), ZERO), Collections.emptyMap());

        verify(procedure).apply(eq(nil), any());
    }

    @Test
    void testLetIncludesLocalVariablesWhenEvaluatingExpression() {
        Symbol symbol = symbol("symbol");
        Object value = ZERO;
        Procedure procedure = (arguments, referencingEnvironment) -> {
            assertTrue(referencingEnvironment.containsKey(symbol));
            assertEquals(value, referencingEnvironment.get(symbol));
            return null;
        };

        LET.apply(list(list(list(symbol, value)), list(procedure)), Collections.emptyMap());
    }

    @Test
    void testLetReturnsExpressionResult() {
        Procedure procedure = mock(Procedure.class);
        when(procedure.apply(any(), any())).thenReturn(TWO);

        assertEquals(TWO, LET.apply(list(list(), list(procedure)), Collections.emptyMap()));
    }

    @Test
    void testLambdaThrowsExceptionWhenGivenWrongNumberOfArguments() {
        assertThrows(Throwable.class, () -> LAMBDA.apply(list(), Collections.emptyMap()));
    }

    @Test
    void testLambdaThrowsExceptionWhenFirstElementOfArgumentListIsNonSymbol() {
        assertThrows(Throwable.class, () -> LAMBDA.apply(list(list(ONE), list(TWO)), Collections.emptyMap()));
    }

    @Test
    void testLambdaThrowsExceptionWhenSecondElementOfArgumentListIsNonSymbol() {
        assertThrows(Throwable.class, () -> LAMBDA.apply(list(list(symbol("symbol"), ONE), list(TWO)), Collections.emptyMap()));
    }

    @Test
    void testLambdaReturnsProcedure() {
        assertInstanceOf(Procedure.class, LAMBDA.apply(list(list(symbol("symbol")), list(ZERO)), Collections.emptyMap()));
    }

    @Test
    void testEvaluatingProcedureReturnedByLambdaCausesExpressionEvaluation() {
        Procedure procedure = mock(Procedure.class);

        Procedure lambdaExpression = (Procedure) LAMBDA.apply(list(list(), list(procedure)), Collections.emptyMap());
        lambdaExpression.apply(list(), Collections.emptyMap());

        verify(procedure).apply(eq(nil), any());
    }

    @Test
    void testEvaluatingProcedureReturnedByLambdaReturnsExpressionResult() {
        Procedure procedure = mock(Procedure.class);
        when(procedure.apply(any(), any())).thenReturn(TEN);

        Procedure lambdaExpression = (Procedure) LAMBDA.apply(list(list(), list(procedure)), Collections.emptyMap());
        assertEquals(TEN, lambdaExpression.apply(list(), Collections.emptyMap()));
    }

    @Test
    void testEvaluationProcedureReturnedByLambdaThrowsExceptionWhenGivenWrongNumberOfArguments() {
        Procedure lambdaExpression = (Procedure) LAMBDA.apply(list(list(), ZERO), Collections.emptyMap());

        assertThrows(Throwable.class, () -> lambdaExpression.apply(list(ZERO), Collections.emptyMap()));
    }

    @Test
    void testLambdaIncludesLocalArgumentsWhenEvaluatingExpression() {
        Symbol symbol = symbol("symbol");
        Object value = ZERO;
        Procedure procedure = (arguments, referencingEnvironment) -> {
            assertTrue(referencingEnvironment.containsKey(symbol));
            assertEquals(value, referencingEnvironment.get(symbol));
            return null;
        };

        Procedure lambdaExpression = (Procedure) LAMBDA.apply(list(list(symbol), list(procedure)), Collections.emptyMap());

        lambdaExpression.apply(list(value), Collections.emptyMap());
    }

    @Test
    void testLambdaCapturesVariablesFromReferencingEnvironmentWhenLambdaExpressionEvaluated() {
        Symbol symbol = symbol("symbol");
        Procedure capturedProcedure = mock(Procedure.class);
        Procedure localProcedure = mock(Procedure.class);

        eval(list(LAMBDA.apply(list(nil, list(symbol)), Collections.singletonMap(symbol, capturedProcedure))), Collections.singletonMap(symbol, localProcedure));

        verify(capturedProcedure).apply(eq(nil), any());
        verify(localProcedure, never()).apply(any(), any());
    }
}
