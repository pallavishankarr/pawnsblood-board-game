# CS3500Assignment5

## Overview

Pawns Board is a two-player strategy game inspired by Queen’s Blood. The game is played on a rectangular board with an odd number of columns, where players take turns placing cards that influence the board based on a grid system. Players compete for control of rows using pawns and cards, and the game ends when both players pass consecutively. The player with the highest row-based score wins.

## Key Information

- The board has a fixed size with positive rows and an odd number of columns greater than 1.

- Players have a limited number of pawns and must manage them strategically.

- Cards have placement costs and apply influence to the board.

- The game model enforces rules, turn-taking, and legal moves.

- A file configuration of the cards must be passed in to start the game. 

## Source Organization
The codebase for Pawns Board is organized into several directories, each focusing on different aspects of the game. Below is a "map" to help you navigate through the source code and understand where different components are located.

### 1. `docs/` ()
Contains the deck files for card reading
- **`triplefile`**: 
- **`biggerDeck`**:
- **`deck.config`**:

### 1. `GameScreenshots/` ()
- **`NonTrivialPoint.png`**:
- **`BluePlayerSelect.png`**:
- **`RedPlayerSelect.png`**:
- **`StartOfGame.png`**:

### 1. `src/` (Root Source Directory)
This is the root directory containing the main game logic and components.
- **`UserPlayerInterfacePlan.txt`**:

### 2. `src/pawnboard` ()
- **`PawnsBoard.java`**:
- **`strategy-transcript-score.txt`**:
- **`strategy-transcript-first.txt`**:

### 3. `src/pawnboard/controller/` (Game Controller)
This directory manages interactions between the game's model and the user interface, including player input handling.

- **`GameController.java`**: Interface defining actions like card clicks, cell clicks, and passing turns.
- **`CardInitializer.java`**: Loads card data from files and initializes the card decks for the game.
- **`PawnsGameController.java`**: A stub for simulating user input, including card and cell selections, and passing turns.
---

### 4. 'src/pawnboard/influences/' (Influence Effects)
- **`InfluenceEffect.java`**: 
- **`DefaultInfluence.java`**:

### 5. `src/pawnboard/boardmodel/` (Game Model)
This directory contains the core classes that define the game logic, rules, and board management.

- **`PawnsGame.java`**:
- **`Count.java`**:
- **`ReadonlyPawnsGame.java`**:
- **`Cell.java`**:
- **`PawnGameImpl.java`**:
- **`PawnCell.java`**:
- **`PawnCard.java`**:
- **`Cost.java`**:
- **`Card.java`**:
- **`InfluenceType.java`**:
- **`Player.java`**:
---

### 6. `src/pawnboard/strategy/` (Player Strategies)
Contains various strategies that players can use to make decisions during the game.
- **`MaximizeRowStrategy.java`**: A strategy that prioritizes maximizing the player's score on a row.
- **`PawnBoardStrategy.java`**:
- **`FillFirstStrategy.java`**: A strategy that quickly fills the board by placing a card in the first available legal spot.
- **`MaxControlStrategy.java`**: An extra credit strategy that maximizes the number of controlled cells. In case of ties, it prioritizes the uppermost-leftmost position.
- **`Move.java`**: 

---

### 7. `src/pawnboard/view/` (Textual and GUI Views)
Responsible for rendering the game view, either as text or through a graphical user interface.

- **`BoardPanel.java`**:
- **`RedFrame.java`**:
- **`GameFrame.java`**:
- **`ScorePanel.java`**:
- **`BlueFrame.java`**:
- **`PawnGameTextualView.java`**:
- **`HandPanel.java`**:
- **`GameWindow.java`**:
- **`HighlightPanel.java`**:
- **`GameObserver.java`**:
- **`GameView.java`**:

---

### 6. `src/tests/pawnboard/` (Unit Tests)
Contains the unit tests for the game logic, strategies, and components to ensure correctness.

- **`PawnGameTest.java`**: 
- **`MockPawnGame.java`**: 
- **`MockGame.java`**: 

---

### 6. `src/tests/pawnboard/model` (Unit Tests)
Contains the unit tests for the game logic, strategies, and components to ensure correctness.

- **`PawnCellTest.java`**:
- **`PawnCardTest.java`**:
- **`CardInitializerTest.java`**:



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
-  introduced the ReadsOnly interface to provide controlled, read-only access to the game's state.

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

### Controller 
- added the CardInitializer to be part of our controller package 
- Created the game controller interface
- Created the stub controller which handles card click and print out the index of the card clicked 
and the player which owns the card
- Also handles the cell click which also prints out the index of the cell
- Handles the key press which after selecting card and cell player can confirm their move 
by pressing "C" or "c" or they could press "p" or "P" to pass the turn 

### Strategies 
Fill First Strategy 
- places a card in the first available legal spot on the board, starting from the top-left
- prioritizes quick board filling and uses the player's hand to find the first valid move
- Picks the first valid move
- Throws exceptions if model/player is null or if the game is over
Maximize Row Strategy 
-  aims to maximize the player's score on a row by placing a card in a position that improves 
their row score more than the opponent’s
- Prioritizes placing cards to maximize the player's row score
- Avoids placing in rows where the player’s score is already higher than the opponent’s
- Caches legal moves to avoid repeating checks



### GUI
- sets up and displays the game window, including the board, player hands, and score panels. 
-  ensures that the game model and player are initialized properly
- Hand panel for tracking the hand and making sure cards get added when a card is drawn 
- RedFrame displays the game window for the red player 
- Blue Frame displays teh game window for the blue player 
- Score panel displays teh score on either side of each row 





Changes for Part 2 
- Changed the String[][] 2D Grid to creating a enum InfluenceType and create a 
InfluenceType 2D grid for the influence grid
- Changed the Cost from a int to a Cost enum and changed the following tests 
and related code
- Changes the Count from a int to a Count enum and changed the following tests 
and related code
- Change startGame to take in two List of PawnCard decks instead of the file path 
- Changed drawCard to be private 
- Changed applyInfluence to be a private method 
- Changed passTurn to not take in a player since the model stores the currentPlayer 
- Change card initializer to take in two file paths and then changed the following code to return a 
hash map of a string and deck 
- Changed PawnCount to become a enum 
- We  changed our PawnsGame Implementation to add the types of the Player and PawnCard, and keeping
    the PawnCell already there




### Extra Credit Strategy 

- We were able to add one extra strategy which is the MaxControlStrategy which can be found 
in the strategy folder
-  A strategy for selecting moves in Pawns Board game by maximizing the number of owned cells. 
If multiple positions yield the same number of controlled cells, the uppermost-leftmost position 
is chosen. If multiple cards yield the same number of controlled cells, the leftmost card in hand is 
chosen.
- We have added strategy tests for this strategy in our PawnGameTest and can 
be found at the end of it. 
