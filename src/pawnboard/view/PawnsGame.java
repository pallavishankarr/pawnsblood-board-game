package pawnboard.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;

public class PawnsGame {
    public static void main(String[] args) {

      String path = "docs" + File.separator + "deck.config";


      PawnGameImpl game = new PawnGameImpl(3, 5);
      game.startGame(path, 5);

      game.drawCard(Player.RED);


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

