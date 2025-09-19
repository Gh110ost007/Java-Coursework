/*
The challenge in this exercise was to establish equal probabilities for each direction and to incorporate a 1-in-8 chance for the robot to randomly change direction. To avoid collisions, the controlRobot method checks for obstacles in each direction, changing the robot’s heading if a wall is detected. The countWalls method assesses surrounding spaces to determine whether the robot is at a dead-end, in a corridor, or at a junction.The controller uses robot.look(direction) to detect if there is a wall in the chosen direction. If look returns IRobot.WALL, the robot chooses another open direction. If robot.look(direction) confirms an open path, the robot will proceed in that direction, using robot.face(direction) to face towards the chosen heading. On the first run, direction is set based on a random choice from 0 to 3(Between ahead and behind). This random selection ensures each direction is equally likely at the start. 
Initially, the robot moved with uneven probabilities, as failing to find an obstacle-free path could lead to biased direction selections. The solution was to adjust the Math.random() function from Exercise 1, refining the probability by ensuring a balanced probability across all possible directions Additionally, we implemented a 1-in-8 random movement chance by introducing a probability check, allowing the robot to occasionally pick a direction independently of its path logic. This was done by introducing a new random variable which would give us a number between 0 and 7 and a Boolean check to see if the random number Is equal to ‘1’ (a 1-in-8 chance) 
*/

import uk.ac.warwick.dcs.maze.logic.IRobot;

public class Ex2
{

	public void controlRobot(IRobot robot) {

		int randno;
		int direction = IRobot.AHEAD;    //Direction initialized to ahead as default starting point
		int nonWalls = countWalls(robot); //counts the number of non wall spaces around the robot
		String facing = "";
		boolean choice = Math.floor(Math.random()*8) == 1; // Randomly decide whether to make a direction choice or not (1 in 8 chance) 

		if (choice || robot.look(IRobot.AHEAD) == IRobot.WALL){ // Check if a random choice was made or if there's a wall ahead 
			do{

			// Select a random number between 0 and 3 to determine direction 

			randno = (int) Math.floor(Math.random()*4);

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