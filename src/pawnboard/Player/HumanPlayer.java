package pawnboard.Player;

import java.util.List;

import pawnboard.controller.PlayerActionListener;
import pawnboard.model.Card;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.view.PlayerActionPublisher;

public class HumanPlayer implements UserPlayer, PlayerActionPublisher {
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

    // Get the current hand of the player
    playerHand = pawnGame.getHand(player);

    // Check if the player has cards available to play
    if (playerHand.isEmpty()) {
      throw new IllegalStateException("No cards available to play.");
    }

    // Simulating player card selection (this would be replaced by actual user input logic)
    int cardIdx = getUserCardSelection();
    playerActionListener.onCardClick(cardIdx);

    // Check for valid moves after selecting the card
    if (!selectAndMakeMove(cardIdx)) {
      throw new IllegalStateException("No legal moves available.");
    }

    // Confirm the move or pass the turn
    char keyPress = getUserKeyPress();  // This should capture the actual key press
    if (keyPress == 'c' || keyPress == 'C') {
      playerActionListener.onConfirmTurn();
    } else if (keyPress == 'p' || keyPress == 'P') {
      playerActionListener.onPassTurn();
    } else {
      throw new IllegalArgumentException("Invalid key press. Use 'c' to confirm, 'p' to pass.");
    }
  }

  @Override
  public Player getPlayer() {
    return player;
  }

  /**
   * A helper method to simulate getting the user's card selection.
   * In a real implementation, this would be replaced by an actual user input mechanism.
   */
  private int getUserCardSelection() {
    // Placeholder for actual user input. For now, we'll select the first card.
    return 0;  // Assuming the first card is selected.
  }

  /**
   * A helper method to simulate getting the user's key press ('c' or 'p').
   * This method should be replaced with actual user input capture.
   */
  private char getUserKeyPress() {
    // Placeholder for actual user input. Simulating 'c' for confirm.
    return 'c';  // Example: User presses 'c' to confirm.
  }

  /**
   * Try to find a legal move for the given card.
   * If a legal move is found, trigger the cell click action.
   */
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
