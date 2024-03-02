import java.util.function.*;
import tester.*; // The tester library
import javalib.worldimages.*; // images, like RectangleImage or OverlayImages
import javalib.funworld.*; // the abstract World class and the big-bang library
import java.awt.Color; // general colors (as triples of red, green, blue values)
// and predefined colors (Color.RED, Color.GRAY, etc.)

// represents a homogeneous list
interface IList<T> {

  // executes func on every element in this IList<T>, creating a new list based
  // on the outputs
  <R> IList<R> map(Function<T, R> func);

  // determines if _any_ of the items in this IList<T> pass the predicate func
  Boolean orMap(Function<T, Boolean> func);

  // counts the number of times the given predicate returns true when applied
  // to each item in this IList<T>
  Integer count(Function<T, Boolean> func);

  // folds this IList<T> from left to right into a single value
  <R> R foldL(BiFunction<T, R, R> func, R base);

  // determines the number of elements in this IList<T>
  Integer length();

  // returns the object in the list at the given index
  // where index starts at 0
  T getIndex(int i);

  // finds the first item in this IList<T> that passes the given predicate
  // otherwise returns base
  int find(Function<T, Boolean> func);

  // returns the current index if the item passed the predicate
  // otherwise increment by one recursively
  int findIndex(Function<T, Boolean> func, int acc);
}

// represents the last empty element in a homogeneous list
class MtList<T> implements IList<T> {
  /*
   * TEMPLATE for MtList:
   * 
   * Fields:
   * 
   * Methods: ... this.map(Function<T, R>) ... -- IList<R> ...
   * this.orMap(Function<T, Boolean>) ... -- Boolean ... this.count(Function<T,
   * Boolean>) ... -- Integer ... this.foldL(BiFunction<T, R, R>, R) ... -- R ...
   * this.length() ... -- Integer ... this.getIndex(int) ... -- T ...
   * this.find(Function<T, Boolean>) ... -- int ... this.findIndex(Function<T,
   * Boolean>, int) ... -- int Methods on fields:
   */

  // executes func on every element in this MtList<T>, creating a new list based
  // on the outputs
  public <R> IList<R> map(Function<T, R> func) {
    /*
     * TEMPLATE Parameters: ... func ... -- Function<T, R> Methods on Parameters:
     * ... func.apply(T) ... -- R
     */
    return new MtList<R>();
  }

  // determines if _any_ of the items in this MtList<T> pass the predicate func
  public Boolean orMap(Function<T, Boolean> func) {
    /*
     * TEMPLATE Parameters: ... func ... -- Function<T, Boolean> Methods on
     * Parameters: ... func.apply(T) ... -- Boolean
     */
    return false;
  }

  // counts the number of times the given predicate returns true when applied
  // to each item in this MtList<T>
  public Integer count(Function<T, Boolean> func) {
    /*
     * TEMPLATE Parameters: ... func ... -- Function<T, Boolean> Methods on
     * Parameters: ... func.apply(T) ... -- Boolean
     */
    return 0;
  }

  // folds this MtList<T> from left to right into a single value
  public <R> R foldL(BiFunction<T, R, R> func, R base) {
    /*
     * TEMPLATE Parameters: ... func ... -- BiFunction<T, R, R> ... base ... -- R
     * Methods on Parameters: ... func.apply(T, R) ... -- R
     */
    return base;
  }

  // determines the number of elements in this MtList<T>
  public Integer length() {
    return 0;
  }

  // returns the object in the list at the given index
  // where index starts at 0
  public T getIndex(int i) {
    /*
     * TEMPLATE Parameters: ... i ... -- int Methods on Parameters:
     */
    throw new IllegalArgumentException("That index cannot be accessed.");
  }

  // finds the first item in this IList<T> that passes the given predicate
  // otherwise returns base
  public int find(Function<T, Boolean> func) {
    /*
     * TEMPLATE Parameters: ... func ... -- Function<T, Boolean> Methods on
     * Parameters: ... func.apply(T) ... -- Boolean
     */
    return -1;
  }

  // returns the current index if the item passed the predicate
  // otherwise increment by one recursively
  public int findIndex(Function<T, Boolean> func, int index) {
    /*
     * TEMPLATE Parameters: ... func ... -- Function<T, Boolean> ... index ... --
     * int Methods on Parameters: ... func.apply(T) ... -- Boolean
     */
    return -1;
  }
}

// represents a non empty item in a list
class ConsList<T> implements IList<T> {
  // the current item in the list
  T first;
  // the list of the rest of the items
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  /*
   * TEMPLATE
   * 
   * Fields: ... this.first ... -- T ... this.rest ... -- IList<T> Methods: ...
   * this.map(Function<T, R>) ... -- IList<R> ... this.orMap(Function<T, Boolean>)
   * ... -- Boolean ... this.count(Function<T, Boolean>) ... -- Integer ...
   * this.foldL(BiFunction<T, R, R>, R) ... -- R ... this.length() ... -- Integer
   * ... this.getIndex(int) ... -- T ... this.find(Function<T, Boolean>) ... --
   * int ... this.findIndex(Function<T, Boolean>, int) ... -- int Methods on
   * fields: ... this.rest.map(Function<T, R>) ... -- IList<R> ...
   * this.rest.orMap(Function<T, Boolean>) ... -- Boolean ...
   * this.rest.count(Function<T, Boolean>) ... -- Integer ...
   * this.rest.foldL(BiFunction<T, R, R>, R) ... -- R ... this.rest.length() ...
   * -- Integer ... this.rest.getIndex(int) ... -- T ...
   * this.rest.find(Function<T, Boolean>) ... -- int ...
   * this.rest.findIndex(Function<T, Boolean>, int) ... -- int
   */

  // executes func on every element in this ConsList<T>, creating a new list based
  // on the outputs
  public <R> IList<R> map(Function<T, R> func) {

    /*
     * TEMPLATE Parameters: ... func ... -- Function<T, R> Methods on Parameters:
     * ... func.apply(T) ... -- R
     */
    return new ConsList<R>(func.apply(this.first), this.rest.map(func));
  }

  // determines if _any_ of the items in this ConsList<T> pass the predicate func
  public Boolean orMap(Function<T, Boolean> func) {
    /*
     * TEMPLATE Parameters: ... func ... -- Function<T, Boolean> Methods on
     * Parameters: ... func.apply(T) ... -- Boolean
     */
    return func.apply(this.first) || this.rest.orMap(func);
  }

  // counts the number of times the given predicate returns true when applied
  // to each item in this ConsList<T>
  public Integer count(Function<T, Boolean> func) {
    /*
     * TEMPLATE Parameters: ... func ... -- Function<T, Boolean> Methods on
     * Parameters: ... func.apply(T) ... -- Boolean
     */
    boolean res = func.apply(this.first);
    if (res) {
      return 1 + this.rest.count(func);
    }
    else {
      return this.rest.count(func);
    }
  }

  // folds this ConsList<T> from left to right into a single value
  public <R> R foldL(BiFunction<T, R, R> func, R base) {
    /*
     * TEMPLATE Parameters: ... func ... -- BiFunction<T, R, R> ... base ... -- R
     * Methods on Parameters: ... func.apply(T, R) ... -- R
     */
    return this.rest.foldL(func, func.apply(this.first, base));
  }

  // determines the number of elements in this ConsList<T>
  public Integer length() {
    return 1 + this.rest.length();
  }

  // returns the object in the list at the given index
  // where index starts at 0
  public T getIndex(int i) {
    /*
     * TEMPLATE Parameters: ... i ... -- int Methods on Parameters:
     */
    if (i == 0) {
      return this.first;
    }
    return this.rest.getIndex(i - 1);
  }

  // finds the first item in this IList<T> that passes the given predicate
  // otherwise returns base
  public int find(Function<T, Boolean> func) {
    /*
     * TEMPLATE Parameters: ... func ... -- Function<T, Boolean> Methods on
     * Parameters: ... func.apply(T) ... -- Boolean
     */
    return this.findIndex(func, 0);
  }

  // returns the current index if the item passed the predicate
  // otherwise increment by one recursively
  public int findIndex(Function<T, Boolean> func, int index) {
    /*
     * TEMPLATE Parameters: ... func ... -- Function<T, Boolean> ... index ... --
     * int Methods on Parameters: ... func.apply(T) ... -- Boolean
     */
    if (func.apply(this.first)) {
      return index;
    }
    return this.rest.findIndex(func, index + 1);
  }
}

// represents a rush hour c state
class Board {
  // the list of vehicles on the board
  // NOTE: the order of the vehicles should NOT change;
  IList<IVehicle> vehicles;
  // the width of the board
  int width;
  // the height of the board, in terms of grid squares
  int height;
  // the position of the exit of the board
  Position exit;

  // represents the index of the vehicle
  // in this.vehicles
  // -1 represents no vehicle being selected
  int selected;

  /*
   * TEMPLATE for Board:
   * 
   * Fields: ... this.vehicles ... -- IList<IVehicle> ... this.width ... -- int
   * ... this.height ... -- int ... this.exit ... -- Position ... this.selected
   * ... -- int
   * 
   * Methods: ... this.toImage(int) ... -- WorldImage ... this.gameWon() ... --
   * boolean ... this.selectTile(Position) ... -- Board
   * 
   * Methods on fields: ... this.vehicles.map(Function<IVehicle, R>) ... --
   * IList<R> ... this.vehicles.orMap(Function<IVehicle, Boolean>) ... -- Boolean
   * ... this.vehicles.count(Function<IVehicle, Boolean>) ... -- Integer ...
   * this.vehicles.foldL(BiFunction<IVehicle, R, R>, R) ... -- R ...
   * this.vehicles.length() ... -- Integer ... this.vehicles.getIndex(int) ... --
   * IVehicle ... this.vehicles.find(Function<IVehicle, Boolean>) ... -- int ...
   * this.vehicles.findIndex(Function<IVehicle, Boolean>, int) ... -- int ...
   * this.exit.diff(int, int) ... -- Position ... this.exit.nextStart() ... --
   * Position ... this.exit.sameX(Position) ... -- boolean ...
   * this.exit.sameY(Position) ... -- boolean ... this.exit.xPosition() ... -- int
   * ... this.exit.yPosition() ... -- int ... this.exit.maxX(Position) ... -- int
   * ... this.exit.maxY(Position) ... -- int ... this.exit.minX(Position) ... --
   * int ... this.exit.minY(Position) ... -- int
   * 
   */

  Board(IList<IVehicle> vehicles, int width, int height, Position exit, int selected) {
    this.vehicles = vehicles;
    this.width = width;
    this.height = height;
    this.exit = exit;
    this.selected = selected; // fill in value for no selected vehicle
  }

  // converts this Board to a WorldImage given the height of a tile
  public WorldImage toImage(int tileHeight) {
    /*
     * TEMPLATE Parameters: ... tileHeight ... -- int Methods on Parameters:
     */
    // the grey border around the board
    WorldImage border = new RectangleImage(tileHeight * (this.width + 2),
        tileHeight * (this.height + 2), OutlineMode.SOLID, Color.GRAY);

    // the base image that contains the vehicles within the border
    // NOTE: has a white outline to align with the outlines of the vehicles
    WorldImage base = new OverlayImage(
        new RectangleImage(tileHeight * this.width, tileHeight * this.height, OutlineMode.OUTLINE,
            Color.WHITE),
        new RectangleImage(tileHeight * this.width, tileHeight * this.height, OutlineMode.SOLID,
            Color.WHITE));

    // overlays all of the vehicles onto the white base
    WorldImage withVehicles = this.vehicles.foldL(new DrawVehicleOnto(tileHeight), base);

    // adds the selected truck on top of the rest
    WorldImage withSelected = withVehicles;

    // if there is a selected vehicle, draw it on top of the rest
    if (this.selected != -1) {
      withSelected = new DrawVehicleOnto(tileHeight).apply(
          // Vehicle.asSelected() sets the color of the vehicle to yellow
          this.vehicles.getIndex(this.selected).asSelected(), withVehicles);
    }

    // the image with the grey block border
    WorldImage withBorder = new OverlayImage(withSelected, border);

    // the exit white square overlay to indicate the exit
    WorldImage exitImage = new RectangleImage(tileHeight, tileHeight, OutlineMode.SOLID,
        Color.WHITE);

    // the image with the white square overlay
    WorldImage withExit = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, exitImage,
        (double) ((this.exit.xPosition() + 1) * tileHeight * -1),
        (double) ((this.exit.yPosition() + 1) * tileHeight * -1), withBorder);

    return withExit;
  }

  // returns if the game has ended by determining if the target vehicle
  // is next to the exit (not overlapping)
  public boolean gameWon() {
    // if isTargetCar() && sOverlappingWithSegment(exit, exit)
    return this.vehicles.orMap(new Overlap(this.exit));

  }

  // creates a new Board with the selected replaced based on the given position
  public Board selectTile(Position p) {
    /*
     * TEMPLATE Parameters: ... p ... -- Position Methods on Parameters: ...
     * p.diff(int, int) ... -- Position ... p.nextStart() ... -- Position ...
     * p.sameX(Position) ... -- boolean ... p.sameY(Position) ... -- boolean ...
     * p.xPosition() ... -- int ... p.yPosition() ... -- int ... p.maxX(Position)
     * ... -- int ... p.maxY(Position) ... -- int ... p.minX(Position) ... -- int
     * ... p.minY(Position) ... -- int
     */
    return new Board(this.vehicles, this.width, this.height, this.exit,
        this.vehicles.find(new IsAtPosition(p)));
  }
}

// a function that determines if the target vehicle of the board
// overlaps with the given tile
// USE: in determining if the target vehicle is at the exit
// NOTE: not to be confused with IsAtPosition(Position class)
class Overlap implements Function<IVehicle, Boolean> {
  // the position of the tile of interestt (exit)
  Position exit;

  Overlap(Position exit) {
    this.exit = exit;
  }

  /*
   * TEMPLATE Fields: ... this.exit ... -- Position
   * 
   * Methods: ... this.apply(IVehicle) ... -- Boolean
   * 
   * Methods on fields: ... this.exit.diff(int, int) ... -- Position ...
   * this.exit.nextStart() ... -- Position ... this.exit.sameX(Position) ... --
   * boolean ... this.exit.sameY(Position) ... -- boolean ...
   * this.exit.xPosition() ... -- int ... this.exit.yPosition() ... -- int ...
   * this.exit.maxX(Position) ... -- int ... this.exit.maxY(Position) ... -- int
   * ... this.exit.minX(Position) ... -- int ... this.exit.minY(Position) ... --
   * int
   */

  // determines if the vehicle is the target and overlaps with the given tile
  // position
  public Boolean apply(IVehicle v) {
    /*
     * TEMPLATE Parameters: ... v ... -- IVehicle Methods on Parameters: ...
     * v.isOverlapping(IVehicle) ... -- boolean ...
     * v.isOverlappingWithSegment(Position, Position) ... -- boolean ...
     * v.isOverlappingWithPoint(Position) ... -- boolean ... v.asSelected() ... --
     * IVehicle ... v.addToImage(int, WorldImage) ... -- WorldImage ...
     * v.toImage(int) ... -- WorldImage ... v.shouldBeTarget(Position) ... --
     * boolean ... v.target() ... -- boolean ... v.setTargetFromExitPos(Position)
     * ... -- IVehicle
     */
    return v.target() && v.isOverlappingWithSegment(this.exit, this.exit);
  }
}

// determines if the a general vehicle overlaps with the given positon
class IsAtPosition implements Function<IVehicle, Boolean> {
  // the position of the tile of interest
  Position p;

  IsAtPosition(Position p) {
    this.p = p;
  }

  /*
   * TEMPLATE
   * 
   * Fields: ... this.p ... -- Position
   * 
   * Methods: ... this.apply(IVehicle) ... -- Boolean
   * 
   * Methods on fields: ... this.p.isOverlapping(IVehicle) ... -- boolean ...
   * this.p.isOverlappingWithSegment(Position, Position) ... -- boolean ...
   * this.p.isOverlappingWithPoint(Position) ... -- boolean ...
   * this.p.asSelected() ... -- IVehicle ... this.p.addToImage(int, WorldImage)
   * ... -- WorldImage ... this.p.toImage(int) ... -- WorldImage ...
   * this.p.shouldBeTarget(Position) ... -- boolean ... this.p.target() ... --
   * boolean ... this.p.setTargetFromExitPos(Position) ... -- IVehicle
   */

  public Boolean apply(IVehicle v) {
    return v.isOverlappingWithPoint(p);
  }
}

// represents a function that draws the given image of this vehicle onto the
// given worldimage
class DrawVehicleOnto implements BiFunction<IVehicle, WorldImage, WorldImage> {
  // the height of a tile on the board
  int tileHeight;

  DrawVehicleOnto(int tileHeight) {
    this.tileHeight = tileHeight;
  }

  /*
   * TEMPLATE:
   * 
   * Fields: ... this.tileHeight ... -- int
   * 
   * Methods: ... this.apply(IVehicle, WorldImage) ... -- WorldImage
   * 
   * Methods on fields:
   */

  // returns a new image with the vehicle added to the worldimage
  public WorldImage apply(IVehicle vehicle, WorldImage img) {
    /*
     * TEMPLATE Parameters: ... vehicle ... -- IVehicle ... img ... -- WorldImage
     * Methods on Parameters: ... vehicle.isOverlapping(IVehicle) ... -- boolean ...
     * vehicle.isOverlappingWithSegment(Position, Position) ... -- boolean ...
     * vehicle.isOverlappingWithPoint(Position) ... -- boolean ...
     * vehicle.asSelected() ... -- IVehicle ... vehicle.addToImage(int, WorldImage)
     * ... -- WorldImage ... vehicle.toImage(int) ... -- WorldImage ...
     * vehicle.shouldBeTarget(Position) ... -- boolean ... vehicle.target() ... --
     * boolean ... vehicle.setTargetFromExitPos(Position) ... -- IVehicle
     * 
     */
    return vehicle.addToImage(tileHeight, img);
  }
}

// represents the x&y (row&column) of a tile on the board
// where Position(0, 0) is the top left corner of the board.
//
// Negative values may be used where appropriate, e.g. to represent
// the position of the exit in the top/left boarder (x, -1) or (-1, y).
//
// WARNING:
// Should NOT be used to represent pixel values anywhere in the
// code. Use new Posn(x, y) for pixel values instead to prevent
// confusion.
class Position {
  // the x position of the tile
  int x;
  // the y position of the tile
  int y;

  Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /*
   * TEMPLATE for Position:
   * 
   * Fields: ... this.y ... -- int ... this.x ... -- int
   * 
   * Methods: ... this.diff(int, int) ... -- Position ... this.nextStart() ... --
   * Position ... this.sameX(Position) ... -- boolean ... this.sameY(Position) ...
   * -- boolean ... this.xPosition() ... -- int ... this.yPosition() ... -- int
   * ... this.maxX(Position) ... -- int ... this.maxY(Position) ... -- int ...
   * this.minX(Position) ... -- int ... this.minY(Position) ... -- int
   * 
   * Methods on fields:
   */

  // changes this Position by the given dx and dy values
  public Position diff(int dx, int dy) {
    /*
     * TEMPLATE Parameters: ... dx ... -- int ... dy ... -- int
     */
    return new Position(this.x + dx, this.y + dy);
  }

  // resets this position for the start of a new row when parsing a String
  // representation of a board
  public Position nextStart() {
    return new Position(-1, this.y + 1);
  }

  // determines the position have the same horizontal position
  public boolean sameX(Position other) {
    /*
     * TEMPLATE Parameters: ... other ... -- Position Methods on Parameters:
     * other.diff(int, int) ... -- Position other.nextStart() ... -- Position
     * other.sameX(Position) ... -- boolean other.sameY(Position) ... -- boolean
     * other.xPosition() ... -- int other.yPosition() ... -- int
     * other.maxX(Position) ... -- int other.maxY(Position) ... -- int
     * other.minX(Position) ... -- int other.minY(Position) ... -- int
     * 
     */
    return this.x == other.x;
  }

  // determines the position have the same vertical position
  public boolean sameY(Position other) {
    /*
     * TEMPLATE Parameters: ... other ... -- Position Methods on Parameters:
     * other.diff(int, int) ... -- Position other.nextStart() ... -- Position
     * other.sameX(Position) ... -- boolean other.sameY(Position) ... -- boolean
     * other.xPosition() ... -- int other.yPosition() ... -- int
     * other.maxX(Position) ... -- int other.maxY(Position) ... -- int
     * other.minX(Position) ... -- int other.minY(Position) ... -- int
     * 
     */
    return this.y == other.y;
  }

  // determines the x coordinate of this Position
  public int xPosition() {
    return x;
  }

  // determines the y coordinate of this Position
  public int yPosition() {
    return y;
  }

  // returns the larger x value of two positions
  public int maxX(Position other) {
    /*
     * TEMPLATE Parameters: ... other ... -- Position Methods on Parameters:
     * other.diff(int, int) ... -- Position other.nextStart() ... -- Position
     * other.sameX(Position) ... -- boolean other.sameY(Position) ... -- boolean
     * other.xPosition() ... -- int other.yPosition() ... -- int
     * other.maxX(Position) ... -- int other.maxY(Position) ... -- int
     * other.minX(Position) ... -- int other.minY(Position) ... -- int
     * 
     */
    return Math.max(this.x, other.x);
  }

  // returns the larger y value of two positions
  public int maxY(Position other) {
    /*
     * TEMPLATE Parameters: ... other ... -- Position Methods on Parameters:
     * other.diff(int, int) ... -- Position other.nextStart() ... -- Position
     * other.sameX(Position) ... -- boolean other.sameY(Position) ... -- boolean
     * other.xPosition() ... -- int other.yPosition() ... -- int
     * other.maxX(Position) ... -- int other.maxY(Position) ... -- int
     * other.minX(Position) ... -- int other.minY(Position) ... -- int
     * 
     */
    return Math.max(this.y, other.y);
  }

  // returns the smaller x value of two positions
  public int minX(Position other) {
    /*
     * TEMPLATE Parameters: ... other ... -- Position Methods on Parameters:
     * other.diff(int, int) ... -- Position other.nextStart() ... -- Position
     * other.sameX(Position) ... -- boolean other.sameY(Position) ... -- boolean
     * other.xPosition() ... -- int other.yPosition() ... -- int
     * other.maxX(Position) ... -- int other.maxY(Position) ... -- int
     * other.minX(Position) ... -- int other.minY(Position) ... -- int
     * 
     */
    return Math.min(this.x, other.x);
  }

  // returns the smaller y value of two positions
  public int minY(Position other) {
    /*
     * TEMPLATE Parameters: ... other ... -- Position Methods on Parameters:
     * other.diff(int, int) ... -- Position other.nextStart() ... -- Position
     * other.sameX(Position) ... -- boolean other.sameY(Position) ... -- boolean
     * other.xPosition() ... -- int other.yPosition() ... -- int
     * other.maxX(Position) ... -- int other.maxY(Position) ... -- int
     * other.minX(Position) ... -- int other.minY(Position) ... -- int
     * 
     */
    return Math.min(this.y, other.y);
  }
}

// represents a vehicle on the board
interface IVehicle {

  // CONSTANT VALUE
  // six possible colors for the vehicles in the game
  // does not include YELLOW, which is used to indicate a selected vehicle
  IList<Color> COLOROPTIONS = new ConsList<Color>(Color.MAGENTA,
      new ConsList<Color>(Color.PINK, new ConsList<Color>(Color.ORANGE, new ConsList<Color>(
          Color.GREEN,
          new ConsList<Color>(Color.BLUE, new ConsList<Color>(Color.CYAN, new MtList<Color>()))))));

  // determines whether this IVehicle is overlapping with other IVehicle
  boolean isOverlapping(IVehicle other);

  // determines if the vehicle overlaps with a given line segment
  // WARNING: undefined and undefined behavior if the segment connecting a&b
  // is not a vertical or horizontal line
  boolean isOverlappingWithSegment(Position a, Position b);

  // determines if the vehicle's positions contain a given tile (position)
  // INTERNAL: a proxy call to isOverlappingWithSegment
  boolean isOverlappingWithPoint(Position p);

  // returns a new vehicle with its color set as yellow
  // (to indicate it as being selected)
  IVehicle asSelected();

  // adds this IVehicle to the given WorldImage in its correct position
  WorldImage addToImage(int tileHeight, WorldImage base);

  // draws the image of this IVehicle
  WorldImage toImage(int tileHeight);

  // determines once if the vehicle is a target
  boolean shouldBeTarget(Position exit);

  // determines if the vehicle is the target
  boolean target();

  // sets this.isTarget to true if on the same level with
  // target
  IVehicle setTargetFromExitPos(Position exit);

}

// represents a vehicle on the board
abstract class AVehicle implements IVehicle {
  // the top left tile position of this vehicle
  Position topleft;
  // the length of the vehicle
  int length;

  // NOTE:
  // the positions a vehicle of length n with topleft at Position(x, y)
  // would occupy (does not occupy x+n, but x+n-1)
  // same for y
  // - Position(x, y),
  // - Position(x + 1, y),
  // - ...
  // - Position(x + n - 1, y)

  // the color of this vehicle
  // randomly selected from IVehicle.COLOROPTIONS constant list
  Color color;
  // whether this vehicle is the target, set at board
  boolean isTarget;

  // constructor for if you know everything about the vehicle:
  // its position, length, color, and whether target
  AVehicle(Position topleft, int length, Color color, boolean isTarget) {
    this.topleft = topleft;
    this.length = length;
    this.color = color;
    this.isTarget = isTarget;
  }

  // constructor for if you only know the location and size of the
  // vehicle; used when parsing strings that contain little information
  AVehicle(Position topleft, int length) {
    this.topleft = topleft;
    this.length = length;

    // picks a random index from the color list to make this car
    int someSum = this.topleft.xPosition() + this.topleft.yPosition();

    if (someSum % 5 == 0) {
      this.color = Color.MAGENTA;
    }
    else if (someSum % 4 == 0) {
      this.color = Color.BLUE;
    }
    else if (someSum % 3 == 0) {
      this.color = Color.GREEN;
    }
    else if (someSum % 2 == 0) {
      this.color = Color.ORANGE;
    }
    else {
      this.color = Color.CYAN;

    }

    this.isTarget = false;

  }

  /*
   * TEMPLATE
   * 
   * Constants: ... COLOROPTIONS ... -- IList<Color>
   * 
   * Fields: ... this.topleft ... -- Position ... this.length ... -- int ...
   * this.color ... -- Color ... this.isTarget ... -- boolean
   * 
   * Methods: ... this.isOverlapping(IVehicle) ... -- boolean ...
   * this.isOverlappingWithSegment(Position, Position) ... -- boolean ...
   * this.isOverlappingWithPoint(Position) ... -- boolean ... this.asSelected()
   * ... -- IVehicle ... this.addToImage(int, WorldImage) ... -- WorldImage ...
   * this.toImage(int) ... -- WorldImage ... this.shouldBeTarget(Position) ... --
   * boolean ... this.target() ... -- boolean ...
   * this.setTargetFromExitPos(Position) ... -- IVehicle
   * 
   * Methods on constants: ... COLOROPTIONS.map(Function<Color, R>) ... --
   * IList<Color> ... COLOROPTIONS.orMap(Function<Color, Boolean>) ... -- Boolean
   * ... COLOROPTIONS.count(Function<Color, Boolean>) ... -- Integer ...
   * COLOROPTIONS.foldL(BiFunction<Color, R, R>, R) ... -- R ...
   * COLOROPTIONS.length() ... -- Integer ... COLOROPTIONS.getIndex(int) ... --
   * Color ... COLOROPTIONS.find(Function<Color, Boolean>) ... -- int ...
   * COLOROPTIONS.findIndex(Function<Color, Boolean>, int) ... -- int Methods on
   * fields: ... this.topleft.diff(int, int) ... -- Position ...
   * this.topleft.nextStart() ... -- Position ... this.topleft.sameX(Position) ...
   * -- boolean ... this.topleft.sameY(Position) ... -- boolean ...
   * this.topleft.xPosition() ... -- int ... this.topleft.yPosition() ... -- int
   * ... this.topleft.maxX(Position) ... -- int ... this.topleft.maxY(Position)
   * ... -- int ... this.topleft.minX(Position) ... -- int ...
   * this.topleft.minY(Position) ... -- int
   * 
   */

  // adds this IVehicle to the given WorldImage in its correct position
  public WorldImage addToImage(int tileHeight, WorldImage base) {
    /*
     * TEMPLATE Parameters: ... tileHeight ... -- int ... base ... -- WorldImage
     * Methods on Parameters:
     */

    WorldImage thisImage = this.toImage(tileHeight);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, thisImage,
        (double) (this.topleft.xPosition() * tileHeight * -1),
        (double) (this.topleft.yPosition() * tileHeight * -1), base);
  }

  public boolean target() {
    return this.isTarget;
  }

  // determines if the vehicle's positions contain a given tile (position)
  // INTERNAL: a proxy call to isOverlappingWithSegment
  public boolean isOverlappingWithPoint(Position p) {
    /*
     * TEMPLATE Parameters: ... p ... -- Position Methods on parameters: ...
     * p.diff(int, int) ... -- Position ... p.nextStart() ... -- Position ...
     * p.sameX(Position) ... -- boolean ... p.sameY(Position) ... -- boolean ...
     * p.xPosition() ... -- int ... p.yPosition() ... -- int ... p.maxX(Position)
     * ... -- int ... p.maxY(Position) ... -- int ... p.minX(Position) ... -- int
     * ... p.minY(Position) ... -- int
     */
    return this.isOverlappingWithSegment(p, p);
  }

}

// a vehicle that moves in the x direction
class HVehicle extends AVehicle {
  HVehicle(Position topleft, int length, Color color, boolean isTarget) {
    super(topleft, length, color, isTarget);
  }

  HVehicle(Position topleft, int length) {
    super(topleft, length);
  }

  /*
   * TEMPLATE
   * 
   * Constants: ... COLOROPTIONS ... -- IList<Color>
   * 
   * Fields: ... this.topleft ... -- Position ... this.length ... -- int ...
   * this.color ... -- Color ... this.isTarget ... -- boolean
   * 
   * Methods: ... this.isOverlapping(IVehicle) ... -- boolean ...
   * this.isOverlappingWithSegment(Position, Position) ... -- boolean ...
   * this.isOverlappingWithPoint(Position) ... -- boolean ... this.asSelected()
   * ... -- IVehicle ... this.addToImage(int, WorldImage) ... -- WorldImage ...
   * this.toImage(int) ... -- WorldImage ... this.shouldBeTarget(Position) ... --
   * boolean ... this.target() ... -- boolean ...
   * this.setTargetFromExitPos(Position) ... -- IVehicle
   * 
   * Methods on constants: ... COLOROPTIONS.map(Function<Color, R>) ... --
   * IList<Color> ... COLOROPTIONS.orMap(Function<Color, Boolean>) ... -- Boolean
   * ... COLOROPTIONS.count(Function<Color, Boolean>) ... -- Integer ...
   * COLOROPTIONS.foldL(BiFunction<Color, R, R>, R) ... -- R ...
   * COLOROPTIONS.length() ... -- Integer ... COLOROPTIONS.getIndex(int) ... --
   * Color ... COLOROPTIONS.find(Function<Color, Boolean>) ... -- int ...
   * COLOROPTIONS.findIndex(Function<Color, Boolean>, int) ... -- int Methods on
   * fields: ... this.topleft.diff(int, int) ... -- Position ...
   * this.topleft.nextStart() ... -- Position ... this.topleft.sameX(Position) ...
   * -- boolean ... this.topleft.sameY(Position) ... -- boolean ...
   * this.topleft.xPosition() ... -- int ... this.topleft.yPosition() ... -- int
   * ... this.topleft.maxX(Position) ... -- int ... this.topleft.maxY(Position)
   * ... -- int ... this.topleft.minX(Position) ... -- int ...
   * this.topleft.minY(Position) ... -- int
   * 
   */

  // overlays this vehicle with a white outline onto the given image
  // in its position
  public WorldImage toImage(int tileHeight) {
    /*
     * TEMPLATE Parameters: ... tileHeight ... -- int Methods on Parameters:
     */
    WorldImage thisImage = new OverlayImage(
        new RectangleImage(this.length * tileHeight, tileHeight, OutlineMode.OUTLINE, Color.WHITE),
        new RectangleImage(this.length * tileHeight, tileHeight, OutlineMode.SOLID, this.color));

    return thisImage;

  }

  // returns a new vehicle with its color set as yellow
  // (to indicate it as being selected)
  public IVehicle asSelected() {
    return new HVehicle(this.topleft, this.length, Color.YELLOW, this.isTarget);
  }

  // determines whether this IVehicle is overlapping with other IVehicle
  public boolean isOverlapping(IVehicle other) {
    /*
     * TEMPLATE Parameters: ... other ... -- IVehicle Methods on Parameters: ...
     * other.isOverlapping(IVehicle) ... -- boolean ...
     * other.isOverlappingWithSegment(Position, Position) ... -- boolean ...
     * other.isOverlappingWithPoint(Position) ... -- boolean ... other.asSelected()
     * ... -- IVehicle ... other.addToImage(int, WorldImage) ... -- WorldImage ...
     * other.toImage(int) ... -- WorldImage ... other.shouldBeTarget(Position) ...
     * -- boolean ... other.target() ... -- boolean ...
     * other.setTargetFromExitPos(Position) ... -- IVehicle
     */
    return other.isOverlappingWithSegment(this.topleft, this.topleft.diff(this.length - 1, 0));
  }

  // determines if the vehicle overlaps with a given line segment
  // WARNING: undefined and undefined behavior if the segment connecting a&b
  // is not a vertical or horizontal line
  public boolean isOverlappingWithSegment(Position a, Position b) {
    /*
     * TEMPLATE Parameters: ... a ... -- Position ... b ... -- Position Methods on
     * Parameters: ... a.diff(int, int) ... -- Position ... a.nextStart() ... --
     * Position ... a.sameX(Position) ... -- boolean ... a.sameY(Position) ... --
     * boolean ... a.xPosition() ... -- int ... a.yPosition() ... -- int ...
     * a.maxX(Position) ... -- int ... a.maxY(Position) ... -- int ...
     * a.minX(Position) ... -- int ... a.minY(Position) ... -- int ... b.diff(int,
     * int) ... -- Position ... b.nextStart() ... -- Position ... b.sameX(Position)
     * ... -- boolean ... b.sameY(Position) ... -- boolean ... b.xPosition() ... --
     * int ... b.yPosition() ... -- int ... b.maxX(Position) ... -- int ...
     * b.maxY(Position) ... -- int ... b.minX(Position) ... -- int ...
     * b.minY(Position) ... -- int
     * 
     */
    return new Utils().overlapping(this.topleft, this.topleft.diff(this.length - 1, 0), a, b);
  }

  public IVehicle setTargetFromExitPos(Position exit) {
    /*
     * TEMPLATE Parameters: ... exit ... -- Position Methods on Parameters: ...
     * exit.diff(int, int) ... -- Position ... exit.nextStart() ... -- Position ...
     * exit.sameX(Position) ... -- boolean ... exit.sameY(Position) ... -- boolean
     * ... exit.xPosition() ... -- int ... exit.yPosition() ... -- int ...
     * exit.maxX(Position) ... -- int ... exit.maxY(Position) ... -- int ...
     * exit.minX(Position) ... -- int ... exit.minY(Position) ... -- int
     */
    if (this.shouldBeTarget(exit)) {
      return new HVehicle(this.topleft, this.length, Color.RED, true);
    }
    return this;
  }

  public boolean shouldBeTarget(Position exit) {
    /*
     * TEMPLATE Parameters: ... exit ... -- Position Methods on Parameters: ...
     * exit.diff(int, int) ... -- Position ... exit.nextStart() ... -- Position ...
     * exit.sameX(Position) ... -- boolean ... exit.sameY(Position) ... -- boolean
     * ... exit.xPosition() ... -- int ... exit.yPosition() ... -- int ...
     * exit.maxX(Position) ... -- int ... exit.maxY(Position) ... -- int ...
     * exit.minX(Position) ... -- int ... exit.minY(Position) ... -- int
     */
    return this.topleft.sameY(exit) && this.length == 2;
  }

}

// a vehicle that moves in the y direction
class VVehicle extends AVehicle {
  VVehicle(Position topleft, int length, Color color, boolean isTarget) {
    super(topleft, length, color, isTarget);
  }

  VVehicle(Position topleft, int length) {
    super(topleft, length);
  }

  /*
   * TEMPLATE
   * 
   * Constants: ... COLOROPTIONS ... -- IList<Color>
   * 
   * Fields: ... this.topleft ... -- Position ... this.length ... -- int ...
   * this.color ... -- Color ... this.isTarget ... -- boolean
   * 
   * Methods: ... this.isOverlapping(IVehicle) ... -- boolean ...
   * this.isOverlappingWithSegment(Position, Position) ... -- boolean ...
   * this.isOverlappingWithPoint(Position) ... -- boolean ... this.asSelected()
   * ... -- IVehicle ... this.addToImage(int, WorldImage) ... -- WorldImage ...
   * this.toImage(int) ... -- WorldImage ... this.shouldBeTarget(Position) ... --
   * boolean ... this.target() ... -- boolean ...
   * this.setTargetFromExitPos(Position) ... -- IVehicle
   * 
   * Methods on constants: ... COLOROPTIONS.map(Function<Color, R>) ... --
   * IList<Color> ... COLOROPTIONS.orMap(Function<Color, Boolean>) ... -- Boolean
   * ... COLOROPTIONS.count(Function<Color, Boolean>) ... -- Integer ...
   * COLOROPTIONS.foldL(BiFunction<Color, R, R>, R) ... -- R ...
   * COLOROPTIONS.length() ... -- Integer ... COLOROPTIONS.getIndex(int) ... --
   * Color ... COLOROPTIONS.find(Function<Color, Boolean>) ... -- int ...
   * COLOROPTIONS.findIndex(Function<Color, Boolean>, int) ... -- int Methods on
   * fields: ... this.topleft.diff(int, int) ... -- Position ...
   * this.topleft.nextStart() ... -- Position ... this.topleft.sameX(Position) ...
   * -- boolean ... this.topleft.sameY(Position) ... -- boolean ...
   * this.topleft.xPosition() ... -- int ... this.topleft.yPosition() ... -- int
   * ... this.topleft.maxX(Position) ... -- int ... this.topleft.maxY(Position)
   * ... -- int ... this.topleft.minX(Position) ... -- int ...
   * this.topleft.minY(Position) ... -- int
   * 
   */

  // overlays this vehicle with a white outline onto the given image
  // in its position
  public WorldImage toImage(int tileHeight) {
    /*
     * TEMPLATE Parameters: ... tileHeight ... -- int Methods on Parameters:
     */
    WorldImage thisImage = new OverlayImage(
        new RectangleImage(tileHeight, tileHeight * this.length, OutlineMode.OUTLINE, Color.WHITE),
        new RectangleImage(tileHeight, tileHeight * this.length, // the color part of the car
            OutlineMode.SOLID, this.color));

    return thisImage;

  }

  // returns a new vehicle with its color set as yellow
  // (to indicate it as being selected)
  public IVehicle asSelected() {
    return new VVehicle(this.topleft, this.length, Color.YELLOW, this.isTarget);
  }

  // determines whether this IVehicle is overlapping with other IVehicle
  public boolean isOverlapping(IVehicle other) {
    /*
     * TEMPLATE Parameters: ... other ... -- IVehicle Methods on Parameters: ...
     * other.isOverlapping(IVehicle) ... -- boolean ...
     * other.isOverlappingWithSegment(Position, Position) ... -- boolean ...
     * other.isOverlappingWithPoint(Position) ... -- boolean ... other.asSelected()
     * ... -- IVehicle ... other.addToImage(int, WorldImage) ... -- WorldImage ...
     * other.toImage(int) ... -- WorldImage ... other.shouldBeTarget(Position) ...
     * -- boolean ... other.target() ... -- boolean ...
     * other.setTargetFromExitPos(Position) ... -- IVehicle
     */
    return other.isOverlappingWithSegment(this.topleft, this.topleft.diff(0, this.length - 1));
  }

  // determines if the vehicle overlaps with a given line segment
  // WARNING: undefined and undefined behavior if the segment connecting a&b
  // is not a vertical or horizontal line
  public boolean isOverlappingWithSegment(Position a, Position b) {
    /*
     * TEMPLATE Parameters: ... a ... -- Position ... b ... -- Position Methods on
     * Parameters: ... a.diff(int, int) ... -- Position ... a.nextStart() ... --
     * Position ... a.sameX(Position) ... -- boolean ... a.sameY(Position) ... --
     * boolean ... a.xPosition() ... -- int ... a.yPosition() ... -- int ...
     * a.maxX(Position) ... -- int ... a.maxY(Position) ... -- int ...
     * a.minX(Position) ... -- int ... a.minY(Position) ... -- int ... b.diff(int,
     * int) ... -- Position ... b.nextStart() ... -- Position ... b.sameX(Position)
     * ... -- boolean ... b.sameY(Position) ... -- boolean ... b.xPosition() ... --
     * int ... b.yPosition() ... -- int ... b.maxX(Position) ... -- int ...
     * b.maxY(Position) ... -- int ... b.minX(Position) ... -- int ...
     * b.minY(Position) ... -- int
     * 
     */
    return new Utils().overlapping(this.topleft, this.topleft.diff(0, this.length - 1), a, b);
  }

  public IVehicle setTargetFromExitPos(Position exit) {
    /*
     * TEMPLATE Parameters: ... exit ... -- Position Methods on Parameters: ...
     * exit.diff(int, int) ... -- Position ... exit.nextStart() ... -- Position ...
     * exit.sameX(Position) ... -- boolean ... exit.sameY(Position) ... -- boolean
     * ... exit.xPosition() ... -- int ... exit.yPosition() ... -- int ...
     * exit.maxX(Position) ... -- int ... exit.maxY(Position) ... -- int ...
     * exit.minX(Position) ... -- int ... exit.minY(Position) ... -- int
     */
    if (this.shouldBeTarget(exit)) {
      return new VVehicle(this.topleft, this.length, Color.RED, true);
    }
    return this;
  }

  public boolean shouldBeTarget(Position exit) {
    /*
     * TEMPLATE Parameters: ... exit ... -- Position Methods on Parameters: ...
     * exit.diff(int, int) ... -- Position ... exit.nextStart() ... -- Position ...
     * exit.sameX(Position) ... -- boolean ... exit.sameY(Position) ... -- boolean
     * ... exit.xPosition() ... -- int ... exit.yPosition() ... -- int ...
     * exit.maxX(Position) ... -- int ... exit.maxY(Position) ... -- int ...
     * exit.minX(Position) ... -- int ... exit.minY(Position) ... -- int
     */
    return this.topleft.sameX(exit) && this.length == 2;
  }
}

// stores information of a board string
// in a single pass
class ParseInformation {
  // the current position
  Position current;
  // the position of the exit
  // WARNING: can be null if the exit has not been encountered
  Position exit;
  // the accumulated list of vehicles parsed
  IList<IVehicle> vehicles;

  /*
   * TEMPLATE for ParseInformation:
   * 
   * Fields: ... this.current ... --Position ... this.exit ... --Position ...
   * this.vehicles ... --IList<Vehicle>
   * 
   * Methods: ... this.nextTile() ... -- ParseInformation ... this.nextLine() ...
   * -- ParseInformation ... this.addVTruck() ... -- ParseInformation ...
   * this.addHTruck() ... -- ParseInformation ... this.addVCar() ... --
   * ParseInformation ... this.addHCar() ... -- ParseInformation ...
   * this.addExit() ... -- ParseInformation ... this.toBoard(int, int) ... --
   * Board
   * 
   * Methods on fields: ... this.current.diff(int, int) ... -- Position ...
   * this.current.nextStart() ... -- Position ... this.current.sameX(Position) ...
   * -- boolean ... this.current.sameY(Position) ... -- boolean ...
   * this.current.xPosition() ... -- int ... this.current.yPosition() ... -- int
   * ... this.current.maxX(Position) ... -- int ... this.current.maxY(Position)
   * ... -- int ... this.current.minX(Position) ... -- int ...
   * this.current.minY(Position) ... -- int ... this.exit.diff(int, int) ... --
   * Position ... this.exit.nextStart() ... -- Position ...
   * this.exit.sameX(Position) ... -- boolean ... this.exit.sameY(Position) ... --
   * boolean ... this.exit.xPosition() ... -- int ... this.exit.yPosition() ... --
   * int ... this.exit.maxX(Position) ... -- int ... this.exit.maxY(Position) ...
   * -- int ... this.exit.minX(Position) ... -- int ... this.exit.minY(Position)
   * ... -- int ... this.vehicles.map(Function<Vehicle, R>) ... -- IList<Vehicle>
   * ... this.vehicles.orMap(Function<Vehicle, Boolean>) ... -- Boolean ...
   * this.vehicles.count(Function<Vehicle, Boolean>) ... -- Integer ...
   * this.vehicles.foldL(BiFunction<Vehicle, R, R>, R) ... -- R ...
   * this.vehicles.length() ... -- Integer ... this.vehicles.getIndex(int) ... --
   * Vehicle ... this.vehicles.find(Function<Vehicle, Boolean>) ... -- int ...
   * this.vehicles.findIndex(Function<Vehicle, Boolean>, int) ... -- int
   * 
   */

  ParseInformation(Position current, Position exit, IList<IVehicle> vehicles) {
    this.current = current;
    this.exit = exit;
    this.vehicles = vehicles;
  }

  // shorthand constructore for the initial state
  ParseInformation() {
    this(new Position(-1, -1), null, new MtList<IVehicle>());
  }

  // move a single tile forwards
  public ParseInformation nextTile() {
    return new ParseInformation(this.current.diff(1, 0), this.exit, this.vehicles);
  }

  // move a single line downwards
  public ParseInformation nextLine() {
    return new ParseInformation(this.current.nextStart(), this.exit, this.vehicles);
  }

  // add a vertical truck to the list of vehicles at the current position
  public ParseInformation addVTruck() { // uppercase T
    return new ParseInformation(this.current, this.exit,
        new ConsList<IVehicle>(new VVehicle(this.current, 3), this.vehicles));
  }

  // add a horizontal truck to the list of vehicles at the current position
  public ParseInformation addHTruck() { // lowercase T
    return new ParseInformation(this.current, this.exit,
        new ConsList<IVehicle>(new HVehicle(this.current, 3), this.vehicles));
  }

  // add a vertical car to the list of vehicles at the current position
  public ParseInformation addVCar() { // uppercase C
    return new ParseInformation(this.current, this.exit,
        new ConsList<IVehicle>(new VVehicle(this.current, 2), this.vehicles));
  }

  // add a horizontal car to the list of vehicles at the current position
  public ParseInformation addHCar() { // lowercase c
    return new ParseInformation(this.current, this.exit,
        new ConsList<IVehicle>(new HVehicle(this.current, 2), this.vehicles));
  }

  // add the exit to the list of vehicles at the current position
  public ParseInformation addExit() { // uppercase X
    if (this.exit != null) {
      throw new IllegalArgumentException("multiple exits in a board string representation.");
    }
    return new ParseInformation(this.current, this.current, this.vehicles);
  }

  // converts this ParseInformation to a Board
  // with the given width and height
  public Board toBoard(int width, int height) {
    /*
     * TEMPLATE Parameters: ... width ... -- int ... height ... -- int Methods on
     * Parameters:
     */
    IList<IVehicle> vehiclesWithTarget = this.vehicles.map(new UpdateCarWithTarget(this.exit));
    int targetCount = vehiclesWithTarget.count(new IsTargetCar(this.exit));
    if (targetCount != 1) {
      throw new RuntimeException("Expect a single target car, got " + targetCount + " instead");
    }
    if (this.exit == null) {
      throw new RuntimeException("No exit found in the board string representation.");
    }
    return new Board(vehiclesWithTarget, width, height, this.exit, -1);
  }
}

// a function that determines if the vehicle is the target
class IsTargetCar implements Function<IVehicle, Boolean> {
  // the position of the exit
  Position exit;

  IsTargetCar(Position exit) {
    this.exit = exit;
  }

  /*
   * TEMPLATE
   * 
   * Fields: ... this.exit ... -- Position
   * 
   * Methods: ... this.apply(IVehicle) ... -- Boolean
   * 
   * Methods on fields: ... this.exit.diff(int, int) ... -- Position ...
   * this.exit.nextStart() ... -- Position ... this.exit.sameX(Position) ... --
   * boolean ... this.exit.sameY(Position) ... -- boolean ...
   * this.exit.xPosition() ... -- int ... this.exit.yPosition() ... -- int ...
   * this.exit.maxX(Position) ... -- int ... this.exit.maxY(Position) ... -- int
   * ... this.exit.minX(Position) ... -- int ... this.exit.minY(Position) ... --
   * int
   */

  // determines if the vehicle is the target
  // e.g. the car that is perpendicular to the exit
  public Boolean apply(IVehicle v) {
    /*
     * TEMPLATE Parameters: ... v ... -- IVehicle Methods on Parameters: ...
     * v.isOverlapping(IVehicle) ... -- boolean ...
     * v.isOverlappingWithSegment(Position, Position) ... -- boolean ...
     * v.isOverlappingWithPoint(Position) ... -- boolean ... v.asSelected() ... --
     * IVehicle ... v.addToImage(int, WorldImage) ... -- WorldImage ...
     * v.toImage(int) ... -- WorldImage ... v.shouldBeTarget(Position) ... --
     * boolean ... v.target() ... -- boolean ... v.setTargetFromExitPos(Position)
     * ... -- IVehicle
     */
    return v.shouldBeTarget(this.exit);
  }
}

// a function that updates the vehicle with the target
class UpdateCarWithTarget implements Function<IVehicle, IVehicle> {
  // the position of the exit
  Position exit;

  UpdateCarWithTarget(Position exit) {
    this.exit = exit;
  }

  /*
   * TEMPLATE
   * 
   * Fields: ... this.exit ... -- Position
   * 
   * Methods: ... this.apply(IVehicle) ... -- IVehicle
   * 
   * Methods on fields: ... this.exit.diff(int, int) ... -- Position ...
   * this.exit.nextStart() ... -- Position ... this.exit.sameX(Position) ... --
   * boolean ... this.exit.sameY(Position) ... -- boolean ...
   * this.exit.xPosition() ... -- int ... this.exit.yPosition() ... -- int ...
   * this.exit.maxX(Position) ... -- int ... this.exit.maxY(Position) ... -- int
   * ... this.exit.minX(Position) ... -- int ... this.exit.minY(Position) ... --
   * int
   */

  // sets the vehicle as target if it should be
  public IVehicle apply(IVehicle v) {
    /*
     * TEMPLATE Parameters: ... v ... -- IVehicle Methods on Parameters: ...
     * v.isOverlapping(IVehicle) ... -- boolean ...
     * v.isOverlappingWithSegment(Position, Position) ... -- boolean ...
     * v.isOverlappingWithPoint(Position) ... -- boolean ... v.asSelected() ... --
     * IVehicle ... v.addToImage(int, WorldImage) ... -- WorldImage ...
     * v.toImage(int) ... -- WorldImage ... v.shouldBeTarget(Position) ... --
     * boolean ... v.target() ... -- boolean ... v.setTargetFromExitPos(Position)
     * ... -- IVehicle
     */
    return v.setTargetFromExitPos(this.exit);
  }

}

/*
 * An example of a board string representation: **appears out of nowhere**
 * **exists**
 * 
 * String boardStr = "+------+\n" + "|      |\n" + "|  C T |\n" + "|c    CX\n" +
 * "|t     |\n" + "|CCC c |\n" + "|    c |\n" + "+------+";
 */

// parses a single character string; updates parseinformation correspondingly
// meant to be used in a IList.foldL setting.
class Parser implements BiFunction<String, ParseInformation, ParseInformation> {
  Parser() {
  }

  /*
   * TEMPLATE
   * 
   * Fields:
   * 
   * Methods: ... this.apply(String, ParseInformation) ... -- ParseInformation
   * 
   * Methods on fields:
   */

  /*
   * increment currentX for all valid characters reset currentX and increment
   * currentY at a newLine char create Vehicles when appropriate create the Exit
   * when appropriate
   */
  public ParseInformation apply(String boardStr, ParseInformation info) {
    /*
     * TEMPLATE Parameters: ... boardStr ... -- String ... info ... --
     * ParseInformation Methods on Parameters: ... info.nextTile() ... --
     * ParseInformation ... info.nextLine() ... -- ParseInformation ...
     * info.addVTruck() ... -- ParseInformation ... info.addHTruck() ... --
     * ParseInformation ... info.addVCar() ... -- ParseInformation ...
     * info.addHCar() ... -- ParseInformation ... info.addExit() ... --
     * ParseInformation ... info.toBoard(int, int) ... -- Board
     */

    if (boardStr.equals("T")) {
      // a vertical truck
      return info.addVTruck().nextTile();
    }
    else if (boardStr.equals("t")) {
      // a horizontal truck
      return info.addHTruck().nextTile();
    }
    else if (boardStr.equals("C")) {
      // a vertical car
      return info.addVCar().nextTile();
    }
    else if (boardStr.equals("c")) {
      // a horizontal car
      return info.addHCar().nextTile();
    }
    else if (boardStr.equals(" ") || boardStr.equals("-") || boardStr.equals("|")
        || boardStr.equals("+")) {
      // tiles that don't matter except counting the current position
      return info.nextTile();
    }
    else if (boardStr.equals("\n")) {
      // new line - reset currentX and increment currentY
      return info.nextLine();
    }
    else if (boardStr.equals("X")) {
      // the exit
      return info.addExit().nextTile();
    }
    else {
      // illegal character
      throw new IllegalArgumentException("Board contains illegal character \"" + boardStr + "\"");
    }
  }
}

// a class that encapsulates helper methods for the game
class Utils {

  /*
   * TEMPLATE:
   * 
   * Fields:
   * 
   * Methods: ... this.listFromString(String) ... -- IList<String> ...
   * this.initializeFromString(String s) ... -- Board ...
   * this.overlapping(Position, Position, Position, Position) ... -- boolean
   * 
   * Methods on fields:
   * 
   */

  // creates a list of the individual characters in the given string
  public IList<String> listFromString(String str) {
    /*
     * TEMPLATE Parameters: ... str ... -- String Methods on Parameters:
     */
    if (str.length() == 0) {
      return new MtList<String>();
    }
    return new ConsList<String>(str.substring(0, 1), this.listFromString(str.substring(1)));
  }

  // creates a board based on a string representation of the board
  // infers the only car in alignment with the exit as the target
  public Board initializeFromString(String s) {
    /*
     * TEMPLATE Parameters: ... s ... -- String Methods on Parameters:
     */

    // a list of the individual characters in the string
    IList<String> boardStr = this.listFromString(s);
    // determines the width of the board
    // based on number of - chars
    // add 1 to compensate for the X. that way, no matter whether it's in that line
    // or not,
    // it will be truncated
    int width = (boardStr.count(new StringEqual("-")) + 1) / 2;
    // determines the width of the board
    // based on number of | chars
    int height = (boardStr.count(new StringEqual("|")) + 1) / 2;

    // parse the board
    ParseInformation info = boardStr.foldL(new Parser(), new ParseInformation());

    // convert the parse information to a board
    return info.toBoard(width, height);
  }

  // determines if two segments of positions overlap with each other
  // where a1 and b1 are the first segment, and a2 and b2 are the second
  // the segments should be parallel to the x or y axis
  public boolean overlapping(Position a1, Position b1, Position a2, Position b2) {
    /*
     * TEMPLATE Parameters: ... a1 ... -- Position ... b1 ... -- Position ... a2 ...
     * -- Position ... b2 ... -- Position Methods on Parameters: ... a1.diff(int,
     * int) ... -- Position ... a1.nextStart() ... -- Position ...
     * a1.sameX(Position) ... -- boolean ... a1.sameY(Position) ... -- boolean ...
     * a1.xPosition() ... -- int ... a1.yPosition() ... -- int ... a1.maxX(Position)
     * ... -- int ... a1.maxY(Position) ... -- int ... a1.minX(Position) ... -- int
     * ... a1.minY(Position) ... -- int
     * 
     * ... b1.diff(int, int) ... -- Position ... b1.nextStart() ... -- Position ...
     * b1.sameX(Position) ... -- boolean ... b1.sameY(Position) ... -- boolean ...
     * b1.xPosition() ... -- int ... b1.yPosition() ... -- int ... b1.maxX(Position)
     * ... -- int ... b1.maxY(Position) ... -- int ... b1.minX(Position) ... -- int
     * ... b1.minY(Position) ... -- int
     * 
     * ... a2.diff(int, int) ... -- Position ... a2.nextStart() ... -- Position ...
     * a2.sameX(Position) ... -- boolean ... a2.sameY(Position) ... -- boolean ...
     * a2.xPosition() ... -- int ... a2.yPosition() ... -- int ... a2.maxX(Position)
     * ... -- int ... a2.maxY(Position) ... -- int ... a2.minX(Position) ... -- int
     * ... a2.minY(Position) ... -- int
     * 
     * ... b2.diff(int, int) ... -- Position ... b2.nextStart() ... -- Position ...
     * b2.sameX(Position) ... -- boolean ... b2.sameY(Position) ... -- boolean ...
     * b2.xPosition() ... -- int ... b2.yPosition() ... -- int ... b2.maxX(Position)
     * ... -- int ... b2.maxY(Position) ... -- int ... b2.minX(Position) ... -- int
     * ... b2.minY(Position) ... -- int
     */
    return a1.maxX(a2) <= b1.minX(b2) && a1.maxY(a2) <= b1.minY(b2);
  }
}

// a predicate that determines if two strings are equal
class StringEqual implements Function<String, Boolean> {
  // the string we are setting as the base for comparison
  String str;

  StringEqual(String str) {
    this.str = str;
  }

  /*
   * TEMPLATE
   * 
   * Fields: ... this.str ... -- String
   * 
   * Methods: ... this.apply(String) ... -- Boolean
   * 
   * Methods on fields:
   */

  // determines if the given string is equal to the string in this class
  public Boolean apply(String other) {
    /*
     * TEMPLATE Parameters: ... other ... -- String Methods on Parameters:
     */
    return this.str.equals(other);
  }
}

// the main world class for the Rush Hour game
class ThisWorld extends javalib.funworld.World {
  // the board
  Board board;
  // the pixel height of a single tile
  int tileHeight;

  ThisWorld(Board board, int tileHeight) {
    this.board = board;
    this.tileHeight = tileHeight;
  }

  // shorthand constructor for a world with a default tile height 32
  ThisWorld(Board board) {
    this.board = board;
    this.tileHeight = 32;
  }

  ThisWorld() {
    String demoLevel = "+------+\n" + "|      |\n" + "|  C T |\n" + "|c    CX\n" + "|t     |\n"
        + "|CCC c |\n" + "|    c |\n" + "+------+";

    this.board = new Utils().initializeFromString(demoLevel);
    this.tileHeight = 32;
  }

  /*
   * TEMPLATE
   * 
   * Fields: ... this.board ... -- Board ... this.tileHeight ... -- int
   * 
   * Methods: ... this.makeScene() ... -- WorldScene ...
   * this.onMouseReleased(Posn) ... -- ThisWorld
   * 
   * Methods on fields: ... this.board.vehicles ... -- IList<IVehicle> ...
   * this.board.width ... -- int ... this.board.height ... -- int ...
   * this.board.exit ... -- Position ... this.board.selected ... -- int
   * 
   */

  // draws a single frame of the game
  public WorldScene makeScene() {
    WorldImage boardImg = board.toImage(this.tileHeight);
    double width = boardImg.getWidth();
    double height = boardImg.getHeight();

    // places the board image in the center of the scene
    WorldScene s = new WorldScene((int) width, (int) height).placeImageXY(boardImg,
        (int) (width / 2), (int) (height / 2));

    return s;
  }

  // handles the mouse released event
  public ThisWorld onMouseReleased(Posn pos) {
    /*
     * TEMPLATE Parameters: ... pos ... -- Posn Methods on Parameters:
     */
    Position clickedTile = new Position((int) Math.floor(pos.x / this.tileHeight) - 1,
        (int) Math.floor(pos.y / this.tileHeight) - 1);

    return new ThisWorld(board.selectTile(clickedTile));
  }
}

class ExamplesBoard {

  // IVEHICLE TESTS // FINISHED

  boolean testAddToImage(Tester t) {
    IVehicle a = new HVehicle(new Position(2, 3), 3, Color.GREEN, false);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.RED, true);
    WorldImage base = new RectangleImage(500, 500, OutlineMode.OUTLINE, Color.WHITE);

    return t.checkExpect(a.addToImage(50, base),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, a.toImage(50),
            (double) (2 * 50 * -1), (double) (3 * 50 * -1), base))
        && t.checkExpect(b.addToImage(30, base),
            new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, b.toImage(30),
                (double) (3 * 30 * -1), (double) (3 * 30 * -1), base))
        && t.checkExpect(c.addToImage(11, base), new OverlayOffsetAlign(AlignModeX.LEFT,
            AlignModeY.TOP, c.toImage(11), (double) (1 * 11 * -1), (double) (2 * 11 * -1), base));
  }

  boolean testIsOverlapping(Tester t) {

    IVehicle a = new HVehicle(new Position(2, 3), 3);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.RED, true);

    return t.checkExpect(a.isOverlapping(new HVehicle(new Position(2, 3), 3)), true)
        && t.checkExpect(a.isOverlapping(new VVehicle(new Position(1, 3), 2)), false)
        && t.checkExpect(a.isOverlapping(new HVehicle(new Position(3, 3), 2)), true)
        && t.checkExpect(a.isOverlapping(new VVehicle(new Position(2, 2), 2)), true)
        && t.checkExpect(c.isOverlapping(new HVehicle(new Position(0, 0), 3)), false)
        && t.checkExpect(b.isOverlapping(new VVehicle(new Position(3, 4), 2)), true)
        && t.checkExpect(b.isOverlapping(new VVehicle(new Position(2, 4), 3)), false)
        && t.checkExpect(b.isOverlapping(new HVehicle(new Position(3, 2), 2)), false);
  }

  boolean testIsOverlappingWithPoint(Tester t) {

    IVehicle a = new HVehicle(new Position(2, 3), 3);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.RED, true);

    return t.checkExpect(a.isOverlappingWithPoint(new Position(2, 3)), true)
        && t.checkExpect(a.isOverlappingWithPoint(new Position(1, 3)), false)
        && t.checkExpect(a.isOverlappingWithPoint(new Position(3, 3)), true)
        && t.checkExpect(a.isOverlappingWithPoint(new Position(2, 2)), false)
        && t.checkExpect(c.isOverlappingWithPoint(new Position(0, 0)), false)
        && t.checkExpect(b.isOverlappingWithPoint(new Position(3, 4)), true)
        && t.checkExpect(b.isOverlappingWithPoint(new Position(2, 4)), false)
        && t.checkExpect(b.isOverlappingWithPoint(new Position(3, 2)), false);
  }

  boolean testIsOverlappingWithSegment(Tester t) {

    IVehicle a = new HVehicle(new Position(2, 3), 3);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.RED, true);

    return t.checkExpect(a.isOverlappingWithSegment(new Position(2, 3), new Position(2, 3)), true)
        && t.checkExpect(a.isOverlappingWithSegment(new Position(1, 3), new Position(1, 3)), false)
        && t.checkExpect(a.isOverlappingWithSegment(new Position(3, 3), new Position(3, 3)), true)
        && t.checkExpect(a.isOverlappingWithSegment(new Position(2, 2), new Position(2, 2)), false)
        && t.checkExpect(c.isOverlappingWithSegment(new Position(0, 0), new Position(4, 2)), true)
        && t.checkExpect(b.isOverlappingWithSegment(new Position(3, 4), new Position(3, 5)), true)
        && t.checkExpect(b.isOverlappingWithSegment(new Position(2, 4), new Position(3, 2)), false)
        && t.checkExpect(b.isOverlappingWithSegment(new Position(3, 2), new Position(1, 1)), false);
  }

  boolean testTarget(Tester t) {
    IVehicle a = new HVehicle(new Position(2, 3), 3);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.RED, true);
    IVehicle d = new HVehicle(new Position(1, 0), 2, Color.RED, true);

    return t.checkExpect(a.target(), false) && t.checkExpect(b.target(), false)
        && t.checkExpect(c.target(), true) && t.checkExpect(d.target(), true);
  }

  boolean testToImageVehicle(Tester t) {
    IVehicle a = new HVehicle(new Position(2, 3), 3, Color.GREEN, false);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.RED, true);

    return t.checkExpect(a.toImage(50),
        new OverlayImage(new RectangleImage(150, 50, OutlineMode.OUTLINE, Color.WHITE),
            new RectangleImage(150, 50, OutlineMode.SOLID, Color.GREEN)))
        && t.checkExpect(b.toImage(50),
            new OverlayImage(new RectangleImage(50, 100, OutlineMode.OUTLINE, Color.WHITE),
                new RectangleImage(50, 100, OutlineMode.SOLID, Color.BLUE)))
        && t.checkExpect(c.toImage(30),
            new OverlayImage(new RectangleImage(60, 30, OutlineMode.OUTLINE, Color.WHITE),
                new RectangleImage(60, 30, OutlineMode.SOLID, Color.RED)));
  }

  boolean testAsSelected(Tester t) {
    IVehicle a = new HVehicle(new Position(2, 3), 3, Color.GREEN, false);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.RED, true);

    return t.checkExpect(a.asSelected().toImage(50),
        new OverlayImage(new RectangleImage(150, 50, OutlineMode.OUTLINE, Color.WHITE),
            new RectangleImage(150, 50, OutlineMode.SOLID, Color.YELLOW)))
        && t.checkExpect(b.asSelected(), new VVehicle(new Position(3, 3), 2, Color.YELLOW, false))
        && t.checkExpect(c.asSelected(), new HVehicle(new Position(1, 2), 2, Color.YELLOW, true));
  }

  boolean testShouldBeTarget(Tester t) {
    IVehicle a = new HVehicle(new Position(2, 3), 2);
    IVehicle aBig = new HVehicle(new Position(2, 3), 3);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.GREEN, false);
    IVehicle d = new VVehicle(new Position(1, 0), 2, Color.ORANGE, false);

    return t.checkExpect(a.shouldBeTarget(new Position(-1, 3)), true)
        && t.checkExpect(aBig.shouldBeTarget(new Position(-1, 3)), false)
        && t.checkExpect(b.shouldBeTarget(new Position(10, 10)), false)
        && t.checkExpect(c.shouldBeTarget(new Position(1, 1)), false)
        && t.checkExpect(d.shouldBeTarget(new Position(1, -1)), true);
  }

  boolean testShouldBeTargetFromExitPosition(Tester t) {
    IVehicle a = new HVehicle(new Position(2, 3), 2);
    IVehicle aBig = new HVehicle(new Position(2, 3), 3, Color.BLUE, false);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.GREEN, false);
    IVehicle d = new VVehicle(new Position(1, 0), 2, Color.ORANGE, false);

    return t.checkExpect(a.setTargetFromExitPos(new Position(-1, 3)),
        new HVehicle(new Position(2, 3), 2, Color.RED, true))
        && t.checkExpect(aBig.setTargetFromExitPos(new Position(-1, 3)), aBig)
        && t.checkExpect(b.setTargetFromExitPos(new Position(10, 10)), b)
        && t.checkExpect(c.setTargetFromExitPos(new Position(1, 1)), c)
        && t.checkExpect(d.setTargetFromExitPos(new Position(1, -1)),
            new VVehicle(new Position(1, 0), 2, Color.RED, true));
  }

  // BOARD TESTS
  boolean testGameWon(Tester t) {
    IVehicle a = new HVehicle(new Position(0, -1), 2, Color.RED, true);
    IVehicle aFalse = new HVehicle(new Position(0, -1), 2, Color.BLUE, false);
    IVehicle aBig = new HVehicle(new Position(2, 3), 3, Color.BLUE, false);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.GREEN, false);

    IList<IVehicle> one = new ConsList<IVehicle>(a, new ConsList<IVehicle>(b,
        new ConsList<IVehicle>(c, new ConsList<IVehicle>(c, new MtList<IVehicle>()))));
    IList<IVehicle> two = new ConsList<IVehicle>(c,
        new ConsList<IVehicle>(c, new MtList<IVehicle>()));
    IList<IVehicle> oneMod1 = new ConsList<IVehicle>(aBig, new ConsList<IVehicle>(b,
        new ConsList<IVehicle>(c, new ConsList<IVehicle>(c, new MtList<IVehicle>()))));
    IList<IVehicle> oneMod2 = new ConsList<IVehicle>(aFalse, new ConsList<IVehicle>(b,
        new ConsList<IVehicle>(c, new ConsList<IVehicle>(c, new MtList<IVehicle>()))));

    Board first = new Board(one, 12, 10, new Position(0, 0), -1);
    Board firstMod1 = new Board(oneMod1, 12, 10, new Position(0, 0), -1);
    Board firstMod2 = new Board(oneMod2, 12, 10, new Position(0, 0), -1);
    Board second = new Board(one, 10, 10, new Position(0, -1), 0);
    Board third = new Board(two, 10, 10, new Position(4, 5), 2);

    return t.checkExpect(first.gameWon(), false) && t.checkExpect(second.gameWon(), true)
        && t.checkExpect(third.gameWon(), false) && t.checkExpect(firstMod1.gameWon(), false)
        && t.checkExpect(firstMod2.gameWon(), false);
  }

  boolean testSelectTile(Tester t) {
    IVehicle a = new HVehicle(new Position(0, -1), 2, Color.RED, true);

    IVehicle aBig = new HVehicle(new Position(2, 3), 3, Color.BLUE, false);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.GREEN, false);

    IList<IVehicle> one = new ConsList<IVehicle>(a, new ConsList<IVehicle>(b,
        new ConsList<IVehicle>(c, new ConsList<IVehicle>(aBig, new MtList<IVehicle>()))));
    IList<IVehicle> two = new ConsList<IVehicle>(c,
        new ConsList<IVehicle>(c, new MtList<IVehicle>()));
    IList<IVehicle> oneMod1 = new ConsList<IVehicle>(aBig, new ConsList<IVehicle>(b,
        new ConsList<IVehicle>(c, new ConsList<IVehicle>(c, new MtList<IVehicle>()))));

    Board firstMod1 = new Board(oneMod1, 12, 10, new Position(0, 0), -1);
    Board second = new Board(one, 10, 10, new Position(0, -1), 0);
    Board third = new Board(two, 10, 10, new Position(4, 5), 2);

    return t.checkExpect(firstMod1.selectTile(new Position(2, 3)),
        new Board(oneMod1, 12, 10, new Position(0, 0), 0))
        && t.checkExpect(third.selectTile(new Position(3, 3)),
            new Board(two, 10, 10, new Position(4, 5), -1))
        && t.checkExpect(second.selectTile(new Position(1, 2)),
            new Board(one, 10, 10, new Position(0, -1), 2));
  }

  boolean testToImageBoard(Tester t) {

    String board2 = "+---+\n" + "|  T|\n" + "|c  X\n" + "|   |\n" + "+---+";
    int tileHeight = 50;

    int width = 3;
    int height = 3;
    Position exit = new Position(3, 1);
    IList<IVehicle> v = new ConsList<IVehicle>(new HVehicle(new Position(0, 1), 2, Color.RED, true),
        new ConsList<IVehicle>(new VVehicle(new Position(2, 0), 3, Color.ORANGE, false),
            new MtList<IVehicle>()));

    Board b = new Board(v, width, height, exit, -1); // board 2

    // the grey border around the board
    WorldImage border = new RectangleImage(tileHeight * (width + 2), tileHeight * (height + 2),
        OutlineMode.SOLID, Color.GRAY);

    // the base image that contains the vehicles within the border
    // NOTE: has a white outline to align with the outlines of the vehicles
    WorldImage base = new OverlayImage(
        new RectangleImage(tileHeight * width, tileHeight * height, OutlineMode.OUTLINE,
            Color.WHITE),
        new RectangleImage(tileHeight * width, tileHeight * height, OutlineMode.SOLID,
            Color.WHITE));

    // overlays all of the vehicles onto the white base
    WorldImage withVehicles = v.foldL(new DrawVehicleOnto(tileHeight), base);

    // the image with the grey block border
    WorldImage withBorder = new OverlayImage(withVehicles, border);

    // the exit white square overlay to indicate the exit
    WorldImage exitImage = new RectangleImage(tileHeight, tileHeight, OutlineMode.SOLID,
        Color.WHITE);

    // the image with the white square overlay
    WorldImage withExit = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, exitImage,
        (double) ((exit.xPosition() + 1) * tileHeight * -1),
        (double) ((exit.yPosition() + 1) * tileHeight * -1), withBorder);

    return t.checkExpect(b.toImage(tileHeight), withExit);
  }

  // LIST TESTS // FINISHED
  boolean testFoldL(Tester t) {
    String board1 = "+---+\n" + "|   |\n" + "|   |\n" + "|   X\n" + "+---+";
    String board2 = "+---+\n" + "|   |\n" + "|c  |\n" + "|   X\n" + "+---+";
    String board3 = "+---+\n" + "|  T|\n" + "|c  |\n" + "|   X\n" + "+---+";

    return t.checkExpect(
        new Utils().listFromString(board1).foldL(new Parser(), new ParseInformation()),
        new ParseInformation(new Position(4, 3), new Position(3, 2), new MtList<IVehicle>()))
        && t.checkExpect(
            new Utils().listFromString(board2).foldL(new Parser(), new ParseInformation()),
            new ParseInformation(new Position(4, 3), new Position(3, 2),
                new ConsList<IVehicle>(new HVehicle(new Position(0, 1), 2, Color.CYAN, false),
                    new MtList<IVehicle>())))
        && t.checkExpect(
            new Utils().listFromString(board3).foldL(new Parser(), new ParseInformation()),
            new ParseInformation(new Position(4, 3), new Position(3, 2),
                new ConsList<IVehicle>(new HVehicle(new Position(0, 1), 2, Color.CYAN, false),
                    new ConsList<IVehicle>(new VVehicle(new Position(2, 0), 3, Color.ORANGE, false),
                        new MtList<IVehicle>()))));
  }

  boolean testMap(Tester t) {
    IList<IVehicle> three = new ConsList<IVehicle>(
        new HVehicle(new Position(0, 0), 2, Color.BLUE, false),
        new ConsList<IVehicle>(new VVehicle(new Position(3, 2), 2, Color.BLUE, false),
            new ConsList<IVehicle>(new HVehicle(new Position(5, 5), 2, Color.BLUE, false),
                new MtList<IVehicle>())));
    IList<IVehicle> two = new ConsList<IVehicle>(
        new VVehicle(new Position(3, 2), 2, Color.BLUE, false), new ConsList<IVehicle>(
            new HVehicle(new Position(5, 5), 2, Color.BLUE, false), new MtList<IVehicle>()));
    IList<IVehicle> one = new ConsList<IVehicle>(
        new HVehicle(new Position(5, 5), 2, Color.BLUE, false), new MtList<IVehicle>());

    // remember that your target vehicle has to be a car!
    return t.checkExpect(three.map(new UpdateCarWithTarget(new Position(-1, 0))),
        new ConsList<IVehicle>(new HVehicle(new Position(0, 0), 2, Color.RED, true),
            new ConsList<IVehicle>(new VVehicle(new Position(3, 2), 2, Color.BLUE, false),
                new ConsList<IVehicle>(new HVehicle(new Position(5, 5), 2, Color.BLUE, false),
                    new MtList<IVehicle>()))))
        && t.checkExpect(two.map(new UpdateCarWithTarget(new Position(-1, 0))),
            new ConsList<IVehicle>(new VVehicle(new Position(3, 2), 2, Color.BLUE, false),
                new ConsList<IVehicle>(new HVehicle(new Position(5, 5), 2, Color.BLUE, false),
                    new MtList<IVehicle>())))
        && t.checkExpect(one.map(new UpdateCarWithTarget(new Position(-1, 5))),
            new ConsList<IVehicle>(new HVehicle(new Position(5, 5), 2, Color.RED, true),
                new MtList<IVehicle>()));
  }

  // orMap
  boolean testOrMap(Tester t) {
    IList<String> three = new ConsList<String>("X",
        new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>())));
    IList<String> two = new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>()));
    IList<String> one = new ConsList<String>(" ", new MtList<String>());
    IList<String> empty = new MtList<String>();

    return t.checkExpect(three.orMap(new StringEqual("|")), true)
        && t.checkExpect(two.orMap(new StringEqual("|")), true)
        && t.checkExpect(one.orMap(new StringEqual("|")), false)
        && t.checkExpect(empty.orMap(new StringEqual("|")), false);
  }

  // length
  boolean testLength(Tester t) {
    IList<String> three = new ConsList<String>("X",
        new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>())));
    IList<String> two = new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>()));
    IList<IVehicle> one = new ConsList<IVehicle>(
        new HVehicle(new Position(5, 5), 2, Color.BLUE, false), new MtList<IVehicle>());
    IList<String> empty = new MtList<String>();

    return t.checkExpect(three.length(), 3) && t.checkExpect(two.length(), 2)
        && t.checkExpect(one.length(), 1) && t.checkExpect(empty.length(), 0);
  }

  // getIndex
  boolean testGetIndex(Tester t) {
    IList<String> three = new ConsList<String>("X",
        new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>())));
    IList<String> two = new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>()));
    IList<IVehicle> one = new ConsList<IVehicle>(
        new HVehicle(new Position(5, 5), 2, Color.BLUE, false), new MtList<IVehicle>());

    return t.checkExpect(three.getIndex(1), "|") && t.checkExpect(two.getIndex(1), " ")
        && t.checkExpect(one.getIndex(0), new HVehicle(new Position(5, 5), 2, Color.BLUE, false));
  }

  // findIndex
  boolean testFindIndex(Tester t) {
    IList<String> three = new ConsList<String>("X",
        new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>())));
    IList<String> two = new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>()));

    return t.checkExpect(three.findIndex(new StringEqual(" "), -1), 1)
        && t.checkExpect(two.findIndex(new StringEqual("X"), -1), -1);
  }

  // count
  boolean testCount(Tester t) {
    IList<String> three = new ConsList<String>("X",
        new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>())));
    IList<String> two = new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>()));
    IList<String> five = new ConsList<String>("X",
        new ConsList<String>("|", new ConsList<String>(" ", two)));

    return t.checkExpect(three.count(new StringEqual(" ")), 1)
        && t.checkExpect(two.count(new StringEqual("X")), 0)
        && t.checkExpect(five.count(new StringEqual(" ")), 2);
  }

  // findIndex
  boolean testFind(Tester t) {
    IList<String> three = new ConsList<String>("X",
        new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>())));
    IList<String> two = new ConsList<String>("|", new ConsList<String>(" ", new MtList<String>()));

    return t.checkExpect(three.find(new StringEqual(" ")), 2)
        && t.checkExpect(two.find(new StringEqual("X")), -1)
        && t.checkExpect(two.find(new StringEqual(" ")), 1);
  }

  // DRAW VEHICLE ONTO TEST
  boolean testDrawVehicleOnto(Tester t) {
    IVehicle a = new HVehicle(new Position(2, 3), 3, Color.GREEN, false);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.RED, true);
    WorldImage base = new RectangleImage(500, 500, OutlineMode.OUTLINE, Color.WHITE);

    return t.checkExpect(new DrawVehicleOnto(50).apply(a, base),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, a.toImage(50),
            (double) (2 * 50 * -1), (double) (3 * 50 * -1), base))
        && t.checkExpect(new DrawVehicleOnto(30).apply(b, base),
            new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, b.toImage(30),
                (double) (3 * 30 * -1), (double) (3 * 30 * -1), base))
        && t.checkExpect(new DrawVehicleOnto(11).apply(c, base),
            new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, c.toImage(11),
                (double) (1 * 11 * -1), (double) (2 * 11 * -1), base));
  }

  // IS AT POSITION TEST // FINISHED
  boolean testIsAtPosition(Tester t) {
    Position a = new Position(1, 2);
    Position c = new Position(0, 0);

    IVehicle one = new HVehicle(new Position(0, 2), 3);
    IVehicle two = new HVehicle(new Position(5, 2), 3);
    IVehicle three = new VVehicle(new Position(3, 3), 2);

    return t.checkExpect(new IsAtPosition(a).apply(one), true)
        && t.checkExpect(new IsAtPosition(a).apply(two), false)
        && t.checkExpect(new IsAtPosition(c).apply(three), false);
  }

  // IS TARGET CAR TEST // FINISHED
  boolean testIsTargetCar(Tester t) {
    IVehicle a = new HVehicle(new Position(2, 3), 2);
    IVehicle aBig = new HVehicle(new Position(2, 3), 3);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.GREEN, false);
    IVehicle d = new VVehicle(new Position(1, 0), 2, Color.ORANGE, false);

    return t.checkExpect(new IsTargetCar(new Position(-1, 3)).apply(a), true)
        && t.checkExpect(new IsTargetCar(new Position(-1, 3)).apply(aBig), false)
        && t.checkExpect(new IsTargetCar(new Position(10, 10)).apply(b), false)
        && t.checkExpect(new IsTargetCar(new Position(1, 1)).apply(c), false)
        && t.checkExpect(new IsTargetCar(new Position(1, -1)).apply(d), true);
  }

  // OVERLAP TEST
  boolean testOverlap(Tester t) {

    IVehicle a = new HVehicle(new Position(2, 3), 2, Color.RED, true);
    IVehicle aBig = new HVehicle(new Position(2, 3), 3, Color.RED, true);
    IVehicle b = new VVehicle(new Position(3, 3), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(1, 2), 2, Color.RED, true);

    return t.checkExpect(new Overlap(new Position(2, 3)).apply(a), true)
        && t.checkExpect(new Overlap(new Position(2, 3)).apply(aBig), true)
        && t.checkExpect(new Overlap(new Position(1, 3)).apply(a), false)
        && t.checkExpect(new Overlap(new Position(3, 3)).apply(a), true)
        && t.checkExpect(new Overlap(new Position(2, 2)).apply(a), false)
        && t.checkExpect(new Overlap(new Position(0, 0)).apply(c), false)
        && t.checkExpect(new Overlap(new Position(3, 4)).apply(a), false)
        && t.checkExpect(new Overlap(new Position(2, 4)).apply(b), false)
        && t.checkExpect(new Overlap(new Position(3, 2)).apply(b), false);
  }

  // POSITION TESTS // FINISHED

  // test diff method in Position class
  boolean testDiff(Tester t) {
    Position a = new Position(1, 2);
    Position b = new Position(4, 2);
    Position c = new Position(0, 0);

    return t.checkExpect(a.diff(0, 0), a) && t.checkExpect(b.diff(2, 3), new Position(6, 5))
        && t.checkExpect(c.diff(5, 5), new Position(5, 5));
  }

  // test maxX method in Position class
  boolean testMaxX(Tester t) {
    Position a = new Position(1, 2);
    Position b = new Position(4, 3);
    Position c = new Position(0, 0);

    return t.checkExpect(a.maxX(b), 4) && t.checkExpect(b.maxX(a), 4) && t.checkExpect(c.maxX(c), 0)
        && t.checkExpect(new Position(-4, 2).maxX(new Position(3, 10)), 3);
  }

  // test maxY method in Position class
  boolean testMaxY(Tester t) {
    Position a = new Position(1, 2);
    Position b = new Position(4, 3);
    Position c = new Position(0, 0);

    return t.checkExpect(a.maxY(b), 3) && t.checkExpect(b.maxY(a), 3) && t.checkExpect(c.maxY(c), 0)
        && t.checkExpect(new Position(-4, 2).maxY(new Position(3, 10)), 10);
  }

  // test minX method in Position class
  boolean testMinX(Tester t) {
    Position a = new Position(1, 2);
    Position b = new Position(4, 3);
    Position c = new Position(0, 0);

    return t.checkExpect(a.minX(b), 1) && t.checkExpect(b.minX(a), 1) && t.checkExpect(c.minX(c), 0)
        && t.checkExpect(new Position(-4, 2).minX(new Position(3, 10)), -4);
  }

  // test maxY method in Position class
  boolean testMinY(Tester t) {
    Position a = new Position(1, 2);
    Position b = new Position(4, 3);
    Position c = new Position(0, 0);

    return t.checkExpect(a.minY(b), 2) && t.checkExpect(b.minY(a), 2) && t.checkExpect(c.minY(c), 0)
        && t.checkExpect(new Position(-4, 2).minY(new Position(3, 10)), 2);
  }

  // test nextStart method in Position class
  boolean testNextStart(Tester t) {
    Position a = new Position(1, 1);
    Position b = new Position(4, 2);
    Position c = new Position(0, 0);

    return t.checkExpect(a.nextStart(), new Position(-1, 2))
        && t.checkExpect(b.nextStart(), new Position(-1, 3))
        && t.checkExpect(c.nextStart(), new Position(-1, 1));
  }

  // test sameX method in Position class
  boolean testSameX(Tester t) {
    Position a = new Position(1, 1);
    Position b = new Position(4, 2);
    Position c = new Position(0, 0);
    Position d = new Position(4, 1);

    return t.checkExpect(a.sameX(b), false) && t.checkExpect(b.sameX(a), false)
        && t.checkExpect(b.sameX(b), true) && t.checkExpect(c.sameX(c), true)
        && t.checkExpect(b.sameX(d), true) && t.checkExpect(d.sameX(b), true);
  }

  // test sameX method in Position class
  boolean testSameY(Tester t) {
    Position a = new Position(1, 1);
    Position b = new Position(4, 2);
    Position c = new Position(0, 0);
    Position d = new Position(4, 1);

    return t.checkExpect(a.sameY(b), false) && t.checkExpect(b.sameY(a), false)
        && t.checkExpect(b.sameY(b), true) && t.checkExpect(c.sameY(c), true)
        && t.checkExpect(a.sameY(d), true) && t.checkExpect(d.sameY(a), true);
  }

  // test xPosition method in Position class
  boolean testXPosition(Tester t) {
    Position a = new Position(1, 1);
    Position b = new Position(4, 2);
    Position c = new Position(0, 0);

    return t.checkExpect(a.xPosition(), 1) && t.checkExpect(b.xPosition(), 4)
        && t.checkExpect(c.xPosition(), 0);
  }

  // test yPosition method in Position class
  boolean testYPosition(Tester t) {
    Position a = new Position(1, 1);
    Position b = new Position(4, 2);
    Position c = new Position(0, 0);

    return t.checkExpect(a.yPosition(), 1) && t.checkExpect(b.yPosition(), 2)
        && t.checkExpect(c.yPosition(), 0);
  }

  // STRING EQUAL TEST // FINISHED
  boolean testStringEqual(Tester t) {
    return t.checkExpect(new StringEqual("X").apply("-"), false)
        && t.checkExpect(new StringEqual("|").apply("|"), true)
        && t.checkExpect(new StringEqual("x").apply("X"), false);
  }

  // UPDATE CAR WITH TARGET TEST // FINISHED
  boolean testUpdateCarWithTarget(Tester t) {
    IVehicle a = new HVehicle(new Position(0, 0), 2, Color.BLUE, false);
    IVehicle aBig = new HVehicle(new Position(0, 0), 3, Color.BLUE, false);
    IVehicle b = new VVehicle(new Position(3, 2), 2, Color.BLUE, false);
    IVehicle c = new HVehicle(new Position(5, 5), 2, Color.BLUE, false);

    return t.checkExpect(new UpdateCarWithTarget(new Position(-1, 0)).apply(a),
        new HVehicle(new Position(0, 0), 2, Color.RED, true))
        && t.checkExpect(new UpdateCarWithTarget(new Position(-1, 0)).apply(aBig), aBig)
        && t.checkExpect(new UpdateCarWithTarget(new Position(-1, 0)).apply(b),
            new VVehicle(new Position(3, 2), 2, Color.BLUE, false))
        && t.checkExpect(new UpdateCarWithTarget(new Position(-1, 5)).apply(c),
            new HVehicle(new Position(5, 5), 2, Color.RED, true));
  }

  // UTILS TEST

  boolean testListFromString(Tester t) {
    String a = "abcd";
    String b = "b";
    String mix = "-| /k";
    return t.checkExpect(new Utils().listFromString(a),
        new ConsList<String>("a",
            new ConsList<String>("b",
                new ConsList<String>("c", new ConsList<String>("d", new MtList<String>())))))
        && t.checkExpect(new Utils().listFromString(b),
            new ConsList<String>("b", new MtList<String>()))
        && t.checkExpect(new Utils().listFromString(mix),
            new ConsList<String>("-", new ConsList<String>("|", new ConsList<String>(" ",
                new ConsList<String>("/", new ConsList<String>("k", new MtList<String>()))))));
  }

  boolean testOverlapping(Tester t) {
    IVehicle a = new VVehicle(new Position(3, 2), 3);
    IVehicle b = new VVehicle(new Position(3, 3), 3);
    IVehicle c = new VVehicle(new Position(4, 2), 3);

    IVehicle d = new HVehicle(new Position(1, 1), 2);
    IVehicle e = new HVehicle(new Position(1, 2), 3);
    IVehicle f = new HVehicle(new Position(3, 2), 3);

    IVehicle ver1 = new VVehicle(new Position(0, 0), 2);
    IVehicle ver2 = new VVehicle(new Position(0, 2), 2);
    IVehicle ver3 = new VVehicle(new Position(1, 0), 2);

    return t.checkExpect(a.isOverlapping(b), true) && t.checkExpect(a.isOverlapping(c), false) 
        && t.checkExpect(a.isOverlapping(c), false) && t.checkExpect(d.isOverlapping(e), false)
        && t.checkExpect(ver1.isOverlapping(ver2), false) // next to each other
        && t.checkExpect(ver1.isOverlapping(ver3), false) // side by side vertical vehicles
        && t.checkExpect(ver2.isOverlapping(ver1), false) // symmetric
        && t.checkExpect(ver3.isOverlapping(ver1), false) 
        && t.checkExpect(a.isOverlapping(a), true) // reflexive
        && t.checkExpect(a.isOverlapping(f), true);
    // two vehicles one h one v that have the same topleft
  }

  boolean testInitializeFromString(Tester t) {
    String board1 = "+---+\n" + "|   |\n" + "|   |\n" + "| c X\n" + "+---+";

    String board2 = "+---+\n" + "|  T|\n" + "|c  X\n" + "|   |\n" + "+---+";

    return t.checkExpect(new Utils().initializeFromString(board1),
        new Board(new ConsList<IVehicle>(new HVehicle(new Position(1, 2), 2, Color.RED, true),
            new MtList<IVehicle>()), 3, 3, new Position(3, 2), -1))
        && t.checkExpect(new Utils().initializeFromString(board2),
            new Board(new ConsList<IVehicle>(new HVehicle(new Position(0, 1), 2, Color.RED, true),
                new ConsList<IVehicle>(new VVehicle(new Position(2, 0), 3, Color.ORANGE, false),
                    new MtList<IVehicle>())),
                3, 3, new Position(3, 1), -1));
  }

  boolean testThisWorldMakeScene(Tester t) {
    String demoLevel = "+---+\n" + "|   |\n" + "|   |\n" + "|c  X\n" + "+---+";
    Board b = new Utils().initializeFromString(demoLevel);
    ThisWorld w = new ThisWorld(b);

    WorldScene expected = new WorldScene(160, 160).placeImageXY(b.toImage(32), 80, 80);

    return t.checkExpect(w.makeScene(), expected);
  }

  // on mouse released
  boolean testWorldReleaseMouse(Tester t) {
    String demoLevel = "+---+" + "|   |" + "|c  |" + "|   X" + "+---+";
    Board demoBoard = new Utils().initializeFromString(demoLevel);
    // intial untouched board
    ThisWorld world = new ThisWorld(demoBoard);

    // after vehicle clicked
    ThisWorld afterClicked = world
        .onMouseReleased(new Posn(world.tileHeight * 5 / 2, world.tileHeight * 7 / 2));

    // after deselect
    ThisWorld afterDeselect = afterClicked
        .onMouseReleased(new Posn(world.tileHeight / 2, world.tileHeight / 2));

    return t.checkExpect(afterClicked, new ThisWorld(demoBoard.selectTile(new Position(0, 1))))
        && t.checkExpect(world, afterDeselect);

  }

  // PARSEINFORMATION TEST // FINISHED
  // addExit
  boolean testAddExit(Tester t) {
    ParseInformation abc = new ParseInformation(new Position(0, 0), null, new MtList<IVehicle>());
    ParseInformation def = new ParseInformation(new Position(2, 3), null, new ConsList<IVehicle>(
        new VVehicle(new Position(1, 1), 2, Color.MAGENTA, false), new MtList<IVehicle>()));

    return t.checkExpect(abc.addExit(),
        new ParseInformation(new Position(0, 0), new Position(0, 0), new MtList<IVehicle>()))
        && t.checkExpect(def.addExit(),
            new ParseInformation(new Position(2, 3), new Position(2, 3),
                new ConsList<IVehicle>(new VVehicle(new Position(1, 1), 2, Color.MAGENTA, false),
                    new MtList<IVehicle>())));

  }

  // add HCar
  boolean testAddHCar(Tester t) {
    ParseInformation abc = new ParseInformation(new Position(1, 1), new Position(0, 0),
        new MtList<IVehicle>());
    ParseInformation def = new ParseInformation(new Position(5, 5), new Position(1, 2),
        new ConsList<IVehicle>(new VVehicle(new Position(1, 1), 2, Color.MAGENTA, false),
            new MtList<IVehicle>()));

    return t.checkExpect(abc.addHCar(),
        new ParseInformation(new Position(1, 1), new Position(0, 0),
            new ConsList<IVehicle>(new HVehicle(new Position(1, 1), 2, Color.ORANGE, false),
                new MtList<IVehicle>())))
        && t.checkExpect(def.addHCar(),
            new ParseInformation(new Position(5, 5), new Position(1, 2),
                new ConsList<IVehicle>(new HVehicle(new Position(5, 5), 2, Color.MAGENTA, false),
                    new ConsList<IVehicle>(
                        new VVehicle(new Position(1, 1), 2, Color.MAGENTA, false),
                        new MtList<IVehicle>()))));

  }

  // add HTruck
  boolean testAddHTruck(Tester t) {
    ParseInformation abc = new ParseInformation(new Position(1, 1), new Position(0, 0),
        new MtList<IVehicle>());
    ParseInformation def = new ParseInformation(new Position(5, 5), new Position(1, 2),
        new ConsList<IVehicle>(new VVehicle(new Position(1, 1), 2, Color.MAGENTA, false),
            new MtList<IVehicle>()));

    return t.checkExpect(abc.addHTruck(),
        new ParseInformation(new Position(1, 1), new Position(0, 0),
            new ConsList<IVehicle>(new HVehicle(new Position(1, 1), 3, Color.ORANGE, false),
                new MtList<IVehicle>())))
        && t.checkExpect(def.addHTruck(),
            new ParseInformation(new Position(5, 5), new Position(1, 2),
                new ConsList<IVehicle>(new HVehicle(new Position(5, 5), 3, Color.MAGENTA, false),
                    new ConsList<IVehicle>(
                        new VVehicle(new Position(1, 1), 2, Color.MAGENTA, false),
                        new MtList<IVehicle>()))));

  }

  // add VCar
  boolean testAddVCar(Tester t) {
    ParseInformation abc = new ParseInformation(new Position(1, 1), new Position(0, 0),
        new MtList<IVehicle>());
    ParseInformation def = new ParseInformation(new Position(5, 5), new Position(1, 2),
        new ConsList<IVehicle>(new VVehicle(new Position(1, 1), 2, Color.MAGENTA, false),
            new MtList<IVehicle>()));

    return t.checkExpect(abc.addVCar(),
        new ParseInformation(new Position(1, 1), new Position(0, 0),
            new ConsList<IVehicle>(new VVehicle(new Position(1, 1), 2, Color.ORANGE, false),
                new MtList<IVehicle>())))
        && t.checkExpect(def.addVCar(),
            new ParseInformation(new Position(5, 5), new Position(1, 2),
                new ConsList<IVehicle>(new VVehicle(new Position(5, 5), 2, Color.MAGENTA, false),
                    new ConsList<IVehicle>(
                        new VVehicle(new Position(1, 1), 2, Color.MAGENTA, false),
                        new MtList<IVehicle>()))));
  }

  // add VTruck
  boolean testAddVTruck(Tester t) {
    ParseInformation abc = new ParseInformation(new Position(1, 1), new Position(0, 0),
        new MtList<IVehicle>());
    ParseInformation def = new ParseInformation(new Position(5, 5), new Position(1, 2),
        new ConsList<IVehicle>(new VVehicle(new Position(1, 1), 2, Color.MAGENTA, false),
            new MtList<IVehicle>()));

    return t.checkExpect(abc.addVTruck(),
        new ParseInformation(new Position(1, 1), new Position(0, 0),
            new ConsList<IVehicle>(new VVehicle(new Position(1, 1), 3, Color.ORANGE, false),
                new MtList<IVehicle>())))
        && t.checkExpect(def.addVTruck(),
            new ParseInformation(new Position(5, 5), new Position(1, 2),
                new ConsList<IVehicle>(new VVehicle(new Position(5, 5), 3, Color.MAGENTA, false),
                    new ConsList<IVehicle>(
                        new VVehicle(new Position(1, 1), 2, Color.MAGENTA, false),
                        new MtList<IVehicle>()))));

  }

  boolean testParseInfoNextLine(Tester t) {
    return t.checkExpect(
        new ParseInformation(new Position(6, 1), null, new MtList<IVehicle>()).nextLine(),
        new ParseInformation(new Position(-1, 2), null, new MtList<IVehicle>()))
        && t.checkExpect(
            new ParseInformation(new Position(-1, -1), null, new MtList<IVehicle>()).nextLine(),
            new ParseInformation(new Position(-1, 0), null, new MtList<IVehicle>()));
  }

  boolean testParseInfoNextTile(Tester t) {
    ParseInformation info = new ParseInformation(new Position(1, 2), null, new MtList<IVehicle>());
    ParseInformation expected = new ParseInformation(new Position(2, 2), null,
        new MtList<IVehicle>());

    return t.checkExpect(info.nextTile(), expected);
  }

  // toBoard
  boolean testToBoard(Tester t) {
    ParseInformation first = new ParseInformation(new Position(5, 5), new Position(1, 2),
        new ConsList<IVehicle>(new VVehicle(new Position(5, 5), 3, Color.MAGENTA, false),
            new ConsList<IVehicle>(new VVehicle(new Position(1, 1), 2, Color.RED, true),
                new MtList<IVehicle>())));

    ParseInformation second = new ParseInformation(new Position(7, 8), new Position(10, -1),
        new ConsList<IVehicle>(new VVehicle(new Position(5, 5), 3, Color.MAGENTA, false),
            new ConsList<IVehicle>(new VVehicle(new Position(10, 1), 2, Color.RED, true),
                new MtList<IVehicle>())));

    return t.checkExpect(first.toBoard(10, 10),
        new Board(
            new ConsList<IVehicle>(new VVehicle(new Position(5, 5), 3, Color.MAGENTA, false),
                new ConsList<IVehicle>(new VVehicle(new Position(1, 1), 2, Color.RED, true),
                    new MtList<IVehicle>())),
            10, 10, new Position(1, 2), -1))
        && t.checkExpect(second.toBoard(12, 13),
            new Board(
                new ConsList<IVehicle>(new VVehicle(new Position(5, 5), 3, Color.MAGENTA, false),
                    new ConsList<IVehicle>(new VVehicle(new Position(10, 1), 2, Color.RED, true),
                        new MtList<IVehicle>())),
                12, 13, new Position(10, -1), -1));
  }

  // PARSER TEST
  boolean testParserAddVehicle(Tester t) {
    Parser p = new Parser();
    ParseInformation pi = new ParseInformation(new Position(2, 4), new Position(-1, 3),
        new ConsList<IVehicle>(new VVehicle(new Position(0, 0), 2), new MtList<IVehicle>()));

    return t.checkExpect(p.apply("T", pi), pi.addVTruck().nextTile())
        && t.checkExpect(p.apply("t", pi), pi.addHTruck().nextTile())
        && t.checkExpect(p.apply("C", pi), pi.addVCar().nextTile())
        && t.checkExpect(p.apply("c", pi), pi.addHCar().nextTile());
  }

  // PARSER TEST
  boolean testParserOtherStrings(Tester t) {
    Parser p = new Parser();
    ParseInformation pi = new ParseInformation(new Position(2, 4), new Position(-1, 3),
        new ConsList<IVehicle>(new VVehicle(new Position(0, 0), 2), new MtList<IVehicle>()));

    return t.checkExpect(p.apply("+", pi), pi.nextTile())
        && t.checkExpect(p.apply("\n", pi), pi.nextLine());
  }
}

class ExamplesBoard2 {
  boolean testBigBang(Tester t) {
    ThisWorld w = new ThisWorld();

    return w.bigBang((8 * 32), (8 * 32), 0.1);
  }

}
