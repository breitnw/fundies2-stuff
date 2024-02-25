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
  GridRect getRect();
  
  // Draws this GameObject to the provided WorldScene, aligned in the top-left corner and offset
  // according to its position.
  WorldScene drawTo(WorldScene scene, int tileSize);
}

// Represents any object in the game with a position
abstract class AGameObject implements IGameObject {
  GridPosn posn;
  
  AGameObject(GridPosn posn) {
    this.posn = posn;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.posn ...                                                   -- GridPosn
  METHODS:
  ... intersects(IGameObject that) ...                                  -- boolean
  ... getRect() ...                                                   -- GridRect
  ... xSize() ...                                                     -- int
  ... ySize() ...                                                     -- int
  ... getImage() ...                                                  -- WorldImage
  ... drawTo(WorldScene scene) ...                                    -- WorldScene
  METHODS ON FIElDS:
  ... this.posn.offset(int dx, int dy) ...                            -- GridPosn
  ... this.posn.drawPositioned(WorldImage im, WorldScene scene) ...   -- WorldScene
   */
  
  // Determines if this AGameObject intersects that IGameObject
  @Override
  public boolean intersects(IGameObject that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...             -- IGameObject
    METHODS ON PARAMETERS:
    ... that.getRect() ...   -- GridRect
     */
    return this.getRect().intersects(that.getRect());
  }
  
  // Returns an GridRect representing the span of this IGameObject, from its top left corner to
  // its bottom right corner.
  @Override
  public GridRect getRect() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return new GridRect(this.posn, this.posn.offset(this.xSize(), this.ySize()));
  }
  
  // Returns the size of the horizontal span of this AGameObject (1 by default).
  int xSize() {
    /* TEMPLATE
    Template: Same as class template.
    */
    return 1;
  }
  
  
  // Returns the size of the vertical span of this AGameObject (1 by default).
  int ySize() {
    /* TEMPLATE
    Template: Same as class template.
    */
    return 1;
  }
  
  // Returns a WorldImage loaded from a file representing this IGameObject, with its pinhole
  // centered.
  abstract WorldImage getImage();
  
  // Draws this GameObject to the provided WorldScene, aligned in the top-left corner and offset
  // according to its position.
  @Override
  public WorldScene drawTo(WorldScene scene, int tileSize) {
    /* TEMPLATE
    PARAMETERS:
    ... scene ...   -- WorldScene
     */
    return this.posn.drawPositioned(this.getImage(), scene, tileSize);
  }
}

// WALLS ------------------------------------------------------------------------------------------

// Represents a wall on the grid.
class Wall extends AGameObject {
  Wall(GridPosn posn) {
    super(posn);
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.posn ...                                                   -- GridPosn
  METHODS:
  ... intersects(IGameObject that) ...                                  -- boolean
  ... getRect() ...                                                   -- GridRect
  ... xSize() ...                                                     -- int
  ... ySize() ...                                                     -- int
  ... getImage() ...                                                  -- WorldImage
  ... drawTo(WorldScene scene) ...                                    -- WorldScene
  METHODS ON FIElDS:
  ... this.posn.offset(int dx, int dy) ...                            -- GridPosn
  ... this.posn.drawPositioned(WorldImage im, WorldScene scene) ...   -- WorldScene
   */
  
  // Returns a WorldImage loaded from a file representing this Wall, with its pinhole
  // centered.
  @Override
  public WorldImage getImage() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return new FromFileImage("sprites/bush.png");
  }
}

// EXITS ------------------------------------------------------------------------------------------

// Represents an exit on the grid.
class Exit extends AGameObject {
  Exit(GridPosn posn) {
    super(posn);
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.posn ...                                                   -- GridPosn
  METHODS:
  ... intersects(IGameObject that) ...                                  -- boolean
  ... getRect() ...                                                   -- GridRect
  ... xSize() ...                                                     -- int
  ... ySize() ...                                                     -- int
  ... getImage() ...                                                  -- WorldImage
  ... drawTo(WorldScene scene) ...                                    -- WorldScene
  METHODS ON FIElDS:
  ... this.posn.offset(int dx, int dy) ...                            -- GridPosn
  ... this.posn.drawPositioned(WorldImage im, WorldScene scene) ...   -- WorldScene
   */
  
  // Returns a WorldImage loaded from a file representing this Exit, with its pinhole
  // centered.
  @Override
  public WorldImage getImage() {
    /* TEMPLATE
    Template: Same as class template.
     */
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
    this.active = active;
    this.color = color;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.posn ...                                                   -- GridPosn
  ... this.color ...                                                  -- int
  ... this.isHorizontal ...                                           -- boolean
  ... this.active ...                                                 -- boolean
  METHODS:
  ... intersects(IGameObject that) ...                                -- boolean
  ... getRect() ...                                                   -- GridRect
  ... xSize() ...                                                     -- int
  ... ySize() ...                                                     -- int
  ... getImage() ...                                                  -- WorldImage
  ... drawTo(WorldScene scene) ...                                    -- WorldScene
  ... getWidth() ...                                                  -- int
  ... getLength() ...                                                 -- int
  ... inWinningState(IList<Exit> exits) ...                           -- boolean
  ... drawFromFile(String filename) ...                               -- WorldImage
  ... registerClick(GridPosn clickPosn) ...                           -- IVehicle
  METHODS ON FIElDS:
  ... this.posn.offset(int dx, int dy) ...                            -- GridPosn
  ... this.posn.drawPositioned(WorldImage im, WorldScene scene) ...   -- WorldScene
   */
  
  // Returns the width of this AVehicle (not accounting for rotation)
  abstract int getWidth();
  
  // Returns the length of this AVehicle (not accounting for rotation)
  abstract int getLength();
  
  // Returns the size of the horizontal span of this AVehicle by selecting either the AVehicle's
  // length or width depending on its rotation.
  @Override
  int xSize() {
    /* TEMPLATE
    Template: Same as class template.
     */
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
    /* TEMPLATE
    Template: Same as class template.
     */
    if (this.isHorizontal) {
      return this.getWidth();
    } else {
      return this.getLength();
    }
  }
  
  // Returns true if this AVehicle is in a winning state; i.e., if this being a PlayerCar implies
  // this is intersecting an Exit.
  public boolean inWinningState(IList<Exit> exits) {
    /* TEMPLATE
    PARAMETERS:
    ... exits ...   -- IList<Exit>
     */
    return true;
  }
  
  // Given the filename corresponding with this AVehicle, creates a WorldImage to represent it,
  // rotated according to its orientation.
  public WorldImage drawFromFile(String filename) {
    /* TEMPLATE
    PARAMETERS:
    ... filename ...   -- String
     */
    WorldImage sprite = new FromFileImage(filename);
    if (this.isHorizontal) {
      return new RotateImage(sprite, 90);
    } else {
      return new RotateImage(sprite, 180);
    }
  }
}

// Represents a car that can be moved forward and backward on the grid.
class Car extends AVehicle {
  Car(GridPosn posn, int color, boolean isHorizontal, boolean active) {
    super(posn, color, isHorizontal, active);
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.posn ...                                                   -- GridPosn
  ... this.color ...                                                  -- int
  ... this.isHorizontal ...                                           -- boolean
  ... this.active ...                                                 -- boolean
  METHODS:
  ... intersects(IGameObject that) ...                                -- boolean
  ... getRect() ...                                                   -- GridRect
  ... xSize() ...                                                     -- int
  ... ySize() ...                                                     -- int
  ... getImage() ...                                                  -- WorldImage
  ... drawTo(WorldScene scene) ...                                    -- WorldScene
  ... getWidth() ...                                                  -- int
  ... getLength() ...                                                 -- int
  ... inWinningState(IList<Exit> exits) ...                           -- boolean
  ... drawFromFile(String filename) ...                               -- WorldImage
  ... registerClick(GridPosn clickPosn) ...                           -- IVehicle
  METHODS ON FIElDS:
  ... this.posn.offset(int dx, int dy) ...                            -- GridPosn
  ... this.posn.drawPositioned(WorldImage im, WorldScene scene) ...   -- WorldScene
   */
  
  // Returns the width of this Car (not accounting for rotation)
  @Override
  public int getWidth() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return 1;
  }
  
  // Returns the length of this IVehicle (not accounting for rotation)
  @Override
  public int getLength() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return 2;
  }
  
  // Returns a WorldImage loaded from a file representing this Car, with its pinhole centered. If
  // the Car is selected, returns a yellow version of that WorldImage.
  @Override
  public WorldImage getImage() {
    /* TEMPLATE
    Template: Same as class template.
     */
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
    /* TEMPLATE
    PARAMETERS:
    ... clickPosn ...   -- GridPosn
     */
    boolean active = this.getRect().containsPosn(clickPosn);
    return new Car(this.posn, this.color, this.isHorizontal, active);
  }
}

// Represents a car that needs to be intersecting with an Exit in order for the player to win
class PlayerCar extends Car {
  PlayerCar(GridPosn posn, boolean isHorizontal, boolean active) {
    super(posn, 0, isHorizontal, active);
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.posn ...                                                   -- GridPosn
  ... this.color ...                                                  -- int
  ... this.isHorizontal ...                                           -- boolean
  ... this.active ...                                                 -- boolean
  METHODS:
  ... intersects(IGameObject that) ...                                -- boolean
  ... getRect() ...                                                   -- GridRect
  ... xSize() ...                                                     -- int
  ... ySize() ...                                                     -- int
  ... getImage() ...                                                  -- WorldImage
  ... drawTo(WorldScene scene) ...                                    -- WorldScene
  ... getWidth() ...                                                  -- int
  ... getLength() ...                                                 -- int
  ... inWinningState(IList<Exit> exits) ...                           -- boolean
  ... drawFromFile(String filename) ...                               -- WorldImage
  ... registerClick(GridPosn clickPosn) ...                           -- IVehicle
  METHODS ON FIElDS:
  ... this.posn.offset(int dx, int dy) ...                            -- GridPosn
  ... this.posn.drawPositioned(WorldImage im, WorldScene scene) ...   -- WorldScene
   */
  
  // Returns true if this IVehicle is in a winning state; i.e., if this being a PlayerCar implies
  // this is intersecting an Exit.
  @Override
  public boolean inWinningState(IList<Exit> exits) {
    /* TEMPLATE
    PARAMETERS:
    ... exits ...                                    -- IList<Exit>
    METHODS ON PARAMETERS:
    ... exits.ormap(Function<T, Boolean> func) ...   -- boolean
     */
    return exits.ormap(new IntersectsPred<>(this));
  }
  
  // Returns a WorldImage loaded from a file representing this PlayerCar, with its pinhole
  // centered. If the PlayerCar is selected, returns a yellow version of that WorldImage.
  @Override
  public WorldImage getImage() {
    /* TEMPLATE
    Template: Same as class template.
     */
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
    /* TEMPLATE
    PARAMETERS:
    ... clickPosn ...   -- GridPosn
     */
    boolean active = this.getRect().containsPosn(clickPosn);
    return new PlayerCar(this.posn, this.isHorizontal, active);
  }
}

// Represents a truck that can be moved forward and backward on the grid.
class Truck extends AVehicle {
  Truck(GridPosn posn, int color, boolean isHorizontal, boolean active) {
    super(posn, color, isHorizontal, active);
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.posn ...                                                   -- GridPosn
  ... this.color ...                                                  -- int
  ... this.isHorizontal ...                                           -- boolean
  ... this.active ...                                                 -- boolean
  METHODS:
  ... intersects(IGameObject that) ...                                -- boolean
  ... getRect() ...                                                   -- GridRect
  ... xSize() ...                                                     -- int
  ... ySize() ...                                                     -- int
  ... getImage() ...                                                  -- WorldImage
  ... drawTo(WorldScene scene) ...                                    -- WorldScene
  ... getWidth() ...                                                  -- int
  ... getLength() ...                                                 -- int
  ... inWinningState(IList<Exit> exits) ...                           -- boolean
  ... drawFromFile(String filename) ...                               -- WorldImage
  ... registerClick(GridPosn clickPosn) ...                           -- IVehicle
  METHODS ON FIElDS:
  ... this.posn.offset(int dx, int dy) ...                            -- GridPosn
  ... this.posn.drawPositioned(WorldImage im, WorldScene scene) ...   -- WorldScene
   */
  
  // Returns the width of this IVehicle (not accounting for rotation)
  @Override
  public int getWidth() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return 1;
  }
  
  // Returns the length of this IVehicle (not accounting for rotation)
  @Override
  public int getLength() {
    /* TEMPLATE
    Template: Same as class template.
     */
    return 3;
  }
  
  // Returns a WorldImage loaded from a file representing this Truck, with its pinhole
  // centered. If the Truck is selected, returns a yellow version of that WorldImage.
  @Override
  public WorldImage getImage() {
    /* TEMPLATE
    Template: Same as class template.
     */
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
    /* TEMPLATE
    PARAMETERS:
    ... clickPosn ...   -- GridPosn
     */
    boolean active = this.getRect().containsPosn(clickPosn);
    return new Truck(this.posn, this.color, this.isHorizontal, active);
  }
}