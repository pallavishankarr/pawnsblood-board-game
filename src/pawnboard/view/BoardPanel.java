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

import javax.swing.*;

import pawnboard.controller.PlayerActionListener;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

/**
 * A panel that represents the board of the game.
 * Updates board of game interactions, highlights cells, renders the game state.
 * This panel is a game observer, listens for the game state changes and user interactions
 * (mouse click and key presses).
 */
public class BoardPanel extends JPanel implements GameObserver, HighlightPanel, PlayerActionPublisher {
  private final int rows;
  private final int cols;
  private final ReadonlyPawnsGame<PawnCell, Player, PawnCard> model;
  private final Dimension logicalSize = new Dimension(800, 600);
  private final HandPanel handPanel;
  private int selectedRow = -1;
  private int selectedCol = -1;
  private PlayerActionListener playerActionListener;

  /**
   * Constructs a board panel object with the game model, and hande panel.
   * Initializes the panel, adds mouse and key listeners, and registers the panel
   * as an observer of the game.
   * Board panel needs hand panel to detect if the hand panel is highlighted.
   *
   * @param model     the game model to observe
   * @param handPanel the hand panel for card selection
   * @throws IllegalArgumentException if the model or hand panel is null
   */
  public BoardPanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model, HandPanel handPanel) {
    super();
    if (model == null || handPanel == null) {
      throw new IllegalArgumentException("Model or handPanel is null");
    }
    this.model = model;
    this.handPanel = handPanel;
    rows = model.getHeight();
    cols = model.getWidth();

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

    setFocusable(true);
    requestFocusInWindow();
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

  @Override
  public boolean isHighlighted() {
    return (selectedRow != -1 && selectedCol != -1);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();

    g2d.transform(transformLogicalToPhysical());

    g2d.setColor(Color.DARK_GRAY);
    g2d.fillRect(0, 0, logicalSize.width, logicalSize.height);

    drawGridLines(g2d);
    drawPawnsOnBoard(g2d);
    drawCardOnBoard(g2d);

    // Highlight selected cell
    if (selectedRow != -1 && selectedCol != -1) {
      highlightCellHelper(g2d, selectedRow, selectedCol);
    }
  }

  @Override
  public int getSelectedCardIndex() {
    int selectedCardIndex = -1;
    return selectedCardIndex;
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
        PawnCell cell = model.getCellAt(row, col);
        int pawnCount = cell.getPawnCount().getValue();

        if (pawnCount > 0) {
          Player owner = cell.getOwner();
          if (owner == null) {
            continue;
          }

          Color pawnColor = (owner == Player.RED) ? Color.PINK : Color.BLUE;
          drawPawn(g2d, col, row, cellWidth, cellHeight, pawnColor, pawnCount);
        }
      }
    }
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

  private void drawCardOnBoard(Graphics2D g2d) {
    double cellWidth = logicalSize.getWidth() / cols;
    double cellHeight = logicalSize.getHeight() / rows;

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        PawnCell cell = model.getCellAt(row, col);
        if (cell.hasCard()) {
          Color cardColor = (cell.getOwner() == Player.RED) ? Color.PINK : Color.BLUE;
          int value = cell.getCard().getValue();
          drawCard(g2d, row, col, cardColor, value, cellWidth, cellHeight);
        }
      }
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


  private void highlightCellHelper(Graphics2D g2d, int row, int col) {
    double cellWidth = logicalSize.getWidth() / cols;
    double cellHeight = logicalSize.getHeight() / rows;
    double x = col * cellWidth;
    double y = row * cellHeight;
    g2d.setColor(new Color(0, 255, 255, 128));
    g2d.fillRect((int) x, (int) y, (int) cellWidth, (int) cellHeight);
  }


  @Override
  public void gameStateChanged() {
    repaint();
  }

  @Override
  public void addPlayerActionListener(PlayerActionListener listener) {
    this.playerActionListener = listener;
  }
}
