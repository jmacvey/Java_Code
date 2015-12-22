/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package waterjug;

import framework.Canvas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.Timer;

/**
 * The WaterjugCanvas class provides primitive static and animated graphics for
 * the Waterjug problem.
 *
 * @author Joshua MacVey, CS2511 Sec 003
 */
public class WaterjugCanvas extends Canvas
{

    public WaterjugCanvas(WaterJugState state)
    {
        super(state);
        setPreferredSize(new Dimension(250, 300));
        initializeObjects();
        initializeRectangles();
        initializeSnowflakes();
        initializeImages();
        t1.start();
    }

    /**
     * Paints the graphics associated with the current state to the game window.
     *
     * @param g
     */
    @Override // so many draw methods.. definitely should abstract that into a class
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        // draw the border/background
        drawBorder(g2);
        // draw the wreaths
        drawWreaths(g2);
        // fill the jug interior graphics
        fillJugs(g2);
        // draw the jug outline
        drawJugs(g2);
        // draw the flakes.
        drawFlakes(g2);
        // draw the labels
        drawLabels(g2);
    }

    /**
     * This method starts animation of the boxes based on user input.
     */
    @Override
    public void render()
    {
        t.start();
    }

    /**
     * Initializes bin graphical objects and their corresponding points for
     * animation purposes.
     */
    private void initializeObjects()
    {
        double x_x0 = graphicsWidth * .111;
        double x_y0 = .30 * (initialPos.y + graphicsHeight);
        // initialize the X bin
        xBin.moveTo(x_x0, x_y0); // top left
        xBin.lineTo(x_x0, x_y0 + .45 * (initialPos.y + graphicsHeight)); // bottom left
        xBin.lineTo((x_x0) + .333 * (graphicsWidth),
                xBin.getCurrentPoint().getY()); // bottom right
        xBin.lineTo(xBin.getCurrentPoint().getX(), x_y0); // top left
        // close the path
        xBin.moveTo(xBin.getCurrentPoint().getX(), x_y0);
        xBin.closePath();
        // store the points for animated purposes
        xBinPoints = new ArrayList<>();
        xBinPoints.add(new Point2D.Double(x_x0, x_y0)); // top left
        xBinPoints.add(new Point2D.Double(x_x0,
                x_y0 + .45 * (initialPos.y + graphicsHeight))); // bottom left
        xBinPoints.add(new Point2D.Double(x_x0 + .333 * (graphicsWidth),
                x_y0 + .45 * (initialPos.y + graphicsHeight))); // bottom right
        xBinPoints.add(new Point2D.Double(x_x0 + .333 * graphicsWidth, x_y0)); // top right

        // create the yBin
        double y_x0 = .6111 * graphicsWidth;
        double y_y0 = .15 * (initialPos.y + graphicsHeight);

        yBin.moveTo(y_x0, y_y0); // topleft
        yBin.lineTo(y_x0, y_y0 + .60 * (initialPos.y + graphicsHeight)); // bottomLeft
        yBin.lineTo(y_x0 + .333 * graphicsWidth, yBin.getCurrentPoint().getY()); // bottom right
        yBin.lineTo(yBin.getCurrentPoint().getX(), y_y0); // top right
        // close the path
        yBin.moveTo(yBin.getCurrentPoint().getX(), yBin.getCurrentPoint().getY());
        yBin.closePath();

        yBinPoints = new ArrayList<>();
        // add points to yBin for animation purposes
        yBinPoints.add(new Point2D.Double(y_x0, y_y0)); // topleft
        yBinPoints.add(new Point2D.Double(y_x0,
                y_y0 + .60 * (initialPos.y + graphicsHeight))); // bottomleft
        yBinPoints.add(new Point2D.Double(y_x0 + .333 * graphicsWidth,
                y_y0 + .60 * (initialPos.y + graphicsHeight))); // bottom right
        yBinPoints.add(new Point2D.Double(y_x0 + .333 * graphicsWidth,
                y_y0)); // top right

    }

    /**
     * Initializes the animation rectangles that are contained within xBinPoints
     * and yBinPoints
     */
    private void initializeRectangles()
    {
        // set the rectangle top to bottom of xBin and its height to 0
        xBox.setRect(new Rectangle2D.Double(xBinPoints.get(1).x, xBinPoints.get(1).y,
                xBinPoints.get(2).x - xBinPoints.get(1).x, 0));

        yBox.setRect(new Rectangle2D.Double(yBinPoints.get(1).x, yBinPoints.get(1).y,
                yBinPoints.get(2).x - yBinPoints.get(1).x, 0));
    }

    /**
     * Initializes images stores as resources in jar file.
     */
    private void initializeImages()
    {
        try
        {
            wreath = ImageIO.read(getClass().getResourceAsStream("/resources/wreath05.gif"));
        } catch (IOException e)
        {
            wreath = null;
        }
        try
        {
            candyCane = ImageIO.read(getClass().getResourceAsStream("/resources/candy_canes.jpg"));
        } catch (IOException e)
        {
            candyCane = null;
        }
        // initialize the texturePaint for the snowflake
        try
        {
            snowImg = ImageIO.read(getClass().getResourceAsStream("/resources/snowflake_image.gif"));
        } catch (IOException e)
        {
            snowImg = null;
        }
    }

    /**
     * Initializes snowflakes being represented by rectangles and their
     * respective velocities.
     */
    private void initializeSnowflakes()
    {
        snowflakesInitialPos = new ArrayList<>();
        sfv = new ArrayList<>();
        Random rn = new Random();
        int leftOrRight;
        // would be easy to implement a slider to control number of flakes
        // would be even easier to abstract the snowflake graphic functionality
        // into a "weather" class that takes an image, and screen width

        for (int i = 0; i < NUM_SNOWFLAKES; i++)
        {
            leftOrRight = rn.nextInt(2);
            // snowflake velocities between 1 and 3 in x and 1 and 4 y
            switch (leftOrRight)
            {
                case 0:
                    sfv.add(new Point2D.Double(rn.nextInt(2) + 1, 1 + rn.nextInt(4)));
                    break;
                default:
                    sfv.add(new Point2D.Double(-(rn.nextInt(2) + 1), 1 + rn.nextInt(4)));
            }

            // snowflake initial positions between 0 and width
            snowflakesInitialPos.add(new Rectangle.Double(rn.nextInt((int) graphicsWidth - 1) + 1, 0,
                    25, 27));
        } // end for
        snowflakesPos = new ArrayList<>();

        for (Rectangle.Double pos : snowflakesInitialPos)
        {
            snowflakesPos.add(new Rectangle.Double(pos.x, pos.y, pos.width, pos.height));
        }
    }

    /**
     * Calculates Fontmetrics for the waterjug labels.
     *
     * @return the Fontmetrics for Sans_Serif, BOLD/ITALIC, 24pt Font
     */
    private FontMetrics getFontMetrics()
    {

        JComponent label = new JTextArea();
        FontMetrics fm = label.getFontMetrics(labelFont);
        return fm;
    }

    /**
     * Initializes the labels associated with each jug.
     *
     * @return an array of the labels for the current bins in the order {xLabel,
     * yLabel}
     */
    private String[] generateBinLabels()
    {
        WaterJugState currentState = (WaterJugState) super.getCurrentState();
        String xLabel = "X("
                + Integer.toString(currentState.getXVolume()) + ")";
        String yLabel = "Y("
                + Integer.toString(currentState.getYVolume()) + ")";
        String[] labels =
        {
            xLabel, yLabel
        };
        return labels;
    }
    
    /**
     * Draws the border and background for the canvas.
     * @param g the graphics context for the component.
     */

    private void drawBorder(Graphics2D g)
    {
                // create and draw the border.
        RoundRectangle2D.Float border = new RoundRectangle2D.Float(
                initialPos.x, initialPos.y,
                graphicsWidth + initialPos.x, graphicsHeight,
                40, 40);
        GradientPaint redtogreen = new GradientPaint(border.x, border.y, Color.RED,
                border.x + border.width, border.y + border.height, Color.GREEN);
        g.setPaint(redtogreen);
        g.fill(border);
        g.draw(border);
    }
    /**
     * Draws the X and Y waterJug outline graphics.
     *
     * @param g the graphics context for the component.
     */
    private void drawJugs(Graphics2D g)
    {
        g.setStroke(new BasicStroke(5));
        g.setColor(Color.black);
        g.draw(xBin);
        g.draw(yBin);
    }
    
    /**
     * Draws the binLabels based on fontmetrics and geometry.
     * @param g the graphics context for the component.
     */
    private void drawLabels(Graphics2D g)
    {
        // draw the Labels using fontmetrics and geometry
        String[] labels = generateBinLabels();
        double xXoffset = ((xBinPoints.get(2).x - xBinPoints.get(1).x)
                - getFontMetrics().stringWidth(labels[0])) * .5;
        double yXoffset = ((yBinPoints.get(2).x - yBinPoints.get(1).x)
                - getFontMetrics().stringWidth(labels[1])) * .5;

        g.setColor(Color.white);
        g.setFont(labelFont);
        g.drawString(labels[0], (int) (xBinPoints.get(1).x + xXoffset),
                (int) (xBinPoints.get(1).y + getFontMetrics().getHeight()));
        g.drawString(labels[1], (int) (yBinPoints.get(1).x + yXoffset),
                (int) (yBinPoints.get(1).y + getFontMetrics().getHeight()));

    }
    
    /**
     * Draws the wreath decorations on the canvas.
     * @param g the graphics context for the component
     */
    private void drawWreaths(Graphics2D g){
                // create and draw the bins (fetching the image)
        TexturePaint tp;
        Rectangle.Double santaRec = new Rectangle.Double(initialPos.x, 0, 50, 60);

        if (wreath != null)
        {
            tp = new TexturePaint(wreath, santaRec);
            g.setPaint(tp);
            g.fill(santaRec);
            g.draw(santaRec);
            santaRec = new Rectangle.Double(santaRec.getX() + graphicsWidth
                    - santaRec.getWidth(),
                    santaRec.getY(), santaRec.getWidth(), santaRec.getHeight());
            g.fill(santaRec);
            g.draw(santaRec);
        }
    } 
    
    /**
     * Fills the jugs if they need to be filled.
     * @param g the graphics context for the component.
     */
    private void fillJugs(Graphics2D g)
    {
        TexturePaint tp;
        if (candyCane != null)
        {
            {
                tp = new TexturePaint(candyCane, xBox);
                g.setPaint(tp);
                g.fill(xBox);
                g.draw(xBox);
                tp = new TexturePaint(candyCane, yBox);
                g.setPaint(tp);
                g.fill(yBox);
                g.draw(yBox);
            }
        } else
        {
            // draw and fill the xbox and ybox
            g.setColor(Color.white);
            g.fill(xBox);
            g.draw(xBox);
            g.fill(yBox);
        }
    }

    /**
     * Updates the xBox height and upper-left coordinate position to reflect
     * animation.
     *
     * @return the double precision difference between the previous state and
     * the current state of the XJug.
     */
    private double updateXBox()
    {
        WaterJugState currentState = (WaterJugState) super.getCurrentState();

        double previousStateYCoord = xBox.y; // current y value of the box is
        // the previous state y coord
        double currentStateHeight = 0;
        if (currentState.getXVolume() != 0)
        {
            currentStateHeight
                    = (double) currentState.getXVolume()
                    * (xBinPoints.get(1).y - xBinPoints.get(0).y) / 3;
        }

        double currentStateYCoord = xBinPoints.get(1).y - currentStateHeight;
        // current xheight = (bottom - top) / xVolume
        // current yCord = bottom - height

        if (previousStateYCoord > currentStateYCoord)
        {
            xBox.setRect(new Rectangle2D.Double(xBox.x,
                    xBox.y - 1, xBox.width, xBox.height + 1));
        } else
        {
            xBox.setRect(new Rectangle2D.Double(xBox.x,
                    xBox.y + 1, xBox.width, xBox.height - 1));
        }

        return previousStateYCoord - currentStateYCoord;
    }

    /**
     * Updates the xBox height and upper-left coordinate position to reflect
     * animation.
     *
     * @return the double precision difference between the previous state and
     * the current state of the XJug.
     */
    private double updateYBox()
    {
        WaterJugState currentState = (WaterJugState) super.getCurrentState();

        double previousStateY2Coord = yBox.y; // current y value of the box is
        // the previous state y coord
        double currentState2Height = 0;
        if (currentState.getYVolume() != 0)
        {
            currentState2Height
                    = (double) currentState.getYVolume()
                    * (yBinPoints.get(1).y - yBinPoints.get(0).y) / 4;
        }

        double currentStateY2Coord = yBinPoints.get(1).y - currentState2Height;
        // current xheight = (bottom - top) / xVolume
        // current yCord = bottom - height

        if (previousStateY2Coord > currentStateY2Coord)
        {
            yBox.setRect(new Rectangle2D.Double(yBox.x,
                    yBox.y - 1, yBox.width, yBox.height + 1));
        } else
        {
            yBox.setRect(new Rectangle2D.Double(yBox.x,
                    yBox.y + 1, yBox.width, yBox.height - 1));
        }
        return (previousStateY2Coord - currentStateY2Coord);
    }

    /**
     * Updates the snowflake position based on velocity.
     */
    private void updateSnowFlakes()
    {
        int velocityIndex = 0;
        Random rn = new Random();
        int leftOrRight;
        for (Rectangle.Double flake : snowflakesPos) // for each snowflake
        {
            leftOrRight = rn.nextInt(2);
            // update position
            if (flake.x > graphicsWidth || flake.y > graphicsHeight)
            // wrap the snowflake to the other side of the screen if
            // out of bounds and change velocity for randomized
            {
                flake.setRect(snowflakesInitialPos.get(velocityIndex));
                switch (leftOrRight)
                {
                    case 0:
                        sfv.set(velocityIndex,
                                new Point2D.Double(rn.nextInt(2) + 1,
                                        1 + rn.nextInt(4)));
                        break;
                    default:
                        sfv.set(velocityIndex,
                                new Point2D.Double(-(rn.nextInt(2) + 1),
                                        1 + rn.nextInt(4)));
                }
            } else
            {
                flake.setRect(flake.x + sfv.get(velocityIndex).x,
                        flake.y + sfv.get(velocityIndex).y,
                        flake.width, flake.height);
                velocityIndex++;
            }
        }
        // switch of the velocities at each index to give illusion of 
        // random motion
//        for (int i = 0, j = sfv.size() - 1; i < sfv.size(); i++, j--)
//        {
//            Collections.swap(sfv, i, j);
//        }
    }

    /**
     * Draws the snowflakes in their positions to the screen.
     */
    private void drawFlakes(Graphics2D g)
    {
        for (Rectangle.Double flake : snowflakesPos)
        {
            g.drawImage(snowImg, (int) flake.x, (int) flake.y,
                    (int) flake.width, (int) flake.height, null);
        }
    }
    /**
     * Default graphics display width.
     */
    private final float graphicsWidth = 240;
    /**
     * Default graphics display height.
     */
    private final float graphicsHeight = 260;
    /**
     * default offset from the graph
     */
    private final double offset = (.416) * 240;
    /**
     * List of points for the canvas painting
     */
    private final Point2D.Float initialPos = new Point2D.Float(5, 5);
    /**
     * Path object that defines the yBin.
     */
    private final GeneralPath yBin = new GeneralPath();
    /**
     * Path object that defines the xBin.
     */
    private final GeneralPath xBin = new GeneralPath();
    /**
     * List of points of 4 corners of xBin.
     */
    private List<Point2D.Double> xBinPoints;
    /**
     * List of points of 4 corners of yBin.
     */
    private List<Point2D.Double> yBinPoints;
    /**
     * List of labels for the bins. The list contains labels in the following
     * order: {xlabel, ylabel}
     */
    private List<String> binLabels;
    /**
     * the primary labelFont used for the bins.
     */
    private final Font labelFont
            = new Font(Font.SERIF, Font.BOLD | Font.ITALIC, 24);
    /**
     * The delay on the animation timer for the bins in ms.
     */
    private final int DELAY = 1;

    /**
     * The delay on the animation timer for snowflakes in ms.
     */
    private final int DELAY1 = 2;

    /**
     * Number of snowflakes
     */
    private final int NUM_SNOWFLAKES = 13;

    /**
     * the primary animation box for filling the X jug
     */
    private final Rectangle2D.Double xBox
            = new Rectangle2D.Double();
    /**
     * the primary animation box for filling the Y jug
     */
    private final Rectangle2D.Double yBox
            = new Rectangle2D.Double();

    /**
     * List of snowflake rectangles initial position
     */
    private List<Rectangle.Double> snowflakesInitialPos;

    /**
     * List of snowflake rectangles
     */
    private List<Rectangle.Double> snowflakesPos;

    /**
     * List of snowflake velocities
     */
    private List<Point2D.Double> sfv;

    /**
     * texturePaint to store the snowflake image
     */
    private TexturePaint tpSnow;

    /**
     * snowflake image
     */
    private BufferedImage snowImg;

    /**
     * Candycane image
     */
    private BufferedImage candyCane;

    /**
     * Wreath image
     */
    private BufferedImage wreath;

    /**
     * A private timer to initialize animation of x and yBoxes
     */
    private final Timer t = new Timer(DELAY,
            new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent event)
                {
                    double yDifference = updateYBox();
                    double xDifference = updateXBox();
                    repaint();
                    // working with doubles, so there will be an interval of error.
                    // let that be 1 pixel.
                    if (Math.abs(yDifference) <= 1 && Math.abs(xDifference) <= 1)
                    {
                        t.stop();
                    } // end if
                } // end actionPerformed
            } // end actionListener
    ); // end timer

    /**
     * A private timer to animate the snowflakes
     */
    private final Timer t1 = new Timer(DELAY1,
            new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent event)
                {
                    updateSnowFlakes();
                    repaint();
                } // end action listener
                // working with doubles, so there will be an interval of error.
                // let that be 1 pixel.
            }
    );// end timer
} // end WaterJugCanvas
