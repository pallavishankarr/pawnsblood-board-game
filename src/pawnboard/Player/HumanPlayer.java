package pawnboard.Player;

import java.util.List;

import pawnboard.controller.PlayerActionListener;
import pawnboard.model.Card;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.view.PlayerActionPublisher;

public class HumanPlayer implements UserPlayer {
  private final Player player;
  private final PawnsGame pawnGame;
  private  PlayerActionPublisher playerActionPublisher;
  private  PlayerActionListener playerActionListener;
  private List<PawnCard> playerHand;

  public HumanPlayer(Player player, PawnsGame pawnGame){
    this.player = player;
    this.pawnGame = pawnGame;

  }

  @Override
  public void addPlayerActionListener(PlayerActionListener listener) {
    this.playerActionListener = listener;

  }

  @Override
  public void makeMove() {
    // Ensure it's this player's turn
    if (pawnGame.getCurrentPlayer() != player) {
      throw new IllegalStateException("It's not this player's turn.");
    }

    playerHand = pawnGame.getHand(player);

    if (playerHand.isEmpty()) {
      throw new IllegalStateException("No cards available to play.");
    }

    int cardIdx = getUserCardSelection();
    playerActionListener.onCardClick(cardIdx);

    if (!selectAndMakeMove(cardIdx)) {
      throw new IllegalStateException("No legal moves available.");
    }


  }

  @Override
  public Player getPlayer() {
    return player;
  }

  /**
   * A helper method to simulate getting the user's card selection.*/
  private int getUserCardSelection() {

    return 0;
  }

  /**
   * A helper method to simulate getting the user's key press ('c' or 'p').
   * This method should be replaced with actual user input capture.
   */
  private char getUserKeyPress() {
    return 'c';
  }


  private boolean selectAndMakeMove(int cardIdx) {
    for (int r = 0; r < pawnGame.getHeight(); r++) {
      for (int c = 0; c < pawnGame.getWidth(); c++) {
        PawnCard card = playerHand.get(cardIdx);
        if (pawnGame.placeCardLegal(player, card, r, c)) {
          // Notify the controller to select the valid move on the board
          playerActionListener.onCellClick(r, c);
          return true; // Legal move found
        }
      }
    }
    return false;
  }




}
