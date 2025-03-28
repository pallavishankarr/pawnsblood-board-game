package pawnboard.Player;

import pawnboard.controller.PlayerActionListener;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

public class HumanPlayer {
  private final Player player;
  private final PawnsGame pawnGame;
  private final PlayerActionListener playerActionListener;

  public HumanPlayer(Player player, PawnsGame pawnGame, PlayerActionListener playerActionListener) {
    this.player = player;
    this.pawnGame = pawnGame;
    this.playerActionListener = playerActionListener;
  }
}
