package pawnboard.view;

import java.awt.Color;
import java.awt.Graphics2D;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

/**
 * Represents a high contrast board panel.
 *
 * <p>It reuses all of BoardPanel's drawing, clicking, and highlighting, and only swaps the
 * colors: a black background and tiles, white tile borders, bright red and cyan for the
 * players, white cards with black values, and a bright yellow selection.
 */
public class HighContrastBoardPanel extends BoardPanel {
  private static final Color BACKGROUND = Color.BLACK;
  private static final Color TILE = Color.BLACK;
  private static final Color TILE_BORDER = new Color(0xC8, 0xC8, 0xC8);
  private static final Color CARD_FACE = Color.BLACK;
  private static final Color RED_PLAYER = new Color(0xFF, 0x4D, 0x4D);
  private static final Color BLUE_PLAYER = new Color(0x00, 0xE5, 0xFF);
  private static final Color SELECTION = new Color(0xFF, 0xE6, 0x00);

  /**
   * Represents a constructor for a high contrast board panel.
   *
   * @param model     the current game state
   * @param handPanel the hand panel
   */
  public HighContrastBoardPanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model,
                                HandPanel handPanel) {
    super(model, handPanel);
  }

  @Override
  protected void drawBackground(Graphics2D g2d) {
    // Solid black instead of the normal board's gradient.
    g2d.setColor(BACKGROUND);
    g2d.fillRect(0, 0, getWidth(), getHeight());
  }

  @Override
  protected Color backgroundColor() {
    return BACKGROUND;
  }

  @Override
  protected Color tileColor() {
    return TILE;
  }

  @Override
  protected Color tileBorderColor() {
    return TILE_BORDER;
  }

  @Override
  protected Color cardFaceColor() {
    return CARD_FACE;
  }

  @Override
  protected Color cardValueColor(Color ownerColor) {
    return Color.WHITE;
  }

  @Override
  protected Color highlightColor() {
    return SELECTION;
  }

  @Override
  protected Color playerColor(Player player) {
    return (player == Player.RED) ? RED_PLAYER : BLUE_PLAYER;
  }
}