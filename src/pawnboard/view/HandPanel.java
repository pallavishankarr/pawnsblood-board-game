package pawnboard.view;


import javax.swing.*;
import java.awt.*;
import java.util.List;

import pawnboard.model.Card;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;


public class HandPanel extends JPanel {
  private List<Card> hand;
  private CardPanel selectedCardPanel = null; // Track the selected card panel

  public HandPanel(ReadonlyPawnsGame<PawnCell> model) {
    super();
    this.hand = model.getHand(Player.RED);
    setLayout(new GridLayout(1, hand.size(), 3, 0)); // Small black gap between cards
    setBackground(Color.BLACK); // Black background behind cards
    displayHand();
  }

  public void displayHand() {
    removeAll(); // Clear previous cards
    for (Card card : hand) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
          handleCardClick(cardPanel);
        }
      });
      add(cardPanel); // Add each card panel
    }
    revalidate();
    repaint();
  }

  private void handleCardClick(CardPanel clickedCardPanel) {
    // If the clicked card is already selected, unselect it (turn it back to blue)
    if (clickedCardPanel == selectedCardPanel) {
      clickedCardPanel.toggleSelection(false); // Deselect the card
      selectedCardPanel = null;
    } else {
      // Deselect the currently selected card (if any)
      if (selectedCardPanel != null) {
        selectedCardPanel.toggleSelection(false); // Turn it back to blue
      }

      // Select the new card
      clickedCardPanel.toggleSelection(true); // Turn it cyan
      selectedCardPanel = clickedCardPanel;
    }

    repaint(); // Repaint the panel to reflect changes
  }
}















