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

  private InfluenceType[][] influenceGrid;
  private PawnCard card;

  @Before
  public void setUp() {
    influenceGrid = new InfluenceType[][] {
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.I, InfluenceType.I, InfluenceType.C, InfluenceType.I, InfluenceType.I},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X}
    };
    card = new PawnCard("TestCard", CostEnum.Two, 5, influenceGrid);
  }

  @Test
  public void testInvalidCost() {
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", null, 1,
            influenceGrid));
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", null, 1,
            influenceGrid));
  }

  @Test
  public void testInvalidValue() {
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", CostEnum.Three, 0,
            influenceGrid));
  }

  @Test
  public void testInvalidRowsInfluenceGrid() {
    InfluenceType[][] influenceGrid = {
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.I, InfluenceType.I, InfluenceType.C, InfluenceType.I, InfluenceType.I},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
    };
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", CostEnum.Three, 1,
            influenceGrid));
  }

  @Test
  public void testInvalidColumnsInfluenceGrid() {
    InfluenceType[][] influenceGrid = {
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X,},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I},
            {InfluenceType.I, InfluenceType.I, InfluenceType.C, InfluenceType.I},
            {InfluenceType.X,InfluenceType.I, InfluenceType.I, InfluenceType.I},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I}
    };

    InfluenceType[][] influenceGrid2 = {
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X,},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I,InfluenceType.I, InfluenceType.X},
            {InfluenceType.I, InfluenceType.C,InfluenceType.I, InfluenceType.I, InfluenceType.I},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I},
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X}
    };

    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", CostEnum.Three, 1,
            influenceGrid2));
    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", CostEnum.Three, 1,
            influenceGrid));
  }

  @Test
  public void testInvalidCenterInfluenceGrid() {
    InfluenceType[][] influenceGrid = {
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X,InfluenceType.X},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I,InfluenceType.I, InfluenceType.X},
            {InfluenceType.I, InfluenceType.C,InfluenceType.I, InfluenceType.I, InfluenceType.I},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I,InfluenceType.X },
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X, InfluenceType.X}
    };

    assertThrows(IllegalArgumentException.class, () -> new PawnCard("a", CostEnum.Three, 1,
            influenceGrid));
  }

  @Test
  public void testValidCardCreation() {
    assertEquals("TestCard", card.getName());
    assertEquals(CostEnum.Two, card.getPawnCost());
    assertEquals(2,card.getPawnCost().getValue());
    assertEquals(5, card.getValue());
    System.out.print(card.getInfluenceGrid().toString());
    assertEquals(this.influenceGrid, card.getInfluenceGrid());
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
    PawnCard sameCard = new PawnCard("TestCard", CostEnum.Two, 5, influenceGrid);
    assertTrue(sameCard.equals(card));
  }

  @Test
  public void testCardInequality() {
    InfluenceType[][] influenceGrid2 = {
            {InfluenceType.X, InfluenceType.X, InfluenceType.I, InfluenceType.X,InfluenceType.X},
            {InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.I, InfluenceType.I, InfluenceType.C, InfluenceType.I, InfluenceType.I},
            {InfluenceType.X, InfluenceType.I, InfluenceType.I, InfluenceType.I, InfluenceType.X},
            {InfluenceType.X, InfluenceType.X,  InfluenceType.I, InfluenceType.X, InfluenceType.X}
    };
    PawnCard card1 = new PawnCard("TestCard", CostEnum.Three, 5, influenceGrid);
    PawnCard card2 = new PawnCard("TestCard", CostEnum.Two, 10, influenceGrid);
    PawnCard card3 = new PawnCard("TestCard", CostEnum.Two, 5, influenceGrid2);
    PawnCard card4 = new PawnCard("DiffName", CostEnum.Two, 5, influenceGrid);

    assertFalse(card.equals(card1));
    assertFalse(card.equals(card2));
    assertFalse(card.equals(card3));
    assertFalse(card.equals(card4));
  }
}