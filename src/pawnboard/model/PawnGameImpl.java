package pawnboard.model;

import java.util.ArrayList;
import java.util.List;

import pawnboard.controller.ModelStatusListener;
import pawnboard.influences.InfluenceEffect;
import pawnboard.view.GameObserver;

/**
 * Represents the logic of the 2 player Pawn Board game.
 * Maintains the game state including the rectangular board, player decks, turns, and
 * move validation.
 * The game board is represented as a 2D grid with rows and columns
 * indexed starting from 0 (0-index based). The top-left cell of the board is at (0, 0).
 */
public class PawnGameImpl implements PawnsGame<PawnCell, Player, PawnCard>, AddModelStatusListener {

  private final int row;
  private final int column;
  private final List<GameObserver> observers;
  private PawnCell[][] board;
  private List<PawnCard> playerRedDeck;
  private List<PawnCard> playerBlueDeck;
  private final List<PawnCard> playerRedHand;
  private final List<PawnCard> playerBlueHand;
  private boolean gameStarted = false;
  private Player turn;
  private int handSize;
  private boolean turnStarted;
  private List<ModelStatusListener> listeners;
  private ModelStatusListener listener;

  /**
   * Constructs a new PawnGameImpl object with the given board dimensions.
   * The game board is represented as a 2D grid with rows and columns
   * indexed starting from 0 (0-index based). The top-left cell of the board is at (0, 0).
   *
   * @param row    the number of rows in the board (must be positive)
   * @param column the number of column in the board (must be odd and >1)
   * @throws IllegalArgumentException if the row is not a positive integer or column is not
   *                                  odd and greater than 1.
   */
  public PawnGameImpl(int row, int column) {
    if (row <= 0 || column <= 1 || column % 2 == 0) {
      throw new IllegalArgumentException("Row or column out of bounds");
    }

    this.playerRedDeck = new ArrayList<>();
    this.playerBlueDeck = new ArrayList<>();
    this.playerRedHand = new ArrayList<>();
    this.playerBlueHand = new ArrayList<>();
    this.turn = Player.RED;
    this.gameStarted = false;
    this.row = row;
    this.column = column;
    this.turnStarted = false;
    observers = new ArrayList<>();
    listeners = new ArrayList<>();

    // INVARIANT: the board does not have null values
    // We decided to enforce this because it prevents null pointer
    // exceptions as there cannot be any null values and ensures that
    // the board data is consistent.
    initializeBoard();
  }

  @Override
  public void startGame(List<PawnCard> deck1, List<PawnCard> deck2, int handSize) {

    if (gameStarted) {
      throw new IllegalStateException("Game has already started.");
    }

    List<PawnCard> redDeck = deck1;
    List<PawnCard> blueDeck = deck2;


    if (redDeck.isEmpty() || blueDeck.isEmpty()) {
      throw new IllegalArgumentException("Decks cannot be null or empty.");
    }

    if (redDeck.size() != blueDeck.size()) {
      throw new IllegalArgumentException("Decks must be the same size");
    }

    if (redDeck.contains(null) || blueDeck.contains(null) ||
            redDeck == null || blueDeck == null) {
      throw new IllegalArgumentException("Decks contain null elements.");
    }

    if (redDeck.size() < row * column || blueDeck.size() < row * column) {
      throw new IllegalArgumentException("Each deck must have at least "
              +
              (row * column) + " cards.");
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
    notifyObservers();
  }

  @Override
  public int getWidth() {
    return column;
  }

  @Override
  public int getHeight() {
    return row;
  }

  @Override
  public boolean placeCardLegal(Player player, PawnCard card, int row, int col) {
    gameNotStartedCheck();

    return row >= 0 && row < board.length
            && col >= 0 && col < board[0].length
            && !board[row][col].hasCard()
            && board[row][col].getOwner() == turn
            && board[row][col].getPawnCount().getValue() >= card.getPawnCost().getValue();
  }

  @Override
  public void placeCardAt(Player player, int cardIdx, int row, int col) {
    gameNotStartedCheck();

    playerNotTurnCheck(this.turn);

    turnNotStartedCheck(this.turn);

    if (turn != player) {
      throw new IllegalArgumentException("It's not this players turn");
    }

    if (getCellAt(row, col).hasCard()) {
      throw new IllegalArgumentException("Card already occupied.");
    }

    if (player == Player.RED) {
      placeCardAtHelper(playerRedHand, player, cardIdx, row, col);
    }

    if (player == Player.BLUE) {
      placeCardAtHelper(playerBlueHand, player, cardIdx, row, col);
    }

    notifyObservers();
    endTurn();
  }

  @Override
  public void passTurn() {

    gameNotStartedCheck();

    playerNotTurnCheck(getCurrentPlayer());

    turnNotStartedCheck(getCurrentPlayer());

    notifyObservers();

    listener.onTurnChanged();

    endTurn();
  }


  private void drawCard(Player player) {
    gameNotStartedCheck();

    playerNotTurnCheck(player);

    turnNotStartedCheck(player);

    List<PawnCard> playerDeck = (player == Player.RED) ? playerRedDeck : playerBlueDeck;
    List<PawnCard> playerHand = (player == Player.RED) ? playerRedHand : playerBlueHand;

    if (!playerDeck.isEmpty()) {
      playerHand.add(playerDeck.remove(0));
    }
  }

  @Override
  public void startTurn(Player player) {
    gameNotStartedCheck();

    playerNotTurnCheck(player);

    turnStarted = true;
    drawCard(player);
    notifyObservers();
  }

  @Override
  public PawnCell[][] getBoard() {
    gameNotStartedCheck();
    PawnCell[][] newBoard = new PawnCell[row][column];

    for (int r = 0; r < row; r++) {
      for (int c = 0; c < column; c++) {
        PawnCell original = this.board[r][c];
        Player originalCellOwner = original.getOwner();
        Count originalPawnCellCount = original.getPawnCount();
        PawnCell copy = new PawnCell(originalCellOwner, originalPawnCellCount);

        if (original.hasCard()) {
          PawnCard originalCard = original.getCard();
          copy.placeCard(originalCard);
        }

        newBoard[r][c] = copy;
      }
    }
    return newBoard;
  }


  @Override
  public PawnCell getCellAt(int row, int col) {
    if (row >= this.row || col >= this.column || row < 0 || col < 0) {
      throw new IllegalArgumentException("Row or column is out of board bounds.");
    }
    return board[row][col];
  }

  @Override
  public Player getCurrentPlayer() {
    gameNotStartedCheck();

    return turn;
  }

  @Override
  public Player getOwnerAt(int row, int column) {
    gameNotStartedCheck();

    return board[row][column].getOwner();
  }

  @Override
  public void addObserver(GameObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException("Observer cannot be null.");
    }
    if (!observers.contains(observer)) {
      observers.add(observer);
    }
  }

  @Override
  public void removeObserver(GameObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException("Observer cannot be null.");
    }
    observers.remove(observer);
  }

  private void notifyObservers() {
    for (GameObserver observer : observers) {
      observer.gameStateChanged();
    }
  }

  @Override
  public List<PawnCard> getHand(Player player) {
    gameNotStartedCheck();

    if (player == Player.RED) {
      return new ArrayList<>(playerRedHand);
    }
    return new ArrayList<>(playerBlueHand);
  }

  @Override
  public int getRemainingDeckSize(Player player) {
    gameNotStartedCheck();

    if (player == Player.RED) {
      return playerRedDeck.size();
    }
    return playerBlueDeck.size();
  }

  @Override
  public String getWinner() {
    gameNotStartedCheck();

    int redScore = checkScore(Player.RED);
    int blueScore = checkScore(Player.BLUE);

    if (redScore > blueScore) {
      return Player.RED.toString();
    } else if (blueScore > redScore) {
      return Player.BLUE.toString();
    } else {
      return "Tie";
    }
  }

  @Override
  public int checkScore(Player player) {
    gameNotStartedCheck();

    int score = 0;

    for (int row = 0; row < this.row; row++) {
      if (rowWinner(row) == player) {
        score += checkScoreRow(player, row);
      } else {
        score += 0;
      }
    }
    return score;
  }

  @Override
  public int checkScoreRow(Player player, int rowIdx) {
    if (rowIdx < 0 || rowIdx >= row) {
      throw new IllegalArgumentException("Row index out of bounds");
    }

    int score = 0;

    for (int col = 0; col < this.column; col++) {
      PawnCell cell = board[rowIdx][col];
      if (cell.hasCard()) {
        PawnCard card = cell.getCard();
        if (cell.getOwner() == player) {
          score += card.getValue();
        }
      }
    }
    listener.onScoreUpdate();
    return score;
  }

  private Player rowWinner(int rowIdx) {
    gameNotStartedCheck();

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
    gameNotStartedCheck();

    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        if (board[i][j].getPawnCount().getValue() > 0) {
          return false;
        }
      }
    }
    listener.onGameOver();
    return true;
  }


  private void applyCardInfluence(int rowIdx, int colIdx, Player currentPlayer, PawnCard card) {
    gameNotStartedCheck();

    if (rowIdx < 0 || rowIdx >= row || colIdx < 0 || colIdx >= column) {
      throw new IllegalArgumentException("Row index out of bounds");
    }

    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null");
    }

    InfluenceType[][] influenceGrid = card.getInfluenceGrid();
    List<InfluenceEffect<PawnCell, Player, PawnCard>> influenceEffects = card.getInfluenceEffects();

    boolean stateChanged = false;

    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (influenceGrid[i][j] == InfluenceType.I) {
          int targetRow = rowIdx + (i - 2);
          int targetCol = colIdx + (j - 2);

          try {
            if (getCellAt(targetRow, targetCol).hasCard()) {
              continue;
            }

            for (InfluenceEffect effect : influenceEffects) {
              effect.apply(this, targetRow, targetCol, currentPlayer);
              stateChanged = true;
            }

          } catch (IllegalArgumentException e) {
            // Ignore out-of-bounds cells
          }
        }
      }
    }
    if (stateChanged) {
      notifyObservers();
    }
  }

  private void initializeBoard() {
    this.board = new PawnCell[row][column];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < column; j++) {
        this.board[i][j] = new PawnCell();

        if (j == 0) {
          board[i][j].addPawn(Player.RED);
        } else if (j == column - 1) {
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

  private void placeCardAtHelper(List<PawnCard> playerHand, Player player,
                                 int cardIdx, int row, int col) {

    if (cardIdx < 0 || cardIdx >= playerHand.size()) {
      throw new IllegalArgumentException("CardIdx out of bounds");
    }


    if (playerHand.size() < handSize) {
      throw new IllegalArgumentException("Cannot play card. " +
              "Hand size cannot drop below " + handSize);
    }

    PawnCard card = playerHand.get(cardIdx);

    if (!this.placeCardLegal(player, card, row, col)) {
      throw new IllegalArgumentException("card cannot be placed here");
    }

    PawnCell cell = this.board[row][col];

    cell.placeCard(card);
    playerHand.remove(cardIdx);
    applyCardInfluence(row, col, player, card);

  }

  private void drawCardHelper(List<PawnCard> playerDeck,
                              List<PawnCard> playerHand) {

    playerHand.add(playerDeck.get(0));
    playerDeck.remove(0);
  }


  private void endTurn() {
    turnStarted = false;
    turn = (turn == Player.RED) ? Player.BLUE : Player.RED;
    notifyObservers();

  }

  private void gameNotStartedCheck() {
    if (!gameStarted) {
      throw new IllegalStateException("Game has not started.");
    }
  }

  private void playerNotTurnCheck(Player player) {
    if (player != turn) {
      throw new IllegalArgumentException("It is not " + player + "'s turn.");
    }
  }

  private void turnNotStartedCheck(Player player) {
    if (!turnStarted) {
      throw new IllegalStateException("You must start your turn before playing a card.");
    }
  }

  @Override
  public void addsModelStatusListener(ModelStatusListener listener) throws IllegalArgumentException {
    if(listener == null) {
      throw new IllegalArgumentException("Listener cannot be null");
    }

    listeners.add(listener);


  }
}
