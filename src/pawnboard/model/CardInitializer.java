package pawnboard.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Initializes a deck of PawnCards from a given a file path.
 * Turns file path into a configuration that can then be read.
 * Reads the name, value, cost, and influence grid.
 */
public class CardInitializer {

  /**
   * Reads a configuration file and initializes a list of PawnCards.
   * @param path the file path to the deck
   * @return a list of pawn cards created
   */
  public static List<PawnCard> initializeDeck(String path) {
    List<PawnCard> deck = new ArrayList<>();
    File config = new File(path);

    try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
      String line;
      while ((line = reader.readLine()) != null) {
        // parse name and cost
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) throw new IOException("Invalid format: Expected '<Name> <Cost> <Value>'");

        String name = parts[0];
        int cost = Integer.parseInt(parts[1]);
        int value = Integer.parseInt(parts[2]);

        // read the grid
        char[][] influenceGrid = new char[5][5];
        for (int i = 0; i < 5; i++) {
          line = reader.readLine();
          if (line == null || line.length() != 5) throw new IOException("Invalid grid format.");
          influenceGrid[i] = line.toCharArray();
        }

        // add card to deck after parsing everything
        deck.add(new PawnCard(name, cost, value, influenceGrid));
      }
    } catch (IOException | NumberFormatException e) {
      System.err.println("Error reading deck file: " + e.getMessage());
    }
    return deck;
  }
}