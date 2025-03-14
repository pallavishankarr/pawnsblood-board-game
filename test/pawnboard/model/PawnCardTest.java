package pawnboard.model;

import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the Pawn Card Implementation.
 */
public class PawnCardTest {

  private char[][] influenceGrid;
  private PawnCard card;

  @Before
  public void setUp() {
    influenceGrid = new char[][] {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'I', 'I', 'I', 'X'},
            {'I', 'I', 'C', 'I', 'I'},
            {'X', 'I', 'I', 'I', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };
    card = new PawnCard("TestCard", 2, 5, influenceGrid);
  }

  @Test
  public void testInvalidCost() {
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", 4, 1,
            influenceGrid));
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", 0, 1,
            influenceGrid));
  }

  @Test
  public void testInvalidValue() {
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", 3, 0,
            influenceGrid));
  }

  @Test
  public void testInvalidRowsInfluenceGrid() {
    char[][] influenceGrid = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'I', 'I', 'I', 'X'},
            {'I', 'I', 'C', 'I', 'I'},
            {'X', 'I', 'I', 'I', 'X'},
    };
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", 3, 1,
            influenceGrid));
  }

  @Test
  public void testInvalidColumnsInfluenceGrid() {
    char[][] influenceGrid = {
            {'X', 'X', 'I', 'X'},
            {'X', 'I', 'I', 'I'},
            {'I', 'I', 'C', 'I'},
            {'X', 'I', 'I', 'I'},
            {'X', 'I', 'I', 'I'}
    };

    char[][] influenceGrid2 = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'I', 'I', 'I', 'X'},
            {'I', 'C', 'I', 'I', 'I'},
            {'X', 'I', 'I', 'I'},
            {'X', 'X', 'I', 'X', 'X'}
    };

    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", 3, 1,
            influenceGrid2));
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", 3, 1,
            influenceGrid));
  }

  @Test
  public void testInvalidCenterInfluenceGrid() {
    char[][] influenceGrid = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'I', 'I', 'I', 'X'},
            {'I', 'C', 'I', 'I', 'I'},
            {'X', 'I', 'I', 'I', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };

    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", 3, 1,
            influenceGrid));
  }

  @Test
  public void testValidCardCreation() {
    assertEquals("TestCard", card.getName());
    assertEquals(2, card.getPawnCost());
    assertEquals(5, card.getValue());
    assertEquals(influenceGrid, card.getInfluenceGrid());
  }

  @Test
  public void testAffectsCell() {
    assertTrue(card.affectsCell(0, 2));
    assertFalse(card.affectsCell(0, 0));
  }

  @Test
  public void testCardGetValue() {
    assertEquals(5, card.getValue());
  }

  @Test
  public void testCardEquality() {
    PawnCard sameCard = new PawnCard("TestCard", 2, 5, influenceGrid);
    assertTrue(sameCard.equals(card));
  }

  @Test
  public void testCardInequality() {
    char[][] influenceGrid2 = {
            {'X', 'X', 'I', 'X', 'X'},
            {'I', 'I', 'I', 'I', 'X'},
            {'I', 'I', 'C', 'I', 'I'},
            {'X', 'I', 'I', 'I', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };
    PawnCard card1 = new PawnCard("TestCard", 3, 5, influenceGrid);
    PawnCard card2 = new PawnCard("TestCard", 2, 10, influenceGrid);
    PawnCard card3 = new PawnCard("TestCard", 2, 5, influenceGrid2);
    PawnCard card4 = new PawnCard("DiffName", 2, 5, influenceGrid);

    assertFalse(card.equals(card1));
    assertFalse(card.equals(card2));
    assertFalse(card.equals(card3));
    assertFalse(card.equals(card4));
  }
}