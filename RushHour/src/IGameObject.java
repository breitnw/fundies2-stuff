import javalib.funworld.WorldScene;
import javalib.worldimages.FromFileImage;
import javalib.worldimages.RotateImage;
import javalib.worldimages.WorldImage;

// Represents any object in the game
interface IGameObject {
  // Determines if this IGameObject overlaps that IGameObject
  boolean intersects(IGameObject that);
  
  // Returns an GridRect representing the span of this IGameObject, from its top left corner to
  // its bottom right corner.
  GridArea getArea();
  
  // Draws this GameObject to the provided WorldScene, aligned in the top-left corner and offset
  // according to its position.
  WorldScene drawTo(WorldScene scene);
}

// Represents any object in the game with a position
abstract class AGameObject implements IGameObject {
  GridPosn posn;
  
  AGameObject(GridPosn posn) {
    this.posn = posn;
  }
  
  // Determines if this AGameObject intersects that IGameObject
  @Override
  public boolean intersects(IGameObject that) {
    return this.getArea().intersects(that.getArea());
  }
  
  // Returns an GridRect representing the span of this IGameObject, from its top left corner to
  // its bottom right corner.
  @Override
  public GridArea getArea() {
    return new GridArea(this.posn, this.posn.offset(this.xSize(), this.ySize()));
  }
  
  // Returns the size of the horizontal span of this AGameObject (1 by default).
  int xSize() {
    return 1;
  }
  
  
  // Returns the size of the vertical span of this AGameObject (1 by default).
  int ySize() {
    return 1;
  }
  
  // Returns a WorldImage loaded from a file representing this IGameObject, with its pinhole
  // centered.
  abstract WorldImage getImage();
  
  // Draws this GameObject to the provided WorldScene, aligned in the top-left corner and offset
  // according to its position.
  @Override
  public WorldScene drawTo(WorldScene scene) {
    return this.posn.drawPositioned(this.getImage(), scene);
  }
}

// WALLS ------------------------------------------------------------------------------------------

// Represents a wall on the grid.
class Wall extends AGameObject {
  Wall(GridPosn posn) {
    super(posn);
  }
  
  // Returns a WorldImage loaded from a file representing this Wall, with its pinhole
  // centered.
  @Override
  public WorldImage getImage() {
    return new FromFileImage("sprites/bush.png");
  }
}

// EXITS ------------------------------------------------------------------------------------------

// Represents an exit on the grid.
class Exit extends AGameObject {
  Exit(GridPosn posn) {
    super(posn);
  }
  
  // Returns a WorldImage loaded from a file representing this Exit, with its pinhole
  // centered.
  @Override
  public WorldImage getImage() {
    return new FromFileImage("sprites/exit.png");
  }
}

// VEHICLES ---------------------------------------------------------------------------------------

// Represents a vehicle on the game board.
interface IVehicle extends IGameObject {
  // Returns true if this IVehicle is in a winning state; i.e., if this being a PlayerCar implies
  // this is intersecting an Exit.
  boolean inWinningState(IList<Exit> exits);
  
  // Registers a click event on this IVehicle, returning a new IVehicle representing the changed
  // vehicle state after the click.
  IVehicle registerClick(GridPosn clickPosn);
  
  // Registers a key event on this IVehicle, returning a new IVehicle representing the changed
  // vehicle after the key. If this IVehicle is selected, moves it left, right, up, or down
  // depending on which movements are available and which key was pressed.
  IVehicle registerKey(String k, IList<Wall> walls, IList<IVehicle> vehicles);
}

// Represents a vehicle with several abstracted methods
abstract class AVehicle extends AGameObject implements IVehicle {
  int color;
  boolean isHorizontal;
  boolean active;
  
  AVehicle(GridPosn posn, int color, boolean isHorizontal, boolean active) {
    super(posn);
    if (color < 0 || color > 4) {
      throw new IllegalArgumentException("color must be an integer between 1 and 4");
    }
    this.isHorizontal = isHorizontal;
    this.color = color;
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
  
  // Returns true if this AVehicle is in a winning state; i.e., if this being a PlayerCar implies
  // this is intersecting an Exit.
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
  
  // Moves this AVehicle to the provided GridPosn, without checking for collisions.
  abstract AVehicle moveTo(GridPosn newPosn);
  
  // If possible, moves this AVehicle dx tiles horizontally and dy tiles vertically.
  // If the resultant AVehicle is colliding with any other vehicles on the grid, or if the
  // movement isn't in this car's direction of travel, does nothing.
  AVehicle move(int dx, int dy, IList<Wall> walls, IList<IVehicle> vehicles) {
    // Do nothing if the car is horizontal and we're trying to move vertically, or the car is
    // vertical and we're trying to move horizontally.
    if ((this.isHorizontal && dy != 0) || (!this.isHorizontal && dx != 0)) {
      return this;
    }
    
    // Move the vehicle
    AVehicle movedVehicle = this.moveTo(this.posn.offset(dx, dy));
    
    // Determine if there are any collisions with walls or _other_ vehicles
    boolean collidesWithWall = walls.ormap(new IntersectsPred<>(movedVehicle));
    boolean collidesWithVehicle = vehicles.remove(this)
                                      .ormap(new IntersectsPred<>(movedVehicle));
    
    // Return the moved vehicle if there was no collision.
    if (collidesWithWall || collidesWithVehicle) {
      return this;
    } else {
      return movedVehicle;
    }
  }
  
  // Registers a key event on this IVehicle, returning a new IVehicle representing the changed
  // vehicle after the key. If this IVehicle is selected, moves it left, right, up, or down
  // depending on which movements are available and which key was pressed.
  @Override
  public IVehicle registerKey(String k, IList<Wall> walls, IList<IVehicle> vehicles) {
    if (!this.active) {
      return this;
    }
    switch (k) {
      case "w":
      case "up":
        return this.move(0, -1, walls, vehicles);
      case "a":
      case "left":
        return this.move(-1, 0, walls, vehicles);
      case "s":
      case "down":
        return this.move(0, 1, walls, vehicles);
      case "d":
      case "right":
        return this.move(1, 0, walls, vehicles);
    }
    return this;
  }
}

// Represents a car that can be moved forward and backward on the grid.
class Car extends AVehicle {
  Car(GridPosn posn, int color, boolean isHorizontal, boolean active) {
    super(posn, color, isHorizontal, active);
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
  
  // Moves this Car to the provided GridPosn, without checking for collisions.
  @Override
  AVehicle moveTo(GridPosn posn) {
    return new Car(posn, this.color, this.isHorizontal, this.active);
  }
  
  // Returns a WorldImage loaded from a file representing this Car, with its pinhole centered. If
  // the Car is selected, returns a yellow version of that WorldImage.
  @Override
  public WorldImage getImage() {
    if (this.active) {
      return this.drawFromFile("sprites/car/car-selected.png");
    } else {
      return this.drawFromFile("sprites/car/car" + this.color + ".png");
    }
  }
  
  // Registers a click event on this Car, returning a new Car representing the changed vehicle
  // state after the click.
  @Override
  public IVehicle registerClick(GridPosn clickPosn) {
    boolean active = this.getArea().containsPosn(clickPosn);
    return new Car(this.posn, this.color, this.isHorizontal, active);
  }
}

// Represents a car that needs to be intersecting with an Exit in order for the player to win
class PlayerCar extends Car {
  PlayerCar(GridPosn posn, boolean isHorizontal, boolean active) {
    super(posn, 0, isHorizontal, active);
  }
  
  // Returns true if this IVehicle is in a winning state; i.e., if this being a PlayerCar implies
  // this is intersecting an Exit.
  @Override
  public boolean inWinningState(IList<Exit> exits) {
    return exits.ormap(new IntersectsPred<>(this));
  }
  
  // Moves this PlayerCar to the provided GridPosn, without checking for collisions.
  @Override
  AVehicle moveTo(GridPosn posn) {
    return new PlayerCar(posn, this.isHorizontal, this.active);
  }
  
  // Returns a WorldImage loaded from a file representing this PlayerCar, with its pinhole
  // centered. If the PlayerCar is selected, returns a yellow version of that WorldImage.
  @Override
  public WorldImage getImage() {
    if (this.active) {
      return this.drawFromFile("sprites/car/car-selected.png");
    } else {
      return this.drawFromFile("sprites/car/car-player.png");
    }
  }
  
  // Registers a click event on this PlayerCar, returning a new PlayerCar representing the changed
  // vehicle state after the click.
  @Override
  public IVehicle registerClick(GridPosn clickPosn) {
    boolean active = this.getArea().containsPosn(clickPosn);
    return new PlayerCar(this.posn, this.isHorizontal, active);
  }
}

// Represents a truck that can be moved forward and backward on the grid.
class Truck extends AVehicle {
  Truck(GridPosn posn, int color, boolean isHorizontal, boolean active) {
    super(posn, color, isHorizontal, active);
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
  
  // Moves this Truck to the provided GridPosn, without checking for collisions.
  @Override
  AVehicle moveTo(GridPosn posn) {
    return new Truck(posn, color, this.isHorizontal, this.active);
  }
  
  // Returns a WorldImage loaded from a file representing this Truck, with its pinhole
  // centered. If the Truck is selected, returns a yellow version of that WorldImage.
  @Override
  public WorldImage getImage() {
    if (this.active) {
      return this.drawFromFile("sprites/truck/truck-selected.png");
    } else {
      return this.drawFromFile("sprites/truck/truck" + this.color + ".png");
    }
  }
  
  // Registers a click event on this Truck, returning a new Truck representing the changed
  // vehicle state after the click.
  @Override
  public IVehicle registerClick(GridPosn clickPosn) {
    boolean active = this.getArea().containsPosn(clickPosn);
    return new Truck(this.posn, this.color, this.isHorizontal, active);
  }
}