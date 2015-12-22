/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package framework;

import graph.DequeAdder;
import graph.Vertex;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

/**
 * A class that creates the GUI components for solving search problems.
 *
 * @author Joshua MacVey, CS2511 Sec. 003
 */
public class GUI extends JComponent
{

    public GUI(Problem problem, Canvas canvas, Canvas finalStateCanvas)
    {
        // initialize problem
        this.problem = problem;
        this.canvas = canvas;
        this.finalStateCanvas = finalStateCanvas;
        // initialize components
        initialState = problem.getCurrentState();
        initializeIntroString();
        initializeOptionsList();
        initializeProblemState();
        initializeResetButton();
        initializeRadioButtons();
        initializeStats();
        initializeShowBtns();

        // manage the layout
        manageLayout();
    }

    //--------------------------------------------------------------------------
    // Private Helper Methods and Instance Fields
    //--------------------------------------------------------------------------
    /**
     * Manages the top-level layout for the GUI using GridBag Layout.
     */
    private void manageLayout()
    {
        // gridBaglayout 
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // intro string
        gbc.weighty = 0;
        gbc.weightx = 0;
        gbc.insets = new Insets(1, 1, 1, 1);
        gbc.anchor = GridBagConstraints.CENTER;
        add(stringLabel, gbc);

        // State / options list
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(stateLabel);
        panel.add(optionsList);
        panel.add(finalStateLabel);
        //gbc.insets = new Insets(2,2,2,2);
        gbc.weightx = .33;
        gbc.weighty = 0;
        gbc.gridy = 1;
        gbc.fill = 1;
        add(panel, gbc);

        // RadioButtons / Reset Button
        radioLabel = new JLabel();
        radioLabel.setLayout(new GridLayout(5, 1, 0, 0));
        radioLabel.setBorder(new TitledBorder("Search Types"));
        radioLabel.setPreferredSize(new Dimension(180, 120));
        // reset button
        gbc.fill = 0;
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1;
        //radioLabel.add(depthFirstButton);
        //radioLabel.add(breadthFirstButton);
        radioLabel.add(AStarButton);
        radioLabel.add(enhancedAStarButton);
        radioLabel.add(solveButton);
        radioLabel.add(showNxtMvBtn);
        radioLabel.add(showAllMoves);
        add(radioLabel, gbc);
        gbc.gridy = 1;
        add(solutionLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(resetButton, gbc);
    }

    /**
     * Initializes the introductory string as a JTextArea.
     */
    private void initializeIntroString()
    {
        introString = new JTextArea(problem.getIntroduction());
        introString.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
        introString.setPreferredSize(new Dimension(
                calculateTextWidth(problem.getIntroduction().split("\\n"), // string array
                        introString.getFontMetrics(introString.getFont())),// font metric
                calculateTextHeight(problem.getIntroduction().split("\\n"),// string array
                        introString.getFontMetrics(introString.getFont()))));  // font metric
        // wrap it in a label
        stringLabel = new JLabel();
        stringLabel.setLayout(new FlowLayout(FlowLayout.CENTER));
        stringLabel.add(introString);
        stringLabel.setPreferredSize(introString.getPreferredSize());
    }

    /**
     * Calculates the text width based on font metrics for use in resizing text
     * boxes.
     *
     * @param lines an array of lines representing a paragraph.
     * @param fm text metrics of the paragraph's desired font.
     * @return the width of the text box, adjusted for text metrics.
     */
    private int calculateTextWidth(String[] lines, FontMetrics fm)
    {
        int numChars = 0;
        String maxString = "";
        // calculate the number of characters in the line
        for (String lineNo : lines)
        {
            if (numChars < lineNo.length())
            {
                numChars = lineNo.length();
                maxString = lineNo;
            }
        }
        // width will be the numChars * text metrics for the font
        int maxWidth = fm.stringWidth(maxString);
        return maxWidth;
    }

    /**
     * Calculates the text height based on font metrics for use in resizing text
     * boxes.
     *
     * @param lines an array of lines representing a paragraph.
     * @param fm text metrics of the paragraph's desired font.
     * @return the height of the text box, adjusted for text metrics.
     */
    private int calculateTextHeight(String[] lines, FontMetrics fm)
    {
        int totalLineHeight = fm.getHeight();
        int totalLines = 1; // account for one line of spacing between Welcome and explanation
        // in the introduction string
        for (String lineNo : lines)
        {
            if (!lineNo.equals("") || !lineNo.equals("\n"))
            {
                totalLines++;
            }
        }

        // max height we want is the total lines * height metrics
        return totalLines * totalLineHeight;
    }

    /**
     * Initializes and stylizes the statistics for the search algorithm.
     */
    private void initializeStats()
    {
        solutionText = new JTextArea();
        solutionLabel = new JLabel();

        solutionText.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 12));
        solutionText.setPreferredSize(
                new Dimension(calculateTextWidth(solutionText.getText().split("\\n"),
                                solutionText.getFontMetrics(solutionText.getFont())) + 15,
                        calculateTextHeight(solutionText.getText().split("\\n"),
                                solutionText.getFontMetrics(solutionText.getFont()))));
        setStats();
        solutionLabel.setLayout(new GridLayout(1, 4));

        solutionLabel.setPreferredSize(new Dimension(solutionText.getPreferredSize().width, +solutionText.getPreferredSize().height + solutionLabel.getIconTextGap() * 2));
        solutionLabel.setBorder(new TitledBorder("Solution Stats"));

        solutionLabel.add(solutionText);
    }

    /**
     * Initializes and stylizes the problem state as a JTextArea component.
     */
    private void initializeProblemState()
    {

        problemState = new JTextArea(problem.getCurrentState().toString());
        problemState.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
        finalProblemState = new JTextArea(problem.getFinalState().toString());
        finalProblemState.setFont(problemState.getFont());
        finalStateLabel.setBorder(new TitledBorder("Final State"));
        finalStateLabel.setLayout(new FlowLayout());
        stateLabel.setBorder(new TitledBorder("Current State"));
        stateLabel.setLayout(new FlowLayout());

        if (canvas != null)
        {
            stateLabel.add(canvas);
            canvas.render();
            stateLabel.setPreferredSize(new Dimension(canvas.getPreferredSize().width
                    + DEFAULT_BUTTON_SPACING * 5,
                    canvas.getPreferredSize().height + DEFAULT_BUTTON_SPACING * 5
            ));
        } else
        {
            stateLabel.add(problemState);
            stateLabel.setPreferredSize(new Dimension(problemState.getPreferredSize().width
                    + (problemState.getFont().getSize() / 2),
                    problemState.getPreferredSize().height + problemState.getFont().getSize() * 5 / 4));
        }
        if (finalStateCanvas != null)
        {
            finalStateLabel.add(finalStateCanvas);
            finalStateCanvas.render();
            finalStateLabel.setPreferredSize(stateLabel.getPreferredSize());
        } else
        {
            finalStateLabel.add(finalProblemState);
            finalStateLabel.setPreferredSize(stateLabel.getPreferredSize());
        }
    }

    /**
     * Initializes and stylizes the options list as a single column comprised of
     * buttons.
     */
    private void initializeOptionsList()
    {
        // set layout and borders
        optionsList = new JLabel();
        optionsList.setBorder(new TitledBorder("Possible Moves"));
        optionsList.setLayout(new GridLayout(problem.getMoves().size(), 1, 0,
                DEFAULT_BUTTON_SPACING));

        // get the moves list
        List<Move> movesList = problem.getMoves();

        // get btn width 
        int btnWidth = calculateBtnWidth(movesList);
        // create and add the buttons
        for (final Move move : movesList)
        {
            // create the button
            JButton button = new JButton(move.getMoveName());
            // set its preferred size
            button.setPreferredSize(new Dimension(btnWidth, DEFAULT_BUTTON_HEIGHT));
            // set the action listemer
            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    State newState = move.doMove(problem.getCurrentState());
                    if (newState != null)
                    {
                        problem.setCurrentState(newState);
                        numMoves++;
                        // change the display
                        if (canvas != null)
                        {
                            canvas.setCurrentState(newState);
                            canvas.render();
                            revalidate();
                        } else
                        {
                            problemState.setText(newState.toString());
                        }
                        // check for win
                        if (problem.success())
                        {
                            JOptionPane.showMessageDialog(introString, "Congratulations."
                                    + " You solved the problem in "
                                    + Integer.toString(numMoves) + " moves.", "Message",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else
                    {
                        JOptionPane.showMessageDialog(introString, "Illegal Move.", "Message",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            });

            btnList.add(button);

            optionsList.add(button);
        }
        optionsList.setPreferredSize(new Dimension(btnWidth,
                (DEFAULT_BUTTON_HEIGHT + DEFAULT_BUTTON_SPACING) * movesList.size()));
    }

    /**
     * Calcuates the width of the button based on a move string's length.
     *
     * @param movesList a list of moves.
     * @return the button width corresponding to the largest string in the moves
     * list (accounts for text metrics).
     */
    private int calculateBtnWidth(List<Move> movesList)
    {
        // get the max width of the string
        String maxString = "";
        for (Move move : movesList)
        {
            if (maxString.length() < move.getMoveName().length())
            {
                maxString = move.getMoveName();
            }
        }

        // get the button width (annoying round-about way to do it)
        JButton tempButton = new JButton();
        Font f = UIManager.getDefaults().getFont("Button.font");
        int btnWidth = tempButton.getFontMetrics(f).stringWidth(maxString)
                + DEFAULT_BUTTON_BUFFER;

        return btnWidth;
    }

    /**
     * Initializes and stylizes the reset button.
     */
    private void initializeResetButton()
    {
        resetButton = new JButton("RESET");
        resetButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                numMoves = 0;
                // implementation of multi-problem choices
                ProblemChooser pc = null;
                if (problem.isMultiProblem())
                {
                    if (problem.hasCanvas())
                    {
                        pc = new ProblemChooser(problem.getInitialStatesList(),
                                problem.getFinalStatesList(),
                                problem.getMoveCounts(),
                                problem.getInitialCanvasList(),
                                problem.getFinalCanvasList()
                        );
                    } else
                    {
                        pc = new ProblemChooser(problem.getInitialStatesList(),
                                problem.getFinalStatesList(), problem.getMoveCounts());
                    }

                    initialState = pc.getStart();
                }
                problem.setCurrentState(initialState);
                if (pc != null) {
                problem.setFinalState(pc.getFinal());}
                problemState.setText(initialState.toString());
                if (canvas != null)
                {
                    canvas.setCurrentState(initialState);
                    if (problem.isMultiProblem() && pc != null)
                    {
                        finalStateCanvas.setCurrentState(pc.getFinal());
                    } else
                    {
                        finalStateCanvas.setCurrentState(problem.getFinalState());
                    }
                    canvas.render();
                    finalStateCanvas.render();
                }
                problem.resetCounters();
                setStats();
                toggleButtons(true);
            }
        });
    }

    /**
     * Initializes and stylizes the reset button.
     */
    private void initializeRadioButtons()
    {
        depthFirstButton = new JRadioButton("Depth-First");
        breadthFirstButton = new JRadioButton("Breadth-First");
        AStarButton = new JRadioButton("Regular A*");
        enhancedAStarButton = new JRadioButton("Enhanced A*");
        AStarButton.setSelected(true);

        solveButton = new JButton("SOLVE");
        solveButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                // turn off all the buttons besides reset
                toggleButtons(false);
                if (breadthFirstButton.isSelected())
                {
                    DequeAdder tailAdder = new DequeAdder()
                    {
                        @Override
                        public void add(Vertex vertex, Deque<Vertex> deque)
                        {
                            deque.addLast(vertex);
                        }
                    };
                    problem.search((Vertex) problem.getCurrentState(),
                            tailAdder);
                } else if (depthFirstButton.isSelected())
                {
                    DequeAdder headAdder = new DequeAdder()
                    {
                        @Override
                        public void add(Vertex vertex, Deque<Vertex> deque)
                        {
                            deque.addFirst(vertex);
                        }
                    };
                    problem.search((Vertex) problem.getCurrentState(),
                            headAdder);
                } else if (AStarButton.isSelected())
                {
                    problem.searchAStar((Vertex) problem.getCurrentState());
                }
                else if (enhancedAStarButton.isSelected())
                {
                    problem.enhancedAStarSearch((Vertex) problem.getCurrentState());
                }
                setStats();
                showNxtMvBtn.setEnabled(true);
                showAllMoves.setEnabled(true);
            }
        }
        );
        btnList.add(solveButton);
        radioButtons = new ButtonGroup();
        radioButtons.add(depthFirstButton);
        radioButtons.add(breadthFirstButton);
        radioButtons.add(AStarButton);
        radioButtons.add(enhancedAStarButton);
    }

    /**
     * Disables the buttons after a search is performed.
     */
    private void toggleButtons(boolean enableFlag)
    {
        for (JButton btn : btnList)
        {
            btn.setEnabled(enableFlag);
        }
    }

    /**
     * Resets the solution statistics text.
     */
    private void setStats()
    {
        solutionText.setText("Solution Length: "
                + Integer.toString(problem.getSolutionLength())
                + "\n# Queue Ops: " + Integer.toString(problem.getQueueOps())
                + "\nMaxQueueSize: " + Integer.toString(problem.getMaxQueueSize())
                //+ "\nOpen Discoveries: " + Integer.toString(problem.getOR())
                //+ "\nClosed Discoveries: " + Integer.toString(problem.getCR())
        );

        // resize
        solutionText.setFont(new Font(Font.SANS_SERIF, Font.BOLD | Font.ITALIC, 12));
        solutionText.setPreferredSize(
                new Dimension(calculateTextWidth(solutionText.getText().split("\\n"),
                                solutionText.getFontMetrics(solutionText.getFont())) + 60,
                        calculateTextHeight(solutionText.getText().split("\\n"),
                                solutionText.getFontMetrics(solutionText.getFont()))));
        solutionLabel.setPreferredSize(new Dimension(solutionText.getPreferredSize().width,
                solutionText.getPreferredSize().height + solutionLabel.getIconTextGap() * 2));
    }

    /**
     * Initializes the showNext move and show move buttons.
     */
    private void initializeShowBtns()
    {
        showNxtMvBtn = new JButton("SHOW NEXT MOVE");
        showNxtMvBtn.setEnabled(false);
        showAllMoves = new JButton("SHOW ALL MOVES");
        showAllMoves.setEnabled(false);
        showNxtMvBtn.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (canvas != null)
                {
                    canvas.setCurrentState((State) problem.getNextMove());
                    canvas.render();
                } else
                {
                    stateLabel.setText(problem.getNextMove().toString());
                }
                if (problem.solnSetEmpty())
                {

                    showNxtMvBtn.setEnabled(false);
                    showAllMoves.setEnabled(false);
                }
            }
        });

        showAllMoves.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                showNxtMvBtn.setEnabled(false);
                showAllMoves.setEnabled(false);
                movesTimer.start();
            }
        }
        );
    }

    /**
     * instance field containing the reset button.
     */
    private JButton resetButton;

    /**
     * instance field containing current problem.
     */
    private final Problem problem;
    /**
     * instance fields containing introString and problemState text areas.
     */
    private JTextArea introString, problemState;
    /**
     * instance label containing state canvas or text representation.
     */
    private final JLabel stateLabel = new JLabel();

    /**
     * instance label containing final state canvas or text representation.
     */
    private final JLabel finalStateLabel = new JLabel();
    /**
     * instance label containing the optionsList for possible moves.
     */
    private JLabel optionsList;
    /**
     * instance label containing state text representation.
     */
    private JLabel stringLabel;
    /**
     * counts number of moves performed.
     */
    private int numMoves = 0;

    /**
     * initial state of the problem.
     */
    private State initialState;

    /**
     * Radio button for search algorithm choices.
     */
    private JRadioButton depthFirstButton;

    /**
     * Radio button for breadthfirstButton
     */
    private JRadioButton breadthFirstButton;

    /**
     * Radio button for A* search
     */
    private JRadioButton AStarButton;
    
    /**
     * Radio button for enhanced A* search
     */
    private JRadioButton enhancedAStarButton;
    
    /**
     * Button group to hold the radio buttons
     */
    private ButtonGroup radioButtons;

    /**
     * Show next move button to enable user to go through algorithmic solution.
     */
    private JButton showNxtMvBtn;

    /**
     * Show all move button to enable user to go through algorithmic solution.
     */
    private JButton showAllMoves;

    /**
     * Solve button to enable search algorithm.
     */
    private JButton solveButton;
    /**
     * default button width.
     */
    private final int DEFAULT_BUTTON_BUFFER = 50;
    /**
     * default button height.
     */
    private final int DEFAULT_BUTTON_HEIGHT = 28;
    /**
     * default button spacing.
     */
    private final int DEFAULT_BUTTON_SPACING = 5;
    /**
     * Label for radios.
     */
    private JLabel radioLabel;

    /**
     * label for solution
     */
    private JLabel solutionLabel;

    /**
     * Text for solution statistics
     */
    private JTextArea solutionText;

    /**
     * List of all the buttons
     */
    private final LinkedList<JButton> btnList = new LinkedList<>();
    /**
     * Canvas for the current state of the game.
     */
    private final Canvas canvas;

    /**
     * Final state canvas for the current game.
     */
    private final Canvas finalStateCanvas;

    /**
     * Final problem state for the current game.
     */
    private JTextArea finalProblemState;

    /**
     * Timer for the moves in event that SHOW ALL MOVES button is clicked.
     */
    private Timer movesTimer = new Timer(2500, new ActionListener()
    {

        @Override
        public void actionPerformed(ActionEvent ae)
        {
            if (!problem.solnSetEmpty())
            {
                if (canvas != null)
                {
                    canvas.setCurrentState((State) problem.getNextMove());
                    canvas.render();
                } else
                {
                    problemState.setText(problem.getNextMove().toString());
                }
            } else
            {
                movesTimer.stop();
            }
        }
    }
    );
}
