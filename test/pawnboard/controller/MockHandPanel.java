package pawnboard.controller;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;
import pawnboard.view.HandPanel;

class MockHandPanel extends HandPanel {
  private final List<PawnCard> mockHand;
  private boolean gameStateChangedCalled = false;
  private PlayerActionListener mockListener;

  public MockHandPanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model, Player player) {
    super(model, player);
    this.mockHand = new ArrayList<>();
  }

  // Overriding getHand to return a controlled hand for testing
  @Override
  public void gameStateChanged() {
    gameStateChangedCalled = true;
  }

  public boolean isGameStateChangedCalled() {
    return gameStateChangedCalled;
  }

  @Override
  public void addPlayerActionListener(PlayerActionListener listener) {
    this.mockListener = listener;
  }

  public PlayerActionListener getMockListener() {
    return mockListener;
  }

  public void addMockCard(PawnCard card) {
    mockHand.add(card);
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(500, 200);
  }
}