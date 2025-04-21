package pawnboard.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;
import pawnboard.controller.PlayerActionListener;

/**
 * Represents a high contrast board panel.
 */
public class HighContrastBoardPanel extends BoardPanel {
  private final Dimension logicalSize = new Dimension(800, 600);
  private final ReadonlyPawnsGame<PawnCell, Player, PawnCard> model;
  private int selectedRow = -1;
  private int selectedCol = -1;
  private final int rows;
  private final int cols;
  private PlayerActionListener playerActionListener;
  private HandPanel handPanel;


  /**
   * Represents a constructor for a high contrast board panel.
   * @param model the current game state
   * @param handPanel the hand panel
   */
  public HighContrastBoardPanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard>
                                        model, HandPanel handPanel) {
    super(model, handPanel);
    this.handPanel = handPanel;
    this.model = model;
    this.rows = model.getHeight();
    this.cols = model.getWidth();

    model.addObserver(this);

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


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    g2d.transform(transformLogicalToPhysical());

    g2d.setColor(Color.BLACK);
    g2d.fillRect(0, 0, logicalSize.width, logicalSize.height);

    drawGridLines(g2d);
    drawPawnsOnBoard(g2d);
    drawCardOnBoard(g2d);

    // Highlight selected cell
    if (selectedRow != -1 && selectedCol != -1) {
      highlightCellHelper(g2d, selectedRow, selectedCol);
    }
  }

  private void drawPawnsOnBoard(Graphics2D g2d) {
    double cellWidth = logicalSize.getWidth() / model.getWidth();
    double cellHeight = logicalSize.getHeight() / model.getHeight();

    for (int row = 0; row < model.getHeight(); row++) {
      for (int col = 0; col < model.getWidth(); col++) {
        PawnCell cell = model.getCellAt(row, col);
        int pawnCount = cell.getPawnCount().getValue();

        if (pawnCount > 0 && cell.getOwner() != null) {
          Color pawnColor = (cell.getOwner() == Player.RED) ? Color.RED : Color.CYAN;
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
          Color cardColor = (cell.getOwner() == Player.RED) ? Color.RED : Color.CYAN;
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
    g2d.setColor(Color.YELLOW);
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

  private AffineTransform transformLogicalToPhysical() {
    AffineTransform xform = new AffineTransform();
    double scaleX = getWidth() / (double) logicalSize.width;
    double scaleY = getHeight() / (double) logicalSize.height;
    xform.scale(scaleX, scaleY);
    return xform;
  }

  private void drawGridLines(Graphics2D g2d) {
    g2d.setColor(Color.LIGHT_GRAY);

    // Draw horizontal lines
    for (int row = 0; row <= rows; row++) {
      drawLine(g2d, new Point(0, row), new Point(cols, row));
    }

    // Draw vertical lines
    for (int col = 0; col <= cols; col++) {
      drawLine(g2d, new Point(col, 0), new Point(col, rows));
    }
  }

  private void drawLine(Graphics2D g2d, Point2D src, Point2D dst) {
    Point2D logicalSrc = transformModelToLogical().transform(src, null);
    Point2D logicalDst = transformModelToLogical().transform(dst, null);

    g2d.drawLine((int) logicalSrc.getX(), (int) logicalSrc.getY(),
            (int) logicalDst.getX(), (int) logicalDst.getY());
  }

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


    if (clickedRow == selectedRow && clickedCol == selectedCol) {
      clearHighlight();
    } else {
      System.out.println("Clicked cell at: (" + clickedRow + ", " + clickedCol + ")");
      highlightCell(clickedRow, clickedCol);
    }

    gameStateChanged();

    if (playerActionListener != null) {
      playerActionListener.onCellClick(clickedRow, clickedCol);
    }
  }

  private void handleKeyPress(KeyEvent e) {
    if (e.getKeyChar() == 'c' || e.getKeyChar() == 'C') {
      if (this.isHighlighted() && handPanel.isHighlighted()) {
        System.out.println("Move confirmed");
        gameStateChanged();

        if (playerActionListener != null) {
          playerActionListener.onCardClick(handPanel.getSelectedCardIndex());
          playerActionListener.onConfirmTurn();
        }
      } else {
        System.out.println("Invalid confirmation: Ensure a card and a board cell are selected.");
      }
    } else if (e.getKeyChar() == 'p' || e.getKeyChar() == 'P') {
      System.out.println("Pass turn.");
      gameStateChanged();

      if (playerActionListener != null) {
        playerActionListener.onPassTurn();
      }
    }
  }


  private void highlightCell(int row, int col) {
    this.selectedRow = row;
    this.selectedCol = col;
    gameStateChanged();
  }

  private void clearHighlight() {
    this.selectedRow = -1;
    this.selectedCol = -1;
    gameStateChanged();
  }




}
