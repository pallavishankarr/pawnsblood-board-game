package pawnboard.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.List;

import javax.swing.JPanel;

import pawnboard.controller.PlayerActionListener;
import pawnboard.model.InfluenceType;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

/**
 * Represents the hand of cards for a player in the game.
 * Allows the player to view and select cards and displays relevant information
 * such as card name, cost, value, and influence grid.
 * Updates the GUI when the game state changes.
 *
 * <p>Cards are drawn to match the board: ivory cards framed in the player's color, with the
 * cost shown as pawn tokens. Hovering lifts a card, and the selected card stays lifted with a
 * gold frame. Colors come from protected palette methods so a subclass can restyle them.
 */
public class HandPanel extends JPanel
        implements GameObserver, HighlightPanel, PlayerActionPublisher {

  // Palette (matches BoardPanel)
  private static final Color FELT = new Color(0x1F, 0x3B, 0x33);
  private static final Color CARD_FACE = new Color(0xF5, 0xEF, 0xE0);
  private static final Color RED_PLAYER = new Color(0xD6, 0x45, 0x45);
  private static final Color BLUE_PLAYER = new Color(0x3A, 0x7B, 0xD5);
  private static final Color SELECTION = new Color(0xF2, 0xC1, 0x4E);
  private static final Color INK = new Color(0x2B, 0x28, 0x22);
  private static final Color MUTED = new Color(0x7D, 0x74, 0x63);

  // Influence grid colors (same meanings as before: cyan, orange, green, purple)
  private static final Color INFLUENCE_DEFAULT = new Color(0x3F, 0xB8, 0xCF);
  private static final Color INFLUENCE_CARD = new Color(0xEE, 0x96, 0x3A);
  private static final Color INFLUENCE_UP = new Color(0x2F, 0xB3, 0x6D);
  private static final Color INFLUENCE_DOWN = new Color(0x9B, 0x5C, 0xE6);
  private static final Color INFLUENCE_NONE = new Color(0xE2, 0xD9, 0xC4);

  // Layout
  private static final int MARGIN = 14;
  private static final int GAP = 12;
  private static final int LIFT = 10;
  private static final int MAX_CARD_WIDTH = 230;

  private final ReadonlyPawnsGame<PawnCell, Player, PawnCard> model;
  private final Player player;
  private int selectedCardIndex = -1;
  private int hoverIndex = -1;
  private PlayerActionListener playerActionListener;

  /**
   * Constructs a HandPanel for a specific player in the game.
   *
   * @param model  The game model that represents the state of the game.
   * @param player The player whose hand is to be displayed.
   * @throws IllegalArgumentException if the model or player is null
   */
  public HandPanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model, Player player) {
    super();
    if (model == null || player == null) {
      throw new IllegalArgumentException("model and player cannot be null");
    }
    this.model = model;
    this.player = player;
    setOpaque(true);
    model.addObserver(this);

    HandPanelMouseListener mouse = new HandPanelMouseListener();
    addMouseListener(mouse);
    addMouseMotionListener(mouse);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(getWidth(), 210);
  }

  // ---------------------------------------------------------------------------
  // Palette (override in a subclass to restyle, e.g. for high contrast)
  // ---------------------------------------------------------------------------

  protected Color backgroundColor() {
    return FELT;
  }

  protected Color cardFaceColor() {
    return CARD_FACE;
  }

  protected Color highlightColor() {
    return SELECTION;
  }

  protected Color playerColor(Player p) {
    return (p == Player.RED) ? RED_PLAYER : BLUE_PLAYER;
  }

  protected Color influenceColor(InfluenceType influence) {
    if (influence == InfluenceType.I) {
      return INFLUENCE_DEFAULT;
    } else if (influence == InfluenceType.C) {
      return INFLUENCE_CARD;
    } else if (influence == InfluenceType.U) {
      return INFLUENCE_UP;
    } else if (influence == InfluenceType.D) {
      return INFLUENCE_DOWN;
    }
    return INFLUENCE_NONE;
  }

  // ---------------------------------------------------------------------------
  // Geometry (shared by painting and click handling so they always agree)
  // ---------------------------------------------------------------------------

  private double cardWidth(int handSize) {
    double available = getWidth() - 2.0 * MARGIN - GAP * (handSize - 1);
    return Math.max(10, Math.min(MAX_CARD_WIDTH, available / handSize));
  }

  /** Left edge of the first card, so the row of cards is centered in the panel. */
  private double rowStartX(int handSize) {
    double total = handSize * cardWidth(handSize) + GAP * (handSize - 1);
    return (getWidth() - total) / 2.0;
  }

  private RoundRectangle2D cardShape(int index, int handSize, boolean lifted) {
    double w = cardWidth(handSize);
    double x = rowStartX(handSize) + index * (w + GAP);
    double top = MARGIN + LIFT;
    double h = getHeight() - top - MARGIN;
    double y = lifted ? top - LIFT : top;
    double arc = Math.min(w, h) * 0.1;
    return new RoundRectangle2D.Double(x, y, w, h, arc, arc);
  }

  /** Returns the index of the card under the given x coordinate, or -1 if none. */
  private int cardIndexAt(int x) {
    int handSize = model.getHand(player).size();
    if (handSize == 0) {
      return -1;
    }
    double w = cardWidth(handSize);
    double start = rowStartX(handSize);
    for (int i = 0; i < handSize; i++) {
      double left = start + i * (w + GAP);
      if (x >= left && x < left + w) {
        return i;
      }
    }
    return -1;
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

    List<PawnCard> hand = model.getHand(player);
    if (hand.isEmpty()) {
      Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
      drawCenteredText(g2d, "No cards in hand", font,
              getWidth() / 2.0, getHeight() / 2.0, withAlpha(Color.WHITE, 170));
      g2d.dispose();
      return;
    }

    int handSize = hand.size();
    for (int i = 0; i < handSize; i++) {
      boolean selected = (i == selectedCardIndex);
      boolean hovered = (i == hoverIndex);
      RoundRectangle2D shape = cardShape(i, handSize, selected || hovered);
      drawCard(g2d, hand.get(i), shape, selected);
    }

    g2d.dispose();
  }

  private void drawBackground(Graphics2D g2d) {
    Color base = backgroundColor();
    g2d.setPaint(new GradientPaint(0, 0, blend(base, Color.BLACK, 0.2f),
            0, getHeight(), blend(base, Color.BLACK, 0.45f)));
    g2d.fillRect(0, 0, getWidth(), getHeight());

    // Seam between the board and the hand tray
    g2d.setColor(new Color(0, 0, 0, 90));
    g2d.fillRect(0, 0, getWidth(), 2);
  }

  private void drawCard(Graphics2D g2d, PawnCard card, RoundRectangle2D shape, boolean selected) {
    Color owner = playerColor(player);
    double x = shape.getX();
    double y = shape.getY();
    double w = shape.getWidth();
    double h = shape.getHeight();
    double arc = shape.getArcWidth();

    // Shadow
    g2d.setColor(new Color(0, 0, 0, 100));
    g2d.fill(new RoundRectangle2D.Double(x, y + 4, w, h, arc, arc));

    // Glow behind the selected card
    if (selected) {
      g2d.setColor(withAlpha(highlightColor(), 90));
      g2d.setStroke(new BasicStroke(8f));
      g2d.draw(shape);
    }

    // Face
    g2d.setColor(cardFaceColor());
    g2d.fill(shape);

    // Frame: player color, or gold when selected
    float frame = (float) Math.max(3, Math.min(w, h) * 0.035);
    g2d.setColor(selected ? highlightColor() : owner);
    g2d.setStroke(new BasicStroke(frame));
    g2d.draw(new RoundRectangle2D.Double(x + frame / 2.0, y + frame / 2.0,
            w - frame, h - frame, arc, arc));

    double pad = frame + Math.max(6, w * 0.06);
    double innerX = x + pad;
    double innerW = w - 2 * pad;
    double cursorY = y + pad;

    // Name
    int nameSize = (int) Math.max(11, Math.min(18, w * 0.09));
    Font nameFont = new Font("Georgia", Font.BOLD, nameSize);
    g2d.setFont(nameFont);
    FontMetrics nameMetrics = g2d.getFontMetrics();
    g2d.setColor(INK);
    g2d.drawString(fitText(card.getName(), nameMetrics, innerW),
            (float) innerX, (float) (cursorY + nameMetrics.getAscent()));
    cursorY += nameMetrics.getHeight() + 4;

    // Cost (as pawn tokens) on the left, value on the right
    double pipR = Math.max(4, Math.min(8, w * 0.035));
    double rowH = Math.max(2 * pipR, nameSize + 4);
    double rowCenterY = cursorY + rowH / 2.0;

    Font captionFont = new Font(Font.SANS_SERIF, Font.PLAIN, Math.max(10, nameSize - 4));
    g2d.setFont(captionFont);
    FontMetrics captionMetrics = g2d.getFontMetrics();
    float captionBaseline = (float) (rowCenterY
            + (captionMetrics.getAscent() - captionMetrics.getDescent()) / 2.0);

    g2d.setColor(MUTED);
    g2d.drawString("cost", (float) innerX, captionBaseline);

    int cost = card.getPawnCost().getValue();
    double pipX = innerX + captionMetrics.stringWidth("cost") + 6 + pipR;
    for (int k = 0; k < cost; k++) {
      drawPip(g2d, pipX + k * pipR * 2.6, rowCenterY, pipR, owner);
    }

    Font valueFont = new Font("Georgia", Font.BOLD, nameSize + 2);
    FontMetrics valueMetrics = g2d.getFontMetrics(valueFont);
    String value = String.valueOf(card.getValue());
    double valueX = innerX + innerW - valueMetrics.stringWidth(value);
    g2d.setFont(valueFont);
    g2d.setColor(blend(owner, Color.BLACK, 0.25f));
    g2d.drawString(value, (float) valueX, (float) (rowCenterY
            + (valueMetrics.getAscent() - valueMetrics.getDescent()) / 2.0));

    g2d.setFont(captionFont);
    g2d.setColor(MUTED);
    g2d.drawString("value",
            (float) (valueX - captionMetrics.stringWidth("value") - 5), captionBaseline);
    cursorY += rowH + 8;

    // Influence grid, centered in the remaining space
    double availableH = (y + h - pad) - cursorY;
    double gridSize = Math.min(innerW, availableH);
    if (gridSize > 10) {
      drawInfluenceGrid(g2d, card.getInfluenceGrid(),
              innerX + (innerW - gridSize) / 2.0,
              cursorY + (availableH - gridSize) / 2.0, gridSize);
    }
  }

  private void drawInfluenceGrid(Graphics2D g2d, InfluenceType[][] grid,
                                 double x, double y, double size) {
    double gap = Math.max(1.5, size * 0.025);
    double cell = (size - gap * 4) / 5.0;
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        double cx = x + col * (cell + gap);
        double cy = y + row * (cell + gap);
        g2d.setColor(influenceColor(grid[row][col]));
        g2d.fill(new RoundRectangle2D.Double(cx, cy, cell, cell, cell * 0.25, cell * 0.25));
      }
    }
  }

  /** Draws a small pawn token, matching the pawns on the board. */
  private void drawPip(Graphics2D g2d, double x, double y, double r, Color color) {
    g2d.setColor(new Color(0, 0, 0, 60));
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
    g2d.setStroke(new BasicStroke(1f));
    g2d.draw(body);
  }

  private void drawCenteredText(Graphics2D g2d, String text, Font font,
                                double cx, double cy, Color color) {
    g2d.setFont(font);
    FontMetrics fm = g2d.getFontMetrics();
    g2d.setColor(color);
    g2d.drawString(text, (float) (cx - fm.stringWidth(text) / 2.0),
            (float) (cy + (fm.getAscent() - fm.getDescent()) / 2.0));
  }

  /** Shortens text with an ellipsis so it fits within maxWidth. */
  private static String fitText(String text, FontMetrics fm, double maxWidth) {
    if (text == null) {
      return "";
    }
    if (fm.stringWidth(text) <= maxWidth) {
      return text;
    }
    String ellipsis = "\u2026";
    for (int end = text.length() - 1; end > 0; end--) {
      String shortened = text.substring(0, end) + ellipsis;
      if (fm.stringWidth(shortened) <= maxWidth) {
        return shortened;
      }
    }
    return ellipsis;
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
  // Selection, observer, and publisher
  // ---------------------------------------------------------------------------

  private void toggleSelection(int index) {
    if (index == selectedCardIndex) {
      selectedCardIndex = -1;  // Deselect card
    } else {
      selectedCardIndex = index;
    }
    repaint();
  }

  @Override
  public int getSelectedCardIndex() {
    return selectedCardIndex;
  }

  @Override
  public boolean isHighlighted() {
    return (selectedCardIndex != -1);
  }

  @Override
  public void gameStateChanged() {
    // If the hand shrank (for example, after playing a card), drop a selection that no longer
    // points at a real card.
    if (selectedCardIndex >= model.getHand(player).size()) {
      selectedCardIndex = -1;
    }
    revalidate();
    repaint();
  }

  @Override
  public void addPlayerActionListener(PlayerActionListener listener) {
    this.playerActionListener = listener;
  }

  private class HandPanelMouseListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      int index = cardIndexAt(e.getX());
      if (index == -1) {
        return;
      }
      toggleSelection(index);
      if (playerActionListener != null) {
        playerActionListener.onCardClick(index);
      }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
      int index = cardIndexAt(e.getX());
      if (index != hoverIndex) {
        hoverIndex = index;
        repaint();
      }
    }

    @Override
    public void mouseExited(MouseEvent e) {
      hoverIndex = -1;
      repaint();
    }
  }
}