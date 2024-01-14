import tester.Tester;
import java.awt.Color;
import javalib.worldimages.*;

// Represents a ball in bouncing motion, with its position coordinates, its color,
// its size (determined by radius), and its rate of motion horizontally and vertically.
class BouncingBall {
  Posn pos;
  Color color;
  int size; // the radius of the ball
  int dx; // how fast is the ball moving to the right?
  int dy; // how fast is the ball moving downward?

  BouncingBall(Posn pos, Color color, int size, int dx, int dy) {
    this.pos = pos;
    this.color = color;
    this.size = size;
    this.dx = dx;
    this.dy = dy;
  }

  /* TEMPLATE
     FIELDS:
     ... this.pos ...                                    -- Posn
     ... this.color ...                                  -- Color
     ... this.size ...                                   -- int
     ... this.dx ...                                     -- int
     ... this.dy ...                                     -- int
     METHODS:
     ... this.area() ...                                 -- double
     ... this.circumference() ...                        -- double
     ... this.distanceTo(BouncingBall that) ...          -- double
     ... this.overlaps(BouncingBall that) ...            -- boolean
     ... this.move() ...                                 -- BouncingBall
     ... this.bounceX() ...                              -- BouncingBall
     ... this.bounceY() ...                              -- BouncingBall
     ... this.collidesX(Posn topLeft, Posn botRight) ... -- boolean
     ... this.collidesY(Posn topLeft, Posn botRight) ... -- boolean
     METHODS FOR FIELDS:
     ... this.pos.offset(int dx, int dy)                 -- Posn
   */

  // Returns the area of this ball
  double area() {
    return Math.PI * Math.pow(this.size, 2);
  }

  // Returns the circumference of this ball
  double circumference() {
    return 2 * Math.PI * this.size;
  }

  // Returns the distance from this ball's center to another ball's center
  double distanceTo(BouncingBall that) {
    return Math.sqrt(Math.pow(this.pos.x - that.pos.x, 2) + Math.pow(this.pos.y - that.pos.y, 2));
  }

  // Returns true if this ball overlaps with another the given Ball
  boolean overlaps(BouncingBall that) {
    return this.distanceTo(that) < this.size + that.size;
  }

  // Returns a new BouncingBall that's just like this BouncingBall, but moved
  // by this BouncingBall's dx and dy
  BouncingBall move() {
    Posn newPos = this.pos.offset(dx, dy);
    return new BouncingBall(newPos, this.color, this.size, this.dx, this.dy);
  }

  // Returns a new BouncingBall that represents this BouncingBall just after
  // it has bounced off a side wall. Does not actually move the ball.
  // This method will be called automatically when `collidesX` returns true
  BouncingBall bounceX() {
    return new BouncingBall(this.pos, this.color, this.size, -this.dx, this.dy);
  }

  // Like bounceX, except for using the top or bottom walls
  BouncingBall bounceY() {
    return new BouncingBall(this.pos, this.color, this.size, this.dx, -this.dy);
  }

  // Detects whether the ball is colliding with a side wall.
  boolean collidesX(Posn topLeft, Posn botRight) {
    return this.pos.x <= topLeft.x + this.size ||
           this.pos.x >= botRight.x - this.size;
  }

  // Detects whether the ball is colliding with a top or bottom wall.
  boolean collidesY(Posn topLeft, Posn botRight) {
    return this.pos.y <= topLeft.y + this.size ||
           this.pos.y >= botRight.y - this.size;
  }
}

class ExamplesBouncingBalls {
  int WIDTH = 300;
  int HEIGHT = 300;

  BouncingBall b1 = new BouncingBall(new Posn(0, 0), Color.BLUE, 5, 1, -2);
  BouncingBall b2 = new BouncingBall(new Posn(4, 3), Color.RED, 3, -3, 2);
  BouncingBall b3 = new BouncingBall(new Posn(20, 20), Color.GREEN, 2, 3, 3);
  BouncingBall b4 = new BouncingBall(new Posn(0, 8), Color.ORANGE, 3, 4, 2);

  boolean testBigBang(Tester t) {
    BouncingWorld w = new BouncingWorld(WIDTH, HEIGHT);
    return w.bigBang(WIDTH, HEIGHT, 0.01);
  }

  boolean testArea(Tester t) {
    return t.checkInexact(b1.area(), 78.5, 0.001);
  }

  boolean testCircumference(Tester t) {
    return t.checkInexact(b1.circumference(), 31.4, 0.001);
  }

  boolean testDistanceTo(Tester t) {
    return
    t.checkInexact(b1.distanceTo(b2), 5d, 0.001) &&
    t.checkInexact(b1.distanceTo(b1), 0d, 0.001);
  }

  boolean testOverlaps(Tester t) {
    return
    t.checkExpect(b1.overlaps(b2), true) &&
    t.checkExpect(b1.overlaps(b3), false) &&
    t.checkExpect(b1.overlaps(b4), false); // just touching
  }

  boolean testMove(Tester t) {
    return t.checkExpect(
      b2.move(),
      new BouncingBall(new Posn(1, 5), Color.RED, 3, -3, 2)
    );
  }

  boolean testBounceX(Tester t) {
    return t.checkExpect(b1.bounceX(), new BouncingBall(new Posn(0, 0), Color.BLUE, 5, -1, -2));
  }

  boolean testBounceY(Tester t) {
    return t.checkExpect(b1.bounceY(), new BouncingBall(new Posn(0, 0), Color.BLUE, 5, 1, 2));
  }

  boolean testCollidesX(Tester t) {
    return
    // Intersecting with left wall
    t.checkExpect(b1.collidesX(new Posn(-1, -10), new Posn(10, 10)),true) &&
    // Touching left wall
    t.checkExpect(b1.collidesX(new Posn(-5, -10), new Posn(10, 10)),true) &&
    // In between walls
    t.checkExpect(b1.collidesX(new Posn(-10, -10), new Posn(10, 10)),false) &&
    // Touching right wall
    t.checkExpect(b1.collidesX(new Posn(-10, -10), new Posn(5, 10)),true) &&
    // Intersecting with right wall
    t.checkExpect(b1.collidesX(new Posn(-10, -10), new Posn(1, 10)),true);
  }

  boolean testCollidesY(Tester t) {
    return
    // Intersecting with top wall.
    t.checkExpect(b1.collidesY(new Posn(-40, -3), new Posn(40, 6)), true) &&
    // Touching the top wall.
    t.checkExpect(b1.collidesY(new Posn(-40, -5), new Posn(40, 10)), true) &&
    // In-between walls, not colliding with respect to y-coordinate.
    t.checkExpect(b1.collidesY(new Posn(-40, -40), new Posn(40, 40)), false) &&
    // Touching the bottom wall.
    t.checkExpect(b1.collidesY(new Posn(-40, -6), new Posn(40, 3)), true) &&
    // Intersecting with bottom wall.
    t.checkExpect(b1.collidesY(new Posn(-40, -10), new Posn(40, 5)), true);
  }
}