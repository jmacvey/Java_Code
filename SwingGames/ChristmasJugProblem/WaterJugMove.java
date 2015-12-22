package waterjug;

import framework.Move;
import framework.State;

/**
 * This class represents moves in the Water Jug problem.
 * A move object stores its move name and knows how to apply itself
 * to a water jug state to create a new state representing the result
 * of the move.
 * Note that this class extends the abstract class <b>Move</b> and
 * therefore imports <b>framework.Move</b>.
 * This class inherits the <b>getMoveName()</b> method from its parent
 * and thus it should not have an instance field for the move name.
 * @author jmacvey
*/
public class WaterJugMove extends Move {

    /**
     * Constructs a new water jug move object.
     * Note that the move name is passed to the parent constructor
     * using <b>super</b>.
     * @param moveName the name of this move.
     * It is an error if the name is not one of the following:
     * <ul>
     * <li> "Fill Jug X" </li>
     * <li> "Fill Jug Y" </li>
     * <li> "Empty Jug X" </li>
     * <li> "Empty Jug Y" </li>
     * <li> "Transfer Jug X to Jug Y" </li>
     * <li> "Transfer Jug Y to Jug X" </li>
     * </ul>
     */
    public WaterJugMove(String moveName) {
	super(moveName); 
        newState = null;
    }

    /**
     * Attempts to perform this move on a given water jug state.
     * Note that this method implements the abstract <b>doMove</b> method declared
     * in the parent.
     * Thus the argument of type <b>State</b> must be cast to type
     * <b>WaterJugState</b> before processing.
     * The move to perform is determined by this object's move name.
     * If the move can be performed a new water jug state object is returned that
     * reflects this move.
     * A move cannot be performed if trying to fill or transfer to an already
     * full jug, or if trying to empty or transfer from an empty jug.
     * If the move cannot be performed <b>null</b> is returned.
     * @param otherState the water jug state on which this move is to be performed
     * @return a new water jug state reflecting the move, or <b>null</b> if it
     * cannot be performed
     */
    public State doMove(State otherState) {
	return doMoveHelper(otherState);
    }
    
    //--------------------------------------------------------------------------
    // Private methods and Instance Fields
    //--------------------------------------------------------------------------
    
    /**
     * private helper method that implements doMove().
     * @param otherState the current state of the problem.
     * The function calls moveIfValid(), which manipulates the private data
     * member newState if and only if the move is valid if applied to doMoveHelper's
     * parameter. If the move is valid, newState holds the nextState, which 
     * is subsequently returned by doMoveHelper(), and ultimately by doMove().  Otherwise
     * both functions return a null value after indicating the invalid move
     * to the client.
     * the current state
     * @return 
     */
    private State doMoveHelper(State otherState)
    {
        WaterJugState state = (WaterJugState) otherState;
        newState = new WaterJugState(state.getXVolume(), state.getYVolume());
        // validate the move
        moveIfValid();
        // if the newState is not equal to the current state, then the move
        // is valid, so return the new state
        if (!newState.equals(state))
            return newState;
        // otherwise return null
        else 
            return null;
    }
    
    /**
     * Utility function for public method doMoveHelper() that manipulates data field
     * newState if the move attempted is valid.
     */
    private void moveIfValid()
    {
        int xVolume = newState.getXVolume();
        int yVolume = newState.getYVolume();
        
        switch (getMoveName())
        {
            case ("Fill Jug X"):
                respondToOption1(xVolume);
                break;
            case ("Fill Jug Y"):
                respondToOption2(yVolume);
                break;
            case ("Empty Jug X"):
                respondToOption3(xVolume);
                break;
            case ("Empty Jug Y"):
                respondToOption4(yVolume);
                break;
            case ("Transfer Jug X to Jug Y"):
                respondToOption5(xVolume, yVolume);
                break;
            case ("Transfer Jug Y to Jug X"):
                respondToOption6(xVolume, yVolume);
                break;
        }
    }
    
    /**
     * Response method for user choice "Fill Jug X."
     * @param xVolume current volume of X jug.
     */
    private void respondToOption1(int xVolume)
    {
        if (xVolume != MAX_X_VOLUME)
            newState.setXVolume(MAX_X_VOLUME);
    }
    
    /**
     * Response method for user choice "Fill Jug Y."
     * @param yVolume current volume of Y jug
     */
    private void respondToOption2(int yVolume)
    {
        if (yVolume != MAX_Y_VOLUME)
            newState.setYVolume(MAX_Y_VOLUME);
    }
    
    /**
     * Response method for user choice "Empty Jug X."
     * @param xVolume current volume of Jug X.
     */
    private void respondToOption3(int xVolume)
    {
        if (xVolume != 0)
            newState.setXVolume(0);
    }
    
    /**
     * Response method for user choice "Empty Jug Y."
     * @param yVolume current volume of Jug Y.
     */
    private void respondToOption4(int yVolume)
    {
        if (yVolume != 0)
            newState.setYVolume(0);
    }
    
    /**
     * Response method for user choice "Transfer Jug X to Jug Y."
     * @param xVolume current volume in jug X
     * @param yVolume current volume in jug Y
     */
    private void respondToOption5(int xVolume, int yVolume)
    {
        if (yVolume != MAX_Y_VOLUME && xVolume != 0)
        {
            int oldYVolume = yVolume;
            newState.setYVolume(Math.min(xVolume + yVolume, MAX_Y_VOLUME));
            newState.setXVolume(Math.max(0, (xVolume + oldYVolume) - MAX_Y_VOLUME));
        }
    }
    
    /**
     * Response method for user choice "Transfer Jug Y to Jug X."
     * @param xVolume current state volume in jug X
     * @param yVolume current state volume in jug X
     */
    private void respondToOption6(int xVolume, int yVolume)
    {
        if (xVolume != MAX_X_VOLUME && yVolume != 0)
        {
            int oldXVolume = xVolume;
            newState.setXVolume(Math.min(xVolume + yVolume, MAX_X_VOLUME));
            newState.setYVolume(Math.max(0, (yVolume + xVolume) - MAX_X_VOLUME));
        }
    }
    
    
        

    /**
     * Maximum volume allowed in X water jug
     */
    private final int MAX_X_VOLUME = 3;
    
    /**
     * Maximum volume allowed in Y water jug
     */
    private final int MAX_Y_VOLUME = 4;
    
    /**
     * Prospective new state to be returned after doMove() executes.
     */
    WaterJugState newState;
}
