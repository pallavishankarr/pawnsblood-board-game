package pawnboard.strategy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pawnboard.model.Card;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class MaximizeRowScoreStrategy implements PawnBoardStrategy {
  @Override
  public List<Move> chooseMove(PawnsGame<PawnCell> model, Player player) {
    List<Card> hand = model.getHand(player);
    Player opponent = (player == Player.RED) ? Player.BLUE : Player.RED;
    List<Move> bestMoves = new ArrayList<>();

    // Store legal moves to avoid rechecking the same legality
    Set<String> legalMovesCache = new HashSet<>();

    for (int r = 0; r < model.getHeight(); r++) {
      int playerRowScore = model.checkScoreRow(player, r);
      int opponentRowScore = model.checkScoreRow(opponent, r);

      if (playerRowScore > opponentRowScore) {
        continue; // Skip rows where the player's score is already higher than the opponent's.
      }

      for (Card card : hand) {
        boolean moveMade = false; // Flag to check if a valid move was found for this card

        for (int c = 0; c < model.getWidth(); c++) {
          String moveKey = r + "," + c; // Create a unique key for the position

          if (legalMovesCache.contains(moveKey)) {
            continue; // Skip if this legality check has already been done
          }

          // Check if placing the card in this row and column is legal
          if (model.placeCardLegal(player, card, r, c)) {
            int newScore = getHypotheticalRowScore(model, player, card, r, c);

            if (newScore > opponentRowScore) {
              bestMoves.add(new Move(card, r, c));
              legalMovesCache.add(moveKey); // Cache the legality check
              moveMade = true; // Mark the move as made
              break; // Break after the first valid move is found for this card
            }
          }

          // Cache the legality of this position to avoid redundant checks
          legalMovesCache.add(moveKey);
        }

        // If a valid move was found for this card, no need to check further for this card
        if (moveMade) {
          break;
        }
      }

      // If we found any valid moves, we can exit early
      if (!bestMoves.isEmpty()) {
        break; // Exit row loop once we find the first best move
      }
    }

    return bestMoves; // Return the best moves found
  }

  private int getHypotheticalRowScore(ReadonlyPawnsGame<PawnCell> model, Player player, Card card, int row, int col) {
    if (!model.placeCardLegal(player, card, row, col)) {
      return model.checkScoreRow(player, row);
    }
    int currentScore = model.checkScoreRow(player, row);
    int cardValue = card.getValue();
    return currentScore + cardValue;
  }

}
