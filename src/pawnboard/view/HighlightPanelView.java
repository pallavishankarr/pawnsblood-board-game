package pawnboard.view;

public interface HighlightPanelView {
  void updateBoard();
  void highlightCell(int row, int col);
  void clearHighlight();
  boolean isHighlighted();
}
