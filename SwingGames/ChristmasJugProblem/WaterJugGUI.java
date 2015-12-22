package waterjug;

import framework.GUI;
import javax.swing.JFrame;

/**
 * A class to test your GUI class on the water jug problem.
 * @author tcolburn
 */
public class WaterJugGUI extends JFrame {
    
    public WaterJugGUI() {
        WaterJugProblem wjp = new WaterJugProblem();
        add(new GUI(wjp, new WaterjugCanvas((WaterJugState)wjp.getCurrentState()), 
                new WaterjugCanvas((WaterJugState) wjp.getFinalState())));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    /**
     * This method launches the gui.
     * @param args ignored
     */
    public static void main(String[] args) {
        new WaterJugGUI();
    }
}
   
