package pawnboard.view;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import pawnboard.controller.CardInitializer;
import pawnboard.model.InfluenceType;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

/**
 * Represents the hand of a cards for a player in the game.
 * Allows the player to view and select cards and displays relevant information
 * such as card name, cost, value, and influence grid.
 * Updates the GUI when the game state changes.
 */
public class HandPanel extends JPanel implements GameObserver, HighlightPanel {
  private static final int CARD_BORDER_THICKNESS = 3;
  private final List<PawnCard> hand;
  private final Color color;
  private int selectedCardIndex = -1;
  private final ReadonlyPawnsGame<PawnCell, Player, PawnCard> model;

  /**
   * Constructs a HandPanel for a specific player in the game.
   *
   * @param model  The game model that represents the state of the game.
   * @param player The player whose hand is to be displayed.
   * @throws IllegalArgumentException if the model is null
   */
  public HandPanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model, Player player) {
    super();
    if (model == null) {
      throw new IllegalArgumentException("model cannot be null");
    }
    this.model = model;

    if (player.equals(Player.RED)) {
      this.hand = model.getHand(Player.RED);
      this.color = Color.PINK;
    } else {
      this.hand = model.getHand(Player.BLUE);
      this.color = Color.BLUE;
    }

    setBackground(Color.BLACK);

    model.addObserver(this);
    this.addMouseListener(new HandPanelMouseListener(this));
  }

  /**
   * Allows to run the hand panel and see screen.
   * @param args arguments
   */
  public static void main(String[] args) {
    String path = "docs" + File.separator + "deck.config";
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = CardInitializer.initializeDeck(path, path);
    List<PawnCard> deck = decks.get("deck1");
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(deck, deck, 5);
    Player player = Player.RED;
    HandPanel handPanel = new HandPanel(model, player);
    JFrame frame = new JFrame("HandPanel Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1200, 200);
    frame.add(handPanel);
    frame.setVisible(true);
  }

  @Override
  public Dimension getPreferredSize() {
    // Set the preferred size to be based on the panel's width and a fixed height
    return new Dimension(getWidth(), 200);  // Default height, can adjust as needed
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int handSize = hand.size();
    int panelWidth = getWidth();
    int cardWidth = panelWidth / handSize - CARD_BORDER_THICKNESS;
    int cardHeight = getHeight();

    for (int i = 0; i < handSize; i++) {
      PawnCard card = hand.get(i);
      int xPosition = i * (cardWidth + CARD_BORDER_THICKNESS);

      drawCardBackground(g, xPosition, cardWidth, cardHeight, i);
      drawCardDetails(g, xPosition, card, cardWidth, cardHeight);
      drawInfluenceGrid(g, card, xPosition, cardWidth, cardHeight);
    }
  }

  private void drawCardBackground(Graphics g, int xPosition, int cardWidth, int cardHeight, int cardIndex) {
    g.setColor(Color.BLACK);
    g.fillRect(xPosition, 0, cardWidth, cardHeight);

    if (cardIndex == selectedCardIndex) {
      g.setColor(Color.CYAN);
    } else {
      g.setColor(color);
    }
    g.fillRect(xPosition + CARD_BORDER_THICKNESS, 0, cardWidth - CARD_BORDER_THICKNESS, cardHeight);
  }

  private void drawCardDetails(Graphics g, int xPosition, PawnCard card, int cardWidth, int cardHeight) {
    g.setColor(Color.WHITE);
    g.drawString(card.getName(), xPosition + CARD_BORDER_THICKNESS + 5, 20);
    g.drawString("Cost: " + card.getPawnCost(), xPosition + CARD_BORDER_THICKNESS + 5, 35);
    g.drawString("Value: " + card.getValue(), xPosition + CARD_BORDER_THICKNESS + 5, 50);
  }

  private void drawInfluenceGrid(Graphics g, PawnCard card, int xPosition, int cardWidth, int cardHeight) {
    InfluenceType[][] grid = card.getInfluenceGrid();
    int cellSize = Math.min((cardWidth - 2 * CARD_BORDER_THICKNESS) / 5, (cardHeight - 70) / 5);
    int startY = 70;

    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        int x = xPosition + CARD_BORDER_THICKNESS + col * cellSize;
        int y = startY + row * cellSize;

        drawGridCell(g, grid[row][col], x, y, cellSize);
      }
    }
  }

  private void drawGridCell(Graphics g, InfluenceType influence, int x, int y, int cellSize) {
    g.setColor(Color.DARK_GRAY);
    g.fillRect(x, y, cellSize, cellSize);

    if (influence == InfluenceType.I) {
      g.setColor(Color.CYAN);
      g.fillRect(x, y, cellSize, cellSize);
    } else if (influence == InfluenceType.C) {
      g.setColor(Color.ORANGE);
      g.fillRect(x, y, cellSize, cellSize);
    }

    g.setColor(Color.BLACK);
    g.drawRect(x, y, cellSize, cellSize);
  }

  private void toggleSelection(int index) {
    if (index == selectedCardIndex) {
      selectedCardIndex = -1;  // Deselect card
    } else {
      selectedCardIndex = index;
    }
    repaint();
  }

  public int getSelectedCardIndex() {
    return selectedCardIndex;
  }

  private void highlightCell(int row, int col) {
    int handSize = hand.size();
    int panelWidth = getWidth();
    int cardWidth = panelWidth / handSize - CARD_BORDER_THICKNESS;

    int x = col * (cardWidth + CARD_BORDER_THICKNESS);
    if (row >= 0 && row < handSize) {
      toggleSelection(row);
    }


  }

  private void clearHighlight() {
    selectedCardIndex = -1;
    repaint();

  }

  @Override
  public boolean isHighlighted() {
    return (selectedCardIndex != -1);
  }

  @Override
  public void gameStateChanged() {
    repaint();
  }

  private class HandPanelMouseListener extends MouseAdapter {
    private final HandPanel handPanel;

    private HandPanelMouseListener(HandPanel handPanel) {
      this.handPanel = handPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      int x = e.getX();


      int cardWidth = handPanel.getWidth() / handPanel.hand.size() - CARD_BORDER_THICKNESS;
      int cardIndex = x / (cardWidth + CARD_BORDER_THICKNESS);

      // Ensure card index doesn't go out of bounds
      if (cardIndex >= 0 && cardIndex < handPanel.hand.size()) {
        handPanel.toggleSelection(cardIndex);

        System.out.println("Card index: " + cardIndex + ", Player: "
                + (color == Color.PINK ? "RED" : "BLUE"));
      }
    }
  }
}






