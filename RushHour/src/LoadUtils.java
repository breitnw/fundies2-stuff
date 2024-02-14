// Represents a suite of utilities to allow loading a Level from a String
// TODO: show how to format the strings
class LoadUtils {
  // Loads a list of AVehicles defined by the provided layout String, assuming that the first
  // character in the String is at the position defined by curX and curY
  IList<AVehicle> loadVehicles(String layout, int curX, int curY) {
    if (layout.isEmpty()) {
      return new Mt<AVehicle>();
    }
    
    IList<AVehicle> rest;
    if (layout.indexOf('\n') == 0) {
      rest = loadVehicles(layout.substring(1), 0, curY + 1);
    } else {
      rest = loadVehicles(layout.substring(1), curX + 1, curY);
    }
    
    switch (layout.charAt(0)) {
      case 'c':
        return new Cons<>(new Car(new GridPosn(curX, curY), "purple", true), rest);
      case 'C':
        return new Cons<>(new Car(new GridPosn(curX, curY), "purple", false), rest);
      case 't':
        return new Cons<>(new Truck(new GridPosn(curX, curY), "purple", true), rest);
      case 'T':
        return new Cons<>(new Truck(new GridPosn(curX, curY), "purple", false), rest);
      case 'p':
        return new Cons<>(new PlayerCar(new GridPosn(curX, curY), true), rest);
      case 'P':
        return new Cons<>(new PlayerCar(new GridPosn(curX, curY), false), rest);
      default:
        return rest;
    }
  }
  
  // Loads a list of walls defined by the provided layout String, assuming that the first
  // character in the String is at the position defined by curX and curY
  IList<Wall> loadWalls(String layout, int curX, int curY) {
    if (layout.isEmpty()) {
      return new Mt<Wall>();
    }
    
    IList<Wall> rest;
    if (layout.indexOf('\n') == 0) {
      rest = loadWalls(layout.substring(1), 0, curY + 1);
    } else {
      rest = loadWalls(layout.substring(1), curX + 1, curY);
    }
    
    if (layout.substring(0, 1).matches("[|+-]")) {
      return new Cons<>(new Wall(new GridPosn(curX, curY)), rest);
    } else {
      return rest;
    }
  }
  
  // Loads a list of exits defined by the provided layout String, assuming that the first
  // character in the String is at the position defined by curX and curY
  IList<Exit> loadExits(String layout, int curX, int curY) {
    if (layout.isEmpty()) {
      return new Mt<Exit>();
    }
    
    IList<Exit> rest;
    if (layout.indexOf('\n') == 0) {
      rest = loadExits(layout.substring(1), 0, curY + 1);
    } else {
      rest = loadExits(layout.substring(1), curX + 1, curY);
    }
    
    if (layout.charAt(0) == 'X') {
      return new Cons<>(new Exit(new GridPosn(curX, curY)), rest);
    } else {
      return rest;
    }
  }
  
  // Determines the width of the grid defined by layout by returning the index of the first
  // newline character. Throws an exception if any two lines in the grid have different lengths.
  int loadWidth(String layout) {
    if (!this.hasSameLineLengths(layout, -1)) {
      throw new IllegalArgumentException("The length of each line in the provided layout " +
                                             "(excluding newline characters) should be equal");
    }
    int newlineIndex = layout.indexOf('\n');
    if (newlineIndex == -1) {
      return layout.length();
    } else {
      return newlineIndex;
    }
  }
  
  // Determines whether all the lines of s have length targetLength. If targetLength is -1,
  // determines whether every line has the same length as the first line.
  boolean hasSameLineLengths(String s, int targetLength) {
    int newlineIndex = s.indexOf('\n');
    if (targetLength == -1) {
      return this.hasSameLineLengths(s.substring(newlineIndex + 1), newlineIndex);
    } else if (newlineIndex == -1) {
      return s.length() == targetLength;
    } else {
      return newlineIndex == targetLength
                 && this.hasSameLineLengths(s.substring(newlineIndex + 1), targetLength);
    }
  }
  
  // Determines the height of the grid defined by layout by counting the number of newlines in
  // layout.
  int loadHeight(String layout) {
    int newlineIndex = layout.indexOf('\n');
    if (newlineIndex == -1) {
      return 1;
    } else {
      return 1 + loadHeight(layout.substring(newlineIndex + 1));
    }
  }
}