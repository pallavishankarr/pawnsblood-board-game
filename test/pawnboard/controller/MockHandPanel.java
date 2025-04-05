package pawnboard.controller;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;
import pawnboard.view.HandPanel;

/**
 * A mock implementation of the HandPanel class used
 * for testing or simulating the player's hand of cards in a game.
 */
public class MockHandPanel extends HandPanel {
  private final List<PawnCard> mockHand;
  private boolean gameStateChangedCalled = false;
  private PlayerActionListener mockListener;


  /**
   * Constructs a MockHandPanel with the specified game model and player.
   * @param model The game model representing the current state of the game.
   * @param player The player associated with this hand panel.
   */
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