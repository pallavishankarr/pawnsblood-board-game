package pawnboard.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PawnCellTest {
  private Cell cell1;
  private Cell cell2;
  private Cell cell3;

  @Before
  public void setUp() {
    this.cell1 = new PawnCell();
    this.cell2 = new PawnCell(Player.RED, 2);
    this.cell3 = new PawnCell();
  }

  @Test
  public void testValidConstructorCell() {

    //checks the initialization of PawnCell with empty constructor
    assertEquals(0, this.cell1.getPawnCount());
    assertNull(this.cell1.getOwner());
    assertFalse(this.cell1.hasCard());

    //checks the initialization of PawnCell with a different constructor
    assertEquals(2, this.cell2.getPawnCount());
    assertEquals(Player.RED, this.cell2.getOwner());
    assertFalse(this.cell2.hasCard());

  }

  @Test
  public void testInvalidConstructorCell() {
    assertThrows(IllegalArgumentException.class, ()
            -> { new PawnCell(null, 5);});

  }

  @Test
  public void testCellAddPawn() {

    this.cell1.addPawn(Player.RED);
    assertEquals(1, this.cell1.getPawnCount());
    this.cell1.addPawn(Player.RED);
    assertEquals(2, this.cell1.getPawnCount());

    //adds a pawn for the Blue Player
    this.cell1.addPawn(Player.BLUE);
    assertEquals(2, this.cell1.getPawnCount());
  }

  //Test to get the owner of a Cel
  @Test
  public void testGetOwner() {

  }

  @Test
  public void testGetPawnCount() {
    assertEquals(0, this.cell1.getPawnCount());
    assertEquals(1, this.cell1.getPawnCount());

  }

  //Test for changing Ownership
  @Test
  public void testChangerOwnership(){
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
  public void testIsEmpty(){

  }





}