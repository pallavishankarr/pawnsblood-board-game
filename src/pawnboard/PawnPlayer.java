package pawnboard;

import java.util.List;

public class PawnPlayer implements Player {

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
