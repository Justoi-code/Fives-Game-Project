# Fives-Game-Project
Hello, this is my contribution to an end of 1st semester group project I made in Parkway West High School in my Advanced Software Development Class.
I was in charge of the game itself, while my other groupmates were in charge of menus and statistics (shoutout to Jason and Quinn!). That final version (with the menus and statistics) is not going to be made available on this repo.

**SPECIAL NOTE: This project was made in the BlueJ IDE for Java so if you intend to try it yourself I recommend following the instructions for installation below. I will also include code.txt that contains all the code in text form if you just want to look at that. Lastly, please be mindful that there is a lot of garbage code I just commented out; they serve no importance to the game in its current state**

INSTRUCTIONS FOR INSTALLATION:
--------------------------------------------------------------------------------------------------------------------------------------------
1. Install BlueJ for your machine from this link: https://bluej.org/
2. Download the files from master and store the files (README, code.txt, and applet png not necessary) into a folder.
4. Open BlueJ and open project (Ctrl + O) and open the folder you created with files in Step 2.
5. Right click the module that appears that says "Game" and run "void main(String[] args)".
6. Enjoy!
--------------------------------------------------------------------------------------------------------------------------------------------

RULES/CONTROLS
--------------------------------------------------------------------------------------------------------------------------------------------
General Idea: If you've played the popular webgame 2048 or the less popular but personal favorite webgame Threes, it's basically a recreation of that genre of game (Much more closely related to Threes).

Arrow Key Control - UP, DOWN, LEFT, and RIGHT arrow keys control the movement of the cards displayed on the screen (see the social preview for a quick peek at what the applet looks like). For example, pressing the left arrow key 

Objective - Match "Two" cards and "Three" cards together to make a "Five" card. From there, continue to match cards of the similar value to make bigger and bigger cards (example: you would then try to merge a "Five" and another "Five" card to make a "Ten" card and so on). Getting to higher cards means your board will continue to get more cluttered. When you run out of spaces on your board, you lose! 

P.S. Hypothetically if you reach a certain amount of digits, the values won't fit the cards anymore. I don't anticipate anyone will reach values that big, but if you see this bug kudos to you! You reached a really high number!
--------------------------------------------------------------------------------------------------------------------------------------------

My most prominent takeaways from this projects are the following:
- Using JFrame to make graphics (making the cards, boards, etc. from scratch)
- Bounds/collision detection between cards and walls
- Using threads to create animation (movement of cards)
