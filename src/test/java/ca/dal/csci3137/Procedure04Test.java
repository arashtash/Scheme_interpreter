package ca.dal.csci3137;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.dal.csci3137.SchemeCore.*;
import static java.math.BigInteger.*;
import static org.junit.jupiter.api.Assertions.*;

public class Procedure04Test {
    @Test
    void testUnaryPredicatesThrowExceptionsWhenGivenTheWrongNumberOfArguments() {
        for(Procedure unaryPredicate : List.of(Procedure.NULL_P, Procedure.LIST_P, Procedure.PAIR_P, Procedure.INTEGER_P, Procedure.NOT)) {
            assertThrows(Throwable.class, () -> unaryPredicate.apply(list(), Collections.emptyMap()));
            assertThrows(Throwable.class, () -> unaryPredicate.apply(list(nil, nil), Collections.emptyMap()));
        }
    }

    @Test
    void testIntegerComparatorsThrowExceptionsWhenGivenTheWrongNumberOfArguments() {
        for(Procedure integerComparator : List.of(
                Procedure.COMPARATOR_EQUAL_TO,
                Procedure.COMPARATOR_LESS_THAN,
                Procedure.COMPARATOR_GREATER_THAN,
                Procedure.COMPARATOR_LESS_THAN_OR_EQUAL_TO,
                Procedure.COMPARATOR_GREATER_THAN_OR_EQUAL_TO)) {
            assertThrows(Throwable.class, () -> integerComparator.apply(list(), Collections.emptyMap()));
            assertThrows(Throwable.class, () -> integerComparator.apply(list(ZERO), Collections.emptyMap()));
            assertThrows(Throwable.class, () -> integerComparator.apply(list(ZERO, ZERO, ZERO), Collections.emptyMap()));
        }
    }

    @Test
    void testIntegerComparatorsThrowExceptionsWhenGivenArgumentsOfTheWrongType() {
        for(Procedure integerComparator : List.of(
                Procedure.COMPARATOR_EQUAL_TO,
                Procedure.COMPARATOR_LESS_THAN,
                Procedure.COMPARATOR_GREATER_THAN,
                Procedure.COMPARATOR_LESS_THAN_OR_EQUAL_TO,
                Procedure.COMPARATOR_GREATER_THAN_OR_EQUAL_TO)) {
            assertThrows(Throwable.class, () -> integerComparator.apply(list(ZERO, nil), Collections.emptyMap()));
            assertThrows(Throwable.class, () -> integerComparator.apply(list(nil, ZERO), Collections.emptyMap()));
            assertThrows(Throwable.class, () -> integerComparator.apply(list(ZERO, Boolean.TRUE), Collections.emptyMap()));
            assertThrows(Throwable.class, () -> integerComparator.apply(list(Boolean.TRUE, ZERO), Collections.emptyMap()));
        }
    }

    @Test
    void testEqualThrowsExceptionsWhenGivenTheWrongNumberOfArguments() {
        assertThrows(Throwable.class, () -> Procedure.EQUAL_P.apply(list(), Collections.emptyMap()));
        assertThrows(Throwable.class, () -> Procedure.EQUAL_P.apply(list(ZERO), Collections.emptyMap()));
        assertThrows(Throwable.class, () -> Procedure.EQUAL_P.apply(list(ZERO, ZERO, ZERO), Collections.emptyMap()));
    }


    @Test
    void testIFThrowsExceptionsWhenGivenTheWrongNumberOfArguments() {
        assertThrows(Throwable.class, () -> Procedure.IF.apply(list(), Collections.emptyMap()));
        assertThrows(Throwable.class, () -> Procedure.IF.apply(list(ZERO), Collections.emptyMap()));
        assertThrows(Throwable.class, () -> Procedure.IF.apply(list(ZERO, ZERO), Collections.emptyMap()));
        assertThrows(Throwable.class, () -> Procedure.IF.apply(list(ZERO, ZERO, ZERO, ZERO), Collections.emptyMap()));
    }
}
