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
Contains the deck files of the cards
- **`triplefile`**: file of cards with cards repeated thrice
- **`biggerDeck`**: file of cards with larger amount of cards, contains duplicates
- **`deck.config`**: file of cards with 20 cards

### 2. `GameScreenshots/` ()
Contains screenshots of the game states
- **`NonTrivialPoint.png`**: screenshot of the game at a nontrivial point
- **`BluePlayerSelect.png`**: screenshot of game with blue player selecting card and cell
- **`RedPlayerSelect.png`**: screenshot of game with red player selecting card and cell
- **`StartOfGame.png`**: screenshot of game at the beginning

### 3. `src/` (Root Source Directory)
This is the root directory containing the main game logic and components.
- **`UserPlayerInterfacePlan.txt`**: file of our plan for implementing AI user and players

### 4. `src/pawnboard` ()
This directory contains the core game logic and structure.
- **`PawnsBoard.java`**: Main class to run the program
- **`strategy-transcript-score.txt`**: Text file of a Max Score strategy 
- **`strategy-transcript-first.txt`**: Text file of Fill First strategy

### 5. `src/pawnboard/controller/` (Game Controller)
This directory manages interactions between the game's model and the user interface, including player input handling.
- **`GameController.java`**: Interface defining controller actions.
- **`CardInitializer.java`**: Loads card data from files and initializes the card decks for the game.
- **`PawnsGameController.java`**: A stub for simulating user input, including card and cell selections, and passing turns.
- **'ModelStatusListener.java'**: Contains methods that notifies observers of changes in the model's state during the game.
- **'PlayerActionListener.java'**: Represents methods for handling player actions in the game.
---

### 6. 'src/pawnboard/influences/' (Influence Effects)
This directory contains classes that define how cards influence the board when placed. These effects are used to modify the state of the game by affecting pawns and other cells around the card placement.
- **`InfluenceEffect.java`**: Interface defining behaviors of an influence.
- **`DefaultInfluence.java`**: Class of default influence that a card can have.

### 7. `src/pawnboard/model/` (Game Model)
This directory contains the core classes that define the game logic, rules, and board management.
- **`PawnsGame.java`**: Interface defining the methods for managing the PawnBoard game.
- **`Count.java`**: Enumeration class of the amount of pawns.
- **`ReadonlyPawnsGame.java`**: This class provides a read-only view of the game. It allows access to the current game state without modifying it, which can be useful for observers or components that need to display game data without altering it.
- **`Cell.java`**: Interface that represents a generic cell on the board.
- **`PawnGameImpl.java`**: This is the primary class that represents the state of the game. It handles the game flow, including turn-taking, card placements, pawn management, and enforcing game rules.
- **`PawnCell.java`**:This class represents a specific type of cell on the board.
- **`PawnCard.java`**: This class represents the cards in the game.
- **`Cost.java`**: Enumeration class with the cost of a card.
- **`Card.java`**: Interface that defines common methods for all types of cards in the game.
- **`InfluenceType.java`**: Enumeration class of the types of influences.
- **`Player.java`**: Enumeration class of each player.
- **'AddModelStatusListener.java'**: Contains methods needed to add a listener to the model.
---

### 8. `src/pawnboard/strategy/` (Player Strategies)
Contains various strategies that players can use to make decisions during the game.
- **`MaximizeRowStrategy.java`**: A strategy that prioritizes maximizing the player's score on a row.
- **`PawnBoardStrategy.java`**: Interface for different strategies. It defines the methods and structure that all specific strategies must follow in order to make decisions during the game.
- **`FillFirstStrategy.java`**: A strategy that quickly fills the board by placing a card in the first available legal spot.
- **`MaxControlStrategy.java`**: An extra credit strategy that maximizes the number of controlled cells. In case of ties, it prioritizes the uppermost-leftmost position.
- **`Move.java`**: A class that represents a possible move in the game.
---

### 9. `src/pawnboard/view/` (Textual and GUI Views)
Responsible for rendering the game view, either as text or through a graphical user interface.

- **`BoardPanel.java`**: Creates the panel for the board 
- **`RedFrame.java`**: Frame for the Red Player that extends the GameFrame. 
- **`GameFrame.java`**: Implementation for the Game Window that shows the board panel, hand panel 
and Score panel. 
- **`ScorePanel.java`**: Has the score of each row when cards get placed 
- **`BlueFrame.java`**:Frame for the Blue Player that extends  the GameFrame.
- **`PawnGameTextualView.java`**: Implementation of the GameView and returns where a Card has been
placed and the score of each player 
- **`HandPanel.java`**: Creates the Hand Panel and holds the cards of each player 
- **`GameWindow.java`**: Interface for the game window which displays the view and updates it 
- **`HighlightPanel.java`**: Interface for any panel that needs to be highlighted 
- **`GameObserver.java`**: Interface for the observer when the game state has changed. 
- **`GameView.java`**:Creates the Game View interface for the textual View 
- **'PlayerActionPublisher.java'**: Represents a way for the view to register a playeractionlistener.

---

### 10. 'src/pawnboard/Player'
Represents the different types of players in the game, each with distinct behaviors for interacting with the game.
- **'UserPlayer.java'**: Interface that defines the general structure for player actions in the game.
- **'HumanPlayer.java'**: Represents a player controlled by a human.
- **'MachinePlayer.java'**: Represents a player controlled by the machine (AI). Makes decisions based on what strategy it is given to make moves from.

### 11. `src/tests/pawnboard/` (Unit Tests)
Contains the unit tests for the game logic, strategies, and components to ensure correctness.



- **`PawnGameTest.java`**: Tests all the public methods in the Pawn Game Implementation. 
- **`MockPawnGame.java`**: Creates the MockGame implementation to test the strategies 
- **`MockGame.java`**: Creates the MockGame interface that is used to test the strategies. 

---

### 12. `src/tests/pawnboard/model` (Unit Tests)
Contains the unit tests for the PawnCell , PawnCard and CardInitializer components


- **`PawnCellTest.java`**: Test the methods within the PawnCell which is used to create the Cells of the Board
- **`PawnCardTest.java`**: Tests the methods withing the PawnCard which stores the values of the Card and is utilized in
  the PawnGame Implementation.
- **`CardInitializerTest.java`**: Test the creation of decks and testing the file paths. 



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

### Class Invariant : 
- INVARIANT: the board does not have null values
-  We decided to enforce this because it prevents null pointer
exceptions as there cannot be any null values and ensures that
the board data is consistent.

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



  
## Strategy
- We were able to add one extra strategy which is the MaxControlStrategy which can be found 
in the strategy folder
-  A strategy for selecting moves in Pawns Board game by maximizing the number of owned cells. 
If multiple positions yield the same number of controlled cells, the uppermost-leftmost position 
is chosen. If multiple cards yield the same number of controlled cells, the leftmost card in hand is 
chosen.
- We have added strategy tests for this strategy in our PawnGameTest and can 
be found at the end of it. 

## Influence Effect Visualized
- If a cell has an upgrading influence, it is represented on the influence grid with green
- If a cell has a decreasing influence, it is represented on the influence grid with purple
- If a cell has a default influence, it is represented on the influence grid with cyan
- The card is represented with orange on the influence grid

### Changes in Part 3
1. In the strategies, they used to take in a PawnsGameImpl which is the mutable version of the game. Instead we replaced it to take in a ReadOnlyPawnGame so that it can't accidentally mutate the game board. In the documentation, we also added that the model taken in must be a copy.
2. In the hand panel, view prints to the console on click rather than delegating this behavior to a stub controller.

# Extra Credit 
## New Influence Effects
- We were able to implement the new decreasing and increasing influences (can be seen in our InfluenceEffect folder)
- Because we initially implemented influence effects as strategies, we were able to easily implement new influence
effects simply by making a new class to represent the new influences.

## Influence Effect Visualized
- If a cell has an upgrading influence, it is represented on the influence grid with green
- If a cell has a decreasing influence, it is represented on the influence grid with purple
- If a cell has a default influence, it is represented on the influence grid with cyan
- The card is represented with orange on the influence grid
- If a cell has been affected by an increasing or decreasing effect, it will be shown through a +1 or -1, if the cell has
not been influenced by this effect, no number will show. If the cell has been influenced, but through gameplay the value gets
decreased back to 0, no number will show. 
- In our textual view, we updated it to represent the changeValues:
  1 R+0 _+1 _+0 _+0 1+0 0
  2 R+0 _+1 _+0 _+0 1+0 0
  0 2+0 _-1 _+0 _+0 1+0 0
We represent the value the cell has been changed by adding a + or - followed by a number. To keep it uniform, if there is no 
effect, there is a 0. For instance R+0 means that red has placed a card there and the value change is 0. 1+0 means that there is 1 pawn
occupying that cell and it has a changeValue of 0.