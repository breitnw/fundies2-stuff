import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import java.nio.file.Path;

interface IConstants {
  // The size of each tile, in pixels
  int TILE_SIZE = 64;
  // The random seed used to generate vehicle colors
  int COLOR_SEED = 6;
}

// Represents a Rush Hour game with a specified level
class Game extends javalib.impworld.World {
  Level level;
  
  Game(Level level) {
    this.level = level;
  }
  
  // Performs a Big Bang, with width and height set according to this the width and height in
  // pixels of this Game's level.
  public void bigBang() {
    int w = this.level.getWidthPixels();
    int h = this.level.getHeightPixels();
    this.bigBang(w, h);
  }
  
  // Creates a WorldScene by drawing level to an empty scene.
  @Override
  public WorldScene makeScene() {
    return this.level.draw();
  }
  
  // Handles a mouse click by converting it to a GridPosn and forwarding it to the level.
  // EFFECT: mutates the selection of this Game's Level to contain the clicked Movable, if
  // one exists
  @Override
  public void onMouseClicked(Posn pos) {
    // Since we have no other way of handling the x and y elements of a returned
    // Posn, we convert it into our own GridPosn type.
    this.level.handleClick(new GridPosn(pos));
  }
  
  // Handles a key event by forwarding it to the level.
  // EFFECT: mutates the selected car of the Level to move it, if a valid move has been input and
  // is possible
  @Override
  public void onKeyEvent(String k) {
    this.level.handleKey(k);
    if (this.level.hasWon()) {
      this.endOfWorld("");
    }
  }

  // draws the current game, overlaying a "You win!" text (img file) above the game.
  @Override
  public WorldScene lastScene(String msg) {
    WorldScene scene = this.level.draw();
    scene.placeImageXY(
        new SpriteLoader().fromSpritesDir(Path.of("you-win.png")),
        this.level.getWidthPixels() / 2,
        this.level.getHeightPixels() / 2);
    return scene;
  }
}

// Represents a level in the game with a width, height, and layout comprised of vehicles, walls
// and exits.
class Level {
  IList<IMovable> movables;
  IList<Wall> walls;
  IList<Exit> exits;
  int width;
  int height;

  Level(
      IList<IMovable> movables,
      IList<Wall> walls,
      IList<Exit> exits,
      int width,
      int height
  ) {
    // Validate that the grid width is at least 1
    if (width < 1) {
      throw new IllegalArgumentException("Grid width must be greater than zero");
    }
    // Validate that the grid height is at least 1
    if (height < 1) {
      throw new IllegalArgumentException("Grid height must be greater than zero");
    }
    // Validate that no vehicles are overlapping walls or other vehicles. Since our primary
    // method of constructing levels is with Strings, it is impossible for two walls, two exits,
    // or a wall and an exit to overlap. As such, we don't need to validate this case.
    if (movables.restOrmap(new IntersectsAnyOtherPred<>())) {
      throw new IllegalArgumentException("No vehicle may overlap with another vehicle");
    }
    if (movables.ormap(new IntersectsAnyPred<>(walls))) {
      throw new IllegalArgumentException("No vehicle may overlap with a wall");
    }
    
    // Validate that every car is fully contained within the grid
    if (!movables.andmap(new InAreaPred<>(new GridArea(
        new GridPosn(),
        new GridPosn().offset(width, height))))) {
      throw new IllegalArgumentException("No vehicle may extend beyond the limits of the grid");
    }
    
    this.movables = movables;
    this.walls = walls;
    this.exits = exits;
    this.width = width;
    this.height = height;
  }
  
  
  /*
   Convenience constructor to build a level from a layout String. The string should be
   formatted as follows:
   - The letter "T" represents the start of a vertical truck, and the letter "t" represents the
     start of a horizontal truck.
   - The letter "C" represents the start of a vertical car, and the letter "c" represents the
     start of a horizontal car.
   - The letter "P" represents the start of a vertical player car, and the letter "p" represents
     the start of a horizontal player car.
   - The letter "X" represents the exit.
   - The characters "+", "|", and "-" all represent walls.
   - The character " " indicates nothing is in a given cell
   - The newline character "\n" separates rows.
   - All lines of the string (omitting newline characters) must be the same length.

   A level, then, might be laid out similarly to the example below:
   new Level("+------+\n"
           + "|c    T|\n"
           + "|T  T  |\n"
           + "| p    X\n"
           + "|      |\n"
           + "|C   c |\n"
           + "|   t  |\n"
           + "+------+");
   */
  Level(String layout) {
    this(layout, new GridPosn(-1, -1));
  }
  
  // Convenience constructor to build a level from a layout String, with the same format as that
  // used in the previous constructor. The vehicle originating at selection, if there is one, is
  // selected. Primary useful for testing selection methods.
  Level(String layout, GridPosn selection) {
    this(new Parser(layout, selection));
  }
  
  // Convenience constructor that creates a level from the information of a Parser p.
  Level(Parser p) {
    this(p.loadVehicles(), p.loadWalls(), p.loadExits(), p.loadWidth(), p.loadHeight());
  }
  
  // Renders the game to a new WorldScene
  WorldScene draw() {
    WorldScene s = new WorldScene(0, 0);
    TiledImage background = new TiledImage(
        new OneSlice(Path.of("grid-cell.png")),
        this.width,
        this.height
    );
    // Draw the background to the scene
    new GridPosn().drawPositioned(background.draw(), s);
    // Draw the exits, walls and vehicles
    this.exits.forEach(new DrawToScene<>(s));
    this.walls.forEach(new DrawToScene<>(s));
    this.movables.forEach(new DrawToScene<>(s));

    return s;
  }
  
  // Gets the width of this level in pixels
  int getWidthPixels() {
    return this.width * IConstants.TILE_SIZE;
  }
  
  // Gets the height of this level in pixels
  int getHeightPixels() {
    return this.height * IConstants.TILE_SIZE;
  }
  
  // Determines if the grid is in a winning state; i.e., each PlayerCar is overlapping an Exit
  boolean hasWon() {
    return this.movables.andmap(new InWinningStatePred(this.exits));
  }
  
  // Handles a click event at the given GridPosn by selecting any cars that overlap that GridPosn
  // and deselecting any cars that do not
  // EFFECT: selects any cars that overlap the provided GridPosn and deselects any that do not
  void handleClick(GridPosn p) {
    this.movables.forEach(new OnClick(p));
  }
  
  // Handles a key event by registering the event on each of the vehicles in this Level
  // EFFECT: registers the key event on each of this Level's movable objects
  void handleKey(String k) {
    this.movables.forEach(new OnKey(k, walls, exits, movables));
  }
}