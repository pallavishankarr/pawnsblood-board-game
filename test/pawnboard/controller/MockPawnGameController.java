package pawnboard.controller;

import pawnboard.Player.UserPlayer;
import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.PawnsGame;
import pawnboard.model.Player;
import pawnboard.view.GameWindow;

public class MockPawnGameController extends PawnsGameController{

  private int selectedCardIdx = -1;
  private int selectedRow = -1;
  private int selectedCol = -1;


  public MockPawnGameController(PawnsGame<PawnCell, Player, PawnCard> model, UserPlayer userPlayer, GameWindow playerView, Appendable output) {
    super(model, userPlayer, playerView, output);
  }

  @Override
  public void onCardClick(int cardIdx) {
    super.onCardClick(cardIdx);
    this.selectedCardIdx = cardIdx;
  }

  @Override
  public void onCellClick(int row, int col) {
    super.onCellClick(row, col);
    this.selectedRow = row;
    this.selectedCol = col;
  }

  // Getter methods for internal state
  public int getSelectedCardIdx() {
    return selectedCardIdx;
  }

  public int getSelectedRow() {
    return selectedRow;
  }

  public int getSelectedCol() {
    return selectedCol;
  }







}
