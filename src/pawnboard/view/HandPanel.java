package pawnboard.view;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import pawnboard.model.Card;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;




public class HandPanel extends JPanel {
  private final List<Card> hand;
  private final Color color;  // Color for the player
  private static final int PANEL_HEIGHT = 200;
  private static final int PANEL_WIDTH = 1200;
  private static final int TEXT_HEIGHT = 50;  // If needed, this can be adjusted or removed
  private static final int CARD_BORDER_THICKNESS = 3;  // Border thickness between cards
  private int selectedCardIndex = -1;  // Index of the selected card (-1 means no card selected)

  public HandPanel(ReadonlyPawnsGame<PawnCell> model, Player player) {
    super();
    // Assign hand and color based on the player
    if (player.equals(Player.RED)) {
      this.hand = model.getHand(Player.RED);
      this.color = Color.RED;
    } else {
      this.hand = model.getHand(Player.BLUE);
      this.color = Color.BLUE;
    }

    setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    setBackground(Color.BLACK);

    // Add MouseListener to this HandPanel instance
    this.addMouseListener(new HandPanelMouseListener(this));
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int handSize = hand.size();
    if (handSize == 0) return;

    // Calculate card width to fill the entire width of the panel
    int cardWidth = (PANEL_WIDTH - (handSize - 1) * CARD_BORDER_THICKNESS) / handSize;
    int cardHeight = PANEL_HEIGHT; // Full height of the panel

    // Draw each card with calculated width and height
    for (int i = 0; i < handSize; i++) {
      Card card = hand.get(i);
      int xPosition = i * (cardWidth + CARD_BORDER_THICKNESS);  // Position of the card along the x-axis

      // Draw the card border (to separate cards visually)
      g.setColor(Color.BLACK);
      g.fillRect(xPosition, 0, cardWidth, cardHeight);  // Border around each card

      // Determine the color for the card: cyan if selected, otherwise the player's color
      if (i == selectedCardIndex) {
        g.setColor(Color.CYAN);  // Highlight selected cards with cyan
      } else {
        g.setColor(color);  // Use the player's color for unselected cards
      }
      g.fillRect(xPosition + CARD_BORDER_THICKNESS, 0, cardWidth - CARD_BORDER_THICKNESS, cardHeight);  // Card background

      // Draw card text (Name, Cost, and Value) at the top
      g.setColor(Color.WHITE);  // Text color
      g.drawString(card.getName(), xPosition + CARD_BORDER_THICKNESS + 5, 20);
      g.drawString("Cost: " + card.getPawnCost(), xPosition + CARD_BORDER_THICKNESS + 5, 35);
      g.drawString("Value: " + card.getValue(), xPosition + CARD_BORDER_THICKNESS + 5, 50);

      // Get influence grid (5x5)
      char[][] grid = card.getInfluenceGrid();
      int cellSize = (cardWidth - 2 * CARD_BORDER_THICKNESS) / 6; // Adjust cell size for grid

      int startY = 50; // Start drawing grid below text area

      // Draw influence grid inside card
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

  // Add mouse listener for card selection
  public void toggleSelection(int index) {
    if (index == selectedCardIndex) {
      // Deselect the card if it is already selected
      selectedCardIndex = -1;
    } else {
      // Select the new card and update the old one to its original color
      selectedCardIndex = index;
    }
    repaint();  // Repaint the panel to reflect the changes
  }

  // Getters for the selected card index
  public int getSelectedCardIndex() {
    return selectedCardIndex;
  }


  public class HandPanelMouseListener extends MouseAdapter {
    private final HandPanel handPanel;

    public HandPanelMouseListener(HandPanel handPanel) {
      this.handPanel = handPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      // Get the x and y positions of the mouse click
      int x = e.getX();


      int cardWidth = (PANEL_WIDTH - (handPanel.hand.size() - 1) * CARD_BORDER_THICKNESS) / handPanel.hand.size();
      int cardIndex = x / (cardWidth + CARD_BORDER_THICKNESS);

      // Toggle the selection of the card
      handPanel.toggleSelection(cardIndex);
    }
  }
  

}











