package pawnboard.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pawnboard.influences.DecreaseInfluence;
import pawnboard.influences.InfluenceEffect;
import pawnboard.influences.UpgradeInfluence;
import pawnboard.model.Cost;
import pawnboard.model.InfluenceType;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;

/**
 * Initializes a deck of PawnCards from a given a file path.
 * Turns file path into a configuration that can then be read.
 * Reads the name, value, cost, and influence grid.
 */
public class CardInitializer {

  /**
   * Reads a configuration file and initializes a list of PawnCards.
   *
   * @param path1 the file path to the deck
   * @param path2 the second file path of the deck
   * @return a list of pawn cards created
   */
  public static Map<String, List<PawnCard>> initializeDeck(String path1, String path2) {


    List<PawnCard> deck1  = new ArrayList<>();
    List<PawnCard> deck2 = new ArrayList<>();
    Map<String, List<PawnCard>> decks = new HashMap<>();

    deck1 = readDeckFromFile(path1);
    deck2 = readDeckFromFile(path2);

    if (areDecksIdentical(deck1, deck2)) {

      deck2 = mirrorDeck(deck2);

      decks.put("deck1", deck1);
      decks.put("deck2", deck2);
    }

    else {
      decks.put("deck1", deck1);
      decks.put("deck2", deck2);
    }
    return decks;
  }

  private static List<PawnCard> readDeckFromFile(String path) {
    List<PawnCard> deck = new ArrayList<>();
    Map<String, Integer> cardCountMap = new HashMap<>();
    File config = new File(path);

    try (BufferedReader reader = new BufferedReader(new FileReader(config))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
          throw new IOException("Invalid format: Expected '<Name> <Cost> <Value>'");
        }

        String name = parts[0];
        int costValue = Integer.parseInt(parts[1]);
        int value = Integer.parseInt(parts[2]);
        Cost cost = convertToCostEnum(costValue);

        List<InfluenceEffect<PawnCell, Player, PawnCard>> rawEffects = new ArrayList<>();
        InfluenceType[][] influenceGrid = readInfluenceGrid(reader, rawEffects);

        Set<InfluenceEffect<PawnCell, Player, PawnCard>> uniqueEffects = new HashSet<>();
        for (InfluenceEffect<PawnCell, Player, PawnCard> effect : rawEffects) {
          if (effect instanceof UpgradeInfluence
                  && !isInfluenceEffectPresent(uniqueEffects, UpgradeInfluence.class)) {
            uniqueEffects.add(effect);
          } else if (effect instanceof DecreaseInfluence
                  && !isInfluenceEffectPresent(uniqueEffects, DecreaseInfluence.class)) {
            uniqueEffects.add(effect);
          }
        }

        if (cardCountMap.getOrDefault(name, 0) < 2) {
          PawnCard card = new PawnCard(name, cost, value, influenceGrid);
          card.getInfluenceEffects().addAll(uniqueEffects);
          deck.add(card);
          cardCountMap.put(name, cardCountMap.getOrDefault(name, 0) + 1);
        }
      }
    } catch (IOException | NumberFormatException e) {
      System.err.println("Error reading deck file: " + e.getMessage());
    }

    return deck;
  }

  private static boolean isInfluenceEffectPresent(Set<InfluenceEffect<PawnCell,
          Player, PawnCard>> effects, Class<? extends InfluenceEffect> effectClass) {
    for (InfluenceEffect<PawnCell, Player, PawnCard> effect : effects) {
      if (effect.getClass().equals(effectClass)) {
        return true; // The effect is already present
      }
    }
    return false;
  }

  private static InfluenceType[][] readInfluenceGrid(BufferedReader reader,
                                                     List<InfluenceEffect<PawnCell, Player, PawnCard>> effects)
          throws IOException {
    InfluenceType[][] influenceGrid = new InfluenceType[5][5];
    for (int i = 0; i < 5; i++) {
      String line = reader.readLine();
      if (line == null || line.length() != 5) {
        throw new IOException("Invalid grid format.");
      }
      for (int j = 0; j < 5; j++) {
        char symbol = line.charAt(j);
        influenceGrid[i][j] = convertToInfluenceType(symbol);
        // Only add Upgrade or Decrease influence effects
        if (symbol == 'U') {
          effects.add(new UpgradeInfluence());
        } else if (symbol == 'D') {
          effects.add(new DecreaseInfluence());
        }
      }
    }
    return influenceGrid;
  }

  private static InfluenceType convertToInfluenceType(char symbol) {
    switch (symbol) {
      case 'X':
        return InfluenceType.X;
      case 'I':
        return InfluenceType.I;
      case 'C':
        return InfluenceType.C;
      case 'U':
        return InfluenceType.U;
      case 'D':
        return InfluenceType.D;
      default:
        throw new IllegalArgumentException("Invalid InfluenceType character: " + symbol);
    }
  }

  private static Cost convertToCostEnum(int costValue) {
    for (Cost cost : Cost.values()) {
      if (cost.getValue() == costValue) {
        return cost;
      }
    }
    throw new IllegalArgumentException("Invalid cost value: " + costValue);
  }


  private static boolean areDecksIdentical(List<PawnCard> deck1, List<PawnCard> deck2) {
    for (int i = 0; i < deck1.size(); i++) {
      PawnCard card1 = deck1.get(i);
      PawnCard card2 = deck2.get(i);

      if (!card1.getName().equals(card2.getName()) ||
              card1.getPawnCost() != card2.getPawnCost() ||
              card1.getValue() != card2.getValue() ||
              !areGridsIdentical(card1.getInfluenceGrid(), card2.getInfluenceGrid())) {
        return false;
      }
    }
    return true;
  }

  private static boolean areGridsIdentical(InfluenceType[][] grid1, InfluenceType[][] grid2) {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (grid1[i][j] != grid2[i][j]) {
          return false;
        }
      }
    }
    return true;
  }

  private static List<PawnCard> mirrorDeck(List<PawnCard> deck) {
    List<PawnCard> mirroredDeck = new ArrayList<>();
    for (PawnCard card : deck) {
      InfluenceType[][] mirroredGrid = mirrorGrid(card.getInfluenceGrid());
      mirroredDeck.add(new PawnCard(card.getName(),
              card.getPawnCost(), card.getValue(), mirroredGrid));
    }
    return mirroredDeck;
  }

  private static InfluenceType[][] mirrorGrid(InfluenceType[][] grid) {
    int size = grid.length;
    InfluenceType[][] mirrored = new InfluenceType[size][size];

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        mirrored[i][j] = grid[i][size - 1 - j];
      }
    }

    return mirrored;
  }


}