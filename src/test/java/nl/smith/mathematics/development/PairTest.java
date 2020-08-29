package nl.smith.mathematics.development;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PairTest {

    @Test
    public void testTostring() {
        Pair pair = new Pair("Mark", "Smith");

        assertEquals("Mark", pair.getLeft());
        assertEquals("Smith", pair.getRight());
    }

}