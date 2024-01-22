import javalib.worldimages.WorldImage;

interface ILightningBolt {
  WorldImage draw();
  boolean isPhysicallyPossible();
  boolean isPhysicallyPossibleHelper(int parentCurrent);
  ILightningBolt combine(int leftLength, int rightLength, int leftCapacity, int rightCapacity,
                         double leftTheta, double rightTheta, ILightningBolt otherBolt);
  
  // Returns a new ILightningBolt identical to this, but with its angle and the angle of all of
  // its children offset by theta.
  ILightningBolt offsetAll(double theta);
  
  // returns the width of the bolt, from the leftmost tip to the rightmost tip. Ignores the
  // actual thicknesses of segments or the branches of a fork themselves, and just assumes that
  // the thickness of each bolt is zero.
  double getWidth();
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
    // TODO
    return null;
  }
  
  public ILightningBolt offsetAll(double theta) {
    // TODO
    return null;
  }
  
  public double getWidth() {
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
    // TODO
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
    // TODO
    return null;
  }
  
  public ILightningBolt offsetAll(double theta) {
    // TODO
    return null;
  }
  
  public double getWidth() {
    // TODO
    return 0;
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
    // TODO
    return null;
  }
  
  public ILightningBolt offsetAll(double theta) {
    // TODO
    return null;
  }
  
  public double getWidth() {
    // TODO
    return 0;
  }
}