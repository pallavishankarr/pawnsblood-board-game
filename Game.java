interface GamePiece {
 
  
  
  public int getValue() ; 
  public GamePiece merge(GamePiece piece); 
  public boolean isValid(); 
  
  }


class baseTile implements GamePiece {
  int value; 
  
  //this is the constructor 
  baseTile(int value){
    this.value = value; 
  }
  
  public int getValue() {
    return this.value;
  }
  
  public boolean isValid() {
    return true; 
  }

  
  public GamePiece merge(GamePiece piece) {
    return new baseTile (this.getValue() + piece.getValue()); 
  
  }
  
}

class mergeTile implements GamePiece {
  GamePiece piece1; 
  GamePiece piece2; 
  
  //this is the constructor 
  mergeTile (GamePiece piece1, GamePiece piece2) {
    this.piece1 = piece1; 
    this.piece2 = piece2; 
  }
  
  public int getValue() {
    return (this.piece1.getValue() + this.piece2.getValue()); 
  }

 
  public GamePiece merge(GamePiece piece) {
   
    return new baseTile (this.getValue() + piece.getValue()); 
  }
  
  
  public boolean isValid() {
    if ((this.getValue()/ 2) == 0) {
      return true; 
    }
    else {
      return false; 
    }
  }
}

class ExamplesGamePieces {
  ExamplesGamePieces(){}
}