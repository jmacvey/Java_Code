
package framework;

import java.awt.Graphics;
//import java.awt.geom.Point2D;
import javax.swing.JComponent;

/**
 * This abstract class represents the canvas in a problem solving domain.
 * The canvas represents the graphical elements associated with a game.
 * The class must be subclassed to override the paintComponent method.
 * @author Joshua MacVey 
 */
public abstract class Canvas extends JComponent
{
    /**
     * Constructor method that takes the initial state of the game and stores
     * it.
     * @param state the initial state of the game.
     */
    public Canvas(State state){
    this.state = state;
    this.previousState = null;
    //this.canvasPos = canvasPos;
    }
    
    /**
     * Getter method for the current state being displayed.
     * @return the current state
     */
    public State getCurrentState(){return state;}
    
    /**
     * Mutator method for the current state.
     * As a side effect, this method sets the previous state to the current state.
     * @param nextState the new state to be displayed.
     */
    void setCurrentState(State nextState) {
        this.previousState = this.state;
        this.state = nextState;}
    
    /**
     * Getter method for the state previously displayed.
     * @return the previous state
     */
    public State getPreviousState(){return previousState;}
    
    /**
     * PaintComponent method for current state. This method must be overridden by
     * subclasses.
     * @param g the graphics context used to display the state.
     */
    @Override
    public abstract void paintComponent(Graphics g);
    
    /**
     * Render method for canvases that apply animation.  This method must
     * be overriden by subclasses that invoke animation graphics.
     */
    public abstract void render();
    
    /**
     * The current state of the game.
     */
    private State state;
    /**
     * The previous state of the game.
     */
    private State previousState;
}
