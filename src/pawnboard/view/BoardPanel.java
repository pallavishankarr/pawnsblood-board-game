package pawnboard.view;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;

import javax.swing.*;

import pawnboard.model.Cell;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class BoardPanel extends JPanel implements HighlightPanelView {
  private final int rows;
  private final int cols;
  private final ReadonlyPawnsGame<PawnCell> model;
  private final Dimension logicalSize = new Dimension(800, 600);
  private int selectedRow = -1;
  private int selectedCol = -1;

  public BoardPanel(ReadonlyPawnsGame<PawnCell> model) {
    super();
    this.model = model;
    rows = model.getHeight();
    cols = model.getWidth();

    // Add Mouse Listener
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        handleCellClick(e);
      }
    });
    
    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        handleKeyPress(e);
      }
    });
  }

 private void handleKeyPress(KeyEvent e) {
    if (e.getKeyChar() == 'c' || e.getKeyChar() == 'C') {
     if (selectedRow != -1 && selectedCol != -1 && cardSelected) {
       System.out.println("Move confirmed: Placing card at (" + selectedRow + ", " + selectedCol + ")");
      } else {
       System.out.println("Invalid confirmation: Ensure a card and a board cell are selected.");
     }
    }
  }

  @Override
  public void updateBoard() {
    repaint();
  }

  @Override
  public void highlightCell(int row, int col) {
    this.selectedRow = row;
    this.selectedCol = col;
    updateBoard();
  }

  @Override
  public void clearHighlight() {
    this.selectedRow = -1;
    this.selectedCol = -1;
    updateBoard();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    // Apply logical -> physical scaling
    g2d.transform(transformLogicalToPhysical());

    // Background color
    g2d.setColor(Color.DARK_GRAY);
    g2d.fillRect(0, 0, logicalSize.width, logicalSize.height);

    drawGridLines(g2d);
    drawPawnsOnBoard(g2d);
    drawCardOnBoard(g2d);

    // Highlight selected cell
    if (selectedRow != -1 && selectedCol != -1) {
      highlightCell(g2d, selectedRow, selectedCol);
    }
  }

  private void drawGridLines(Graphics2D g2d) {
    g2d.setColor(Color.BLACK);

    // Draw horizontal lines
    for (int row = 0; row <= rows; row++) {
      drawLine(g2d, new Point(0, row), new Point(cols, row));
    }

    // Draw vertical lines
    for (int col = 0; col <= cols; col++) {
      drawLine(g2d, new Point(col, 0), new Point(col, rows));
    }
  }

  private void drawPawnsOnBoard(Graphics2D g2d) {
    double cellWidth = logicalSize.getWidth() / cols;
    double cellHeight = logicalSize.getHeight() / rows;

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Cell cell = model.getCellAt(row, col);
        int pawnCount = cell.getPawnCount();

        if (pawnCount > 0) {
          Player owner = cell.getOwner();
          if (owner == null) {
            continue; // Skip drawing if owner is not assigned
          }

          Color pawnColor = (owner == Player.RED) ? Color.PINK : Color.BLUE;
          drawPawn(g2d, col, row, cellWidth, cellHeight, pawnColor, pawnCount);
        }
      }
    }
  }

  private void drawPawn(Graphics2D g2d, int col, int row, double cellWidth, double cellHeight, Color color, int count) {
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
                {0, spacing / 2}}; // Triangle formation
        break;
      default:
        return; // Should never happen since max is 3
    }

    for (double[] offset : offsets) {
      double pawnX = centerX + offset[0] - (baseSize / 2);
      double pawnY = centerY + offset[1] - (baseSize / 2);
      g2d.fillOval((int) Math.round(pawnX), (int) Math.round(pawnY), (int) Math.round(baseSize), (int) Math.round(baseSize));
    }
  }

  private void drawCardOnBoard(Graphics2D g2d) {
    double cellWidth = logicalSize.getWidth() / cols;
    double cellHeight = logicalSize.getHeight() / rows;

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        Cell cell = model.getCellAt(row, col);
        if (cell.hasCard()) {
          Color cardColor = (cell.getOwner() == Player.RED) ? Color.PINK : Color.BLUE;
          int value = cell.getCard().getValue();
          drawCard(g2d, row, col, cardColor, value, cellWidth, cellHeight);
        }
      }
    }
  }

  private void drawCard(Graphics2D g2d, int row, int col, Color color, int value, double cellWidth, double cellHeight) {
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

  private void drawLine(Graphics2D g2d, Point2D src, Point2D dst) {
    Point2D logicalSrc = transformModelToLogical().transform(src, null);
    Point2D logicalDst = transformModelToLogical().transform(dst, null);

    g2d.drawLine((int) logicalSrc.getX(), (int) logicalSrc.getY(),
            (int) logicalDst.getX(), (int) logicalDst.getY());
  }

  // Logical to physical
  private AffineTransform transformLogicalToPhysical() {
    AffineTransform xform = new AffineTransform();
    double scaleX = getWidth() / (double) logicalSize.width;
    double scaleY = getHeight() / (double) logicalSize.height;
    xform.scale(scaleX, scaleY);
    return xform;
  }

  // Logical to model transformation
  private AffineTransform transformModelToLogical() {
    AffineTransform xform = new AffineTransform();
    xform.scale(logicalSize.width / (double) cols, logicalSize.height / (double) rows);
    return xform;
  }

  private void handleCellClick(MouseEvent e) {
    double cellWidth = getWidth() / (double) cols;
    double cellHeight = getHeight() / (double) rows;

    int clickedCol = (int) (e.getX() / cellWidth);
    int clickedRow = (int) (e.getY() / cellHeight);

    System.out.println("Clicked cell at: (" + clickedRow + ", " + clickedCol + ")");

    highlightCell(clickedRow, clickedCol);
  }

  private void highlightCell(Graphics2D g2d, int row, int col) {
    double cellWidth = logicalSize.getWidth() / cols;
    double cellHeight = logicalSize.getHeight() / rows;
    double x = col * cellWidth;
    double y = row * cellHeight;
    g2d.setColor(new Color(0, 255, 255, 128));
    g2d.fillRect((int) x, (int) y, (int) cellWidth, (int) cellHeight);
  }
}
