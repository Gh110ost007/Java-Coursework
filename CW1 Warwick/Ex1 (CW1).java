/*
Preamble : 
This program controls a robot navigating a maze, ensuring that it only moves randomly in directions to avoid collisions. The controlRobot method chooses a random direction until a path without a wall is found. To detect the maze type (e.g., dead-end, corridor, junction), the program uses the countWalls method, which determines the number of non-wall directions around the controller.
To avoid collisions, the program uses the look method, which returns IRobot.WALL if the direction of the controller is leading it to a wall. By using a do-while loop, the program selects a random direction repeatedly until look returns a direction with no wall.
The countWalls method checks all possible directions and counts non-wall spaces. With one non-wall, the robot is at a dead-end; two non-walls is a corridor, and three or more is a junction. The non-wall count (nonWalls) is initialized to 0 and declared within the countWalls method, as it needs to be used only in this method, making it a local variable.
A for loop checks the surroundings of the controller to count the non-wall spaces available.
*/

import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Ex1
{

	public void controlRobot(IRobot robot) {

		int randno;
		int direction;
		int nonWalls = countWalls(robot); //counts the number of non wall spaces around the robot
		String facing = "";


		do{

		// Select a random number

		randno = (int) Math.round(Math.random()*3);

		// Convert this to a direction

		if (randno == 0){
			direction = IRobot.AHEAD;
			facing = "forwards";
		}
		else if (randno == 1){
			direction = IRobot.RIGHT;
			facing = "right";
		}
		else if (randno == 2){
			direction = IRobot.LEFT;
			facing = "left";
		}
		else{
			direction = IRobot.BEHIND;
			facing = "backwards";
		}

		}while(robot.look(direction) == IRobot.WALL);{
			robot.face(direction);
			
			//Determine the type of the current location

			if (nonWalls == 1){
				System.out.println("I'm going "+ facing + " at a deadend" );
			}
			else if (nonWalls == 2){
				System.out.println("I'm going "+ facing + " down a corridor" );
			} 
			else if(nonWalls >=3){
				System.out.println("I'm going "+ facing + " at a junction" );
			}
		}
		
		

		
	}
    
	// Method to the count the number of empty spaces around the controller

	private int countWalls(IRobot robot){
		int nonWalls=0;

		//for loop initialised at Ahead(0) and increments 
		//the value of IRobot.direction untill it reaches Behind
		//and all spaces around the controller are checked and
		//non-wall spaces are counted

		for(int direction=IRobot.AHEAD;direction<=IRobot.BEHIND;direction++){
			if (robot.look(direction) != IRobot.WALL){
				nonWalls++;
			}
		}

		return nonWalls;  //returns the number of emoty spaces around controller
	}

}