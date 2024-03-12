interface IGamePiece {
  int getValue();
  MergeTile merge(IGamePiece other);
  
  boolean isValid();
}

class BaseTile implements IGamePiece {
  int val;
  
  BaseTile(int val) {
    // TODO: throw for anything NOT a power of two.
    this.val = val;
  }
  
  public int getValue() {
    return val;
  }
  
  public MergeTile merge(IGamePiece other) {
    return new MergeTile(this, other);
  }
  
  public boolean isValid() {
    return true;
  }
}

class MergeTile implements IGamePiece {
  IGamePiece first;
  IGamePiece second;
  
  MergeTile(IGamePiece first, IGamePiece second) {
    this.first = first;
    this.second = second;
  }
  
  public int getValue() {
    return this.first.getValue() + this.second.getValue();
  }
  
  public MergeTile merge(IGamePiece other) {
    return new MergeTile(this, other);
  }
  
  public boolean isValid() {
    return this.first.getValue() == this.second.getValue()
        && this.first.isValid()
        && this.second.isValid();
  }
}