import javalib.funworld.WorldScene;
import javalib.worldimages.FromFileImage;
import javalib.worldimages.Posn;
import javalib.worldimages.RotateImage;
import javalib.worldimages.WorldImage;

// Represents any object in the game
interface IGameObject {
  // Determines if this IGameObject overlaps that IGameObject
  boolean overlaps(IGameObject that);
  
  // Returns an GridRect representing the span of this IGameObject, from
  GridRect getRect();
  
  // Determines if this GameObject is fully contained in the grid defined by width gridWidth and
  // height gridHeight.
  boolean inGrid(int gridWidth, int gridHeight);
  
  // Returns a WorldImage loaded from a file representing this IGameObject, with its pinhole
  // centered.
  WorldImage draw();
  
  // Draws this GameObject to the provided WorldScene at its
  WorldScene drawTo(WorldScene scene);
}

// Represents any object in the game with a position
abstract class AGameObject implements IGameObject {
  GridPosn posn;
  
  AGameObject(GridPosn posn) {
    this.posn = posn;
  }
  
  // Determines if this AGameObject overlaps that IGameObject
  @Override
  public boolean overlaps(IGameObject that) {
    return this.getRect().intersects(that.getRect());
  }
  
  @Override
  public GridRect getRect() {
    return new GridRect(this.posn, this.posn.offset(this.xSize(), this.ySize()));
  }
  
  // Returns the size of the horizontal span of this AGameObject (1 by default).
  int xSize() { return 1; }
  
  // Returns the size of the vertical span of this AGameObject (1 by default).
  int ySize() { return 1; }
  
  // Determines if this GameObject is fully contained in the grid defined by width gridWidth and
  // height gridHeight.
  @Override
  public boolean inGrid(int gridWidth, int gridHeight) {
    return new GridRect(new GridPosn(), new GridPosn(gridWidth, gridHeight)).containsRect(this.getRect());
  }
  
  @Override
  public WorldScene drawTo(WorldScene scene) {
    return this.posn.drawPositioned(this.draw(), scene);
  }
}

// WALLS ------------------------------------------------------------------------------------------

// Represents a wall on the grid.
class Wall extends AGameObject {
  Wall(GridPosn posn) {
    super(posn);
  }
  
  @Override
  public WorldImage draw() {
    return new FromFileImage("sprites/bush.png");
  }
}

// EXITS ------------------------------------------------------------------------------------------

// Represents an exit on the grid.
class Exit extends AGameObject {
  Exit(GridPosn posn) {
    super(posn);
  }
  
  @Override
  public WorldImage draw() {
    return new FromFileImage("sprites/exit.png");
  }
}

// VEHICLES ---------------------------------------------------------------------------------------

abstract class AVehicle extends AGameObject {
  String color;
  boolean isHorizontal;
  boolean active;
  
  AVehicle(GridPosn posn, String color, boolean isHorizontal, boolean active) {
    super(posn);
    this.color = color;
    this.isHorizontal = isHorizontal;
    this.active = active;
  }
  
  // Returns the width of this AVehicle (not accounting for rotation)
  abstract int getWidth();
  // Returns the length of this AVehicle (not accounting for rotation)
  abstract int getLength();
  
  // Returns the size of the horizontal span of this AVehicle by selecting either the AVehicle's
  // length or width depending on its rotation.
  @Override
  int xSize() {
    if (this.isHorizontal) {
      return this.getLength();
    } else {
      return this.getWidth();
    }
  }
  
  // Returns the length of the vertical span of this AVehicle by selecting either the AVehicle's
  // length or width depending on its rotation.
  @Override
  int ySize() {
    if (this.isHorizontal) {
      return this.getWidth();
    } else {
      return this.getLength();
    }
  }
  
  // Returns true if this IVehicle is in a winning state; i.e., if this being a PlayerCar implies
  // this is overlapping an Exit.
  public boolean inWinningState(IList<Exit> exits) {
    return true;
  }
  
  // Given the filename corresponding with this AVehicle, creates a WorldImage to represent it,
  // rotated according to its orientation.
  public WorldImage drawFromFile(String filename) {
    WorldImage sprite = new FromFileImage(filename);
    if (this.isHorizontal) {
      return new RotateImage(sprite, 90);
    } else {
      return new RotateImage(sprite, 180);
    }
  }
  
  abstract AVehicle registerClick(GridPosn clickPosn);
}

// Represents a car that can be moved forward and backward on the grid.
class Car extends AVehicle {
  Car(GridPosn posn, String color, boolean isHorizontal, boolean active) {
    super(posn, color, isHorizontal, active);
  }
  
  Car(GridPosn posn, String color, boolean isHorizontal) {
    this(posn, color, isHorizontal, false);
  }
  
  // Returns the width of this Car (not accounting for rotation)
  @Override
  public int getWidth() {
    return 1;
  }
  
  // Returns the length of this IVehicle (not accounting for rotation)
  @Override
  public int getLength() {
    return 2;
  }
  
  @Override
  public WorldImage draw() {
    if (this.active) {
      return this.drawFromFile("sprites/car/car-selected.png");
    } else {
      return this.drawFromFile("sprites/car/car1.png");
    }
  }
  
  @Override
  AVehicle registerClick(GridPosn clickPosn) {
    boolean active = this.getRect().containsPosn(clickPosn);
    return new Car(this.posn, this.color, this.isHorizontal, active);
  }
}

// Represents a car that needs to be overlapping with an Exit in order for the player to win
class PlayerCar extends Car {
  PlayerCar(GridPosn posn, boolean isHorizontal, boolean active) {
    super(posn, "red", isHorizontal, active);
  }
  
  PlayerCar(GridPosn posn, boolean isHorizontal) {
    this(posn, isHorizontal, false);
  }
  
  // Returns true if this IVehicle is in a winning state; i.e., if this being a PlayerCar implies
  // this is overlapping an Exit.
  @Override
  public boolean inWinningState(IList<Exit> exits) {
    return exits.ormap(new OverlappingPred<>(this));
  }
  
  @Override
  public WorldImage draw() {
    if (this.active) {
      return this.drawFromFile("sprites/car/car-selected.png");
    } else {
      return this.drawFromFile("sprites/car/car-player.png");
    }
  }
  
  @Override
  AVehicle registerClick(GridPosn clickPosn) {
    boolean active = this.getRect().containsPosn(clickPosn);
    return new PlayerCar(this.posn, this.isHorizontal, active);
  }
}

// Represents a truck that can be moved forward and backward on the grid.
class Truck extends AVehicle {
  Truck(GridPosn posn, String color, boolean isHorizontal, boolean active) {
    super(posn, color, isHorizontal, active);
  }
  
  Truck(GridPosn posn, String color, boolean isHorizontal) {
    this(posn, color, isHorizontal, false);
  }
  
  // Returns the width of this IVehicle (not accounting for rotation)
  @Override
  public int getWidth() {
    return 1;
  }
  
  // Returns the length of this IVehicle (not accounting for rotation)
  @Override
  public int getLength() {
    return 3;
  }
  
  @Override
  public WorldImage draw() {
    if (this.active) {
      return this.drawFromFile("sprites/truck/truck-selected.png");
    } else {
      return this.drawFromFile("sprites/truck/truck1.png");
    }
  }
  
  @Override
  AVehicle registerClick(GridPosn clickPosn) {
    boolean active = this.getRect().containsPosn(clickPosn);
    return new Truck(this.posn, this.color, this.isHorizontal, active);
  }
}