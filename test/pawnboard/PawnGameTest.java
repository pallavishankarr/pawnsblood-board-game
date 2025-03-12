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





import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;


public class PawnGameTest {

  private Player player;
  private PawnsGame game;


  //Test placing  a Card
  @Test
  public void testPlaceCard() {

    File file = new File("/Users/rachelelevathingal/Desktop/OOD/GitHub/CS3500Assignment5/src/pawnboard/model/CardList");
    CardInitializer initializer = new CardInitializer();
    List<PawnCard> deck = CardInitializer.initializeDeck(file.getAbsolutePath());
    List<Card> playerDeck = new ArrayList<>(deck);
    System.out.println("Deck size: " + deck.size());


    this.game = new PawnGameImpl(3, 5);
    game.startGame(playerDeck, playerDeck, 5);

    System.out.println(game.getHand(Player.RED).size());


    game.placeCardAt(Player.RED, 0, 1, 2);














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

    File file = new File("/Users/rachelelevathingal/Desktop/OOD/GitHub/CS3500Assignment5/src/pawnboard/model/CardList");
    CardInitializer initializer = new CardInitializer();
    List<PawnCard> deck = CardInitializer.initializeDeck(file.getAbsolutePath());
    List<Card> playerDeck = new ArrayList<>(deck);

    game = new PawnGameImpl(3, 5);
    game.startGame(playerDeck, playerDeck, 5);
    assertEquals(3, game.getHeight());
    assertEquals(5, game.getWidth());
    assertEquals(5, game.getHand(Player.RED).size());
    assertEquals(5, game.getHand(Player.BLUE).size());





  }

  //test that the correct Illegal Argument Exceptions are thrown
  @Test
  public void testIllegalStartGame(){

    char[][] influenceGrid = {
            {'X', 'X', 'I', 'X', 'X'},
            {'X', 'I', 'I', 'I', 'X'},
            {'I', 'I', 'C', 'I', 'I'},
            {'X', 'I', 'I', 'I', 'X'},
            {'X', 'X', 'I', 'X', 'X'}
    };

    List<InfluenceEffect> emptyEffects = new ArrayList<>();


    PawnCard card = new PawnCard("Example Pawn", 2, 5, influenceGrid,
            emptyEffects);

    List<Card> redPlayerDecks = new ArrayList<>();

    //error thrown when decks are null
    assertThrows(IllegalArgumentException.class,() -> new PawnGameImpl(0,0)
            .startGame(redPlayerDecks, redPlayerDecks, 5));
    //error thrown when the decks have cards less than what is needed
    redPlayerDecks.add(card);
    assertThrows(IllegalArgumentException.class,() -> new PawnGameImpl(0,0)
            .startGame(redPlayerDecks, redPlayerDecks, 5));


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
    this.game = new PawnGameImpl(3, 5);
    game.passTurn(Player.RED);
    assertEquals(Player.BLUE, game.getCurrentPlayer());
    game.passTurn(Player.BLUE);
    assertEquals(Player.RED, game.getCurrentPlayer());

  }

  //test for legal draw Card
  @Test
  public void testLegalDrawCard(){
    File file = new File("/Users/rachelelevathingal/Desktop/OOD/GitHub/CS3500Assignment5/src/pawnboard/model/CardList");
    CardInitializer initializer = new CardInitializer();
    List<PawnCard> deck = CardInitializer.initializeDeck(file.getAbsolutePath());
    List<Card> playerDeck = new ArrayList<>(deck);

    game = new PawnGameImpl(3, 5);
    game.startGame(playerDeck, playerDeck, 5);

    Card card = game.getHand(Player.RED).get(0);
    game.drawCard(Player.BLUE);
    Card card2 = game.getHand(Player.BLUE).get(0);
    assertNotEquals(card, card2);



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
    File file = new File("/Users/rachelelevathingal/Desktop/OOD/GitHub/CS3500Assignment5/src/pawnboard/model/CardList");
    CardInitializer initializer = new CardInitializer();
    List<PawnCard> deck = CardInitializer.initializeDeck(file.getAbsolutePath());
    List<Card> playerDeck = new ArrayList<>(deck);

    game = new PawnGameImpl(3, 5);
    game.startGame(playerDeck, playerDeck, 5);
    game.getCellAt(1, 1);

  }

  //test Illegal get Cell At
  @Test
  public void testIllegalGetCellAt(){

  }

  //test get legal current player
  @Test
  public void testLegalGetCurrentPlayer(){
    this.game = new PawnGameImpl(3, 5);
    assertEquals(Player.RED, this.game.getCurrentPlayer());
    this.game.passTurn(Player.RED);
    assertEquals(Player.BLUE, this.game.getCurrentPlayer());

  }

  //test get Illegal current player
  @Test
  public void testIllegalGetCurrentPlayer(){

  }











}
