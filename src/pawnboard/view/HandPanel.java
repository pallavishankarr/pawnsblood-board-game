package pawnboard.view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pawnboard.model.Card;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;


public class HandPanel extends JPanel implements HighlightPanelView {
  private final List<Card> hand;
  private final Color color;
  private int selectedCardIndex = -1;

  private static final int CARD_BORDER_THICKNESS = 3;

  public HandPanel(ReadonlyPawnsGame<PawnCell> model, Player player) {
    super();

    if (player.equals(Player.RED)) {
      this.hand = model.getHand(Player.RED);
      this.color = Color.PINK;
    } else {
      this.hand = model.getHand(Player.BLUE);
      this.color = Color.BLUE;
    }

    setBackground(Color.BLACK);
    this.addMouseListener(new HandPanelMouseListener(this));
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
    int panelWidth = getWidth(); // Get current panel width
    int cardWidth = panelWidth / handSize - CARD_BORDER_THICKNESS; // Calculate card width based on the panel width
    int cardHeight = getHeight(); // Use full height for each card

    for (int i = 0; i < handSize; i++) {
      Card card = hand.get(i);
      int xPosition = i * (cardWidth + CARD_BORDER_THICKNESS);

      g.setColor(Color.BLACK);
      g.fillRect(xPosition, 0, cardWidth, cardHeight);

      if (i == selectedCardIndex) {
        g.setColor(Color.CYAN);
      } else {
        g.setColor(color); // Use player color for non-selected cards
      }
      g.fillRect(xPosition + CARD_BORDER_THICKNESS, 0, cardWidth - CARD_BORDER_THICKNESS, cardHeight);

      // Draw card information
      g.setColor(Color.WHITE);
      g.drawString(card.getName(), xPosition + CARD_BORDER_THICKNESS + 5, 20);
      g.drawString("Cost: " + card.getPawnCost(), xPosition + CARD_BORDER_THICKNESS + 5, 35);
      g.drawString("Value: " + card.getValue(), xPosition + CARD_BORDER_THICKNESS + 5, 50);

      // Draw influence grid (5x5)
      char[][] grid = card.getInfluenceGrid();
      int cellSize = Math.min((cardWidth - 2 * CARD_BORDER_THICKNESS) / 5, (cardHeight - 70) / 5);
      int startY = 70;

      for (int row = 0; row < 5; row++) {
        for (int col = 0; col < 5; col++) {
          int x = xPosition + CARD_BORDER_THICKNESS + col * cellSize;
          int y = startY + row * cellSize;

          // Default grid cell (Dark Gray)
          g.setColor(Color.DARK_GRAY);
          g.fillRect(x, y, cellSize, cellSize);

          // Highlight Influence Cells
          if (grid[row][col] == 'I') {
            g.setColor(Color.CYAN);  // Influence cells as Cyan
            g.fillRect(x, y, cellSize, cellSize);
          } else if (grid[row][col] == 'C') {
            g.setColor(Color.ORANGE);  // Coverage cells as Orange
            g.fillRect(x, y, cellSize, cellSize);
          }

          // Draw black border for each cell
          g.setColor(Color.BLACK);
          g.drawRect(x, y, cellSize, cellSize);
        }
      }
    }
  }

  public void toggleSelection(int index) {
    if (index == selectedCardIndex) {
      selectedCardIndex = -1;  // Deselect card
    } else {
      selectedCardIndex = index;  // Select new card
    }
    repaint();
  }

  public int getSelectedCardIndex() {
    return selectedCardIndex;
  }

  @Override
  public void updateBoard() {
    repaint();
  }

  @Override
  public void highlightCell(int row, int col) {
    int handSize = hand.size();
    int panelWidth = getWidth();
    int cardWidth = panelWidth / handSize - CARD_BORDER_THICKNESS;

    int x = col * (cardWidth + CARD_BORDER_THICKNESS);
    if (row >= 0 && row < handSize) {
      toggleSelection(row);
    }


  }

  @Override
  public void clearHighlight() {
    selectedCardIndex = -1;
    repaint();

  }

  @Override
  public boolean isHighlighted() {
    return (selectedCardIndex != -1);
  }

  public class HandPanelMouseListener extends MouseAdapter {
    private final HandPanel handPanel;

    public HandPanelMouseListener(HandPanel handPanel) {
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
                + (color == Color.RED ? "RED" : "BLUE"));
      }
    }
  }

  public static void main(String[] args) {
    String path = "docs" + File.separator + "deck.config";
    PawnGameImpl model = new PawnGameImpl(3, 5);
    model.startGame(path, 5);


    model.drawCard(Player.RED);

    // Create a player (either RED or BLUE)
    Player player = Player.RED;

    // Create the HandPanel
    HandPanel handPanel = new HandPanel(model, player);

    // Create the frame to display the HandPanel
    JFrame frame = new JFrame("HandPanel Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1200, 200);
    frame.add(handPanel);
    frame.setVisible(true);
  }
}






