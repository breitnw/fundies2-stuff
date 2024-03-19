import javalib.funworld.WorldScene;
import javalib.worldimages.RotateImage;
import javalib.worldimages.WorldImage;

import java.nio.file.Path;

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
    return new SpriteLoader().fromSpritesDir(Path.of("bush.png"));
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
    return new SpriteLoader().fromSpritesDir(Path.of("exit.png"));
  }
}

// MOVABLES

// Represents a movable object on the game board.
interface IMovable extends IGameObject {
  // Returns true if this IMovable is in a winning state; i.e., if this being the player object
  // implies it is intersecting an Exit.
  boolean inWinningState(IList<Exit> exits);

  // Registers a click event on this IMovable, returning a new IMovable representing the changed
  // movable state after the click.
  IMovable registerClick(GridPosn clickPosn);

  // Registers a key event on this IMovable, returning a new IMovable representing the changed
  // vehicle after the key. If this IMovable is selected, moves it left, right, up, or down
  // depending on which movements are available and which key was pressed.
  IMovable registerKey(String k, IList<Wall> walls, IList<Exit> exits, IList<IMovable> movables);


}

// Represents a movable object with several abstracted methods
abstract class AMovable extends AGameObject implements IMovable {
  boolean active;

  AMovable(GridPosn posn, boolean active) {
    super(posn);
    this.active = active;
  }

  // Returns the size of the horizontal span of this AMovable
  @Override
  abstract int xSize();

  // Returns the length of the vertical span of this AMovable
  @Override
  abstract int ySize();

  // Returns true if this AMovable is in a winning state
  @Override
  public boolean inWinningState(IList<Exit> exits) {
    return true;
  }

  // Moves this AVehicle to the provided GridPosn, without checking for collisions.
  abstract AMovable moveTo(GridPosn newPosn);

  // If possible, moves this AMovable dx tiles horizontally and dy tiles vertically.
  // If the resultant AMovable is colliding with any other vehicles on the grid, or if the
  // movement isn't in this car's direction of travel, does nothing.
  AMovable move(int dx, int dy, IList<Wall> walls, IList<Exit> exits, IList<IMovable> movables) {
    // Move the AMovable
    AMovable moved = this.moveTo(this.posn.offset(dx, dy));

    // Determine if there are any collisions with walls or _other_ vehicles
    boolean collidesWithWall = walls.ormap(new IntersectsPred<>(moved));
    boolean collidesWithVehicle = movables.remove(this)
        .ormap(new IntersectsPred<>(moved));

    // Return the moved vehicle if there was no collision.
    if (collidesWithWall || collidesWithVehicle) {
      return this;
    } else {
      return moved;
    }
  }

  // Registers a key event on this IMovable, returning a new IMovable representing the changed
  // vehicle after the key. If this IMovable is selected, moves it left, right, up, or down
  // depending on which movements are available and which key was pressed.
  @Override
  public IMovable registerKey(
      String k,
      IList<Wall> walls,
      IList<Exit> exits,
      IList<IMovable> movables
  ) {
    if (!this.active) {
      return this;
    }
    switch (k) {
      case "w":
      case "up":
        return this.move(0, -1, walls, exits, movables);
      case "a":
      case "left":
        return this.move(-1, 0, walls, exits, movables);
      case "s":
      case "down":
        return this.move(0, 1, walls, exits, movables);
      case "d":
      case "right":
        return this.move(1, 0, walls, exits, movables);
      default:
        return this;
    }
  }
}

// VEHICLES ---------------------------------------------------------------------------------------

abstract class AVehicle extends AMovable {
  boolean isHorizontal;
  int color;

  AVehicle(GridPosn posn, int color, boolean isHorizontal, boolean active) {
    super(posn, active);
    if (color < 0 || color > 4) {
      throw new IllegalArgumentException("color must be an integer between 1 and 4");
    }
    this.color = color;
    this.isHorizontal = isHorizontal;
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

  // Given the filename corresponding with this AVehicle, creates a WorldImage to represent it,
  // rotated according to its orientation.
  public WorldImage drawFromFile(Path filename) {
    WorldImage sprite = new SpriteLoader().fromSpritesDir(filename);
    if (this.isHorizontal) {
      return new RotateImage(sprite, 90);
    } else {
      return new RotateImage(sprite, 180);
    }
  }

  AMovable move(int dx, int dy, IList<Wall> walls, IList<Exit> exits, IList<IMovable> movables) {
    // Do nothing if the car is horizontal and we're trying to move vertically, or the car is
    // vertical and we're trying to move horizontally.
    if ((this.isHorizontal && dy != 0) || (!this.isHorizontal && dx != 0)) {
      return this;
    }
    return super.move(dx, dy, walls, exits, movables);
  }
}

// Represents a car that can be moved forward and backward on the grid.
class Car extends AVehicle {
  Car(GridPosn posn, boolean active, int color, boolean isHorizontal) {
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
    return new Car(posn, this.active, this.color, this.isHorizontal);
  }

  // Returns a WorldImage loaded from a file representing this Car, with its pinhole centered. If
  // the Car is selected, returns a yellow version of that WorldImage.
  @Override
  public WorldImage getImage() {
    if (this.active) {
      return this.drawFromFile(Path.of("car", "car-selected.png"));
    } else {
      return this.drawFromFile(Path.of("car", "car" + this.color + ".png"));
    }
  }

  // Registers a click event on this Car, returning a new Car representing the changed vehicle
  // state after the click.
  @Override
  public IMovable registerClick(GridPosn clickPosn) {
    boolean active = this.getArea().containsPosn(clickPosn);
    return new Car(this.posn, active, this.color, this.isHorizontal);
  }
}

// Represents a car that needs to be intersecting with an Exit in order for the player to win
class PlayerCar extends Car {
  PlayerCar(GridPosn posn, boolean active, boolean isHorizontal) {
    super(posn, active, 0, isHorizontal);
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
    return new PlayerCar(posn, this.active, this.isHorizontal);
  }

  // Returns a WorldImage loaded from a file representing this PlayerCar, with its pinhole
  // centered. If the PlayerCar is selected, returns a yellow version of that WorldImage.
  @Override
  public WorldImage getImage() {
    if (this.active) {
      return this.drawFromFile(Path.of("car", "car-selected.png"));
    } else {
      return this.drawFromFile(Path.of("car", "car-player.png"));
    }
  }

  // Registers a click event on this PlayerCar, returning a new PlayerCar representing the changed
  // vehicle state after the click.
  @Override
  public IMovable registerClick(GridPosn clickPosn) {
    boolean active = this.getArea().containsPosn(clickPosn);
    return new PlayerCar(this.posn, active, this.isHorizontal);
  }
}

// Represents a truck that can be moved forward and backward on the grid.
class Truck extends AVehicle {
  Truck(GridPosn posn, int color, boolean active, boolean isHorizontal) {
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
    return new Truck(posn, color, this.active, this.isHorizontal);
  }

  // Returns a WorldImage loaded from a file representing this Truck, with its pinhole
  // centered. If the Truck is selected, returns a yellow version of that WorldImage.
  @Override
  public WorldImage getImage() {
    if (this.active) {
      return this.drawFromFile(Path.of("truck", "truck-selected.png"));
    } else {
      return this.drawFromFile(Path.of("truck", "truck" + this.color + ".png"));
    }
  }

  // Registers a click event on this Truck, returning a new Truck representing the changed
  // vehicle state after the click.
  @Override
  public IMovable registerClick(GridPosn clickPosn) {
    boolean active = this.getArea().containsPosn(clickPosn);
    return new Truck(this.posn, this.color, active, this.isHorizontal);
  }
}

// Klotski pieces ---------------------------------------------------------------------------------

// TODO: needs tests for everything

// Represents an abstract Klotski piece with functionality for generating images
abstract class ABox extends AMovable {
  int width, height;

  ABox(GridPosn posn, boolean active, int width, int height) {
    super(posn, active);
    this.width = width;
    this.height = height;
  }

  // Gets the filename of this ABox's tileset
  abstract String tilesetFilename();

  // Gets the image associated with this ABox according to its tileset filename
  @Override
  WorldImage getImage() {
    TiledImage image = new TiledImage(
        new NineSlice(Path.of("klotski", this.tilesetFilename())),
        this.width * 2,
        this.height * 2
    );
    return image.draw();
  }

  // Gets the horizontal size of this ABox, which is specified by the user
  @Override
  int xSize() {
    return width;
  }

  // Gets the vertical size of this ABox, which is specified by the user
  @Override
  int ySize() {
    return height;
  }
}

// Represents a Klotski piece that can be moved in all directions and may have an arbitrary size.
// It may not be moved to overlap an exit.
class NormalBox extends ABox {
  NormalBox(GridPosn posn, boolean active, int width, int height) {
    super(posn, active, width, height);
  }

  // Gets the filename of this NormalBox's tileset depending on whether it's active
  @Override
  String tilesetFilename() {
    if (this.active) {
      return "normal-box-active.png";
    } else {
      return "normal-box.png";
    }
  }

  // Registers a click event on this Box, returning a new Box representing the changed box state
  // after the click.
  @Override
  public IMovable registerClick(GridPosn clickPosn) {
    boolean active = this.getArea().containsPosn(clickPosn);
    return new NormalBox(this.posn, active, this.width, this.height);
  }

  // Moves this Box to the provided GridPosn, without checking for collisions.
  @Override
  AMovable moveTo(GridPosn newPosn) {
    return new NormalBox(newPosn, this.active, this.width, this.height);
  }

  // If possible, moves this NormalBox dx tiles horizontally and dy tiles vertically.
  // If the resultant NormalBox is colliding with any other movables (boxes), walls, or the
  // exit tile, returns the NormalBox in its original position
  @Override
  AMovable move(int dx, int dy, IList<Wall> walls, IList<Exit> exits, IList<IMovable> movables) {
    AMovable moved = this.moveTo(this.posn.offset(dx, dy));
    if (exits.ormap(new IntersectsPred<>(moved))) {
      return this;
    }
    else return super.move(dx, dy, walls, exits, movables);
  }
}

// Represents a Klotski piece representing the player, that can be moved in all directions and
// may have an arbitrary size. It may also be moved to the exit.
class PlayerBox extends ABox {
  PlayerBox(GridPosn posn, boolean active, int width, int height) {
    super(posn, active, width, height);
  }

  // Returns true if this IMovable is in a winning state; i.e., if this being a PlayerBox implies
  // this is intersecting an Exit.
  @Override
  public boolean inWinningState(IList<Exit> exits) {
    return exits.ormap(new IntersectsPred<>(this));
  }

  // Gets the filename of this PlayerBox's tileset depending on whether it's active
  @Override
  String tilesetFilename() {
    if (this.active) {
      return "player-box-active.png";
    } else {
      return "player-box.png";
    }
  }

  // Registers a click event on this PlayerBox, returning a new Box representing the changed box state
  // after the click.
  @Override
  public IMovable registerClick(GridPosn clickPosn) {
    boolean active = this.getArea().containsPosn(clickPosn);
    return new PlayerBox(this.posn, active, this.width, this.height);
  }

  // Moves this PlayerBox to the provided GridPosn, without checking for collisions.
  @Override
  AMovable moveTo(GridPosn newPosn) {
    return new PlayerBox(newPosn, this.active, this.width, this.height);
  }
}