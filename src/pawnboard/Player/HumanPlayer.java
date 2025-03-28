package pawnboard.Player;

import java.util.List;

import pawnboard.controller.PlayerActionListener;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

public class HumanPlayer implements UserPlayer {
  private final Player player;
  private final PawnsGame pawnGame;
  private final PlayerActionListener playerActionListener;

  public HumanPlayer(Player player, PawnsGame pawnGame, PlayerActionListener playerActionListener) {
    this.player = player;
    this.pawnGame = pawnGame;
    this.playerActionListener = playerActionListener;
  }
<<<<<<< Updated upstream
  private final

=======


  @Override
  public void makeMove() {
    if (pawnGame.getCurrentPlayer() != player) {
      throw new IllegalStateException("It's not this player's turn.");
    }

    List<PawnCard> playerHand = pawnGame.getHand(player);

    if (playerHand.isEmpty()) {
      throw new IllegalStateException("No cards available to play.");
    }

    // Auto-select the first card (can be replaced with actual user input logic)
    int cardIdx = 0;
    playerActionListener.onCardClick(cardIdx);

    int selectedRow = -1;
    int selectedCol = -1;

    // Loop to find a legal move for the selected card
    for (int r = 0; r < pawnGame.getHeight(); r++) {
      for (int c = 0; c < pawnGame.getWidth(); c++) {
        if (pawnGame.placeCardLegal(player, playerHand.get(cardIdx), r, c)) {
          selectedRow = r;
          selectedCol = c;
          break;
        }
      }
      if (selectedRow != -1) break;
    }

    if (selectedRow == -1 || selectedCol == -1) {
      throw new IllegalStateException("No legal moves available.");
    }

    // Select the board cell
    playerActionListener.onCellClick(selectedRow, selectedCol);

    // Now we need to wait for the user's confirmation (check for a key press)
    char keyPress = getKeyPressFromInput();

    if (keyPress == 'c' || keyPress == 'C') {  // Confirm Move
      playerActionListener.onConfirmTurn();
    } else if (keyPress == 'p' || keyPress == 'P') {  // Pass Turn
      playerActionListener.onPassTurn();
    } else {
      throw new IllegalArgumentException("Invalid key press. Use 'c' to confirm, 'p' to pass.");
    }




  }

  private char getKeyPressFromInput() {
    return 'c';
  }
>>>>>>> Stashed changes
}
