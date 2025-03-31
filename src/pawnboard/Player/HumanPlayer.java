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

    if (pawnGame.getCurrentPlayer() != player) {
      throw new IllegalStateException("It's not this player's turn.");
    }


   playerHand = pawnGame.getHand(player);

    if (playerHand.isEmpty()) {
      throw new IllegalStateException("No cards available to play.");
    }

    int cardIdx = 0;
    playerActionListener.onCardClick(cardIdx);

    PawnCard card = playerHand.get(cardIdx);

    int row = 0;
    int col = 0;
    playerActionListener.onCellClick(row, col);



    if(pawnGame.placeCardLegal(player,card, row, col)) {
      playerActionListener.onConfirmTurn();
      pawnGame.placeCardAt(player, cardIdx, row, col);
    }
    else {
      throw new IllegalStateException("Cannot place card");
    }


  }

  @Override
  public Player getPlayer() {
    return player;
  }




}
