import javalib.funworld.WorldScene;
import javalib.worldimages.*;
import java.nio.file.Path;

interface IConstants {
  // The size of each tile, in pixels
  int TILE_SIZE = 64;
  // The random seed used to generate vehicle colors
  int COLOR_SEED = 6;
}

// Represents a Rush Hour game with a specified level
class Game extends javalib.funworld.World {
  Level level;
  
  Game(Level level) {
    this.level = level;
  }
  
  // Performs a Big Bang, with width and height set according to this the width and height in
  // pixels of this Game's level.
  public boolean bigBang() {
    int w = this.level.getWidthPixels();
    int h = this.level.getHeightPixels();
    return this.bigBang(w, h);
  }
  
  // Creates a WorldScene by drawing level to an empty scene.
  @Override
  public WorldScene makeScene() {
    return this.level.draw();
  }
  
  // Handles a mouse click by converting it to a GridPosn and forwarding it to the level.
  @Override
  public Game onMouseClicked(Posn pos) {
    
    // Since we have no other way of handling the x and y elements of a returned
    // Posn, we convert it into our own GridPosn type.
    return new Game(this.level.handleClick(new GridPosn(pos)));
  }
  
  // Handles a key event by forwarding it to the level.
  @Override
  public Game onKeyEvent(String k) {
    return new Game(this.level.handleKey(k));
  }
  
  // determines if the game level has been won.
  @Override
  public boolean shouldWorldEnd() {
    return this.level.hasWon();
  }
  
  // draws the current game, overlaying a "You win!" text (img file) above the game.
  @Override
  public WorldScene lastScene(String msg) {
    return this.level.draw().placeImageXY(
        new SpriteLoader().fromSpritesDir(Path.of("you-win.png")),
        this.level.getWidthPixels() / 2,
        this.level.getHeightPixels() / 2);
  }
}

// Represents a level in the game with a width, height, and layout comprised of vehicles, walls
// and exits.
class Level {
  IList<IMovable> vehicles;
  IList<Wall> walls;
  IList<Exit> exits;
  int width;
  int height;
  
  Level(
      IList<IMovable> vehicles,
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
    if (vehicles.restOrmap(new IntersectsAnyOtherPred<>())) {
      throw new IllegalArgumentException("No vehicle may overlap with another vehicle");
    }
    if (vehicles.ormap(new IntersectsAnyPred<>(walls))) {
      throw new IllegalArgumentException("No vehicle may overlap with a wall");
    }
    
    // Validate that every car is fully contained within the grid
    if (!vehicles.andmap(new InAreaPred<>(new GridArea(
        new GridPosn(),
        new GridPosn().offset(width, height))))) {
      throw new IllegalArgumentException("No vehicle may extend beyond the limits of the grid");
    }
    
    this.vehicles = vehicles;
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
  
  // Renders the game to the provided WorldScene
  WorldScene draw() {
    WorldScene s = new WorldScene(0, 0);
    TiledImage background = new TiledImage(
        new OneSlice(Path.of("grid-cell.png")),
        this.width,
        this.height
    );
    return this.vehicles.foldr(new DrawToScene<>(),
        this.walls.foldr(new DrawToScene<>(),
            this.exits.foldr(new DrawToScene<>(),
                new GridPosn().drawPositioned(background.draw(), s))));
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
    return this.vehicles.andmap(new InWinningStatePred(this.exits));
  }
  
  // Handles a click event at the given GridPosn by selecting any cars that overlap that GridPosn
  // and deselecting any cars that do not
  Level handleClick(GridPosn p) {
    return new Level(
        this.vehicles.map(new OnClick(p)),
        this.walls,
        this.exits,
        this.width,
        this.height
    );
  }
  
  // Handles a key event by registering the event on each of the vehicles in this Level
  Level handleKey(String k) {
    return new Level(
        this.vehicles.map(new OnKey(k, this.walls, this.exits, this.vehicles)),
        this.walls,
        this.exits,
        this.width,
        this.height
    );
  }
}