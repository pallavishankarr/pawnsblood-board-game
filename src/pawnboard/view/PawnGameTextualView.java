package pawnboard.view;

import pawnboard.model.Card;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;

/**
 * Represents the textual view of a pawn game.
 */
public class PawnGameTextualView implements GameView {
  private final PawnsGame<PawnCell, Player, PawnCard> model;

  /**
   * Creates a PawnGameTextualView object.
   *
   * @param model the model of the game
   */
  public PawnGameTextualView(PawnsGame<PawnCell, Player, PawnCard> model) {
    this.model = model;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int row = 0; row < model.getHeight(); row++) {
      sb.append(model.checkScoreRow(Player.RED, row)).append(" ");

      for (int col = 0; col < model.getWidth(); col++) {
        PawnCell cell = model.getCellAt(row, col);
        StringBuilder cellRepresentation = new StringBuilder();

        if (cell.getPawnCount().getValue() > 0) {
          cellRepresentation.append(cell.getPawnCount());
        }

        if (cell.hasCard()) {
          Card card = cell.getCard();
          String owner = (cell.getOwner() == Player.RED) ? "R" : "B";
          cellRepresentation.append(owner);
        } else if (cell.getPawnCount().getValue() == 0) {
          cellRepresentation.append("_");
        }

        sb.append(cellRepresentation);

        if (col < model.getWidth() - 1) {
          sb.append(" ");
        }
      }

      sb.append(" ").append(model.checkScoreRow(Player.BLUE, row));

      sb.append("\n");
    }

    return sb.toString();
  }
}
