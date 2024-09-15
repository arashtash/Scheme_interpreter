package ca.dal.csci3137;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static ca.dal.csci3137.SchemeCore.*;
import static org.junit.jupiter.api.Assertions.*;


class PairTest {
    @Test
    void testNilToString() {
        assertEquals("()", nil.toString());
    }

    @Test
    void testSingletonToString() {
        assertEquals("(1)", new Pair(1, nil).toString());
    }

    @Test
    void testImproperListToString() {
        assertEquals("(1 . 2)", new Pair(1, 2).toString());
    }

    @Test
    void testDoubletonToString() {
        assertEquals("(1 2)", new Pair(1, new Pair(2, nil)).toString());
    }

    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(Pair.class).verify();
    }
}