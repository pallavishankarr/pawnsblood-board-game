package pawnboard.player;

import java.util.List;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

/**
 * Represents a human player in the PawnsBoard game.
 * Interacts with the game through the GUI.
 */
public class HumanPlayer implements UserPlayer {
  private final Player player;
  private List<PawnCard> playerHand;

  /**
   * Constructs a HumanPlayer object.
   * @param player the player color they play in the game
   * @param pawnGame the model of the game
   */
  public HumanPlayer(Player player, PawnsGame<PawnCell, Player, PawnCard> pawnGame ) {
    this.player = player;

  }

  @Override
  public void makeMove() {
    //Empty because the controller handles placing the card
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  @Override
  public boolean isMachine() {
    return false;
  }


}
