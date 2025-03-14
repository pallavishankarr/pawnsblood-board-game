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

### Card Initializer
- loads card data into a list when given file

### Textual View
- provides text based reprsentation of the board
- displays pawn with integers and card placements with an R for red or B for blue


