package pawnboard.controller;

import java.awt.*;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;
import pawnboard.view.BoardPanel;
import pawnboard.view.HandPanel;

public class MockBoardPanel extends BoardPanel {
  private final StringBuilder log;

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

