package waterjug;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A class to test the WaterJugState class
 * @author Joshua MacVey, CS2511 Sec 003, Assignment 2
 */
public class WaterJugStateTest {
    
    private String state1String = "       |   |\n" +
                                  "|   |  |   |\n" +
                                  "|   |  |   |\n" +
                                  "|   |  |   |\n" +
                                  "+---+  +---+\n" +
                                  "  X      Y  \n";
                                 
    
    private String state2String = "       |***|\n" +
                                  "|***|  |***|\n" +
                                  "|***|  |***|\n" +
                                  "|***|  |***|\n" +
                                  "+---+  +---+\n" +
                                  "  X      Y  \n";
    
    private String state3String = "       |   |\n" +
                                  "|***|  |   |\n" +
                                  "|***|  |   |\n" +
                                  "|***|  |   |\n" +
                                  "+---+  +---+\n" +
                                  "  X      Y  \n";
    
    private WaterJugState state1 = new WaterJugState(0,0);
    
    private WaterJugState state2 = new WaterJugState(3,4);
    
    private WaterJugState state3 = new WaterJugState(3,0);
    
    private WaterJugState state3Copy = new WaterJugState(3,0);
    
    private WaterJugState state2Copy = new WaterJugState(3,4);
    
    private WaterJugState state1Copy = new WaterJugState(0,0);

    /**
     * This method tests the accessors (getters) for <b>WaterJugState</b> objects.
     */
    @Test
    public void testAccessors() {
        assertTrue(state1.getXVolume() == 0);
        assertTrue(state1.getYVolume() == 0);
        assertTrue(state2.getXVolume() == 3);
        assertTrue(state2.getYVolume() == 4);
        assertTrue(state3.getXVolume() == 3);
        assertTrue(state3.getYVolume() == 0);
    }

    /**
     * This method tests the <b>equals</b> method for <b>WaterJugState</b> objects.
     */
    @Test
    public void testEquals() {
        assertTrue(state1.equals(state1Copy));
        assertTrue(state2.equals(state2Copy));
        assertTrue(state3.equals(state3Copy));
        assertFalse(state1.equals(state2));
        assertFalse(state2.equals(state3));
        assertFalse(state3.equals(state1));
        assertFalse(state1.equals(state3));
    }

    /**
     * This method tests the <b>toString</b> method for <b>WaterJugState</b> objects.
     * Look at the definitions of <b>state1String</b> and <b>state2String</b> to see
     * how <b>toString</b> should format a state's string representation.
     */
    @Test
    public void testToString() {
        assertTrue(state1.toString().equals(state1String));
        assertTrue(state2.toString().equals(state2String));
        assertTrue(state3.toString().equals(state3String));
        assertFalse(state3.toString().equals(state1String));
        assertFalse(state1.toString().equals(state2String));
    }
}
