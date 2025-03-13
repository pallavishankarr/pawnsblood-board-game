package pawnboard;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pawnboard.influences.InfluenceEffect;
import pawnboard.model.*;
import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.PawnsGame;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.CardInitializer;
import pawnboard.view.GameView;
import pawnboard.view.PawnGameTextualView;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;


public class PawnGameTest {

  private Player player;
  private PawnsGame<PawnCard> game;
  String path = "docs" + File.separator + "deck.config";


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
    game = new PawnGameImpl(3, 5);
    game.startGame(path,5);
    assertEquals(3, game.getHeight());
    assertEquals(5, game.getWidth());
    assertEquals(5, game.getHand(Player.RED).size());
    assertEquals(5, game.getHand(Player.BLUE).size());

    //test that the first column has one pawn count for Red Pawn
    Cell cell = game.getCellAt(0,0);
    Cell cell2 = game.getCellAt(1,0);
    Cell cell3 = game.getCellAt(2,0);


    assertEquals(1,cell.getPawnCount());
    assertEquals(Player.RED, cell.getOwner());
    assertEquals(1,cell2.getPawnCount());
    assertEquals(Player.RED, cell2.getOwner());
    assertEquals(1,cell3.getPawnCount());
    assertEquals(Player.RED, cell3.getOwner());


  }

  //test that the correct Illegal Argument Exceptions are thrown
  @Test
  public void testIllegalStartGame(){
    String path2 = "CardList";



   game = new PawnGameImpl(3, 5);

   //Throws because of path should create null decks 
   assertThrows(IllegalArgumentException.class, () -> game.startGame(path2,5));

   //Throws because hand Size is greater  than 1/3
   assertThrows(IllegalArgumentException.class, () -> game.startGame(path,7));



  }

  //test for getting the width of the game
  @Test
  public void testGetWidthPawnGame(){
    this.game = new PawnGameImpl(3, 5);
    assertEquals(5, this.game.getWidth());

  }

  //test for getting the height of the game
  @Test
  public void testGetHeightPawnGame(){
    this.game = new PawnGameImpl(3, 5);
    assertEquals(3, this.game.getHeight());

  }

  //test for legally placing a Red Card At
  @Test
  public void testRedCardPlaceCardAt(){
    this.game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);

    //adds pawn so that the card can be placed
    Cell cell = game.getCellAt(0,0);

    //card that will be placed
    PawnCard card = game.getHand(Player.RED).get(0);

    //places card
    game.placeCardAt(Player.RED, 0, 0, 0);

    //checks that the card has been placed and a new card has been picked up
    assertNotEquals(card, game.getHand(Player.RED).get(0));

    //Gets the card at the cell at row : 0 col : 0
    Cell cell2 = game.getCellAt(0,0);
    PawnCard card2 = (PawnCard) cell2.getCard();

    Cell cell3 = game.getCellAt(0, 1);
    Cell cell4 = game.getCellAt(1, 0);

    //checks that the card has been placed on the cell
    assertEquals(card, card2);
    //checks that ownership has been changed to Red
    assertEquals(Player.RED,cell.getOwner());

    //makes sure that the influence has changed for the specific cells
    assertEquals(1, cell3.getPawnCount());
    assertEquals(2, cell4.getPawnCount());


  }

  //test for legally placing a Blue Card At
  @Test
  public void testBlueCardPlaceCardAt(){
    this.game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);

    //adds pawn so that the card can be placed
    Cell cell = game.getCellAt(0,4);


    //card that will be placed
    PawnCard card = game.getHand(Player.BLUE).get(0);

    //places card
    game.placeCardAt(Player.BLUE, 0, 0, 4);

    //checks that the card has been placed and a new card has been picked up
    assertNotEquals(card, game.getHand(Player.BLUE).get(0));

    //Gets the card at the cell at row : 0 col : 0
    Cell cell2 = game.getCellAt(0,4);
    PawnCard card2 = (PawnCard) cell2.getCard();

    Cell cell3 = game.getCellAt(2, 4);
    Cell cell4 = game.getCellAt(1, 4);

    //checks that the card has been placed on the cell
    assertEquals(card, card2);
    //checks that ownership has been changed to Red
    assertEquals(Player.BLUE,cell.getOwner());

    //makes sure that the influence has changed for the specific cells
    assertEquals(1, cell3.getPawnCount());
    assertEquals(2, cell4.getPawnCount());

  }

  

  //test for Illegal Place Card at
  @Test
  public void testIllegalPlaceCardAt(){
    this.game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);

    //Pawn cost not met for placing the card
    assertThrows(IllegalArgumentException.class,() ->
            game.placeCardAt(Player.RED, 5, 1,2));

    //Adds pawn so cell cost can be met
    Cell cell = game.getCellAt(0,0);
    cell.addPawn(Player.RED);

    //Throws because the row index and col index is out of bounds
    assertThrows(IllegalArgumentException.class,
            () ->game.placeCardAt(Player.RED, 0, 3,5));

  }

  //test for Pass Turn through the game
  @Test
  public void testPassTurnGame(){
    this.game = new PawnGameImpl(3, 5);
    game.passTurn(Player.RED);
    assertEquals(Player.BLUE, game.getCurrentPlayer());
    game.passTurn(Player.BLUE);
    assertEquals(Player.RED, game.getCurrentPlayer());

  }

  //test for legal draw Card
  @Test
  public void testLegalDrawCard(){

    game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);

    Cell cell = game.getCellAt(0,0);
    cell.addPawn(Player.BLUE);

    game.placeCardAt(Player.BLUE, 0, 0, 0);

    //Makes sure that handSize is less than the hand size so a new card can be drawn for the deck
    assertEquals(4, game.getHand(Player.BLUE).size());
    //Checks the deck size
    assertEquals(15,game.getRemainingDeckSize(Player.BLUE));

    game.drawCard(Player.BLUE);

    //Makes sure that the card is added to hand
    assertEquals(5, game.getHand(Player.BLUE).size());

    //Makes sure that the deck size has been reduced by one card
    assertEquals(14,game.getRemainingDeckSize(Player.BLUE));

  }



  //test legal get Cell At
  @Test
  public void testLegalGetCellAt(){

    game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);

    // Test a valid cell retrieval
    Cell cell = game.getCellAt(1, 1);

    // Ensure the cell is not null
    assertNotNull(cell);

  }

  //test Illegal get Cell At
  @Test
  public void testIllegalGetCellAt(){

    game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);


    assertThrows(IllegalArgumentException.class, () -> game.getCellAt(-1, 0));
    assertThrows(IllegalArgumentException.class, () -> game.getCellAt(0, -1));
    assertThrows(IllegalArgumentException.class, () -> game.getCellAt(3, 0));
    assertThrows(IllegalArgumentException.class, () -> game.getCellAt(0, 5));
    assertThrows(IllegalArgumentException.class, () -> game.getCellAt(3, 5));

  }

  //test get legal current player
  @Test
  public void testLegalGetCurrentPlayer(){

    //test the current player which should be Red Player at start of game
    this.game = new PawnGameImpl(3, 5);
    assertEquals(Player.RED, this.game.getCurrentPlayer());

    //Passes the turn so that the player changes
    this.game.passTurn(Player.RED);

    //tests that the turn has been efficiently passed
    assertEquals(Player.BLUE, this.game.getCurrentPlayer());

  }




  //test get Winner
  @Test
  public void testGetWinner(){
    this.game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);
    game.placeCardAt(Player.RED, 0, 0, 0);

    System.out.println(game.rowWinner(0));
  }


  //test illegal if game is over
  @Test
  public void testIllegalGetWinner(){
    this.game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);
    game.placeCardAt(Player.RED, 0, 0, 0);
    game.placeCardAt(Player.RED, 0, 0, 1);
    game.placeCardAt(Player.RED, 0, 0, 2);
    game.placeCardAt(Player.RED, 0, 0, 3);
    game.placeCardAt(Player.RED, 0, 0, 4);
    assertThrows(IllegalArgumentException.class, () -> game.getWinner());


  }
  //test legal if game is over
  @Test
  public void testLegalGetWinner() {

  }

  //test checkScore
  @Test
  public void testCheckScore(){

  }

  //test check Score Row
  @Test
  public void testLegalCheckScoreRow(){

    game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);


    Cell cell = game.getCellAt(0,0);
    Cell cell2 = game.getCellAt(0,1);
    Cell cell3 = game.getCellAt(0,2);
    Cell cell4 = game.getCellAt(0,3);
    Cell cell5 = game.getCellAt(0,4);



    cell3.addPawn(Player.RED);
    cell4.addPawn(Player.RED);
    cell5.changeOwnership(Player.RED);






    game.placeCardAt(Player.RED, 0, 0, 0);
    game.placeCardAt(Player.RED, 0, 0, 1);
    game.placeCardAt(Player.RED, 0, 0, 2);
    game.placeCardAt(Player.RED, 0, 0, 3);
    game.placeCardAt(Player.RED, 0, 0, 4);

    assertEquals(9, game.checkScoreRow(Player.RED,0));
  }


  //test Row Winner
  @Test
  public void testRowWinner() {
    game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);

    Cell cell = game.getCellAt(0,0);
    Cell cell2 = game.getCellAt(0,1);
    Cell cell3 = game.getCellAt(0,2);
    Cell cell4 = game.getCellAt(0,3);
    Cell cell5 = game.getCellAt(0,4);


    cell3.addPawn(Player.RED);
    cell4.addPawn(Player.RED);
    cell5.changeOwnership(Player.RED);


    game.placeCardAt(Player.RED, 0, 0, 0);
    game.placeCardAt(Player.RED, 0, 0, 1);
    game.placeCardAt(Player.RED, 0, 0, 2);
    game.placeCardAt(Player.RED, 0, 0, 3);
    game.placeCardAt(Player.RED, 0, 0, 4);
    assertEquals(Player.RED, game.rowWinner(0));

  }

  //test isGameOver
  @Test
  public void testIsGameOver() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);

    Cell cell = game.getCellAt(0,0);
    Cell cell2 = game.getCellAt(0,1);
    Cell cell3 = game.getCellAt(0,2);
    Cell cell4 = game.getCellAt(0,3);
    Cell cell5 = game.getCellAt(0,4);



    cell3.addPawn(Player.RED);
    cell4.addPawn(Player.RED);
    cell5.changeOwnership(Player.RED);


    game.placeCardAt(Player.RED, 0, 0, 0);
    game.placeCardAt(Player.RED, 0, 0, 1);
    game.placeCardAt(Player.RED, 0, 0, 2);
    game.placeCardAt(Player.RED, 0, 0, 3);
    game.placeCardAt(Player.RED, 0, 0, 4);
    assertFalse(game.isGameOver());
  }

  //test Apply Influence in the game
  @Test
  public void testApplyInfluence() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(path, 5);

    Cell cell = game.getCellAt(0, 0);
    Cell cell2 = game.getCellAt(0, 1);
    Cell cell3 = game.getCellAt(1, 0);

    //checks that the pawn count of the cells before card influence is applied
    assertEquals(0,cell2.getPawnCount());
    assertEquals(1,cell3.getPawnCount());

    PawnCard card  = game.getHand(Player.RED).get(0);
    game.applyCardInfluence(0, 0, card, Player.RED);

    //checks that the pawn count of the cells after card influence is applied
    assertEquals(1, cell2.getPawnCount());
    assertEquals(2, cell3.getPawnCount());

  }

  @Test
  public void testMirroredDeck() {
    this.game = new PawnGameImpl(3, 5);
    game.startGame(path,5);

    PawnCard card  = game.getHand(Player.BLUE).get(2);
    char[][] mirroredGrid = {
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'I', 'X', 'X'},
            {'I', 'I', 'C', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'}
    };

    assertEquals(card.getInfluenceGrid(), mirroredGrid);
  }

  @Test
  public void testInitialEmptyBoard() {
    game = new PawnGameImpl(3, 5);
    GameView view = new PawnGameTextualView(game);
    game.startGame(path, 5);

    String expected =
            "0 1 _ _ _ 1 0\n" +
                    "0 1 _ _ _ 1 0\n" +
                    "0 1 _ _ _ 1 0\n";
    assertEquals(expected, view.toString());
  }

  @Test
  public void testPlaceRedCard() {
    game = new PawnGameImpl(3, 5);
    GameView view = new PawnGameTextualView(game);
    game.startGame(path, 5);

    // Place "Security" card (assume index 0 in hand) at row 1, col 2
    game.placeCardAt(Player.RED, 0, 0, 0);

    // Expected board after placement
    String expectedAfter =
            "2 R 1 _ _ 1 0\n" +
            "0 2 _ _ _ 1 0\n" +
            "0 1 _ _ _ 1 0\n";

    assertEquals(expectedAfter, view.toString());
  }










}
