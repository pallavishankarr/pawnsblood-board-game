package pawnboard.view;

import java.awt.*;
import java.awt.geom.AffineTransform;

import javax.swing.*;

import pawnboard.model.PawnCell;
import pawnboard.model.ReadonlyPawnsGame;

public class BoardPanel extends JPanel {
  private final int rows;
  private final int cols;
  private final ReadonlyPawnsGame<PawnCell> model;

  public BoardPanel(ReadonlyPawnsGame<PawnCell> model) {
    super();
    this.model = model;
    rows = model.getWidth();
    cols = model.getHeight();
  }
//
//  // logical to physical
//  private AffineTransform transformLogicalToPhysical() {
//    AffineTransform xform = new AffineTransform();
//    double scaleX = getWidth() / logicalSize
//    xform.scale(getWidth() / logicalSize.getWidth(), getHeight() / logicalSize.getHeight());
//    return xform;
//  }
//
//  // physical to logical
//  private AffineTransform transformPhysicalToLogical() {
//    AffineTransform xform = transformLogicalToPhysical();
//    try {
//      return xform.createInverse();
//    } catch (Exception e) {
//      throw new RuntimeException(e);
//    }
//  }

  // logical to model
  private AffineTransform transformLogicalToModel() {
    AffineTransform xform = new AffineTransform();
    Dimension logicalSize = new Dimension(30, 30);
    Dimension modelSize = new Dimension(model.getHeight(), model.getWidth());
    xform.scale(logicalSize.getWidth() / modelSize.getWidth(), logicalSize.getHeight() / modelSize.getHeight());
    return xform;
  }
}
