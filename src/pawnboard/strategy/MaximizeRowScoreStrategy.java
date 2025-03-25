package pawnboard.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pawnboard.model.Card;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class MaximizeRowScoreStrategy implements PawnBoardStrategy {
  @Override
  public List<Move> chooseMove(PawnsGame<PawnCell> model, Player player) {
    List<PawnCard> hand = model.getHand(player);
    Player opponent = (player == Player.RED) ? Player.BLUE : Player.RED;
    List<Move> bestMoves = new ArrayList<>();

    Set<String> legalMovesCache = new HashSet<>();

    for (int r = 0; r < model.getHeight(); r++) {
      int playerRowScore = model.checkScoreRow(player, r);
      int opponentRowScore = model.checkScoreRow(opponent, r);

      if (playerRowScore > opponentRowScore) {
        continue;
      }

      for (PawnCard card : hand) {
        boolean moveMade = false;

        for (int c = 0; c < model.getWidth(); c++) {
          String moveKey = r + "," + c;

          if (legalMovesCache.contains(moveKey)) {
            continue;
          }

          if (model.placeCardLegal(player, card, r, c)) {
            int newScore = getHypotheticalRowScore(model, player, card, r, c);

            if (newScore > opponentRowScore) {
              bestMoves.add(new Move(card, r, c));
              legalMovesCache.add(moveKey);
              moveMade = true;
              break;
            }
          }

          legalMovesCache.add(moveKey);
        }

        if (moveMade) {
          break;
        }
      }

      if (!bestMoves.isEmpty()) {
        break;
      }
    }
    return bestMoves;
  }

  private int getHypotheticalRowScore(ReadonlyPawnsGame<PawnCell> model, Player player, Card card, int row, int col) {
    int currentScore = model.checkScoreRow(player, row);
    int cardValue = card.getValue();
    return currentScore + cardValue;
  }

}

