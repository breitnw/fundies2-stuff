import javalib.worldimages.*;

import java.nio.file.Path;

// Represents a set of images that can be tiled horizontally and vertically
interface ITileset {
  // Produces a tile depending on whether it is at the top, bottom, left, and/or right of a given tiling.
  WorldImage drawTile(boolean isTop, boolean isBottom, boolean isLeft, boolean isRight);
}

// Represents an image that is tiled horizontally and vertically with itself
class OneSlice implements ITileset {
  WorldImage img;

  OneSlice(WorldImage img) {
    this.img = img;
  }

  // Convenience constructor to create a OneSliced from a filename in the Sprites directory
  OneSlice(Path filename) {
    this(new SpriteLoader().fromSpritesDir(filename));
  }

  // Produces a tile depending on whether it is at the top, bottom, left, and/or right of a given tiling.
  public WorldImage drawTile(boolean isTop, boolean isBottom, boolean isLeft, boolean isRight) {
    return img;
  }
}

// Represents a set of nine sprites that can be used in a nine-sliced TiledImage
class NineSlice implements ITileset {
  WorldImage topLeft, topMid, topRight;
  WorldImage midLeft, midMid, midRight;
  WorldImage botLeft, botMid, botRight;
  
  NineSlice(
      WorldImage topLeft,
      WorldImage topMid,
      WorldImage topRight,
      WorldImage midLeft,
      WorldImage midMid,
      WorldImage midRight,
      WorldImage botLeft,
      WorldImage botMid,
      WorldImage botRight
  ) {
    this.topLeft = topLeft;
    this.topMid = topMid;
    this.topRight = topRight;
    this.midLeft = midLeft;
    this.midMid = midMid;
    this.midRight = midRight;
    this.botLeft = botLeft;
    this.botMid = botMid;
    this.botRight = botRight;
  }

  // Convenience constructor to load a NineSliced from an image filename in the Sprites directory.
  NineSlice(Path filename) {
    WorldImage sheetImg = new SpriteLoader().fromSpritesDir(filename);
    int tileW = (int)sheetImg.getWidth() / 3;
    int tileH = (int)sheetImg.getHeight() / 3;
    
    this.topLeft = new CropImage(0, 0, tileW, tileH, sheetImg);
    this.topMid = new CropImage(tileW, 0, tileW, tileH, sheetImg);
    this.topRight = new CropImage(2 * tileW, 0, tileW, tileH, sheetImg);
    this.midLeft = new CropImage(0, tileH, tileW, tileH, sheetImg);
    this.midMid = new CropImage(tileW, tileH, tileW, tileH, sheetImg);
    this.midRight = new CropImage(2 * tileW, tileH, tileW, tileH, sheetImg);
    this.botLeft = new CropImage(0, 2 * tileH, tileW, tileH, sheetImg);
    this.botMid = new CropImage(tileW, 2 * tileH, tileW, tileH, sheetImg);
    this.botRight = new CropImage(2 * tileW, 2 * tileH, tileW, tileH, sheetImg);
  }

  // Produces a tile depending on whether it is at the top, bottom, left, and/or right of a given
  // tiling. Left takes precedence over right (i.e., if the isLeft and isRight flags are both
  // true the left sprite will be selected), and top takes precedence over bottom.
  public WorldImage drawTile(boolean isTop, boolean isBottom, boolean isLeft, boolean isRight) {
    if (isLeft) {
      if (isTop) {
        return this.topLeft;
      } else if (isBottom) {
        return this.botLeft;
      } else {
        return this.midLeft;
      }
    } else if (isRight) {
      if (isTop) {
        return this.topRight;
      } else if (isBottom) {
        return this.botRight;
      } else {
        return this.midRight;
      }
    } else {
      if (isTop) {
        return this.topMid;
      } else if (isBottom) {
        return this.botMid;
      } else {
        return this.midMid;
      }
    }
  }
}

// Represents images from a tile set tiled an arbitrary number of times horizontally and vertically
class TiledImage {
  ITileset spriteSheet;
  int tilesX, tilesY;
  
  // Creates a TiledImage with a tileset
  TiledImage(ITileset tileset, int tilesX, int tilesY) {
    if (tilesX < 0 || tilesY < 0) {
      throw new IllegalArgumentException("tilesX and tilesY must both be non-negative integers");
    }
    
    this.spriteSheet = tileset;
    this.tilesX = tilesX;
    this.tilesY = tilesY;
  }

  // Draws a WorldImage of `width` tiles next to each other
  WorldImage drawRow(boolean isTop, boolean isBottom) {
    WorldImage row = new EmptyImage();
    for (int col = 0; col < this.tilesX; col += 1) {
      boolean isRight = col == 0;
      boolean isLeft = col == this.tilesX - 1;

      WorldImage curTile = this.spriteSheet.drawTile(isTop, isBottom, isLeft, isRight);
      row = new BesideImage(curTile, row);
    }
    return row;
  }
  
  // Draws this TiledImage as a WorldImage with the pinhole at the top-left
  WorldImage draw() {
    WorldImage img = new EmptyImage();
    for (int row = 0; row < this.tilesY; row += 1) {
      boolean isTop = row == this.tilesY - 1;
      boolean isBottom = row == 0;
      img = new AboveImage(this.drawRow(isTop, isBottom), img);
    }
    return img;
  }
}