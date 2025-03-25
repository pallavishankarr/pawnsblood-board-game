package pawnboard.strategy;

import java.util.ArrayList;
import java.util.List;

import pawnboard.model.Card;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class FillFirstStrategy implements PawnBoardStrategy {
  public List<Move> chooseMove(PawnsGame<PawnCell> model, Player player) {
    List<PawnCard> hand = model.getHand(player);
    List<Move> possibleMoves = new ArrayList<>();

    for (PawnCard card : hand) {
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

