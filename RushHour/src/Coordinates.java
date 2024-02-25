import javalib.funworld.WorldScene;
import javalib.worldimages.*;

// Represents a position on the grid with integer x and y coordinates.
class GridPosn {
  int x;
  int y;
  
  GridPosn(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  // Convenience constructor to create a GridPosn from a GamePosn, dividing both of the
  // coordinates by tileSize to convert from pixels to grid coordinates.
  GridPosn(Posn gamePosn, int tileSize) {
    this(gamePosn.x / tileSize, gamePosn.y / tileSize);
  }
  
  // Convenience constructor to create a GridPosn with coordinates (0, 0).
  GridPosn() {
    this(0, 0);
  }
  
  /* TEMPLATE:
  FIELDS:
  ... TILE_SIZE ...                                         -- int
  ... this.x ...                                            -- int
  ... this.y ...                                            -- int
  METHODS:
  ... samePosn(GridPosn other) ...                          -- boolean
  ... offset(int dx, int dy) ...                            -- GridPosn
  ... drawPositioned(WorldImage im, WorldScene scene) ...   -- WorldScene
  ... compareX(GridPosn other) ...                          -- int
  ... compareY(GridPosn other) ...                          -- int
   */
  
  // Determines if this GridPosn is at the same x and y coordinates as another Posn
  boolean samePosn(GridPosn other) {
    /* TEMPLATE
    PARAMETERS:
    ... other ...     -- GridPosn
    FIELDS ON PARAMETERS:
    ... other.x ...   -- int
    ... other.y ...   -- int
     */
    return this.x == other.x
        && this.y == other.y;
  }
  
  // Offsets this GridPosn by dx horizontally and dy vertically.
  GridPosn offset(int dx, int dy) {
    /* TEMPLATE
    PARAMETERS:
    ... dx ...   -- int
    ... dy ...   -- int
     */
    return new GridPosn(this.x + dx, this.y + dy);
  }
  
  // Draws the provided image to the provided scene at this GridPosn, aligned at the top-left.
  WorldScene drawPositioned(WorldImage im, WorldScene scene, int tileSize) {
    /* TEMPLATE:
    PARAMETERS:
    ... im ...                                         -- WorldImage
    ... scene ...                                      -- WorldScene
    ... tileSize ...                                   -- int
    METHODS ON PARAMETERS:
    ... im.movePinhole(int, int) ...                   -- WorldImage
    ... im.getWidth() ...                              -- int
    ... im.getHeight() ...                             -- int
    ... scene.placeImageXY(WorldImage, int, int) ...   -- WorldScene
     */
    WorldImage imPinholed = im.movePinhole(
        -im.getWidth() / 2,
        -im.getHeight() / 2
    );
    return scene.placeImageXY(imPinholed, this.x * tileSize, this.y * tileSize);
  }
  
  // Compares the x-coordinate of this GridPosn to the x-coordinate of the other GridPosn.
  // Returns a negative number if it is less, zero if it is the same, and a positive number if it
  // is greater.
  int compareX(GridPosn other) {
    /* TEMPLATE
    PARAMETERS:
    ... other ...     -- GridPosn
    FIELDS ON PARAMETERS:
    ... other.x ...   -- int
     */
    return this.x - other.x;
  }
  
  // Compares the y-coordinate of this GridPosn to the y-coordinate of the other GridPosn.
  // Returns a negative number if it is less, zero if it is the same, and a positive number if
  // it is greater.
  int compareY(GridPosn other) {
    /* TEMPLATE
    PARAMETERS:
    ... other ...     -- GridPosn
    FIELDS ON PARAMETERS:
    ... other.y ...   -- int
     */
    return this.y - other.y;
  }
}

// Represents a two-dimensional interval on the grid spanning from topLeft (inclusive) to botRight
// (exclusive)
class GridRect {
  GridPosn topLeft;
  GridPosn botRight;
  
  GridRect(GridPosn topLeft, GridPosn botRight) {
    if (topLeft.compareX(botRight) > 0 || topLeft.compareY(botRight) > 0) {
      throw new IllegalArgumentException("Cannot construct a GridRect with topLeft to the right "
                                             + "of or below botRight");
    }
    this.topLeft = topLeft;
    this.botRight = botRight;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.topLeft ...                             -- GridPosn
  ... this.botRight ...                            -- GridPosn
  METHODS:
  ... intersects(GridRect that) ...                -- boolean
  ... containsRect(GridRect that) ...              -- boolean
  ... containsPosn(GridPosn that) ...              -- boolean
  METHODS ON PARAMETERS:
  ... this.topLeft.compareX(GridPosn other) ...    -- int
  ... this.topLeft.compareY(GridPosn other) ...    -- int
  ... this.botRight.compareX(GridPosn other) ...   -- int
  ... this.botRight.compareY(GridPosn other) ...   -- int
  */
  
  // Determines if this GridRect intersects that GridRect; i.e., there exists a point that is in
  // both this GridRect and that GridRect.
  boolean intersects(GridRect that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...            -- GridRect
    FIELDS ON PARAMETERS:
    ... that.botRight ...   -- GridPosn
    ... that.topLeft ...    -- GridPosn
     */
    return this.topLeft.compareX(that.botRight) < 0
        && this.topLeft.compareY(that.botRight) < 0
        && this.botRight.compareX(that.topLeft) > 0
        && this.botRight.compareY(that.topLeft) > 0;
  }
  
  // Determines if this GridRect contains that GridRect; i.e., every point in that GridRect is
  // also in this GridRect.
  boolean containsRect(GridRect that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...            -- GridRect
    FIELDS ON PARAMETERS:
    ... that.topLeft ...    -- GridPosn
    ... that.botRight ...   -- GridPosn
     */
    return this.topLeft.compareX(that.topLeft) <= 0
        && this.topLeft.compareY(that.topLeft) <= 0
        && this.botRight.compareX(that.botRight) >= 0
        && this.botRight.compareY(that.botRight) >= 0;
  }
  
  // Determines if this GridRect contains the given Posn between its top left (inclusive) and
  // bottom right (exclusive)
  boolean containsPosn(GridPosn that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- GridPosn
     */
    return this.topLeft.compareX(that) <= 0
        && this.topLeft.compareY(that) <= 0
        && this.botRight.compareX(that) > 0
        && this.botRight.compareY(that) > 0;
  }
}