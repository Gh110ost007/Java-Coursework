/*
This exercise involved designing a heading controller for the robot to ensure it navigates toward the target  while avoiding obstacles. The controller first determines the relative direction of the target using `isTargetNorth`  and `isTargetEast` methods. These methods identify if the target is north, south, east, or west of the robot.  Based on this, the `headingController` checks if the robot can move in either of the target's directions *(e.g., NORTH or EAST) without hitting a wall. To ensure collision-free movement, the controller prioritizes available directions that bring the robot closer  to the target. 
If both preferred directions are free, a random choice between them introduces slight variation in movement.  If neither is free, the robot selects a random heading that isn’t blocked. This approach ensures that the robot  moves toward the target when possible.  The homing robot can generally be expected to find the target as long as there is a viable path.  However, there is a chance that random direction choices may cause it to deviate temporarily. Improvements could include adding path memory so that the controller avoids that path the next time.
*/

import uk.ac.warwick.dcs.maze.logic.IRobot; 

public class Ex3 { 


    public void controlRobot(IRobot robot){
        // Get heading direction based on target location
        int heading = headingController(robot);
        ControlTest.test(heading,robot);
        robot.setHeading(heading);
     } 
 

    // Determines if the target is north of the robot
    private byte isTargetNorth(IRobot robot) {  
        int robotY = robot.getLocation().y; // Get robot's Y coordinate
        int targetY = robot.getTargetLocation().y; // Get target's Y coordinate
 
        // If the robot's Y coordinate is greater, the target is north
        if (robotY > targetY) {  
            return 1;  
        } else if (robotY < targetY) {  
            // If the robot's Y coordinate is less, the target is south
            return -1;  
        } else {  
            // If Y coordinates are equal, target is at the same latitude
            return 0;  
        }  
    }  
 
    // Determines if the target is east of the robot
    private byte isTargetEast(IRobot robot) {  
        int robotX = robot.getLocation().x; // Get robot's X coordinate
        int targetX = robot.getTargetLocation().x; // Get target's X coordinate
 
        // If the robot's X coordinate is less, the target is east
        if (robotX < targetX) {  
            return 1;  
        } else if (robotX > targetX) {  
            // If the robot's X coordinate is greater, the target is west
            return -1;  
        } else {  
            // If X coordinates are equal, target is at the same longitude
            return 0;  
        }  
    }  
 
    // Checks if the given heading has a wall in that direction
    private int lookHeading(IRobot robot, int absoluteDirection) { 
        int originalHeading = robot.getHeading(); // Store the original heading
        robot.setHeading(absoluteDirection); // Temporarily set the robot's heading to the specified direction
        int result = robot.look(IRobot.AHEAD); // Check if there's a wall in this direction
        robot.setHeading(originalHeading); // Reset to the original heading
        return result; 
    } 
 
       // Determines the best heading direction to move toward the target
    private int headingController(IRobot robot) { 
        byte targetNorth = isTargetNorth(robot); // Check if target is north
        byte targetEast = isTargetEast(robot); // Check if target is east
        int preferredHeading1 = 0; // First preferred heading direction
        int preferredHeading2 = 0; // Second preferred heading direction
 
        // Set the preferred headings based on the target's relative position
        if (targetNorth == 1) preferredHeading1 = IRobot.NORTH; 
        else if (targetNorth == -1) preferredHeading1 = IRobot.SOUTH; 
        if (targetEast == 1) preferredHeading2 = IRobot.EAST; 
        else if (targetEast == -1) preferredHeading2 = IRobot.WEST; 
 
        // Check if preferred headings are free of walls
        boolean heading1Free = lookHeading(robot, preferredHeading1) != IRobot.WALL; 
        boolean heading2Free = lookHeading(robot, preferredHeading2) != IRobot.WALL; 
 
        // If both preferred headings are free, randomly choose one
        if (heading1Free && heading2Free) { 
            return Math.random() > 0.5 ? preferredHeading1 : preferredHeading2; 
        } else if (heading1Free) { 
            // If only the first preferred heading is free, choose it
            return preferredHeading1; 
        } else if (heading2Free) { 
            // If only the second preferred heading is free, choose it
            return preferredHeading2; 
        } else { 
            // If neither preferred heading is free, pick a random available direction
            return getRandomAvailableHeading(robot); 
        } 
    } 
 
    // Chooses a random heading if no preferred heading is available  
    private int getRandomAvailableHeading(IRobot robot) {  
        int randno;  
        int direction;  
 
        do {  
            // Generate a random number between 0 and 3
            randno = (int) Math.floor(Math.random() * 4);  
 
            // Convert the random number to a direction
            if (randno == 0) {  
                direction = IRobot.AHEAD;  
            } else if (randno == 1) {  
                direction = IRobot.RIGHT;  
            } else if (randno == 2) {  
                direction = IRobot.LEFT;  
            } else {  
                direction = IRobot.BEHIND;  
            }  
        } while (lookHeading(robot, direction) == IRobot.WALL); // Repeat until a wall-free direction is found
        return direction;  
    }  

    public void reset(){
        ControlTest.printResults();
    }

} 


 