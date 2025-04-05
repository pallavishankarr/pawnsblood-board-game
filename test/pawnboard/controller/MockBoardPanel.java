package pawnboard.controller;

import java.awt.Graphics;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;
import pawnboard.view.BoardPanel;
import pawnboard.view.HandPanel;

/**
 * MockBoardPanel that extends BoardPanel.
 */
public class MockBoardPanel extends BoardPanel {
  private final StringBuilder log;

  /**
   * Constructs a MockBoardPanel with the given game model, hand panel, and log.
   * @param model The game model representing the current game state.
   * @param handPanel The panel displaying the player's hand of cards.
   * @param log The StringBuilder for logging game events.
   */
  public MockBoardPanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model,
                        HandPanel handPanel, StringBuilder log) {
    super(model, handPanel);
    this.log = log;
  }

  @Override
  public void gameStateChanged() {
    log.append("gameStateChanged called\n");
  }

  @Override
  public void addPlayerActionListener(PlayerActionListener listener) {
    log.append("addPlayerActionListener called\n");
    super.addPlayerActionListener(listener); // or skip this if you don’t want to call it
  }

  @Override
  protected void paintComponent(Graphics g) {
    log.append("paintComponent called\n");
  }

  @Override
  public boolean isHighlighted() {
    log.append("isHighlighted called\n");
    return super.isHighlighted(); // or `return true;` for full mocking
  }
}

