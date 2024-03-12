import javalib.funworld.World;
import javalib.funworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;
import java.awt.*;
import java.util.function.*;

// represents a generic list
interface IList<T> {
  
  //returns the indexed element of this list
  T getIndex(int index);
  
  //returns the length of this list
  int length();
  
  //performs foldr
  <U> U foldr(BiFunction<T,U,U> func, U base);
  
  // maps the given function to this list
  <U> IList<U> map(Function<T, U> func);
  
  // returns whether any element in this list returns true when the given function is applied
  boolean orMap(Function<T, Boolean> func);
  
  //returns if this list contains a that element of the same type,
  // based on .equals equality
  boolean contains(T that);
  
  //returns if any element of the list returns true in a
  //comparison with the rest of the elements
  <U> boolean compareOrMap(BiFunction<T, U, Boolean> func, U that);
  
  //returns if any item in the list compares with an ormap to any other item in the list,
  //not including themselves
  boolean compareAllOrMap(BiFunction<T, T, Boolean> func);
}

/*
  TEMPLATE:
  FIELDS:
   this.first - T
   this.rest - IList<T>
  METHODS
    this.getIndex(int index) - T
    this.length() - int
    this.foldr(BiFunction<T,U,U> func, U base) - U
    this.map(Function<T, U> func) - IList<U>
    this.orMap(Function<T, Boolean> func) - boolean
    this.contains(T that) - boolean
  METHODS ON FIELD:
    this.rest.getIndex(int index) - T
    this.rest.length() - int
    this.rest.foldr(BiFunction<T,U,U> func, U base) - U
    this.rest.map(Function<T, U> func) - IList<U>
    this.rest.orMap(Function<T, Boolean> func) - boolean
    this.rest.contains(T that) - boolean
*/
// represents a ConsList with first element T cons onto rest of list of T
class ConsLo<T> implements IList<T> {
  
  T first;
  IList<T> rest;
  
  // constructor
  ConsLo(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }
  
  // returns the element at the given index in this list
  public T getIndex(int index) {
    
    //starts from 0
    if (index == 0) {
      return this.first;
    }
    return this.rest.getIndex(index - 1);
  }
  
  // returns the length of this list
  public int length() {
    return 1 + this.rest.length();
  }
  
  /* TEMPLATE
      PARAMETERS
        func - BiFunction<T, U, U>
      METHODS ON PARAMETERS
        func.apply(T, U) - U
  */
  //performs folder by applying the function on the rest of the list folded
  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    return func.apply(this.first,
        this.rest.foldr(func, base));
  }
  
  /* TEMPLATE
      PARAMETERS
        func - Function<T, U>
      METHODS ON PARAMETERS
        func.apply(T) - U
  */
  //performs the function on the first element, and maps the rest
  public <U> IList<U> map(Function<T, U> func) {
    return new ConsLo<U>(func.apply(this.first), this.rest.map(func));
  }
  
  
  /* TEMPLATE
      PARAMETERS
        func - Function<T, Boolean>
      METHODS ON PARAMETERS
        func.apply(T, Boolean) - Boolean
  */
  //performs the function on the first element to or with its return value and the rest of the list
  public boolean orMap(Function<T, Boolean> func) {
    return func.apply(this.first) || this.rest.orMap(func);
  }
  
  /*TEMPLATE
    PARAMETERS
    that - T
    */
  //checks the equality on the first element and that to or
  // with its return value and the rest of the list
  public boolean contains(T that) {
    return this.first.equals(that) || this.rest.contains(that);
  }
  
  
  /* TEMPLATE
  PARAMETERS
    func - BiFunction<T, T, Boolean>
    that - T
  METHODS ON PARAMETERS
    func.apply(T, T) - Boolean
   */
  //returns if the first elements comparison worked ored with the rest under
  //compareOrMap
  public <U> boolean compareOrMap(BiFunction<T, U, Boolean> func, U that) {
    return func.apply(this.first, that) || this.rest.compareOrMap(func, that);
  }
  
  
  /* TEMPLATE
  PARAMETERS
    func - BiFunction<T, T, Boolean>
    that -T
  METHODS ON PARAMETERS
    func.apply(T, T) - Boolean
   */
  //calls compareOrmap on the rest of the list against the first to check their comparisons,
  // and then recursively calls the rest of the list to check their comparisons
  public boolean compareAllOrMap(BiFunction<T, T, Boolean> func) {
    return this.rest.compareOrMap(func, this.first) || this.rest.compareAllOrMap(func);
  }
}

/*
  TEMPLATE:
  FIELDS: NONE
  METHODS
    this.getIndex(int index) - T
    this.length() - int
    this.foldr(BiFunction<T,U,U> func, U base) - U
    this.map(Function<T, U> func) - IList<U>
    this.orMap(Function<T, Boolean> func) - boolean
    this.contains(T that) - boolean
  METHODS ON FIELD: NONE
*/
class MtLo<T> implements IList<T> {
  
  //given no elements are in this list, the index is always invalid
  public T getIndex(int index) {
    throw new RuntimeException("Error, index out of bounds");
  }
  
  // returns 0 because empty list
  public int length() {
    return 0;
  }
  
  /* TEMPLATE
      PARAMETERS
        func - BiFunction<T, U, U>
      METHODS ON PARAMETERS
        func.apply(T, U) - U
  */
  // returns the base case
  public <U> U foldr(BiFunction<T, U, U> func, U base) {
    return base;
  }
  
  /* TEMPLATE
      PARAMETERS
        func - Function<T, U>
      METHODS ON PARAMETERS
        func.apply() - U
  */
  // returns empty list because there's no element to map
  public <U> IList<U> map(Function<T, U> func) {
    return new MtLo<U>();
  }
  
  /* TEMPLATE
      PARAMETERS
        func - Function<T, Boolean>
      METHODS ON PARAMETERS
        func.apply() - Boolean
  */
  // returns false because this is the end of list / empty list
  public boolean orMap(Function<T, Boolean> func) {
    return false;
  }
  
  /* TEMPLATE
  PARAMETERS
    that - T
   */
  // returns false because empty list doesn't contain anything
  public boolean contains(T that) {
    return false;
  }
  
  /* TEMPLATE
  PARAMETERS
    func - BiFunction<T, U, Boolean>
    that -T
  METHODS ON PARAMETERS
    func.apply(T, U) - Boolean
   */
  //given there are no more elements in the list, always returns false
  public <U> boolean compareOrMap(BiFunction<T, U, Boolean> func, U that) {
    return false;
  }
  
  /* TEMPLATE
  PARAMETERS
    func - BiFunction<T, T, Boolean>
    that -T
  METHODS ON PARAMETERS
    func.apply(T, T) - Boolean
   */
  //given there are no more elements in the list, always returns false
  public boolean compareAllOrMap(BiFunction<T, T, Boolean> func) {
    return false;
  }
}

/* TEMPLATE
    METHODS
      apply(IVehicle, WorldScene) - WorldScene
*/
// represents a function object that places vehicle image onto a world scene
class RenderVehicleToImage
    implements BiFunction<IVehicle, WorldScene, WorldScene> {
  
  /* TEMPLATE
      PARAMETERS
        vehicle - IVehicle
        base - WorldScene
      METHODS ON PARAMETERS
  */
  // places the given vehicle's image onto the given world scene
  public WorldScene apply(IVehicle vehicle, WorldScene base) {
    return vehicle.drawOnCanvas(base);
  }
}

/* TEMPLATE
  METHODS
    apply(IVehicle, IVehicle) - Boolean
*/
//returns if the vehicles overlap
class AreOverlapping
    implements BiFunction<IVehicle, IVehicle, Boolean> {
  
  /* TEMPLATE
    PARAMETERS
      vehicle - IVehicle
      base - WorldScene
    METHODS ON PARAMETERS
      vehicle
  */
  //returns if the vehicles overlap
  public Boolean apply(IVehicle one, IVehicle two) {
    return one.isOverlapping(two);
  }
}

/* TEMPLATE
METHODS
  apply(Boundary, WorldScene) - WorldScene
*/
//represents a function object that places boundary image onto a world scene
class RenderBoundaryToImage
    implements BiFunction<Boundary, WorldScene, WorldScene> {
  
  /* TEMPLATE
    PARAMETERS
      boundary - Boundary
      base - WorldScene
    METHODS ON PARAMETERS
      boundary.
  */
  // places the given boundary's image onto the given world scene
  public WorldScene apply(Boundary boundary, WorldScene base) {
    return boundary.drawOnCanvas(base);
  }
}


/* TEMPLATE
    PARAMETERS
      exit - Posn
    METHODS
      IVehicle apply(IVehicle) - IVehicle
*/
// represents function object that returns if IVehicle is the main car and on the exit
class MainVehicleOnExit implements Function<IVehicle, Boolean> {
  Posn exit;
  
  public MainVehicleOnExit(Posn exit) {
    this.exit = exit;
  }
  
  /* TEMPLATE
      PARAMETERS
        vehicle - IVehicle
      METHODS ON PARAMETERS
        vehicle.isSelected() - boolean
        vehicle.
  */
  // returns whether the given vehicle is the main car and is on the exit
  public Boolean apply(IVehicle vehicle) {
    return vehicle.mainCarOnExit(this.exit);
  }
  
}

/* TEMPLATE
    PARAMETERS
      mouseClickPos - Posn
    METHODS
      IVehicle apply(IVehicle) - IVehicle
*/
// represents function object that selects a IVehicle if it's on the mouse click position
class SelectVehicle implements Function<IVehicle, IVehicle> {
  Posn mouseClickPos;
  
  public SelectVehicle(Posn mouseClickPos) {
    this.mouseClickPos = mouseClickPos;
  }
  
  /* TEMPLATE
      PARAMETERS
        vehicle - IVehicle
      METHODS ON PARAMETERS
        vehicle.isSelected() - boolean
        vehicle.
  */
  // returns the vehicle given selected if on mouse click, else return it not changed
  public IVehicle apply(IVehicle vehicle) {
    if (vehicle.onMouseClick(mouseClickPos)) {
      return vehicle.select();
    }
    else {
      return vehicle;
    }
  }
  
}

/* TEMPLATE
    METHODS
      IVehicle apply(IVehicle) - IVehicle
*/
// represents function object that resets vehicle to not selected
class ResetSelection implements Function<IVehicle, IVehicle> {
  /* TEMPLATE
      PARAMETERS
        vehicle - IVehicle
      METHODS ON PARAMETERS
        vehicle.isSelected() - boolean
        vehicle.
  */
  // returns the given IVehicle with isSelected false
  public IVehicle apply(IVehicle vehicle) {
    return vehicle.resetSelection();
  }
}


/* TEMPLATE
    METHODS
      apply(IVehicle) - boolean
*/
// represents function object that returns if vehicle is selected
class AnySelection implements Function<IVehicle, Boolean> {
  
  /* TEMPLATE
      PARAMETERS
        vehicle - IVehicle
      METHODS ON PARAMETERS
        vehicle.isSelected() - boolean
        vehicle.
  */
  // returns if the given vehicle is selected
  public Boolean apply(IVehicle vehicle) {
    return vehicle.isSelected();
  }
}

/* TEMPLATE
    FIELDS
      xOrY - String
    METHODS
      apply(Boundary, Posn) - Posn
*/
// represents a function object that returns Posn
// with higher x or y value between the given boundary and posn
// depending on the string "x" or "y" passed in
class LargestXY implements BiFunction<Boundary, Posn, Posn> {
  String xOrY;
  
  public LargestXY(String xOrY) {
    this.xOrY = xOrY;
  }
  
  public Posn apply(Boundary boundary, Posn posn) {
    if (xOrY.equals("x")) {
      if (boundary.greaterX(posn.x)) {
        return boundary.position;
      }
      else {
        return posn;
      }
    }
    else {
      if (boundary.greaterY(posn.y)) {
        return boundary.position;
      }
      else {
        return posn;
      }
    }
  }
}

/* TEMPLATE
    METHODS
      apply(IVehicle, Boundary) - Boolean
*/
// function object that returns if a vehicle and a boundary are overlapping
class OverlapsBoundary
    implements BiFunction<IVehicle, Boundary, Boolean> {
  public Boolean apply(IVehicle vehicle, Boundary boundary) {
    return vehicle.isOverlappingPoints(new ConsLo<>( boundary.position,new MtLo<>()));
  }
}

/* TEMPLATE
      apply(Boundary, IList<IVehicle>) - Boolean
*/
// function object that returns if any vehicle in the given list overlaps a given boundary
class OverlapsAnyBoundaryVehicle
    implements BiFunction<Boundary, IList<IVehicle>, Boolean> {
  public Boolean apply(Boundary bound, IList<IVehicle> vehicles) {
    return  vehicles.compareOrMap(new OverlapsBoundary(), bound);
  }
}

/*
TEMPLATE:
FIELDS:
  this.colorList - IList<Color>
METHODS
  this.stringToVehicles(String desc) -IList<IVehicle>
  this.stringToBoundaries(String desc) -IList<Boundary>
  this.stringToBoundaryList(String, int, int, int, IList<Boundary>) - IList<Boundary>
  this.validCharacters(String desc, int index) - boolean
  this.isBoundaryValid(String description) - boolean
  this.boundaryRect(String, int, int, int) - boolean
  this.boundaryHeight(String, int, int) -int
  this.stringToVehicleList(String, int, int, int, int, IList<IVehicle>) - IList<IVehicle>
  this.findExitPosn(String) - Posn
  this.exitPosnAcc(String, int, int, int) - Posn
METHODS ON FIELD:
  colorList.getIndex(int) - Color
  colorList.length() - int
  colorList.foldr(BiFunction<T,U,U>, U) - U
  colorList.map(Function<T, U> - IList<U>
  colorList.orMap(Function<T, Boolean>) - boolean
  colorList.contains(T) - boolean
*/
// represents an utilities class
class Util {
  IList<Color> colorList;
  
  Util() {
    this.colorList = new ConsLo<>(Color.blue,
        new ConsLo<>(Color.black,
            new ConsLo<>(Color.cyan,
                new ConsLo<>(Color.green,
                    new ConsLo<>(Color.pink,
                        new ConsLo<>(Color.orange,
                            new ConsLo<>(Color.magenta, new MtLo<Color>())))))));
  }
  
  // parses the given String to a list of IVehicles
  IList<IVehicle> stringToVehicles(String desc) {
    return new Util().stringToVehicleList(desc, 0, 0, 0, 0, new MtLo<IVehicle>());
  }
  
  // parses the given String to a list of Boundary
  IList<Boundary> stringToBoundaries(String desc) {
    return new Util().stringToBoundaryList(desc, 0, 0,0, new MtLo<Boundary>());
  }
  
  /* TEMPLATE
      PARAMETERS
        list - IList<Boundary>
      METHODS ON PARAMETERS
        list.getIndex(int index) - Boundary
        list.length() - int
        list.foldr(BiFunction<T,U,U> func, U base) - U
        list.map(Function<T, U> func) - IList<U>
        list.orMap(Function<T, Boolean> func) - boolean
        list.contains(T that) - boolean
    */
  // accumulator that accumulates a list of Boundary blocks with their respective position
  IList<Boundary> stringToBoundaryList(String desc, int row,
                                       int column, int index, IList<Boundary> list) {
    if (index < desc.length()) {
      String character = desc.substring(index, index + 1);
      if (character.equals("+")
              || character.equals("-")
              || character.equals("|")) {
        return stringToBoundaryList(desc, row, column + 1, index + 1,
            new ConsLo<Boundary>(new Boundary(new Posn(column, row)), list));
      }
      else if (character.equals("\n")) {
        return stringToBoundaryList(desc, row + 1, 0, index + 1, list);
      }
      else {
        return stringToBoundaryList(desc, row, column + 1, index + 1, list);
      }
    }
    else {
      return list;
    }
  }
  
  //checks if the given string description contains only valid characters
  boolean validCharacters(String desc, int index) {
    String validCharacters = " TtCc+-|AaBaX\n";
    if (index < desc.length()) {
      String character = desc.substring(index, index + 1);
      if (validCharacters.contains(character)) {
        return validCharacters(desc, index + 1);
      }
      else {
        return false;
      }
    }
    else {
      return true;
    }
  }
  
  // returns whether the given string makes up a valid boundary for the game
  boolean isBoundaryValid(String description) {
    if (new Util().boundaryHeight(description, 0,0) < 4) {
      return false;
    }
    return boundaryRect(description, 0, 0, 0);
  }
  
  // returns whether the given string makes up a rectangular boundary
  boolean boundaryRect(String description, int row, int column, int index) {
    if (index < description.length()) {
      String boundary = "+-|X";
      String character = description.substring(index, index + 1);
      if ((column == 0 || column == description.indexOf("\n") - 1
               || (row == 0 && !character.equals("\n"))
               || row == new Util().boundaryHeight(description, 0, 0) - 1)) {
        if (!boundary.contains(character)) {
          return false;
        }
      }
      else {
        if (boundary.contains(character)) {
          return false;
        }
      }
      if (character.equals("\n")) {
        return boundaryRect(description, row + 1, 0, index + 1);
      }
      else {
        return boundaryRect(description, row, column + 1, index + 1);
      }
    }
    else {
      return true;
    }
  }
  
  // returns the height of boundary of given level description
  int boundaryHeight(String description, int index, int height) {
    if (index < description.length()) {
      String character = description.substring(index, index + 1);
      if (character.equals("\n")) {
        return boundaryHeight(description, index + 1, height + 1);
      }
      else {
        return boundaryHeight(description, index + 1, height);
      }
    }
    else {
      return height + 1;
    }
  }
  
  
  /*  TEMPLATE
      PARAMETERS
        list - IList<IVehicle>
      METHODS ON PARAMETERS
        IVehicle list.getIndex(int index) - IVehicle
        int list.length() - int
        list.foldr(BiFunction<T,U,U> func, U base) - U
        list.map(Function<T, U> func) - IList<U>
        list.orMap(Function<T, Boolean> func) - boolean
        list.contains(T that) - boolean
    */
  // accumulator that accumulates a list of Boundary blocks with their respective position
  // MODIFICATION TO STRING DESCRIPTION: "A and a, B and b"
  // represents target car and truck, capitalized = vertical
  IList<IVehicle> stringToVehicleList(String desc, int row, int column, int index,
                                      int colorIndex, IList<IVehicle> list) {
    if (index < desc.length()) {
      String character = desc.substring(index, index + 1);
      if (character.equals("C") || character.equals("c")) {
        boolean isVertical = character.equals("C");
        IVehicle car = new Car(new Posn(column, row), isVertical, false, false,
            this.colorList.getIndex(colorIndex % this.colorList.length()));
        return stringToVehicleList(desc, row, column + 1, index + 1, colorIndex + 1,
            new ConsLo<>(car, list));
      }
      else if (character.equals("T") || character.equals("t")) {
        boolean isVertical = character.equals("T");
        IVehicle truck = new Truck(new Posn(column, row), isVertical, false, false,
            this.colorList.getIndex(colorIndex % this.colorList.length()));
        return stringToVehicleList(desc, row, column + 1, index + 1, colorIndex + 1,
            new ConsLo<IVehicle>(truck, list));
      }
      else if (character.equals("A") || character.equals("a")) {
        boolean isVertical = character.equals("A");
        IVehicle car = new Car(new Posn(column, row), isVertical, true, false, Color.red);
        return stringToVehicleList(desc, row, column + 1, index + 1, colorIndex,
            new ConsLo<IVehicle>(car, list));
      }
      else if (character.equals("B") || character.equals("b")) {
        boolean isVertical = character.equals("B");
        IVehicle truck = new Truck(new Posn(column, row), isVertical, true, false, Color.red);
        return stringToVehicleList(desc, row, column + 1, index + 1, colorIndex,
            new ConsLo<IVehicle>(truck, list));
      }
      else if (character.equals("\n")) {
        return stringToVehicleList(desc, row + 1, 0, index + 1, colorIndex, list);
      }
      else {
        return stringToVehicleList(desc, row, column + 1, index + 1, colorIndex, list);
      }
    }
    else {
      return list;
    }
  }
  
  // returns the position of the exit given the string description
  public Posn findExitPosn(String desc) {
    return exitPosnAcc(desc, 0, 0, 0);
  }
  
  // accumulator that accumulates row and column and returns the position when the exit is found
  public Posn exitPosnAcc(String desc, int row, int column, int index) {
    if (index < desc.length()) {
      String character = desc.substring(index, index + 1);
      if (character.equals("X")) {
        return new Posn(column, row);
      }
      else if (character.equals("\n")) {
        return exitPosnAcc(desc, row + 1, 0, index + 1);
      }
      else {
        return exitPosnAcc(desc, row, column + 1, index + 1);
      }
    }
    else {
      throw new RuntimeException("Exit not found");
    }
  }
  
}

// represents a game board
/* TEMPLATE:
    Fields:
    this.vehicles - IList<Vehicle>
    this.boundaries - IList<Boundary>
    this.exit - Posn
   Methods:
    this.renderBoard() - WorldScene
    this.gameWon() - boolean
    this.makeSelection(Posn) - Board
    this.resetBoardSelection - Board
    this.boardVehicleSelected() - boolean
   Methods for fields:
    this.vehicles.getIndex(int index) - T
    this.vehicles.length() - int
    this.vehicles.foldr(BiFunction<T,U,U> func, U base) - U
    this.vehicles.map(Function<T, U> func) - IList<U>
    this.vehicles.orMap(Function<T, Boolean> func) - boolean
    this.vehicles.contains(T that) - boolean
    this.boundaries.getIndex(int index) - T
    this.boundaries.length() - int
    this.boundaries.foldr(BiFunction<T,U,U> func, U base) - U
    this.boundaries.map(Function<T, U> func) - IList<U>
    this.boundaries.orMap(Function<T, Boolean> func) - boolean
    this.boundaries.contains(T that) - boolean
*/
class Board {
  IList<IVehicle> vehicles;
  IList<Boundary> boundaries;
  Posn exit;
  
  Board(IList<IVehicle> vehicles, IList<Boundary> boundaries, Posn exit) {
    this.vehicles = vehicles;
    this.boundaries = boundaries;
    this.exit = exit;
  }
  
  // renders this board to an image
  WorldScene renderBoard() {
    return this.boundaries.foldr(new RenderBoundaryToImage(),
        this.vehicles.foldr(new RenderVehicleToImage(),
            new WorldScene(this.largestBoundaryX(), 240)));
  }
  
  // returns whether the player won the game (main vehicle on exit)
  boolean gameWon() {
    return this.vehicles.orMap(new MainVehicleOnExit(exit));
  }
  
  // returns a new board with the vehicle at the mouse click selected
  Board makeSelection(Posn mouseClickPos) {
    return new Board(this.vehicles.map(new SelectVehicle(mouseClickPos)),
        this.boundaries, this.exit);
  }
  
  // returns a new board with all the vehicles deselected
  public Board resetBoardSelection() {
    return new Board(this.vehicles.map(new ResetSelection()), this.boundaries, this.exit);
  }
  
  // returns whether any vehicles in this board is selected
  public boolean boardVehicleSelected() {
    return this.vehicles.orMap(new AnySelection());
  }
  
  // returns if there are any vehicles of this board that are overlapping with each other
  public boolean isBadOverlap() {
    return this.vehicles.compareAllOrMap(new AreOverlapping());
  }
  
  // returns the largest x value in this board's list of boundary
  public int largestBoundaryX() {
    return this.boundaries.foldr(new LargestXY("x"), new Posn(0, 0)).x;
  }
  
  // returns the largest y value in this board's list of boundary
  public int largestBoundaryY() {
    return this.boundaries.foldr(new LargestXY("y"), new Posn(0,0)).y;
  }
  
  //returns if this board has any vehicles that overlap with boundaries
  public boolean isBadVehicles() {
    return this.boundaries.compareOrMap(new OverlapsAnyBoundaryVehicle(), this.vehicles);
  }
  
}

// represents a game level
/* TEMPLATE:
 Fields:
  this.board - Board
  this.exit - Posn
 Methods:
  this.validLevel(String) - boolean
  this.levelWon() - boolean
  this.renderLevel() - WorldScene
  this.selectVehicle(Posn) - Level
  this.resetSelection() - Level
  this.vehicleSelected() - boolean
 Methods for fields:
  this.board.renderBoard() - WorldScene
  this.board.gameWon() - boolean
  this.board.makeSelection(Posn) - Board
  this.board.resetBoardSelection - Board
  this.board.boardVehicleSelected() - boolean
  this.board.isBadOverLap() - boolean
*/
class Level {
  Board board;
  Posn exit;
  
  // constructs a level with a string and throws exception
  // if string is not valid level description
  Level(String description) {
    if (!validLevel(description)) {
      throw new IllegalArgumentException("Invalid level description: missing exit / "
                                             + "invalid characters / boundary not valid");
    }
    this.board = new Board(
        new Util().stringToVehicles(description),
        new Util().stringToBoundaries(description),
        new Util().findExitPosn(description));
    this.exit = new Util().findExitPosn(description);
    if (this.board.isBadOverlap()) {
      throw new IllegalStateException("Bad state, cars are overlapping");
    }
    if (this.board.isBadVehicles()) {
      throw new IllegalStateException("Bad state, cars overlap boundaries");
    }
  }
  
  // constructs a level with a board and an exit
  Level(Board board, Posn exit) {
    this.board = board;
    this.exit = exit;
  }
  
  // returns whether the given string description makes up a valid level
  boolean validLevel(String desc) {
    return desc.contains("X")
               && new Util().validCharacters(desc, 0)
               && new Util().isBoundaryValid(desc);
  }
  
  // returns whether the game is won (vehicle on exit)
  boolean levelWon() {
    return this.board.gameWon();
  }
  
  // renders this level to an image
  WorldScene renderLevel() {
    return this.board.renderBoard();
  }
  
  // returns a new level with the vehicle selected at the given position
  Level selectVehicle(Posn posn) {
    return new Level(this.board.makeSelection(posn), this.exit);
  }
  
  // returns a new level with all the vehicles de-selected
  public Level resetSelection() {
    return new Level(this.board.resetBoardSelection(), this.exit);
  }
  
  // returns whether any vehicles are selected in this level
  public boolean vehicleSelected() {
    return this.board.boardVehicleSelected();
  }
  
  // returns the scene size of this level as a Posn
  public Posn sceneSize() {
    return new Posn(this.board.largestBoundaryX(), this.board.largestBoundaryY());
  }
}


// represents a Boundary block
/* TEMPLATE:
 Fields:
  this.exit - Posn
 Methods:
  this.renderBoundary() - WorldImage
  this.drawOnCanvas(WorldScene) - WorldScene
 Methods for fields: NONE
*/
class Boundary {
  // left top corner position
  Posn position;
  
  public Boundary(Posn position) {
    this.position = position;
  }
  
  // renders this Boundary to an image
  public WorldImage renderBoundary() {
    WorldImage img = new RectangleImage(30, 30, "solid", Color.gray);
    WorldImage imgMoved = img.movePinholeTo(
        new Posn((int) (0 - img.getWidth() / 2),
            (int)(0 - img.getHeight() / 2)));
    
    return imgMoved;
  }
  
  // places this Boundary on to the given scene based on this Boundary's position
  public WorldScene drawOnCanvas(WorldScene scene) {
    return scene.placeImageXY(this.renderBoundary(), this.position.x * 30, this.position.y * 30);
  }
  
  // returns if this Boundary's x position is greater than the given x value
  public boolean greaterX(int x) {
    return this.position.x > x;
  }
  
  // returns if this Boundary's y position is greater than the given y value
  public boolean greaterY(int y) {
    return this.position.y > y;
  }
  
}

// represents a Vehicle
interface IVehicle {
  // renders this vehicle to an image
  WorldImage renderVehicle();
  
  // draws this vehicle onto the given scene
  WorldScene drawOnCanvas(WorldScene base);
  
  //returns the end point of this vehicle (bottom left for vertical/ top right horizontal)
  Posn endPoint();
  
  //returns if the two vehicles are overlapping
  boolean isOverlapping(IVehicle that);
  
  // returns if the given vehicle overlaps any of the list of points
  boolean isOverlappingPoints(IList<Posn> pList);
  
  // returns true if this vehicle is the target vehicle and touching the exit
  boolean mainCarOnExit(Posn exit);
  
  // returns whether this Vehicle is touching the given position
  boolean touchingPos(Posn pos);
  
  // returns this IVehicle with isSelected to false
  IVehicle resetSelection();
  
  // returns this IVehicle with isSelected to true
  IVehicle select();
  
  // return whether this IVehicle is on the given position of the mouse click
  boolean onMouseClick(Posn mousePosn);
  
  // returns whether this IVehicle is selected
  Boolean isSelected();
}

/* TEMPLATE:
 Fields:
  this.position - Posn
  this.isVertical - boolean
  this.isMain - boolean
  this.isSelected - boolean
  this.color - Color
 Methods:
  this.renderVehicle() - WorldImage
  this.drawOnCanvas(WorldScene) - WorldScene
  this.isOverlapping() - boolean
  this.isOverlappingPoints(IList<Posn>) - boolean
  this.endPoint() - Posn
  this.mainCarOnExit() - boolean
  this.touchingPos(Posn) - boolean
  this.resetSelection() - IVehicle
  this.select() - IVehicle
  this.onMouseClick(Posn mousePosn) - boolean
  this.isSelected() - boolean
 Methods for fields: NONE
*/
// represents an abstract class that abstracts IVehicle methods and fields
abstract class AVehicle implements IVehicle {
  // represents top left corner position
  Posn position;
  boolean isVertical;
  boolean isMain;
  boolean isSelected;
  Color color;
  
  AVehicle(Posn position, boolean isVertical, boolean isMain, boolean isSelected, Color color) {
    this.position = position;
    this.isVertical = isVertical;
    this.isMain = isMain;
    this.isSelected = isSelected;
    this.color = color;
  }
  
  // renders this vehicle to an image
  public abstract WorldImage renderVehicle();
  
  // places this IVehicle's image onto the given canvas based on this Vehicle's position
  public WorldScene drawOnCanvas(WorldScene base) {
    return base.placeImageXY(this.renderVehicle(), this.position.x * 30, this.position.y * 30);
  }
  
  // returns whether this vehicle is overlapping with the given vehicle
  public abstract boolean isOverlapping(IVehicle that);
  
  // returns whether this vehicle is overlapping with any of the positions in the given list
  public abstract boolean isOverlappingPoints(IList<Posn> pList);
  
  // returns the end point of this IVehicle
  public abstract Posn endPoint();
  
  // returns true if this IVehicle is the main car and touching the exit
  public boolean mainCarOnExit(Posn exit) {
    return this.isMain && this.touchingPos(exit);
  }
  
  // returns whether this IVehicle is touching the given position
  public boolean touchingPos(Posn pos) {
    return this.position.x == pos.x && this.position.y == pos.y
               || this.endPoint().x == pos.x && this.endPoint().y == pos.y;
  }
  
  // de-selects this IVehicle by returning new IVehicle with isSelect being false
  public abstract IVehicle resetSelection();
  
  // selects a Vehicle by returning a new IVehicle with isSelect being true
  public abstract IVehicle select();
  
  // returns whether this IVehicle is on the given position of mouse click
  public abstract boolean onMouseClick(Posn mousePosn);
  
  // returns whether this vehicle is selected
  public Boolean isSelected() {
    return this.isSelected;
  }
  
}

/* TEMPLATE:
 Fields:
  this.position - Posn
  this.isVertical - boolean
  this.isMain - boolean
  this.isSelected - boolean
  this.color - Color
 Methods:
  this.renderVehicle() - WorldImage
  this.drawOnCanvas(WorldScene) - WorldScene
  this.isOverlapping() - boolean
  this.isOverlappingPoints(IList<Posn>) - boolean
  this.endPoint() - Posn
  this.mainCarOnExit() - boolean
  this.touchingPos(Posn) - boolean
  this.resetSelection() - IVehicle
  this.select() - IVehicle
  this.onMouseClick(Posn mousePosn) - boolean
  this.isSelected() - boolean
 Methods for fields: NONE
*/
// represents a car
class Car extends AVehicle {
  public Car(Posn position, boolean isVertical, boolean isMain, boolean isSelected, Color color) {
    super(position, isVertical, isMain, isSelected, color);
  }
  
  // renders this car to an image
  public WorldImage renderVehicle() {
    Color color = this.color;
    if (this.isSelected) {
      color = Color.yellow;
    }
    WorldImage rect = new RectangleImage(60, 30, "solid", color);
    if (this.isVertical) {
      WorldImage rotateRect = new RotateImage(rect, 90);
      WorldImage setPosnImage = rotateRect.movePinholeTo(
          new Posn(
              (int)(0 - rotateRect.getWidth() / 2),
              (int)(0 - rotateRect.getHeight() / 2)));
      
      return setPosnImage;
    }
    else {
      return rect.movePinholeTo(new Posn((int)(0 - rect.getWidth() / 2),
          (int)(0 - rect.getHeight() / 2)));
    }
  }
  
  // returns the end point of this car (left bottom vertical / right top horizontal)
  public Posn endPoint() {
    if (this.isVertical) {
      return new Posn(this.position.x, this.position.y + 1);
    }
    return new Posn(this.position.x + 1, this.position.y);
  }
  
  
  
  // returns whether this car is overlapping with the given IVehicle
  public boolean isOverlapping(IVehicle that) {
    return that.isOverlappingPoints(
        new ConsLo<>(this.position, new ConsLo<>(this.endPoint(), new MtLo<Posn>())));
  }
  
  // returns whether this car is overlapping with any of the positions in the given list
  public boolean isOverlappingPoints(IList<Posn> pList) {
    return pList.contains(this.position) || pList.contains(this.endPoint());
  }
  
  // returns a new Car with everything the same but isSelected to false
  public IVehicle resetSelection() {
    return new Car(this.position, this.isVertical, this.isMain, false, this.color);
  }
  
  // returns a new Car with everything the same but isSelected to true
  public IVehicle select() {
    return new Car(this.position, this.isVertical, this.isMain, true, this.color);
  }
  
  // returns whether this Car's position is on the position of the given mouse click
  public boolean onMouseClick(Posn mousePos) {
    int scaledX = position.x * 30;
    int scaledY = position.y * 30;
    if (this.isVertical) {
      return scaledX <= mousePos.x && mousePos.x <= scaledX + 30
                 && scaledY <=  mousePos.y && mousePos.y <= scaledY + 60;
    }
    else {
      return scaledX <= mousePos.x && mousePos.x <= scaledX + 60
                 && scaledY <=  mousePos.y && mousePos.y <= scaledY + 30;
    }
  }
}

/* TEMPLATE:
 Fields:
  this.position - Posn
  this.isVertical - boolean
  this.isMain - boolean
  this.isSelected - boolean
  this.color - Color
 Methods:
  this.renderVehicle() - WorldImage
  this.drawOnCanvas(WorldScene) - WorldScene
  this.isOverlapping() - boolean
  this.isOverlappingPoints(IList<Posn>) - boolean
  this.endPoint() - Posn
  this.mainCarOnExit() - boolean
  this.touchingPos(Posn) - boolean
  this.resetSelection() - IVehicle
  this.select() - IVehicle
  this.onMouseClick(Posn mousePosn) - boolean
  this.isSelected() - boolean
 Methods for fields: NONE
*/
// represents a Truck
class Truck extends AVehicle {
  // constructor
  public Truck(Posn position, boolean isVertical, boolean isMain, boolean isSelected, Color color) {
    super(position, isVertical, isMain, isSelected, color);
  }
  
  // renders this Truck to an image
  public WorldImage renderVehicle() {
    Color color = this.color;
    if (this.isSelected) {
      color = Color.yellow;
    }
    WorldImage rect = new RectangleImage(90, 30, "solid", color);
    if (this.isVertical) {
      WorldImage rotateRect = new RotateImage(rect, 90);
      WorldImage setPosnImage = rotateRect.movePinholeTo(
          new Posn(
              (int)(0 - rotateRect.getWidth() / 2),
              (int)(0 - rotateRect.getHeight() / 2)));
      
      return setPosnImage;
    }
    else {
      return rect.movePinholeTo(new Posn((int)(0 - rect.getWidth() / 2),
          (int)(0 - rect.getHeight() / 2)));
    }
  }
  
  // returns the end point of this Truck (left bottom for vertical / right top for horizontal)
  public Posn endPoint() {
    if (this.isVertical) {
      return new Posn(this.position.x, this.position.y + 2);
    }
    return new Posn(this.position.x + 2, this.position.y);
  }
  
  /* TEMPLATE:
      Parameters:
        that - IVehicle
      Methods on Parameters:
          that.renderVehicle() - WorldImage
          that.drawOnCanvas(WorldScene) - WorldScene
          that.isOverlapping() - boolean
          that.isOverlappingPoints(IList<Posn>) - boolean
          that.endPoint() - Posn
          that.mainCarOnExit() - boolean
          that.touchingPos(Posn) - boolean
          that.resetSelection() - IVehicle
          that.select() - IVehicle
          that.onMouseClick(Posn mousePosn) - boolean
          that.isSelected() - boolean
  */
  // returns whether this truck is overlapping with the given IVehicle
  public boolean isOverlapping(IVehicle that) {
    return that.isOverlappingPoints(
        new ConsLo<>(this.position,
            new ConsLo<>(this.midPoint(),
                new ConsLo<>(this.endPoint(), new MtLo<Posn>()))));
  }
  
  
  //calculates the midpoint of this truck
  Posn midPoint() {
    return new Posn((this.position.x + this.endPoint().x) / 2,
        (this.position.y + this.endPoint().y) / 2);
  }
  
  /* TEMPLATE:
    Parameters:
      pList - IList<Posn>
    Methods on Parameters:

*/
  // returns whether this truck is overlapping with any of the positions in the given list
  public boolean isOverlappingPoints(IList<Posn> pList) {
    return pList.contains(this.position) || pList.contains(this.endPoint())
               || pList.contains(this.midPoint());
  }
  
  // de-selects this truck by returning a new truck with everything the same but isSelected to false
  public IVehicle resetSelection() {
    return new Truck(this.position,
        this.isVertical, this.isMain, false, this.color);
  }
  
  // selects this truck by returning a new truck with everything the same but isSelected to true
  public IVehicle select() {
    return new Truck(this.position, this.isVertical,
        this.isMain, true, this.color);
  }
  
  // returns whether this truck is on given position of mouse click
  public boolean onMouseClick(Posn mousePos) {
    int scaledX = position.x * 30;
    int scaledY = position.y * 30;
    if (this.isVertical) {
      return scaledX <= mousePos.x && mousePos.x <= scaledX + 30
                 && scaledY <=  mousePos.y && mousePos.y <= scaledY + 90;
    }
    else {
      return scaledX <= mousePos.x && mousePos.x <= scaledX + 90
                 && scaledY <=  mousePos.y && mousePos.y <= scaledY + 30;
    }
  }
  
}

// represents a Rush Hour World
class RushHourWorld extends World {
  Level level;
  
  public RushHourWorld(Level level) {
    this.level = level;
  }
  
  public WorldScene makeScene() {
    return level.renderLevel();
  }
  
  public World onMouseClicked(Posn posn, String buttonName) {
    if (buttonName.equals("LeftButton")) {
      if (this.level.vehicleSelected()) {
        return new RushHourWorld(this.level.resetSelection().selectVehicle(posn));
      }
      return new RushHourWorld(this.level.selectVehicle(posn));
    }
    else {
      return this;
    }
  }
  
  // ends the world when the level is won
  public boolean shouldWorldEnd() {
    return this.level.levelWon();
  }
}

class ExamplesGame {
  IList<Integer> testListInt = new ConsLo<>(1,
      new ConsLo<>(2,
          new ConsLo<>(3,
              new ConsLo<>(4,
                  new MtLo<>()))));
  IList<String> testListStr = new ConsLo<>("1",
      new ConsLo<>("2",
          new ConsLo<>("3",
              new ConsLo<>("4",
                  new MtLo<>()))));
  
  boolean testFoldr(Tester t) {
    // function object that adds all integers in a list
    class SumFunc implements BiFunction<Integer, Integer, Integer> {
      public Integer apply(Integer num1, Integer num2) {
        return num1 + num2;
      }
      
    }
    
    // function object that concat all string in a list
    class StringComb implements BiFunction<String, String, String> {
      public String apply(String str1, String str2) {
        return str1 + str2;
      }
    }
    
    return t.checkExpect(new SumFunc().apply(1,2), 3)
               && t.checkExpect(new SumFunc().apply(0,1), 1)
               && t.checkExpect(new StringComb().apply("1", "2"), "12")
               && t.checkExpect(new StringComb().apply("3", "0"), "30")
               
               && t.checkExpect(testListInt.foldr(new SumFunc(), 0), 10)
               && t.checkExpect(testListInt.foldr(new SumFunc(), 10), 20)
               && t.checkExpect(testListStr.foldr(new StringComb(), ""), "1234")
               && t.checkExpect(testListStr.foldr(new StringComb(), "base"), "1234base");
  }
  
  boolean testIListFunctions(Tester t) {
    IList<String> list = new ConsLo<>("bob",
        new ConsLo<>("bobby",
            new ConsLo<>("rob",
                new MtLo<>())));
    
    IList<String> list2 = new ConsLo<>("bobery",
        new ConsLo<>("bobby",
            new ConsLo<>("robert",
                new MtLo<>())));
    
    IList<Posn> list3 = new ConsLo<>(new Posn(1,2),
        new ConsLo<>(new Posn(3,4),
            new MtLo<>()));
    
    // function object that converts integer to string
    class IntToString implements Function<Integer, String> {
      public String apply(Integer integer) {
        return Integer.toString(integer);
      }
    }
    
    // function object that returns if string length < 4
    class StringLessThan4 implements Function<String, Boolean> {
      
      public Boolean apply(String s) {
        return s.length() < 4;
      }
    }
    
    return
        //test index
        t.checkExpect(list.getIndex(1), "bobby")
            && t.checkExpect(list.getIndex(0), "bob")
            && t.checkExpect(list.getIndex(2), "rob")
            
            // test length
            && t.checkExpect(list.length(), 3)
            && t.checkExpect(list3.length(), 2)
            && t.checkExpect(new MtLo<>().length(), 0)
            
            // test function object apply
            && t.checkExpect(new IntToString().apply(1), "1")
            && t.checkExpect(new IntToString().apply(20), "20")
            && t.checkExpect(new StringLessThan4().apply("1234"), false)
            && t.checkExpect(new StringLessThan4().apply("123"), true)
            
            // test map
            && t.checkExpect(testListInt.map(new IntToString()), testListStr)
            && t.checkExpect(list.map(new StringLessThan4()),
            new ConsLo<>(true,
                new ConsLo<>(false,
                    new ConsLo<>(true,
                        new MtLo<>()))))
            
            // test orMap
            && t.checkExpect(list.orMap(new StringLessThan4()), true)
            && t.checkExpect(testListStr.orMap(new StringLessThan4()), true)
            && t.checkExpect(list2.orMap(new StringLessThan4()), false)
            
            // test contains
            && t.checkExpect(list2.contains("bob"), false)
            && t.checkExpect(list.contains("bob"), true)
            && t.checkExpect(list3.contains(new Posn(1,2)), true)
            && t.checkExpect(list3.contains(new Posn(5,6)), false);
  }
  
  IVehicle testTruck = new Truck(new Posn(1,4),false, true, false, Color.black);
  IVehicle testTruckNotMain = new Truck(new Posn(1,4),false, false, false, Color.black);
  IVehicle testTruckVert = new Truck(new Posn(1,4),true, true, false, Color.black);
  IVehicle testCar = new Car(new Posn(1,4),false, true, false, Color.black);
  IVehicle testCarNotMain = new Car(new Posn(1,4),false, false, false, Color.black);
  IVehicle testCarVert = new Car(new Posn(1,4),true, true, false, Color.black);
  IList<IVehicle> list = new ConsLo<>(testCarNotMain,
      new ConsLo<>(testTruckNotMain,
          new ConsLo<>(testCarVert, new MtLo<>())));
  
  IList<IVehicle> list2 = new ConsLo<>(testCarNotMain,
      new ConsLo<>(testTruckNotMain,
          new ConsLo<>(testTruck, new MtLo<>())));
  
  boolean testLevelChecks(Tester t) {
    return t.checkConstructorException(
        new IllegalArgumentException("Invalid level description: missing exit / "
                                         + "invalid characters / boundary not valid"),
        "Level", "");
  }
  
  boolean testLevelChecks2(Tester t) {
    return t.checkConstructorException(
        new IllegalArgumentException("Invalid level description: missing exit / "
                                         + "invalid characters / boundary not valid"),
        "Level", "X");
  }
  
  boolean testLevelChecks3(Tester t) {
    return t.checkConstructorException(
        new IllegalArgumentException("Invalid level description: missing exit / "
                                         + "invalid characters / boundary not valid"),
        "Level", "+-----+\n"
                     + "| gggg|\n"
                     + "|     X\n"
                     + "|     |\n"
                     + "+-----+\n");
  }
  
  boolean testLevelChecks4(Tester t) {
    return t.checkConstructorException(
        new IllegalStateException("Bad state, cars overlap boundaries"),
        "Level", "+-----+\n"
                     + "|     |\n"
                     + "|     X\n"
                     + "|CCC  |\n"
                     + "+-----+");
  }
  
  boolean testGameWonMethods(Tester t) {
    Board wonBoard = new Board(list, new MtLo<>(), new Posn(1,5));
    Board wonBoard2 = new Board(list2, new MtLo<>(), new Posn(3,4));
    Board board = new Board(list, new MtLo<>(), new Posn(3,7));
    
    return t.checkExpect(testTruck.mainCarOnExit(new Posn(3,4)), true)
               && t.checkExpect(testTruck.mainCarOnExit(new Posn(6,4)), false)
               && t.checkExpect(testTruckNotMain.mainCarOnExit(new Posn(3,4)), false)
               && t.checkExpect(testTruckVert.mainCarOnExit(new Posn(3,4)), false)
               && t.checkExpect(testCar.mainCarOnExit(new Posn(2,4)), true)
               && t.checkExpect(testCarNotMain.mainCarOnExit(new Posn(2,4)), false)
               && t.checkExpect(testCarVert.mainCarOnExit(new Posn(2,4)), false)
               
               && t.checkExpect(new MainVehicleOnExit(new Posn(3,4)).apply(testTruck), true)
               && t.checkExpect(new MainVehicleOnExit(new Posn(3, 4)).apply(testTruckNotMain), false)
               && t.checkExpect(new MainVehicleOnExit(new Posn(1,6)).apply(testTruckVert), true)
               && t.checkExpect(new MainVehicleOnExit(new Posn(2,4)).apply(testCar), true)
               && t.checkExpect(new MainVehicleOnExit(new Posn(2, 4)).apply(testCarNotMain), false)
               && t.checkExpect(new MainVehicleOnExit(new Posn(3,4)).apply(testCarVert), false)
               && t.checkExpect(new MainVehicleOnExit(new Posn(1,5)).apply(testCarVert), true)
               
               && t.checkExpect(wonBoard.gameWon(), true)
               && t.checkExpect(board.gameWon(), false)
               && t.checkExpect(wonBoard2.gameWon(), true);
    
  }
  
  IVehicle selectTruck = new Truck(new Posn(1,4),false, true, true, Color.black);
  IVehicle selectCar =
      new Truck(new Posn(1,4),false, false, true, Color.black);
  IVehicle notSelectTruck =
      new Truck(new Posn(1,4),false, true, false, Color.black);
  IVehicle notSelectCar =
      new Truck(new Posn(1,4),false, false, false, Color.black);
  
  boolean testSelectVehicleMethods(Tester t) {
    
    return
        // test isSelected()
        t.checkExpect(selectCar.isSelected(), true)
            && t.checkExpect(selectTruck.isSelected(), true)
            && t.checkExpect(notSelectCar.isSelected(), false)
            && t.checkExpect(notSelectTruck.isSelected(), false)
            
            // test select()
            && t.checkExpect(notSelectCar.select().isSelected(), true)
            && t.checkExpect(notSelectTruck.select().isSelected(), true)
            && t.checkExpect(selectCar.select().isSelected(), true)
            
            // test resetSelection()
            && t.checkExpect(selectCar.resetSelection().isSelected(), false)
            && t.checkExpect(selectCar.resetSelection().isSelected(), false)
            && t.checkExpect(notSelectCar.resetSelection().isSelected(), false)
            
            // test ResetSelection function object
            && t.checkExpect(new ResetSelection().apply(selectCar).isSelected(), false)
            && t.checkExpect(new ResetSelection().apply(selectTruck).isSelected(), false)
            && t.checkExpect(new ResetSelection().apply(notSelectCar).isSelected(), false)
            
            // test onMouseClick()
            && t.checkExpect(testTruck.onMouseClick(new Posn(120, 150)), true)
            && t.checkExpect(testTruck.onMouseClick(new Posn(121, 150)), false)
            && t.checkExpect(testTruckVert.onMouseClick(new Posn(120, 150)), false)
            && t.checkExpect(testTruckVert.onMouseClick(new Posn(60, 210)), true)
            && t.checkExpect(testCar.onMouseClick(new Posn(90, 150)), true)
            && t.checkExpect(testCar.onMouseClick(new Posn(30, 151)), false)
            && t.checkExpect(testCar.onMouseClick(new Posn(91, 150)), false)
            && t.checkExpect(testCarVert.onMouseClick(new Posn(60,180)), true)
            && t.checkExpect(testCarVert.onMouseClick(new Posn(61, 180)), false)
            && t.checkExpect(testCarVert.onMouseClick(new Posn(60, 181)), false)
            
            // test SelectVehicle function object
            && t.checkExpect(new SelectVehicle(new Posn(120, 150))
                                 .apply(testTruck).isSelected(), true)
            && t.checkExpect(new SelectVehicle(new Posn(121, 150))
                                 .apply(testTruck).isSelected(), false)
            && t.checkExpect(new SelectVehicle(new Posn(90, 150))
                                 .apply(testCar).isSelected(), true)
            && t.checkExpect(new SelectVehicle(new Posn(90, 150))
                                 .apply(testCarVert).isSelected(), false)
            
            // test AnySelection function object
            && t.checkExpect(new AnySelection().apply(selectCar), true)
            && t.checkExpect(new AnySelection().apply(notSelectCar), false)
            && t.checkExpect(new AnySelection().apply(selectTruck), true)
            && t.checkExpect(new AnySelection().apply(notSelectTruck), false);
  }
  
  IVehicle car1 = new Car(new Posn(5,6), false,false, false,Color.black);
  IVehicle car2 = new Car(new Posn(3,5), true,false, false,Color.magenta);
  
  IVehicle car = new Car(new Posn(5, 5), false, false,false, Color.blue);
  IVehicle car3 = new Car(new Posn(2,5), true,false, false,Color.orange);
  IVehicle car4 = new Car(new Posn(1,5), true,false, false,Color.pink);
  IVehicle car5 = new Truck(new Posn(1,4), false,false, false,Color.green);
  IVehicle car6 = new Car(new Posn(6,3), true,false, false,Color.cyan);
  IVehicle car7 = new Car(new Posn(1,3), false,true, false,Color.red);
  IVehicle car8 = new Truck(new Posn(5,2), true,false, false,Color.black);
  IVehicle car9 = new Car(new Posn(3,2), true,false, false,Color.blue);
  
  IList<IVehicle> vehicleList = new ConsLo<>(car1,
      new ConsLo<>(car,
          new ConsLo<>(car2,new ConsLo<>(car3,
              new ConsLo<>(car4,
                  new ConsLo<>(car5,
                      new ConsLo<>(car6,
                          new ConsLo<>(car7,
                              new ConsLo<>(car8,
                                  new ConsLo<>(car9,
                                      new MtLo<>()))))))))));
  
  
  IList<IVehicle> vehicleList2 = new ConsLo<>(car1,
      new ConsLo<>(car,
          new ConsLo<>( new Car(new Posn(3,5), true,false, true, Color.magenta),
              new ConsLo<>(car3,
                  new ConsLo<>(car4,
                      new ConsLo<>(car5,
                          new ConsLo<>(car6,
                              new ConsLo<>(car7,
                                  new ConsLo<>(car8,
                                      new ConsLo<>(car9,
                                          new MtLo<>()))))))))));
  
  IList<IVehicle> vehicleList3 = new ConsLo<>(car1,
      new ConsLo<>(car,
          new ConsLo<>(car2,
              new ConsLo<>(car3,
                  new ConsLo<>(car4,
                      new ConsLo<>(new Truck(new Posn(1,4),
                          false,false, true, Color.green),
                          new ConsLo<>(car6,
                              new ConsLo<>(car7,
                                  new ConsLo<>(car8,
                                      new ConsLo<>(car9,
                                          new MtLo<>()))))))))));
  Board board = new Board(vehicleList, new MtLo<>(), new Posn(7, 4));
  
  boolean testBoardSelectionMethods(Tester t) {
    IList<IVehicle> listNoSelect = new ConsLo<>(notSelectCar,
        new ConsLo<>(notSelectTruck,
            new ConsLo<>(notSelectTruck,
                new ConsLo<>(notSelectCar,
                    new MtLo<>()))));
    
    IList<IVehicle> listSelect = new ConsLo<>(notSelectCar,
        new ConsLo<>(notSelectTruck,
            new ConsLo<>(selectTruck,
                new ConsLo<>(notSelectCar,
                    new MtLo<>()))));
    
    Board boardSelect = new Board(listSelect, new MtLo<>(), new Posn(3, 3));
    Board boardNoSelect = new Board(listNoSelect, new MtLo<>(), new Posn(3, 3));
    
    return t.checkExpect(boardSelect.boardVehicleSelected(), true)
               && t.checkExpect(boardNoSelect.boardVehicleSelected(), false)
               && t.checkExpect(boardSelect.resetBoardSelection(), boardNoSelect)
               && t.checkExpect(boardNoSelect.resetBoardSelection(), boardNoSelect)
               
               && t.checkExpect(board.makeSelection(new Posn(120, 180)),
        new Board(vehicleList2, new MtLo<>(), new Posn(7,4)))
               
               && t.checkExpect(board.makeSelection(new Posn(40, 130)),
        new Board(vehicleList3, new MtLo<>(), new Posn(7,4)));
    
  }
  
  
  String demoLevel =
      "+------+\n" +
          "|      |\n" +
          "|  C T |\n" +
          "|a    CX\n" +
          "|t     |\n" +
          "|CCC c |\n" +
          "|    c |\n" +
          "+------+";
  Level demo = new Level(demoLevel);
  
  String levelWon =
      "+------+\n" +
          "|      |\n" +
          "|  C   |\n" +
          "|     aX\n" +
          "|t     |\n" +
          "|CCC c |\n" +
          "|    c |\n" +
          "+------+";
  
  Level wonLevel = new Level(levelWon);
  
  String level2Str = "+------+\n" +
                         "|c    T|\n" +
                         "|T  T  |\n" +
                         "| a    |\n" +
                         "|      X\n" +
                         "|C   c |\n" +
                         "|  t   |\n" +
                         "+------+";
  
  Level level2 = new Level(level2Str);
  String levelStrFAIL = "+------+\n" +
                            "|cT   T|\n" +
                            "|T  T  |\n" +
                            "| a    |\n" +
                            "|      X\n" +
                            "|C   c |\n" +
                            "|  t   |\n" +
                            "+------+";
  
  boolean testFailedConstruct(Tester t) {
    return t.checkConstructorException(new IllegalStateException("Bad state, cars are overlapping"),
        "Level",
        levelStrFAIL);
  }
  
  String levelStr2FAIL = "+------+\n" +
                             "|c   T |\n" +
                             "|T  T  |\n" +
                             "| a    |\n" +
                             "|      X\n" +
                             "|C   cc|\n" +
                             "|  t   |\n" +
                             "+------+";
  
  boolean testFailedConstruct2(Tester t) {
    return t.checkConstructorException(new IllegalStateException("Bad state, cars are overlapping"),
        "Level",
        levelStr2FAIL);
  }
  
  String levelStr3FAIL = "+------+\n" +
                             "|c   T |\n" +
                             "|T  T  |\n" +
                             "| a    |\n" +
                             "|      X\n" +
                             "|C    c|\n" +
                             "|t t   |\n" +
                             "+------+";
  
  boolean testFailedConstruct3(Tester t) {
    return t.checkConstructorException(new IllegalStateException("Bad state, cars are overlapping"),
        "Level",
        levelStr3FAIL);
  }
  
  boolean testLevelMethods(Tester t) {
    Level level = new Level(
        new Board(vehicleList, new MtLo<>(), new Posn(7, 4)), new Posn(7, 4));
    Level levelSelected = new Level(
        new Board(vehicleList2, new MtLo<>(), new Posn(7, 4)), new Posn(7, 4));
    Level levelSelected2 = new Level(
        new Board(vehicleList3, new MtLo<>(), new Posn(7, 4)), new Posn(7,4));
    
    return t.checkExpect(demo.levelWon(), false)
               && t.checkExpect(wonLevel.levelWon(), true)
               
               && t.checkExpect(level.selectVehicle(new Posn(120, 180)), levelSelected)
               && t.checkExpect(level.selectVehicle(new Posn(40, 130)), levelSelected2)
               
               && t.checkExpect(levelSelected.resetSelection(), level)
               && t.checkExpect(levelSelected2.resetSelection(), level)
               
               && t.checkExpect(level.vehicleSelected(), false)
               && t.checkExpect(levelSelected.vehicleSelected(), true)
               && t.checkExpect(levelSelected2.vehicleSelected(), true);
  }
  
  boolean testUtils(Tester t) {
    Util util = new Util();
    
    return t.checkExpect(util.stringToVehicles(demoLevel), vehicleList)
               && t.checkExpect(util.stringToVehicleList(demoLevel,
        0, 0, 0,0, new MtLo<>()), vehicleList)
               
               && t.checkExpect(util.exitPosnAcc(demoLevel, 0,0,0), new Posn(7, 3))
               && t.checkExpect(util.exitPosnAcc(level2Str, 0,0,0), new Posn(7,4))
               && t.checkExpect(util.findExitPosn(demoLevel), new Posn(7, 3))
               && t.checkExpect(util.findExitPosn(level2Str), new Posn(7, 4))
               
               && t.checkExpect(util.isBoundaryValid(demoLevel), true)
               && t.checkExpect(util.isBoundaryValid("+---+"), false)
               
               && t.checkExpect(util.validCharacters("skdlflkasfj", 0), false)
               && t.checkExpect(util.validCharacters("+---+aTc", 0), true)
               
               && t.checkExpect(util.boundaryHeight(demoLevel, 0, 0), 8)
               && t.checkExpect(util.boundaryHeight("+--------+", 0, 0), 1)
               && t.checkExpect(util.boundaryHeight("+----+\n+|   |+\n+-----+",0,0), 3)
               
               && t.checkExpect(util.boundaryRect(demoLevel, 0, 0, 0), true)
               && t.checkExpect(util.boundaryRect(levelWon,0,0,0), true)
               && t.checkExpect(util.boundaryRect(level2Str,0,0,0), true)
               && t.checkExpect(util.boundaryRect("+-----+\n|     |",
        0,0,0), false);
  }
  
  boolean testDrawBoundary1(Tester t) {
    Boundary bound = new Boundary(new Posn(0,0));
    return t.checkExpect(bound.renderBoundary(),
        (new RectangleImage(30, 30, "solid", Color.gray)
             .movePinholeTo(new Posn(-15,-15))));
  }
  
  boolean testDrawBoundary2(Tester t) {
    Boundary bound = new Boundary(new Posn(6,6));
    return t.checkExpect(bound.renderBoundary(),
        (new RectangleImage(30, 30, "solid", Color.gray))
            .movePinholeTo(new Posn(-15,-15)));
  }
  
  
  Car car11 = new Car(new Posn(0,0),false,false,false,Color.black);
  
  boolean testCar1(Tester t) {
    return t.checkExpect(car11.renderVehicle(),
        (new RectangleImage(60, 30, "solid", Color.black))
            .movePinholeTo(new Posn(-30, -15)));
  }
  
  
  Car car22 = new Car(new Posn(0,0),true,true,false,Color.red);
  
  boolean testCar2(Tester t) {
    return t.checkExpect(car22.renderVehicle(),
        new RotateImage(new RectangleImage(60, 30, "solid", Color.red), 90
        ).movePinholeTo(new Posn(-15, -30)));
  }
  
  
  Car car33 = new Car(new Posn(0,0),true,true,true,Color.red);
  
  boolean testCar3(Tester t) {
    return t.checkExpect(car33.renderVehicle(),
        new RotateImage(new RectangleImage(60, 30, "solid", Color.yellow), 90
        ).movePinholeTo(new Posn(-15, -30)));
  }
  
  
  
  Truck truck1 = new Truck(new Posn(0,0),false,false,false,Color.black);
  
  boolean testTruck1(Tester t) {
    return t.checkExpect(truck1.renderVehicle(),
        (new RectangleImage(90, 30, "solid", Color.black))
            .movePinholeTo(new Posn(-45, -15)));
  }
  
  
  Truck truck2 = new Truck(new Posn(0,0),true,true,false,Color.blue);
  
  boolean testTruck2(Tester t) {
    return t.checkExpect(truck2.renderVehicle(),
        new RotateImage(new RectangleImage(90, 30, "solid", Color.blue), 90
        ).movePinholeTo(new Posn(-15, -45)));
  }
  
  
  Truck truck3 = new Truck(new Posn(10,10),true,true,true,Color.red);
  
  boolean testTruck3(Tester t) {
    return t.checkExpect(truck3.renderVehicle(),
        new RotateImage(new RectangleImage(90, 30, "solid", Color.yellow), 90
        ).movePinholeTo(new Posn(-15, -45)));
  }
  
  
  boolean testOrMap(Tester t) {
    return t.checkExpect((new ConsLo<IVehicle>(car1, new MtLo<>()))
                             .compareOrMap(new AreOverlapping(), car1), true);
  }
  
  boolean testOrMap2(Tester t) {
    return t.checkExpect((new ConsLo<IVehicle>(car1, new MtLo<>()))
                             .compareOrMap(new AreOverlapping(), truck3), false);
  }
  
  boolean testOrMap3(Tester t) {
    return t.checkExpect((new ConsLo<IVehicle>(truck3, new ConsLo<IVehicle>(car11, new MtLo<>()))
    ).compareOrMap(new AreOverlapping(), car22), true);
  }
  
  
  boolean testAllOrMap(Tester t) {
    return t.checkExpect((new MtLo<IVehicle>()).compareAllOrMap(new AreOverlapping()), false);
  }
  
  boolean testAllOrMap2(Tester t) {
    return t.checkExpect((new ConsLo<IVehicle>(truck3, new ConsLo<IVehicle>(truck2, new MtLo<>()))
    ).compareAllOrMap(new AreOverlapping()), false);
  }
  
  boolean testAllOrMap3(Tester t) {
    return t.checkExpect((new ConsLo<IVehicle>(car22, new ConsLo<IVehicle>(car11, new MtLo<>()))
    ).compareAllOrMap(new AreOverlapping()), true);
  }
  
  boolean testOverlappingBoard1(Tester t) {
    return t.checkExpect((new Board(new ConsLo<IVehicle>(truck3,
        new ConsLo<IVehicle>(truck2, new MtLo<>())),
        new MtLo<Boundary>(), new Posn(0,0))).isBadOverlap(), false);
  }
  
  boolean testOverlappingBoard2(Tester t) {
    return t.checkExpect((new Board(new MtLo<IVehicle>(),
        new MtLo<Boundary>(), new Posn(0,0))).isBadOverlap(), false);
  }
  
  boolean testOverlappingBoard3(Tester t) {
    return t.checkExpect((new Board((new ConsLo<IVehicle>(truck1,
        new ConsLo<IVehicle>(car22, new MtLo<>()))),
        new MtLo<Boundary>(), new Posn(0,0))).isBadOverlap(), true);
  }
  
  IList<Boundary> boundaryList1 = new MtLo<>();
  IList<Boundary> boundaryList2 = new ConsLo<>(new Boundary(new Posn(0,0)), boundaryList1);
  IList<Boundary> boundaryList3 = new ConsLo<>(new Boundary(new Posn(5,1)), boundaryList2);
  
  boolean testListBoundaries1(Tester t) {
    return t.checkExpect(boundaryList1.foldr(new RenderBoundaryToImage(),
        new WorldScene(500, 500)), new WorldScene(500, 500));
  }
  
  boolean testListBoundaries2(Tester t) {
    return t.checkExpect(boundaryList2.foldr(new RenderBoundaryToImage(),
            new WorldScene(500, 500)),
        (new WorldScene(500, 500))
            .placeImageXY(new Boundary(new Posn(0,0)).renderBoundary(),0,0));
  }
  
  boolean testListBoundaries3(Tester t) {
    return t.checkExpect(boundaryList3.foldr(new RenderBoundaryToImage(),
            new WorldScene(500, 500)),
        boundaryList2.foldr(new RenderBoundaryToImage(),
                new WorldScene(500, 500))
            .placeImageXY(new Boundary(new Posn(0,0)).renderBoundary(),150,30));
  }
  
  
  IList<IVehicle> vehicleList1 = new MtLo<>();
  IList<IVehicle> vehicleList4 = new ConsLo<>(car11, vehicleList1);
  IList<IVehicle> vehicleList5 = new ConsLo<>(truck3, vehicleList4);
  
  boolean testListVehicles1(Tester t) {
    return t.checkExpect(vehicleList1.foldr(new RenderVehicleToImage(),
        new WorldScene(500, 500)), new WorldScene(500, 500));
  }
  
  boolean testListVehicles2(Tester t) {
    return t.checkExpect(vehicleList4.foldr(new RenderVehicleToImage(),
            new WorldScene(500, 500)),
        (new WorldScene(500, 500)).placeImageXY(car11.renderVehicle(),0,0));
  }
  
  boolean testListVehicles3(Tester t) {
    return t.checkExpect(vehicleList5.foldr(new RenderVehicleToImage(),
            new WorldScene(500, 500)),
        vehicleList4.foldr(new RenderVehicleToImage(),
            new WorldScene(500, 500)).placeImageXY(truck3.renderVehicle(),300,300));
  }
  
  Board board1 = new Board(vehicleList1, boundaryList1, new Posn(0,0));
  Board board2 = new Board(vehicleList4, boundaryList2, new Posn(10,0));
  Board board3 = new Board(vehicleList5, boundaryList3, new Posn(0,10));
  
  boolean testListBoard1(Tester t) {
    return t.checkExpect(board1.renderBoard(),
        boundaryList1.foldr(new RenderBoundaryToImage(),
            vehicleList1.foldr(new RenderVehicleToImage(), new WorldScene(0, 240))));
  }
  
  boolean testListBoard2(Tester t) {
    return t.checkExpect(board2.renderBoard(),
        boundaryList2.foldr(new RenderBoundaryToImage(),
            vehicleList4.foldr(new RenderVehicleToImage(), new WorldScene(0, 240))));
  }
  
  boolean testListBoard3(Tester t) {
    return t.checkExpect(board3.renderBoard(),
        boundaryList3.foldr(new RenderBoundaryToImage(),
            vehicleList5.foldr(new RenderVehicleToImage(), new WorldScene(5, 240))));
  }
  
  boolean testLargestXY(Tester t) {
    Boundary boundary = new Boundary(new Posn(2,0));
    return t.checkExpect(boundary.greaterX(1), true)
               && t.checkExpect(boundary.greaterX(3), false)
               && t.checkExpect(boundary.greaterY(1), false)
               && t.checkExpect(boundary.greaterY(-1), true)
               
               && t.checkExpect(new LargestXY("x").apply(boundary, new Posn(1,0)), new Posn(2,0))
               && t.checkExpect(new LargestXY("y").apply(boundary, new Posn(1,0)), new Posn(1,0))
               
               && t.checkExpect(demo.board.largestBoundaryX(), 7)
               && t.checkExpect(demo.board.largestBoundaryY(), 7)
               && t.checkExpect(demo.sceneSize(), new Posn(7,7));
  }
  
  boolean testOverlapBoundary(Tester t) {
    Boundary boundary = new Boundary(new Posn(1, 5));
    IVehicle car = new Car(new Posn(1,4), true, false,false,Color.black);
    IVehicle car1 = new Car(new Posn(1,4), false, false,false,Color.black);
    IVehicle car2 = new Car(new Posn(1,4), false, false,false,Color.black);
    
    IList<IVehicle> list = new ConsLo<>(car,
        new ConsLo<>(car2,
            new ConsLo<>(car1, new MtLo<>())));
    
    IList<IVehicle> list2 = new ConsLo<>(car2,
        new ConsLo<>(car2,
            new ConsLo<>(car1, new MtLo<>())));
    
    return t.checkExpect(new OverlapsBoundary().apply(car, boundary), true)
               && t.checkExpect(new OverlapsBoundary().apply(car, new Boundary(new Posn(1,6))), false)
               && t.checkExpect(new OverlapsAnyBoundaryVehicle().apply(boundary, list), true)
               && t.checkExpect(new OverlapsAnyBoundaryVehicle().apply(boundary, list2), false)
               && t.checkExpect(new Board(list, new ConsLo<>(boundary, new MtLo<>()),
            new Posn(3,4)).isBadVehicles(),
        true)
               && t.checkExpect(new Board(list2, new ConsLo<>(boundary, new MtLo<>()),
            new Posn(3,4)).isBadVehicles(),
        false);
  }
  
  
  boolean testBigBang(Tester t) {
    String description = demoLevel; // replace with your description
    Level level = new Level(description);
    
    RushHourWorld w = new RushHourWorld(level);
    int worldWidth = (level.sceneSize().x + 1) * 30;
    int worldHeight = (level.sceneSize().y + 1) * 30;
    double tickRate = 0;
    return w.bigBang(worldWidth, worldHeight, tickRate);
  }
  
  
}