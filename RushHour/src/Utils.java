import javalib.worldimages.FromFileImage;
import javalib.worldimages.WorldImage;

import java.nio.file.Path;
import java.util.Random;

// String utils -------------------------------------------------------------------------------------------------------

// Represents a suite of utilities to allow loading a Level from a String. For information on how
// to format level Strings, see the Level convenience constructor.
class Parser {
  String layout;
  GridPosn selection;
  
  Parser(String layout, GridPosn selection) {
    this.layout = layout;
    this.selection = selection;
  }
  
  Parser(String layout) {
    this(layout, new GridPosn(-1, -1));
  }
  
  // Loads a list of IMovables defined by the provided layout String. The vehicle at the same
  // GridPosn as selection, if one exists, is selected.
  IList<IMovable> loadVehicles() {
    return this.loadMovables(
        this.layout,
        0,
        0,
        new Random(IConstants.COLOR_SEED),
        this.selection);
  }
  
  // Loads a list of IMovables defined by the provided layout String, assuming that the first
  // character in the String is at the position defined by curX and curY. The vehicle at the same
  // GridPosn as selection, if one exists, is selected.
  IList<IMovable> loadMovables(
      String restLayout,
      int curX,
      int curY,
      Random rng,
      GridPosn selection) {
    
    if (restLayout.isEmpty()) {
      return new Mt<>();
    }
    
    IList<IMovable> rest;
    if (restLayout.indexOf('\n') == 0) {
      rest = loadMovables(restLayout.substring(1), 0, curY + 1, rng, selection);
    } else {
      rest = loadMovables(restLayout.substring(1), curX + 1, curY, rng, selection);
    }
    
    int color = rng.nextInt(5);
    GridPosn pos = new GridPosn(curX, curY);
    boolean selected = pos.samePosn(selection);
    
    switch (restLayout.charAt(0)) {
      case 'c':
        return new Cons<>(new Car(pos, selected, color, true), rest);
      case 'C':
        return new Cons<>(new Car(pos, selected, color, false), rest);
      case 't':
        return new Cons<>(new Truck(pos, color, selected, true), rest);
      case 'T':
        return new Cons<>(new Truck(pos, color, selected, false), rest);
      case 'p':
        return new Cons<>(new PlayerCar(pos, selected, true), rest);
      case 'P':
        return new Cons<>(new PlayerCar(pos, selected, false), rest);
      case '.':
        return new Cons<>(new NormalBox(pos, selected, 1, 1), rest);
      case 'b':
        return new Cons<>(new NormalBox(pos, selected, 2, 1), rest);
      case 'B':
        return new Cons<>(new NormalBox(pos, selected, 1, 2), rest);
      case 'S':
        return new Cons<>(new PlayerBox(pos, selected, 2, 2), rest);
      default:
        return rest;
    }
  }
  
  // Loads a list of walls defined by the provided layout String
  IList<Wall> loadWalls() {
    return this.loadRemainingWalls(this.layout, 0, 0);
  }
  
  // Loads a list of walls defined by the provided layout String, assuming that the first
  // character in the String is at the position defined by curX and curY
  IList<Wall> loadRemainingWalls(String restLayout, int curX, int curY) {
    if (restLayout.isEmpty()) {
      return new Mt<>();
    }
    
    IList<Wall> rest;
    if (restLayout.indexOf('\n') == 0) {
      rest = loadRemainingWalls(restLayout.substring(1), 0, curY + 1);
    } else {
      rest = loadRemainingWalls(restLayout.substring(1), curX + 1, curY);
    }
    
    if (restLayout.substring(0, 1).matches("[|+-]")) {
      return new Cons<>(new Wall(new GridPosn(curX, curY)), rest);
    } else {
      return rest;
    }
  }
  
  // Loads a list of exits defined by the provided layout String
  IList<Exit> loadExits() {
    return this.loadRemainingExits(this.layout, 0, 0);
  }
  
  // Loads a list of exits defined by the provided layout String, assuming that the first
  // character in the String is at the position defined by curX and curY
  IList<Exit> loadRemainingExits(String restLayout, int curX, int curY) {
    if (restLayout.isEmpty()) {
      return new Mt<Exit>();
    }
    
    IList<Exit> rest;
    if (restLayout.indexOf('\n') == 0) {
      rest = loadRemainingExits(restLayout.substring(1), 0, curY + 1);
    } else {
      rest = loadRemainingExits(restLayout.substring(1), curX + 1, curY);
    }
    
    if (restLayout.charAt(0) == 'X') {
      return new Cons<>(new Exit(new GridPosn(curX, curY)), rest);
    } else {
      return rest;
    }
  }
  
  // Determines the width of the grid defined by layout by returning the index of the first
  // newline character. Throws an exception if any two lines in the grid have different lengths.
  int loadWidth() {
    int maxWidth = 0;
    int curWidth = 0;
    for (int i = 0; i < this.layout.length(); i += 1) {
      String cur = this.layout.substring(i, i + 1);
      if (cur.equals("\n")) {
        curWidth = 0;
      } else {
        curWidth += 1;
        maxWidth = Math.max(maxWidth, curWidth);
      }
    }
    return maxWidth;
  }
  
  // Determines the height of the grid defined by layout by counting the number of newlines in
  // layout.
  int loadHeight() {
    int numNewlines = 0;
    for (int i = 0; i < this.layout.length(); i += 1) {
      String curChar = this.layout.substring(i, i + 1);
      if (curChar.equals("\n")) {
        numNewlines += 1;
      }
    }
    return numNewlines + 1;
  }
}

// Image utils ------------------------------------------------------------------------------------

// Represents a set of utilities related to loading sprites
class SpriteLoader {
  // TODO: needs tests
  // Loads an image from the /sprites directory based on the provided path
  WorldImage fromSpritesDir(Path path) {
    return new FromFileImage(
        Path.of("sprites").resolve(path).toString()
    );
  }
}