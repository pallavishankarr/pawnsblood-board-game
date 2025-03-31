package pawnboard.Player;

import java.util.List;

import pawnboard.controller.PlayerActionListener;
import pawnboard.model.Card;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.view.PlayerActionPublisher;

public class HumanPlayer implements UserPlayer, SetController{
  private final Player player;
  private final PawnsGame<PawnCell, Player, PawnCard> pawnGame;
  private  PlayerActionPublisher playerActionPublisher;
  private  PlayerActionListener playerActionListener;
  private List<PawnCard> playerHand;

  public HumanPlayer(Player player, PawnsGame<PawnCell, Player, PawnCard> pawnGame ) {
    this.player = player;
    this.pawnGame = pawnGame;
    this.playerActionListener = null;

  }


  @Override
  public void setController(PlayerActionListener playerActionListener) {
    this.playerActionListener = playerActionListener;
  }



  @Override
  public void makeMove() {
    //Empty because the controller handles placing the card
  }

  @Override
  public Player getPlayer() {
    return player;
  }




}
