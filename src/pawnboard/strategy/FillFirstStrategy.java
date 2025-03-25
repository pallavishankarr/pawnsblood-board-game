package pawnboard.strategy;

import java.util.ArrayList;
import java.util.List;

import pawnboard.model.Card;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

/**
 * A strategy that selects the first available legal move.
 *  * The strategy iterates through the player's hand and the board,
 *  * placing the first card that can be legally placed.
 */
public class FillFirstStrategy implements PawnBoardStrategy {
  public List<Move> chooseMove(PawnsGame<PawnCell> model, Player player) {
    if (model == null || player == null) {
      throw new IllegalArgumentException("Model and player cannot be null.");
    }
    if (model.isGameOver()) {
      throw new IllegalStateException("Cannot choose a move when the game is over.");
    }
    
    List<Card> hand = model.getHand(player);
    List<Move> possibleMoves = new ArrayList<>();

    for (Card card : hand) {
      for (int r = 0; r < model.getHeight(); r++) {
        for (int c = 0; c < model.getWidth(); c++) {
          if (model.placeCardLegal(player, card, r, c)) {
            possibleMoves.add(new Move(card, r, c));
            return possibleMoves;
          }
        }
      }
    }
    return possibleMoves;
  }
}

