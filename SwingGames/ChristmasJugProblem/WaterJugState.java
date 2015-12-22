package waterjug;

import framework.State;
import graph.SimpleVertex;

/**
 * This class represents states of the Water Jug problem. It creates new water
 * jug states, tests states for equality, and produces string representations of
 * them. Note that this class implements the <b>State</b> interface and
 * therefore imports <b>framework.State</b>.
 * @author Joshua MacVey, CS2511 Sec 003, Assignment 2
 */
public class WaterJugState extends SimpleVertex implements State
{

    /**
     * Creates a new water jug state.
     *
     * @param xVolume the volume in the X jug (integer value: 0-3)
     * @param yVolume the volume in the Y jug (integer value: 0-4)
     */
    public WaterJugState(int xVolume, int yVolume)
    {
        // set the xVolume and yVolume
        this.xVolume = xVolume;
        this.yVolume = yVolume;
    }

    /**
     * Tests for equality between this state and the argument state. Two states
     * are equal if the X jugs have the same amount of water and the Y jugs have
     * the same amount of water.
     *
     * @param other the state to test against this state
     * @return whether the states are equal
     */
    public boolean equals(Object other)
    {
        return this.toString().equals(other.toString());
    }

    /**
     * Creates a primitive, non-GUI representation of this WaterJugState object.
     *
     * @return the string representation of this water jug state
     */
    public String toString()
    {
        StringBuilder stateString = new StringBuilder();
        return stringHelper(stateString);
    }

    /**
     * Getter (accessor) method for volume contained in X-jug.
     * @return volume contained in X-jug (integer value 0-3)
     */
    public int getXVolume()
    {
        return xVolume;
    }
    
     /**
     * Getter (accessor) method for volume contained in Y-jug.
     * @return volume contained in Y-jug (integer value 0-4)
     */
    public int getYVolume()
    {
        return yVolume;
    }
    
    /**
     * Setter (mutator) method to set volume of jug X
     * @param xVolume new volume
     * @return this object's xVolume will hold value of parameter
     */
    public void setXVolume(int xVolume)
    {
        this.xVolume = xVolume;
    }
    
      /**
     * Setter (mutator) method to set volume of jug Y
     * @param yVolume new volume for jug Y
     */
    public void setYVolume(int yVolume)
    {
        this.yVolume = yVolume;
    }
    
    /**
     * Heuristic getter.  Due to size of state space, we just return 0.
     * @param goal the final state of the problem.
     * @return returns 0 for all cases.
     */
    public int getHeuristic(State goal) {
        return 0;
    }
    
    
    //--------------------------------------------------------------------------
    // Private Methods and Instances
    //--------------------------------------------------------------------------
    
    /**
     * Private utility method to help public method toString().
     * Calls appendLn.  
     * @param stateString empty StringBuilder used to build the string.
     * @return string contained in stateString after processing.
     */
    private String stringHelper(StringBuilder stateString)
    {
        // append the first line (special case)
        appendFirstLn(stateString);
        // append all other lines depending on the volume of X and Y jugs
        for (int i = 3; i > 0; i--)
            appendLn(i, stateString);
        // append final line
        stateString.append("+---+  +---+\n" 
                + "  X      Y  \n");
        return stateString.toString();
    }
    
     /**
     * Private utility method to append the first line of the JugState display to a
     * StringBuilder object.
     * @param stateString stringBuilder containing partial or complete string
     * representation of the water jug state. 
     * .
     */
    private void appendFirstLn(StringBuilder stateString)
    {
        if (yVolume == 4)
            stateString.append("       |***|\n");
        else
            stateString.append("       |   |\n");
    }
    
    /**
     * Private utility method to append a line of the JugState display to a
     * StringBuilder object.
     * @param currentLn the line to be appended.
     * @param stateString stringBuilder containing partial or complete string
     * representation of the water jug state. 
     * .
     */
    private void appendLn(int currentLn, StringBuilder stateString)
    {
        if (xVolume >= currentLn)
        {
            if (yVolume >= currentLn)
                stateString.append("|***|  |***|\n");
            else stateString.append("|***|  |   |\n");
        }
        else if (yVolume >= currentLn)
            stateString.append("|   |  |***|\n");
        else
            stateString.append("|   |  |   |\n");
    }

        /**
         * Volume of the X-jug (0-3)
         */
        private int xVolume;
        /**
         * Volume of the Y-jug (0-4).
         */
        private int yVolume;
    }
