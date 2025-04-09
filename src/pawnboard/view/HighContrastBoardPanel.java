package pawnboard.view;

import pawnboard.model.PawnCard;
import pawnboard.model.PawnCell;
import pawnboard.model.Player;
import pawnboard.model.ReadonlyPawnsGame;

public class HighContrastBoardPanel extends BoardPanel {

  public HighContrastBoardPanel(ReadonlyPawnsGame<PawnCell, Player, PawnCard> model, HandPanel handPanel) {
    super(model, handPanel);
  }


}
