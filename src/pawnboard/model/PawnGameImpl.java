package pawnboard.model;

import java.util.ArrayList;
import java.util.List;

public class PawnGameImpl implements PawnsGame{

  private final int row;
  private final  int column;
  private Cell[][] board;
  private List<Card> playerRedDeck;
  private List<Card> playerBlueDeck;
  private List<Card> playerRedHand;
  private List<Card> playerBlueHand;
  private boolean gameStarted = false;
  private Player turn;
  private final int handSize = 5;

  public PawnGameImpl(int row, int column) {
    if(row < 0 || column < 1 || column%2 == 0 ) {
      throw new IllegalArgumentException("Row or column out of bounds");
    }

    initializeBoard();
    
    CardInitializer cardInitializer = new CardInitializer();
    this.playerRedDeck = cardInitializer.initializeDeck(RedDeckFile);
    this.playerBlueDeck = cardInitializer.initializeDeck(BlueDeckFile);

    this.playerRedHand = new ArrayList<>();
    this.playerBlueHand = new ArrayList<>();
    this.turn = Player.RED;
    this.gameStarted = false;
    this.row = row;
    this.column = column;

  }

  private void initializeBoard() {
    this.board = new Cell[row][column];
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < column; j++) {
        this.board[i][j] = new PawnCell();
      }
    }
  }

  @Override
  public void startGame(List<Card> player1deck, List<Card> player2deck, int handSize) {

    if(player1deck == null || player2deck == null) {
      throw new IllegalArgumentException("Player decks cannot be null");
    }

    if(player1deck.contains(null) || player2deck.contains(null)) {
      throw new IllegalArgumentException("Player decks contain null elements");
    }

    if(gameStarted) {
      throw new IllegalStateException("Game has already started.");
    }

    if(handSize <= 0) {
      throw new IllegalArgumentException("Hand size must be greater than 0");
    }

    if(player1deck.size() < row*column || player2deck.size() < row*column) {
      throw new IllegalArgumentException("Each deck doesn't have enough cards.");
    }

    if(handSize > player1deck.size() / 3 || handSize > player2deck.size() / 3 ) {
      throw new IllegalArgumentException("Hand size cannot be greater than ⅓ of the deck.");
    }


    this.playerRedDeck = player1deck;
    this.playerBlueDeck = player2deck;
    this.board = new Cell[row][column];
    this.gameStarted = true;
    this.turn = Player.RED;
    this.playerRedHand = new ArrayList<Card>();
    this.playerBlueHand = new ArrayList<Card>();

    for(int i = 0; i < playerRedDeck.size(); i++) {
      if(i <= handSize) {
        playerRedHand.add(playerRedDeck.get(i));
      }
    }

    playerRedDeck.removeAll(playerRedHand);

    for(int i = 0; i < playerBlueDeck.size(); i++) {
      if(i <= handSize) {
        playerBlueHand.add(playerBlueDeck.get(i));
      }
    }

    playerBlueDeck.removeAll(playerBlueHand);

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

    if(board[row][col] != null) {
      throw new IllegalArgumentException("Card already occupied");
    }

    if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
      throw new IllegalArgumentException("Row or column is out of board bounds.");
    }

    if(turn == Player.RED) {
      if ((cardIdx < 0 || cardIdx > playerRedHand.size() - 1)) {
        throw new IllegalArgumentException("Card Index is out of Bounds");
      }

      if (cardIdx > playerRedHand.size() + 1) {
        throw new IllegalArgumentException("Card Index is out of Bounds");
      }

      Card card = playerRedHand.get(cardIdx);
      ;
      board[row][col].placeCard(card);
      playerRedHand.remove(cardIdx);

    }
      else {
        if ((cardIdx < 0 || cardIdx > playerBlueHand.size() - 1)) {
          throw new IllegalArgumentException("Card Index is out of Bounds");
        }

        if(cardIdx > playerBlueHand.size() + 1) {
          throw new IllegalArgumentException("Card Index is out of Bounds");
        }

        Card card2 = playerBlueHand.get(cardIdx);;
        board[row][col].placeCard(card2);
        playerBlueHand.remove(cardIdx);
      }
  }

  @Override
  public void passTurn(Player player) {
    if (player == Player.RED) {
      turn = Player.BLUE;
    }
    else if (player == Player.BLUE) {
      turn = Player.RED;
    }
  }

  @Override
  public void drawCard(Player player) {

  }

  @Override
  public List<Card> getNewDeck() {
    return List.of();
  }

  @Override
  public Cell getCellAt(int row, int col) {
    return null;
  }

  @Override
  public Player getCurrentPlayer() {
    return turn;
  }

  @Override
  public List<Card> getHand(Player player) {
    if(player == Player.RED) {
      return playerRedDeck;
    }
    return playerBlueDeck;
  }

  @Override
  public int getRemainingDeckSize(Player player) {
    if(player == Player.RED) {
      return playerRedDeck.size();
    }
    return playerBlueDeck.size();
  }

  @Override
  public Player getWinner() {
    return null;
  }

  @Override
  public int checkScore(Player player) {
    return 0;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public void applyCardInfluence(int row, int col, Card card, Player currentPlayer) {

  }
}
