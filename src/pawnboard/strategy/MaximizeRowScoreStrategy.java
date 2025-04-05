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

/**
 * A strategy for selecting moves in Pawns Board game to maximize the row score.
 * This strategy prioritizes placing cards in rows the opponent has equal or greater control
 * and tries to surpass their score. Selects the first possible card and row to accomplish
 * this task.
 */
public class MaximizeRowScoreStrategy implements PawnBoardStrategy<PawnCell, Player, PawnCard> {

  @Override
  public List<Move<PawnCard>> chooseMove(ReadonlyPawnsGame<PawnCell,
          Player, PawnCard> model, Player player) {
    if (model == null || player == null) {
      throw new IllegalArgumentException("Model and player cannot be null.");
    }
    if (model.isGameOver()) {
      throw new IllegalStateException("Cannot choose a move when the game is over.");
    }
    List<PawnCard> hand = model.getHand(player);
    Player opponent = (player == Player.RED) ? Player.BLUE : Player.RED;
    List<Move<PawnCard>> bestMoves = new ArrayList<>();
    for (int r = 0; r < model.getHeight(); r++) {
      int playerRowScore = model.checkScoreRow(player, r);
      int opponentRowScore = model.checkScoreRow(opponent, r);
      if (playerRowScore > opponentRowScore) {
        continue;
      }
      for (PawnCard card : hand) {
        Set<String> legalMovesCache = new HashSet<>();
        if (tryPlacingCard(model, player, card, r, opponentRowScore, bestMoves, legalMovesCache)) {
          return bestMoves;
        }
      }
    }
    return bestMoves;
  }

  private boolean tryPlacingCard(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model, Player player,
                                 PawnCard card, int row, int opponentRowScore,
                                 List<Move<PawnCard>> bestMoves, Set<String> legalMovesCache) {
    for (int col = 0; col < model.getWidth(); col++) {
      String moveKey = row + "," + col;
      if (legalMovesCache.contains(moveKey)) {
        continue;
      }
      if (model.placeCardLegal(player, card, row, col)) {
        int newScore = getHypotheticalRowScore(model, player, card, row, col);
        if (newScore > opponentRowScore) {
          bestMoves.add(new Move<PawnCard>(card, row, col));
          legalMovesCache.add(moveKey);
          return true;
        }
      }
      legalMovesCache.add(moveKey);
    }
    return false;
  }

  private int getHypotheticalRowScore(ReadonlyPawnsGame<PawnCell,
          Player, PawnCard> model, Player player, Card card, int row, int col) {
    if (card == null) {
      throw new IllegalArgumentException("Card cannot be null.");
    }
    if (row < 0 || row >= model.getHeight() || col < 0 || col >= model.getWidth()) {
      throw new IllegalArgumentException("Row or column index out of bounds.");
    }

    int currentScore = model.checkScoreRow(player, row);
    int cardValue = card.getValue();
    return currentScore + cardValue;
  }
}

