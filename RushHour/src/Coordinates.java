import javalib.funworld.WorldScene;
import javalib.worldimages.Posn;
import javalib.worldimages.WorldImage;

// Represents a position on the grid with integer x and y coordinates.
class GridPosn {
  // The size of each tile, in pixels
  final int TILE_SIZE = 64;
  
  int x;
  int y;
  
  GridPosn(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  // Convenience constructor to create a GridPosn from a GamePosn, dividing both of the
  // coordinates by 64 to convert from pixels to grid coordinates.
  GridPosn(Posn gamePosn) {
    this(gamePosn.x / 64, gamePosn.y / 64); // TODO: can't use the final, workaround maybe?
  }
  
  // Convenience constructor to create a GridPosn with coordinates (0, 0).
  GridPosn() {
    this(0, 0);
  }
  
  // Determines if this GridPosn is at the same x and y coordinates as another Posn
  boolean samePosn(GridPosn other) {
    return this.x == other.x
        && this.y == other.y;
  }
  
  // Offsets this GridPosn by dx horizontally and dy vertically.
  GridPosn offset(int dx, int dy) {
    return new GridPosn(this.x + dx, this.y + dy);
  }
  
  // Draws the provided image to the provided scene at this GridPosn, aligned at the top-left.
  WorldScene drawPositioned(WorldImage im, WorldScene scene) {
    WorldImage imPinholed = im.movePinhole(
        -im.getWidth() / 2,
        -im.getHeight() / 2
    );
    return scene.placeImageXY(imPinholed, this.x * TILE_SIZE, this.y * TILE_SIZE);
  }
  
  // Compares the x-coordinate of this GridPosn to the x-coordinate of the other GridPosn.
  // Returns a negative number if it is less, zero if it is the same, and a positive number if it
  // is greater.
  int compareX(GridPosn other) {
    return this.x - other.x;
  }
  
  // Compares the y-coordinate of this GridPosn to the y-coordinate of the other GridPosn.
  // Returns a negative number if it is less, zero if it is the same, and a positive number if
  // it is greater.
  int compareY(GridPosn other) {
    return this.y - other.y;
  }
}

// Represents a two-dimensional interval on the grid spanning from topLeft (inclusive) to botRight
// (exclusive)
class GridRect {
  GridPosn topLeft;
  GridPosn botRight;
  
  GridRect(GridPosn topLeft, GridPosn botRight) {
    this.topLeft = topLeft;
    this.botRight = botRight;
  }
  
  // Determines if this GridRect intersects that GridRect; i.e., there exists a point that is in
  // both this GridRect and that GridRect.
  boolean intersects(GridRect that) {
    return this.topLeft.compareX(that.botRight) < 0
        && this.topLeft.compareY(that.botRight) < 0
        && this.botRight.compareX(that.topLeft) > 0
        && this.botRight.compareY(that.topLeft) > 0;
  }
  
  // Determines if this GridRect contains that GridRect; i.e., every point in that GridRect is
  // also in this GridRect.
  boolean containsRect(GridRect that) {
    return this.topLeft.compareX(that.topLeft) <= 0
        && this.topLeft.compareY(that.topLeft) <= 0
        && this.botRight.compareX(that.botRight) >= 0
        && this.botRight.compareY(that.botRight) >= 0;
  }
  
  // Determines if this GridRect contains the given Posn between its top left (inclusive) and
  // bottom right (exclusive)
  boolean containsPosn(GridPosn that) {
    return this.topLeft.compareX(that) <= 0
        && this.topLeft.compareY(that) <= 0
        && this.botRight.compareX(that) > 0
        && this.botRight.compareY(that) > 0;
  }
}