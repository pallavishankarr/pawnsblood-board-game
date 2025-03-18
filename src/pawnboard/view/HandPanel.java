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
  private CardPanel selectedCardPanel = null;

  public HandPanel(ReadonlyPawnsGame<PawnCell> model) {
    super();
    this.hand = model.getHand(Player.RED);
    setLayout(new GridLayout(1, hand.size(), 3, 0));
    setBackground(Color.BLACK);
    displayHand();
  }

  public void displayHand() {
    removeAll();
    for (Card card : hand) {
      CardPanel cardPanel = new CardPanel(card);
      cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
          handleCardClick(cardPanel);
        }
      });
      add(cardPanel);
    }
    revalidate();
    repaint();
  }

  private void handleCardClick(CardPanel clickedCardPanel) {

    if (clickedCardPanel == selectedCardPanel) {
      clickedCardPanel.toggleSelection(false);
      selectedCardPanel = null;
    } else {

      if (selectedCardPanel != null) {
        selectedCardPanel.toggleSelection(false);
      }


      clickedCardPanel.toggleSelection(true);
      selectedCardPanel = clickedCardPanel;
    }

    repaint();
  }
}















