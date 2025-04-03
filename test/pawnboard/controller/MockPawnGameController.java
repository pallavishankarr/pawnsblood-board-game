package pawnboard.controller;

public interface MockPawnGameController extends GameController, PlayerActionListener, ModelStatusListener{
  /**
   *
   * @param redFilePath file path of the red player deck
   * @param blueFilePath file path of the blue player deck
   * @param handSize the starting handSize
   */
  void playGame(String redFilePath, String blueFilePath, int handSize);
  void onCardClick(int cardIdx);
  void onCellClick(int row, int col);
  void onConfirmTurn();
  void onPassTurn();
  void onTurnChanged();
  void onScoreUpdate();
  void onCardPlaced();
  void onGameOver();




}
