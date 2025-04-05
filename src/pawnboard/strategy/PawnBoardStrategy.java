package pawnboard.strategy;

import java.util.List;
import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

/**
 * Represents a strategy for selecting moves in the Pawns Board game.
 * Implementations of this interface define how a move is
 * chosen based on the current game state.
 * @param <C> the type of cell on the board
 * @param <P> the type of player
 * @param <T> the type of card the game plays with
 */
public interface PawnBoardStrategy<C extends Cell<P, T>, P extends Player, T extends Card> {
  /**
   * Chooses list of possible moves for the given player based on the game state.
   * The strategy determines the best or valid move that can be made.
   * If there is no best move, an empty list will be returned.
   * @param model a non-mutable version of the model, must be a copy of the current pawnsgame model
   * @param player the player for whom the move is being chosen
   * @return a list of valid moves for the player; an empty list if no moves are possible
   * @throws IllegalArgumentException if the model or player is null
   * @throws IllegalStateException if the game is over
   */
  List<Move<T>> chooseMove(ReadonlyPawnsGame<C, P, T> model, P player);
}
