package pawnboard.controller;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import pawnboard.model.CardInitializer;
import pawnboard.model.Cell;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.PawnsGame;
import pawnboard.view.BlueFrame;
import pawnboard.view.GameWindow;

import pawnboard.view.RedFrame;



public class PawnsGameController implements GameController {
  private final Readable input;
  private final Appendable output;

  public PawnsGameController(Readable input, Appendable output) {
    if (input == null || output == null) {
      throw new IllegalArgumentException("Input and output must not be null");
    }
    this.input = input;
    this.output = output;
  }

  @Override
  public <C extends Cell> void playGame(PawnsGame<PawnCell> model, GameWindow redView,
                                        GameWindow blueView, String redFilePath,
                                        String blueFilePath, int handSize) {
    if (model == null || redView == null || blueView == null ||
            redFilePath == null || blueFilePath == null) {
      throw new IllegalArgumentException("Invalid arguments provided.");
    }
    if (handSize < 1) {
      throw new IllegalArgumentException("Hand size must be greater than 0");
    }

    // Initialize decks and start the game
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(redFilePath, blueFilePath);
    List<PawnCard> deck1 = decks.get("deck1");
    List<PawnCard> deck2 = decks.get("deck2");
    model.startGame(deck1, deck2, handSize);

    Scanner sc = new Scanner(input);

    // Update GUI before the game starts
    redView.updateView();
    blueView.updateView();

    while (!model.isGameOver()) {
      try {
        output.append("Enter a command (place card index row col, pass, quit): \n");
        output.append(model.toString()).append("\n");

        if (!sc.hasNext()) {
          break;
        }

        String command = sc.nextLine().trim();
        String[] parts = command.split(" ");

        if (command.equalsIgnoreCase("quit")) {
          output.append("Game ended by user.\n");
          break;
        } else if (command.equalsIgnoreCase("pass")) {
          model.passTurn();
        } else if (parts.length == 4 && parts[0].equalsIgnoreCase("place")) {
          try {
            int cardIndex = Integer.parseInt(parts[1]);
            int row = Integer.parseInt(parts[2]);
            int col = Integer.parseInt(parts[3]);
            model.placeCardAt(model.getCurrentPlayer(), cardIndex, row, col);
          } catch (NumberFormatException e) {
            output.append("Invalid move format. Use: place <index> <row> <col>\n");
          }
        } else {
          output.append("Invalid command. Try again.\n");
        }

        // Update both GUI frames
        redView.updateView();
        blueView.updateView();

      } catch (IOException e) {
        throw new IllegalStateException("Failed to write to output", e);
      }
    }

    try {
      output.append("Game Over!\n");
      output.append("Winner: ").append(model.getWinner()).append("\n");
    } catch (IOException e) {
      throw new IllegalStateException("Failed to write to output", e);
    }
  }



  public static void main(String[] args) {
    String simulatedInput = "place 0 0 0\npass\nquit\n";
    Readable input = new StringReader(simulatedInput);
    Appendable output = new StringWriter();

    String deckPath = "docs" + File.separator + "biggerDeck";
    CardInitializer cardInitializer = new CardInitializer();
    Map<String, List<PawnCard>> decks = cardInitializer.initializeDeck(deckPath, deckPath);
    List<PawnCard> deck = decks.get("deck1");

    PawnGameImpl model = new PawnGameImpl(5, 7);
    GameControllerImpl controller = new GameControllerImpl(input, output);

    GameWindow redView = new RedFrame(model);
    GameWindow blueView = new BlueFrame(model);

    controller.playGame(model, redView, blueView, deckPath, deckPath, 5);

    System.out.println(output.toString());
  }



}






