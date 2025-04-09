package pawnboard.view;

import java.awt.*;

import java.awt.Dimension;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class HighContrastBoardPanel extends BoardPanel {
  private final Dimension logicalSize = new Dimension(800, 600);

  ReadonlyPawnsGame<PawnCell, Player, PawnCard> model;

  public HighContrastBoardPanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model, HandPanel handPanel) {
    super(model, handPanel);
  }


  private void drawPawnsOnBoard(Graphics2D g2d) {
    double cellWidth = logicalSize.getWidth() / model.getWidth();
    double cellHeight = logicalSize.getHeight() / model.getHeight();

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

  private void drawCardOnBoard(Graphics2D g2d) {
    double cellWidth = logicalSize.getWidth() / model.getWidth();
    double cellHeight = logicalSize.getHeight() / model.getHeight();

    for (int row = 0; row < model.getHeight(); row++) {
      for (int col = 0; col < model.getWidth(); col++) {
        PawnCell cell = model.getCellAt(row, col);
        if (cell.hasCard()) {
          Color cardColor = (cell.getOwner() == Player.RED) ? Color.YELLOW : Color.CYAN;
          drawCard(g2d, row, col, cardColor, cell.getCard().getValue(), cellWidth, cellHeight);
        }
      }
    }
  }

  private void highlightCellHelper(Graphics2D g2d, int row, int col) {
    double cellWidth = logicalSize.getWidth() / model.getWidth();
    double cellHeight = logicalSize.getHeight() / model.getHeight();
    double x = col * cellWidth;
    double y = row * cellHeight;
    g2d.setColor(new Color(255, 255, 0, 192)); // Brighter yellow for visibility
    g2d.fillRect((int) x, (int) y, (int) cellWidth, (int) cellHeight);
  }


  private void drawPawn(Graphics2D g2d, int col, int row,
                        double cellWidth, double cellHeight, Color color, int count) {
    g2d.setColor(color);

    // center of cell
    double centerX = (col + 0.5) * cellWidth;
    double centerY = (row + 0.5) * cellHeight;

    // base size of pawn
    double baseSize = Math.min(cellWidth, cellHeight) * 0.1;
    double spacing = baseSize * 1.5;

    double[][] offsets;
    switch (count) {
      case 1:
        offsets = new double[][]{{0, 0}};
        break;
      case 2:
        offsets = new double[][]{{-spacing / 2, 0}, {spacing / 2, 0}};
        break;
      case 3:
        offsets = new double[][]{{-spacing / 2, -spacing / 2},
                {spacing / 2, -spacing / 2},
                {0, spacing / 2}};
        break;
      default:
        return;
    }

    for (double[] offset : offsets) {
      double pawnX = centerX + offset[0] - (baseSize / 2);
      double pawnY = centerY + offset[1] - (baseSize / 2);
      g2d.fillOval((int) Math.round(pawnX), (int) Math.round(pawnY),
              (int) Math.round(baseSize), (int) Math.round(baseSize));
    }
  }

  private void drawCard(Graphics2D g2d, int row, int col, Color color, int value,
                        double cellWidth, double cellHeight) {
    double x = col * cellWidth;
    double y = row * cellHeight;

    g2d.setColor(color);
    g2d.fillRect((int) x, (int) y, (int) cellWidth, (int) cellHeight);

    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.BOLD, (int) (cellHeight * 0.4))); // Smaller font

    String valueText = String.valueOf(value);
    FontMetrics metrics = g2d.getFontMetrics();

    int textX = (int) (x + (cellWidth - metrics.stringWidth(valueText)) / 2);
    int textY = (int) (y + (cellHeight + metrics.getAscent() - metrics.getDescent()) / 2);

    g2d.drawString(valueText, textX, textY);
  }


}
