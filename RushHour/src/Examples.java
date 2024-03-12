import javalib.funworld.WorldScene;
import javalib.worldimages.*;
import tester.Tester;

import java.awt.*;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

// Examples for Game.java =========================================================================

class ExamplesGame {
  
  // +----------+
  // | Examples |
  // +----------+
  
  // ==== Provided example levels ====
  
  Level level1 = new Level("+------+\n"
                               + "|c    T|\n"
                               + "|T  T  |\n"
                               + "| p    X\n"
                               + "|      |\n"
                               + "|C   c |\n"
                               + "|   t  |\n"
                               + "+------+");
  
  Level level2 = new Level("+------+\n"
                               + "|Tc T  |\n"
                               + "|      |\n"
                               + "| p    X\n"
                               + "|  Ct  |\n"
                               + "|     C|\n"
                               + "|  t   |\n"
                               + "+------+");
  
  Level level3 = new Level("+------+\n"
                               + "|c CT  |\n"
                               + "|T     |\n"
                               + "| p    X\n"
                               + "| t    |\n"
                               + "|      |\n"
                               + "|  t   |\n"
                               + "+------+");
  
  Level level4 = new Level("+------+\n"
                               + "|c  t  |\n"
                               + "|   Cc |\n"
                               + "|Cp   TX\n"
                               + "|  Tc  |\n"
                               + "|c     |\n"
                               + "|   t  |\n"
                               + "+------+");
  
  // ==== Other extraneous (but valid) cases ====
  
  // level1, but with the player car selected
  Level level5 = new Level("+------+\n"
                               + "|c    T|\n"
                               + "|T  T  |\n"
                               + "| p    X\n"
                               + "|      |\n"
                               + "|C   c |\n"
                               + "|   t  |\n"
                               + "+------+", new GridPosn(2, 3));
  
  // No walls
  Level level6 = new Level("        \n"
                               + " c    T \n"
                               + " T  T   \n"
                               + "  p    X\n"
                               + "        \n"
                               + "        ");
  
  // No cars
  Level level7 = new Level("+------+\n"
                               + "|      |\n"
                               + "|      |\n"
                               + "|      X\n"
                               + "|      |\n"
                               + "+------+");
  
  // No exits
  Level level8 = new Level("+------+\n"
                               + "|c    T|\n"
                               + "|T  T  |\n"
                               + "| p    |\n"
                               + "|      |\n"
                               + "+------+");
  
  // Multiple player cars and exits
  Level level9 = new Level("+------+\n"
                               + "|c P  T|\n"
                               + "|T  T  |\n"
                               + "| p    X\n"
                               + "|      |\n"
                               + "+--X---+");
  
  // Walls within the level
  Level level10 = new Level("+------+\n"
                               + "|c    T|\n"
                               + "|T +T  |\n"
                               + "| p    X\n"
                               + "|  +   |\n"
                               + "+------+");
  
  // Wider level shape
  Level level11 = new Level("+---------+\n"
                                + "|c    T   |\n"
                                + "|T  C     |\n"
                                + "| p       X\n"
                                + "|   c     |\n"
                                + "+---------+");
  
  // Completely empty, 1x1
  Level level12 = new Level(" ");
  
  // Very simple, including every possible element, for testing
  Level level13 = new Level("CPTc |\n"
                                + "   p -\n"
                                + "X+ t  ");
  
  // Very simple, including every possible element, for testing
  Level level14 = new Level("CPTc |\n"
                                + "   p -\n"
                                + "X+ t  ", new GridPosn(3, 1));
  
  // Game is in a winning state with one PlayerCar
  Level level15 = new Level("+------+\n"
                               + "|c     |\n"
                               + "|T  T  |\n"
                               + "|     pX\n"
                               + "|      |\n"
                               + "+------+");
  
  // Game is in a winning state with two PlayerCars
  Level level16 = new Level("+------+\n"
                                + "|c     |\n"
                                + "|T  T  |\n"
                                + "|     pX\n"
                                + "|  P   |\n"
                                + "+--X---+");
  
  // Game is in a non-winning state with two PlayerCars (only one overlapping an exit)
  Level level17 = new Level("+------+\n"
                                + "|c     |\n"
                                + "|T  T  |\n"
                                + "|  P  pX\n"
                                + "|      |\n"
                                + "+--X---+");
  
  // Irregular board shape
  Level level18 = new Level("  +--+ +----+\n"
                                + "  |  | |t  C|\n"
                                + "+-+CC+-+C   +--+\n"
                                + "|p             X\n"
                                + "+--------------+");
  
  
  Game game1 = new Game(level1);
  Game game2 = new Game(level2);
  Game game3 = new Game(level3);
  Game game4 = new Game(level4);
  Game game5 = new Game(level5);
  Game game6 = new Game(level18);
  
  // +------------+
  // | Game Tests |
  // +------------+
  
  void testBigBang(Tester t) {
    // Runs the game via bigBang()
    game3.bigBang();
  }
  
  void testMakeScene(Tester t) {
    // Since we've tested Level.draw(), we're checking against its output in this test
    t.checkExpect(game1.makeScene(), level1.draw());
    t.checkExpect(game2.makeScene(), level2.draw());
  }
  
  void testOnKeyEvent(Tester t) {
    Game g = new Game(
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T     |\n"
                      + "| p    X\n"
                      + "|      |\n"
                      + "|C   c |\n"
                      + "|   t  |\n"
                      + "+------+", new GridPosn(2, 3))
    );
    t.checkExpect(g.onKeyEvent("d"),
        new Game(
            new Level("+------+\n"
                          + "|c    T|\n"
                          + "|T     |\n"
                          + "|  p   X\n"
                          + "|      |\n"
                          + "|C   c |\n"
                          + "|   t  |\n"
                          + "+------+", new GridPosn(3, 3))
        ));
    // blocked by vehicle
    t.checkExpect(game5.onKeyEvent("d"),
        new Game(
            new Level("+------+\n"
                          + "|c    T|\n"
                          + "|T  T  |\n"
                          + "| p    X\n"
                          + "|      |\n"
                          + "|C   c |\n"
                          + "|   t  |\n"
                          + "+------+", new GridPosn(2, 3))
        ));
    // cannot move truck down because blocked by obstacle
    Game ob = new Game(new Level("+------+\n"
                                  + "|c    T|\n"
                                  + "|T +T  |\n"
                                  + "| p    X\n"
                                  + "|  +  +|\n"
                                  + "+------+", new GridPosn(5, 0)));
    t.checkExpect(ob.onKeyEvent("s"), ob);
    
    // no selected vehicle
    t.checkExpect(game4.onKeyEvent("s"), game4);
    // wrong direction
    t.checkExpect(ob.onKeyEvent("a"), ob);
  }
  
  
  void testLastScene(Tester t) {
    Game g = new Game(
        new Level("+------+\n"
                      + "|c     |\n"
                      + "|T  T  |\n"
                      + "|     pX\n"
                      + "|      |\n"
                      + "|C     |\n"
                      + "|   t  |\n"
                      + "+------+")
    );
    t.checkExpect(g.lastScene(""), g.level.draw().placeImageXY(
            new FromFileImage("sprites/you-win.png"),
            g.level.getWidthPixels() / 2,
            g.level.getHeightPixels() / 2));
  }
  
  void testShouldWorldEnd(Tester t) {
    t.checkExpect(game1.shouldWorldEnd(), false);
    Game g = new Game(
        new Level("+------+\n"
                      + "|c     |\n"
                      + "|T  T  |\n"
                      + "|     pX\n"
                      + "|      |\n"
                      + "|C     |\n"
                      + "|   t  |\n"
                      + "+------+")
    );
    t.checkExpect(g.shouldWorldEnd(), true);
    Game g2 = new Game(
        new Level("+------+\n"
                      + "|c     |\n"
                      + "|T  T  |\n"
                      + "|    p X\n"
                      + "|      |\n"
                      + "|C     |\n"
                      + "|   t  |\n"
                      + "+------+")
    );
    t.checkExpect(g2.shouldWorldEnd(), false);
  }
  
  void testOnMouseClicked(Tester t) {
    // Clicking on the player car to select it
    t.checkExpect(game1.onMouseClicked(new Posn(130, 200)), game5);
    // Doing nothing by clicking on a wall
    t.checkExpect(game1.onMouseClicked(new Posn(30, 30)), game1);
    // Deselecting the player car
    t.checkExpect(game5.onMouseClicked(new Posn(30, 30)), game1);
  }
  
  // +-------------+
  // | Level Tests |
  // +-------------+
  void testConstructorEx(Tester t) {
    // Grid has a width of 0
    t.checkConstructorException(
        new IllegalArgumentException("Grid width must be greater than zero"),
        "Level",
        new Mt<AVehicle>(), new Mt<Wall>(), new Mt<Exit>(), 0, 7);
    // Grid has a height of 0
    t.checkConstructorException(
        new IllegalArgumentException("Grid height must be greater than zero"),
        "Level",
        new Mt<AVehicle>(), new Mt<Wall>(), new Mt<Exit>(), 8, 0);
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
    t.checkExpect(level12, new Level(new Mt<>(), new Mt<>(), new Mt<>(), 1, 1));
    t.checkExpect(level13, new Level(
        new Cons<>(
            new Car(new GridPosn(0, 0), 3, false, false),
            new Cons<>(
                new PlayerCar(new GridPosn(1, 0), false, false),
                new Cons<>(
                    new Truck(new GridPosn(2, 0), 0, false, false),
                    new Cons<>(
                        new Car(new GridPosn(3, 0), 3, true, false),
                        new Cons<>(
                            new PlayerCar(new GridPosn(3, 1),true, false),
                            new Cons<>(
                                new Truck(new GridPosn(3, 2), 1, true, false),
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
        6, 3));
  }
  
  // Tests the third constructor, which builds a level from a String with the car at the
  // specified position (if any) selected
  void testConstructor3(Tester t) {
    t.checkExpect(new Level(" ", new GridPosn(-1, -1)), level12);
    t.checkExpect(level14, new Level(
        new Cons<>(
            new Car(new GridPosn(0, 0), 3, false, false),
            new Cons<>(
                new PlayerCar(new GridPosn(1, 0), false, false),
                new Cons<>(
                    new Truck(new GridPosn(2, 0), 0, false, false),
                    new Cons<>(
                        new Car(new GridPosn(3, 0), 3, true, false),
                        new Cons<>(
                            new PlayerCar(new GridPosn(3, 1),true, true),
                            new Cons<>(
                                new Truck(new GridPosn(3, 2), 1, true, false),
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
        6, 3));
  }
  
  
  void testLevelHandleKey(Tester t) {
    // see testOnKeyEvent for more extensive integration tests
    t.checkExpect(level5.handleKey("d"),
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T  T  |\n"
                      + "| p    X\n"
                      + "|      |\n"
                      + "|C   c |\n"
                      + "|   t  |\n"
                      + "+------+", new GridPosn(2, 3))
    );
  }
  
  // An integration test to check the functionality of the draw method on a simple scene.
  // Utilizes draw methods on IGameObject and GridPosn that are tested individually below.
  void testDraw(Tester t) {
    t.checkExpect(level13.draw(),
        new Car(new GridPosn(0, 0), 3, false, false).drawTo(
            new PlayerCar(new GridPosn(1, 0), false, false).drawTo(
                new Truck(new GridPosn(2, 0), 0, false, false).drawTo(
                    new Car(new GridPosn(3, 0), 3, true, false).drawTo(
                        new PlayerCar(new GridPosn(3, 1),true, true).drawTo(
                            new Truck(new GridPosn(3, 2), 1, true, false).drawTo(
                                new Exit(new GridPosn(0, 2)).drawTo(
                                    new Wall(new GridPosn(5, 0)).drawTo(
                                        new Wall(new GridPosn(5, 1)).drawTo(
                                            new Wall(new GridPosn(1, 2)).drawTo(
                                                new GridPosn().drawPositioned(
                                                    new TiledImage(
                                                        new FromFileImage("sprites/grid-cell.png"),
                                                        6, 3).draw(),
                                                    new WorldScene(0, 0)))))))))))));
  }
  
  void testGetWidthPixels(Tester t) {
    t.checkExpect(level12.getWidthPixels(), 64);
    t.checkExpect(level1.getWidthPixels(), 512);
    t.checkExpect(level11.getWidthPixels(), 704);
  }
  
  void testGetHeightPixels(Tester t) {
    t.checkExpect(level12.getHeightPixels(), 64);
    t.checkExpect(level1.getHeightPixels(), 512);
    t.checkExpect(level11.getHeightPixels(), 384);
  }
  
  void testHasWon(Tester t) {
    t.checkExpect(level1.hasWon(), false);
    t.checkExpect(level15.hasWon(), true);
    t.checkExpect(level16.hasWon(), true);
    t.checkExpect(level17.hasWon(), false);
  }
  
  void testHandleClick(Tester t) {
    // Clicking on the player car to select it
    t.checkExpect(level1.handleClick(new GridPosn(2, 3)), level5);
    // Doing nothing by clicking on a wall
    t.checkExpect(level1.handleClick(new GridPosn(0, 0)), level1);
    // Deselecting the player car
    t.checkExpect(level5.handleClick(new GridPosn(0, 0)), level1);
    // Selecting a different car
    t.checkExpect(level5.handleClick(new GridPosn(1, 1)),
        new Level("+------+\n"
                      + "|c    T|\n"
                      + "|T  T  |\n"
                      + "| p    X\n"
                      + "|      |\n"
                      + "|C   c |\n"
                      + "|   t  |\n"
                      + "+------+", new GridPosn(1, 1)));
  }
}

// Examples for IGameObject.java ==================================================================

class ExamplesIGameObject {
  
  // +----------+
  // | Examples |
  // +----------+
  
  // Walls
  Wall wall1 = new Wall(new GridPosn());
  Wall wall2 = new Wall(new GridPosn(2, 4));
  // Exits
  Exit exit1 = new Exit(new GridPosn());
  Exit exit2 = new Exit(new GridPosn(2, 4));
  // Cars
  Car car1 = new Car(new GridPosn(), 0, true, true);
  Car car2 = new Car(new GridPosn(0, 1), 2, false, false);
  Car car3 = new Car(new GridPosn(1, 0), 4, false, false);
  // PlayerCars
  PlayerCar pCar1 = new PlayerCar(new GridPosn(), true, true);
  PlayerCar pCar2 = new PlayerCar(new GridPosn(1, 1), false, false);
  // Trucks
  Truck truck1 = new Truck(new GridPosn(0, 2), 3, true, true);
  Truck truck2 = new Truck(new GridPosn(1, 1), 1, false, false);
  
  // +-------------------+
  // | AGameObject Tests |
  // +-------------------+
  
  void testIntersects(Tester t) {
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
  }
  
  void testGetArea(Tester t) {
    t.checkExpect(wall1.getArea(), new GridArea(new GridPosn(), new GridPosn(1, 1)));
    t.checkExpect(wall2.getArea(), new GridArea(new GridPosn(2, 4), new GridPosn(3, 5)));
    t.checkExpect(exit2.getArea(), new GridArea(new GridPosn(2, 4), new GridPosn(3, 5)));
    t.checkExpect(car1.getArea(), new GridArea(new GridPosn(), new GridPosn(2, 1)));
    t.checkExpect(car2.getArea(), new GridArea(new GridPosn(0, 1), new GridPosn(1, 3)));
    t.checkExpect(pCar2.getArea(), new GridArea(new GridPosn(1, 1), new GridPosn(2, 3)));
    t.checkExpect(truck1.getArea(), new GridArea(new GridPosn(0, 2), new GridPosn(3, 3)));
    t.checkExpect(truck2.getArea(), new GridArea(new GridPosn(1, 1), new GridPosn(2, 4)));
  }
  
  void testXSize(Tester t) {
    t.checkExpect(wall1.xSize(), 1);
    t.checkExpect(exit2.xSize(), 1);
    t.checkExpect(car1.xSize(), 2);
    t.checkExpect(car2.xSize(), 1);
    t.checkExpect(pCar2.xSize(), 1);
    t.checkExpect(truck1.xSize(), 3);
    t.checkExpect(truck2.xSize(), 1);
  }
  
  void testYSize(Tester t) {
    t.checkExpect(wall1.ySize(), 1);
    t.checkExpect(exit2.ySize(), 1);
    t.checkExpect(car1.ySize(), 1);
    t.checkExpect(car2.ySize(), 2);
    t.checkExpect(pCar2.ySize(), 2);
    t.checkExpect(truck1.ySize(), 1);
    t.checkExpect(truck2.ySize(), 3);
  }
  
  void testGetImage(Tester t) {
    t.checkExpect(wall1.getImage(), new FromFileImage("sprites/bush.png"));
    t.checkExpect(exit1.getImage(), new FromFileImage("sprites/exit.png"));
    t.checkExpect(car1.getImage(), new RotateImage(
        new FromFileImage("sprites/car/car-selected.png"), 90.0));
    t.checkExpect(car2.getImage(), new RotateImage(
        new FromFileImage("sprites/car/car2.png"), 180.0));
    t.checkExpect(pCar1.getImage(), new RotateImage(
        new FromFileImage("sprites/car/car-selected.png"), 90.0));
    t.checkExpect(pCar2.getImage(), new RotateImage(
        new FromFileImage("sprites/car/car-player.png"), 180));
    t.checkExpect(truck1.getImage(), new RotateImage(
        new FromFileImage("sprites/truck/truck-selected.png"), 90.0));
    t.checkExpect(truck2.getImage(), new RotateImage(
        new FromFileImage("sprites/truck/truck1.png"), 180.0));
  }
  
  void testDrawTo(Tester t) {
    t.checkExpect(wall1.drawTo(new WorldScene(0, 0)),
        new GridPosn().drawPositioned(wall1.getImage(), new WorldScene(0, 0)));
    t.checkExpect(wall1.drawTo(new WorldScene(512, 512)),
        new GridPosn().drawPositioned(wall1.getImage(), new WorldScene(512, 512)));
    t.checkExpect(exit2.drawTo(new WorldScene(0, 0)),
        new GridPosn(2, 4).drawPositioned(exit2.getImage(), new WorldScene(0, 0)));
    t.checkExpect(car2.drawTo(new WorldScene(0, 0)),
        new GridPosn(0, 1).drawPositioned(car2.getImage(), new WorldScene(0, 0)));
    t.checkExpect(pCar1.drawTo(new WorldScene(0, 0)),
        new GridPosn(1, 1).drawPositioned(pCar2.getImage(), new WorldScene(0, 0)));
    t.checkExpect(truck1.drawTo(new WorldScene(0, 0)),
        new GridPosn(0, 2).drawPositioned(pCar2.getImage(), new WorldScene(0, 0)));
  }
  
  // +----------------+
  // | AVehicle Tests |
  // +----------------+
  
  void testGetWidth(Tester t) {
    t.checkExpect(car1.getWidth(), 1);
    t.checkExpect(car2.getWidth(), 1);
    t.checkExpect(pCar1.getWidth(), 1);
    t.checkExpect(truck1.getWidth(), 1);
    t.checkExpect(truck2.getWidth(), 1);
  }
  
  void testGetLength(Tester t) {
    t.checkExpect(car1.getLength(), 2);
    t.checkExpect(car2.getLength(), 2);
    t.checkExpect(pCar1.getLength(), 2);
    t.checkExpect(truck1.getLength(), 3);
    t.checkExpect(truck2.getLength(), 3);
  }
  
  void testInWinningState(Tester t) {
    t.checkExpect(car1.inWinningState(new Mt<>()), true);
    t.checkExpect(car1.inWinningState(new Cons<>(exit1, new Mt<>())), true);
    t.checkExpect(car1.inWinningState(new Cons<>(exit2, new Mt<>())), true);
    t.checkExpect(pCar1.inWinningState(new Mt<>()), false);
    t.checkExpect(pCar1.inWinningState(new Cons<>(exit1, new Mt<>())), true);
    t.checkExpect(pCar1.inWinningState(new Cons<>(exit2, new Mt<>())), false);
    t.checkExpect(truck1.inWinningState(new Mt<>()), true);
    t.checkExpect(truck2.inWinningState(new Cons<>(exit1, new Mt<>())), true);
  }
  
  void testDrawFromFile(Tester t) {
    t.checkExpect(car1.drawFromFile("sprites/car/car-selected.png"), new RotateImage(
        new FromFileImage("sprites/car/car-selected.png"), 90.0));
    t.checkExpect(car1.drawFromFile("sprites/car/car1.png"), new RotateImage(
        new FromFileImage("sprites/car/car1.png"), 90.0));
    t.checkExpect(car1.drawFromFile("sprites/truck/truck1.png"), new RotateImage(
        new FromFileImage("sprites/truck/truck1.png"), 90.0));
    t.checkExpect(car2.drawFromFile("sprites/car/car2.png"), new RotateImage(
        new FromFileImage("sprites/car/car2.png"), 180.0));
    t.checkExpect(pCar1.drawFromFile("sprites/car/car-selected.png"), new RotateImage(
        new FromFileImage("sprites/car/car-selected.png"), 90.0));
    t.checkExpect(pCar2.drawFromFile("sprites/car/car-player.png"), new RotateImage(
        new FromFileImage("sprites/car/car-player.png"), 180));
    t.checkExpect(truck1.drawFromFile("sprites/truck/truck-selected.png"), new RotateImage(
        new FromFileImage("sprites/truck/truck-selected.png"), 90.0));
    t.checkExpect(truck2.drawFromFile("sprites/truck/truck1.png"), new RotateImage(
        new FromFileImage("sprites/truck/truck1.png"), 180.0));
  }
  
  void testRegisterClick(Tester t) {
    // clicking an already active car yields an active car
    t.checkExpect(car1.registerClick(new GridPosn(1, 0)),
        new Car(new GridPosn(), 0, true, true));
    // clicking outside an already active car yields an inactive car
    t.checkExpect(car1.registerClick(new GridPosn(1, 1)),
        new Car(new GridPosn(), 0, true, false));
    // clicking outside an inactive car yields an inactive car
    t.checkExpect(car2.registerClick(new GridPosn(1, 1)),
        new Car(new GridPosn(0, 1), 2, false, false));
    // clicking an inactive car yields an active car
    t.checkExpect(car2.registerClick(new GridPosn(0, 1)),
        new Car(new GridPosn(0, 1), 2, false, true));
  }
  
  void testRegisterKey(Tester t) {
    Car moveCar = new Car(new GridPosn(), 1, true, true);
    Truck moveTruck = new Truck(new GridPosn(2, 0), 3, false, true);
    Truck inactiveCar = new Truck(new GridPosn(2, 0), 3, false, false);
    Wall w = new Wall(new GridPosn(2, 0));
    
    // Do nothing if the car is inactive
    t.checkExpect(inactiveCar.registerKey("a",
            new Cons<>(w, new Mt<>()),
            new Cons<>(inactiveCar, new Cons<>(moveTruck, new Mt<>()))),
        inactiveCar);
    
    // Do nothing if the desired move isn't in the direction of movement
    t.checkExpect(moveCar.registerKey("s", new Mt<>(), new Mt<>()), moveCar);
    t.checkExpect(moveTruck.registerKey("d", new Mt<>(), new Mt<>()), moveTruck);
    
    // Do nothing if the move would cause a collision with another vehicle
    t.checkExpect(moveCar.registerKey("d", new Mt<>(),
            new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>()))),
        moveCar);
    
    // Do nothing if the move would cause a collision with a wall
    t.checkExpect(moveCar.registerKey("d", new Cons<>(w, new Mt<>()), new Mt<>()), moveCar);
    
    // Otherwise, move the vehicle
    t.checkExpect(moveCar.registerKey("a",
            new Cons<>(w, new Mt<>()),
            new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>()))),
        new Car(new GridPosn(-1, 0), 1, true, true));
    t.checkExpect(moveCar.registerKey("d",
            new Mt<>(),
            new Cons<>(moveCar, new Mt<>())),
        new Car(new GridPosn(1, 0), 1, true, true));
    t.checkExpect(moveTruck.registerKey("w",
            new Mt<>(),
            new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>()))),
        new Truck(new GridPosn(2, -1), 3, false, true));
    t.checkExpect(moveTruck.registerKey("s",
            new Mt<>(),
            new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>()))),
        new Truck(new GridPosn(2, 1), 3, false, true));
    
    // Arrow keys should work as well
    t.checkExpect(moveCar.registerKey("left",
            new Cons<>(w, new Mt<>()),
            new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>()))),
        new Car(new GridPosn(-1, 0), 1, true, true));
  }
  
  void testMoveTo(Tester t) {
   t.checkExpect(car1.moveTo(new GridPosn(1, 3)),
       new Car(new GridPosn(1, 3), 0, true, true));
   t.checkExpect(pCar1.moveTo(new GridPosn(1, 3)),
       new PlayerCar(new GridPosn(1, 3), true, true));
   t.checkExpect(truck1.moveTo(new GridPosn(1, 3)),
       new Truck(new GridPosn(1, 3), 3, true, true));
  }
  
  void testMove(Tester t) {
    Car moveCar = new Car(new GridPosn(), 1, true, false);
    Truck moveTruck = new Truck(new GridPosn(2, 0), 3, false, false);
    Wall w = new Wall(new GridPosn(2, 0));
    
    // Do nothing if the desired move isn't in the direction of movement
    t.checkExpect(moveCar.move(0, 1, new Mt<>(), new Mt<>()), moveCar);
    t.checkExpect(moveTruck.move(1, 0, new Mt<>(), new Mt<>()), moveTruck);
    
    // Do nothing if the move would cause a collision with another vehicle
    t.checkExpect(moveCar.move(1, 0, new Mt<>(),
            new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>()))),
        moveCar);
    
    // Do nothing if the move would cause a collision with a wall
    t.checkExpect(moveCar.move(1, 0, new Cons<>(w, new Mt<>()), new Mt<>()), moveCar);
    
    // Otherwise, move the vehicle
    t.checkExpect(moveCar.move(-1, 0,
        new Cons<>(w, new Mt<>()),
        new Cons<>(moveCar, new Cons<>(moveTruck, new Mt<>()))),
        new Car(new GridPosn(-1, 0), 1, true, false));
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
  Car car1 = new Car(new GridPosn(), 0, true, true);
  Car car2 = new Car(new GridPosn(0, 1), 2, false, false);
  Car car3 = new Car(new GridPosn(1, 0), 4, false, false);
  // PlayerCars
  PlayerCar pCar1 = new PlayerCar(new GridPosn(), true, true);
  PlayerCar pCar2 = new PlayerCar(new GridPosn(1, 1), false, false);
  // Trucks
  Truck truck1 = new Truck(new GridPosn(0, 2), 3, true, true);
  Truck truck2 = new Truck(new GridPosn(1, 1), 1, false, false);
  Truck truck3 = new Truck(new GridPosn(4, 1), 1, true, false);
  // Lists of IGameObjects
  IList<IVehicle> vehicles = new Cons<>(car1, new Cons<>(pCar2, new Cons<>(truck1, new Mt<>())));
  IList<Wall> walls = new Cons<>(wall1, new Cons<>(wall2, new Mt<>()));
  IList<Exit> exits = new Cons<>(exit1, new Cons<>(exit2, new Mt<>()));
  // GridAreas
  GridArea r = new GridArea(new GridPosn(0, 1), new GridPosn(6, 6));
  
  // ... Function examples
  IntersectsPred<Wall> wallIntersectsWall1 = new IntersectsPred<>(wall1);
  IntersectsPred<Exit> exitIntersectsWall1 = new IntersectsPred<>(wall1);
  IntersectsPred<IVehicle> vehicleIntersectsExit1 = new IntersectsPred<>(exit1);
  IntersectsPred<IVehicle> vehicleIntersectsPCar2 = new IntersectsPred<>(pCar2);
  
  IntersectsAnyPred<Wall, Wall> wallIntersectsAnyWall1 = new IntersectsAnyPred<>(new Mt<>());
  IntersectsAnyPred<Wall, Wall> wallIntersectsAnyWall2 = new IntersectsAnyPred<>(walls);
  IntersectsAnyPred<Wall, Exit> wallIntersectsAnyExit = new IntersectsAnyPred<>(exits);
  IntersectsAnyPred<Exit, Wall> exitIntersectsAnyWall = new IntersectsAnyPred<>(walls);
  IntersectsAnyPred<IVehicle, Wall> vehicleIntersectsAnyWall = new IntersectsAnyPred<>(walls);
  IntersectsAnyPred<IVehicle, IVehicle> vehicleIntersectsAnyVehicle =
      new IntersectsAnyPred<>(vehicles);
  
  IntersectsAnyOtherPred<Wall> wallIntersectsAnyOther = new IntersectsAnyOtherPred<>();
  IntersectsAnyOtherPred<Exit> exitIntersectsAnyOther = new IntersectsAnyOtherPred<>();
  IntersectsAnyOtherPred<IVehicle> vehicleIntersectsAnyOther = new IntersectsAnyOtherPred<>();
  
  DrawToScene<Wall> drawWallToScene = new DrawToScene<>();
  DrawToScene<Exit> drawExitToScene = new DrawToScene<>();
  DrawToScene<IVehicle> drawVehicleToScene = new DrawToScene<>();
  
  InAreaPred<Wall> wallInAreaPred = new InAreaPred<>(r);
  InAreaPred<Exit> exitInAreaPred = new InAreaPred<>(r);
  InAreaPred<IVehicle> vehicleInAreaPred = new InAreaPred<>(r);
  
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
    t.checkExpect(vehicleIntersectsExit1.apply(car1), true);
    t.checkExpect(vehicleIntersectsExit1.apply(car2), false);
    t.checkExpect(vehicleIntersectsPCar2.apply(car3), true);
    t.checkExpect(vehicleIntersectsPCar2.apply(truck1), true);
  }
  
  void testIntersectsAnyPred(Tester t) {
    t.checkExpect(wallIntersectsAnyWall1.apply(wall1), false);
    t.checkExpect(wallIntersectsAnyWall2.apply(wall1), true);
    t.checkExpect(wallIntersectsAnyWall2.apply(new Wall(new GridPosn(1, 1))), false);
    
    t.checkExpect(wallIntersectsAnyExit.apply(wall2), true);
    t.checkExpect(wallIntersectsAnyExit.apply(new Wall(new GridPosn(1, 1))), false);
    t.checkExpect(exitIntersectsAnyWall.apply(exit2), true);
    
    t.checkExpect(vehicleIntersectsAnyWall.apply(car1), true);
    t.checkExpect(vehicleIntersectsAnyVehicle.apply(car2), true);
    t.checkExpect(vehicleIntersectsAnyWall.apply(truck1), false);
    t.checkExpect(vehicleIntersectsAnyVehicle.apply(car1), true);
    t.checkExpect(vehicleIntersectsAnyVehicle.apply(truck3), false);
  }
  
  void testIntersectsAnyOtherPred(Tester t) {
    t.checkExpect(wallIntersectsAnyOther.apply(wall1, walls), true);
    t.checkExpect(wallIntersectsAnyOther.apply(wall2, walls), true);
    t.checkExpect(wallIntersectsAnyOther.apply(new Wall(new GridPosn(1, 1)), walls), false);
    t.checkExpect(exitIntersectsAnyOther.apply(new Exit(new GridPosn()), exits), true);
    t.checkExpect(exitIntersectsAnyOther.apply(new Exit(new GridPosn(1, 1)), exits), false);
    t.checkExpect(vehicleIntersectsAnyOther.apply(car2, vehicles), true);
    t.checkExpect(vehicleIntersectsAnyOther.apply(truck3, vehicles), false);
  }
  
  void testDrawToScene(Tester t) {
    // DrawTo has already been extensively tested, so we're just checking against that.
    t.checkExpect(drawWallToScene.apply(wall2, new WorldScene(0, 0)),
        wall2.drawTo(new WorldScene(0, 0)));
    t.checkExpect(drawExitToScene.apply(exit1, new WorldScene(0, 0)),
        exit1.drawTo(new WorldScene(0, 0)));
    t.checkExpect(drawVehicleToScene.apply(truck3, new WorldScene(512, 512)),
        truck3.drawTo(new WorldScene(512, 512)));
  }
  
  void testInAreaPred(Tester t) {
    t.checkExpect(wallInAreaPred.apply(wall2), true);
    t.checkExpect(wallInAreaPred.apply(new Wall(new GridPosn(-1, 0))), false);
    t.checkExpect(wallInAreaPred.apply(new Wall(new GridPosn(0, 6))), false);
    t.checkExpect(exitInAreaPred.apply(exit2), true);
    t.checkExpect(exitInAreaPred.apply(new Exit(new GridPosn(0, -1))), false);
    t.checkExpect(exitInAreaPred.apply(new Exit(new GridPosn(6, 0))), false);
    t.checkExpect(vehicleInAreaPred.apply(car1), false);
    t.checkExpect(vehicleInAreaPred.apply(truck2), true);
    t.checkExpect(vehicleInAreaPred.apply(truck3), false);
  }
  
  void testInWinningStatePred(Tester t) {
    t.checkExpect(inWinningStatePred1.apply(car1), true);
    t.checkExpect(inWinningStatePred1.apply(pCar1), false);
    t.checkExpect(inWinningStatePred2.apply(truck1), true);
    t.checkExpect(inWinningStatePred2.apply(pCar1), true);
    t.checkExpect(inWinningStatePred2.apply(pCar2), false);
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
    WorldScene scene = new WorldScene(512, 512);
    
    t.checkExpect(
        p1.drawPositioned(sqr, scene),
        scene.placeImageXY(sqr.movePinhole(-32, -32), 0, 0));
    t.checkExpect(
        p2.drawPositioned(sqr, scene),
        scene.placeImageXY(sqr.movePinhole(-32, -32), 64, 192));
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

// A test function that adds the two provided Integers.
class Add implements BiFunction<Integer, Integer, Integer> {
  // Adds the two provided Integers and returns an Integer representing the result
  @Override
  public Integer apply(Integer i1, Integer i2) {
    return i1 + i2;
  }
}

// A test function that converts a provided Integer to a String
class ToString implements Function<Integer, String> {
  // Converts that Integer to a String
  @Override
  public String apply(Integer that) {
    return that.toString();
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
  
  void testFoldr(Tester t) {
    t.checkExpect(mt.foldr(new Add(), 0), 0);
    t.checkExpect(mt.foldr(new Add(), 2), 2);
    t.checkExpect(ints1.foldr(new Add(), 0), 10);
  }
  
  void testMap(Tester t) {
    t.checkExpect(mt.map(new ToString()), new Mt<String>());
    t.checkExpect(ints1.map(new ToString()),
        new Cons<>("1",
            new Cons<>("7",
                new Cons<>("2",
                    new Mt<>()))));
  }
  
  void testRemove(Tester t) {
    t.checkExpect(mt.remove(1), mt);
    t.checkExpect(ints1.remove(1), new Cons<>(7, new Cons<>(2, new Mt<>())));
    t.checkExpect(ints2.remove(1), new Cons<>(1, new Cons<>(1, new Mt<>())));
    t.checkExpect(ints1.remove(3), ints1);
    
    Car c1 = new Car(new GridPosn(1, 1), 0, false, false);
    Car c2 = new Car(new GridPosn(1, 1), 0, false, false);
    t.checkExpect(new Cons<>(c1, new Mt<>()).remove(c1), new Mt<>());
    t.checkExpect(new Cons<>(c1, new Mt<>()).remove(c2), new Cons<>(c1, new Mt<>()));
  }
}

// Examples for LoadUtils.java ====================================================================

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
        new Cons<>(new Car(new GridPosn(0, 0), 1, true, false), new Mt<>()));
    t.checkExpect(p3.loadVehicles(),
        new Cons<>(new Car(new GridPosn(0, 0), 1, false, false), new Mt<>()));
    t.checkExpect(p4.loadVehicles(),
        new Cons<>(new PlayerCar(new GridPosn(0, 0), false, false), new Mt<>()));
    t.checkExpect(p5.loadVehicles(),
        new Cons<>(new Truck(new GridPosn(0, 0), 1, true, false), new Mt<>()));
    t.checkExpect(p6.loadVehicles(),
        new Cons<>(
            new Car(new GridPosn(0, 0), 3, false, false),
            new Cons<>(new PlayerCar(new GridPosn(1, 0), false, true),
                new Cons<>(new Truck(new GridPosn(2, 0), 0, false, false),
                    new Cons<>(new Car(new GridPosn(3, 0), 3, true, false),
                        new Cons<>(new PlayerCar(new GridPosn(3, 1),true, false),
                            new Cons<>(new Truck(new GridPosn(3, 2), 1, true, false),
                                new Mt<>())))))));
  }
  
  void testLoadRemainingVehicles(Tester t) {
    t.checkExpect(p1.loadRemainingVehicles(" +|-X", 0, 0, new Random(6), new GridPosn(0, 0)),
        new Mt<>());
    t.checkExpect(p1.loadRemainingVehicles("c", 0, 0, new Random(6), new GridPosn(-1, -1)),
        new Cons<>(new Car(new GridPosn(0, 0), 1, true, false), new Mt<>()));
    t.checkExpect(p1.loadRemainingVehicles("c", 4, 2, new Random(6), new GridPosn(-1, -1)),
        new Cons<>(new Car(new GridPosn(4, 2), 1, true, false), new Mt<>()));
    t.checkExpect(p1.loadRemainingVehicles("c", 4, 2, new Random(6), new GridPosn(4, 2)),
        new Cons<>(new Car(new GridPosn(4, 2), 1, true, true), new Mt<>()));
    t.checkExpect(p1.loadRemainingVehicles("C", 0, 0, new Random(6), new GridPosn(-1, -1)),
        new Cons<>(new Car(new GridPosn(0, 0), 1, false, false), new Mt<>()));
    t.checkExpect(p1.loadRemainingVehicles("P", 0, 0, new Random(6), new GridPosn(-1, -1)),
        new Cons<>(new PlayerCar(new GridPosn(0, 0), false, false), new Mt<>()));
    t.checkExpect(p1.loadRemainingVehicles("t", 0, 0, new Random(6), new GridPosn(-1, -1)),
        new Cons<>(new Truck(new GridPosn(0, 0), 1, true, false), new Mt<>()));
    t.checkExpect(p2.loadRemainingVehicles("CPTc |\n"
                                      + "   p -\n"
                                      + "X+ t  ", 0, 0, new Random(6), new GridPosn(1, 0)),
        new Cons<>(
            new Car(new GridPosn(0, 0), 3, false, false),
            new Cons<>(new PlayerCar(new GridPosn(1, 0), false, true),
                new Cons<>(new Truck(new GridPosn(2, 0), 0, false, false),
                    new Cons<>(new Car(new GridPosn(3, 0), 3, true, false),
                        new Cons<>(new PlayerCar(new GridPosn(3, 1),true, false),
                            new Cons<>(new Truck(new GridPosn(3, 2), 1, true, false),
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
}

// Examples for TiledImage.java ===================================================================

class ExamplesTiledImage {
  
  // +----------+
  // | Examples |
  // +----------+
  
  WorldImage im1 = new RectangleImage(16, 32, OutlineMode.SOLID, Color.BLACK);
  WorldImage im2 = new FromFileImage("sprites/grid-cell.png");
  
  TiledImage ti1 = new TiledImage(im1, 1, 1);
  TiledImage ti2 = new TiledImage(im1, 4, 1);
  TiledImage ti3 = new TiledImage(im1, 1, 5);
  TiledImage ti4 = new TiledImage(im1, 4, 5);
  TiledImage ti5 = new TiledImage(im2, 4, 5);
  
  
  // +-------+
  // | Tests |
  // +-------+
  void testConstructorEx(Tester t) {
    t.checkConstructorException(
        new IllegalArgumentException("tilesX and tilesY must both be positive integers"),
        "TiledImage",
        im1, 0, 1
    );
    t.checkConstructorException(
        new IllegalArgumentException("tilesX and tilesY must both be positive integers"),
        "TiledImage",
        im1, 1, 0
    );
    t.checkConstructorException(
        new IllegalArgumentException("tilesX and tilesY must both be positive integers"),
        "TiledImage",
        im1, -1, -1
    );
  }
  
  void testDrawRow(Tester t) {
    t.checkExpect(ti1.drawRow(1), im1);
    t.checkExpect(ti1.drawRow(3),
        new BesideImage(im1,
            new BesideImage(im1, im1)));
    t.checkExpect(ti4.drawRow(3),
        new BesideImage(im1,
            new BesideImage(im1, im1)));
    t.checkExpect(ti5.drawRow(3),
        new BesideImage(im2,
            new BesideImage(im2, im2)));
  }
  
  void testDrawRows(Tester t) {
    WorldImage row = new BesideImage(im1,
        new BesideImage(im1,
            new BesideImage(im1, im1)));
    
    t.checkExpect(ti1.drawRows(1), im1);
    t.checkExpect(ti1.drawRows(3),
        new AboveImage(im1,
            new AboveImage(im1, im1)));
    t.checkExpect(ti2.drawRows(2),
        new AboveImage(row, row));
    t.checkExpect(ti4.drawRows(3),
        new AboveImage(row,
            new AboveImage(row, row)));
  }
  
  void testDraw(Tester t) {
    WorldImage row = new BesideImage(im1,
        new BesideImage(im1,
            new BesideImage(im1, im1)));
    t.checkExpect(ti1.draw(), im1);
    t.checkExpect(ti2.draw(), row);
    t.checkExpect(ti3.draw(),
        new AboveImage(im1,
            new AboveImage(im1,
                new AboveImage(im1,
                    new AboveImage(im1, im1)))));
    t.checkExpect(ti4.draw(),
        new AboveImage(row,
            new AboveImage(row,
                new AboveImage(row,
                    new AboveImage(row, row)))));
  }
}