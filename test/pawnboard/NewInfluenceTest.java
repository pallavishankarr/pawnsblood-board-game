package pawnboard;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pawnboard.controller.CardInitializer;
import pawnboard.model.Card;
import pawnboard.model.Cell;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnGameImpl;
import pawnboard.model.Player;

import static org.junit.Assert.assertEquals;

public class NewInfluenceTest {
  Map<String, List<PawnCard>> decks = new HashMap<>();
  CardInitializer initializer = new CardInitializer();
  PawnGameImpl game;
  List<PawnCard> deck1 = new ArrayList<>();
  List<PawnCard> deck2 = new ArrayList<>();
  String path = "docs" + File.separator + "newInfluenceDeck.config";


  @Before
  public void setUp() throws Exception {
    game = new PawnGameImpl(3, 5);


    decks = initializer.initializeDeck(path, path);
    deck1 = decks.get("deck1");
    deck2 = decks.get("deck2");

    game.startGame(deck1, deck2, 5);

  }


  @Test
  public void testNewInfluence() {
    game.placeCardAt(Player.RED, 0, 0, 0);
    Cell cell = game.getCellAt(0,0);
    Card card = cell.getCard();



  }


}