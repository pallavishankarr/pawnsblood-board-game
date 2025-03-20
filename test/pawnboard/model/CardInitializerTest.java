package pawnboard.model;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;



import pawnboard.influences.DefaultInfluence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the Card Intializer Implementation.
 */
public class CardInitializerTest {

  @Test
  public void testLoadDeckFromFile() throws IOException {
    File tempFile = File.createTempFile("deck", ".txt");
    tempFile.deleteOnExit();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
      writer.write("Security 1 2\n");
      writer.write("XXXXX\n");
      writer.write("XXIXX\n");
      writer.write("XICIX\n");
      writer.write("XXIXX\n");
      writer.write("XXXXX\n");
    }

    // Load deck from file
    List<PawnCard> deck = CardInitializer.initializeDeck(tempFile.getAbsolutePath());

    // Assertions to check correctness
    assertEquals(1, deck.size());
    PawnCard card = deck.get(0);
    assertEquals("Security", card.getName());
    assertEquals(1, card.getPawnCost());
    assertEquals(2, card.getValue());

    // Expected influence grid
    InfluenceType[][] expectedGrid = {
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.I, InfluenceType.C, InfluenceType.I, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X}
    };

    assertArrayEquals(expectedGrid, card.getInfluenceGrid());
  }

  @Test
  public void testLoadMultipleCardsFromFile() throws IOException {
    File tempFile = File.createTempFile("deck", ".txt");
    tempFile.deleteOnExit();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
      // First card: Security
      writer.write("Security 1 2\n");
      writer.write("XXXXX\n");
      writer.write("XXIXX\n");
      writer.write("XICIX\n");
      writer.write("XXIXX\n");
      writer.write("XXXXX\n");

      // Second card: Bee
      writer.write("Bee 1 1\n");
      writer.write("XXIXX\n");
      writer.write("XXXXX\n");
      writer.write("XXCXX\n");
      writer.write("XXXXX\n");
      writer.write("XXIXX\n");
    }

    // Load deck from file
    List<PawnCard> deck = CardInitializer.initializeDeck(tempFile.getAbsolutePath());

    // Assertions to check correctness
    assertEquals(2, deck.size());

    // Verify first card
    PawnCard card1 = deck.get(0);
    assertEquals("Security", card1.getName());
    assertEquals(1, card1.getPawnCost());
    assertEquals(2, card1.getValue());

    InfluenceType[][] expectedGrid1 = {
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.I, InfluenceType.C, InfluenceType.I, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X}
    };
    assertArrayEquals(expectedGrid1, card1.getInfluenceGrid());

    // Verify second card
    PawnCard card2 = deck.get(1);
    assertEquals("Bee", card2.getName());
    assertEquals(1, card2.getPawnCost());
    assertEquals(1, card2.getValue());

    InfluenceType[][] expectedGrid2 = {
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.C, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X}
    };
    assertArrayEquals(expectedGrid2, card2.getInfluenceGrid());
  }


  @Test
  public void testLoadMultipleCards() {
    String path = "docs" + File.separator + "deck.config";

    List<PawnCard> deck = CardInitializer.initializeDeck(path);

    assertNotNull(deck);
    assertEquals(20, deck.size());

    PawnCard firstCard = deck.get(0);
    assertEquals("Security", firstCard.getName());
    assertEquals(1, firstCard.getPawnCost());
    assertEquals(2, firstCard.getValue());
    assertEquals(1, firstCard.getInfluenceEffects().size());

    PawnCard lobber = deck.get(3);
    assertEquals("Lobber", lobber.getName());
    assertEquals(2, lobber.getPawnCost());
    assertEquals(1, lobber.getValue());
    assertEquals(1, lobber.getInfluenceEffects().size());

    assertTrue(firstCard.getInfluenceEffects().get(0) instanceof DefaultInfluence);
  }






}