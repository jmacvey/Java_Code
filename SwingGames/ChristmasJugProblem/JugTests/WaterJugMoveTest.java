package waterjug;

import framework.Move;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A class to test the WaterJugMove class.
 * @author Joshua MacVey, CS2511 Sec 003, Assignment 2
 */
public class WaterJugMoveTest {
    
    // You should use the BridgeMoveTest class as a model for setting up
    // the tests with private instance fields here.
    
    private WaterJugState start = new WaterJugState(0,0);
    
    private Move option1 = new WaterJugMove("Fill Jug X");
    private Move option2 = new WaterJugMove("Fill Jug Y");
    private Move option3 = new WaterJugMove("Empty Jug X");
    private Move option4 = new WaterJugMove("Empty Jug Y");
    private Move option5 = new WaterJugMove("Transfer Jug X to Jug Y");
    private Move option6 = new WaterJugMove("Transfer Jug Y to Jug X");

    /**
     * Tests filling jug X
     */
    @Test
    public void testFillX() {
        WaterJugState next = (WaterJugState) option1.doMove(start);
        // jug y should be full in next state
        assertTrue(next.equals(new WaterJugState(3, 0)));
        // perform same move on next state
        next = (WaterJugState) option1.doMove(next);
        // should receive a null 
        assertTrue(next == null);
    }

    /**
     * Tests filling jug Y
     */
    @Test
    public void testFillY() {
        WaterJugState next = (WaterJugState) option2.doMove(start);
        // jug y should be full in next state
        assertTrue(next.equals(new WaterJugState(0, 4)));
        // perform same move on next state
        next = (WaterJugState) option2.doMove(next);
        // should receive null, as the move is invalid 
        assertTrue(next == null);
    }
    
    /**
     * Tests emptying jug X
     */
    @Test
    public void testEmptyX() {
        WaterJugState next = (WaterJugState) option3.doMove(start);
        // try to empty from an empty jug should return false
        assertTrue(next == null);
        // fill an empty jug
        next = (WaterJugState) option1.doMove(start);
        assertTrue(next.equals(new WaterJugState(3,0)));
        // empty the jug
        next = (WaterJugState) option3.doMove(next);
        // should be back to start
        assertTrue(next.equals(start));
    }
    
    
    /**
     * Tests emptying jug Y
     */
    @Test
    public void testEmptyY() {
        WaterJugState next = (WaterJugState) option4.doMove(start);
        // try to empty from an empty jug should return false
        assertTrue(next == null);
        // fill an empty jug
        next = (WaterJugState) option2.doMove(start);
        assertTrue(next.equals(new WaterJugState(0,4)));
        // empty the jug
        next = (WaterJugState) option4.doMove(next);
        // should be back to start
        assertTrue(next.equals(start));
    }
    
    /**
     * Tests transferring jug X to jug Y
     */
    @Test
    public void testXToY() {
        // try moving from X to Y with an empty State
        WaterJugState next = (WaterJugState) option5.doMove(start);
        // should return a null value
        assertTrue(next == null);
        // fill jug X
        next = (WaterJugState) option1.doMove(start);
        assertTrue(next.equals(new WaterJugState(3,0)));
        // fill jug Y
        next = (WaterJugState) option2.doMove(next);
        assertTrue(next.equals(new WaterJugState(3,4)));
        // try transferring to Y (should return null value)
        next = (WaterJugState) option5.doMove(next);
        assertTrue(next == null);
       // fill jug X
        next = (WaterJugState) option1.doMove(start);
        assertTrue(next.equals(new WaterJugState(3,0)));
        // try transferring to Y (should return a valid state)
        next = (WaterJugState) option5.doMove(next);
        assertTrue(next.equals(new WaterJugState(0,3)));
    }
    
    /**
     * Tests transferring jug Y to jug X
     */
    @Test
    public void testYToX() {
        // try moving from Y to X with an empty State
        WaterJugState next = (WaterJugState) option6.doMove(start);
        // should return a null value
        assertTrue(next == null);
        // fill jug Y
        next = (WaterJugState) option2.doMove(start);
        assertTrue(next.equals(new WaterJugState(0,4)));
        // fill jug X
        next = (WaterJugState) option1.doMove(next);
        assertTrue(next.equals(new WaterJugState(3,4)));
        // try transferring Y to X (should return null value)
        next = (WaterJugState) option6.doMove(next);
        assertTrue(next == null);
       // fill jug Y
        next = (WaterJugState) option2.doMove(start);
        assertTrue(next.equals(new WaterJugState(0,4)));
        // try transferring to Y (should return a valid state)
        next = (WaterJugState) option6.doMove(next);
        assertTrue(next.equals(new WaterJugState(3,1)));
        // try transferring to x to y
        next = (WaterJugState) option5.doMove(next);
        assertTrue(next.equals(new WaterJugState(0,4)));
    }
}
