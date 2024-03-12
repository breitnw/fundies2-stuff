import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.impworld.*;      // the abstract World class and the big-bang library for imperative worlds
import java.awt.Color;          // general colors (as triples of red,green,blue values)
import java.util.ArrayList;
// and predefined colors (Red, Green, Yellow, Blue, Black, White)


interface ICell {
  // gets the state of this ICell
  int getState();
  
  // render this ICell as an image of a rectangle with this width and height
  WorldImage render(int width, int height);
  
  // produces the child cell of this ICell with the given left and right neighbors
  ICell childCell(ICell left, ICell right);
}

abstract class ACell implements ICell {
  int state;
  
  ACell(int state) {
    if (state != 0 && state != 1) {
      throw new IllegalArgumentException("state must be 0 or 1, given " + state);
    }
    this.state = state;
  }
  
  // gets the state of this ICell
  public int getState() {
    return this.state;
  }
  
  // render this ACell as an image of a rectangle with this width and height
  public WorldImage render(int width, int height) {
    if (this.getState() == 1) {
      return new RectangleImage(width, height, OutlineMode.SOLID, Color.BLACK);
    } else {
      return new RectangleImage(width, height, OutlineMode.SOLID, Color.WHITE);
    }
  }
}

class InertCell extends ACell {
  InertCell() {
    super(0);
  }
  
  // produces the child cell of this ICell with the given left and right neighbors
  public ICell childCell(ICell left, ICell right) {
    return new InertCell();
  }
}

abstract class ARuleCell extends ACell {
  int rule;
  
  ARuleCell(int state, int rule) {
    super(state);
    if (rule < 0 || rule > 255) {
      throw new IllegalArgumentException("rule must be between 0 and 255, given " + rule);
    }
    this.rule = rule;
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

class Rule60 extends ARuleCell {
  Rule60(int state) {
    super(state, 60);
  }
  
  public ICell childCell(ICell left, ICell right) {
    return new Rule60(this.childState(left, right));
  }
}

class Rule30 extends ARuleCell {
  Rule30(int state) {
    super(state, 30);
  }
  
  public ICell childCell(ICell left, ICell right) {
    return new Rule30(this.childState(left, right));
  }
}

class CellArray {
  ArrayList<ICell> cells;
  
  CellArray(ArrayList<ICell> cells) {
    this.cells = cells;
  }
  
  // Produces a new generation of cells from the current population. Cells at the start and end
  // of the list are treated as having InertCells as left and right neighbors, respectively.
  CellArray nextGen() {
    ArrayList<ICell> nextGen = new ArrayList<>();
    for (int cellIndex = 0; cellIndex < this.cells.size(); cellIndex += 1) {
      nextGen.add(this.get(cellIndex).childCell(this.get(cellIndex - 1), this.get(cellIndex + 1)));
    }
    return new CellArray(nextGen);
  }
  
  // Gets the ICell at the provided index of this CellArray, or a new InertCell if the provided
  // index is invalid.
  ICell get(int index) {
    if (index < 0 || index >= this.cells.size()) {
      return new InertCell();
    } else {
      return this.cells.get(index);
    }
  }
  
  // takes in two numbers, representing the width and the height of an individual cell, and
  // produces an image of all of this row's cells side-by-side.
  WorldImage draw(int cellWidth, int cellHeight) {
    WorldImage row = new EmptyImage();
    for (ICell c : this.cells) {
      row = new BesideImage(row, c.render(cellWidth, cellHeight));
    }
    return row;
  };
}

class CAWorld extends World {
  
  // constants
  static final int CELL_WIDTH = 10;
  static final int CELL_HEIGHT = 10;
  static final int INITIAL_OFF_CELLS = 20;
  static final int TOTAL_CELLS = INITIAL_OFF_CELLS * 2 + 1;
  static final int NUM_HISTORY = 41;
  static final int TOTAL_WIDTH = TOTAL_CELLS * CELL_WIDTH;
  static final int TOTAL_HEIGHT = NUM_HISTORY * CELL_HEIGHT;
  
  // the current generation of cells
  CellArray curGen;
  // the history of previous generations (earliest state at the start of the list)
  ArrayList<CellArray> history;
  
  // Constructs a CAWorld with INITIAL_OFF_CELLS of off cells on the left,
  // then one on cell, then INITIAL_OFF_CELLS of off cells on the right
  CAWorld(ICell off, ICell on) {
    this.history = new ArrayList<>();
    
    // Build a list of INITIAL_OFF_CELLS * 2 off cells
    ArrayList<ICell> cells = new ArrayList<>();
    for (int index = 0; index < INITIAL_OFF_CELLS * 2; index += 1) {
      cells.add(off);
    }
    // Insert an on cell at the middle of the list
    cells.add(INITIAL_OFF_CELLS, on);
    
    this.curGen = new CellArray(cells);
  }
  
  // Modifies this CAWorld by adding the current generation to the history
  // and setting the current generation to the next one
  public void onTick() {
    this.history.add(this.curGen);
    this.curGen = this.curGen.nextGen();
  }
  
  // Draws the current world, scrolling up from the bottom of the image
  public WorldImage makeImage() {
    // make a light-gray background image big enough to hold 41 generations of 41 cells each
    WorldImage bg = new RectangleImage(TOTAL_WIDTH, TOTAL_HEIGHT,
        OutlineMode.SOLID, new Color(240, 240, 240));
    
    // build up the image containing the past and current cells
    WorldImage cells = new EmptyImage();
    for (CellArray array : this.history) {
      cells = new AboveImage(cells, array.draw(CELL_WIDTH, CELL_HEIGHT));
    }
    cells = new AboveImage(cells, this.curGen.draw(CELL_WIDTH, CELL_HEIGHT));
    
    // draw all the cells onto the background
    return new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM,
        cells, 0, 0, bg);
  }
  
  public WorldScene makeScene() {
    WorldScene canvas = new WorldScene(TOTAL_WIDTH, TOTAL_HEIGHT);
    canvas.placeImageXY(this.makeImage(), TOTAL_WIDTH / 2, TOTAL_HEIGHT / 2);
    return canvas;
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

  void testBigBang(Tester t) {
    CAWorld g = new CAWorld(new Rule60(0), new Rule60(1));
    g.bigBang(g.TOTAL_WIDTH, g.TOTAL_HEIGHT, 0.2);
  }

  void testRule60ChildCell(Tester t) {
    ICell cellZero = new Rule60(0);
    ICell cellOne = new Rule60(1);

    // Test child cell creation
    t.checkExpect(cellZero.childCell(cellZero, cellZero), new Rule60(0));
    t.checkExpect(cellZero.childCell(cellOne, cellZero), new Rule60(1));
    t.checkExpect(cellOne.childCell(cellZero, cellOne), new Rule60(1));
    t.checkExpect(cellOne.childCell(cellOne, cellOne), new Rule60(0));
  }

  void testInertCellChildCell(Tester t) {
    ICell inert = new InertCell();
    ICell cellOne = new Rule60(1);

    // Regardless of neighbors, an inert cell should always produce an inert cell
    t.checkExpect(inert.childCell(inert, inert), new InertCell());
    t.checkExpect(inert.childCell(cellOne, inert), new InertCell());
  }

  void testCellArrayNextGen(Tester t) {
    ArrayList<ICell> initialCells = new ArrayList<>();
    initialCells.add(new Rule60(0));
    initialCells.add(new Rule60(1));
    initialCells.add(new Rule60(0));

    CellArray array = new CellArray(initialCells);
    CellArray nextGen = array.nextGen();

    ArrayList<ICell> expectedCellsNextGen = new ArrayList<>();
    expectedCellsNextGen.add(new Rule60(1)); // Assuming this is the expected state based on your rules
    expectedCellsNextGen.add(new Rule60(0)); // Adjust these based on the actual expected behavior
    expectedCellsNextGen.add(new Rule60(1));

    // Compare expected next generation with actual next generation
    // This assumes you implement a method in CellArray to get its cells for comparison
    t.checkExpect(nextGen.cells, expectedCellsNextGen);
  }


  
  void testSimulation(Tester t) {
    new CAWorld(new Rule30(0), new Rule30(1))
        .bigBang(CAWorld.TOTAL_WIDTH, CAWorld.TOTAL_HEIGHT, 0.5);
  }
}