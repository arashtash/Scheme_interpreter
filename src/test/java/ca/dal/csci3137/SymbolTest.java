package ca.dal.csci3137;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class SymbolTest {
    @Test
    public void testEqualsContract() {
        EqualsVerifier.forClass(Symbol.class).verify();
    }
}