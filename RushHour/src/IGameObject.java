import javalib.impworld.WorldScene;
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
  // EFFECT: the provided WorldScene is mutated to display this object
  void drawTo(WorldScene scene);
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
  public void drawTo(WorldScene scene) {
    this.posn.drawPositioned(this.getImage(), scene);
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

// MOVABLES ---------------------------------------------------------------------------------------

// Represents a movable object on the game board.
interface IMovable extends IGameObject {
  // Returns true if this IMovable is in a winning state; i.e., if this being the player object
  // implies it is intersecting an Exit.
  boolean inWinningState(IList<Exit> exits);

  // Registers a click event on this IMovable, selecting it if the click was within its area and 
  // deselecting it otherwise
  // EFFECT: mutates the selected field of this IMovable
  void registerClick(GridPosn clickPosn);

  // Registers a key event on this IMovable. If this IMovable is selected, moves it left, right,
  // up, or down depending on which movements are available and which key was pressed.
  // EFFECT: the position of this IMovable is mutated depending on which key was pressed and
  // which moves are available
  void registerKey(String k, IList<Wall> walls, IList<Exit> exits, IList<IMovable> movables);
}

// Represents a movable object with several abstracted methods
abstract class AMovable extends AGameObject implements IMovable {
  boolean selected;

  AMovable(GridPosn posn, boolean selected) {
    super(posn);
    this.selected = selected;
  }

  // Returns true if this AMovable is in a winning state
  @Override
  public boolean inWinningState(IList<Exit> exits) {
    return true;
  }

  // If possible, moves this AMovable dx tiles horizontally and dy tiles vertically.
  // If the resultant AMovable is colliding with any other vehicles on the grid, or if the
  // movement isn't in this car's direction of travel, does nothing.
  // EFFECT: changes the posn of this vehicle if the provided move doesn't result in a collision
  void move(int dx, int dy, IList<Wall> walls, IList<Exit> exits, IList<IMovable> movables) {
    // Move the AMovable
    this.posn = this.posn.offset(dx, dy);

    // Determine if there are any collisions with walls or _other_ vehicles
    boolean collidesWithWall = walls.ormap(new IntersectsPred<>(this));
    boolean collidesWithVehicle = movables
        .without(this)
        .ormap(new IntersectsPred<>(this));

    // Move the vehicle back to where it was if there was a collision
    if (collidesWithWall || collidesWithVehicle) {
      this.posn = this.posn.offset(-dx, -dy);
    }
  }

  // Registers a click event on this IMovable, selecting it if the clicked GridPosn is contained
  // by this IMovable's Area
  // EFFECT: mutates this IMovable's selected field according to whether its area contains the
  // provided GridPosn
  @Override
  public void registerClick(GridPosn clickPosn) {
    this.selected = this.getArea().containsPosn(clickPosn);
  }

  // Registers a key event on this IMovable, returning a new IMovable representing the changed
  // vehicle after the key. If this IMovable is selected, moves it left, right, up, or down
  // depending on which movements are available and which key was pressed.
  // EFFECT: changes the posn of this vehicle if the provided key represents a valid move,
  // doesn't result in a collision, and the car is selected.
  @Override
  public void registerKey(
      String k,
      IList<Wall> walls,
      IList<Exit> exits,
      IList<IMovable> movables
  ) {
    if (!this.selected) {
      return;
    }
    switch (k) {
      case "w":
      case "up":
        this.move(0, -1, walls, exits, movables);
        break;
      case "a":
      case "left":
        this.move(-1, 0, walls, exits, movables);
        break;
      case "s":
      case "down":
        this.move(0, 1, walls, exits, movables);
        break;
      case "d":
      case "right":
        this.move(1, 0, walls, exits, movables);
        break;
    }
  }
}

// VEHICLES ---------------------------------------------------------------------------------------

abstract class AVehicle extends AMovable {
  boolean isHorizontal;
  int color;

  AVehicle(GridPosn posn, int color, boolean isHorizontal, boolean selected) {
    super(posn, selected);
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

  // Gets the path to the image associated with this AVehicle
  abstract Path imagePath();

  // Returns a WorldImage loaded from a file representing this Car, with its pinhole centered,
  // rotated if necessary. If the Car is selected, returns a yellow version of that WorldImage.
  @Override
  public WorldImage getImage() {
    WorldImage sprite = new SpriteLoader().fromSpritesDir(this.imagePath());
    if (this.isHorizontal) {
      return new RotateImage(sprite, 90);
    } else {
      return new RotateImage(sprite, 180);
    }
  }

  // If possible, moves this AMovable dx tiles horizontally and dy tiles vertically.
  // If the resultant AMovable is colliding with any other vehicles on the grid, or if the
  // movement isn't in this car's direction of travel, does nothing.
  // EFFECT: changes the posn of this vehicle if the provided move doesn't result in a collision
  @Override
  void move(int dx, int dy, IList<Wall> walls, IList<Exit> exits, IList<IMovable> movables) {
    // Only move if the car is horizontal or there's no horizontal movement, AND the car is
    // vertical or there's no vertical movement.
    if ((this.isHorizontal || dx == 0) && (!this.isHorizontal || dy == 0)) {
      super.move(dx, dy, walls, exits, movables);
    }
  }
}

// Represents a car that can be moved forward and backward on the grid.
class Car extends AVehicle {
  Car(GridPosn posn, boolean selected, int color, boolean isHorizontal) {
    super(posn, color, isHorizontal, selected);
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

  // Gets the path to the image associated with this Car
  @Override
  public Path imagePath() {
    if (this.selected) {
      return Path.of("car", "car-selected.png");
    } else {
      return Path.of("car", "car" + this.color + ".png");
    }
  }
}

// Represents a car that needs to be intersecting with an Exit in order for the player to win
class PlayerCar extends Car {
  PlayerCar(GridPosn posn, boolean selected, boolean isHorizontal) {
    super(posn, selected, 0, isHorizontal);
  }

  // Returns true if this IVehicle is in a winning state; i.e., if this being a PlayerCar implies
  // this is intersecting an Exit.
  @Override
  public boolean inWinningState(IList<Exit> exits) {
    return exits.ormap(new IntersectsPred<>(this));
  }

  // Gets the path to the image associated with this Truck
  @Override
  public Path imagePath() {
    if (this.selected) {
      return Path.of("car", "car-selected.png");
    } else {
      return Path.of("car", "car-player.png");
    }
  }
}

// Represents a truck that can be moved forward and backward on the grid.
class Truck extends AVehicle {
  Truck(GridPosn posn, int color, boolean selected, boolean isHorizontal) {
    super(posn, color, isHorizontal, selected);
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

  // Gets the path to the image associated with this Truck
  @Override
  public Path imagePath() {
    if (this.selected) {
      return Path.of("truck", "truck-selected.png");
    } else {
      return Path.of("truck", "truck" + this.color + ".png");
    }
  }
}

// Klotski pieces ---------------------------------------------------------------------------------

// TODO: needs tests for everything

// Represents an abstract Klotski piece with functionality for generating images
abstract class ABox extends AMovable {
  int width, height;

  ABox(GridPosn posn, boolean selected, int width, int height) {
    super(posn, selected);
    if (width < 1 || height < 1) {
      throw new IllegalArgumentException("box width and height may not be less than 1");
    }
    this.width = width;
    this.height = height;
  }

  // Gets the path to the image associated with this ABox
  abstract Path tilesetPath();

  // Gets the image associated with this ABox according to its tileset filename
  @Override
  WorldImage getImage() {
    TiledImage image = new TiledImage(
        new NineSlice(this.tilesetPath()),
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
  NormalBox(GridPosn posn, boolean selected, int width, int height) {
    super(posn, selected, width, height);
  }

  // Gets the filename of this NormalBox's tileset depending on whether it's selected
  @Override
  Path tilesetPath() {
    if (this.selected) {
      return Path.of("klotski", "normal-box-selected.png");
    } else {
      return Path.of("klotski", "normal-box.png");
    }
  }

  // If possible, moves this NormalBox dx tiles horizontally and dy tiles vertically.
  // If the resultant NormalBox is colliding with any other movables (boxes), walls, or the
  // exit tile, returns the NormalBox in its original position
  @Override
  void move(int dx, int dy, IList<Wall> walls, IList<Exit> exits, IList<IMovable> movables) {
    // Move the NormalBox
    this.posn = this.posn.offset(dx, dy);

    // Determine if there are any collisions with walls or _other_ vehicles
    boolean collidesWithWall = walls.ormap(new IntersectsPred<>(this));
    boolean collidesWithExit = exits.ormap(new IntersectsPred<>(this));
    boolean collidesWithVehicle = movables
        .without(this)
        .ormap(new IntersectsPred<>(this));

    // Move the vehicle back to where it was if there was a collision
    if (collidesWithWall || collidesWithVehicle || collidesWithExit) {
      this.posn = this.posn.offset(-dx, -dy);
    }
  }
}

// Represents a Klotski piece representing the player, that can be moved in all directions and
// may have an arbitrary size. It may also be moved to the exit.
class PlayerBox extends ABox {
  PlayerBox(GridPosn posn, boolean selected, int width, int height) {
    super(posn, selected, width, height);
  }

  // Returns true if this IMovable is in a winning state; i.e., if this being a PlayerBox implies
  // this is intersecting an Exit.
  @Override
  public boolean inWinningState(IList<Exit> exits) {
    return exits.ormap(new IntersectsPred<>(this));
  }

  // Gets the filename of this NormalBox's tileset depending on whether it's selected
  @Override
  Path tilesetPath() {
    if (this.selected) {
      return Path.of("klotski", "player-box-selected.png");
    } else {
      return Path.of("klotski", "player-box.png");
    }
  }
}