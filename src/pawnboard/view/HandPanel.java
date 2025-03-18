package pawnboard.view;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

import pawnboard.model.Card;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;


public class HandPanel extends JPanel {
  private List<Card> hand;
  private CardPanel selectedCardPanel = null;
  private Color color;


  public HandPanel(ReadonlyPawnsGame<PawnCell> model) {
    super();
    if(model.getCurrentPlayer().equals(Player.RED)) {
      this.hand = model.getHand(Player.RED);
      this.color = Color.RED;
    }

    else {
      this.hand = model.getHand(Player.BLUE);
      this.color = Color.BLUE;
    }

    setLayout(new GridLayout(1, hand.size(), 3, 0));
    setBackground(Color.BLACK);
    displayHand();
  }

  public void displayHand() {
    removeAll();
    for (Card card : hand) {
      CardPanel cardPanel = new CardPanel(card, color);
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

  public static void main(String[] args) {

    String path = "docs" + File.separator + "deck.config";


    PawnGameImpl game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);
    System.out.print(game.getCurrentPlayer());

    game.drawCard(Player.RED);

    System.out.print(game.getCurrentPlayer());


    HandPanel handPanel = new HandPanel(game);


    JFrame frame = new JFrame("Hand Panel");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    frame.add(handPanel, BorderLayout.CENTER);


    frame.setPreferredSize(new Dimension(1200, 200));


    frame.pack();


    handPanel.displayHand();


    frame.setVisible(true);

  }

}















