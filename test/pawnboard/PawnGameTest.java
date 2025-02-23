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
  public void testValidConstructorCell() {
    assertEquals(0, this.cell1.getPawnCount());
    assertNull(this.cell1.getOwner());
    assertFalse(this.cell1.hasCard());
  }

  @Test
  public void testInvalidConstructorCell() {

  }

  @Test
  public void testCellAddPawn() {

  }

  //Test placing  a Card
  @Test
  public void testPlaceCard() {

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

  //test for get Name of the card
  @Test
  public void testGetNameCard() {

  }

  //test to get the Pawn Cost of a card
  @Test
  public void testGetPawnCostCard(){

  }

  //test to get the Value of a card
  @Test
  public void testGetValueCard() {

  }

  //test to get Influence Grid of the Card
  @Test
  public void testGetInfluenceGridCard(){

  }

  // test for Affects Cell Card
  @Test
  public void testAffectsCellCard(){

  }

  //test to Apply Influence Card
  @Test
  public void testApplyInfluenceCard() {

  }

  //test toStringMethod for the Card
  @Test
  public void testToStringCard(){

  }

  //test to PlayGame from the Player
  @Test
  public void testPlayCardPlayer() {

  }

  //test for the Player to pass the Move
  @Test
  public void testPassMovePlayer() {

  }

  //test for the Player to draw the card
  @Test
  public void testDrawCardPlayer(){

  }

  //test for the Get Color Player
  @Test
  public void testGetColorPlayer(){

  }

  //test for Valid Constructor of the PawnsGame Class
  @Test
  public void testValidConstructor(){

  }

  //test for Invalid Constructor of the PawnGame Class
  @Test
  public void testInvalidConstructor(){

  }


  //test for starting the Game
  @Test
  public void testLegalStartGame(){

  }

  //test that the correct Illegal Argument Exceptions are thrown
  @Test
  public void testIllegalStartGame(){

  }

  //test for getting the width of the game
  @Test
  public void testGetWidthPawnGame(){

  }

  //test for getting the height of the game
  @Test
  public void testGetHeightPawnGame(){

  }

  //test for legally placing a Card At
  @Test
  public void testLegalPlaceCardAt(){

  }

  //test for Illegal Place Card AT
  @Test
  public void testIllegalPlaceCardAt(){

  }

  //test for Pass Turn through the game
  @Test
  public void testPassTurnGame(){

  }

  //test for legal draw Card
  @Test
  public void testLegalDrawCard(){

  }

  //test for illegal draw card
  @Test
  public void testIllegalDrawCard(){

  }

  //test creating new Deck
  @Test
  public void testGetNewDeck(){

  }

  //test legal get Cell At
  @Test
  public void testLegalGetCellAt(){

  }

  //test Illegal get Cell At
  @Test
  public void testIllegalGetCellAt(){

  }

  //test get legal current player
  @Test
  public void testLegalGetCurrentPlayer(){

  }

  //test get Illegal current player
  @Test
  public void testIllegalGetCurrentPlayer(){

  }











}
