package pawnboard.model;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import pawnboard.influences.AddPawnInfluence;
import pawnboard.influences.ChangeOwnershipInfluence;
import pawnboard.influences.NoEffectInfluence;

import static org.junit.Assert.*;

public class CardInitializerTest {

  @Test
  public void testLoadDeckFromFile() throws IOException {
    // Create a temporary file with sample card data
    File tempFile = File.createTempFile("deck", ".txt");
    tempFile.deleteOnExit(); // Ensure deletion after test

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
      writer.write("Security 1 2\n");
      writer.write("Security\n");
      writer.write("1 2\n");
      writer.write("Security\n");
      writer.write("1 2\n");
      writer.write("XXXXX\n");
      writer.write("XXIXX\n");
      writer.write("XICIX\n");
      writer.write("XXIXX\n");
      writer.write("XXXXX\n");
      writer.write("A N C\n"); // Influence effects: AddPawnInfluence, NoEffectInfluence, ChangeOwnershipInfluence
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
    char[][] expectedGrid = {
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'I', 'C', 'I', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'}
    };

    assertArrayEquals(expectedGrid, card.getInfluenceGrid());

    // Check influence effects
    assertEquals(3, card.getInfluenceEffects().size());
    assertTrue(card.getInfluenceEffects().get(0) instanceof AddPawnInfluence);
    assertTrue(card.getInfluenceEffects().get(1) instanceof NoEffectInfluence);
    assertTrue(card.getInfluenceEffects().get(2) instanceof ChangeOwnershipInfluence);
  }

  @Test
  public void testLoadMultipleCardsFromFile() throws IOException {
    // Create a temporary file with multiple card entries
    File tempFile = File.createTempFile("deck", ".txt");
    tempFile.deleteOnExit(); // Ensure cleanup after test

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
      // First card: Security
      writer.write("Security 1 2\n");
      writer.write("Security\n");
      writer.write("1 2\n");
      writer.write("Security\n");
      writer.write("1 2\n");
      writer.write("XXXXX\n");
      writer.write("XXIXX\n");
      writer.write("XICIX\n");
      writer.write("XXIXX\n");
      writer.write("XXXXX\n");
      writer.write("A N\n"); // Influence effects

      // Second card: Bee
      writer.write("Bee 1 1\n");
      writer.write("Bee\n");
      writer.write("1 1\n");
      writer.write("Bee\n");
      writer.write("1 1\n");
      writer.write("XXIXX\n");
      writer.write("XXXXX\n");
      writer.write("XXCXX\n");
      writer.write("XXXXX\n");
      writer.write("XXIXX\n");
      writer.write("C A\n"); // Influence effects
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

    char[][] expectedGrid1 = {
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'I', 'C', 'I', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'}
    };
    assertArrayEquals(expectedGrid1, card1.getInfluenceGrid());
    assertEquals(2, card1.getInfluenceEffects().size());
    assertTrue(card1.getInfluenceEffects().get(0) instanceof AddPawnInfluence);
    assertTrue(card1.getInfluenceEffects().get(1) instanceof NoEffectInfluence);

    // Verify second card
    PawnCard card2 = deck.get(1);
    assertEquals("Bee", card2.getName());
    assertEquals(1, card2.getPawnCost());
    assertEquals(1, card2.getValue());

    char[][] expectedGrid2 = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'C', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };
    assertArrayEquals(expectedGrid2, card2.getInfluenceGrid());
    assertEquals(2, card2.getInfluenceEffects().size());
    assertTrue(card2.getInfluenceEffects().get(0) instanceof ChangeOwnershipInfluence);
    assertTrue(card2.getInfluenceEffects().get(1) instanceof AddPawnInfluence);
  }

  @Test
  public void testLoadMultipleCards() {
    File file = new File("C:\\Users\\pshan\\OneDrive\\Desktop\\cs3500\\CS3500Assignment5\\src\\pawnboard\\model\\CardList");
    assertTrue(file.exists());

    List<PawnCard> deck = CardInitializer.initializeDeck(file.getAbsolutePath());

    assertNotNull(deck);
    assertEquals(20, deck.size());

    PawnCard firstCard = deck.get(0);
    assertEquals("Security", firstCard.getName());
    assertEquals(1, firstCard.getPawnCost());
    assertEquals(2, firstCard.getValue());
    assertEquals(3, firstCard.getInfluenceEffects().size());

    PawnCard lobber = deck.get(3);
    assertEquals("Lobber", lobber.getName());
    assertEquals(2, lobber.getPawnCost());
    assertEquals(1, lobber.getValue());
    assertEquals(3, lobber.getInfluenceEffects().size());

    assertTrue(firstCard.getInfluenceEffects().get(0) instanceof AddPawnInfluence);
    assertTrue(firstCard.getInfluenceEffects().get(1) instanceof NoEffectInfluence);
    assertTrue(firstCard.getInfluenceEffects().get(2) instanceof ChangeOwnershipInfluence);
  }






}