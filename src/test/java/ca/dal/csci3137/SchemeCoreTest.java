package ca.dal.csci3137;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static ca.dal.csci3137.SchemeCore.*;
import static java.math.BigInteger.*;
import static org.junit.jupiter.api.Assertions.*;

class SchemeCoreTest {
    @Test
    void testEmptyListHasNoAR() {
        assert nil.ar == null;
    }

    @Test
    void testEmptyListHasNoDR() {
        assert nil.dr == null;
    }

    @Test
    void testSymbol() {
        assertEquals(new Symbol("foo"), symbol("foo"));
        assertEquals(new Symbol("bar"), symbol("bar"));
        assertEquals(new Symbol("baz"), symbol("baz"));
    }

    @Test
    void testCons() {
        assertEquals("(1)", cons(1, nil).toString());
        assertEquals("(1 2)", cons(1, cons(2, nil)).toString());
        assertEquals("(1 . 2)", cons(1, 2).toString());
    }

    @Test
    void testCarThrowsExceptionWhenGivenNonPair() {
        assertThrows(Throwable.class, () -> car(ZERO));
        assertThrows(Throwable.class, () -> car(new Symbol("symbol")));
    }

    @Test
    void testCarThrowsExceptionWhenGivenNil() {
        assertThrows(Throwable.class, () -> car(nil));
    }

    @Test
    void testCarReturnsArWhenGivenValidPair() {
        Object ar = new Object();
        assertEquals(ar, car(cons(ar, nil)));
    }

    @Test
    void testCdrThrowsExceptionWhenGivenNonPair() {
        assertThrows(Throwable.class, () -> cdr(ZERO));
        assertThrows(Throwable.class, () -> cdr(new Symbol("symbol")));
    }

    @Test
    void testCdrThrowsExceptionWhenGivenNil() {
        assertThrows(Throwable.class, () -> cdr(nil));
    }

    @Test
    void testCdrReturnsDrWhenGivenValidPair() {
        Object dr = new Object();
        assertEquals(dr, cdr(cons(nil, dr)));
    }

    @Test
    void testListReturnsNilWhenGivenZeroArgs() {
        assertEquals(nil, list());
    }

    @Test
    void testListReturnsPairWithFirsArgAsCar() {
        Object first = new Object();
        assertEquals(first, car(list(first)));
    }

    @Test
    void testListReturnsPairWithSecondArgAsCadr() {
        Object second = new Object();
        assertEquals(second, car(cdr(list(new Object(), second))));
    }

    @Test
    void testIsListReturnsTrueWhenGivenEmptyList() {
        assertTrue(isList(nil));
    }

    @Test
    void testIsListReturnsFalseWhenGivenNonPair() {
        assertFalse(isList(ZERO));
    }

    @Test
    void testIsListReturnsFalseWhenGivenImproperList() {
        assertFalse(isList(cons(ZERO, ONE)));
    }

    @Test
    void testIsListReturnsTrueWhenGivenProperList() {
        assertTrue(isList(list(ZERO, ONE, TWO, TEN)));
    }

    @Test
    void testLengthThrowsExceptionWhenGivenNonPair() {
        assertThrows(Throwable.class, () -> length(ZERO));
    }

    @Test
    void testLengthThrowsExceptionWhenGivenImproperList() {
        assertThrows(Throwable.class, () -> length(cons(ZERO, ONE)));
    }

    @Test
    void testLengthReturnsZeroWhenGivenEmptyList() {
        assertEquals(0, length(nil));
    }

    @Test
    void testLengthReturnsLengthWhenGivenProperList() {
        assertEquals(0, length(list()));
        assertEquals(1, length(list(1)));
        assertEquals(2, length(list(1, 2)));
    }

    @Test
    void testZipThrowsExceptionWhenGivenNoLists() {
        assertThrows(Throwable.class, SchemeCore::zip);
    }

    @Test
    void testZipReturnsEmptyListIfGivenEmptyList() {
        assertEquals(nil, zip(nil));
        assertEquals(nil, zip(list(ZERO), nil));
        assertEquals(nil, zip(nil, list(ZERO)));
    }

    @Test
    void testZipReturnsListOfLists() {
        assertEquals(list(list(ZERO, ONE, TWO, TEN)), zip(list(ZERO), list(ONE), list(TWO), list(TEN)));
        assertEquals(list(list(ZERO, ONE), list(TWO, TEN)), zip(list(ZERO, TWO), list(ONE, TEN)));

        assertEquals(
                list(
                        list(symbol("one"), ONE, symbol("odd")),
                        list(symbol("two"), TWO, symbol("even")),
                        list(symbol("three"), BigInteger.valueOf(3), symbol("odd"))
                ),
                zip(
                        list(symbol("one"), symbol("two"), symbol("three")),
                        list(ONE, TWO, BigInteger.valueOf(3)),
                        list(symbol("odd"), symbol("even"), symbol("odd"), symbol("even"), symbol("odd"), symbol("even"))
                )
        );
    }
}