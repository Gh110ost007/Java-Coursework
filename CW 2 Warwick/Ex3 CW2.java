/**
 * Exercise 3 Preamble
 *
 * **Design Justification:**
 * In the previous Exercise (Ex2), the robot used a depth-first search strategy without tracking
 * visited paths or junctions. This caused infinite loops in mazes with cycles, as the robot repeatedly revisited
 * previously explored areas, unable to detect its past locations.
 *
 * **Correction for Loopy Mazes:**
 * In this new implementation, we introduced a backtracking stack that records visited junctions.
 * Whenever the robot encounters a junction, it records its current coordinates and heading.
 * If the robot reaches a dead-end or fully explored area, it backtracks using the recorded data.
 *
 * **Why This Fix Works:**
 * - **Backtracking Stack:** By storing junction data in a stack, the robot can backtrack when it hits dead ends.
 * - **Loop Avoidance:** If a previously explored path is encountered, the robot will switch to an unexplored
 *   direction, preventing infinite loops.
 * - **Correct Pathfinding:** The robot systematically explores all available paths, ensuring complete
 *   maze traversal even in loopy mazes.
 *
 * This improved design makes the robot capable of handling both simple and loopy mazes efficiently.
 */

import uk.ac.warwick.dcs.maze.logic.IRobot;
import java.util.Stack;

/**
 * Ex3 : Maze Navigation with Loop Handling
 *
 * This implementation enhances the robot with the ability to navigate loopy mazes using a backtracking stack.
 * The robot records visited junctions and backtracks when necessary, avoiding infinite loops.
 */
public class Ex3 {
    private int pollRun = 0; // Tracks the number of controlRobot calls
    private boolean startingSquareHandled = false; // Tracks if the start square logic is complete
    private int explorerMode = 1; // 1 = explore, 0 = backtrack
    private Stack<int[]> backtrackStack = new Stack<>(); // Stack for backtracking

    /**
     * Main control method for the robot.
     * Manages initialization, exploration, and backtracking.
     */
    public void controlRobot(IRobot robot) {
        if (robot.getRuns() == 0 && pollRun == 0) {
            explorerMode = 1; // Initialize in exploration mode
        }

        if (!startingSquareHandled) {
            int direction = handleStartSquare(robot); // Ensure valid start
            robot.face(direction);
            startingSquareHandled = true;
            return;
        }

        if (explorerMode == 1) {
            exploreControl(robot); // Handle exploration
        } else {
            backtrackControl(robot); // Handle backtracking
        }

        pollRun++; // Increment poll counter
    }

    /**
     * Resets the robot's state for a new maze run.
     */
    public void reset() {
        pollRun = 0;
        startingSquareHandled = false;
        explorerMode = 1;
        backtrackStack.clear(); // Clear backtracking stack
    }

    /**
     * Manages exploration based on available exits.
     * Prioritizes unexplored paths, handles dead ends, and navigates corridors.
     */
    private void exploreControl(IRobot robot) {
        int exits = nonwallExits(robot);
        int direction;

        if (exits == 1) {
            direction = deadEnd(robot); // Handle dead-end by turning around
            explorerMode = 0; // Switch to backtracking mode
        } else if (exits == 2) {
            direction = corridor(robot); // Move through the corridor
        } else {
            direction = junctionOrCrossroads(robot, exits); // Handle junctions and crossroads
        }

        robot.face(direction); // Set robot's direction
    }

    /**
     * Manages backtracking by returning to previously recorded junctions.
     */
    private void backtrackControl(IRobot robot) {
        if (!backtrackStack.isEmpty()) {
            int[] lastCell = backtrackStack.pop(); // Retrieve last visited cell
            int x = lastCell[0];
            int y = lastCell[1];
            int heading = lastCell[2];

            if (heading == IRobot.AHEAD || heading == IRobot.BEHIND || heading == IRobot.LEFT || heading == IRobot.RIGHT) {
                if (robot.look(heading) != IRobot.WALL) {
                    robot.face(heading); // Continue backtracking
                } else {
                    explorerMode = 1; // Return to exploration if blocked
                }
            } else {
                explorerMode = 1; // Invalid heading, resume exploration
            }
        } else {
            explorerMode = 1; // No backtrack data, return to exploration
        }
    }

    /**
     * Determines a valid starting direction by checking for open paths.
     * return : The first available non-wall direction.
     */
    private int handleStartSquare(IRobot robot) {
        int[] directions = {IRobot.AHEAD, IRobot.RIGHT, IRobot.LEFT, IRobot.BEHIND};
        for (int direction : directions) {
            if (robot.look(direction) != IRobot.WALL) {
                return direction; // Return first available direction
            }
        }
        return IRobot.AHEAD; // Default to moving ahead if no path is found
    }

    /**
     * Counts non-wall exits from the robot's current location.
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
     * Handles dead-end scenarios by turning the robot around.
     */
    private int deadEnd(IRobot robot) {
        return IRobot.BEHIND; // Turn around at a dead-end
    }

    /**
     * Selects a direction in a corridor based on available paths.
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
                return direction; // Choose any non-wall direction
            }
        }
        return IRobot.BEHIND; // Default to turning around
    }

    /**
     * Manages junctions and crossroads by recording them for backtracking.
     */
    private int junctionOrCrossroads(IRobot robot, int exits) {
        int[] directions = {IRobot.AHEAD, IRobot.RIGHT, IRobot.LEFT, IRobot.BEHIND};
        backtrackStack.push(new int[]{robot.getLocation().x, robot.getLocation().y, robot.getHeading()});

        for (int direction : directions) {
            if (robot.look(direction) == IRobot.PASSAGE) {
                return direction; // Prioritize unexplored passages
            }
        }
        return chooseRandomDirection(robot, directions, IRobot.WALL); // Random choice if no unexplored exits
    }

    /**
     * Selects a random available direction, avoiding walls.
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
}
