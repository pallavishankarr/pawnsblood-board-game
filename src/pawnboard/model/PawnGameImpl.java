package pawnboard.model;

import java.util.ArrayList;
import java.util.List;

public class PawnGameImpl implements PawnsGame{

  private final int row;
  private final  int column;
  private Cell[][] board;
  private List<PawnCard> playerRedDeck;
  private List<PawnCard> playerBlueDeck;
  private List<PawnCard> playerRedHand;
  private List<PawnCard> playerBlueHand;
  private boolean gameStarted = false;
  private Player turn;
  private final int handSize = 5;

  public PawnGameImpl(int row, int column) {
    if(row < 0 || column < 1 || column%2 == 0 ) {
      throw new IllegalArgumentException("Row or column out of bounds");
    }

    initializeBoard();

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

  // need to initialize the deck somewhere since you give in a deck

  // create private method to initialize the hand

  @Override
  public void startGame(List<Card> player1deck, List<Card> player2deck) {

    if(player1deck == null || player2deck == null) {
      throw new IllegalArgumentException("Player decks cannot be null");
    }

    if(player1deck.contains(null) || player2deck.contains(null)) {
      throw new IllegalArgumentException("Player decks contain null elements");
    }

    if(gameStarted) {
      throw new IllegalStateException("Game has already started.");
    }

    if(player1deck.size() < row * column || player2deck.size() < row * column) {
      throw new IllegalArgumentException("Each deck doesn't have enough cards.");
    }

    if(handSize > player1deck.size() / 3 || handSize > player2deck.size() / 3 ) {
      throw new IllegalArgumentException("Hand size cannot be greater than ⅓ of the deck.");
    }
    
    CardInitializer cardInitializer = new CardInitializer();
    this.playerRedDeck = cardInitializer.initializeDeck("CardList.txt");
    this.playerBlueDeck = cardInitializer.initializeDeck("CardList.txt");

    this.gameStarted = true;
    this.turn = Player.RED;


    // initializes a players hand
    for (int i = 0; i < playerRedDeck.size(); i++) {
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

    Cell cell = this.board[row][col];

    if(!gameStarted) {
      throw new IllegalStateException("Game has not started.");
    }

    if(getCellAt(row, col).hasCard()) {
      throw new IllegalArgumentException("Card already occupied");
    }

    if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
      throw new IllegalArgumentException("Row or column is out of board bounds.");
    }

    // ABSTRACT THIS PART

    if (turn == Player.RED) {
      if ((cardIdx < 0 || cardIdx > playerRedHand.size() - 1)) {
        throw new IllegalArgumentException("Card Index is out of Bounds");
      }

      if (cardIdx > playerRedHand.size() + 1) {
        throw new IllegalArgumentException("Card Index is out of Bounds");
      }

      Card card = playerRedHand.get(cardIdx);

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
    if(player == Player.BLUE) {
      playerBlueHand.add(playerBlueDeck.get(0));
      playerBlueDeck.remove(0);
    }

    else {
      playerRedHand.add(playerRedDeck.get(0));
      playerRedDeck.remove(0);
    }

  }

  // do we really need this?
  @Override
  public List<Card> getNewDeck() {
    return List.of();
  }

  @Override
  public Cell getCellAt(int row, int col) {
    return board[row][col];
  }

  @Override
  public Player getCurrentPlayer() {
    return turn;
  }

  @Override
  public List<PawnCard> getHand(Player player) {
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
    int score = 0;

    for(int row = 0 ; row < this.row ; row++) {
      if(rowWinner(row) == player){
        score =+ checkScoreRow(player, row);
      }
      else {
        score =+ 0;
      }

    }
    return score;

  }


  private int checkScoreRow(Player player, int rowIdx) {
    int score = 0;

    for (int col = 0; col < column; col++) {
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

  private Player rowWinner(int rowIdx) {
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
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < column; j++) {
        if (!board[i][j].hasCard()) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public void applyCardInfluence(int row, int col, Card card, Player currentPlayer) {

  }
}
