package pawnboard.view;

import java.awt.*;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class HighContrastBoardPanel extends BoardPanel {
  ReadonlyPawnsGame<PawnCell, Player, PawnCard> model;

  public HighContrastBoardPanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model, HandPanel handPanel) {
    super(model, handPanel);
  }
  @Override
  protected void drawPawnsOnBoard(Graphics2D g2d) {
    double cellWidth = super.getLogicalSize().getWidth() / model.getWidth();
    double cellHeight = super.getLogicalSize().getHeight() / model.getHeight();

    for (int row = 0; row < model.getHeight(); row++) {
      for (int col = 0; col < model.getWidth(); col++) {
        PawnCell cell = model.getCellAt(row, col);
        int pawnCount = cell.getPawnCount().getValue();

        if (pawnCount > 0 && cell.getOwner() != null) {
          Color pawnColor = (cell.getOwner() == Player.RED) ? Color.YELLOW : Color.CYAN;
          drawPawn(g2d, col, row, cellWidth, cellHeight, pawnColor, pawnCount);
        }
      }
    }
  }

  @Override
  protected void drawCardOnBoard(Graphics2D g2d) {
    double cellWidth = getLogicalSize().getWidth() / getCols();
    double cellHeight = getLogicalSize().getHeight() / getRows();

    for (int row = 0; row < getRows(); row++) {
      for (int col = 0; col < getCols(); col++) {
        PawnCell cell = getModel().getCellAt(row, col);
        if (cell.hasCard()) {
          Color cardColor = (cell.getOwner() == Player.RED) ? Color.YELLOW : Color.CYAN;
          drawCard(g2d, row, col, cardColor, cell.getCard().getValue(), cellWidth, cellHeight);
        }
      }
    }
  }

  @Override
  protected void highlightCellHelper(Graphics2D g2d, int row, int col) {
    double cellWidth = getLogicalSize().getWidth() / getCols();
    double cellHeight = getLogicalSize().getHeight() / getRows();
    double x = col * cellWidth;
    double y = row * cellHeight;
    g2d.setColor(new Color(255, 255, 0, 192)); // Brighter yellow for visibility
    g2d.fillRect((int) x, (int) y, (int) cellWidth, (int) cellHeight);
  }


}
