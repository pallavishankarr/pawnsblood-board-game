package pawnboard.strategy;
import java.util.List;

import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

/**
 * Represents a strategy for selecting moves in the Pawns Board game.
 * Implementations of this interface define how a move is
 * chosen based on the current game state.
 */
public interface PawnBoardStrategy<C extends Cell<P, T>, P extends Player, T extends Card> {
  /**
   * Chooses list of possible moves for the given player based on the game state.
   * The strategy determines the best or valid move that can be made.
   * If there is no best move, an empty list will be returned.
   * @param model the current game model
   * @param player the player for whom the move is being chosen
   * @return a list of valid moves for the player; an empty list if no moves are possible
   * @throws IllegalArgumentException if the model or player is null
   * @throws IllegalStateException if the game is over
   */
  List<Move<T>> chooseMove(PawnsGame<C, P, T> model, P player);
}
