package waterjug;

import java.util.List;
import framework.Problem;
import framework.State;
import framework.Move;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

/**
 * A class to test the WaterJugProblem class.
 * Note: I used a few methods from Colburn's BridgeProblemTest.java file.
 * This is documented below where necessary.
 * @author Joshua MacVey, CS2511 Sec 003, Assignment 2
 */
public class WaterJugProblemTest {
    
    //--------------------------------------------------------------------------
    // Private Instance Fields and Utility functions
    //--------------------------------------------------------------------------
    
    private Problem problem = new WaterJugProblem();
    
    private List<Move> moves = problem.getMoves();
    
    private Move fillX;     // "Fill Jug X"
    private Move fillY;     // "Fill Jug Y"
    private Move emptyX;    // "Empty Jug X"
    private Move emptyY;    // "Empty Jug Y"
    private Move xferXtoY;  // "Transfer Jug X to Jug Y"
    private Move xferYtoX;  // "Transfer Jug Y to Jug X"
    
    /**
     * Searches the problem's move list for a move with the given name
     * @param moveName the name of the desired move
     * @return the move with the given name or null if not found
     * @author tcolburn
     */
    private Move findMove(String moveName) {
        for (Move move : moves) {
            if (move.getMoveName().equals(moveName)) {
                return move;
            }
        }
        return null;
    }
    
    /**
     * Initializes the move objects.
     */
    private void initializeMoves() {
        fillX = findMove("Fill Jug X");
        fillY = findMove("Fill Jug Y");
        emptyX = findMove("Empty Jug X");
        emptyY = findMove("Empty Jug Y");
        xferXtoY = findMove("Transfer Jug X to Jug Y");
        xferYtoX = findMove("Transfer Jug Y to Jug X");
    }
    
        /**
     * Tries to perform a move on the current state of the problem.
     * The problem's current state is updated to the next state, which 
     * could be null.
     * @param move the move to try
     * @author tcolburn
     */
    private void tryMove(Move move) {
        State state = problem.getCurrentState();
        State next = move.doMove(state);
        problem.setCurrentState(next);
    }
    
    //--------------------------------------------------------------------------
    // Test Methods
    //--------------------------------------------------------------------------
    
    /**
     * Tests to be sure the problem represents all the moves.
     */
    @Test
    public void testMoves() {
        initializeMoves();
        List<Move> moveArray = Arrays.asList(fillX, fillY, 
                emptyX, emptyY, xferXtoY, xferYtoX);
        for (Move moveIterator : moveArray)
        {
            assertTrue(moveIterator != null);
        }
    }

    /**
     * Tests the 4-move solution to the water jug problem
     */
    @Test
    public void testSolution1() {
       initializeMoves();
       assertFalse(problem.success());
                                            //       |   |
                                            //|   |  |   |
                                            //|   |  |   |
                                            //|   |  |   |
                                            //+---+  +---+
                                            //  X      Y  
       
       // fill jug X
       tryMove(fillX);
       assertFalse(problem.success());
                                            //       |   |
                                            //|***|  |   |
                                            //|***|  |   |
                                            //|***|  |   |
                                            //+---+  +---+
                                            //  X      Y         
       
       // xfer from X to Y
       tryMove(xferXtoY);
       assertFalse(problem.success());
                                            //       |   |
                                            //|   |  |***|
                                            //|   |  |***|
                                            //|   |  |***|
                                            //+---+  +---+
                                            //  X      Y         
       
       // fill X
       tryMove(fillX);
       assertFalse(problem.success());
                                            //       |   |
                                            //|***|  |***|
                                            //|***|  |***|
                                            //|***|  |***|
                                            //+---+  +---+
                                            //  X      Y         
       
       // xfer from X to Y
       tryMove(xferXtoY);
       assertTrue(problem.success());
                                            //       |   |
                                            //|   |  |***|
                                            //|***|  |***|
                                            //|***|  |***|
                                            //+---+  +---+
                                            //  X      Y         
    }

    /**
     * Tests the 6-move solution to the water jug problem
     */
    @Test
    public void testSolution2() {
         initializeMoves();                 // Initial State
         assertFalse(problem.success());
                                            //       |   |
                                            //|   |  |   |
                                            //|   |  |   |
                                            //|   |  |   |
                                            //+---+  +---+
                                            //  X      Y  
         
         // fill jug Y
         tryMove(fillY);                    // Move 1
         assertFalse(problem.success());
                                            //       |***|
                                            //|   |  |***|
                                            //|   |  |***|
                                            //|   |  |***|
                                            //+---+  +---+
                                            //  X      Y  

         // xfer to Jug X
         tryMove(xferYtoX);                 // Move 2
         assertFalse(problem.success());
                                            //       |   |
                                            //|***|  |   |
                                            //|***|  |   |
                                            //|***|  |***|
                                            //+---+  +---+
                                            //  X      Y  
         // empty jug Y
         tryMove(emptyY);                   // Move 3
         assertFalse(problem.success());
                                            //       |   |
                                            //|***|  |   |
                                            //|***|  |   |
                                            //|***|  |   |
                                            //+---+  +---+
                                            //  X      Y           
         
         
         // xfer to jug Y
         tryMove(xferXtoY);                 // Move 4
         assertFalse(problem.success());
         
                                            //       |   |
                                            //|   |  |***|
                                            //|   |  |***|
                                            //|   |  |***|
                                            //+---+  +---+
                                            //  X      Y  
         // fill jug X
         tryMove(fillX);                    // Move 5
         assertFalse(problem.success());
                                            //       |   |
                                            //|***|  |***|
                                            //|***|  |***|
                                            //|***|  |***|
                                            //+---+  +---+
                                            //  X      Y  
         
         // xfer to jug Y
         tryMove(xferXtoY);                 // Move 6
         assertTrue(problem.success());
                                            //       |***|
                                            //|   |  |***|
                                            //|***|  |***|
                                            //|***|  |***|
                                            //+---+  +---+
                                            //  X      Y  
         
    }
    
//    /**
//     * Tests the 5-move solution to the water jug problem
//     */
//    @Test
//    public void testSolution3() {
//         assertFalse(problem.success());
//         
//         // fill jug Y
//         tryMove(fillY);
//         assertFalse(problem.success());
//         
//         // xfer to Jug X
//         tryMove(xferYtoX);
//         assertFalse(problem.success());
//         
//         // empty jug Y
//         tryMove(emptyY);
//         assertFalse(problem.success());
//         
//         // xfer to jug Y
//         tryMove(xferXtoY);
//         assertFalse(problem.success());
//         
//         // fill jug X
//         tryMove(fillX);
//         assertFalse(problem.success());
//         
//         // xfer to jug Y
//         tryMove(xferXtoY);
//         assertTrue(problem.success());
//    }
    
    /**
     * Tests the problem's introduction string.
     */
    @Test
    public void testIntro() {
       assertTrue(problem.getIntroduction().equals(
                  "Welcome to the Water Jug Problem.\n\n" 
                + "You are given two empty jugs: jug X holds 3 gallons, jug Y holds 4.\n"
                + "Neither has any measuring markers on it. You have a ready supply\n"
                + "of water. You can fill either jug, empty either jug on the ground\n"
                + "or pour all or some of either jug into the other.  The goal is to\n"
                + "get exactly 2 gallons of water into either jug."));
    }
}
