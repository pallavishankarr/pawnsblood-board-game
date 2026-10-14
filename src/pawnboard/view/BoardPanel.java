package pawnboard.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import pawnboard.controller.PlayerActionListener;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

/**
 * A panel that represents the board of the game.
 * Updates board of game interactions, highlights cells, renders the game state.
 * This panel is a game observer, listens for the game state changes and user interactions
 * (mouse click, mouse hover, and key presses).
 *
 * <p>The board is drawn like a tabletop: a felt surface with raised tiles, pawns drawn as
 * game tokens, and cards drawn as ivory cards framed in the owner's color. Colors come from
 * protected palette methods so a subclass (such as a high-contrast view) can override them.
 */
public class BoardPanel extends JPanel
        implements GameObserver, HighlightPanel, PlayerActionPublisher {

  // Palette
  private static final Color FELT = new Color(0x1F, 0x3B, 0x33);
  private static final Color TILE = new Color(0x2B, 0x4D, 0x43);
  private static final Color CARD_FACE = new Color(0xF5, 0xEF, 0xE0);
  private static final Color RED_PLAYER = new Color(0xD6, 0x45, 0x45);
  private static final Color BLUE_PLAYER = new Color(0x3A, 0x7B, 0xD5);
  private static final Color SELECTION = new Color(0xF2, 0xC1, 0x4E);
  private static final Color INCREASE = new Color(0x2F, 0xB3, 0x6D);
  private static final Color DECREASE = new Color(0x9B, 0x5C, 0xE6);

  private final int rows;
  private final int cols;
  private final ReadonlyPawnsGame<PawnCell, Player, PawnCard> model;
  private final HandPanel handPanel;
  private int selectedRow = -1;
  private int selectedCol = -1;
  private int hoverRow = -1;
  private int hoverCol = -1;
  private PlayerActionListener playerActionListener;

  /**
   * Constructs a board panel object with the game model, and hand panel.
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

    MouseAdapter mouse = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        handleCellClick(e);
      }

      @Override
      public void mouseMoved(MouseEvent e) {
        handleHover(e);
      }

      @Override
      public void mouseExited(MouseEvent e) {
        clearHover();
      }
    };
    addMouseListener(mouse);
    addMouseMotionListener(mouse);

    addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        handleKeyPress(e);
      }
    });

    setFocusable(true);
    requestFocusInWindow();
  }

  // ---------------------------------------------------------------------------
  // Palette (override in a subclass to restyle, e.g. for high contrast)
  // ---------------------------------------------------------------------------

  protected Color backgroundColor() {
    return FELT;
  }

  protected Color tileColor() {
    return TILE;
  }

  protected Color cardFaceColor() {
    return CARD_FACE;
  }

  protected Color highlightColor() {
    return SELECTION;
  }

  protected Color playerColor(Player player) {
    return (player == Player.RED) ? RED_PLAYER : BLUE_PLAYER;
  }

  // ---------------------------------------------------------------------------
  // Input handling
  // ---------------------------------------------------------------------------

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

  private void handleCellClick(MouseEvent e) {
    int clickedRow = rowAt(e.getY());
    int clickedCol = colAt(e.getX());

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

  private void handleHover(MouseEvent e) {
    int row = rowAt(e.getY());
    int col = colAt(e.getX());
    if (row != hoverRow || col != hoverCol) {
      hoverRow = row;
      hoverCol = col;
      repaint();
    }
  }

  private void clearHover() {
    hoverRow = -1;
    hoverCol = -1;
    repaint();
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
  public int getSelectedCardIndex() {
    int selectedCardIndex = -1;
    return selectedCardIndex;
  }

  // ---------------------------------------------------------------------------
  // Geometry
  // ---------------------------------------------------------------------------

  private double cellWidth() {
    return getWidth() / (double) cols;
  }

  private double cellHeight() {
    return getHeight() / (double) rows;
  }

  private int colAt(int x) {
    int col = (int) (x / cellWidth());
    return Math.max(0, Math.min(cols - 1, col));
  }

  private int rowAt(int y) {
    int row = (int) (y / cellHeight());
    return Math.max(0, Math.min(rows - 1, row));
  }

  /** The rounded tile for a cell, inset so there is a gap between neighboring tiles. */
  private RoundRectangle2D tileShape(int row, int col) {
    double cw = cellWidth();
    double ch = cellHeight();
    double size = Math.min(cw, ch);
    double inset = Math.max(2, size * 0.05);
    double arc = size * 0.18;
    return new RoundRectangle2D.Double(col * cw + inset, row * ch + inset,
            cw - 2 * inset, ch - 2 * inset, arc, arc);
  }

  // ---------------------------------------------------------------------------
  // Painting
  // ---------------------------------------------------------------------------

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

    drawBackground(g2d);

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        PawnCell cell = model.getCellAt(row, col);
        RoundRectangle2D tile = tileShape(row, col);
        boolean hovered = (row == hoverRow && col == hoverCol);

        drawTile(g2d, tile, cell, hovered);
        if (cell.hasCard()) {
          drawCard(g2d, tile, cell);
        } else {
          drawPawns(g2d, tile, cell);
        }
        drawChangeBadge(g2d, tile, cell.getChangedValue());
      }
    }

    if (isHighlighted()) {
      drawSelection(g2d, tileShape(selectedRow, selectedCol));
    }

    g2d.dispose();
  }

  private void drawBackground(Graphics2D g2d) {
    Color base = backgroundColor();
    g2d.setPaint(new GradientPaint(0, 0, blend(base, Color.WHITE, 0.05f),
            0, getHeight(), blend(base, Color.BLACK, 0.3f)));
    g2d.fillRect(0, 0, getWidth(), getHeight());
  }

  private void drawTile(Graphics2D g2d, RoundRectangle2D tile, PawnCell cell, boolean hovered) {
    Color fill = hovered ? blend(tileColor(), Color.WHITE, 0.12f) : tileColor();
    g2d.setColor(fill);
    g2d.fill(tile);

    // Tint tiles that hold pawns so each player's territory is visible at a glance.
    Player owner = cell.getOwner();
    if (!cell.hasCard() && owner != null && cell.getPawnCount().getValue() > 0) {
      g2d.setColor(withAlpha(playerColor(owner), 40));
      g2d.fill(tile);
    }

    // Light top edge so tiles read as slightly raised.
    g2d.setColor(new Color(255, 255, 255, 22));
    g2d.setStroke(new BasicStroke(1f));
    g2d.draw(tile);
  }

  private void drawPawns(Graphics2D g2d, RoundRectangle2D tile, PawnCell cell) {
    int count = cell.getPawnCount().getValue();
    Player owner = cell.getOwner();
    if (count <= 0 || owner == null) {
      return;
    }

    double cx = tile.getCenterX();
    double cy = tile.getCenterY();
    double r = Math.min(tile.getWidth(), tile.getHeight()) * 0.13;
    double s = r * 1.25;

    double[][] offsets;
    switch (count) {
      case 1:
        offsets = new double[][]{{0, 0}};
        break;
      case 2:
        offsets = new double[][]{{-s, 0}, {s, 0}};
        break;
      case 3:
        offsets = new double[][]{{-s, -s * 0.85}, {s, -s * 0.85}, {0, s * 0.85}};
        break;
      default:
        return;
    }

    for (double[] offset : offsets) {
      drawPawn(g2d, cx + offset[0], cy + offset[1], r, playerColor(owner));
    }
  }

  /** Draws one pawn as a glossy token with a soft shadow beneath it. */
  private void drawPawn(Graphics2D g2d, double x, double y, double r, Color color) {
    g2d.setColor(new Color(0, 0, 0, 90));
    g2d.fill(new Ellipse2D.Double(x - r, y - r + r * 0.25, 2 * r, 2 * r));

    float[] fractions = {0f, 0.6f, 1f};
    Color[] colors = {
            blend(color, Color.WHITE, 0.55f), color, blend(color, Color.BLACK, 0.35f)
    };
    g2d.setPaint(new RadialGradientPaint((float) (x - r * 0.35), (float) (y - r * 0.35),
            (float) (r * 1.6), fractions, colors));
    Ellipse2D body = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
    g2d.fill(body);

    g2d.setColor(blend(color, Color.BLACK, 0.45f));
    g2d.setStroke(new BasicStroke(1.2f));
    g2d.draw(body);
  }

  /** Draws a placed card as an ivory card framed in its owner's color, showing its value. */
  private void drawCard(Graphics2D g2d, RoundRectangle2D tile, PawnCell cell) {
    Player owner = cell.getOwner();
    Color ownerColor = (owner == null) ? Color.GRAY : playerColor(owner);

    double size = Math.min(tile.getWidth(), tile.getHeight());
    double pad = size * 0.05;
    RoundRectangle2D card = new RoundRectangle2D.Double(
            tile.getX() + pad, tile.getY() + pad,
            tile.getWidth() - 2 * pad, tile.getHeight() - 2 * pad,
            tile.getArcWidth() * 0.8, tile.getArcHeight() * 0.8);

    // Shadow
    g2d.setColor(new Color(0, 0, 0, 90));
    g2d.fill(new RoundRectangle2D.Double(card.getX(), card.getY() + size * 0.03,
            card.getWidth(), card.getHeight(), card.getArcWidth(), card.getArcHeight()));

    // Face
    g2d.setColor(cardFaceColor());
    g2d.fill(card);

    // Owner frame
    float frame = (float) Math.max(2, size * 0.06);
    double half = frame / 2.0;
    g2d.setColor(ownerColor);
    g2d.setStroke(new BasicStroke(frame));
    g2d.draw(new RoundRectangle2D.Double(card.getX() + half, card.getY() + half,
            card.getWidth() - frame, card.getHeight() - frame,
            card.getArcWidth(), card.getArcHeight()));

    // Value
    String text = String.valueOf(cell.getCard().getValue());
    int fontSize = Math.max(12, (int) (size * 0.42));
    Font font = new Font("Georgia", Font.BOLD, fontSize);
    drawCenteredText(g2d, text, font, card.getCenterX(), card.getCenterY(),
            blend(ownerColor, Color.BLACK, 0.25f), false);
  }

  /** Draws a small +n / -n badge in the tile's corner when a cell's value has been changed. */
  private void drawChangeBadge(Graphics2D g2d, RoundRectangle2D tile, int changeValue) {
    if (changeValue == 0) {
      return;
    }
    String text = (changeValue > 0) ? "+" + changeValue : String.valueOf(changeValue);
    Color fill = (changeValue > 0) ? INCREASE : DECREASE;

    double size = Math.min(tile.getWidth(), tile.getHeight());
    Font font = new Font(Font.SANS_SERIF, Font.BOLD, Math.max(10, (int) (size * 0.16)));
    g2d.setFont(font);
    FontMetrics fm = g2d.getFontMetrics();
    double h = fm.getHeight() * 1.05;
    double w = Math.max(h, fm.stringWidth(text) + h * 0.6);
    double margin = size * 0.07;

    RoundRectangle2D pill = new RoundRectangle2D.Double(
            tile.getMaxX() - w - margin, tile.getY() + margin, w, h, h, h);
    g2d.setColor(fill);
    g2d.fill(pill);
    g2d.setColor(new Color(255, 255, 255, 140));
    g2d.setStroke(new BasicStroke(1f));
    g2d.draw(pill);

    drawCenteredText(g2d, text, font, pill.getCenterX(), pill.getCenterY(), Color.WHITE, false);
  }

  /** Draws a glowing outline around the selected tile. */
  private void drawSelection(Graphics2D g2d, RoundRectangle2D tile) {
    Color c = highlightColor();
    g2d.setColor(withAlpha(c, 45));
    g2d.fill(tile);
    g2d.setColor(withAlpha(c, 90));
    g2d.setStroke(new BasicStroke(7f));
    g2d.draw(tile);
    g2d.setColor(c);
    g2d.setStroke(new BasicStroke(3f));
    g2d.draw(tile);
  }

  private void drawCenteredText(Graphics2D g2d, String text, Font font,
                                double cx, double cy, Color color, boolean shadow) {
    g2d.setFont(font);
    FontMetrics fm = g2d.getFontMetrics();
    float x = (float) (cx - fm.stringWidth(text) / 2.0);
    float y = (float) (cy + (fm.getAscent() - fm.getDescent()) / 2.0);
    if (shadow) {
      g2d.setColor(new Color(0, 0, 0, 110));
      g2d.drawString(text, x + 1.5f, y + 2f);
    }
    g2d.setColor(color);
    g2d.drawString(text, x, y);
  }

  // ---------------------------------------------------------------------------
  // Color helpers
  // ---------------------------------------------------------------------------

  /** Mixes color a toward color b by fraction t (0 = all a, 1 = all b). */
  private static Color blend(Color a, Color b, float t) {
    float u = 1f - t;
    return new Color(
            Math.round(a.getRed() * u + b.getRed() * t),
            Math.round(a.getGreen() * u + b.getGreen() * t),
            Math.round(a.getBlue() * u + b.getBlue() * t));
  }

  private static Color withAlpha(Color c, int alpha) {
    return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
  }

  // ---------------------------------------------------------------------------
  // Observer / publisher
  // ---------------------------------------------------------------------------

  @Override
  public void gameStateChanged() {
    repaint();
  }

  @Override
  public void addPlayerActionListener(PlayerActionListener listener) {
    this.playerActionListener = listener;
  }
}