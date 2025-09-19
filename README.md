# Maze-Solving Robot Controllers (Java, CS118 Coursework)

This repository contains my solutions for **Coursework 1 (CW1)** and **Coursework 2 (CW2)** from the _Programming for Computer Scientists_ module (CS118, Warwick, 2024/25).

## Overview

The coursework involved designing **robot controllers in Java** to navigate a maze simulation environment.  
Across CW1 and CW2, the controllers progressed from simple randomised movement to **memory-driven and search-based strategies**.

- **Coursework 1 – Simple Robots**

  - _Exercise 1_: Implemented a randomised movement strategy with collision avoidance.
  - _Exercise 2_: Enhanced with directional bias and probability-based decision-making.
  - _Exercise 3_: Built a homing robot using heading control logic to detect and move towards the target.

- **Coursework 2 – Smarter Robots**
  - _Exercise 1_: Introduced memory to store and reuse past path decisions for efficient navigation.
  - _Exercise 2_: Explored worst-case analysis and probability refinements.
  - _Exercise 3_: Applied **Depth-First Search (DFS)** for systematic pathfinding.
  - _Grand Finale_: Integrated memory and search strategies into a final optimised controller for complex mazes.

## Technologies & Concepts

- **Language:** Java
- **Paradigms:** Object-Oriented Programming (OOP), modular design
- **Algorithms:** Randomised movement, probability bias, Depth-First Search (DFS), memory-based exploration
- **Software Engineering:** Modularisation, documentation, testing with `ControlTest`

## Running the Code

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/CS118-Maze-Robots.git
   cd CS118-Maze-Robots
   ```

2. Compile the Java files:

   ```bash
   javac *.java
   ```

3. Run the control test:
   ```bash
   java ControlTest
   ```

## 📁 Project Structure

```
Java coursework/
├── CW1 Warwick/          # Coursework 1 - Simple Robots
│   ├── Ex1 (CW1).java    # Randomised movement with collision avoidance
│   ├── Ex2 (CW1).java    # Directional bias and probability-based decisions
│   ├── Ex3 (CW1).java    # Homing robot with heading control
│   └── 5661937 (CW1).txt # Submission details
└── CW2 Warwick/          # Coursework 2 - Smarter Robots
    ├── Ex1 CW2.java      # Memory-based navigation
    ├── Ex2 CW2.java      # Worst-case analysis and probability refinements
    ├── Ex3 CW2.java      # Depth-First Search implementation
    ├── GrandFinale CW2.java # Integrated memory and search strategies
    └── 5661937 (CW2).txt # Submission details
```

## Key Features

- **Modular Design**: Each exercise builds upon previous concepts
- **Algorithm Progression**: From random movement to sophisticated search algorithms
- **Memory Management**: Efficient storage and retrieval of path information
- **Search Strategies**: Implementation of DFS for systematic exploration
- **Integration**: Final controller combines multiple approaches for optimal performance

## Notes

- All code follows Java best practices and OOP principles
- Extensive documentation and comments for clarity
- Tested with the provided `ControlTest` framework
- Designed for the CS118 maze simulation environment
