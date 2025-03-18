package pawnboard.view;

import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;

import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class BlueScorePanel extends JPanel {
  private final ReadonlyPawnsGame<PawnCell> model;
  private final Dimension logicalSize = new Dimension(100, 600); // Logical reference for scaling

  public BlueScorePanel(ReadonlyPawnsGame<PawnCell> model) {
    this.model = model;
    setPreferredSize(new Dimension(100, 600)); // Ensures correct width
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    // Apply logical -> physical scaling only if valid
    if (getWidth() > 0 && getHeight() > 0) {
      g2d.transform(transformLogicalToPhysical());
    }

    // Background color
    g2d.setColor(Color.LIGHT_GRAY);
    g2d.fillRect(0, 0, logicalSize.width, logicalSize.height);

    drawRowScores(g2d);
  }

  private void drawRowScores(Graphics2D g2d) {
    int rows = model.getHeight();
    double rowHeight = logicalSize.getHeight() / rows;

    for (int row = 0; row < rows; row++) {
      int score = model.checkScoreRow(Player.RED, row); // Get Red player's row score

      // Center text in the row
      double textX = logicalSize.getWidth() / 2;
      double textY = (row + 0.5) * rowHeight;

      g2d.setColor(Color.BLACK);
      g2d.setFont(new Font("SansSerif", Font.BOLD, 16)); // Adjust font size for visibility
      drawCenteredString(g2d, String.valueOf(score), textX, textY);
    }
  }

  private void drawCenteredString(Graphics2D g2d, String text, double x, double y) {
    FontMetrics metrics = g2d.getFontMetrics();
    int textWidth = metrics.stringWidth(text);
    int textHeight = metrics.getAscent();

    g2d.drawString(text, (float) (x - textWidth / 2), (float) (y + textHeight / 2));
  }

  private AffineTransform transformLogicalToPhysical() {
    double scaleX = getWidth() / (double) logicalSize.width;
    double scaleY = getHeight() / (double) logicalSize.height;
    AffineTransform xform = new AffineTransform();
    xform.scale(scaleX, scaleY);
    return xform;
  }
}