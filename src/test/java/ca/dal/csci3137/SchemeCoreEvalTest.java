package ca.dal.csci3137;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ca.dal.csci3137.SchemeCore.*;
import static java.math.BigInteger.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchemeCoreEvalTest {
    @Test
    void testEvalReturnsLiteralArgument() {
        for(Object literal : List.of(Boolean.FALSE, ZERO, new Object())) {
            assertEquals(literal, eval(literal, Collections.emptyMap()));
        }
    }

    @Test
    void testEvalMapsSymbolsToTheReferencingEnvironment() {
        Symbol symbol = symbol("symbol");
        Object object = new Object();

        assertEquals(object, eval(symbol, Collections.singletonMap(symbol, object)));
    }

    @Test
    void testEvalThrowsExceptionWhenGivenSymbolNotInTheReferencingEnvironment() {
        assertThrows(Throwable.class, () -> eval(symbol("missing"), Collections.emptyMap()));
    }

    @Test
    void testEvalAppliesProcedures() {
        Procedure procedure = mock(Procedure.class);
        Pair arguments = list(new Object());
        Object returnValue = new Object();

        when(procedure.apply(arguments, Collections.emptyMap())).thenReturn(returnValue);

        assertEquals(returnValue, eval(cons(procedure, arguments), Collections.emptyMap()));

        verify(procedure).apply(arguments, Collections.emptyMap());
    }

    @Test
    void testEvalAlsoEvaluatesCarToDetermineWhichProcedureToCall() {
        Symbol symbol = symbol("procedure");
        Procedure procedure = mock(Procedure.class);
        Pair arguments = list(new Object());
        Object returnValue = new Object();
        Map<Symbol, Object> referencingEnvironment = Collections.singletonMap(symbol, procedure);

        when(procedure.apply(arguments, referencingEnvironment)).thenReturn(returnValue);

        assertEquals(returnValue, eval(cons(symbol, arguments), referencingEnvironment));

        verify(procedure).apply(arguments, referencingEnvironment);
    }

    @Test
    void testEvalThrowsExceptionWhenApplyingNonProcedure() {
        assertThrows(Throwable.class, () -> eval(cons(new Object(), list(new Object())), Collections.emptyMap()));
    }
}