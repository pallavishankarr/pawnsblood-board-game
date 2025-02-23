package pawnboard;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class PawnGameTest {

  private Cell cell1;
  private Cell cell2;
  private Cell cell3;

  @Before
  public void setUp() {
    this.cell1 = new PawnCell();
    this.cell2 = new PawnCell();
    this.cell3 = new PawnCell();
  }


  @Test
  public void testValidConstructor() {
    assertEquals(0, this.cell1.getPawnCount());
    assertNull(this.cell1.getOwner());
    assertFalse(this.cell1.hasCard());
  }

  @Test
  public void testCellAddPawn() {
    this.cell1.addPawn();
    assertEquals(1, cell1.getPawnCount());
    this.cell1.addPawn();
    assertEquals(2, cell1.getPawnCount());

  }

  @Test
  public void testPlaceCard() {

  }

  @Test
  public void testGetOwner() {

  }

  @Test
  public void testGetPawnCount() {
    assertEquals(0, this.cell1.getPawnCount());
    this.cell1.addPawn();
    assertEquals(1, this.cell1.getPawnCount());

  }

  @Test
  public void testChangerOwnership(){

  }

  @Test
  public void testHasCard() {
    assertFalse(cell1.hasCard());
    assertFalse(cell2.hasCard());
    assertFalse(cell3.hasCard());

  }




}
