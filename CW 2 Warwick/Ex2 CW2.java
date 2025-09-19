/**
 * Exercise 2 Preamble:
 * This implementation saves space by using a stack-based backtracking system.
 * The stack only stores headings when the robot encounters a junction or crossroads.
 * This minimizes memory usage compared to storing full coordinate data.
 * Additionally, the robot dynamically clears the stack when backtracking completes,
 * further reducing space requirements.
 */ 
import uk.ac.warwick.dcs.maze.logic.IRobot;
import java.util.Stack;

/**
 * The Ex2 class manages the robot's movement through a maze using exploration
 * and backtracking strategies with an emphasis on Stack-based backtracking.
 */
public class Ex2 {
    private int pollRun = 0; // Tracks controlRobot calls
    private boolean startingSquareHandled = false; // Tracks if the start square logic is complete
    private int explorerMode = 1; // 1 = explore, 0 = backtrack
    private Stack<Integer> backtrackStack = new Stack<>(); // Stack for storing backtrack headings

    /**
     * Main robot control method called on each polling cycle.
     * Manages initialization, exploration, and backtracking.
     */
    public void controlRobot(IRobot robot) {
        // Initialize only on the first run
        if (robot.getRuns() == 0 && pollRun == 0) {
            explorerMode = 1; // Start in explore mode
        }

        // Handle the starting square to avoid collisions
        if (!startingSquareHandled) {
            int direction = handleStartSquare(robot); // Ensure valid start
            robot.face(direction);
            startingSquareHandled = true;
            return;
        }

        if (explorerMode == 1) {
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
        pollRun = 0; // Reset poll counter
        startingSquareHandled = false; // Reset starting square handling
        explorerMode = 1; // Reset to explorer mode
        backtrackStack.clear(); // Clear stack for fresh exploration
    }

    /**
     * Handles exploration when the robot is not backtracking.
     * Chooses direction based on available exits.
     */
    private void exploreControl(IRobot robot) {
        int exits = nonwallExits(robot);
        int direction;

        if (exits == 1) {
            direction = deadEnd(robot); // Handle dead ends
            explorerMode = 0; // Switch to backtrack mode
        } else if (exits == 2) {
            direction = corridor(robot); // Handle corridors
        } else {
            direction = junctionOrCrossroads(robot, exits); // Handle junctions
        }

        robot.face(direction); // Set robot's direction
    }

    /**
     * Backtracks through previously stored headings when exploration is complete.
     */
    private void backtrackControl(IRobot robot) {
        if (!backtrackStack.isEmpty()) {
            int direction = backtrackStack.pop(); // Retrieve last heading

            // Check if the direction is valid before making the move
            if (direction == IRobot.AHEAD || direction == IRobot.BEHIND ||
                direction == IRobot.LEFT || direction == IRobot.RIGHT) {

                if (robot.look(direction) != IRobot.WALL) {
                    robot.face(direction); // Continue backtracking
                } else {
                    explorerMode = 1; // Backtrack failed, resume exploration
                }
            } else {
                explorerMode = 1; // Invalid direction, resume exploration
            }
        } else {
            explorerMode = 1; // No backtrack data, return to exploration
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
        return IRobot.BEHIND; // Turn around at a dead end
    }

    /**
     * Chooses a direction in a corridor based on available passages.
     */
    private int corridor(IRobot robot) {
        int[] directions = {IRobot.AHEAD, IRobot.RIGHT, IRobot.LEFT};
        for (int direction : directions) {
            if (robot.look(direction) == IRobot.PASSAGE) {
                return direction; // Prioritize unexplored passages
            }
        }
        for (int direction : directions) {
            if (robot.look(direction) != IRobot.WALL) {
                return direction; // Choose any valid non-wall path
            }
        }
        return IRobot.BEHIND; // Default to turning around
    }

    /**
     * Manages junction and crossroads navigation, recording unexplored paths.
     */
    private int junctionOrCrossroads(IRobot robot, int exits) {
        int[] directions = {IRobot.AHEAD, IRobot.RIGHT, IRobot.LEFT, IRobot.BEHIND};

        // Record the current heading for backtracking
        backtrackStack.push(robot.getHeading());

        for (int direction : directions) {
            if (robot.look(direction) == IRobot.PASSAGE) {
                return direction; // Choose unexplored path
            }
        }
        return chooseRandomDirection(robot, directions, IRobot.WALL); // Random path if no unexplored exits
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
}
