import tester.Tester;
import java.awt.*;

// TODO: include in submission or no?
class Ball {
  int x;
  int y;
  int radius;
  Color color;

  Ball(int x, int y, int radius, Color color) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.color = color;
  }

  // Returns the area of this ball.
  double area() {
    return Math.PI * Math.pow(this.radius, 2);
  }

  // Returns the circumference of this ball.
  double circumference() {
    return 2 * Math.PI * this.radius;
  }

  // Returns the distance from this ball's center to another ball's center
  double distanceTo(Ball that) {
    return Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2));
  }

  // Returns true if this ball overlaps with another the given Ball
  boolean overlaps(Ball that) {
    return this.distanceTo(that) < this.radius + that.radius;
  }
}

// TODO: ask if tests for separate functions should go in different tester-functions
class ExamplesBall {
  Ball b1 = new Ball(0, 0, 5, Color.BLUE);
  Ball b2 = new Ball(4, 3, 3, Color.RED);
  Ball b3 = new Ball(20, 20, 2, Color.GREEN);
  Ball b4 = new Ball(0, 8, 3, Color.ORANGE);

  boolean testArea(Tester t) {
    return t.checkInexact(b1.area(), 78.5, 0.001);
  }

  boolean testCircumference(Tester t) {
    return t.checkInexact(b1.circumference(), 31.4, 0.001);
  }

  boolean testDistanceTo (Tester t) {
    return t.checkInexact(b1.distanceTo(b2), 5d, 0.001) &&
            t.checkInexact(b1.distanceTo(b1), 0d, 0.001);
  }

  boolean testOverlaps (Tester t) {
    return t.checkExpect(b1.overlaps(b2), true) &&
            t.checkExpect(b1.overlaps(b3), false) &&
            t.checkExpect(b1.overlaps(b4), false); // just touching
  }
}