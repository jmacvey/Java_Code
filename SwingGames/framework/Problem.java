package framework;

import graph.DequeAdder; // BFS/DFS class to help add to queues
import graph.Vertex; // abstract object representation on state space tree
import java.util.Comparator; // for priority queue comparisons
import java.util.HashMap;  // obsolete class, but suited purposes
import java.util.LinkedList; // because linked lists are easy
import java.util.List; 
import java.util.PriorityQueue; // necessary for A* algorithms.
import java.util.Stack; // to hold the found solution path

/**
 * This abstract class represents a problem in a problem solving domain.
 *
 * It provides getters and setters for the current state of the problem, the
 * list of moves for the problem, and the problem's introduction string.
 * Extending classes need not have an instance fields for these attributes, as
 * they are inherited from this class. Extending classes must set these values
 * in their constructors using the setters (mutators) provided in this class.
 * Extending classes must also override the abstract <b>success</b> method.
 */
public abstract class Problem
{

    /**
     * Checks to see if a vertex appears on the predecessor path of an ancestor
     * vertex.
     *
     * @param v the vertex to check
     * @param ancestor the ancestor of v
     * @return true if v equals ancestor or any ancestor of ancestor
     * @author Joshua MacVey
     */
    public boolean occursOnPath(Vertex v, Vertex ancestor)
    {
        // recursive algorithm:
        // base case 1
        if (ancestor == null)
        {
            return false;
        } // base case 2
        else if (v.equals(ancestor))
        {
            return true;
        } // recursive call
        else
        {
            return occursOnPath(v, ancestor.getPredecessor());
        }
    }

    /**
     * Expands a vertex v in a state space search tree by creating a list (its
     * children) of all vertices adjacent to it in the state space. The list may
     * not include any vertex on the predecessor path leading to v. Each child
     * on the list has its predecessor set to v and its distance from the root
     * (its depth in the search tree) set to one more than v's distance.
     *
     * @param v the vertex being expanded
     * @param checkOccursOnPath flag to ensure this expand checks if the vertex
     * already occurs on a path. This is optimized away in the enhanced A*, and
     * so is needed to differentiate between search algorithms.
     * @return a list of the children
     * @author jmacvey
     */
    public List<Vertex> expand(Vertex v, boolean checkOccursOnPath)
    {
        // create a list for the children
        LinkedList<Vertex> children = new LinkedList<>();
        // for each move, try to create a vertex.  If that vertex
        // exists and isn't already added to a path, go ahead and add it
        for (Move move : moves)
        {
            Vertex child = (Vertex) move.doMove((State) v);
            if (checkOccursOnPath
                    ? (child != null && !occursOnPath(child, v))
                    : (child != null))
            {
                child.setDistance(v.getDistance() + 1);
                child.setPredecessor(v);
                children.add(child);
            } // end if
        } // end for
        return children;
    }

    /**
     * BFS/DFS implementation
     * Searches for a possible solution to the problem given an initial state.
     *
     * @param state the starting point for the solution.
     * @param adder the dequeue adding object to facilitate
     * multi-implementation of both BFS and DFS
     * @return the solution found by the algorithm.
     * @author jmacvey
     */
    public Vertex search(Vertex state, DequeAdder adder)
    {
        state.setDistance(0);
        state.setPredecessor(null);
        deq.add(state);
        while (!deq.isEmpty())
        {
            State u = (State) deq.remove();
            queueOps++;
            queueSize--;
            this.setCurrentState(u);
            if (this.success())
            { // return the solution vertex if possible.
                Vertex ve = (Vertex) u;
                pushSolutions(ve);
                solutionLength = solutionStack.size();
                return (Vertex) u;
            } else
            {
                LinkedList<Vertex> children
                        = (LinkedList<Vertex>) this.expand((Vertex) u, true);
                for (Vertex vertex : children)
                {
                    adder.add(vertex, deq);
                    queueOps++;
                    queueSize++;
                    maxQueueSize = Math.max(queueSize, maxQueueSize);
                }
            }
        } // end while
        return null;     // returns null if no solution is found
    }

    /**
     * A* Search Implementation.
     *
     * @param state the starting point for the solution.
     * @return the solution vertex if it is found; null otherwise.
     * @author jmacvey
     */
    public Vertex searchAStar(Vertex state)
    {
        initializePQ();
        state.setDistance(0);
        state.setPredecessor(null);
        pq.add(state);

        queueSize++;
        maxQueueSize = 1;
        queueOps++;
        while (!pq.isEmpty())
        {
            State u = (State) pq.remove();
            queueSize--;
            queueOps++;
            this.setCurrentState(u);
            if (this.success())
            {
                Vertex ve = (Vertex) u;
                pushSolutions(ve);
                solutionLength = solutionStack.size();
                return ve;
            } else
            {
                for (Vertex ve : expand((Vertex) u, true))
                {
                    pq.add(ve);
                    queueSize++;
                    queueOps++;
                    maxQueueSize = Math.max(queueSize, maxQueueSize);
                }
            }
        }
        return null;
    }

    /**
     * Enhanced A* search Implementation.
     * Utilizes hash tables to implement optimizations in priority queue operations
     * as well as eliminating the need for path occurrence checking (O(n) to O(1)) 
     * efficiency boost.
     * @param state the start state in the search.
     * @return the solution vertex, if found. Null otherwise.
     * @author jmacvey
     */
    public Vertex enhancedAStarSearch(Vertex state)
    {
        initializePQ();
        initializeHashMaps();
        state.setDistance(0);
        state.setPredecessor(null);
        pq.add(state);
        queueOps++;
        queueSize++;
        maxQueueSize = 1;
        openHash.put((State) state, (State) state); // state is both value and key.
        closedHash.clear();
        while (!pq.isEmpty())
        {
            State u = (State) pq.remove();
            openHash.remove((State) u);
            queueOps++;
            queueSize--;
            // success story...
            this.setCurrentState(u);
            if (this.success())
            {
                pushSolutions((Vertex) u);
                solutionLength = solutionStack.size();
                return (Vertex) u;
            } else
            {
                for (Vertex ve : expand((Vertex) u, false))
                {
                    // case 1: vertex on the hash.
                    if (openHash.containsKey((State)ve)) // scenario 1
                    {
                        // hash might code to same value,need to ensure equals
                        Vertex x = (Vertex) openHash.get((State)ve);
                        if (((State) x).equals((State) ve))
                        {
                            if (ve.getDistance() < x.getDistance())
                            {
                                openRediscoveries++;
                                // x is on the PQ, so its already promoted.
                                x.setDistance(ve.getDistance());
                                x.setPredecessor(ve.getPredecessor());
                                // promotion is done by removing and reinserting
                                pq.remove(x);
                                pq.add(x);
                                queueOps += 2;
                            }
                        }
                    } else if (closedHash.containsKey((State)ve)) // scenario 2
                    {
                        Vertex x = (Vertex) closedHash.get((State)ve);
                        // might be false positive
                        if (((State) x).equals((State) ve))
                        {
                            closedRediscoveries++;
                         // case 1: distance now is less than distance before
                            if (ve.getDistance() < x.getDistance())
                            {
                                x.setDistance(ve.getDistance());
                                x.setPredecessor(ve.getPredecessor());
                                // readd x to the queue and openHash
                                pq.add(x);
                                queueOps++;
                                queueSize++;
                                maxQueueSize = Math.max(queueSize, maxQueueSize);
                                openHash.put((State)x, (State)x);
                                // remove from closed
                                closedHash.remove((State)x);
                            }
                        }
                    } else // not contained on either closed or open
                    {
                        pq.add(ve);
                        queueOps++;
                        queueSize++;
                        maxQueueSize = Math.max(queueSize, maxQueueSize);
                        openHash.put((State)ve, (State)ve);
                    }
                } // end for
                closedHash.put((State) u, (State) u);
            }
        }
        return null;
    }

    /**
     * initializes the priority queue.
     */
    private void initializePQ()
    {
        pq = new PriorityQueue<>(PEQ_DEFAULT_CAPACITY, new Comparator<Vertex>()
        {
            @Override
            public int compare(Vertex v1, Vertex v2)
            {
                int h1 = ((State) v1).getHeuristic(finalState);
                int d1 = v1.getDistance();
                int h2 = ((State) v2).getHeuristic(finalState);
                int d2 = v2.getDistance();
                return (h1 + d1) - (h2 + d2);
            }
        });
    }

    /**
     * initializes the hashMaps for enhanced A* search.
     */
    private void initializeHashMaps()
    {
        openHash = new HashMap<>();
        closedHash = new HashMap<>();
    }

    /**
     * Creates a solution stack for the solution back to the root of the state
     * space tree.
     *
     * @param solution the solution state.
     */
    private void pushSolutions(Vertex solution)
    {
        solutionStack.push(solution);
        // 2nd test ensures the first state is not pushed on the stack
        // first test guards against 2nd test nullException access.
        if (solution.getPredecessor() != null
                && solution.getPredecessor().getPredecessor() != null)
        {
            pushSolutions(solution.getPredecessor()); // recursive call
        }
    }

    /**
     * Determines whether the current state of this problem is a success.
     * Extending classes need to override this method.
     *
     * @return whether the current state is a success
     */
    public abstract boolean success();

    /**
     * Gets the current state of the problem.
     *
     * @return the current state
     */
    public State getCurrentState()
    {
        return currentState;
    }

    /**
     * Sets the current state of the problem.
     *
     * @param currentState the current state
     */
    public void setCurrentState(State currentState)
    {
        this.currentState = currentState;
    }

    /**
     * Gets an explanatory introduction string for the problem.
     *
     * @return the introduction string
     */
    public String getIntroduction()
    {
        return introduction;
    }

    /**
     * Gets the maximum queue size after execution of search algorithm.
     *
     * @return the maximum queue size.
     */
    public int getMaxQueueSize()
    {
        return maxQueueSize;
    }

    /**
     * Gets the number of queue operations required to execute the search
     * algorithm.
     *
     * @return the number of queue operations.
     */
    public int getQueueOps()
    {
        return queueOps;
    }

    /**
     * Gets the length of the solution found post the search algorithm.
     *
     * @return the length of the solution found.
     */
    public int getSolutionLength()
    {
        return solutionLength;
    }
    
    /**
     * Gets the open rediscovery numbers found post search algorithm.
     * The number of open rediscoveries.
     */
    public int getOR()
    {
        return openRediscoveries;
    }
    
    /**
     * Gets the closed rediscovery numbers found post search algorithm.
     * @return the number of closed rediscoveries.
     */
    public int getCR()
    {
        return closedRediscoveries;
    }

    /**
     * Resets the counters for the search algorithms.
     */
    public void resetCounters()
    {
        openRediscoveries = 0;
        closedRediscoveries = 0;
        solutionLength = 0;
        queueSize = 0;
        maxQueueSize = 0;
        queueOps = 0;
        solutionStack.clear();
        deq.clear();
    }

    /**
     * Sets the introduction string for this problem.
     *
     * @param introduction the introduction string
     */
    public void setIntroduction(String introduction)
    {
        this.introduction = introduction;
    }

    /**
     * Returns the next move in the problem for algorithmic search at user's
     * request.
     *
     * @return the size of the solution stack.
     */
    public boolean solnSetEmpty()
    {
        return solutionStack.isEmpty();
    }

    /**
     * Gets the next state in the solution path at the user's request.
     *
     * @return the next state in the solution path
     */
    public Vertex getNextMove()
    {
        if (solutionStack != null)
        {
            return solutionStack.pop();
        } else
        {
            return null;
        }
    }

    /**
     * Gets the list of moves for this problem.
     *
     * @return the list of moves
     */
    public List<Move> getMoves()
    {
        return moves;
    }

    /**
     * Sets the list of moves for this problem.
     *
     * @param moves the list of moves
     */
    public void setMoves(List<Move> moves)
    {
        this.moves = moves;
    }

    /**
     * Sets the final state for this problem.
     *
     * @param finalState the final state of the problem.
     */
    public void setFinalState(State finalState)
    {
        this.finalState = finalState;
    }

    /**
     * Gets the final state for this problem.
     *
     * @return the final state for the problem.
     */
    public State getFinalState()
    {
        return this.finalState;
    }

    /**
     * Gets the initialStatesList for this problem.
     *
     * @return the initial states list.
     */
    public LinkedList<State> getInitialStatesList()
    {
        return initialStatesList;
    }

    /**
     * Sets the initialStatesList for this problem.
     *
     * @param initialStatesList the list of initial states.
     */
    public void setInitialStatesList(LinkedList<State> initialStatesList)
    {
        this.initialStatesList = initialStatesList;
    }

    /**
     * Gets the final StatesList for this problem.
     *
     * @return the final states list.
     */
    public LinkedList<State> getFinalStatesList()
    {
        return finalStatesList;
    }

    /**
     * Sets the finalStatesList for this problem.
     *
     * @param finalStatesList the list of initial states.
     */
    public void setFinalStatesList(LinkedList<State> finalStatesList)
    {
        this.finalStatesList = finalStatesList;
    }

    /**
     * Getter for the initialCanvasList for this problem.
     *
     * @return the initial canvas list.
     */
    public LinkedList<Canvas> getInitialCanvasList()
    {
        return initialCanvasList;
    }

    /**
     * Setter for the initialCanvasList.
     *
     * @param initialCanvasList the initialCanvasList to be set.
     */
    public void setInitialCanvasList(LinkedList<Canvas> initialCanvasList)
    {
        this.initialCanvasList = initialCanvasList;
    }

    /**
     * Getter for the final canvas list.
     *
     * @return the final canvas list.
     */
    public LinkedList<Canvas> getFinalCanvasList()
    {
        return finalCanvasList;
    }

    /**
     * Setter for the final canvas List.
     *
     * @param finalCanvasList the finalCanvasList to be set.
     */
    public void setFinalCanvasList(LinkedList<Canvas> finalCanvasList)
    {
        this.finalCanvasList = finalCanvasList;
    }

    /**
     * Getter for the moves list associated with each initial/final state.
     *
     * @return the moves count.
     */
    public LinkedList<Integer> getMoveCounts()
    {
        return this.moveCounts;
    }

    /**
     * Setter for the moves list associated with each initial/final State.
     *
     * @param moveCounts the list of move counts.
     */
    public void setMoveCounts(LinkedList<Integer> moveCounts)
    {
        this.moveCounts = moveCounts;
    }

    /**
     * Getter for isMultiProblem flag.
     *
     * @return true is the problem has multiple starting positions. false
     * otherwise.
     */
    public boolean isMultiProblem()
    {
        return this.isMultiProblem;
    }

    /**
     * Setter for isMultiProblem flag. Inheriting classes of single initial and
     * final state must set this flag to false to avoid null pointer exception
     * errors on reset button.
     *
     * @param isMultiProblem flag declaring problem to be multiProblem
     */
    public void setMultiProblem(boolean isMultiProblem)
    {
        this.isMultiProblem = isMultiProblem;
    }

    /**
     * Getter for hasCanvas Flag.
     *
     * @return true if problem has a canvas. False otherwise.
     */
    public boolean hasCanvas()
    {
        return this.hasCanvas;
    }

    /**
     * Setter for hasCanvas flag (true by default). This flag should be set to
     * false if the problem does not have a canvas graphical representation.
     *
     * @param hasCanvas true if the problem has canvas graphics; false
     * otherwise.
     */
    public void setHasCanvas(boolean hasCanvas)
    {
        this.hasCanvas = hasCanvas;
    }

    /**
     * The current state of this problem
     */
    private State currentState;

    /**
     * The explanatory string for this problem.
     */
    private String introduction;

    /**
     * The list of moves for this problem.
     */
    private List<Move> moves;

    /**
     * a Dequeue interface for automated search algorithm.
     */
    private final LinkedList<Vertex> deq = new LinkedList<>();

    /**
     * a Priority queue interface for automated search algorithms A*.
     */
    private PriorityQueue<Vertex> pq;

    /**
     * a stack for user inquiry into solution state.
     */
    private final Stack<Vertex> solutionStack = new Stack<>();

    /**
     * # of queue operations required for solution path.
     */
    private int queueOps = 0;

    /**
     * # of items currently in the queue
     */
    private int queueSize = 0;

    /**
     * # of maximum items on the queue.
     */
    private int maxQueueSize = 0;
    
    /**
     * # of open rediscoveries
     */
    private int openRediscoveries;
    
    /**
     * # of closed rediscoveries
     */
    private int closedRediscoveries;

    /**
     * Default capacity for number of items in priority quque
     */
    private final int PEQ_DEFAULT_CAPACITY = 1;

    /**
     * length of the solution
     */
    private int solutionLength = 0;

    /**
     * Final state of the problem.
     */
    private State finalState;

    /**
     * InitialStatesList for user to optionally choose a different version of
     * the problem.
     */
    private LinkedList<State> initialStatesList = new LinkedList<>();

    /**
     * FinalStatesList corresponding to the initialStatesList
     */
    private LinkedList<State> finalStatesList = new LinkedList<>();

    /**
     * InitialCanvasList for the problemChooser class.
     */
    private LinkedList<Canvas> initialCanvasList = new LinkedList<>();

    /**
     * Final canvas list for the problemChooser class.
     */
    private LinkedList<Canvas> finalCanvasList = new LinkedList<>();

    /**
     * Move counts for the problemChooser class.
     */
    private LinkedList<Integer> moveCounts = new LinkedList<>();

    /**
     * Hashtable containing open states on the priority queue.
     */
    private HashMap<State, State> openHash;

    /**
     * Hashtable containing closed states on the priority queue.
     */
    private HashMap<State, State> closedHash;

    /**
     * Flag to see if this problem has multiple starting points.
     */
    private boolean isMultiProblem = true;

    /**
     * Flag to see if this problem has a canvas available.
     */
    private boolean hasCanvas = true;
}
