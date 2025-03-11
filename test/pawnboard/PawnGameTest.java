package pawnboard;

import org.junit.Before;
import org.junit.Test;

import pawnboard.model.Cell;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.PawnsGame;
import pawnboard.model.PawnGameImpl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class PawnGameTest {

  private Player player;
  private PawnsGame game;


  //Test placing  a Card
  @Test
  public void testPlaceCard() {


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
    this.game = new PawnGameImpl(3, 5);
    assertEquals(3, this.game.getHeight());
    assertEquals(5, this.game.getWidth());
  }

  //test for Invalid Constructor of the PawnGame Class
  @Test
  public void testInvalidConstructor(){

    assertThrows(IllegalArgumentException.class,() -> new PawnGameImpl(3,6));
    assertThrows(IllegalArgumentException.class,() -> new PawnGameImpl(0,6));


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
