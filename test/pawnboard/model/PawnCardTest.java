package pawnboard.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import pawnboard.influences.InfluenceEffect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PawnCardTest {

  private char[][] influenceGrid = {
          {'X', 'X', 'I', 'X', 'X'},
          {'X', 'I', 'I', 'I', 'X'},
          {'I', 'I', 'C', 'I', 'I'},
          {'X', 'I', 'I', 'I', 'X'},
          {'X', 'X', 'I', 'X', 'X'}
  };

  List<InfluenceEffect> emptyEffects = new ArrayList<>();

  @Test
  public void testInvalidCost() {
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", 4, 1,
            influenceGrid, emptyEffects));
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", 0, 1,
            influenceGrid, emptyEffects));
  }

  @Test
  public void testInvalidValue() {
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", 3, 0,
            influenceGrid, emptyEffects));
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
            influenceGrid, emptyEffects));
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
            influenceGrid2, emptyEffects));
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", 3, 1,
            influenceGrid, emptyEffects));
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
            influenceGrid, emptyEffects));
  }

  @Test
  public void testValidCardCreation() {
    PawnCard card = new PawnCard("TestCard", 2, 5, influenceGrid, emptyEffects);

    assertEquals("TestCard", card.getName());
    assertEquals(2, card.getPawnCost());
    assertEquals(5, card.getValue());
    assertEquals(influenceGrid, card.getInfluenceGrid());
  }

  @Test
  public void testAffectsCell() {
    PawnCard card = new PawnCard("TestCard", 2, 5, influenceGrid,
            new ArrayList<>());

    assertTrue(card.affectsCell(0, 2));
    assertFalse(card.affectsCell(0, 0));
  }

  @Test
  public void testCardGetValue() {
    PawnCard card = new PawnCard("TestCard", 2, 5, influenceGrid, emptyEffects);
    assertEquals(5, card.getValue());

  }

  // create tests for the apply card influence from card

}