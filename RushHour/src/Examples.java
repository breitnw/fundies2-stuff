import javalib.impworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.*;
import java.util.Random;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.nio.file.Path;

// Examples for Game.java =========================================================================


class ExamplesGame {

  // +----------+
  // | Examples |
  // +----------+

  // ==== Provided example levels ====

  Level level1;
  Level level2;
  Level level3;
  Level level4;

  // ==== Other extraneous (but valid) cases ====

  // level1, but with the player car selected
  Level level5;
  // No walls
  Level level6;
  // No cars
  Level level7;
  // No exits
  Level level8;
  // Multiple player cars and exits
  Level level9;
  // Walls within the level
  Level level10;
  // Wider level shape
  Level level11;
  // Completely empty, 1x1
  Level level12;
  // Very simple, including every possible element, for testing
  Level level13;
  // Very simple, including every possible element, for testing
  Level level14;
  // Game is in a winning state with one PlayerCar
  Level level15;
  // Game is in a winning state with two PlayerCars
  Level level16;
  // Game is in a non-winning state with two PlayerCars (only one overlapping an exit)
  Level level17;
  // Irregular board shape

  // ==== Klotski ====

  Level level18;
  // Klotski level
  Level kLevel1;
  // Easy klotski level for testing
  Level kLevel2;

  Game game1;
  Game game2;
  Game game3;
  Game game4;
  Game game5;
  Game game6;
  Game kGame1;

  void resetExamples() {
    // ==== Provided example levels ====

    level1 = new Level("+------+\n"
        + "|c    T|\n"
        + "|T  T  |\n"
        + "| p    X\n"
        + "|      |\n"
        + "|C   c |\n"
        + "|   t  |\n"
        + "+------+");

    level2 = new Level("+------+\n"
        + "|Tc T  |\n"
        + "|      |\n"
        + "| p    X\n"
        + "|  Ct  |\n"
        + "|     C|\n"
        + "|  t   |\n"
        + "+------+");

    level3 = new Level("+------+\n"
        + "|c CT  |\n"
        + "|T     |\n"
        + "| p    X\n"
        + "| t    |\n"
        + "|      |\n"
        + "|  t   |\n"
        + "+------+");

    level4 = new Level("+------+\n"
        + "|c  t  |\n"
        + "|   Cc |\n"
        + "|Cp   TX\n"
        + "|  Tc  |\n"
        + "|c     |\n"
        + "|   t  |\n"
        + "+------+");

    // ==== Other extraneous (but valid) cases ====

    // level1, but with the player car selected
    level5 = new Level("+------+\n"
        + "|c    T|\n"
        + "|T  T  |\n"
        + "| p    X\n"
        + "|      |\n"
        + "|C   c |\n"
        + "|   t  |\n"
        + "+------+",
        new GridPosn(2, 3),
        new ScoreCounter(0, true),
        new StateRecord());

    // No walls
    level6 = new Level("        \n"
        + " c    T \n"
        + " T  T   \n"
        + "  p    X\n"
        + "        \n"
        + "        ");

    // No cars
    level7 = new Level("+------+\n"
        + "|      |\n"
        + "|      |\n"
        + "|      X\n"
        + "|      |\n"
        + "+------+");

    // No exits
    level8 = new Level("+------+\n"
        + "|c    T|\n"
        + "|T  T  |\n"
        + "| p    |\n"
        + "|      |\n"
        + "+------+");

    // Multiple player cars and exits
    level9 = new Level("+------+\n"
        + "|c P  T|\n"
        + "|T  T  |\n"
        + "| p    X\n"
        + "|      |\n"
        + "+--X---+");

    // Walls within the level
    level10 = new Level("+------+\n"
        + "|c    T|\n"
        + "|T +T  |\n"
        + "| p    X\n"
        + "|  +   |\n"
        + "+------+");

    // Wider level shape
    level11 = new Level("+---------+\n"
        + "|c    T   |\n"
        + "|T  C     |\n"
        + "| p       X\n"
        + "|   c     |\n"
        + "+---------+");

    // Completely empty, 1x1
    level12 = new Level(" ");

    // Very simple, including every possible element, for testing
    level13 = new Level("CPTc |\n"
        + "   p -\n"
        + "X+ t  ");

    // Very simple, including every possible element, for testing
    level14 = new Level("CPTc |\n"
        + "   p -\n"
        + "X+ t  ",
        new GridPosn(3, 1),
        new ScoreCounter(0, true),
        new StateRecord());

    // Game is in a winning state with one PlayerCar
    level15 = new Level("+------+\n"
        + "|c     |\n"
        + "|T  T  |\n"
        + "|     pX\n"
        + "|      |\n"
        + "+------+");

    // Game is in a winning state with two PlayerCars
    level16 = new Level("+------+\n"
        + "|c     |\n"
        + "|T  T  |\n"
        + "|     pX\n"
        + "|  P   |\n"
        + "+--X---+");

    // Game is in a non-winning state with two PlayerCars (only one overlapping an exit)
    level17 = new Level("+------+\n"
        + "|c     |\n"
        + "|T  T  |\n"
        + "|  P  pX\n"
        + "|      |\n"
        + "+--X---+");

    // Irregular board shape
    level18 = new Level("  +--+ +----+\n"
        + "  |  | |t  C|\n"
        + "+-+CC+-+C   +--+\n"
        + "|p             X\n"
        + "+--------------+");

    // Klotski level
    kLevel1 = new Level("+----+\n"
        + "|BS B|\n"
        + "|    |\n"
        + "|Bb B|\n"
        + "| .. |\n"
        + "|.  .|\n"
        + "+-XX-+");
    // Easy klotski level for testing
    kLevel2 = new Level("+----+\n"
        + "|BS B|\n"
        + "|    |\n"
        + "|B  B|\n"
        + "|    |\n"
        + "|.  .|\n"
        + "+-XX-+");

    game1 = new Game(level1);
    game2 = new Game(level2);
    game3 = new Game(level3);
    game4 = new Game(level4);
    game5 = new Game(level5);
    game6 = new Game(level18);
    kGame1 = new Game(kLevel1);
  }

  // +------------+
  // | Game Tests |
  // +------------+
  void testBigBang(Tester t) {
    this.resetExamples();
    // Runs the game via bigBang()
    kGame1.bigBang();
  }

  void testMakeScene(Tester t) {
    this.resetExamples();
    // Since we've tested Level.draw(), we're checking against its output in this test
    t.checkExpect(game1.makeScene(), level1.draw());
    t.checkExpect(game2.makeScene(), level2.draw());
  }

  void testOnKeyEvent(Tester t) {
    this.resetExamples();

    // A game with a newly selected car
    Game gNewSelection = new Game(
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T     |\n"
                      + "| p    X\n"
                      + "|      |\n"
                      + "|C   c |\n"
                      + "|   t  |\n"
                      + "+------+",
            new GridPosn(2, 3),
            new ScoreCounter(0, true),
            new StateRecord()));

    // A game with a car that was already moved on this selection
    Game gOldSelection = new Game(
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T     |\n"
                      + "| p    X\n"
                      + "|      |\n"
                      + "|C   c |\n"
                      + "|   t  |\n"
                      + "+------+",
            new GridPosn(2, 3),
            new ScoreCounter(0, false),
            new StateRecord()));

    // A game where the player car is blocked on both sides
    Game blocked = new Game(
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T +T  |\n"
                      + "| p    X\n"
                      + "|  +  +|\n"
                      + "+------+",
            new GridPosn(5, 0),
            new ScoreCounter(),
            new StateRecord()));

    // cannot move player car right (blocked by vehicle)
    game5.onKeyEvent("d");
    t.checkExpect(game5, new Game(
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T  T  |\n"
                      + "| p    X\n"
                      + "|      |\n"
                      + "|C   c |\n"
                      + "|   t  |\n"
                      + "+------+",
            new GridPosn(2, 3),
            new ScoreCounter(0, true),
            new StateRecord()))); // We still have a new selection

    // cannot move truck down (blocked by obstacle)
    blocked.onKeyEvent("s");
    t.checkExpect(blocked, new Game(
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T +T  |\n"
                      + "| p    X\n"
                      + "|  +  +|\n"
                      + "+------+",
            new GridPosn(5, 0),
            new ScoreCounter(),
            new StateRecord())));

    // wrong direction
    blocked.onKeyEvent("a");
    t.checkExpect(blocked, new Game(
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T +T  |\n"
                      + "| p    X\n"
                      + "|  +  +|\n"
                      + "+------+",
            new GridPosn(5, 0),
            new ScoreCounter(),
            new StateRecord())));

    // no selected vehicle
    game4.onKeyEvent("s");
    t.checkExpect(game4, new Game(
        new Level("+------+\n"
                      + "|c  t  |\n"
                      + "|   Cc |\n"
                      + "|Cp   TX\n"
                      + "|  Tc  |\n"
                      + "|c     |\n"
                      + "|   t  |\n"
                      + "+------+")));

    // Moving player car to the right when it was just selected
    gNewSelection.onKeyEvent("d");
    t.checkExpect(gNewSelection, new Game(
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T     |\n"
                      + "|  p   X\n"
                      + "|      |\n"
                      + "|C   c |\n"
                      + "|   t  |\n"
                      + "+------+",
            new GridPosn(3, 3),
            new ScoreCounter(1, false),
            new StateRecord())));

    // Moving player car to the right when it has already been moved during this selection
    gOldSelection.onKeyEvent("d");
    t.checkExpect(gOldSelection, new Game(
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T     |\n"
                      + "|  p   X\n"
                      + "|      |\n"
                      + "|C   c |\n"
                      + "|   t  |\n"
                      + "+------+",
            new GridPosn(3, 3),
            new ScoreCounter(0, false),
            new StateRecord())));
  }

  void testLastScene(Tester t) {
    this.resetExamples();

    WorldScene levelScene = game1.level.draw();
    levelScene.placeImageXY(
        new SpriteLoader().fromSpritesDir(Path.of("you-win.png")),
        game1.level.getWidthPixels() / 2,
        game1.level.getHeightPixels() / 2);
    t.checkExpect(game1.lastScene(""), levelScene);
  }


  void testOnMouseClicked(Tester t) {
    this.resetExamples();

    // Clicking on the player car to select it
    // Also mutates the score counter to register a new selection
    game1.onMouseClicked(new Posn(130, 200));
    t.checkExpect(game1.level.movables, game5.level.movables);
    t.checkExpect(game1.level.sc, game5.level.sc);
    t.checkExpect(game1.level.record.record.size(), 1);

    // Doing nothing by clicking on a wall
    this.resetExamples();
    game1.onMouseClicked(new Posn(30, 30));
    t.checkExpect(game1,
        new Game(
            new Level("+------+\n"
                + "|c    T|\n"
                + "|T  T  |\n"
                + "| p    X\n"
                + "|      |\n"
                + "|C   c |\n"
                + "|   t  |\n"
                + "+------+")));
    // Deselecting the player car
    this.resetExamples();
    game5.onMouseClicked(new Posn(30, 30));
    t.checkExpect(game5,
        new Game(
            new Level("+------+\n"
                + "|c    T|\n"
                + "|T  T  |\n"
                + "| p    X\n"
                + "|      |\n"
                + "|C   c |\n"
                + "|   t  |\n"
                + "+------+",
                new GridPosn(-1, -1),
                new ScoreCounter(0, true),
                new StateRecord())));
  }

  // +-------------+
  // | Level Tests |
  // +-------------+
  void testConstructorEx(Tester t) {
    // Grid has a width of 0
    t.checkConstructorException(
        new IllegalArgumentException("Grid width must be greater than zero"),
        "Level",
        new Mt<AVehicle>(), new Mt<Wall>(), new Mt<Exit>(), 0, 7,
        new ScoreCounter(), new StateRecord());
    // Grid has a height of 0
    t.checkConstructorException(
        new IllegalArgumentException("Grid height must be greater than zero"),
        "Level",
        new Mt<AVehicle>(), new Mt<Wall>(), new Mt<Exit>(), 8, 0,
        new ScoreCounter(), new StateRecord());
    // A vehicle is overlapping a wall
    t.checkConstructorException(
        new IllegalArgumentException("No vehicle may overlap with a wall"),
        "Level",
        "+------+\n"
            + "|    t |\n"
            + "|      |\n"
            + "|      X\n"
            + "+------+"
    );
    // A vehicle is overlapping another vehicle
    t.checkConstructorException(
        new IllegalArgumentException("No vehicle may overlap with another vehicle"),
        "Level",
        "+------+\n"
            + "|      |\n"
            + "|   P  |\n"
            + "| t    X\n"
            + "+------+"
    );
    // A vehicle extends beyond the limits of the grid
    t.checkConstructorException(
        new IllegalArgumentException("No vehicle may extend beyond the limits of the grid"),
        "Level",
        "+------+\n"
            + "|     t \n"
            + "|      |\n"
            + "|      X\n"
            + "+------+"
    );
  }

  // Test the second constructor, which builds a level based on a String with no active cars
  void testConstructor2(Tester t) {
    this.resetExamples();
    t.checkExpect(level12, new Level(new Mt<>(), new Mt<>(), new Mt<>(), 1, 1,
        new ScoreCounter(),
        new StateRecord()));
    t.checkExpect(level13, new Level(
        new Cons<>(
            new Car(new GridPosn(0, 0), false, 3, false),
            new Cons<>(
                new PlayerCar(new GridPosn(1, 0), false, false),
                new Cons<>(
                    new Truck(new GridPosn(2, 0), 0, false, false),
                    new Cons<>(
                        new Car(new GridPosn(3, 0), false, 3, true),
                        new Cons<>(
                            new PlayerCar(new GridPosn(3, 1), false, true),
                            new Cons<>(
                                new Truck(new GridPosn(3, 2), 1, false, true),
                                new Mt<>())))))),
        new Cons<>(
            new Wall(new GridPosn(5, 0)),
            new Cons<>(
                new Wall(new GridPosn(5, 1)),
                new Cons<>(
                    new Wall(new GridPosn(1, 2)),
                    new Mt<>()))),
        new Cons<>(
            new Exit(new GridPosn(0, 2)),
            new Mt<>()),
        6, 3,
        new ScoreCounter(),
        new StateRecord()));
  }

  // Tests the third constructor, which builds a level from a String with the car at the
  // specified position (if any) selected
  void testConstructor3(Tester t) {
    this.resetExamples();
    t.checkExpect(
        new Level(" ", new GridPosn(-1, -1), new ScoreCounter(0, false), new StateRecord()),
        level12);
    t.checkExpect(level14, new Level(
        new Cons<>(
            new Car(new GridPosn(0, 0), false, 3, false),
            new Cons<>(
                new PlayerCar(new GridPosn(1, 0), false, false),
                new Cons<>(
                    new Truck(new GridPosn(2, 0), 0, false, false),
                    new Cons<>(
                        new Car(new GridPosn(3, 0), false, 3, true),
                        new Cons<>(
                            new PlayerCar(new GridPosn(3, 1), true, true),
                            new Cons<>(
                                new Truck(new GridPosn(3, 2), 1, false, true),
                                new Mt<>())))))),
        new Cons<>(
            new Wall(new GridPosn(5, 0)),
            new Cons<>(
                new Wall(new GridPosn(5, 1)),
                new Cons<>(
                    new Wall(new GridPosn(1, 2)),
                    new Mt<>()))),
        new Cons<>(
            new Exit(new GridPosn(0, 2)),
            new Mt<>()),
        6, 3,
        new ScoreCounter(0, true),
        new StateRecord()));
  }


  void testLevelHandleKey(Tester t) {
    // see testOnKeyEvent for more extensive integration tests
    this.resetExamples();
    level5.handleKey("d");
    t.checkExpect(level5,
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T  T  |\n"
                      + "| p    X\n"
                      + "|      |\n"
                      + "|C   c |\n"
                      + "|   t  |\n"
                      + "+------+",
            new GridPosn(2, 3),
            new ScoreCounter(0, true),
            new StateRecord())
    );
  }

  // An integration test to check the functionality of the draw method on a simple scene.
  // Utilizes draw methods on IGameObject and GridPosn that are tested individually below.
  void testDraw(Tester t) {
    this.resetExamples();

    WorldScene scene = new WorldScene(0, 0);
    // Draw the background
    new GridPosn().drawPositioned(
        new TiledImage(
            new OneSlice(Path.of("grid-cell.png")),
            6, 3).draw(),
        scene);
    // Draw the walls and exits
    new Wall(new GridPosn(1, 2)).drawTo(scene);
    new Wall(new GridPosn(5, 1)).drawTo(scene);
    new Wall(new GridPosn(5, 0)).drawTo(scene);
    new Exit(new GridPosn(5, 1)).drawTo(scene);
    // Draw the vehicles
    new Truck(new GridPosn(3, 2), 1, false, true).drawTo(scene);
    new PlayerCar(new GridPosn(3, 1), true, true).drawTo(scene);
    new Car(new GridPosn(3, 0), false, 3, true).drawTo(scene);
    new Truck(new GridPosn(2, 0), 0, false, false).drawTo(scene);
    new PlayerCar(new GridPosn(1, 0), false, false).drawTo(scene);
    new Car(new GridPosn(0, 0), false, 3, false).drawTo(scene);

    // Test the result
    t.checkExpect(level13.draw(), scene);
  }

  void testGetWidthPixels(Tester t) {
    this.resetExamples();
    t.checkExpect(level12.getWidthPixels(), 64);
    t.checkExpect(level1.getWidthPixels(), 512);
    t.checkExpect(level11.getWidthPixels(), 704);
  }

  void testGetHeightPixels(Tester t) {
    this.resetExamples();
    t.checkExpect(level12.getHeightPixels(), 64);
    t.checkExpect(level1.getHeightPixels(), 512);
    t.checkExpect(level11.getHeightPixels(), 384);
  }

  void testHasWon(Tester t) {
    this.resetExamples();
    t.checkExpect(level1.hasWon(), false);
    t.checkExpect(level15.hasWon(), true);
    t.checkExpect(level16.hasWon(), true);
    t.checkExpect(level17.hasWon(), false);
  }

  void testHandleClick(Tester t) {
    this.resetExamples();
    // Doing nothing by clicking on a wall
    level1.handleClick(new GridPosn(0, 0));
    t.checkExpect(level1,
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T  T  |\n"
                      + "| p    X\n"
                      + "|      |\n"
                      + "|C   c |\n"
                      + "|   t  |\n"
                      + "+------+"));

    // Clicking on the player car to select it
    level1.handleClick(new GridPosn(2, 3));
    // The selections are correct
    t.checkExpect(level1.movables, level5.movables);
    // the score counter is correct
    t.checkExpect(level1.sc, level5.sc);
    // the selection was added to the
    t.checkExpect(level1.record.record.empty(), false);

    // Deselecting the player car
    this.resetExamples();
    level5.handleClick(new GridPosn(0, 0));
    t.checkExpect(level5,
        new Level("+------+\n"
                + "|c    T|\n"
                + "|T  T  |\n"
                + "| p    X\n"
                + "|      |\n"
                + "|C   c |\n"
                + "|   t  |\n"
                + "+------+",
            new GridPosn(-1, -1), // No selection
            new ScoreCounter(0, true),
            new StateRecord()));
    // Selecting a different car
    this.resetExamples();
    level5.handleClick(new GridPosn(1, 1));
    Level testLevel = new Level("+------+\n"
        + "|c    T|\n"
        + "|T  T  |\n"
        + "| p    X\n"
        + "|      |\n"
        + "|C   c |\n"
        + "|   t  |\n"
        + "+------+",
        new GridPosn(1, 1),
        new ScoreCounter(0, true),
        new StateRecord());
    t.checkExpect(level5.movables, testLevel.movables);
    t.checkExpect(level5.sc, testLevel.sc);
    t.checkExpect(level5.record.record.empty(), false);

  }
}

// Examples for IGameObject.java ==================================================================

class ExamplesIGameObject {

  // +----------+
  // | Examples |
  // +----------+

  // Walls
  Wall wall1;
  Wall wall2;
  // Exits
  Exit exit1;
  Exit exit2;
  // Cars
  Car car1;
  Car car2;
  Car car3;
  // PlayerCars
  PlayerCar pCar1;
  PlayerCar pCar2;
  // Trucks
  Truck truck1;
  Truck truck2;
  // Klotski pieces
  NormalBox nBox1;
  NormalBox nBox2;
  PlayerBox pBox1;
  PlayerBox pBox2;

  void resetExamples() {
    // Walls
    wall1 = new Wall(new GridPosn());
    wall2 = new Wall(new GridPosn(2, 4));
    // Exits
    exit1 = new Exit(new GridPosn());
    exit2 = new Exit(new GridPosn(2, 4));
    // Cars
    car1 = new Car(new GridPosn(), true, 0, true);
    car2 = new Car(new GridPosn(0, 1), false, 2, false);
    car3 = new Car(new GridPosn(1, 0), false, 4, false);
    // PlayerCars
    pCar1 = new PlayerCar(new GridPosn(), true, true);
    pCar2 = new PlayerCar(new GridPosn(1, 1), false, false);
    // Trucks
    truck1 = new Truck(new GridPosn(0, 2), 3, true, true);
    truck2 = new Truck(new GridPosn(1, 1), 1, false, false);
    // Klotski pieces
    nBox1 = new NormalBox(new GridPosn(), false, 1, 1);
    nBox2 = new NormalBox(new GridPosn(2, 1), true, 2, 3);
    pBox1 = new PlayerBox(new GridPosn(1, 0), false, 3, 1);
    pBox2 = new PlayerBox(new GridPosn(1, 1), true, 2, 2);
  }

  // +-------------------+
  // | AGameObject Tests |
  // +-------------------+

  void testIntersects(Tester t) {
    this.resetExamples();
    // walls/exits intersecting walls/exits
    t.checkExpect(wall1.intersects(wall1), true);
    t.checkExpect(wall1.intersects(wall2), false);
    t.checkExpect(wall2.intersects(exit2), true);
    // vehicles intersecting vehicles
    t.checkExpect(car1.intersects(car2), false);
    t.checkExpect(car1.intersects(car3), true);
    t.checkExpect(pCar2.intersects(car3), true);
    t.checkExpect(pCar1.intersects(pCar2), false);
    t.checkExpect(truck1.intersects(pCar2), true);
    t.checkExpect(truck1.intersects(truck2), true);
    // vehicles intersecting walls/exits
    t.checkExpect(car1.intersects(wall1), true);
    t.checkExpect(car1.intersects(exit1), true);
    t.checkExpect(pCar2.intersects(wall1), false);
    // klotski pieces intersecting other things
    t.checkExpect(nBox1.intersects(wall1), true);
    t.checkExpect(nBox2.intersects(exit2), false);
    t.checkExpect(pBox1.intersects(car2), false);
    t.checkExpect(pBox2.intersects(nBox2), true);
  }

  void testGetArea(Tester t) {
    this.resetExamples();
    t.checkExpect(wall1.getArea(), new GridArea(new GridPosn(), new GridPosn(1, 1)));
    t.checkExpect(wall2.getArea(), new GridArea(new GridPosn(2, 4), new GridPosn(3, 5)));
    t.checkExpect(exit2.getArea(), new GridArea(new GridPosn(2, 4), new GridPosn(3, 5)));
    t.checkExpect(car1.getArea(), new GridArea(new GridPosn(), new GridPosn(2, 1)));
    t.checkExpect(car2.getArea(), new GridArea(new GridPosn(0, 1), new GridPosn(1, 3)));
    t.checkExpect(pCar2.getArea(), new GridArea(new GridPosn(1, 1), new GridPosn(2, 3)));
    t.checkExpect(truck1.getArea(), new GridArea(new GridPosn(0, 2), new GridPosn(3, 3)));
    t.checkExpect(truck2.getArea(), new GridArea(new GridPosn(1, 1), new GridPosn(2, 4)));
    t.checkExpect(nBox1.getArea(), new GridArea(new GridPosn(), new GridPosn(1, 1)));
    t.checkExpect(nBox2.getArea(), new GridArea(new GridPosn(2, 1), new GridPosn(4, 4)));
    t.checkExpect(pBox1.getArea(), new GridArea(new GridPosn(1, 0), new GridPosn(4, 1)));
  }

  void testXSize(Tester t) {
    this.resetExamples();
    t.checkExpect(wall1.xSize(), 1);
    t.checkExpect(exit2.xSize(), 1);
    t.checkExpect(car1.xSize(), 2);
    t.checkExpect(car2.xSize(), 1);
    t.checkExpect(pCar2.xSize(), 1);
    t.checkExpect(truck1.xSize(), 3);
    t.checkExpect(truck2.xSize(), 1);
    t.checkExpect(nBox2.xSize(), 2);
    t.checkExpect(pBox1.xSize(), 3);
  }

  void testYSize(Tester t) {
    this.resetExamples();
    t.checkExpect(wall1.ySize(), 1);
    t.checkExpect(exit2.ySize(), 1);
    t.checkExpect(car1.ySize(), 1);
    t.checkExpect(car2.ySize(), 2);
    t.checkExpect(pCar2.ySize(), 2);
    t.checkExpect(truck1.ySize(), 1);
    t.checkExpect(truck2.ySize(), 3);
    t.checkExpect(nBox2.ySize(), 3);
    t.checkExpect(pBox1.ySize(), 1);
  }

  void testGetImage(Tester t) {
    this.resetExamples();

    t.checkExpect(wall1.getImage(), new SpriteLoader().fromSpritesDir(Path.of("bush.png")));
    t.checkExpect(exit1.getImage(), new SpriteLoader().fromSpritesDir(Path.of("exit.png")));

    t.checkExpect(car1.getImage(), new RotateImage(
        new SpriteLoader().fromSpritesDir(Path.of("car","car-selected.png")), 90.0));
    t.checkExpect(car2.getImage(), new RotateImage(
        new SpriteLoader().fromSpritesDir(Path.of("car", "car2.png")), 180.0));
    t.checkExpect(pCar1.getImage(), new RotateImage(
        new SpriteLoader().fromSpritesDir(Path.of("car", "car-selected.png")), 90.0));
    t.checkExpect(pCar2.getImage(), new RotateImage(
        new SpriteLoader().fromSpritesDir(Path.of("car", "car-player.png")), 180));
    t.checkExpect(truck1.getImage(), new RotateImage(
        new SpriteLoader().fromSpritesDir(Path.of("truck", "truck-selected.png")), 90.0));
    t.checkExpect(truck2.getImage(), new RotateImage(
        new SpriteLoader().fromSpritesDir(Path.of("truck", "truck1.png")), 180.0));

    t.checkExpect(nBox1.getImage(), new TiledImage(
        new NineSlice(Path.of("klotski", "normal-box.png")), 2, 2).draw());
    t.checkExpect(nBox2.getImage(), new TiledImage(
        new NineSlice(Path.of("klotski", "normal-box-selected.png")), 4, 6).draw());
    t.checkExpect(pBox1.getImage(), new TiledImage(
        new NineSlice(Path.of("klotski", "player-box.png")), 6, 2).draw());
    t.checkExpect(pBox2.getImage(), new TiledImage(
        new NineSlice(Path.of("klotski", "player-box-selected.png")), 4, 4).draw());

  }

  void testDrawTo(Tester t) {
    this.resetExamples();

    // 0x0 world scene (the size doesn't matter, as images are still added to the object structure)
    WorldScene sc1 = new WorldScene(0, 0);
    WorldScene sc2 = new WorldScene(0, 0);
    wall1.drawTo(sc1);
    new GridPosn().drawPositioned(wall1.getImage(), sc2);
    t.checkExpect(sc1, sc2);

    // 512x512 world scene
    WorldScene sc3 = new WorldScene(512, 512);
    WorldScene sc4 = new WorldScene(512, 512);
    wall1.drawTo(sc3);
    new GridPosn().drawPositioned(wall1.getImage(), sc4);
    t.checkExpect(sc3, sc4);

    // not drawn at (0, 0)
    // we can reuse the scenes from earlier since they're already the same
    exit2.drawTo(sc1);
    new GridPosn(2, 4).drawPositioned(exit2.getImage(), sc2);
    t.checkExpect(sc1, sc2);

    // drawing a car
    car2.drawTo(sc1);
    new GridPosn(0, 1).drawPositioned(car2.getImage(), sc2);
    t.checkExpect(sc1, sc2);

    // drawing a player car
    pCar1.drawTo(sc1);
    new GridPosn(1, 1).drawPositioned(pCar1.getImage(), sc2);
    t.checkExpect(sc1, sc2);

    // drawing a truck
    truck1.drawTo(sc1);
    new GridPosn(0, 2).drawPositioned(truck1.getImage(), sc2);
    t.checkExpect(sc1, sc2);

    // drawing klotski pieces
    nBox2.drawTo(sc1);
    new GridPosn(2, 1).drawPositioned(nBox2.getImage(), sc2);
    t.checkExpect(sc1, sc2);

    pBox2.drawTo(sc1);
    new GridPosn(1, 1).drawPositioned(pBox2.getImage(), sc2);
    t.checkExpect(sc1, sc2);
  }

  // +----------------+
  // | AMovable Tests |
  // +----------------+
  void testInWinningState(Tester t) {
    this.resetExamples();

    t.checkExpect(car1.inWinningState(new Mt<>()), true);
    t.checkExpect(car1.inWinningState(new Cons<>(exit1, new Mt<>())), true);
    t.checkExpect(car1.inWinningState(new Cons<>(exit2, new Mt<>())), true);
    t.checkExpect(pCar1.inWinningState(new Mt<>()), false);
    t.checkExpect(pCar1.inWinningState(new Cons<>(exit1, new Mt<>())), true);
    t.checkExpect(pCar1.inWinningState(new Cons<>(exit2, new Mt<>())), false);
    t.checkExpect(truck1.inWinningState(new Mt<>()), true);
    t.checkExpect(truck2.inWinningState(new Cons<>(exit1, new Mt<>())), true);

    t.checkExpect(nBox1.inWinningState(new Mt<>()), true);
    t.checkExpect(pBox1.inWinningState(new Mt<>()), false);
    t.checkExpect(pBox1.inWinningState(new Cons<>(exit1, new Mt<>())), false);
    t.checkExpect(pBox2.inWinningState(
        new Cons<>(new Exit(new GridPosn(2, 2)), new Mt<>())),true);
  }

  void testRegisterClick(Tester t) {
    this.resetExamples();
    ScoreCounter sc = new ScoreCounter();
    StateRecord record = new StateRecord();

    // clicking an already active car yields an active car
    car1.registerClick(new GridPosn(1, 0), sc, record);
    t.checkExpect(car1,
        new Car(new GridPosn(), true, 0, true));
    t.checkExpect(sc, new ScoreCounter());
    t.checkExpect(record, new StateRecord());

    // clicking outside an already active car yields an inactive car
    car1.registerClick(new GridPosn(1, 1), sc, record);
    t.checkExpect(car1,
        new Car(new GridPosn(), false, 0, true));
    t.checkExpect(sc, new ScoreCounter());
    t.checkExpect(record, new StateRecord());

    // clicking outside an inactive car yields an inactive car
    car2.registerClick(new GridPosn(1, 1), sc, record);
    t.checkExpect(car2,
        new Car(new GridPosn(0, 1), false, 2, false));
    t.checkExpect(sc, new ScoreCounter());
    t.checkExpect(record, new StateRecord());

    // clicking an inactive car yields an active car
    car2.registerClick(new GridPosn(0, 1), sc, record);
    t.checkExpect(car2,
        new Car(new GridPosn(0, 1), true, 2, false));
    t.checkExpect(sc, new ScoreCounter(0, true));
    Stack<Action> newStack = new Stack<>();
    newStack.push(new Action(car2));
    t.checkExpect(record, new StateRecord(newStack));

    // clicking will also work with other vehicles and boxes, as it is defined on the AMovable
    // class using only tested methods.
  }


  void testMoveUnchecked(Tester t) {
    this.resetExamples();

    car1.moveUnchecked(1, 2);
    t.checkExpect(car1.posn, new GridPosn(1, 2));
    car2.moveUnchecked(2, -1);
    t.checkExpect(car2.posn, new GridPosn(2, 0));
  }

  void testRegisterSelectEvent(Tester t) {
    this.resetExamples();

    ScoreCounter sc = new ScoreCounter();
    StateRecord sr = new StateRecord();
    car2.registerSelectEvent(true, sc, sr);
    t.checkExpect(car2.selected, true);
    t.checkExpect(sc.hasNewSelection, true);
    t.checkExpect(sr.record.peek(), new Action(car2));
  }

  void testRegisterSelection(Tester t) {
    this.resetExamples();

    ScoreCounter sc = new ScoreCounter();
    StateRecord sr = new StateRecord();
    RegisterSelection<Car> rs = new RegisterSelection<Car>(true, sc, sr);
    rs.accept(car2);
    t.checkExpect(car2.selected, true);
    t.checkExpect(sc.hasNewSelection, true);
    t.checkExpect(sr.record.peek(), new Action(car2));
  }

  void testRegisterKey(Tester t) {
    this.resetExamples();

    Car moveCar = new Car(new GridPosn(), true, 1, true);
    Truck moveTruck = new Truck(new GridPosn(2, 0), 3, true, false);
    Truck inactiveTruck = new Truck(new GridPosn(2, 0), 3, false, false);

    NormalBox moveNBox = new NormalBox(new GridPosn(), true, 2, 1);
    PlayerBox movePBox = new PlayerBox(new GridPosn(), true, 2, 1);
    NormalBox inactiveBox = new NormalBox(new GridPosn(), false, 2, 1);

    Wall w = new Wall(new GridPosn(2, 0));
    Exit e = new Exit(new GridPosn(2, 0));

    ScoreCounter sc = new ScoreCounter();
    StateRecord record = new StateRecord();
    Stack<Action> recordStack = new Stack<>();

    // Do nothing if the movable is inactive
    inactiveTruck.registerKey("a", new Mt<>(), new Mt<>(), new Mt<>(), sc, record);
    t.checkExpect(inactiveTruck,
        new Truck(new GridPosn(2, 0), 3, false, false));
    inactiveBox.registerKey("a",  new Mt<>(), new Mt<>(), new Mt<>(), sc, record);
    t.checkExpect(inactiveBox,
        new NormalBox(new GridPosn(), false, 2, 1));
    // The score counter remains at 0
    t.checkExpect(sc, new ScoreCounter(0, false));

    // Do nothing if the desired move isn't in the direction of movement (only on vehicles)
    sc.registerReselect();
    record.startAction(moveCar);
    recordStack.push(new Action(moveCar));
    moveCar.registerKey("s", new Mt<>(), new Mt<>(), new Mt<>(), sc, record);
    t.checkExpect(moveCar,
        new Car(new GridPosn(), true, 1, true));

    sc.registerReselect();
    record.startAction(moveTruck);
    recordStack.push(new Action(moveTruck));
    moveTruck.registerKey("d", new Mt<>(), new Mt<>(), new Mt<>(), sc, record);
    t.checkExpect(moveTruck,
        new Truck(new GridPosn(2, 0), 3, true, false));

    // The score counter remains at 0
    t.checkExpect(sc, new ScoreCounter(0, true));
    t.checkExpect(record, new StateRecord(recordStack));


    // Do nothing if the move would cause a collision with another movable
    sc.registerReselect();
    record.startAction(moveCar);
    recordStack.push(new Action(moveCar));
    moveCar.registerKey("d",
        new Mt<>(),
        new Mt<>(),
        new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>())),
        sc, record);
    t.checkExpect(moveCar,
        new Car(new GridPosn(), true, 1, true));

    sc.registerReselect();
    record.startAction(moveNBox);
    recordStack.push(new Action(moveNBox));
    moveNBox.registerKey("d",
        new Mt<>(),
        new Mt<>(),
        new Cons<>(moveNBox, new Cons<>(moveTruck, new Mt<>())),
        sc, record);
    t.checkExpect(moveNBox,
        new NormalBox(new GridPosn(), true, 2, 1));

    // The score counter remains at 0
    t.checkExpect(sc, new ScoreCounter(0, true));
    t.checkExpect(record, new StateRecord(recordStack));


    // Do nothing if the move would cause a collision with a wall
    sc.registerReselect();
    record.startAction(moveCar);
    recordStack.push(new Action(moveCar));
    moveCar.registerKey("d", new Cons<>(w, new Mt<>()), new Mt<>(), new Mt<>(), sc, record);
    t.checkExpect(moveCar,
        new Car(new GridPosn(), true, 1, true));

    sc.registerReselect();
    record.startAction(moveNBox);
    recordStack.push(new Action(moveNBox));
    moveNBox.registerKey("d", new Cons<>(w, new Mt<>()), new Mt<>(), new Mt<>(), sc, record);
    t.checkExpect(moveNBox,
        new NormalBox(new GridPosn(), true, 2, 1));

    // The score counter remains at 0
    t.checkExpect(sc, new ScoreCounter(0, true));
    t.checkExpect(record, new StateRecord(recordStack));


    // Do nothing if we're trying to move over an exit (only on NormalBox)
    sc.registerReselect();
    record.startAction(moveNBox);
    recordStack.push(new Action(moveNBox));
    moveNBox.registerKey("d", new Mt<>(), new Cons<>(e, new Mt<>()), new Mt<>(), sc, record);
    t.checkExpect(moveNBox,
        new NormalBox(new GridPosn(), true, 2, 1));

    sc.registerReselect();
    record.startAction(movePBox);
    recordStack.push(new Action(movePBox, 1, 0));
    movePBox.registerKey("d", new Mt<>(), new Cons<>(e, new Mt<>()), new Mt<>(), sc, record);
    t.checkExpect(movePBox,
        new PlayerBox(new GridPosn(1, 0), true, 2, 1));

    // The score counter is now at 1, since we moved a box
    t.checkExpect(sc, new ScoreCounter(1, false));
    t.checkExpect(record, new StateRecord(recordStack));


    // Otherwise, move the AMovable
    // for the purposes of these tests, we simulate re-selections manually
    sc.registerReselect();
    record.startAction(moveCar);
    recordStack.push(new Action(moveCar));
    moveCar.registerKey("a",
        new Cons<>(w, new Mt<>()),
        new Mt<>(),
        new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>())),
        sc, record);
    t.checkExpect(moveCar,
        new Car(new GridPosn(-1, 0), true, 1, true));
    moveCar.registerKey("d",
        new Mt<>(),
        new Mt<>(),
        new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>())),
        sc, record);
    t.checkExpect(moveCar,
        new Car(new GridPosn(), true, 1, true));

    // The score counter is now at 2, since the same vehicle was moved twice.
    t.checkExpect(sc, new ScoreCounter(2, false));
    t.checkExpect(record, new StateRecord(recordStack));

    sc.registerReselect();
    record.startAction(moveTruck);
    recordStack.push(new Action(moveTruck));
    moveTruck.registerKey("w",
        new Mt<>(),
        new Mt<>(),
        new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>())),
        sc, record);
    t.checkExpect(moveTruck,
        new Truck(new GridPosn(2, -1), 3, true, false));
    moveTruck.registerKey("s",
        new Mt<>(),
        new Mt<>(),
        new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>())),
        sc, record);
    t.checkExpect(moveTruck,
        new Truck(new GridPosn(2, 0), 3, true, false));

    // The score counter is now at 3, since the same vehicle was moved twice.
    t.checkExpect(sc, new ScoreCounter(3, false));
    t.checkExpect(record, new StateRecord(recordStack));

    sc.registerReselect();
    record.startAction(moveNBox);
    recordStack.push(new Action(moveNBox, 1, -1));
    moveNBox.registerKey("w",
        new Mt<>(),
        new Mt<>(),
        new Cons<>(moveNBox, new Cons<>(moveTruck, new Mt<>())),
        sc, record);
    t.checkExpect(moveNBox,
        new NormalBox(new GridPosn(0, -1), true, 2, 1));
    moveNBox.registerKey("d",
        new Mt<>(),
        new Mt<>(),
        new Cons<>(moveNBox, new Cons<>(moveTruck, new Mt<>())),
        sc, record);
    t.checkExpect(moveNBox,
        new NormalBox(new GridPosn(1, -1), true, 2, 1));
    // The score counter is now at 4, since the same vehicle was moved twice.
    t.checkExpect(sc, new ScoreCounter(4, false));
    t.checkExpect(record, new StateRecord(recordStack));

    // Arrow keys should work as well
    sc.registerReselect();
    record.startAction(moveCar);
    recordStack.push(new Action(moveCar, -1, 0));
    moveCar.registerKey("left",
        new Cons<>(w, new Mt<>()),
        new Mt<>(),
        new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>())),
        sc, record);
    t.checkExpect(moveCar,
        new Car(new GridPosn(-1, 0), true, 1, true));
    // The score counter is now at 5.
    t.checkExpect(sc, new ScoreCounter(5, false));
  }


  void testMove(Tester t) {
    this.resetExamples();

    Car moveCar = new Car(new GridPosn(), false, 1, true);
    Truck moveTruck = new Truck(new GridPosn(2, 0), 3, false, false);

    NormalBox moveNBox = new NormalBox(new GridPosn(), true, 2, 1);
    PlayerBox movePBox = new PlayerBox(new GridPosn(), true, 2, 1);

    Wall w = new Wall(new GridPosn(2, 0));
    Exit e = new Exit(new GridPosn(2, 0));
    ScoreCounter sc = new ScoreCounter(0, false);
    StateRecord record = new StateRecord();
    Stack<Action> recordStack = new Stack<>();

    // Do nothing if the desired move isn't in the direction of movement
    sc.registerReselect();
    record.startAction(moveCar);
    recordStack.push(new Action(moveCar));
    moveCar.move(0, 1, new Mt<>(), new Mt<>(), new Mt<>(), sc, record);
    t.checkExpect(moveCar,
        new Car(new GridPosn(), false, 1, true));
    sc.registerReselect();
    record.startAction(moveTruck);
    recordStack.push(new Action(moveTruck));
    moveTruck.move(1, 0, new Mt<>(), new Mt<>(), new Mt<>(), sc, record);
    t.checkExpect(moveTruck,
        new Truck(new GridPosn(2, 0), 3, false, false));
    // The score counter remains at 0 and the record remains empty
    t.checkExpect(sc, new ScoreCounter(0, true));
    t.checkExpect(record, new StateRecord(recordStack));

    // Do nothing if the move would cause a collision with another movable
    sc.registerReselect();
    record.startAction(moveCar);
    recordStack.push(new Action(moveCar));
    moveCar.move(1, 0,
        new Mt<>(),
        new Mt<>(),
        new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>())),
        sc, record);
    t.checkExpect(moveCar,
        new Car(new GridPosn(), false, 1, true));
    sc.registerReselect();
    record.startAction(moveNBox);
    recordStack.push(new Action(moveNBox));
    moveNBox.move(1, 0,
        new Mt<>(),
        new Mt<>(),
        new Cons<>(moveNBox, new Cons<>(moveTruck, new Mt<>())),
        sc, record);
    t.checkExpect(moveNBox,
        new NormalBox(new GridPosn(), true, 2, 1));
    // The score counter remains at 0 and the record remains empty
    t.checkExpect(sc, new ScoreCounter(0, true));
    t.checkExpect(record, new StateRecord(recordStack));

    // Do nothing if the move would cause a collision with a wall
    sc.registerReselect();
    record.startAction(moveCar);
    recordStack.push(new Action(moveCar));
    moveCar.move(1, 0, new Cons<>(w, new Mt<>()), new Mt<>(), new Mt<>(), sc, record);
    t.checkExpect(moveCar,
        new Car(new GridPosn(), false, 1, true));
    sc.registerReselect();
    record.startAction(moveNBox);
    recordStack.push(new Action(moveNBox));
    moveNBox.move(1, 0, new Cons<>(w, new Mt<>()), new Mt<>(), new Mt<>(), sc, record);
    t.checkExpect(moveNBox,
        new NormalBox(new GridPosn(), true, 2, 1));
    // The score counter remains at 0 and the record remains empty
    t.checkExpect(sc, new ScoreCounter(0, true));
    t.checkExpect(record, new StateRecord(recordStack));

    // Do nothing if we're trying to move over an exit (only on NormalBox)
    sc.registerReselect();
    record.startAction(moveNBox);
    recordStack.push(new Action(moveNBox));
    moveNBox.move(1, 0, new Mt<>(), new Cons<>(e, new Mt<>()), new Mt<>(), sc, record);
    t.checkExpect(moveNBox,
        new NormalBox(new GridPosn(), true, 2, 1));
    sc.registerReselect();
    record.startAction(movePBox);
    recordStack.push(new Action(movePBox, 1, 0));
    movePBox.move(1, 0, new Mt<>(), new Cons<>(e, new Mt<>()), new Mt<>(), sc, record);
    t.checkExpect(movePBox,
        new PlayerBox(new GridPosn(1, 0), true, 2, 1));
    // The score counter goes to 1 and the record remains empty
    t.checkExpect(sc, new ScoreCounter(1, false));
    t.checkExpect(record, new StateRecord(recordStack));

    // Otherwise, move the movable
    sc.registerReselect();
    record.startAction(moveCar);
    recordStack.push(new Action(moveCar, -1, 0));
    moveCar.move(-1, 0,
        new Cons<>(w, new Mt<>()),
        new Mt<>(),
        new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>())), sc, record);
    t.checkExpect(moveCar,
        new Car(new GridPosn(-1, 0), false, 1, true));
    sc.registerReselect();
    record.startAction(moveNBox);
    recordStack.push(new Action(moveNBox, 1, -1));
    sc.registerReselect();
    moveNBox.move(1, -1,
        new Mt<>(),
        new Mt<>(),
        new Cons<>(moveNBox, new Cons<>(moveTruck, new Mt<>())), sc, record);
    t.checkExpect(moveNBox,
        new NormalBox(new GridPosn(1, -1), true, 2, 1));
    // The score counter increases to 3
    t.checkExpect(sc, new ScoreCounter(3, false));
    t.checkExpect(record, new StateRecord(recordStack));
  }

  // +----------------+
  // | AVehicle Tests |
  // +----------------+
  void testGetWidth(Tester t) {
    this.resetExamples();
    t.checkExpect(car1.getWidth(), 1);
    t.checkExpect(car2.getWidth(), 1);
    t.checkExpect(pCar1.getWidth(), 1);
    t.checkExpect(truck1.getWidth(), 1);
    t.checkExpect(truck2.getWidth(), 1);
  }

  void testGetLength(Tester t) {
    this.resetExamples();
    t.checkExpect(car1.getLength(), 2);
    t.checkExpect(car2.getLength(), 2);
    t.checkExpect(pCar1.getLength(), 2);
    t.checkExpect(truck1.getLength(), 3);
    t.checkExpect(truck2.getLength(), 3);
  }

  void testImagePath(Tester t) {
    // We need to convert to strings or else the tester library will hang
    this.resetExamples();
    t.checkExpect(car1.imagePath().toString(),
        Path.of("car", "car-selected.png").toString());
    t.checkExpect(car2.imagePath().toString(),
        Path.of("car", "car2.png").toString());
    t.checkExpect(pCar2.imagePath().toString(),
        Path.of("car", "car-player.png").toString());
    t.checkExpect(truck1.imagePath().toString(),
        Path.of("truck", "truck-selected.png").toString());
    t.checkExpect(truck2.imagePath().toString(),
        Path.of("truck", "truck1.png").toString());
  }

  // +------------+
  // | ABox Tests |
  // +------------+
  void testTilesetPath(Tester t) {
    // We need to convert to strings or else the tester library will hang
    this.resetExamples();
    t.checkExpect(nBox1.tilesetPath().toString(),
        Path.of("klotski", "normal-box.png").toString());
    t.checkExpect(nBox2.tilesetPath().toString(),
        Path.of("klotski", "normal-box-selected.png").toString());
    t.checkExpect(pBox1.tilesetPath().toString(),
        Path.of("klotski", "player-box.png").toString());
    t.checkExpect(pBox2.tilesetPath().toString(),
        Path.of("klotski", "player-box-selected.png").toString());
  }

  void testBoxXSize(Tester t) {
    this.resetExamples();

    t.checkExpect(nBox1.xSize(), 1);
    t.checkExpect(nBox2.xSize(), 2);
    t.checkExpect(pBox1.xSize(), 3);
    t.checkExpect(pBox2.xSize(), 2);
  }

  void testBoxYSize(Tester t) {
    this.resetExamples();

    t.checkExpect(nBox1.ySize(), 1);
    t.checkExpect(nBox2.ySize(), 3);
    t.checkExpect(pBox1.ySize(), 1);
    t.checkExpect(pBox2.ySize(), 2);
  }


}

// Examples for Functions.java ====================================================================

class ExamplesFunctions {

  // +----------+
  // | Examples |
  // +----------+

  // ... For use in functions
  // Walls
  Wall wall1 = new Wall(new GridPosn());
  Wall wall2 = new Wall(new GridPosn(2, 4));
  // Exits
  Exit exit1 = new Exit(new GridPosn());
  Exit exit2 = new Exit(new GridPosn(2, 4));
  // Cars
  Car car1 = new Car(new GridPosn(), true, 0, true);
  Car car2 = new Car(new GridPosn(0, 1), false, 2, false);
  Car car3 = new Car(new GridPosn(1, 0), false, 4, false);
  // PlayerCars
  PlayerCar pCar1 = new PlayerCar(new GridPosn(), true, true);
  PlayerCar pCar2 = new PlayerCar(new GridPosn(1, 1), false, false);
  // Trucks
  Truck truck1 = new Truck(new GridPosn(0, 2), 3, true, true);
  Truck truck2 = new Truck(new GridPosn(1, 1), 1, false, false);
  Truck truck3 = new Truck(new GridPosn(4, 1), 1, false, true);
  // Klotski pieces
  NormalBox nBox1 = new NormalBox(new GridPosn(), false, 1, 1);
  NormalBox nBox2 = new NormalBox(new GridPosn(2, 1), true, 2, 3);
  PlayerBox pBox1 = new PlayerBox(new GridPosn(), false, 3, 1);
  PlayerBox pBox2 = new PlayerBox(new GridPosn(1, 1), true, 2, 2);

  // Lists of IGameObjects
  IList<IMovable> vehicles = new Cons<>(car1, new Cons<>(pCar2, new Cons<>(truck1, new Mt<>())));
  IList<Wall> walls = new Cons<>(wall1, new Cons<>(wall2, new Mt<>()));
  IList<Exit> exits = new Cons<>(exit1, new Cons<>(exit2, new Mt<>()));
  // GridAreas
  GridArea r = new GridArea(new GridPosn(0, 1), new GridPosn(6, 6));

  // ... Function examples
  IntersectsPred<Wall> wallIntersectsWall1 = new IntersectsPred<>(wall1);
  IntersectsPred<Exit> exitIntersectsWall1 = new IntersectsPred<>(wall1);
  IntersectsPred<IMovable> movableIntersectsExit1 = new IntersectsPred<>(exit1);
  IntersectsPred<IMovable> movableIntersectsPCar2 = new IntersectsPred<>(pCar2);

  IntersectsAnyPred<Wall, Wall> wallIntersectsAnyWall1 = new IntersectsAnyPred<>(new Mt<>());
  IntersectsAnyPred<Wall, Wall> wallIntersectsAnyWall2 = new IntersectsAnyPred<>(walls);
  IntersectsAnyPred<Wall, Exit> wallIntersectsAnyExit = new IntersectsAnyPred<>(exits);
  IntersectsAnyPred<Exit, Wall> exitIntersectsAnyWall = new IntersectsAnyPred<>(walls);
  IntersectsAnyPred<IMovable, Wall> movableIntersectsAnyWall = new IntersectsAnyPred<>(walls);
  IntersectsAnyPred<IMovable, IMovable> movableIntersectsAnyMovable =
      new IntersectsAnyPred<>(vehicles);

  IntersectsAnyOtherPred<Wall> wallIntersectsAnyOther = new IntersectsAnyOtherPred<>();
  IntersectsAnyOtherPred<Exit> exitIntersectsAnyOther = new IntersectsAnyOtherPred<>();
  IntersectsAnyOtherPred<IMovable> movableIntersectsAnyOther = new IntersectsAnyOtherPred<>();

  WorldScene s = new WorldScene(0, 0);
  DrawToScene<Wall> drawWallToScene = new DrawToScene<>(s);
  DrawToScene<Exit> drawExitToScene = new DrawToScene<>(s);
  DrawToScene<IMovable> drawMovableToScene = new DrawToScene<>(s);

  InAreaPred<Wall> wallInAreaPred = new InAreaPred<>(r);
  InAreaPred<Exit> exitInAreaPred = new InAreaPred<>(r);
  InAreaPred<IMovable> movableInAreaPred = new InAreaPred<>(r);

  InWinningStatePred inWinningStatePred1 = new InWinningStatePred(new Mt<>());
  InWinningStatePred inWinningStatePred2 = new InWinningStatePred(exits);

  // +-------+
  // | Tests |
  // +-------+

  void testIntersectsPred(Tester t) {
    // For a more exhaustive test suite, see the "intersects()" tests. The `apply` method here
    // simply invokes intersects() on that.
    t.checkExpect(wallIntersectsWall1.apply(wall1), true);
    t.checkExpect(wallIntersectsWall1.apply(wall2), false);
    t.checkExpect(exitIntersectsWall1.apply(exit1), true);
    t.checkExpect(movableIntersectsExit1.apply(car1), true);
    t.checkExpect(movableIntersectsExit1.apply(car2), false);
    t.checkExpect(movableIntersectsPCar2.apply(car3), true);
    t.checkExpect(movableIntersectsPCar2.apply(truck1), true);
  }

  void testIntersectsAnyPred(Tester t) {
    t.checkExpect(wallIntersectsAnyWall1.apply(wall1), false);
    t.checkExpect(wallIntersectsAnyWall2.apply(wall1), true);
    t.checkExpect(wallIntersectsAnyWall2.apply(new Wall(new GridPosn(1, 1))), false);

    t.checkExpect(wallIntersectsAnyExit.apply(wall2), true);
    t.checkExpect(wallIntersectsAnyExit.apply(new Wall(new GridPosn(1, 1))), false);
    t.checkExpect(exitIntersectsAnyWall.apply(exit2), true);

    t.checkExpect(movableIntersectsAnyWall.apply(car1), true);
    t.checkExpect(movableIntersectsAnyMovable.apply(car2), true);
    t.checkExpect(movableIntersectsAnyWall.apply(truck1), false);
    t.checkExpect(movableIntersectsAnyMovable.apply(car1), true);
    t.checkExpect(movableIntersectsAnyMovable.apply(truck3), false);
    t.checkExpect(movableIntersectsAnyMovable.apply(nBox2), true);
  }

  void testIntersectsAnyOtherPred(Tester t) {
    t.checkExpect(wallIntersectsAnyOther.apply(wall1, walls), true);
    t.checkExpect(wallIntersectsAnyOther.apply(wall2, walls), true);
    t.checkExpect(wallIntersectsAnyOther.apply(new Wall(new GridPosn(1, 1)), walls), false);
    t.checkExpect(exitIntersectsAnyOther.apply(new Exit(new GridPosn()), exits), true);
    t.checkExpect(exitIntersectsAnyOther.apply(new Exit(new GridPosn(1, 1)), exits), false);
    t.checkExpect(movableIntersectsAnyOther.apply(car2, vehicles), true);
    t.checkExpect(movableIntersectsAnyOther.apply(truck3, vehicles), false);
    t.checkExpect(movableIntersectsAnyOther.apply(nBox2, new Cons<>(nBox1, new Mt<>())), false);
  }

  void testDrawToScene(Tester t) {
    // DrawTo has already been extensively tested, so we're just checking against that.
    WorldScene s2 = new WorldScene(0, 0);

    drawWallToScene.accept(wall2);
    wall2.drawTo(s2);
    t.checkExpect(s, s2);

    drawExitToScene.accept(exit1);
    exit1.drawTo(s2);
    t.checkExpect(s, s2);

    drawMovableToScene.accept(truck3);
    truck3.drawTo(s2);
    t.checkExpect(s, s2);

    drawMovableToScene.accept(nBox1);
    nBox1.drawTo(s2);
    t.checkExpect(s, s2);

    drawMovableToScene.accept(pBox1);
    pBox1.drawTo(s2);
    t.checkExpect(s, s2);
  }

  void testInAreaPred(Tester t) {
    t.checkExpect(wallInAreaPred.apply(wall2), true);
    t.checkExpect(wallInAreaPred.apply(new Wall(new GridPosn(-1, 0))), false);
    t.checkExpect(wallInAreaPred.apply(new Wall(new GridPosn(0, 6))), false);
    t.checkExpect(exitInAreaPred.apply(exit2), true);
    t.checkExpect(exitInAreaPred.apply(new Exit(new GridPosn(0, -1))), false);
    t.checkExpect(exitInAreaPred.apply(new Exit(new GridPosn(6, 0))), false);
    t.checkExpect(movableInAreaPred.apply(car1), false);
    t.checkExpect(movableInAreaPred.apply(truck2), true);
    t.checkExpect(movableInAreaPred.apply(truck3), false);
  }

  void testInWinningStatePred(Tester t) {
    t.checkExpect(inWinningStatePred1.apply(car1), true);
    t.checkExpect(inWinningStatePred1.apply(pCar1), false);
    t.checkExpect(inWinningStatePred2.apply(truck1), true);
    t.checkExpect(inWinningStatePred2.apply(pCar1), true);
    t.checkExpect(inWinningStatePred2.apply(pCar2), false);
    t.checkExpect(inWinningStatePred1.apply(nBox1), true);
    t.checkExpect(inWinningStatePred1.apply(pBox1), false);
    t.checkExpect(inWinningStatePred2.apply(pBox2), false);
  }
}

// Examples for Coordinates.java ==================================================================

class ExamplesCoordinates {
  
  // +----------+
  // | Examples |
  // +----------+

  GridPosn p1 = new GridPosn(0, 0);
  GridPosn p2 = new GridPosn(1, 3);
  GridPosn p3 = new GridPosn(2, -4);
  
  GridArea r1 = new GridArea(p1, p2);
  GridArea r2 = new GridArea(p1, new GridPosn(3, 3));
  GridArea r3 = new GridArea(new GridPosn(1, 0), new GridPosn(2, 3));
  GridArea r4 = new GridArea(new GridPosn(2, 0), new GridPosn(3, 3));
  GridArea r5 = new GridArea(new GridPosn(0, 1), new GridPosn(3, 2));
  
  // +--------------------+
  // | Tests for GridPosn |
  // +--------------------+
  void testGridPosnConstructors(Tester t) {
    t.checkExpect(p1, new GridPosn());
    t.checkExpect(p3, new GridPosn(new Posn(128, -256)));
  }
  
  void testSamePosn(Tester t) {
    t.checkExpect(p1.samePosn(new GridPosn()), true);
    t.checkExpect(p1.samePosn(new GridPosn(0, 1)), false);
    t.checkExpect(p1.samePosn(new GridPosn(1, 0)), false);
    t.checkExpect(p3.samePosn(new GridPosn(2, -4)), true);
  }
  
  void testOffset(Tester t) {
    t.checkExpect(p1.offset(0, 0), p1);
    t.checkExpect(p1.offset(1, 3), p2);
    t.checkExpect(p2.offset(1, -7), p3);
  }
  
  void testDrawPositioned(Tester t) {
    WorldImage sqr = new RectangleImage(64, 64, OutlineMode.SOLID, Color.BLACK);
    WorldScene scene1 = new WorldScene(512, 512);
    WorldScene scene2 = new WorldScene(512, 512);

    p1.drawPositioned(sqr, scene1);
    scene2.placeImageXY(sqr.movePinhole(-32, -32), 0, 0);
    t.checkExpect(scene1, scene2);

    p2.drawPositioned(sqr, scene1);
    scene2.placeImageXY(sqr.movePinhole(-32, -32), 64, 192);
    t.checkExpect(scene1, scene2);
  }
  
  void testCompareX(Tester t) {
    t.checkExpect(p1.compareX(p1), 0);
    t.checkExpect(p1.compareX(p2), -1);
    t.checkExpect(p2.compareX(p1), 1);
  }
  
  void testCompareY(Tester t) {
    t.checkExpect(p1.compareY(p1), 0);
    t.checkExpect(p1.compareY(p2), -3);
    t.checkExpect(p2.compareY(p1), 3);
  }
  
  // +--------------------+
  // | Tests for GridArea |
  // +--------------------+
  void testGridAreaConstructorEx(Tester t) {
    IllegalArgumentException ex = new IllegalArgumentException(
        "Cannot construct a GridArea with topLeft to the right of or below botRight"
    );
    t.checkConstructorException(ex, "GridArea", p2, p1);
    t.checkConstructorException(ex, "GridArea", p2, p3);
    t.checkConstructorException(ex, "GridArea", p3, p2);
  }
  
  void testIntersects(Tester t) {
    t.checkExpect(r1.intersects(r1), true);
    t.checkExpect(r3.intersects(r4), false);
    t.checkExpect(r3.intersects(r5), true);
    t.checkExpect(r5.intersects(r3), true);
  }
  
  void testContainsArea(Tester t) {
    t.checkExpect(r1.containsArea(r1), true);
    t.checkExpect(r3.containsArea(r5), false);
    t.checkExpect(r5.containsArea(r3), false);
    t.checkExpect(r2.containsArea(r3), true);
    t.checkExpect(r3.containsArea(r2), false);
  }
  
  void testContainsPosn(Tester t) {
    t.checkExpect(r2.containsPosn(new GridPosn(0, 0)), true);
    t.checkExpect(r2.containsPosn(new GridPosn(3, 3)), false);
    t.checkExpect(r2.containsPosn(new GridPosn(1, -1)), false);
    t.checkExpect(r2.containsPosn(new GridPosn(-1, 1)), false);
    t.checkExpect(r2.containsPosn(new GridPosn(1, 1)), true);
    t.checkExpect(r2.containsPosn(new GridPosn(1, 3)), false);
    t.checkExpect(r2.containsPosn(new GridPosn(3, 1)), false);
  }
}

// Examples for IList.java ========================================================================

// +-------------------+
// | Example functions |
// +-------------------+

// A test function that checks whether the given Integer is equal to the Integer provided
// during construction.
class EqualsPred implements Function<Integer, Boolean> {
  int other;
  
  EqualsPred(int other) {
    this.other = other;
  }
  
  // Determines whether the provided Integer is greater than the one provided during construction
  @Override
  public Boolean apply(Integer that) {
    return that.equals(other);
  }
}

// A test function that checks if there are any items in the rest of this list that are equal
// to the first argument
class AnyEqualInRestPred implements BiFunction<Integer, IList<Integer>, Boolean> {
  // Checks if there are any items in the rest of this list that are greater than the first
  // argument.
  @Override
  public Boolean apply(Integer that, IList<Integer> rest) {
    return rest.ormap(new EqualsPred(that));
  }
}

class ExamplesIList {
  
  // +---------------+
  // | Example lists |
  // +---------------+
  IList<Integer> mt = new Mt<>();
  IList<Integer> ints1 = new Cons<>(1, new Cons<>(7, new Cons<>(2, new Mt<>())));
  IList<Integer> ints2 = new Cons<>(1, new Cons<>(1, new Cons<>(1, new Mt<>())));
  IList<Integer> ints3 = new Cons<>(1, new Cons<>(7, new Cons<>(1, new Mt<>())));
  IList<String> strings = new Cons<>("foo", new Cons<>("bar", new Mt<>()));
  
  // +-------+
  // | Tests |
  // +-------+
  void testAndmap(Tester t) {
    t.checkExpect(mt.andmap(new EqualsPred(3)), true);
    t.checkExpect(ints1.andmap(new EqualsPred(3)), false);
    t.checkExpect(ints2.andmap(new EqualsPred(1)), true);
  }
  
  void testOrmap(Tester t) {
    t.checkExpect(mt.ormap(new EqualsPred(0)), false);
    t.checkExpect(ints1.ormap(new EqualsPred(2)), true);
    t.checkExpect(ints1.ormap(new EqualsPred(10)), false);
  }
  
  void testRestOrmap(Tester t) {
    t.checkExpect(mt.restOrmap(new AnyEqualInRestPred()), false);
    t.checkExpect(ints1.restOrmap(new AnyEqualInRestPred()), false);
    t.checkExpect(ints3.restOrmap(new AnyEqualInRestPred()), true);
  }
  
  void testWithout(Tester t) {
    t.checkExpect(mt.without(1), mt);
    t.checkExpect(ints1.without(1), new Cons<>(7, new Cons<>(2, new Mt<>())));
    t.checkExpect(ints2.without(1), new Cons<>(1, new Cons<>(1, new Mt<>())));
    t.checkExpect(ints1.without(3), ints1);
    
    Car c1 = new Car(new GridPosn(1, 1), false, 0, false);
    Car c2 = new Car(new GridPosn(1, 1), false, 0, false);
    t.checkExpect(new Cons<>(c1, new Mt<>()).without(c1), new Mt<>());
    t.checkExpect(new Cons<>(c1, new Mt<>()).without(c2), new Cons<>(c1, new Mt<>()));
  }

  void testForEach(Tester t) {
    WorldScene s1 = new WorldScene(0, 0);
    WorldScene s2 = new WorldScene(0, 0);
    Car car1 = new Car(new GridPosn(), false, 0, true);
    Car car2 = new Car(new GridPosn(1, 1), false, 0, false);

    IList<IMovable> cars = new Cons<>(car1, new Cons<>(car2, new Mt<>()));
    cars.forEach(new DrawToScene<>(s1));

    car1.drawTo(s2);
    car2.drawTo(s2);

    t.checkExpect(s1, s2);
  }
}

// Examples for Utils.java ====================================================================

class ExamplesParser {
  Parser p1 = new Parser(" +|-X", new GridPosn(0, 0));
  Parser p2 = new Parser("c");
  Parser p3 = new Parser("C");
  Parser p4 = new Parser("P");
  Parser p5 = new Parser("t");
  Parser p6 = new Parser("CPTc |\n"
                             + "   p -\n"
                             + "X+ t  ", new GridPosn(1, 0));
  Parser p7 = new Parser("  +--+ +----+\n"
                             + "  |  | |t  C|\n"
                             + "+-+CC+-+C   +--+\n"
                             + "|p             X\n"
                             + "+--------------+");
  
  // +-------+
  // | Tests |
  // +-------+
  void testLoadVehicles(Tester t) {
    t.checkExpect(p1.loadVehicles(), new Mt<>());
    t.checkExpect(p2.loadVehicles(),
        new Cons<>(new Car(new GridPosn(0, 0), false, 1, true), new Mt<>()));
    t.checkExpect(p3.loadVehicles(),
        new Cons<>(new Car(new GridPosn(0, 0), false, 1, false), new Mt<>()));
    t.checkExpect(p4.loadVehicles(),
        new Cons<>(new PlayerCar(new GridPosn(0, 0), false, false), new Mt<>()));
    t.checkExpect(p5.loadVehicles(),
        new Cons<>(new Truck(new GridPosn(0, 0), 1, false, true), new Mt<>()));
    t.checkExpect(p6.loadVehicles(),
        new Cons<>(
            new Car(new GridPosn(0, 0), false, 3, false),
            new Cons<>(new PlayerCar(new GridPosn(1, 0), true, false),
                new Cons<>(new Truck(new GridPosn(2, 0), 0, false, false),
                    new Cons<>(new Car(new GridPosn(3, 0), false, 3, true),
                        new Cons<>(new PlayerCar(new GridPosn(3, 1), false, true),
                            new Cons<>(new Truck(new GridPosn(3, 2), 1, false, true),
                                new Mt<>())))))));
  }
  
  void testLoadRemainingVehicles(Tester t) {
    t.checkExpect(p1.loadMovables(" +|-X", 0, 0, new Random(6), new GridPosn(0, 0)),
        new Mt<>());
    t.checkExpect(p1.loadMovables("c", 0, 0, new Random(6), new GridPosn(-1, -1)),
        new Cons<>(new Car(new GridPosn(0, 0), false, 1, true), new Mt<>()));
    t.checkExpect(p1.loadMovables("c", 4, 2, new Random(6), new GridPosn(-1, -1)),
        new Cons<>(new Car(new GridPosn(4, 2), false, 1, true), new Mt<>()));
    t.checkExpect(p1.loadMovables("c", 4, 2, new Random(6), new GridPosn(4, 2)),
        new Cons<>(new Car(new GridPosn(4, 2), true, 1, true), new Mt<>()));
    t.checkExpect(p1.loadMovables("C", 0, 0, new Random(6), new GridPosn(-1, -1)),
        new Cons<>(new Car(new GridPosn(0, 0), false, 1, false), new Mt<>()));
    t.checkExpect(p1.loadMovables("P", 0, 0, new Random(6), new GridPosn(-1, -1)),
        new Cons<>(new PlayerCar(new GridPosn(0, 0), false, false), new Mt<>()));
    t.checkExpect(p1.loadMovables("t", 0, 0, new Random(6), new GridPosn(-1, -1)),
        new Cons<>(new Truck(new GridPosn(0, 0), 1, false, true), new Mt<>()));
    t.checkExpect(p2.loadMovables("CPTc |\n"
                                      + "   p -\n"
                                      + "X+ t  ", 0, 0, new Random(6), new GridPosn(1, 0)),
        new Cons<>(
            new Car(new GridPosn(0, 0), false, 3, false),
            new Cons<>(new PlayerCar(new GridPosn(1, 0), true, false),
                new Cons<>(new Truck(new GridPosn(2, 0), 0, false, false),
                    new Cons<>(new Car(new GridPosn(3, 0), false, 3, true),
                        new Cons<>(new PlayerCar(new GridPosn(3, 1), false, true),
                            new Cons<>(new Truck(new GridPosn(3, 2), 1, false, true),
                                new Mt<>())))))));
  }
  
  void testLoadWalls(Tester t) {
    t.checkExpect(new Parser(" CcPpTtX").loadWalls(), new Mt<>());
    t.checkExpect(new Parser("+|-").loadWalls(),
        new Cons<>(new Wall(new GridPosn(0, 0)),
            new Cons<>(new Wall(new GridPosn(1, 0)),
                new Cons<>(new Wall(new GridPosn(2, 0)),
                    new Mt<>()))));
    t.checkExpect(new Parser(" + | - ").loadWalls(),
        new Cons<>(new Wall(new GridPosn(1, 0)),
            new Cons<>(new Wall(new GridPosn(3, 0)),
                new Cons<>(new Wall(new GridPosn(5, 0)),
                    new Mt<>()))));
    t.checkExpect(p6.loadWalls(),
        new Cons<>(new Wall(new GridPosn(5, 0)),
            new Cons<>(new Wall(new GridPosn(5, 1)),
                new Cons<>(new Wall(new GridPosn(1, 2)),
                    new Mt<>()))));
  }
  
  void testLoadRemainingWalls(Tester t) {
    t.checkExpect(p1.loadRemainingWalls(" CcPpTtX", 0, 0), new Mt<>());
    t.checkExpect(p1.loadRemainingWalls("+|-", 0, 0),
        new Cons<>(new Wall(new GridPosn(0, 0)),
            new Cons<>(new Wall(new GridPosn(1, 0)),
                new Cons<>(new Wall(new GridPosn(2, 0)),
                    new Mt<>()))));
    t.checkExpect(p1.loadRemainingWalls(" + | - ", 0, 0),
        new Cons<>(new Wall(new GridPosn(1, 0)),
            new Cons<>(new Wall(new GridPosn(3, 0)),
                new Cons<>(new Wall(new GridPosn(5, 0)),
                    new Mt<>()))));
    t.checkExpect(p1.loadRemainingWalls(" + | - ", 2, 1),
        new Cons<>(new Wall(new GridPosn(3, 1)),
            new Cons<>(new Wall(new GridPosn(5, 1)),
                new Cons<>(new Wall(new GridPosn(7, 1)),
                    new Mt<>()))));
    t.checkExpect(p2.loadRemainingWalls("CPTc |\n"
                                   + "   p -\n"
                                   + "X+ t  ", 0, 0),
        new Cons<>(new Wall(new GridPosn(5, 0)),
            new Cons<>(new Wall(new GridPosn(5, 1)),
                new Cons<>(new Wall(new GridPosn(1, 2)),
                    new Mt<>()))));
  }
  
  void testLoadExits(Tester t) {
    t.checkExpect(new Parser(" CcPpTt+|-").loadExits(), new Mt<>());
    t.checkExpect(new Parser("X").loadExits(),
        new Cons<>(new Exit(new GridPosn(0, 0)),
            new Mt<>()));
    t.checkExpect(new Parser(" X X ", new GridPosn(1, 0)).loadExits(),
        new Cons<>(new Exit(new GridPosn(1, 0)),
            new Cons<>(new Exit(new GridPosn(3, 0)),
                    new Mt<>())));
    t.checkExpect(p6.loadExits(),
        new Cons<>(new Exit(new GridPosn(0, 2)),
            new Mt<>()));
  }
  
  void testLoadRemainingExits(Tester t) {
    t.checkExpect(p1.loadRemainingExits(" CcPpTt+|-", 0, 0), new Mt<>());
    t.checkExpect(p1.loadRemainingExits("X", 0, 0),
        new Cons<>(new Exit(new GridPosn(0, 0)),
            new Mt<>()));
    t.checkExpect(p1.loadRemainingExits(" X X ", 0, 0),
        new Cons<>(new Exit(new GridPosn(1, 0)),
            new Cons<>(new Exit(new GridPosn(3, 0)),
                new Mt<>())));
    t.checkExpect(p1.loadRemainingExits(" X X ", 2, 1),
        new Cons<>(new Exit(new GridPosn(3, 1)),
            new Cons<>(new Exit(new GridPosn(5, 1)),
                new Mt<>())));
    t.checkExpect(p2.loadRemainingExits("CPTc |\n"
                                            + "   p X\n"
                                            + "X+ t  ", 0, 0),
        new Cons<>(new Exit(new GridPosn(5, 1)),
            new Cons<>(new Exit(new GridPosn(0, 2)),
                new Mt<>())));
  }
  
  void testLoadWidth(Tester t) {
    t.checkExpect(new Parser(" ").loadWidth(), 1);
    t.checkExpect(new Parser("       \n       ").loadWidth(), 7);
    t.checkExpect(new Parser("|+ p X cC |").loadWidth(), 11);
    t.checkExpect(new Parser("+------+\n"
                                   + "|c    T|\n"
                                   + "|T  T  |\n"
                                   + "| p    X\n"
                                   + "|      |\n"
                                   + "|C   c |\n"
                                   + "|   t  |\n"
                                   + "+------+").loadWidth(), 8);
    
    
  }
  
  void testLoadHeight(Tester t) {
    t.checkExpect(new Parser(" ").loadHeight(), 1);
    t.checkExpect(new Parser("   ").loadHeight(), 1);
    t.checkExpect(new Parser(" \n ").loadHeight(), 2);
    t.checkExpect(new Parser("x\n|x|\n\n").loadHeight(), 4);
    t.checkExpect(new Parser(
        "+------+\n"
            + "|c P  T|\n"
            + "|T  T  |\n"
            + "| p    X\n"
            + "|      ||\n"
            + "+--X---+").loadHeight(), 6);
  }

  // Tests for SpriteLoader ==================================================

  void testSpriteLoaderFromSpritesDir(Tester t) {
    SpriteLoader sl = new SpriteLoader();
    t.checkExpect(sl.fromSpritesDir(Path.of("bush.png")),
        new FromFileImage("sprites/bush.png"));
    t.checkExpect(sl.fromSpritesDir(Path.of("truck", "truck0.png")),
        new FromFileImage("sprites/truck/truck0.png"));
  }
}

// Examples for Tiles.java ========================================================================

class ExamplesTiles {
  
  // +----------+
  // | Examples |
  // +----------+

  // Automatically creating a OneSlice
  ITileset oneSliceAuto = new OneSlice(Path.of("grid-cell.png"));
  // Manually creating the same OneSlice
  WorldImage gCellSprite = new SpriteLoader().fromSpritesDir(Path.of("grid-cell.png"));
  ITileset oneSliceManual = new OneSlice(gCellSprite);

  // Automatically creating a NineSlice
  NineSlice nineSliceAuto = new NineSlice(Path.of("klotski","normal-box.png"));
  // Manually creating an identical NineSlice
  WorldImage spriteSheet = new SpriteLoader().fromSpritesDir(Path.of("klotski", "normal-box.png"));
  WorldImage topLeft = new CropImage(0, 0, 32, 32, spriteSheet);
  WorldImage topMid = new CropImage(32, 0, 32, 32, spriteSheet);
  WorldImage topRight = new CropImage(64, 0, 32, 32, spriteSheet);
  WorldImage midLeft = new CropImage(0, 32, 32, 32, spriteSheet);
  WorldImage midMid = new CropImage(32, 32, 32, 32, spriteSheet);
  WorldImage midRight = new CropImage(64, 32, 32, 32, spriteSheet);
  WorldImage botLeft = new CropImage(0, 64, 32, 32, spriteSheet);
  WorldImage botMid = new CropImage(32, 64, 32, 32, spriteSheet);
  WorldImage botRight = new CropImage(64, 64, 32, 32, spriteSheet);
  ITileset nineSliceManual = new NineSlice(
      topLeft, topMid, topRight,
      midLeft, midMid, midRight,
      botLeft, botMid, botRight
  );

  // Creating TiledImages from the above OneSlice and NineSlice
  TiledImage tiMt = new TiledImage(oneSliceAuto, 0, 0);
  TiledImage tiMtVert = new TiledImage(oneSliceAuto, 0, 2);
  TiledImage tiMtHoriz = new TiledImage(oneSliceAuto, 2, 0);
  TiledImage ti1 = new TiledImage(oneSliceAuto, 1, 1);
  TiledImage ti2 = new TiledImage(oneSliceAuto, 4, 1);
  TiledImage ti3 = new TiledImage(oneSliceAuto, 1, 5);
  TiledImage ti4 = new TiledImage(oneSliceAuto, 4, 5);
  TiledImage ti5 = new TiledImage(nineSliceAuto, 4, 5);

  
  // +--------------------+
  // | Tests for ITileset |
  // +--------------------+
  void testTilesetConstructors(Tester t) {
    t.checkExpect(oneSliceAuto, oneSliceManual);
    t.checkExpect(nineSliceAuto, nineSliceManual);
  }

  void testDrawTile(Tester t) {
    // OneSlice always returns the same image regardless of the inputs
    t.checkExpect(oneSliceAuto.drawTile(false, false, false, false), gCellSprite);
    t.checkExpect(oneSliceAuto.drawTile(true, true, false, false), gCellSprite);
    t.checkExpect(oneSliceAuto.drawTile(false, true, true, true), gCellSprite);

    // NineSlice changes depending on the inputs according to the following truth table:
    t.checkExpect(nineSliceAuto.drawTile(false, false, false, false), midMid);
    t.checkExpect(nineSliceAuto.drawTile(false, false, false, true), midRight);
    t.checkExpect(nineSliceAuto.drawTile(false, false, true, false), midLeft);
    t.checkExpect(nineSliceAuto.drawTile(false, false, true, true), midLeft);
    t.checkExpect(nineSliceAuto.drawTile(false, true, false, false), botMid);
    t.checkExpect(nineSliceAuto.drawTile(false, true, false, true), botRight);
    t.checkExpect(nineSliceAuto.drawTile(false, true, true, false), botLeft);
    t.checkExpect(nineSliceAuto.drawTile(false, true, true, true), botLeft);
    t.checkExpect(nineSliceAuto.drawTile(true, false, false, false), topMid);
    t.checkExpect(nineSliceAuto.drawTile(true, false, false, true), topRight);
    t.checkExpect(nineSliceAuto.drawTile(true, false, true, false), topLeft);
    t.checkExpect(nineSliceAuto.drawTile(true, false, true, true), topLeft);
    t.checkExpect(nineSliceAuto.drawTile(true, true, false, false), topMid);
    t.checkExpect(nineSliceAuto.drawTile(true, true, false, true), topRight);
    t.checkExpect(nineSliceAuto.drawTile(true, true, true, false), topLeft);
    t.checkExpect(nineSliceAuto.drawTile(true, true, true, true), topLeft);
  }

  // +----------------------+
  // | Tests for TiledImage |
  // +----------------------+
  void testConstructorEx(Tester t) {
    t.checkConstructorException(
        new IllegalArgumentException("tilesX and tilesY must both be non-negative integers"),
        "TiledImage",
        oneSliceAuto, -1, 1
    );
    t.checkConstructorException(
        new IllegalArgumentException("tilesX and tilesY must both be non-negative integers"),
        "TiledImage",
        oneSliceAuto, 1, -1
    );
    t.checkConstructorException(
        new IllegalArgumentException("tilesX and tilesY must both be non-negative integers"),
        "TiledImage",
        oneSliceAuto, -1, -1
    );
  }
  
  void testDrawRow(Tester t) {
    // Drawing a row with one element and an empty row
    t.checkExpect(tiMt.drawRow(false, false),
        new EmptyImage());
    t.checkExpect(tiMtVert.drawRow(false, false),
        new EmptyImage());
    t.checkExpect(ti1.drawRow(false, false),
        new BesideImage(gCellSprite, new EmptyImage()));

    // When making a row with a one-sliced image, the image should always be the same
    t.checkExpect(ti4.drawRow(false, false),
        new BesideImage(gCellSprite,
                new BesideImage(gCellSprite,
                    new BesideImage(gCellSprite,
                        new BesideImage(gCellSprite, new EmptyImage())))));
    t.checkExpect(ti4.drawRow(true, false),
        new BesideImage(gCellSprite,
            new BesideImage(gCellSprite,
                new BesideImage(gCellSprite,
                    new BesideImage(gCellSprite, new EmptyImage())))));

    // When making a row with a nine-sliced tileset, the image should be dependent on its position
    // according to the tileset
    t.checkExpect(ti5.drawRow(false, false),
        new BesideImage(midLeft,
            new BesideImage(midMid,
                new BesideImage(midMid,
                    new BesideImage(midRight, new EmptyImage())))));
    t.checkExpect(ti5.drawRow(false, true),
        new BesideImage(botLeft,
            new BesideImage(botMid,
                new BesideImage(botMid,
                    new BesideImage(botRight, new EmptyImage())))));
    t.checkExpect(ti5.drawRow(true, false),
        new BesideImage(topLeft,
            new BesideImage(topMid,
                new BesideImage(topMid,
                    new BesideImage(topRight, new EmptyImage())))));
  }

  void testDraw(Tester t) {
    WorldImage gCellRow = new BesideImage(gCellSprite,
        new BesideImage(gCellSprite,
            new BesideImage(gCellSprite,
                new BesideImage(gCellSprite,
                    new EmptyImage()))));

    WorldImage boxTopRow = new BesideImage(topLeft,
        new BesideImage(topMid,
            new BesideImage(topMid,
                new BesideImage(topRight,
                    new EmptyImage()))));
    WorldImage boxMidRow = new BesideImage(midLeft,
        new BesideImage(midMid,
            new BesideImage(midMid,
                new BesideImage(midRight,
                    new EmptyImage()))));
    WorldImage boxBotRow = new BesideImage(botLeft,
        new BesideImage(botMid,
            new BesideImage(botMid,
                new BesideImage(botRight,
                    new EmptyImage()))));

    // Drawing a TiledImage with one element or empty
    t.checkExpect(tiMt.draw(),
        new EmptyImage());
    t.checkExpect(tiMtHoriz.draw(),
        new EmptyImage());
    t.checkExpect(tiMtVert.draw(),
        new AboveImage(new EmptyImage(),
            new AboveImage(new EmptyImage(), new EmptyImage())));
    t.checkExpect(ti1.draw(),
        new AboveImage(new BesideImage(gCellSprite), new EmptyImage()));

    // Drawing a TiledImage with multiple rows
    t.checkExpect(ti4.draw(),
        new AboveImage(gCellRow,
            new AboveImage(gCellRow,
                new AboveImage(gCellRow,
                    new AboveImage(gCellRow,
                        new AboveImage(gCellRow,
                            new EmptyImage()))))));
    t.checkExpect(ti5.draw(),
        new AboveImage(boxTopRow,
            new AboveImage(boxMidRow,
                new AboveImage(boxMidRow,
                    new AboveImage(boxMidRow,
                        new AboveImage(boxBotRow,
                            new EmptyImage()))))));
  }
}

// Tests for Recording.java ==============================================

class ExamplesRecording {
  NormalBox nb;
  StateRecord mtRecord;
  StateRecord record2;

  IList<IMovable> movables;

  ScoreCounter sc;

  Action a1;
  Action a2;
  ScoreCounter sc2;

  void resetExamples() {
    mtRecord = new StateRecord();
    nb = new NormalBox(
        new GridPosn(2, 3),
        false,
        2, 2
    );

    Stack<Action> st = new Stack<>();
    a1 = new Action(nb, 1, 1);
    a2 = new Action(nb, -1, 2);
    st.add(a1);
    st.add(a2);

    record2 = new StateRecord(st);

    movables = new Cons<>(nb, new Mt<>());

    sc = new ScoreCounter(5, false);
    sc2 = new ScoreCounter(12, false);
  }


  // Tests for StateRecord ====================================
  void testStateRecordUndo(Tester t) {
    resetExamples();
    t.checkExpect(mtRecord.record.size(), 0);
    mtRecord.undo(sc, movables);
    t.checkExpect(mtRecord.record.size(), 0);

    t.checkExpect(record2.record.size(), 2);
    record2.undo(sc2, movables);
    t.checkExpect(record2.record.size(), 1);
    t.checkExpect(nb.posn.y, 1);
    t.checkExpect(nb.posn.x, 3);
  }


  void testStateRecordRegisterMove(Tester t) {
    resetExamples();

    record2.registerMove(1, 1);
    t.checkExpect(a2.dx, 0);
    t.checkExpect(a2.dy, 3);
  }

  void testStateRecordStartAction(Tester t) {
    resetExamples();

    mtRecord.startAction(nb);
    t.checkExpect(mtRecord.record.peek(), new Action(nb, 0, 0));
  }

  // Tests for Action =========================================

  void testActionUndo(Tester t) {
    resetExamples();

    a2.undo(sc2);

    t.checkExpect(nb.posn, new GridPosn(3, 1));
  }

  void testActionRegisterMove(Tester t) {
    resetExamples();

    a2.registerMove(3, 3);
    t.checkExpect(a2.dx, 2);
    t.checkExpect(a2.dy, 5);
  }

  // Tests for ScoreCounter =============================

  void testScoreRegisterReselect(Tester t) {
    resetExamples();

    sc.registerReselect();
    t.checkExpect(sc.hasNewSelection, true);
    sc2.registerReselect();
    t.checkExpect(sc2.hasNewSelection, true);
  }

  void testScoreRegisterMove(Tester t) {
    resetExamples();

    sc.registerMove();
    t.checkExpect(sc.score, 5);
    sc2.hasNewSelection = true;
    sc2.registerMove();
    t.checkExpect(sc2.hasNewSelection, false);
    t.checkExpect(sc2.score, 13);
  }

  void testScoreIncrement(Tester t) {
    resetExamples();

    sc.increment();
    t.checkExpect(sc.score, 6);
    sc2.increment();
    t.checkExpect(sc2.score, 13);
  }

  void testScoreDraw(Tester t) {
    resetExamples();

    t.checkExpect(sc.toImage(), new TextImage("score: 5", 20, FontStyle.BOLD, Color.white)
        .movePinhole(-10, -10));
    t.checkExpect(sc2.toImage(), new TextImage("score: 12", 20, FontStyle.BOLD, Color.white)
        .movePinhole(-10, -10));
  }
}