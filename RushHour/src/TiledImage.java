import javalib.worldimages.AboveImage;
import javalib.worldimages.BesideImage;
import javalib.worldimages.WorldImage;

// Represents a WorldImage tiled horizontally and vertically
public class TiledImage {
  WorldImage image;
  int tilesX;
  int tilesY;
  
  TiledImage(WorldImage image, int tilesX, int tilesY) {
    if (tilesX < 1 || tilesY < 1) {
      throw new IllegalArgumentException("tilesX and tilesY must both be positive integers");
    }
    this.image = image;
    this.tilesX = tilesX;
    this.tilesY = tilesY;
  }
  
  // Draws a WorldImage of `width` tiles next to each other
  WorldImage drawRow(int width) {
    if (width > 1) {
      return new BesideImage(image, drawRow(width - 1));
    } else {
      return image;
    }
  }
  
  // Draws a WorldImage of `height` rows on top of each other
  WorldImage drawRows(int height) {
    WorldImage row = this.drawRow(this.tilesX);
    if (height > 1) {
      return new AboveImage(row, drawRows(height - 1));
    } else {
      return row;
    }
  }
  
  // Draws this TiledImage as a WorldImage with the pinhole at the top-left
  WorldImage draw() {
    return this.drawRows(this.tilesY);
  }
}
