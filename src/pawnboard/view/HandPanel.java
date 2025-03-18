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
  private final Dimension logicalSize = new Dimension(1200, 200);
  private static final int CARD_BORDER_THICKNESS = 3;
  private int selectedCardIndex = -1;

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

    setPreferredSize(new Dimension((int)logicalSize.getWidth(), (int)logicalSize.getHeight()));
    setBackground(Color.BLACK);

    // Add MouseListener to this HandPanel instance
    this.addMouseListener(new HandPanelMouseListener(this));
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int handSize = hand.size();


    int cardWidth = (int)logicalSize.getWidth() / handSize;
    int cardHeight = (int)logicalSize.getHeight();


    for (int i = 0; i < handSize; i++) {
      Card card = hand.get(i);
      int xPosition = i * (cardWidth + CARD_BORDER_THICKNESS);


      g.setColor(Color.BLACK);
      g.fillRect(xPosition, 0, cardWidth, cardHeight);

      if (i == selectedCardIndex) {
        g.setColor(Color.CYAN);
      } else {
        g.setColor(color);
      }
      g.fillRect(xPosition + CARD_BORDER_THICKNESS, 0, cardWidth - CARD_BORDER_THICKNESS, cardHeight);  // Card background


      g.setColor(Color.WHITE);
      g.drawString(card.getName(), xPosition + CARD_BORDER_THICKNESS + 5, 20);
      g.drawString("Cost: " + card.getPawnCost(), xPosition + CARD_BORDER_THICKNESS + 5, 35);
      g.drawString("Value: " + card.getValue(), xPosition + CARD_BORDER_THICKNESS + 5, 50);

      // Get influence grid (5x5)
      char[][] grid = card.getInfluenceGrid();
      int cellSize = (cardWidth - 2 * CARD_BORDER_THICKNESS) / 6;

      int startY = 50;

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

  @Override
  public void updateBoard() {
    repaint();
  }

  @Override
  public void highlightCell(int row, int col) {

  }

  @Override
  public void clearHighlight() {

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


      int cardWidth = ((int)logicalSize.getWidth()- (handPanel.hand.size() - 1) * CARD_BORDER_THICKNESS) / handPanel.hand.size();
      int cardIndex = x / (cardWidth + CARD_BORDER_THICKNESS);

      // Toggle the selection of the card
      handPanel.toggleSelection(cardIndex);
    }
  }


  public static void main(String[] args) {


    String path = "docs" + File.separator + "deck.config";
   PawnGameImpl model = new PawnGameImpl(3,5);

   model.startGame(path, 5);


    // Create a player (either RED or BLUE)
    Player player = Player.RED;

    // Create the HandPanel
    HandPanel handPanel = new HandPanel(model, player);

    // Create the frame to display the HandPanel
    JFrame frame = new JFrame("HandPanel Test");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1300, 300);
    frame.add(handPanel);
    frame.setVisible(true);
  }


}











