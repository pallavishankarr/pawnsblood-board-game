package pawnboard.strategy;

import java.util.Collections;
import java.util.List;

import pawnboard.model.InfluenceType;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

/**
 * A strategy for selecting moves in Pawns Board game by maximizing the number of owned cells.
 * If multiple positions yield the same number of controlled cells,
 * the uppermost-leftmost position is chosen.
 * If multiple cards yield the same number of controlled cells, the leftmost card in hand is chosen.
 **/
public class MaxControlStrategy implements PawnBoardStrategy<PawnCell, Player, PawnCard> {
  @Override
  public List<Move<PawnCard>> chooseMove(PawnsGame<PawnCell, Player,
          PawnCard> model, Player player) {
    if (model == null || player == null) {
      throw new IllegalArgumentException("Model and player cannot be null.");
    }
    if (model.isGameOver()) {
      throw new IllegalStateException("Cannot choose a move when the game is over.");
    }

    List<PawnCard> hand = model.getHand(player);
    Move<PawnCard> bestMove = null;
    int maxControlledCells = -1;

    for (PawnCard card : hand) {
      for (int row = 0; row < model.getHeight(); row++) {
        for (int col = 0; col < model.getWidth(); col++) {
          if (model.placeCardLegal(player, card, row, col)) {
            int controlledCells = countControlledCells(model, player, card, row, col);
            if (isBetterMove(bestMove, card, row, col, controlledCells, maxControlledCells)) {
              bestMove = new Move<PawnCard>(card, row, col);
              maxControlledCells = controlledCells;
            }
          }
        }
      }
    }

    return bestMove != null ? List.of(bestMove) : Collections.emptyList();
  }

  private int countControlledCells(PawnsGame<PawnCell, Player, PawnCard> model,
                                   Player player, PawnCard card, int row, int col) {
    int capturedCells = 0;
    InfluenceType[][] influenceGrid = card.getInfluenceGrid();
    Player opponent = (player == Player.RED) ? Player.BLUE : Player.RED;

    for (int rOffset = -2; rOffset <= 2; rOffset++) {
      for (int cOffset = -2; cOffset <= 2; cOffset++) {
        int newRow = row + rOffset;
        int newCol = col + cOffset;

        if (newRow >= 0 && newRow < model.getHeight() && newCol >= 0 && newCol < model.getWidth()) {
          if (influenceGrid[rOffset + 2][cOffset + 2] == InfluenceType.C &&
                  model.getOwnerAt(newRow, newCol) == opponent) {
            capturedCells++;
          }
        }
      }
    }
    return capturedCells;
  }

  private boolean isBetterMove(Move<PawnCard> bestMove, PawnCard newCard,
                               int newRow, int newCol, int newControlledCells,
                               int maxControlledCells) {
    if (newControlledCells > maxControlledCells) {
      return true;
    }
    if (newControlledCells == maxControlledCells) {
      return bestMove == null || newRow < bestMove.getRow() ||
              (newRow == bestMove.getRow() && newCol < bestMove.getCol());
    }
    return false;
  }
}
