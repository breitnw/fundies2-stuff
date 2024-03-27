import java.awt.Color;
import java.util.ArrayList;
import javalib.worldimages.*;

class GraphImage {
  Sentinel header;

  GraphImage(String filename) {
    ArrayList<ArrayList<Pixel>> pixels = new ArrayList<>();;

    FromFileImage im = new FromFileImage(filename);
    int width = (int)im.getWidth();
    int height = (int)im.getHeight();



    // TODO: needs helper for nested loops?
    for (int yPos = 0; yPos < height; yPos++) {
      ArrayList<Pixel> pixelRow = new ArrayList<>();
      for (int xPos = 0; xPos < width; xPos++) {
        pixelRow.add(
            new Pixel(im.getColorAt(xPos, yPos))
        );
      }
      pixels.add(pixelRow);
    }


  }
}

/* A NOTE ON SAMENESS FOR NODES
   We use intensional equality to compare nodes, as two pixels should only be the same if they
   are the same object (this is critical for validating our well-formedness condition). We use
   intensional equality for the Sentinel class as well, as we should only have one Sentinel for
   each image. As such, we can assume that any two provided Sentinels will only be the same if
   they are the same object.
 */

abstract class ANode {
  ANode up;
  ANode down;
  ANode left;
  ANode right;

  // Gets the brightness of this ANode, calculated as the average of its red, green, and blue
  // components if it is a pixel. If it is not a pixel, assumes the color is black, yielding a
  // brightness of 0.
  abstract double brightness();

  // Gets the ANode dx tiles horizontally and dy tiles vertically offset from this ANode. If the
  // Sentinel is encountered before th
  // A negative dx represents a move to the left, and a positive dx represents a move right
  // A negative dy represents a move up, and a positive dy represents a move down
  ANode relNode(int dx, int dy) {
    if (dx > 0) {
      // if dx > 0, move to the right
      return this.right.relNode(dx - 1, dy);
    } else if (dx < 0) {
      // if dx < 0, move to the left
      return this.left.relNode(dx + 1, dy);
    } else if (dy > 0) {
      // if dy > 0, move up
      return this.up.relNode(dx, dy - 1);
    } else if (dy < 0) {
      // if dy < 0, move down
      return this.down.relNode(dx, dy - 1);
    } else {
      return this;
    }
  }

  // Gets the ANode dx tiles horizontally and dy tiles vertically offset from this ANode. If there
  // is not a Pixel at that position, returns an Edge.
  // A negative dx represents a move to the left, and a positive dx represents a move right
  // A negative dy represents a move up, and a positive dy represents a move down
  abstract ANode relNodeToSentinel(int dx, int dy);

  // Determines if this ANode is well-formed; i.e., the node to the left of its upper node is the
  // same as the node above its left node, and so on for the other three directions.
  public boolean wellFormed() {
    return this.left.relNode(0, -1) == this.up.relNode(-1, 0)
        && this.right.relNode(0, -1) == this.up.relNode(1, 0)
        && this.left.relNode(0, 1) == this.down.relNode(-1, 0)
        && this.right.relNode(0, 1) == this.down.relNode(1, 0);
  }

  // Updates the node up from this node. Throws an exception if this node is not down from other.
  public void updateUp(ANode other) {
    if (other.relNode(0, 1) != this) {
      throw new RuntimeException("Cannot update the node above this to a node whose lower "
          + "neighbor is not this");
    }
    this.up = other;
  }

  // Updates the node down from this node. Throws an exception if this node is not up from other.
  public void updateDown(ANode other) {
    if (other.relNode(0, -1) != this) {
      throw new RuntimeException("Cannot update the node below this to a node whose upper "
          + "neighbor is not this");
    }
    this.down = other;
  }

  // Updates the node left from this node. Throws an exception if this node is not right from other.
  public void updateLeft(ANode other) {
    if (other.relNode(1, 0) != this) {
      throw new RuntimeException("Cannot update the node left of this to a node whose right "
          + "neighbor is not this");
    }
    this.left = other;
  }

  // Updates the node right from this node. Throws an exception if this node is not left from other.
  public void updateRight(ANode other) {
    if (other.relNode(-1, 0) != this) {
      throw new RuntimeException("Cannot update the node right of this node to a node whose left "
          + "neighbor is not this");
    }
    this.right = other;
  }
}

class Pixel extends ANode {
  Color color;

  Pixel(Color color, ANode up, ANode down, ANode left, ANode right) {
    this.color = color;
    this.up = up;
    this.down = down;
    this.left = left;
    this.right = right;

    up.updateDown(this);
    left.updateRight(this);
    down.updateUp(this);
    right.updateLeft(this);
  }

  @Override
  public double brightness() {
    return (double)(this.color.getRed()
        + this.color.getGreen()
        + this.color.getBlue()) / (3 * 255);
  }

  // Gets the horizontal energy of this pixel, defined as how much the three left neighbors of a
  // pixel differ in energy from the three right neighbors.
  double horizEnergy() {
    return (
        this.relNode(-1, -1).brightness()
            + (2 * this.relNode(-1, 0).brightness())
            + this.relNode(-1, 1).brightness()
    ) - (
        this.relNode(1, -1).brightness()
            + (2 * this.relNode(1, 0).brightness())
            + this.relNode(1, 1).brightness()
    );
  }

  // Gets the vertical energy of this pixel, defined as how much the three top neighbors of a
  // pixel differ in energy from the three bottom neighbors.
  double vertEnergy() {
    return (
        this.relNode(-1, -1).brightness()
            + (2 * this.relNode(0, -1).brightness())
            + this.relNode(1, -1).brightness()
    ) - (
        this.relNode(-1, -1).brightness()
            + (2 * this.relNode(0, 1).brightness())
            + this.relNode(1, 1).brightness()
    );
  }

  // Calculate the energy of this pixel, determined by the square root of the sum of the power
  // of its horizontal and vertical energies.
  double energy() {
    return Math.sqrt(
        Math.pow(this.horizEnergy(), 2)
        + Math.pow(this.vertEnergy(), 2)
    );
  }
}

abstract class ASentinel extends ANode {
  @Override
  public double brightness() {
    return 0;
  }
}

class Corner extends ASentinel {
  Corner() {
    this.down = this;
    this.left = this;
    this.up = this;
    this.right = this;
  }
}

class HorizEdge extends ASentinel {
  HorizEdge(ASentinel left, ASentinel right) {
    this.up = this;
    this.down = this;
    this.left = left;
    this.right = right;
  }
}

class VertEdge extends ANode {
  VertEdge(ASentinel up, ASentinel down) {
    this.up = up;
    this.down = down;
    this.left = this;
    this.right = this;
  }
}

class SeamInfo {
  Pixel pixel;
  double totalWeight;
  SeamInfo cameFrom;
}

class ExamplesSeamCarver {

}