import tester.*;                // The tester library
import javalib.worldcanvas.*;
import javalib.worldimages.*;   // images, like RectangleImage or OverlayImages
import javalib.funworld.*;      // the abstract World class and the big-bang library
import java.awt.Color;          // general colors (as triples of red,green,blue values)
// and predefined colors (Color.RED, Color.GRAY, etc.)

interface ILightningBolt {
  // Draws this ILightningBolt using the javalib library
  WorldImage draw();
  
  // Determines whether the downsegment parts of this bolt have at most as much current as its
  // upsegment parts. In other words, the sum of the currents flowing out the tips may never
  // exceed the current at the beginning of the bolt.
  boolean isPhysicallyPossible();
  
  // Determines whether this bolt is physically possible based on the current of a parent bolt;
  // i.e., its current is less than the parent's, and all of its children have a current less
  // than this bolt's.
  boolean isPhysicallyPossibleHelper(int parentCurrent);
  
  // takes the current bolt and a given bolt and produces a Fork using the given arguments, with
  // this bolt on the left and the given bolt on the right. All the angles in this bolt are
  // offset by leftTheta - 90, and all the angles in the other bolt by rightTheta - 90.
  ILightningBolt combine(int leftLength, int rightLength, int leftCapacity, int rightCapacity,
                         double leftTheta, double rightTheta, ILightningBolt otherBolt);
  
  // Returns a new ILightningBolt identical to this, but with its angle and the angle of all of
  // its children offset by theta.
  ILightningBolt offsetAll(double theta);
  
  // returns the width of this bolt, from the leftmost tip to the rightmost tip. Ignores the
  // actual thicknesses of segments or the branches of a fork themselves, and just assumes that
  // the thickness of each bolt is zero.
  double getWidth();
  
  // Gets the offset from this bolt itself (0) to the rightmost point in any of its children. If
  // none of the children contain a point to the right of the bolt itself, returns 0.
  double getRightmostX();
  
  // Gets the offset from this bolt itself (0) to the rightmost point in any of its children. If
  // none of the children contain a point to the right of the bolt itself, returns 0.
  double getLeftmostX();
}

// represents one final endpoint of a lightning bolt
class Tip implements ILightningBolt {
  /* TEMPLATE:
  METHODS:
  ... draw() ...                                      -- WorldImage
  ... isPhysicallyPossible()                          -- boolean
  ... isPhysicallyPossibleHelper(int parentCurrent)   -- boolean
  ... combine(int leftLength, int rightLength,
              int leftCapacity, int rightCapacity,
              double leftTheta, double rightTheta,
              ILightningBolt otherBolt) ...           -- ILightningBolt
  ... offsetAll(double theta) ...                     -- ILightningBolt
  ... getWidth() ...                                  -- double
  ... getRightmostX() ...                             -- double
  ... getLeftmostX() ...                              -- double
   */
  
  // Returns an orange circle to represent this tip in an image.
  public WorldImage draw() {
    return new CircleImage(5, OutlineMode.SOLID, Color.ORANGE);
  }
  
  // Returns true iff this Tip is physically possible. A tip on its own is always physically
  // possible, so true is always returned.
  public boolean isPhysicallyPossible() {
    return true;
  }
  
  // Returns true iff this Tip is physically possible based on the current of its parent. A tip is
  // always physically possible regardless of the parent current, so true is always returned.
  public boolean isPhysicallyPossibleHelper(int parentCurrent) {
    /* TEMPLATE:
    PARAMETERS:
    ... parentCurrent ...   -- int
     */
    return true;
  }
  
  // takes the current bolt and a given bolt and produces a Fork using the given arguments, with
  // this bolt on the left and the given bolt on the right. All the angles in the other bolt are
  // offset by rightTheta - 90.
  public ILightningBolt combine(int leftLength, int rightLength, int leftCapacity,
                                int rightCapacity, double leftTheta, double rightTheta,
                                ILightningBolt otherBolt) {
    /* TEMPLATE:
    PARAMETERS:
    ... leftLength ...                          -- int
    ... rightLength ...                         -- int
    ... leftCapacity ...                        -- int
    ... rightCapacity ...                       -- int
    ... leftTheta ...                           -- double
    ... rightTheta ...                          -- double
    ... otherBolt ...                           -- ILightningBolt
    METHODS ON PARAMETERS:
    ... otherBolt.offsetAll(double theta) ...   -- ILightningBolt
     */
    return new Fork(leftLength, rightLength, leftCapacity, rightCapacity, leftTheta, rightTheta,
      this.offsetAll(leftTheta - 90),
      otherBolt.offsetAll(rightTheta - 90));
  }
  
  // Returns this, since there are no angles to offset.
  public ILightningBolt offsetAll(double theta) {
    /* TEMPLATE:
    PARAMETERS:
    ... theta ...   -- double
     */
    return this;
  }
  
  // Returns the width of this Tip, which is always 0
  public double getWidth() {
    return 0;
  }
  
  // Returns the rightmost X position of this Tip relative to the Tip itself, which is always 0.
  public double getRightmostX() {
    return 0;
  }
  
  // Returns the leftmost X position of this Tip relative to the Tip itself, which is always 0.
  public double getLeftmostX() {
    return 0;
  }
}

// represents a straight section of a lightning bolt
class Segment implements ILightningBolt {
  // How long this piece of the bolt is
  int length;
  // The electric current carried in this part of the bolt, measured in kilo-Amperes
  int current;
  // The angle (in degrees) of this flow, relative to the +x axis
  double theta;
  // The rest of the lightning bolt
  ILightningBolt bolt;
  
  Segment(int length, int current, double theta, ILightningBolt bolt) {
    this.length = length;
    this.current = current;
    this.theta = theta;
    this.bolt = bolt;
  }
  
  /* TEMPLATE:
  FIELDS:
  ... this.length ...                                              -- int
  ... this.current ...                                             -- int
  ... this.theta ...                                               -- double
  ... this.bolt ...                                                -- ILightningBolt
  METHODS:
  ... draw() ...                                                   -- WorldImage
  ... IsPhysicallyPossible() ...                                   -- boolean
  ... isPhysicallyPossibleHelper(int parentCurrent) ...            -- boolean
  ... combine(int leftLength, int rightLength,
              int leftCapacity, int rightCapacity,
              double leftTheta, double rightTheta,
              ILightningBolt otherBolt) ...                        -- ILightningBolt
  ... offsetAll(double theta) ...                                  -- ILightningBolt
  ... getWidth() ...                                               -- double
  ... getRightmostX() ...                                          -- double
  ... getLeftmostX() ...                                           -- double
  METHODS ON FIELDS:
  ... this.bolt.draw() ...                                         -- WorldImage
  ... this.bolt.isPhysicallyPossibleHelper(int parentCurrent)...   -- boolean
  ... this.bolt.offsetAll(double theta)...                         -- ILightningBolt
  ... this.bolt.getRightmostX()...                                 -- double
  ... this.bolt.getLeftmostX()...                                  -- double
   */
  
  // Draws this Segment as a red rectangle using the javalib library, angled this.theta degrees
  // from the horizontal, with this.bolt rendered at the tip and the pinhole placed at the base.
  public WorldImage draw() {
    WorldImage segRect = new RectangleImage(this.length, 5, OutlineMode.SOLID, Color.RED)
                           .movePinholeTo(new Posn(-this.length / 2, 0));
    
    // Rotate rest by -theta so that the final rotation by theta returns it to its original
    // position. This could be avoided by rotating segRectPinholed earlier, but then we would
    // need to use trig to reposition the pinhole.
    WorldImage rest = new RotateImage(this.bolt.draw(), -this.theta);
    
    // Overlay rest on top of segRect
    WorldImage overlay = new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE,
        rest, 0, 0, segRect);
    
    // Move the pinhole to the base of the segment
    WorldImage overlayPinholed = overlay.movePinhole(this.length, 0);
    // Rotate the segment and return
    return new RotateImage(overlayPinholed, this.theta);
  }
  
  // Returns true iff this Segment is physically possible; i.e., its child has a current less than
  // or equal to the Segment itself, and itself is physically possible.
  public boolean isPhysicallyPossible() {
    return this.bolt.isPhysicallyPossibleHelper(this.current);
  }
  
  // Returns true iff this Segment is physically possible based on the current of its parent. For
  // this to be true, its current must be less than parentCurrent, and its child must be physically
  // possible
  public boolean isPhysicallyPossibleHelper(int parentCurrent) {
    /* TEMPLATE:
    PARAMETERS:
    ... parentCurrent ...   -- int
     */
    return this.current <= parentCurrent && this.bolt.isPhysicallyPossibleHelper(this.current);
  }
  
  // takes the current bolt and a given bolt and produces a Fork using the given arguments, with
  // this bolt on the left and the given bolt on the right. All the angles in this bolt are
  // offset by leftTheta - 90, and all the angles in the other bolt by rightTheta - 90.
  public ILightningBolt combine(int leftLength, int rightLength, int leftCapacity,
                                int rightCapacity, double leftTheta, double rightTheta,
                                ILightningBolt otherBolt) {
    /* TEMPLATE:
    PARAMETERS:
    ... leftLength ...                          -- int
    ... rightLength ...                         -- int
    ... leftCapacity ...                        -- int
    ... rightCapacity ...                       -- int
    ... leftTheta ...                           -- double
    ... rightTheta ...                          -- double
    ... otherBolt ...                           -- ILightningBolt
    METHODS ON PARAMETERS:
    ... otherBolt.offsetAll(double theta) ...   -- ILightningBolt
     */
    return new Fork(leftLength, rightLength, leftCapacity, rightCapacity, leftTheta, rightTheta,
      this.offsetAll(leftTheta - 90),
      otherBolt.offsetAll(rightTheta - 90));
  }
  
  // Returns a new Segment whose theta, and all angles of children, are offset by the given theta.
  public ILightningBolt offsetAll(double theta) {
    /* TEMPLATE:
    ... theta ...   -- double
     */
    return new Segment(this.length, this.current, this.theta + theta,
      this.bolt.offsetAll(theta));
  }
  
  // Returns the horizontal distance between the leftmost point of this Segment (including
  // children) to its rightmost point.
  public double getWidth() {
    return Math.abs(this.getRightmostX() - this.getLeftmostX());
  }
  
  // Returns the rightmost X position of this Segment relative to the Segment itself, which is the
  // maximum of the following two values:
  // - 0 (the relative position of the Fork itself),
  // - the tip of this Segment, plus the rightmost point after it.
  public double getRightmostX() {
    return Math.max(0,
      this.length * Math.cos(Math.toRadians(this.theta)) + this.bolt.getRightmostX());
  }
  
  // Returns the leftmost X position of this Segment relative to the Segment itself, which is the
  // minimum of the following two values:
  // - 0 (the relative position of the Fork itself),
  // - the tip of this Segment, plus the leftmost point after it.
  public double getLeftmostX() {
    return Math.min(0,
      this.length * Math.cos(Math.toRadians(this.theta)) + this.bolt.getLeftmostX());
  }
}

// represents the lightning bolt splitting in two
class Fork implements ILightningBolt {
  // How long the left and right branches are
  int leftLength;
  int rightLength;
  // The electric current in each part of the bolt, measured in kilo-Amperes
  int leftCurrent;
  int rightCurrent;
  // The angle (in degrees) of the two branches, relative to the +x axis,
  double leftTheta;
  double rightTheta;
  // The remaining parts of the lightning bolt
  ILightningBolt left;
  ILightningBolt right;
  
  Fork(int leftLength, int rightLength, int leftCurrent, int rightCurrent, double leftTheta,
       double rightTheta, ILightningBolt left, ILightningBolt right) {
    this.leftLength = leftLength;
    this.rightLength = rightLength;
    this.leftCurrent = leftCurrent;
    this.rightCurrent = rightCurrent;
    this.leftTheta = leftTheta;
    this.rightTheta = rightTheta;
    this.left = left;
    this.right = right;
  }
  /* TEMPLATE:
  FIELDS:
  ... this.leftLength ...                                          -- int
  ... this.rightLength ...                                         -- int
  ... this.leftCurrent ...                                         -- int
  ... this.rightCurrent ...                                        -- int
  ... this.leftTheta ...                                           -- double
  ... this.rightTheta ...                                          -- double
  ... this.left ...                                                -- ILightningBolt
  ... this.right ...                                               -- ILightningBolt
  METHODS:
  ... draw() ...                                                   -- WorldImage
  ... isPhysicallyPossible() ...                                   -- boolean
  ... isPhysicallyPossibleHelper(int parentCurrent) ...            -- boolean
  ... combine(int leftLength, int rightLength,
              int leftCapacity, int rightCapacity,
              double leftTheta, double rightTheta,
              ILightningBolt otherBolt) ...                        -- ILightningBolt
  ... offsetAll(double theta) ...                                  -- ILightningBolt
  ... getWidth() ...                                               -- double
  ... getRightmostX() ...                                          -- double
  ... getLeftmostX() ...                                           -- double
  METHODS ON FIELDS:
  ... this.left.draw() ...                                         -- WorldImage
  ... this.right.draw() ...                                        -- WorldImage
  ... this.left.isPhysicallyPossibleHelper(int parentCurrent) ...  -- boolean
  ... this.right.isPhysicallyPossibleHelper(int parentCurrent) ... -- boolean
  ... this.left.offsetAll(double theta) ...                        -- ILightningBolt
  ... this.right.offsetAll(double theta) ...                       -- ILightningBolt
  ... this.left.getRightmostX() ...                                -- double
  ... this.right.getRightmostX() ...                               -- double
  ... this.left.getLeftmostX() ...                                 -- double
  ... this.right.getLeftmostX() ...                                -- double
   */
  
  // Draws this Fork as a red rectangle using the javalib library, with each branch angled
  // according to the corresponding theta, the corresponding bolts rendered at their tips and the
  // pinhole placed at the shared base.
  public WorldImage draw() {
    WorldImage leftRect = new RectangleImage(this.leftLength, 5, OutlineMode.SOLID, Color.RED)
                               .movePinholeTo(new Posn(-this.leftLength / 2, 0));
    WorldImage rightRect = new RectangleImage(this.rightLength, 5, OutlineMode.SOLID, Color.RED)
                                .movePinholeTo(new Posn(-this.rightLength / 2, 0));
    
    // Rotate remaining parts of the branch by -theta so that the final rotation by theta returns it
    // to its original position.
    WorldImage leftRest = new RotateImage(this.left.draw(), -this.leftTheta);
    WorldImage rightRest = new RotateImage(this.right.draw(), -this.rightTheta);
    
    // Overlay the remaining parts on top of the rects
    WorldImage leftOverlay = new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE,
        leftRest, 0, 0, leftRect);
    WorldImage rightOverlay = new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE,
        rightRest, 0, 0, rightRect);
    
    // Move the pinholes to the base of the branches
    WorldImage lOverlayPinholed = leftOverlay.movePinhole(this.leftLength, 0);
    WorldImage rOverlayPinholed = rightOverlay.movePinhole(this.rightLength, 0);
    
    // Combine the left and right branches and return
    return new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE,
      new RotateImage(lOverlayPinholed, this.leftTheta), 0, 0,
      new RotateImage(rOverlayPinholed, this.rightTheta));
  }
  
  // Returns true iff this Fork is physically possible; i.e., each the children of each of its
  // branches have a current less than or equal to the branch itself, and themselves are
  // physically possible.
  public boolean isPhysicallyPossible() {
    return this.left.isPhysicallyPossibleHelper(this.leftCurrent)
      && this.right.isPhysicallyPossibleHelper(this.rightCurrent);
  }
  
  // Returns true iff this Fork is physically possible based on the current of its parent. For this
  // to be true, the sum of the currents of its branches must be less than parentCurrent, and the
  // children of its branches must be physically possible.
  public boolean isPhysicallyPossibleHelper(int parentCurrent) {
    /* TEMPLATE:
    PARAMETERS:
    ... parentCurrent ...   -- int
     */
    return this.leftCurrent + this.rightCurrent <= parentCurrent
      && this.left.isPhysicallyPossibleHelper(this.leftCurrent)
      && this.right.isPhysicallyPossibleHelper(this.rightCurrent);
  }
  
  // takes the current bolt and a given bolt and produces a Fork using the given arguments, with
  // this bolt on the left and the given bolt on the right. All the angles in this bolt are
  // offset by leftTheta - 90, and all the angles in the other bolt by rightTheta - 90.
  public ILightningBolt combine(int leftLength, int rightLength, int leftCapacity,
                                int rightCapacity, double leftTheta, double rightTheta,
                                ILightningBolt otherBolt) {
    /* TEMPLATE:
    PARAMETERS:
    ... leftLength ...                      -- int
    ... rightLength ...                     -- int
    ... leftCapacity ...                    -- int
    ... rightCapacity ...                   -- int
    ... leftTheta ...                       -- double
    ... rightTheta ...                      -- double
    ... otherBolt ...                       -- ILightningBolt
    METHODS ON PARAMETERS:
    ... otherBolt.offsetAll(double theta)   -- ILightningBolt
     */
    return new Fork(leftLength, rightLength, leftCapacity, rightCapacity, leftTheta, rightTheta,
      this.offsetAll(leftTheta - 90),
      otherBolt.offsetAll(rightTheta - 90));
  }
  
  // Returns a new Fork whose left and right theta, and all angles of children, are offset by
  // the given theta.
  public ILightningBolt offsetAll(double theta) {
    /* TEMPLATE:
    PARAMETERS:
    ... theta ...   -- double
     */
    return new Fork(this.leftLength, this.rightLength, this.leftCurrent, this.rightCurrent,
      this.leftTheta + theta, this.rightTheta + theta,
      this.left.offsetAll(theta), this.right.offsetAll(theta));
  }
  
  // Returns the horizontal distance between the leftmost point of this Fork (including children)
  // to its rightmost point.
  public double getWidth() {
    return Math.abs(this.getRightmostX() - this.getLeftmostX());
  }
  
  // Returns the rightmost X position of this Fork relative to the Fork itself, which is the
  // maximum of the following three values:
  // - 0 (the relative position of the Fork itself),
  // - the tip of the left branch of the Fork, plus the rightmost point after that branch
  // - the tip of the right branch of the Fork, plus the rightmost point after that branch
  public double getRightmostX() {
    double lRightmost =
        this.leftLength * Math.cos(Math.toRadians(this.leftTheta)) + this.left.getRightmostX();
    double rRightmost =
        this.rightLength * Math.cos(Math.toRadians(this.rightTheta)) + this.right.getRightmostX();
    return Math.max(0, Math.max(lRightmost, rRightmost));
  }
  
  // Returns the leftmost X position of this Fork relative to the Fork itself, which is the
  // minimum of the following three values:
  // - 0 (the relative position of the Fork itself),
  // - the tip of the left branch of the Fork, plus the leftmost point after that branch
  // - the tip of the right branch of the Fork, plus the leftmost point after that branch
  public double getLeftmostX() {
    double lLeftmost =
        this.leftLength * Math.cos(Math.toRadians(this.leftTheta)) + this.left.getLeftmostX();
    double rLeftmost =
        this.rightLength * Math.cos(Math.toRadians(this.rightTheta)) + this.right.getLeftmostX();
    return Math.min(0, Math.min(lLeftmost, rLeftmost));
  }
}

class ExamplesLightning {
  ILightningBolt tip = new Tip();
  ILightningBolt fork1 = new Fork(33, 30, 10, 25, 135, 40, new Tip(), new Tip());
  ILightningBolt fork2 = new Fork(30, 22, 20, 10, 115, 65, new Tip(), new Tip());
  ILightningBolt seg1 = new Segment(40, 20, 130, fork1); // Not plausible!
  ILightningBolt seg2 = new Segment(50, 30, 77, fork2); // Plausible!
  ILightningBolt seg3 = new Segment(50, 29, 77, fork2); // Not plausible!
  
  ILightningBolt forked = new Fork(40, 50, 30, 35, 150, 30, seg2, seg2);
  
  boolean testDrawBolt(Tester t) {
    WorldCanvas c = new WorldCanvas(500, 500);
    WorldScene s = new WorldScene(500, 500);

    return c.drawScene(s.placeImageXY(forked.draw(), 250, 250))
             && c.show();
  }
  
  boolean testDrawTip(Tester t) {
    WorldImage expected = new CircleImage(5, OutlineMode.SOLID, Color.ORANGE);
    return t.checkExpect(tip.draw(), expected);
  }
  
  boolean testDrawFork(Tester t) {
    WorldImage leftRect = new RectangleImage(33, 5, OutlineMode.SOLID, Color.RED)
                            .movePinholeTo(new Posn(-16, 0));
    WorldImage rightRect = new RectangleImage(30, 5, OutlineMode.SOLID, Color.RED)
                             .movePinholeTo(new Posn(-15, 0));
    WorldImage leftRest = new RotateImage(tip.draw(), -135);
    WorldImage rightRest = new RotateImage(tip.draw(), -40);
    WorldImage leftOverlay = new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE,
        leftRest, 0, 0, leftRect);
    WorldImage rightOverlay = new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE,
        rightRest, 0, 0, rightRect);
    WorldImage lOverlayPinholed = leftOverlay.movePinhole(33, 0);
    WorldImage rOverlayPinholed = rightOverlay.movePinhole(30, 0);
    
    WorldImage expected = new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE,
        new RotateImage(lOverlayPinholed, 135), 0, 0,
        new RotateImage(rOverlayPinholed, 40));
    
    return t.checkExpect(fork1.draw(), expected);
  }
  
  boolean testDrawSegment(Tester t) {
    WorldImage segRect = new RectangleImage(40, 5, OutlineMode.SOLID, Color.RED)
                           .movePinholeTo(new Posn(-40 / 2, 0));
    WorldImage rest = new RotateImage(fork1.draw(), -130);
    WorldImage overlay = new OverlayOffsetAlign(AlignModeX.PINHOLE, AlignModeY.PINHOLE,
        rest, 0, 0, segRect);
    WorldImage overlayPinholed = overlay.movePinhole(40, 0);
    
    WorldImage expected = new RotateImage(overlayPinholed, 130);
    
    return t.checkExpect(seg1.draw(), expected);
  }
  
  boolean testIsPhysicallyPossible1(Tester t) {
    return t.checkExpect(seg1.isPhysicallyPossible(), false);
  }
  
  boolean testIsPhysicallyPossible2(Tester t) {
    return t.checkExpect(seg2.isPhysicallyPossible(), true);
  }
  
  boolean testIsPhysicallyPossible3(Tester t) {
    return t.checkExpect(forked.isPhysicallyPossible(), true);
  }
  
  boolean testIsPhysicallyPossible4(Tester t) {
    return t.checkExpect(seg3.isPhysicallyPossible(), false);
  }
  
  boolean testIsPhysicallyPossibleHelper(Tester t) {
    return t.checkExpect(tip.isPhysicallyPossibleHelper(0), true)
        && t.checkExpect(seg1.isPhysicallyPossibleHelper(20), false)
        && t.checkExpect(forked.isPhysicallyPossibleHelper(70), true);
  }
  
  boolean testOffsetAll(Tester t) {
    return t.checkExpect(tip.offsetAll(30), tip)
        && t.checkExpect(fork1.offsetAll(30),
            new Fork(33, 30, 10, 25,165, 70, tip, tip))
        && t.checkExpect(seg1.offsetAll(30), new Segment(40, 20, 160, fork1.offsetAll(30)));
  }
  
  boolean testCombine1(Tester t) {
    int lAngleOffset = 150 - 90;
    int rAngleOffset = 30 - 90;
    
    ILightningBolt combined = seg1.combine(40, 50, 50, 40, 150, 30, seg2);
    
    return t.checkExpect(combined,
      new Fork(40, 50, 50, 40, 150, 30,
        new Segment(40, 20, 130 + lAngleOffset,
          new Fork(33, 30, 10, 25, 135 + lAngleOffset, 40 + lAngleOffset, new Tip(), new Tip())),
        new Segment(50, 30, 77 + rAngleOffset,
          new Fork(30, 22, 20, 10, 115 + rAngleOffset, 65 + rAngleOffset,
            new Tip(), new Tip()))));
  }
  
  boolean testCombine2(Tester t) {
    ILightningBolt shortSeg1 = new Segment(20, 49, 22, new Tip());
    ILightningBolt shortSeg2 = new Tip();
    ILightningBolt combined = shortSeg1.combine(40, 50, 50, 40, 90, 90, shortSeg2);
    
    return t.checkExpect(combined,
      new Fork(40, 50, 50, 40, 90, 90,
        shortSeg1,
        shortSeg2));
  }
  
  boolean testCombine3(Tester t) {
    int lAngleOffset = 150 - 90;
    int rAngleOffset = 30 - 90;
    
    ILightningBolt combined = fork1.combine(40, 50, 50, 40, 150, 30, fork2);
    
    return t.checkExpect(combined,
      new Fork(40, 50, 50, 40, 150, 30,
        new Fork(33, 30, 10, 25, 135 + lAngleOffset, 40 + lAngleOffset,
          new Tip(),
          new Tip()),
        new Fork(30, 22, 20, 10, 115 + rAngleOffset, 65 + rAngleOffset,
          new Tip(),
          new Tip())));
  }
  
  boolean testCombine4(Tester t) {
    ILightningBolt combined = fork1.combine(40, 50, 50, 40, 90, 90, fork2);
    
    return t.checkExpect(combined,
      new Fork(40, 50, 50, 40, 90, 90,
        new Fork(33, 30, 10, 25, 135, 40,
          new Tip(),
          new Tip()),
        new Fork(30, 22, 20, 10, 115, 65,
          new Tip(),
          new Tip())));
  }
  
  boolean testGetWidth1(Tester t) {
    return t.checkInexact(tip.getWidth(), 0.0, 0.001);
  }

  boolean testGetWidth2(Tester t) {
    ILightningBolt straightSegment = new Segment(40, 20, 80, tip);
    return t.checkInexact(straightSegment.getWidth(), 40 * Math.cos(Math.toRadians(80)), 0.001);
  }

  boolean testGetWidth3(Tester t) {
    return t.checkInexact(
      fork1.getWidth(),
      30 * Math.cos(Math.toRadians(40)) - 33 * Math.cos(Math.toRadians(135)), 0.001);
  }

  boolean testGetWidth4(Tester t) {
    // left and right swapped
    ILightningBolt bolt = new Fork(30, 33, 10, 25, 40, 135, new Tip(), new Tip());
    return t.checkInexact(bolt.getWidth(),
      30 * Math.cos(Math.toRadians(40)) - 33 * Math.cos(Math.toRadians(135)), 0.001);
  }
  
  boolean testGetRightmostX(Tester t) {
    return t.checkInexact(tip.getRightmostX(), 0.0, 0.001)
        && t.checkInexact(fork1.getRightmostX(), 22.9813, 0.001)
        && t.checkInexact(seg1.getRightmostX(), 0.0, 0.001);
  }
  
  boolean testGetLeftmostX(Tester t) {
    return t.checkInexact(tip.getLeftmostX(), 0.0, 0.001)
        && t.checkInexact(fork1.getLeftmostX(), -23.334, 0.001)
        && t.checkInexact(seg1.getLeftmostX(), -49.046, 0.001);
  }
}