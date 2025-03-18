package pawnboard.strategy;

import java.util.List;

import pawnboard.model.Card;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class MaximizeRowScoreStrategy implements PawnBoardStrategy {
  @Override
  public Move chooseMove(ReadonlyPawnsGame<PawnCell> model, Player player) {
    List<Card> hand = model.getHand(player);
    Player opponent = (player == Player.RED) ? Player.BLUE : Player.RED;

    for (int r = 0; r < model.getHeight(); r++) {
      int playerRowScore = model.checkScoreRow(player, r);
      int opponentRowScore = model.checkScoreRow(opponent, r);

      if (playerRowScore <= opponentRowScore) {
        for (Card card : hand) {
          for (int c = 0; c < model.getWidth(); c++) {
            if (model.placeCardLegal(player, card, r, c)) {
              int newScore = getHypotheticalRowScore(model, player, r, card, c);

              if (newScore >= opponentRowScore) {
                return new Move(card, r, c);
              }
            }
          }
        }
      }
    }

    // No valid move found
    return null;
  }

  private int getHypotheticalRowScore(ReadonlyPawnsGame<PawnCell> model, Player player, int row, Card card, int col) {
    if (!model.placeCardLegal(player, card, row, col)) {
      return model.checkScoreRow(player, row);
    }

    int currentScore = model.checkScoreRow(player, row);
    int cardValue = card.getValue();

    return currentScore + cardValue;
  }

}
