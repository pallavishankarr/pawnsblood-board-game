package pawnboard.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Initializes a deck of PawnCards from a given a file path.
 * Turns file path into a configuration that can then be read.
 * Reads the name, value, cost, and influence grid.
 */
public class CardInitializer {

  /**
   * Reads a configuration file and initializes a list of PawnCards.
   *
   * @param path the file path to the deck
   * @return a list of pawn cards created
   */
  public static List<PawnCard> initializeDeck(String path) {
    List<PawnCard> deck = new ArrayList<>();
    Map<String, Integer> cardCountMap = new HashMap<>();
    File config = new File(path);

    try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
      String line;
      while ((line = reader.readLine()) != null) {
        // parse name and cost
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
          throw new IOException("Invalid format: Expected '<Name> <Cost> <Value>'");
        }

        String name = parts[0];
        int costValue = Integer.parseInt(parts[1]);
        int value = Integer.parseInt(parts[2]);

        CostEnum cost = convertToCostEnum(costValue);

        // read the grid
        InfluenceType[][] influenceGrid = new InfluenceType[5][5];
        for (int i = 0; i < 5; i++) {
          line = reader.readLine();
          if (line == null || line.length() != 5) {
            throw new IOException("Invalid grid format.");
          }
          for (int j = 0; j < 5; j++) {
            char symbol = line.charAt(j);
            influenceGrid[i][j] = convertToInfluenceType(symbol);
          }
        }

        if (cardCountMap.getOrDefault(name, 0) < 2) {
          deck.add(new PawnCard(name, cost, value, influenceGrid));
          cardCountMap.put(name, cardCountMap.getOrDefault(name, 0) + 1);
        }
      }
    } catch (IOException | NumberFormatException e) {
      System.err.println("Error reading deck file: " + e.getMessage());
    }
    return deck;
  }


  private static InfluenceType convertToInfluenceType(char symbol) {
    switch (symbol) {
      case 'X':
        return InfluenceType.X;
      case 'I':
        return InfluenceType.I;
      case 'C':
        return InfluenceType.C;
      default:
        throw new IllegalArgumentException("Invalid InfluenceType character: " + symbol);
    }
  }

  private static CostEnum convertToCostEnum(int costValue) {
    for (CostEnum cost : CostEnum.values()) {
      if (cost.getValue() == costValue) {
        return cost;
      }
    }
    throw new IllegalArgumentException("Invalid cost value: " + costValue);
  }
}