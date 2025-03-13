package pawnboard.model;

import java.util.ArrayList;
import java.util.List;

import pawnboard.influences.InfluenceEffect;

public class PawnGameImpl implements PawnsGame<PawnCard> {

  private final int row;
  private final  int column;
  private Cell[][] board;
  private List<PawnCard> playerRedDeck;
  private List<PawnCard> playerBlueDeck;
  private List<PawnCard> playerRedHand;
  private List<PawnCard> playerBlueHand;
  private boolean gameStarted = false;
  private Player turn;
  private int handSize;
  private boolean turnStarted;

  public PawnGameImpl(int row, int column) {
    if(row <= 0 || column <= 1 || column%2 == 0 ) {
      throw new IllegalArgumentException("Row or column out of bounds");
    }

    // INVARIANT: a players deck cannot be null
    this.playerRedDeck = new ArrayList<>();
    this.playerBlueDeck = new ArrayList<>();
    this.playerRedHand = new ArrayList<>();
    this.playerBlueHand = new ArrayList<>();
    this.turn = Player.RED;
    this.gameStarted = false;
    this.row = row;
    this.column = column;
    this.turnStarted = false;

    // INVARIANT: the board does not have null values
    initializeBoard();
  }

  @Override
  public void startGame(String path, int handSize) {

    if (gameStarted) {
      throw new IllegalStateException("Game has already started.");
    }

    List<PawnCard> redDeck = CardInitializer.initializeDeck(path);
    List<PawnCard> blueDeck = mirrorDeck(redDeck);

    if (redDeck.isEmpty() || blueDeck.isEmpty()) {
      throw new IllegalArgumentException("Decks cannot be null or empty.");
    }

    if (redDeck.size() != blueDeck.size()) {
      throw new IllegalArgumentException("Decks must be the same size");
    }

    if (redDeck.contains(null) || blueDeck.contains(null)) {
      throw new IllegalArgumentException("Decks contain null elements.");
    }

    if (redDeck.size() < row * column || blueDeck.size() < row * column) {
      throw new IllegalArgumentException("Each deck must have at least " + (row * column) + " cards.");
    }

    if (handSize > redDeck.size() / 3 || handSize > blueDeck.size() / 3 || handSize < 0) {
      throw new IllegalArgumentException("Hand size cannot be greater than ⅓ of the deck.");
    }

    this.playerRedDeck = redDeck;
    this.playerBlueDeck = blueDeck;
    this.handSize = handSize;

    initializeHand(playerRedDeck, playerRedHand);
    initializeHand(playerBlueDeck, playerBlueHand);

    this.gameStarted = true;
    this.turn = Player.RED;
    this.turnStarted = true;
  }

  @Override
  public int getWidth() {
    return this.column;
  }

  @Override
  public int getHeight() {
    return this.row;
  }

  @Override
  public void placeCardAt(Player player, int cardIdx, int row, int col) {
    if(!gameStarted) {
      throw new IllegalStateException("Game has not started.");
    }

    if (player != turn) {
      throw new IllegalArgumentException("It is not " + player + "'s turn.");
    }

    if (!turnStarted) {
      throw new IllegalStateException("You must start your turn before playing a card.");
    }

    if(getCellAt(row, col).hasCard()) {
      throw new IllegalArgumentException("Card already occupied");
    }

    if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
      throw new IllegalArgumentException("Row or column is out of board bounds.");
    }

    if (cardIdx > playerRedDeck.size()) {
      throw new IllegalArgumentException("Card index out of bounds");
    }

    if (player == Player.RED) {
      placeCardAtHelper(playerRedHand, player, cardIdx,row,col);
    }

    if (player == Player.BLUE) {
      placeCardAtHelper(playerBlueHand, player, cardIdx,row,col);
    }

    endTurn();
  }

  @Override
  public void passTurn(Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started.");
    }

    if (player != turn) {
      throw new IllegalArgumentException("It is not " + player + "'s turn.");
    }

    if (!turnStarted) {
      throw new IllegalStateException("You must start your turn before passing.");
    }

    endTurn();
  }

  @Override
  public void drawCard(Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started.");
    }

    if (player != turn) {
      throw new IllegalArgumentException("It is not " + player + "'s turn.");
    }

    if (!turnStarted) {
      throw new IllegalStateException("You must start your turn before passing.");
    }

    List<PawnCard> playerDeck = (player == Player.RED) ? playerRedDeck : playerBlueDeck;
    List<PawnCard> playerHand = (player == Player.RED) ? playerRedHand : playerBlueHand;

    if (!playerDeck.isEmpty()) {
      playerHand.add(playerDeck.remove(0));
    }
  }

  @Override
  public void startTurn(Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started.");
    }

    if (player != turn) {
      throw new IllegalArgumentException("It is not " + player + "'s turn.");
    }

    turnStarted = true;
    drawCard(player);
  }


  @Override
  public Cell getCellAt(int row, int col) {
    if (row >= this.row || col >= this.column || row < 0 || col < 0) {
      throw new IllegalArgumentException("Row or column is out of board bounds.");
    }
    return board[row][col];
  }

  @Override
  public Player getCurrentPlayer() {
    if (!gameStarted) {
      throw new IllegalStateException("game has not been started");
    }

    return turn;
  }

  @Override
  public List<PawnCard> getHand(Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("game has not been started");
    }

    if(player == Player.RED) {
      return playerRedHand;
    }
    return playerBlueHand;
  }

  @Override
  public int getRemainingDeckSize(Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("game has not been started");
    }

    if (player == Player.RED) {
      return playerRedDeck.size();
    }
    return playerBlueDeck.size();
  }

  @Override
  public Player getWinner() {
    if (!gameStarted) {
      throw new IllegalStateException("game has not been started");
    }

    if (!isGameOver()) {
      throw new IllegalArgumentException("Game isn't over.");
    }

    int redScore = checkScore(Player.RED);
    int blueScore = checkScore(Player.BLUE);

    if (redScore > blueScore) {
      return Player.RED;
    }
    else {
      return Player.BLUE;
    }
  }

  @Override
  public int checkScore(Player player) {
    if (!gameStarted) {
      throw new IllegalStateException("game has not been started");
    }

    int score = 0;

    for(int row = 0 ; row < this.row ; row++) {
      if(rowWinner(row) == player){
        score += checkScoreRow(player, row);
      }
      else {
        score += 0;
      }
    }
    return score;
  }

  @Override
  public int checkScoreRow(Player player, int rowIdx) {
    if (!gameStarted) {
      throw new IllegalStateException("game has not been started");
    }

    if (rowIdx < 0 || rowIdx >= row) {
      throw new IllegalArgumentException("Row index out of bounds");
    }

    int score = 0;

    for (int col = 0; col < this.column; col++) {
      Cell cell = board[rowIdx][col];
      if (cell.hasCard()) {
        Card card = cell.getCard();
        if (cell.getOwner() == player) {
          score += card.getValue();
        }
      }
    }
    return score;
  }

  @Override
  public Player rowWinner(int rowIdx) {
    if (!gameStarted) {
      throw new IllegalStateException("game has not been started");
    }

    if (rowIdx < 0 || rowIdx >= row) {
      throw new IllegalArgumentException("Row index out of bounds");
    }

    int redScore = checkScoreRow(Player.RED, rowIdx);
    int blueScore = checkScoreRow(Player.BLUE, rowIdx);

    if (redScore > blueScore) {
      return Player.RED;
    } else if (blueScore > redScore) {
      return Player.BLUE;
    } else {
      return null;
    }
  }

  @Override
  public boolean isGameOver() {
    if (!gameStarted) {
      throw new IllegalStateException("game has not been started");
    }

    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j].getPawnCount() > 0) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public void applyCardInfluence(int rowIdx, int colIdx, Player currentPlayer, Card card) {
    if (!gameStarted) {
      throw new IllegalStateException("game has not been started");
    }

    if (rowIdx < 0 || rowIdx >= row || colIdx < 0 || colIdx >= column) {
      throw new IllegalArgumentException("Row index out of bounds");
    }

    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }

    char[][] influenceGrid = card.getInfluenceGrid();
    List<InfluenceEffect> influenceEffects = card.getInfluenceEffects();


    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (influenceGrid[i][j] == 'I') {
          int targetRow = rowIdx + (i - 2);
          int targetCol = colIdx + (j - 2);

          try {
            if (getCellAt(targetRow, targetCol).hasCard()) {
              continue;
            }

              for (InfluenceEffect effect : influenceEffects) {
                effect.apply(this, targetRow, targetCol, currentPlayer);
              }

          } catch (IllegalArgumentException e) {
            // Ignore out-of-bounds cells
          }
        }
      }
    }
  }

  private void initializeBoard() {
    this.board = new Cell[row][column];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < column; j++) {
        this.board[i][j] = new PawnCell();

        if (j == 0) {
          board[i][j].addPawn(Player.RED);
        }
        else if (j == column - 1) {
          board[i][j].addPawn(Player.BLUE);
        }
      }
    }
  }

  private void initializeHand(List<PawnCard> deck, List<PawnCard> hand) {
    for (int i = 0; i < handSize; i++) {
      hand.add(deck.remove(0));
    }
  }

  private void placeCardAtHelper(List<PawnCard> playerHand,Player player,
                                 int cardIdx, int row, int col) {

    if (cardIdx < 0 || cardIdx >= playerHand.size()) {
      throw new IllegalArgumentException("CardIdx out of bounds");
    }


    if (playerHand.size() < handSize) {
      throw new IllegalArgumentException("Cannot play card. Hand size cannot drop below " + handSize);
    }

    Card card = playerHand.get(cardIdx);
    Cell cell = this.board[row][col];

    if(cell.getOwner() != player) {
      throw new IllegalArgumentException("Player is not the owner of this cell");
    }

    if (cell.getPawnCount() < card.getPawnCost()) {
      throw new IllegalArgumentException("Not enough pawns to place this card");
    }

    else {
      cell.placeCard(card);
      playerHand.remove(cardIdx);
      applyCardInfluence(row, col, player, card);
    }


  }

  private void drawCardHelper(List<PawnCard> playerDeck,
                              List<PawnCard> playerHand) {

    playerHand.add(playerDeck.get(0));
    playerDeck.remove(0);
  }

  private char[][] mirrorGrid(char[][] grid) {
    int size = grid.length;
    char[][] mirrored = new char[size][size];

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        mirrored[i][j] = grid[i][size - 1 - j]; // Reverse columns
      }
    }

    return mirrored;
  }

  private List<PawnCard> mirrorDeck(List<PawnCard> deck) {
    List<PawnCard> mirroredDeck = new ArrayList<>();
    for (PawnCard card : deck) {
      char[][] mirroredGrid = mirrorGrid(card.getInfluenceGrid());
      mirroredDeck.add(new PawnCard(card.getName(), card.getPawnCost(), card.getValue(), mirroredGrid));
    }
    return mirroredDeck;
  }

  private void endTurn() {
    turnStarted = false;
    turn = (turn == Player.RED) ? Player.BLUE : Player.RED;

    if (isGameOver()) {
      gameStarted = false;
    }
  }
}
