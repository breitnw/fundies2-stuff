import javalib.worldimages.WorldImage;
import tester.Tester;

interface ILightningBolt {
  // Draws this ILightningBolt using the javalib library
  WorldImage draw();
  // Determines whether the downsegment parts of this bolt have at most as much current as its
  // upsegment parts. In other words, the sum of the currents flowing out the tips may never
  // exceed the current at the beginning of the bolt.
  boolean isPhysicallyPossible();
  boolean isPhysicallyPossibleHelper(int parentCurrent);
  ILightningBolt combine(int leftLength, int rightLength, int leftCapacity, int rightCapacity,
                         double leftTheta, double rightTheta, ILightningBolt otherBolt);
  
  // Returns a new ILightningBolt identical to this, but with its angle and the angle of all of
  // its children offset by theta.
  ILightningBolt offsetAll(double theta);
  
  // returns the width of this bolt, from the leftmost tip to the rightmost tip. Ignores the
  // actual thicknesses of segments or the branches of a fork themselves, and just assumes that
  // the thickness of each bolt is zero.
  double getWidth();
  
  double getRightmostX();
  double getLeftmostX();
}

// represents one final endpoint of a lightning bolt
class Tip implements ILightningBolt {
  public WorldImage draw() {
    // TODO
    return null;
  }
  
  // Returns true iff this Tip is physically possible. A tip on its own is always physically
  // possible, so true is always returned.
  public boolean isPhysicallyPossible() {
    return true;
  }
  
  // Returns true iff this Tip is physically possible based on the current of its parent. A tip is
  // always physically possible regardless of the parent current, so true is always returned.
  public boolean isPhysicallyPossibleHelper(int parentCurrent) {
    return true;
  }
  
  public ILightningBolt combine(int leftLength, int rightLength, int leftCapacity,
                                int rightCapacity, double leftTheta, double rightTheta,
                                ILightningBolt otherBolt) {
    return new Fork(leftLength, rightLength, leftCapacity, rightCapacity, leftTheta, rightTheta,
      this.offsetAll(leftTheta - 90),
      otherBolt.offsetAll(rightTheta - 90));
  }
  
  public ILightningBolt offsetAll(double theta) {
    return this;
  }
  
  public double getWidth() {
    return 0;
  }
  public double getRightmostX() {
    return 0;
  }
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
  
  public WorldImage draw() {
    return null;
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
    return this.current <= parentCurrent && this.bolt.isPhysicallyPossibleHelper(this.current);
  }
  
  public ILightningBolt combine(int leftLength, int rightLength, int leftCapacity,
                                int rightCapacity, double leftTheta, double rightTheta,
                                ILightningBolt otherBolt) {
    return new Fork(leftLength, rightLength, leftCapacity, rightCapacity, leftTheta, rightTheta,
      this.offsetAll(leftTheta - 90),
      otherBolt.offsetAll(rightTheta - 90));
  }
  
  public ILightningBolt offsetAll(double theta) {
    return new Segment(this.length, this.current, this.theta + theta,
      this.bolt.offsetAll(theta));
  }
  
  public double getWidth() {
    return Math.abs(this.getRightmostX() - this.getLeftmostX());
  }
  
  public double getRightmostX() {
    return Math.max(0,
      this.length * Math.cos(Math.toRadians(this.theta)) + this.bolt.getRightmostX());
  }
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
  
  public WorldImage draw() {
    // TODO
    return null;
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
    return this.leftCurrent + this.rightCurrent <= parentCurrent
      && this.left.isPhysicallyPossibleHelper(this.leftCurrent)
      && this.right.isPhysicallyPossibleHelper(this.rightCurrent);
  }
  
  public ILightningBolt combine(int leftLength, int rightLength, int leftCapacity,
                                int rightCapacity, double leftTheta, double rightTheta,
                                ILightningBolt otherBolt) {
    return new Fork(leftLength, rightLength, leftCapacity, rightCapacity, leftTheta, rightTheta,
      this.offsetAll(leftTheta - 90),
      otherBolt.offsetAll(rightTheta - 90));
  }
  
  public ILightningBolt offsetAll(double theta) {
    return new Fork(this.leftLength, this.rightLength, this.leftCurrent, this.rightCurrent,
      this.leftTheta + theta, this.rightTheta + theta,
      this.left.offsetAll(theta), this.right.offsetAll(theta));
  }
  
  public double getWidth() {
    return Math.abs(this.getRightmostX() - this.getLeftmostX());
  }
  
  public double getRightmostX() {
    double lRightmost =
      this.leftLength * Math.cos(Math.toRadians(this.leftTheta)) + this.left.getRightmostX();
    double rRightmost =
      this.rightLength * Math.cos(Math.toRadians(this.rightTheta)) + this.right.getRightmostX();
    return Math.max(0, Math.max(lRightmost, rRightmost));
  }
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
}


