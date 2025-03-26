package pawnboard.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Represents tests for a pawn cell.
 */
public class PawnCellTest {
  private Cell<Player, PawnCard> cell1;
  private Cell<Player, PawnCard>  cell2;
  private Cell<Player, PawnCard>  cell3;

  @Before
  public void setUp() {
    this.cell1 = new PawnCell();
    this.cell2 = new PawnCell(Player.RED, Count.Two);
    this.cell3 = new PawnCell();
  }

  @Test
  public void testValidConstructorCell() {

    //checks the initialization of PawnCell with empty constructor
    assertEquals(Count.Zero, this.cell1.getPawnCount());
    assertNull(this.cell1.getOwner());
    assertFalse(this.cell1.hasCard());

    //checks the initialization of PawnCell with a different constructor
    assertEquals(2, this.cell2.getPawnCount().getValue());
    assertEquals(Player.RED, this.cell2.getOwner());
    assertFalse(this.cell2.hasCard());

  }

  @Test
  public void testCellAddPawn() {

    this.cell1.addPawn(Player.RED);
    assertEquals(1, this.cell1.getPawnCount().getValue());
    this.cell1.addPawn(Player.RED);
    assertEquals(2, this.cell1.getPawnCount().getValue());

    //adds a pawn for the Blue Player
    this.cell1.addPawn(Player.BLUE);
    assertEquals(2, this.cell1.getPawnCount().getValue());
  }

  //Test to get the owner of a Cel
  @Test
  public void testGetOwner() {
    assertNull(this.cell1.getOwner());
    assertEquals(Player.RED, this.cell2.getOwner());

  }

  @Test
  public void testGetPawnCount() {
    assertEquals(0, this.cell1.getPawnCount().getValue());
    cell1.addPawn(Player.RED);
    assertEquals(1, this.cell1.getPawnCount().getValue());

  }

  //Test for changing Ownership
  @Test
  public void testChangerOwnership() {
    cell2.changeOwnership(Player.BLUE);
    assertEquals(Player.BLUE, cell2.getOwner());

  }

  //tests if the cell had a card
  @Test
  public void testHasCard() {
    assertFalse(cell1.hasCard());
    assertFalse(cell2.hasCard());
    assertFalse(cell3.hasCard());

  }

  //test finds if the cell is empty
  @Test
  public void testIsEmpty() {
    assertTrue(cell1.isCellEmpty());
    assertFalse(cell2.isCellEmpty());

  }


}