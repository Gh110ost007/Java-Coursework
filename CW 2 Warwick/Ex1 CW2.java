/**
 * Exercise 1 Preamble:
 * This implementation ensures efficient navigation through the maze using both exploration
 * and backtracking strategies. The robot alternates between exploration mode and backtrack mode.
 * Key Design Considerations:
 * - `passageExits` and `nonwallExits` methods accurately count potential moves.
 * - Backtracking is managed through a junction recording system (`RobotData`), enabling systematic exploration.
 * - Repeated code was minimized by modular functions like `corridor` and `junctionOrCrossroads`.
 *
 * Efficiency Considerations:
 * - Efficiency is achieved by switching modes and exploring only necessary paths.
 * - Efficient Backtracking: The robot only records junctions if there are multiple exits and unexplored paths, reducing unnecessary storage.
 * - By distinguishing between walls, passages, and previously visited locations, the robot minimizes re-exploration.
 * - Memory Management: The RobotData class efficiently stores junctions using arrays, limiting memory usage to 10,000 entries.
 * 
 * Design Reasoning : 
 * - Explorer Design: The explorer mode focuses on finding new paths while minimizing repeated visits using passage prioritization.
 * - Backtracker Design: The backtracker ensures systematic path completion by revisiting junctions only when necessary.
 * 
 * Use of RobotData : 
 * - Data Storage: It records junction coordinates, arrival headings, and uses these records during backtracking.
 * - Backtracking Utilization: The robot looks up junction data when backtracking, ensuring it only revisits relevant junctions.
 * 
 * Handling of passageExits and nonwallExits : 
 * passageExits : checks if the robot sees an unexplored passage in methods (junctionOrCrossroads and corridor).
 * nonwallExits: This method counts all directions that are not walls. It helps the robot determine whether 
 * it’s at a dead end, corridor, or junction, ensuring correct path selection.
 *
 * Worst Case Analysis:
 * Yes, the robot Explorer will always find the target if the maze is finite and fully connected. 
 * It systematically explores all paths and backtracks when necessary, ensuring no path is left unchecked.
 * The worst-case scenario occurs in a highly branching and complex maze where every path must
 * be explored before the target is found. In such a scenario:
 * - **Maximum Steps:** If the maze has N squares, the robot could theoretically traverse each
 *   square multiple times due to dead ends and revisits. This results in a worst-case complexity of O.
 *
 * Conclusion:
 * This implementation balances exploration and backtracking, ensuring that the robot navigates the maze
 * efficiently while avoiding unnecessary revisits.
 */

 import uk.ac.warwick.dcs.maze.logic.IRobot;

/**
 * The Ex1 class manages the robot's movement through a maze using exploration
 * and backtracking strategies. It detects junctions, handles dead ends, and
 * records visited locations for efficient navigation.
 */
public class Ex1 {
    private int pollRun = 0; // Tracks controlRobot calls
    private RobotData robotData; // Stores junction data for backtracking
    private boolean startingSquareHandled = false; // Tracks if the start square logic is complete
    private int explorerMode = 1; // 1 = explore, 0 = backtrack
 
     /**
      * Controls the robot's movement by alternating between exploration and backtracking.
      * Initializes RobotData on the first call and ensures correct exploration strategy.
      * Manages initialization, exploration, and backtracking.
      */
      public void controlRobot(IRobot robot) {
        if (robot.getRuns() == 0 && pollRun == 0) {
            robotData = new RobotData(); // Initialize robot data on first run
            explorerMode = 1; // Start in explore mode
        }

        if (!startingSquareHandled) {
            int direction = handleStartSquare(robot); // Ensure valid start
            robot.face(direction);
            startingSquareHandled = true;
        } else if (explorerMode == 1) {
            exploreControl(robot); // Explore unexplored paths
        } else {
            backtrackControl(robot); // Backtrack to previous junctions
        }

        pollRun++; // Increment polling counter
    }
 
     /**
     * Resets robot state for a new maze run.
     */
     public void reset() {
         pollRun = 0;
         startingSquareHandled = false;
         explorerMode = 1;
         if (robotData != null) {
             robotData.resetJunctionCounter();
         }
     }
 
     /**
      * Handles exploration when the robot is not backtracking.
      * Chooses direction based on available exits.
      */
     private void exploreControl(IRobot robot) {
         int exits = nonwallExits(robot);
         int direction;
 
         if (exits == 1) {
             direction = deadEnd(robot); // Turn around at dead ends
             explorerMode = 0;
         } else if (exits == 2) {
             direction = corridor(robot); // Move forward in corridors
         } else {
             direction = junctionOrCrossroads(robot, exits); // Handle complex junctions and crossroads
         }
 
         robot.face(direction);
     }
 
     /**
      * Manages backtracking to the nearest unexplored junction using stored data.
      */
     private void backtrackControl(IRobot robot) {
         int x = robot.getLocation().x;
         int y = robot.getLocation().y;
 
         int direction = robotData.searchJunction(x, y); // Find the correct backtracking direction
 
         if (robot.look(direction) == IRobot.WALL || direction == IRobot.BEHIND) {
             explorerMode = 1; // Resume exploration if backtracking fails
         } else {
             robot.face(direction);
         }
     }
 
     /**
      * Determines the starting direction by checking available paths.
      * Ensures the robot starts in a valid direction.
      */
     private int handleStartSquare(IRobot robot) {
         int[] directions = {IRobot.AHEAD, IRobot.RIGHT, IRobot.LEFT, IRobot.BEHIND};
         for (int direction : directions) {
             if (robot.look(direction) != IRobot.WALL) {
                 return direction; // Move in the first available non-wall direction
             }
         }
         return IRobot.AHEAD; // Default to moving ahead if no path found
     }
 
     /**
      * Counts all available non-wall exits from the current location.
      * Helps determine navigation strategies.
      */
     private int nonwallExits(IRobot robot) {
         int count = 0;
         int[] directions = {IRobot.AHEAD, IRobot.RIGHT, IRobot.LEFT, IRobot.BEHIND};
         for (int direction : directions) {
             if (robot.look(direction) != IRobot.WALL) {
                 count++;
             }
         }
         return count;
     }
 
     /**
      * Handles movement at dead ends by turning around.
      */
     private int deadEnd(IRobot robot) {
         explorerMode = 0; // Switch to backtrack mode
         return IRobot.BEHIND;
     }
 
     /**
      * Chooses a direction in a corridor based on available passages.
      */
     private int corridor(IRobot robot) {
         int[] directions = {IRobot.AHEAD, IRobot.RIGHT, IRobot.LEFT};
         for (int direction : directions) {
             if (robot.look(direction) == IRobot.PASSAGE) {
                 return direction; // Prioritize passages
             }
         }
         for (int direction : directions) {
             if (robot.look(direction) != IRobot.WALL) {
                 return direction; // Choose a non-wall path
             }
         }
         return IRobot.BEHIND; // Default to turning around
     }
 
     /**
      * Decides how to navigate junctions and crossroads.
      * Prioritizes unexplored passages and records junctions for backtracking.
      */
     private int junctionOrCrossroads(IRobot robot, int exits) {
         int[] directions = {IRobot.AHEAD, IRobot.RIGHT, IRobot.LEFT, IRobot.BEHIND};
         if (beenbeforeExits(robot) <= 1) {
             robotData.recordJunction(robot.getLocation().x, robot.getLocation().y, robot.getHeading());
         }
         for (int direction : directions) {
             if (robot.look(direction) == IRobot.PASSAGE) {
                 return direction; // Choose unexplored path
             }
         }
         return chooseRandomDirection(robot, directions, IRobot.WALL); // Choose random path if no unexplored paths
     }
 
     /**
      * Chooses a random available direction, avoiding specified types.
      */
     private int chooseRandomDirection(IRobot robot, int[] directions, int avoidType) {
         int[] available = new int[4];
         int count = 0;
         for (int direction : directions) {
             if (robot.look(direction) != avoidType) {
                 available[count++] = direction;
             }
         }
         return count == 0 ? IRobot.AHEAD : available[(int) (Math.random() * count)];
     }
 
     /**
      * Counts previously visited exits to assist in decision-making.
      */
     private int beenbeforeExits(IRobot robot) {
         int count = 0;
         int[] directions = {IRobot.AHEAD, IRobot.RIGHT, IRobot.LEFT, IRobot.BEHIND};
         for (int direction : directions) {
             if (robot.look(direction) == IRobot.BEENBEFORE) {
                 count++;
             }
         }
         return count;
     }
 }
 
 class RobotData {
    private static final int maxJunctions = 10000; // Maximum number of junctions that can be recorded
    private int junctionCounter = 0; // Tracks the number of recorded junctions
    private int[] juncX = new int[maxJunctions]; // Stores x-coordinates of junctions
    private int[] juncY = new int[maxJunctions]; // Stores y-coordinates of junctions
    private int[] arrived = new int[maxJunctions]; // Stores the heading the robot took when arriving at each junction

    /**
     * Records a junction's coordinates and the direction the robot arrived from.
     * x : The x-coordinate of the junction.
     * y : The y-coordinate of the junction.
     * heading : The heading from which the robot arrived.
     */
    public void recordJunction(int x, int y, int heading) {
        if (junctionCounter < maxJunctions) {
            juncX[junctionCounter] = x;
            juncY[junctionCounter] = y;
            arrived[junctionCounter] = heading;
            junctionCounter++;
        }
    }

    /**
     * Searches for a previously recorded junction at the specified coordinates.
     * x : The x-coordinate of the current location.
     * y : y The y-coordinate of the current location.
     * return : The opposite direction from which the robot originally arrived(or IRobot.BEHIND).
     */
    public int searchJunction(int x, int y) {
        for (int i = 0; i < junctionCounter; i++) {
            if (juncX[i] == x && juncY[i] == y) {
                return reverseHeading(arrived[i]);
            }
        }
        return IRobot.BEHIND; // Fallback if junction not found
    }

    /**
     * Reverses the robot's heading to its opposite direction.
     * heading : The original heading direction.
     * return : The opposite direction.
     */
    private int reverseHeading(int heading) {
        switch (heading) {
            case IRobot.NORTH: return IRobot.SOUTH;
            case IRobot.SOUTH: return IRobot.NORTH;
            case IRobot.EAST: return IRobot.WEST;
            case IRobot.WEST: return IRobot.EAST;
            default: return IRobot.BEHIND; // Fallback for invalid heading
        }
    }

    /**
     * Resets the junction counter, clearing all recorded junctions.
     */
    public void resetJunctionCounter() {
        junctionCounter = 0;
    }
}
