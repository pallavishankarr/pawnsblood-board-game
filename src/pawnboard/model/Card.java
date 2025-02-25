package pawnboard.model;

import java.util.List;

/**
 * Interface represents a card defining its properties and behaviors.
 * It includes methods the retrieve Card properties and how a card
 * affects Cells and applies influence.
 */
public interface Card {

  /**
   * Returns the name of the card.
   * @return String of the card name
   */
  String getName();

  /**
   * Returns the pawn cost of the card
   * @return a integer of the pawn cost
   */
  int getPawnCost();

  /**
   * Returns the value which is points of the card added to the score.
   * @return the value of the card
   */
  int getValue();

  /**
   * Returns the influence grid which shows what cells state changes.
   * @return a 2D array representing the influence pattern of the grid
   */
  char[][] getInfluenceGrid();

  /**
   * Determines if the Card affects cell of the given row and column.
   * @param row row index of the cell
   * @param col col index of the cell
   * @return true if the cell is affected by the card or false if not
   * @throws IllegalArgumentException if the row and col indexes are invalid
   */
  boolean affectsCell(int row, int col) throws IllegalArgumentException;

  /**
   * Applies the influence of the card onto the cells to the board.
   */
  void applyInfluence(PawnsGame game, int boardRow, int boardCol, Player currentPlayer);

  /**
   * Returns a String representation of the card
   * @return String of the card
   */
  String toString();


  class PawnPlayer implements Player {

    private PlayerColor color;

    //first index represents row and second represents column
    private Cell[][] board;
    private List<Card> deck;
    private List<Card> hand;

    PawnPlayer(PlayerColor color, Cell[][] board, List<Card> deck, List<Card> hand) {

      if(color == null) {
        throw new IllegalArgumentException("color is null");
      }
      if(board == null) {
        throw new IllegalArgumentException("Board cannot be null");
      }

      if(deck == null) {
        throw new IllegalArgumentException("Deck cannot be null");
      }

      if(hand == null) {
        throw new IllegalArgumentException("Hand cannot be null");
      }
      this.color = color;
      this.board = board;
      this.deck = deck;
      this.hand = hand;
    }



    @Override
    public void playCard(PawnsGame game, int cardIdx, int row, int col) {

      if(game == null) {
        throw new IllegalArgumentException("Game is null");
      }

      if(cardIdx > hand.size() + 1 || cardIdx < 0) {
        throw new IllegalArgumentException("Card index out of bounds");
      }

      if(row > 0 || col > 1 && col%2 == 0 )  {
        throw new IllegalArgumentException("Row and column out of bounds");
      }

      Card card = hand.get(cardIdx);
      board[row][col].placeCard(card);
      hand.remove(cardIdx);

    }

    @Override
    public void passMove(PawnsGame game) {

      if(game == null) {
        throw new IllegalArgumentException("Game is null");
      }

      if(this.color == PlayerColor.BLUE) {
        this.color = PlayerColor.RED;
      }

      else {
        this.color = PlayerColor.BLUE;
      }

    }

    @Override
    public void drawCard(PawnsGame game) {

    }

    @Override
    public PlayerColor getColor() {
      return null;
    }
  }
}
