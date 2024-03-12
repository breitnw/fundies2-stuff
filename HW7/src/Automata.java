import tester.*;                // The tester library
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.impworld.*;      // the abstract World class and the big-bang library for imperative worlds
import java.awt.Color;          // general colors (as triples of red,green,blue values)
import java.util.ArrayList;
import java.util.List;
// and predefined colors (Red, Green, Yellow, Blue, Black, White)

// Represents a cell with an integer state that can be rendered and produce a child cell
// depending on its neighbors
interface ICell {
  // gets the state of this ICell
  int getState();
  
  // render this ICell as an image of a rectangle with this width and height
  WorldImage render(int width, int height);
  
  // produces the child cell of this ICell with the given left and right neighbors
  ICell childCell(ICell left, ICell right);
}

// Represents an abstract cell storing a state, capable of rendering itself based on that state.
abstract class ACell implements ICell {
  int state;
  
  ACell(int state) {
    if (state != 0 && state != 1) {
      throw new IllegalArgumentException("state must be 0 or 1, given " + state);
    }
    this.state = state;
  }
  
  // gets the state of this ICell
  @Override
  public int getState() {
    return this.state;
  }
  
  // render this ACell as an image of a rectangle with the provided width and height. Renders as
  // a black rectangle if the state is 1, or a white rectangle if the state is 0.
  @Override
  public WorldImage render(int width, int height) {
    if (this.getState() == 1) {
      return new RectangleImage(width, height, OutlineMode.SOLID, Color.BLACK);
    } else {
      return new RectangleImage(width, height, OutlineMode.SOLID, Color.WHITE);
    }
  }
}

// Represents a cell that is always off, and always produces an off cell as a child.
class InertCell extends ACell {
  InertCell() {
    super(0);
  }
  
  // produces the child cell of this ICell with the given left and right neighbors
  @Override
  public ICell childCell(ICell left, ICell right) {
    return new InertCell();
  }
}

// Represents a cell takes the states of its left and right neighbors, along with its own state,
// into account when producing a child cell. Depending on these states and the provided rule from
// 0 to 255, a specific child cell is produced.
abstract class ARuleCell extends ACell {
  int rule;
  
  ARuleCell(int state, int rule) {
    super(state);
    if (rule < 0 || rule > 255) {
      throw new IllegalArgumentException("rule must be between 0 and 255, given " + rule);
    }
    this.rule = rule;
  }
  
  // Determines the state of this cell's child depending on the states of this cell and its left
  // and right neighbors. The nth bit of the rule is selected for the child state, where n is
  // formed by combining the states of the left, middle, and right cells into a three-bit
  // integer.
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

// Represents a cell that produces its child according to rule 60
class Rule60 extends ARuleCell {
  Rule60(int state) {
    super(state, 60);
  }
  
  // Produces a cell with rule 60 and state determined by this cell's child state.
  public ICell childCell(ICell left, ICell right) {
    return new Rule60(this.childState(left, right));
  }
}

// Represents a cell that produces its child according to rule 30
class Rule30 extends ARuleCell {
  Rule30(int state) {
    super(state, 30);
  }
  
  // Produces a cell with rule 30 and state determined by this cell's child state.
  public ICell childCell(ICell left, ICell right) {
    return new Rule30(this.childState(left, right));
  }
}

// Represents a row of cells, bounded by `InertCell`s on the left and right.
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
  }
}

// Represents an imperative world with a cellular automata simulation
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
  
  // Creates a WorldScene representing this CAWorld
  public WorldScene makeScene() {
    WorldScene canvas = new WorldScene(TOTAL_WIDTH, TOTAL_HEIGHT);
    canvas.placeImageXY(this.makeImage(), TOTAL_WIDTH / 2, TOTAL_HEIGHT / 2);
    return canvas;
  }
}

class ExamplesAutomata {
  Rule60 r60Off = new Rule60(0);
  Rule60 r60On = new Rule60(1);
  Rule30 r30Off = new Rule30(0);
  Rule30 r30On = new Rule30(1);
  InertCell inert = new InertCell();
  
  CellArray ca1 = new CellArray(new ArrayList<>(
      List.of(r60Off, r60On, r30Off, r30On, inert)
  ));

  void testBigBang(Tester t) {
    CAWorld g = new CAWorld(new Rule60(0), new Rule60(1));
    g.bigBang(g.TOTAL_WIDTH, g.TOTAL_HEIGHT, 0.2);
  }
  
  // Methods on ICell -----------------------------------------------------------------------------
  
  void testICellGetState(Tester t) {
    t.checkExpect(r60Off.getState(), 0);
    t.checkExpect(r60On.getState(), 1);
    t.checkExpect(r30Off.getState(), 0);
    t.checkExpect(r30On.getState(), 1);
    t.checkExpect(inert.getState(), 0);
  }
  
  void testICellRender(Tester t) {
    WorldImage onImg = new RectangleImage(10, 12, OutlineMode.SOLID, Color.BLACK);
    WorldImage offImg = new RectangleImage(10, 12, OutlineMode.SOLID, Color.WHITE);
    t.checkExpect(r30On.render(10, 12), onImg);
    t.checkExpect(r60On.render(10, 12), onImg);
    t.checkExpect(r30Off.render(10, 12), offImg);
    t.checkExpect(r60Off.render(10, 12), offImg);
    t.checkExpect(inert.render(10, 12), offImg);
  }
  
  void testRule60ChildCell(Tester t) {
    t.checkExpect(r60Off.childCell(r60Off, r60Off), r60Off);
    t.checkExpect(r60Off.childCell(r60On, r60Off), r60On);
    t.checkExpect(r60On.childCell(r60Off, r60On), r60On);
    t.checkExpect(r60On.childCell(r60On, r60On), r60Off);
    t.checkExpect(r60Off.childCell(r60Off, r60On), r60Off);
  }
  
  void testRule30ChildCell(Tester t) {
    t.checkExpect(r30Off.childCell(r30Off, r30Off), r30Off);
    t.checkExpect(r30Off.childCell(r30On, r30Off), r30On);
    t.checkExpect(r30On.childCell(r30Off, r30On), r30On);
    t.checkExpect(r30On.childCell(r30On, r30On), r30Off);
    t.checkExpect(r30Off.childCell(r30Off, r30On), r30On);
  }
  
  void testInertCellChildCell(Tester t) {
    t.checkExpect(inert.childCell(inert, inert), inert);
    t.checkExpect(inert.childCell(r60On, inert), inert);
  }
  
  // Methods on ARuleCell -------------------------------------------------------------------------
  
  void testChildState(Tester t) {
    t.checkExpect(r60Off.childState(r60Off, r60Off), 0);
    t.checkExpect(r60Off.childState(r60Off, r60On), 0);
    t.checkExpect(r60On.childState(r60Off, r60Off), 1);
    t.checkExpect(r60On.childState(r60Off, r60On), 1);
    t.checkExpect(r60Off.childState(r60On, r60Off), 1);
    t.checkExpect(r60Off.childState(r60On, r60On), 1);
    t.checkExpect(r60On.childState(r60On, r60Off), 0);
    t.checkExpect(r60On.childState(r60On, r60On), 0);
  }
  
  // Methods on CellArray -------------------------------------------------------------------------

  void testCellArrayNextGen(Tester t) {
    ArrayList<ICell> initialCells = new ArrayList<>();
    initialCells.add(new Rule60(0));
    initialCells.add(new Rule60(1));
    initialCells.add(new Rule60(0));

    CellArray array = new CellArray(initialCells);
    CellArray nextGen = array.nextGen();

    ArrayList<ICell> expectedCellsNextGen = new ArrayList<>();
    expectedCellsNextGen.add(new Rule60(0));
    expectedCellsNextGen.add(new Rule60(1));
    expectedCellsNextGen.add(new Rule60(1));
    t.checkExpect(nextGen.cells, expectedCellsNextGen);
  }
  
  void testCellArrayGet(Tester t) {
    t.checkExpect(ca1.get(0), r60Off);
    t.checkExpect(ca1.get(1), r60On);
    t.checkExpect(ca1.get(2), r30Off);
    t.checkExpect(ca1.get(3), r30On);
    t.checkExpect(ca1.get(4), inert);
    
    // out of index get operations should return inert
    t.checkExpect(ca1.get(-1), inert);
    t.checkExpect(ca1.get(5), inert);
  }
  
  void testCellArrayDraw(Tester t) {
    t.checkExpect(
        ca1.draw(10, 12),
        new BesideImage(
            new BesideImage(
                new BesideImage(
                    new BesideImage(
                        new BesideImage(new EmptyImage(),
                            r60Off.render(10, 12)),
                        r60On.render(10, 12)),
                    r30Off.render(10, 12)),
                r30On.render(10, 12)),
            inert.render(10, 12)));
  }
  
  // Methods on CAWorld ---------------------------------------------------------------------------
  // Per Piazza, makeScene and makeImage are not tested here, as we assume them to work.
  
  void testCAWorldConstructor(Tester t) {
    // there's only one CAWorld constructor. I need field access to test it.
    // how should i test this one ok
    // You can do field access for tests
    CAWorld world = new CAWorld(inert, r30On);
    t.checkExpect(world.history, new ArrayList<>());
    t.checkExpect(world.curGen, new CellArray(new ArrayList<>(List.of(
        inert, inert, inert, inert, inert, inert, inert, inert, inert, inert,
        inert, inert, inert, inert, inert, inert, inert, inert, inert, inert,
        r30On,
        inert, inert, inert, inert, inert, inert, inert, inert, inert, inert,
        inert, inert, inert, inert, inert, inert, inert, inert, inert, inert
    ))));
  }
  
  void testCAWorldOnTick(Tester t) {
    CAWorld world = new CAWorld(r30Off, r30On);
    world.onTick();
    t.checkExpect(world.history.get(0), new CellArray(new ArrayList<>(List.of(
        r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off,
        r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off,
        r30On,
        r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off,
        r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off
    ))));

    t.checkExpect(world.curGen, new CellArray(new ArrayList<>(List.of(
        r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off,
        r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30On,
        r30On,
        r30On, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off,
        r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off, r30Off
    ))));
  }
}