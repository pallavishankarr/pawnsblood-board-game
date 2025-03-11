package pawnboard.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pawnboard.influences.AddPawnInfluence;
import pawnboard.influences.ChangeOwnershipInfluence;
import pawnboard.influences.InfluenceEffect;
import pawnboard.influences.NoEffectInfluence;

// reads the card file and makes a deck
public class CardInitializer {
  public static List<PawnCard> initializeDeck(String filename) {
    List<PawnCard> deck = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = reader.readLine()) != null) {

        // Parse name, cost, and value from the same line
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) throw new IOException("Invalid format: Expected '<Name> <Cost> <Value>'");

        String name = parts[0];
        int cost = Integer.parseInt(parts[1]);
        int value = Integer.parseInt(parts[2]);

        // Read the 5x5 influence grid

        String name = line.trim();
        if (name.isEmpty()) continue; // Skip empty lines

        line = reader.readLine();
        if (line == null) throw new IOException("Unexpected file format: missing cost and value.");
        String[] parts = line.split(" ");
        if (parts.length != 2) throw new IOException("Invalid format for cost and value.");

        int cost = Integer.parseInt(parts[0]);
        int value = Integer.parseInt(parts[1]);


        char[][] influenceGrid = new char[5][5];
        for (int i = 0; i < 5; i++) {
          line = reader.readLine();
          if (line == null || line.length() != 5) throw new IOException("Invalid grid format.");
          influenceGrid[i] = line.toCharArray();
        }

        // Read and parse the influence effects line
        line = reader.readLine();
        if (line == null) throw new IOException("Missing influence effect line.");

        String[] effectInitials = line.trim().split("\\s+");

        String[] effectInitials = line.split(" ");

        String[] effectInitials = line.split(" ");


        List<InfluenceEffect> effects = new ArrayList<>();
        for (String effectInitial : effectInitials) {
          switch (effectInitial) {
            case "A":
              effects.add(new AddPawnInfluence());
              break;
            case "N":
              effects.add(new NoEffectInfluence());
              break;
            case "C":
              effects.add(new ChangeOwnershipInfluence());
              break;
            default:
              throw new IllegalArgumentException("Invalid influence effect: " + effectInitial);
          }
        }


        // Add card to the deck

        deck.add(new PawnCard(name, cost, value, influenceGrid, effects));
      }
    } catch (IOException | NumberFormatException e) {
      System.err.println("Error reading deck file: " + e.getMessage());
    }
    return deck;
  }
}