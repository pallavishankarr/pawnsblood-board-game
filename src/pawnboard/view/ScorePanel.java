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
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

/**
 * Represents a panel that displays the score of each row of a player.
 * Listens to changes in the game state and updates the displayed score accordingly.
 * The score is displayed for each row on the game board.
 *
 * <p>Each row's score is drawn as a token lined up with that board row. When this player is
 * currently winning the row, the token is filled in the player's color; otherwise it is drawn
 * as an outline, so who is winning each row is visible at a glance.
 */
public class ScorePanel extends JPanel implements GameObserver {

  // Palette (matches BoardPanel and HandPanel)
  private static final Color FELT = new Color(0x1F, 0x3B, 0x33);
  private static final Color TILE = new Color(0x2B, 0x4D, 0x43);
  private static final Color CARD_FACE = new Color(0xF5, 0xEF, 0xE0);
  private static final Color RED_PLAYER = new Color(0xD6, 0x45, 0x45);
  private static final Color BLUE_PLAYER = new Color(0x3A, 0x7B, 0xD5);

  protected final ReadonlyPawnsGame<PawnCell, Player, PawnCard> model;
  private final Player player;

  /**
   * Constructs a ScorePanel for the specified player and game model.
   *
   * @param model  the game model to observe
   * @param player the player whose score will be displayed
   * @throws IllegalArgumentException if the model or player is null
   */
  public ScorePanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model, Player player) {
    if (model == null || player == null) {
      throw new IllegalArgumentException("model and player cannot be null");
    }

    this.model = model;
    this.player = player;
    model.addObserver(this);
    setPreferredSize(new Dimension(100, 600)); // Ensures correct width
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

  protected Color textColor() {
    return CARD_FACE;
  }

  protected Color playerColor(Player p) {
    return (p == Player.RED) ? RED_PLAYER : BLUE_PLAYER;
  }

  // ---------------------------------------------------------------------------
  // Painting
  // ---------------------------------------------------------------------------

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (getWidth() <= 0 || getHeight() <= 0) {
      return;
    }
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    drawBackground(g2d);
    drawRowScores(g2d);

    g2d.dispose();
  }

  private void drawBackground(Graphics2D g2d) {
    // Same gradient as the board so the panels read as one surface.
    Color base = backgroundColor();
    g2d.setPaint(new GradientPaint(0, 0, blend(base, Color.WHITE, 0.05f),
            0, getHeight(), blend(base, Color.BLACK, 0.3f)));
    g2d.fillRect(0, 0, getWidth(), getHeight());

    // A thin strip in the player's color on the outer edge marks whose side this is.
    int strip = 4;
    int x = (player == Player.RED) ? 0 : getWidth() - strip;
    g2d.setColor(withAlpha(playerColor(player), 200));
    g2d.fillRect(x, 0, strip, getHeight());
  }

  private void drawRowScores(Graphics2D g2d) {
    int rows = model.getHeight();
    double rowHeight = getHeight() / (double) rows;
    Player opponent = (player == Player.RED) ? Player.BLUE : Player.RED;

    double diameter = Math.max(18, Math.min(getWidth() * 0.6, rowHeight * 0.55));
    double r = diameter / 2.0;
    double cx = getWidth() / 2.0;
    Font font = new Font("Georgia", Font.BOLD, Math.max(12, (int) (diameter * 0.45)));

    for (int row = 0; row < rows; row++) {
      int score = model.checkScoreRow(player, row);
      int opponentScore = model.checkScoreRow(opponent, row);
      boolean winningRow = score > opponentScore;
      double cy = (row + 0.5) * rowHeight;

      if (winningRow) {
        drawFilledToken(g2d, cx, cy, r, playerColor(player));
        drawCenteredString(g2d, String.valueOf(score), font, cx, cy, Color.WHITE);
      } else {
        drawOutlinedToken(g2d, cx, cy, r, playerColor(player));
        drawCenteredString(g2d, String.valueOf(score), font, cx, cy,
                withAlpha(textColor(), 200));
      }
    }
  }

  /** A glossy token in the player's color, matching the pawns on the board. */
  private void drawFilledToken(Graphics2D g2d, double x, double y, double r, Color color) {
    g2d.setColor(new Color(0, 0, 0, 90));
    g2d.fill(new Ellipse2D.Double(x - r, y - r + r * 0.15, 2 * r, 2 * r));

    float[] fractions = {0f, 0.6f, 1f};
    Color[] colors = {
            blend(color, Color.WHITE, 0.45f), color, blend(color, Color.BLACK, 0.35f)
    };
    g2d.setPaint(new RadialGradientPaint((float) (x - r * 0.35), (float) (y - r * 0.35),
            (float) (r * 1.6), fractions, colors));
    Ellipse2D body = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
    g2d.fill(body);

    g2d.setColor(blend(color, Color.BLACK, 0.45f));
    g2d.setStroke(new BasicStroke(1.2f));
    g2d.draw(body);
  }

  /** A quiet outlined token for rows this player is not currently winning. */
  private void drawOutlinedToken(Graphics2D g2d, double x, double y, double r, Color color) {
    Ellipse2D body = new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r);
    g2d.setColor(tileColor());
    g2d.fill(body);
    g2d.setColor(withAlpha(color, 170));
    g2d.setStroke(new BasicStroke(2f));
    g2d.draw(body);
  }

  private void drawCenteredString(Graphics2D g2d, String text, Font font,
                                  double x, double y, Color color) {
    g2d.setFont(font);
    FontMetrics metrics = g2d.getFontMetrics();
    g2d.setColor(color);
    g2d.drawString(text, (float) (x - metrics.stringWidth(text) / 2.0),
            (float) (y + (metrics.getAscent() - metrics.getDescent()) / 2.0));
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

  @Override
  public void gameStateChanged() {
    repaint();
  }
}