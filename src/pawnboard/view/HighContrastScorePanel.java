package pawnboard.view;

import java.awt.*;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class HighContrastScorePanel extends ScorePanel{

  public HighContrastScorePanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model, Player player) {
    super(model, player);
  }

  @Override
  protected void paintComponent(Graphics g) {

  }



}
