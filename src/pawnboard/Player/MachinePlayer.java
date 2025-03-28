package pawnboard.Player;

import java.util.List;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.strategy.Move;
import pawnboard.strategy.PawnBoardStrategy;

/**
 * Represents an AI-controlled player in the game.
 * Uses strategy to decide best move based on the current
 * game.
 */
public class MachinePlayer implements UserPlayer {
  private final Player player;
  private final PawnsGame<PawnCell, Player, PawnCard> gameModel;
  private final PawnBoardStrategy<PawnCell, Player, PawnCard> strategy;

  /**
   * Constructs a new MachinePlayer.
   * @param player the player (Red or Blue)
   * @param gameModel the game model that holds the current game state
   * @param strategy the strategy used by the machine player to make moves
   */
  public MachinePlayer(Player player, PawnsGame<PawnCell, Player, PawnCard> gameModel,
                       PawnBoardStrategy<PawnCell, Player, PawnCard> strategy) {
    this.player = player;
    this.gameModel = gameModel;
    this.strategy = strategy;
  }

  @Override
  public void makeMove() {
    List<Move<PawnCard>> moves = strategy.chooseMove(gameModel, player);

    if (moves.isEmpty()) {
      gameModel.passTurn();
    }

    Move<PawnCard> move = moves.get(0);

    PawnCard card = move.getCard();
    int row = move.getRow();
    int col = move.getCol();

    List<PawnCard> playerHand = gameModel.getHand(player);
    int cardIdx = findCardIndex(playerHand, card);

    try {
      gameModel.placeCardAt(player, cardIdx, row, col);
    } catch (Exception e) {
      // do not execute the move if you can't
    }
  }

  private int findCardIndex(List<PawnCard> hand, PawnCard card) {
    for (int i = 0; i < hand.size(); i++) {
      if (hand.get(i).equals(card)) {
        return i;
      }
    }
    return -1;
  }
}
