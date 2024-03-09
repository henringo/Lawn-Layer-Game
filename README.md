Game Name: Lawn Layer

This is the game based on the Pacxon Game (https://www.pacxon.net/), created by: Henry Ngo, SID:510064233

How to Start:
  + Firstly, to run this game, go into the project and simply run "gradle clean build run"
  + The game will pop up and you will use the arrow keys to control the ball. Good Luck!

The game rule is as follows:
  + You are the Ball, spawned at the top left corner. The enemies are the worms and lady bugs, if they either touch you or the path you created, you are dead and got respawned with a 
    life deducted. For the lady bugs, it can destroy whichever grass blocks it touches.
  + You can move and control the ball by pressing arrow keys. In the concrete/grey block, you can only move one block at a time. However, in dirt/brown box, the ball will continue
    moving in the direction of the pressed key, until it stops when meets the concrete/grey block.
  + The goal of the game is to fill the grass by 80% of the area. Then it will move you to the next level. If there is no more levels left, the game is won.
  + To grass is filled when start a path on the dirt from a concrete box/grass box and enter another concrete box/grass box.
  + There are 2 power-ups, represented by a clock icon and a lightning icon.
    - The lightning icon: Increase your speed for a timed period.
    - The clock icon: Stop the enemies for a timed period.

Features and Knowledges used: 
  + Flood Fill Algorithm: to fill the grass.
  + BFS: to find the wanted node in the path.
  + OOP Design: this code base follows OOP Principles and Designs.
  + Use Processing Library and Gradle.