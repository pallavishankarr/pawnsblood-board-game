# CS3500Assignment5

## Overview

Pawns Board is a two-player strategy game inspired by Queen’s Blood. The game is played on a rectangular board with an odd number of columns, where players take turns placing cards that influence the board based on a grid system. Players compete for control of rows using pawns and cards, and the game ends when both players pass consecutively. The player with the highest row-based score wins.

## Key Information

- The board has a fixed size with positive rows and an odd number of columns greater than 1.

- Players have a limited number of pawns and must manage them strategically.

- Cards have placement costs and apply influence to the board.

- The game model enforces rules, turn-taking, and legal moves.

- A file configuration of the cards must be passed in to start the game. 


## How to Play
1. make a new game object
2. call startGame method on game object and input file path and handsize
3. start a players turn so that a card can be drawn into their hand
   1. card will not be drawn if adding the card will make the handsize 1/3 of the handsize
4. make a move
   1. place a card on a position on the board if there are enough pawns and player owns those pawns
   2. pass a turn (the card drawn is still in the hand)
5. Alternate turns until there are no more pawns on the board 
6. A player wins on row basis, meaning that the player earns points for controlling rows. The player with the highest total wins the game.

## Key Components
### Game Model (PawnsBoardGame)
- controls the game state, turn-taking, and win conditions
- enforces rules, legal moves, and pawn/card interaction
- we chose that the game model stores the decks, hands, turns, 
 the board, move validation, and altering the board as the model
is supposed to store the state of the program, logic, and ensuring
that the state is correct.

### Player (Player)
- enum representation of RED and BLUE players

### Pawn Card (PawnCard)
- each card has a value, cost, and (5x5) influence grid
- uses 5x5 grid to apply influence in different ways

### Pawn Cell (PawnCell)
- represents a board cell 
- can contain pawns or a card

### Influence Effect
- determines how a card influences the board upon placement
- applies modifications to surrounding pawns or cells
- we decided to create a separate class for this so that adding
different influences in future assignments is easier 

### Card Initializer
- loads card data into a list when given file
- the game model uses the card initializer in startGame to 
configure the deck since the deck is only constructed
when the game is started

### Textual View
- provides text based representation of the board
- displays pawn with integers and card placements with an R for red or B for blue
- helps with visualizing card influencing and the current state of the board


Updates Made in Homework 6 
- Changed the String[][] 2D Grid to creating a enum InfluenceType and create a 
InfluenceType 2D grid for the influence grid
- Changed the Cost from a int to a Cost enum and changed the following tests 
and related code
- Change startGame to take in two List of PawnCard decks instead of the file path 
- Changed drawCard to be private 
- Changed applyInfluence to be a private method 
- Changed passTurn to not take in a player since the model stores the currentPlayer 
- Change card Initalizer  to take in two file paths and then changed the following code to return a 
hash map of a string and deck 
- Changed PawnCount to become a enum 
- Stub controller created 
- C or c for confirm the move 
- P or p for passing the move 
- 