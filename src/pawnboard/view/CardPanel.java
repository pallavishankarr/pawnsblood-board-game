package pawnboard.view;

import javax.swing.*;
import java.awt.*;

import pawnboard.model.Card;

public class CardPanel extends JPanel {
  private final Card card;
  private static final int CELL_SIZE = 30;
  private static final int GRID_SIZE = 5;
  private static final int TEXT_HEIGHT = 50;
  private static final int BORDER_THICKNESS = 3;
  private boolean selected = false;
  private Color color;

  public CardPanel(Card card, Color color) {
    this.card = card;
    this.color = color;


    setPreferredSize(new Dimension(
            GRID_SIZE * CELL_SIZE + 2 * BORDER_THICKNESS,
            GRID_SIZE * CELL_SIZE + TEXT_HEIGHT + 2 * BORDER_THICKNESS));

    setBackground(Color.BLACK);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);


    g.setColor(Color.BLACK);
    g.fillRect(0, 0, getWidth(), getHeight());


    if (selected) {
      g.setColor(Color.CYAN);
    } else {
      g.setColor(color);
    }

    // Draw card background (Blue or Cyan depending on selection)
    g.fillRect(BORDER_THICKNESS, BORDER_THICKNESS,
            getWidth() - 2 * BORDER_THICKNESS, getHeight() - 2 * BORDER_THICKNESS);

    // Draw text (Card name, cost, value) at the top
    g.setColor(Color.WHITE);
    g.drawString(card.getName(), BORDER_THICKNESS + 5, 20);
    g.drawString("Cost: " + card.getPawnCost(), BORDER_THICKNESS + 5, 35);
    g.drawString("Value: " + card.getValue(), BORDER_THICKNESS + 5, 50);

    // Get influence grid (5x5)
    char[][] grid = card.getInfluenceGrid();

    // Start influence grid lower (after text)
    int startY = BORDER_THICKNESS + TEXT_HEIGHT;

    // Draw influence grid inside card
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        int x = BORDER_THICKNESS + col * CELL_SIZE;
        int y = startY + row * CELL_SIZE;

        // Default grid cell (Dark Gray)
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y, CELL_SIZE, CELL_SIZE);

        // Highlight Influence Cells
        if (grid[row][col] == 'I') {
          g.setColor(Color.CYAN);
          g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        } else if (grid[row][col] == 'C') {
          g.setColor(Color.ORANGE);
          g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }

        // Draw black border for each cell
        g.setColor(Color.BLACK);
        g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
      }
    }
  }


  public void toggleSelection(boolean isSelected) {
    this.selected = isSelected;
    repaint();
  }
}






