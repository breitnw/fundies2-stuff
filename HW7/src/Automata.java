import tester.*; // The tester library

// general colors (as triples of red,green,blue values)
// and predefined colors (Red, Green, Yellow, Blue, Black, White)


interface ICell {
  // gets the state of this ICell
  int getState();
  
  // render this ICell as an image of a rectangle with this width and height
//    WorldImage render(int width, int height);
  
  // produces the child cell of this ICell with the given left and right neighbors
  ICell childCell(ICell left, ICell right);
}

class InertCell implements ICell {
  // gets the state of this ICell
  public int getState() {
    return 0;
  }
  
  // produces the child cell of this ICell with the given left and right neighbors
  public ICell childCell(ICell left, ICell right) {
    return new InertCell();
  }
}

abstract class ACell implements ICell {
  int rule;
  int state;
  
  ACell(int state, int rule) {
    this.state = state;
    this.rule = rule;
  }
  
  public int getState() {
    return this.state;
  }
  
  public int childState(ICell left, ICell right) {
    /*
    by masking ruleNum with 2 raised to the power of our state, we get a number that is only
    positive when the rule's result for our state is positive.
    
    For example, consider rule 114, with the left and right cells on and this cell off.
    - binaryState will be ((1*4)+(0*2)+(1*1)) = 5
    - binaryStateMask will be 1 << 5 = 00100000
    The binary representation of binaryStateMask will always have exactly one 1, lined up with
    the corresponding output in our rule's binary representation. Thus, we can simply take the
    bitwise AND:
    
      00100000
    & 01110010
      --------
      00100000 > 0
      
    If the value is greater than 0, that means that our desired rule has an output of 1. So, this
    cell should have a value of 1 for the next state.
     */
    int binaryState = left.getState() * 4 + this.getState() * 2 + right.getState();
    int binaryStateMask = 1 << binaryState;
    return Math.min(this.rule & binaryStateMask, 1);
  }
}

class Rule60 extends ACell {
  Rule60(int state) {
    super(state, 60);
  }
  
  public ICell childCell(ICell left, ICell right) {
    return new Rule60(this.childState(left, right));
  }
}

class ExamplesAutomata {
  Rule60 s0 = new Rule60(0);
  Rule60 s1 = new Rule60(1);
  
  void testNextState(Tester t) {
    t.checkExpect(s0.childState(s0, s0), 0);
    t.checkExpect(s0.childState(s0, s1), 0);
    t.checkExpect(s1.childState(s0, s0), 1);
    t.checkExpect(s1.childState(s0, s1), 1);
    t.checkExpect(s0.childState(s1, s0), 1);
    t.checkExpect(s0.childState(s1, s1), 1);
    t.checkExpect(s1.childState(s1, s0), 0);
    t.checkExpect(s1.childState(s1, s1), 0);
  }
}