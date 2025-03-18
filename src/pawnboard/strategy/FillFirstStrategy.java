package pawnboard.strategy;

import java.util.List;

import pawnboard.model.Card;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class FillFirstStrategy implements PawnBoardStrategy {
  public Move chooseMove(ReadonlyPawnsGame<PawnCell> model, Player player) {
    List<Card> hand = model.getHand(player);

    for (Card card : hand) {
      for (int r = 0; r < model.getHeight(); r++) {
        for (int c = 0; c < model.getWidth(); c++) {
          if (model.placeCardLegal(player, card, r, c)) {
            return new Move(card, r, c);
          }
        }
      }
    }

    return null;
  }
}

