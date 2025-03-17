package pawnboard.view;

import javax.swing.*;

import pawnboard.model.PawnCell;
import pawnboard.model.ReadonlyPawnsGame;

public class BoardPanel extends JPanel {
  private final ReadonlyPawnsGame<PawnCell> model;

  public BoardPanel(ReadonlyPawnsGame<PawnCell> model) {
    super();
    this.model = model;
  }
}
