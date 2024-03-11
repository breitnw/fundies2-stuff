# PROGRAM DESIGN

> Design two example levels whose playable area is not a straightforward rectangle. In your  
> Design.txt file, explain what new implementation support, if any, you needed to build to make
> this work.


🚧 _Refer to section "Level loading helpers" for implementation details_

We did not need to make many major changes to make this work. As mentioned in `Changes.txt`, we 
already had support for placing obstacles anywhere in the game board. As a direct consequence for
this feature, one can easily use these walls to create an irregular border for the board, 
fulfilling functionality for non-rectangular levels. 

The only significant change we made was more of a convenience: rather than throwing an 
`IllegalArgumentException` when the rows of the level `String` are uneven lengths, we chose to 
represent the undefined cells as empty cells. Methods to retrieve the width of the game board, 
then, simply find the length of the longest row. 

---

## GAME STATE
Two classes manage the overall game state: Game and Level. As the names suggest, Game manages the
state of the game as a whole, while Level manages an individual level.


### Game
Game extends javalib.funworld.World, and contains:
- An integer TILE_SIZE that represents the size of each tile, in pixels. As of now, this number is
  always 64, as that is the size of a tile in each sprite.
- a Level that represents the level this game renders

It has the following methods:
- bigBang(), which overloads the default bigBang method by determining the width and the height of
  the game based on the level
- `makeScene()`, which draws the level to an empty scene
- `onMouseClicked(Posn pos)`, which manages mouse clicks by converting their positions from screen to
  grid coordinates and invoking `handleClick` on level
- `onKeyEvent(String k)`, which handles a key event with code `k` by propagating it to the key 
  event handler on `Level`
- `shouldWorldEnd()`, which determines whether the `Level`'s win condition is satisfied
- `lastScene(String msg)`, which draws the current game with an overlay of "You win!" text

### Level
Level represents the logic for a level and its layout, including all of its vehicles, walls, exits,
width, and height. Although they all implement the IGameObject interface, we chose to represent
vehicles, walls, and exits as separate lists, as subclasses (i.e., vehicle) have unique
functionality that we would like to be able to easily invoke. For example, we'd like to iterate over
all the cars when registering clicks, without needing to redundantly iterate over walls or exits.

Another quirk arising from this design is that it allows us to create levels with multiple player
cars and exits, allowing for challenges that involve multiple exits we need to reach.


#### Loading levels
We have three constructors for levels:
- The first takes in a list of vehicles, walls, exits, width, and height. It throws a variety of
  exceptions to validate its data:
  - If the width or height are less than 1
  - If any vehicle overlaps another vehicle
  - If any vehicle overlaps a wall
  - If any vehicle extends beyond the limits of the grid
  - The latter three tests are _not_ performed with walls or exits, since we will realistically only
    ever be loading a level from a String, in which case it is impossible to make these mistakes.
- The second takes in a String to simplify Level construction. For more information on how to format
  the level strings, see the documentation for this constructor.
- The third takes in a String and a GridPosn. The Level is constructed in the same manner as it
  would be in the second constructor, just with the car at the specified GridPosn (if one exists)
  selected.


##### Level loading helpers

To aid in loading levels from Strings, we have a `Parser` class instantiated with a string 
representation of the level and a `GridPosn` representing the selected tile on the level.

`loadVehicles` loads a list of `AVehicles` within the parser's layout String. It calls 
`loadRemainingVehicles` with a set of default parameters.

`loadRemainingVehicles` assumes that the first character in the String is at the position 
defined by two accumulators, `curX` and `curY`. The vehicle at the specified `GridPosn`, if one 
exists, is selected. With each subsequent iteration, `curX` and `curY` are updated according to 
whether the next character is a newline.

The method also utilizes a seeded instance of the `Random` class to generate colors for cars. This
instance always uses the same seed, since it isn't important to generate different colors each time
the program is run.

loadWalls and loadExits function in a similar way to loadVehicles, but accept any of the valid
wall characters or exit character, respectively, and produce a list of walls or exits instead

loadWidth retrieves the width of the level from the String by finding the index of the first newline
character. It also throws an exception if hasSameLineLengths, which validates that all of the lines
in the String are the same length, returns false.

loadHeight retrieves the height of the level by counting the number of newline characters in the
provided String.


---

## GAME OBJECTS

### IGameObject
To represent any object in our game with physical dimensions, we created the IGameObject interface.
Every IGameObject has three publicly accessible (defined on the interface) methods:
- intersects, which determines if this IGameObject overlaps another IGameObject utilizing the
  methods on each IGameObject's generated GridArea
- getRect, which creates a GridArea representing the two-dimensional span of this IGameObject,
  from its top-left to bottom-right corner.
- drawTo, which given a scene and a tileSize, draws this IGameObject to the scene at its respective
  GridPosn.


### AGameObject
In order to abstract some of this functionality over the classes that implement IGameObject, we also
added the abstract class AGameObject. This class contains a GridPosn representing the upper right
corner of the game object. It also fills in some of the methods from the interface, such as
intersects, getRect (based on two new methods, xSize and ySize) and drawTo (based on a new method,
getImage).

xSize and ySize have default implementations (namely, returning 1, since this is the most common
width/height value), but they are overridden. getImage is left as an abstract method.


### Wall and Exit
The Wall and Exit classes each use all the default implementations except for getImage(), since it
has one. In each case, they just load an image from the corresponding file.


### IVehicle
The IVehicle interface represents public functionality across all vehicles, and extends the
IGameObject interface. The only two methods it contains are:
- inWinningState, which determines whether this IVehicle is in a winning state. We decided to make
  this method accessible to all vehicles instead of just PlayerCars so that we can just run andmap
  over the list of cars, thus allowing for multiple PlayerCars as well.
- registerClick, which registers a click at the provided GridPosn, acting on this Car depending on
  where the click occurred.
- `registerKey`, which registers a key event, returning a new `IVehicle` representing the changed 
  vehicle after the key. If this `IVehicle` is selected, moves it left, right, up, or down 
  depending on which movements are available and which key was pressed. Collisions are tested 
  with the provided lists of walls and vehicles before movement. 


### AVehicle
The AVehicle interface abstracts some of the functionality present across all IVehicle implementors.
It contains three fields:
- color, which is an integer (validated to be between 0 and 4) representing the color of the
  vehicle. It maps to the number present in the corresponding sprite's filename.
- isHorizontal, which represents whether this vehicle is horizontal or vertical
- active, which represents whether this vehicle is selected.

This allows us to define xSize and ySize depending on two new abstract methods, getWidth and
getHeight. These methods do not vary based on the orientation of the vehicle, but xSize and ySize
transform the values, according to isHorizontal, such that they do.

We also abstract inWinningState with a default implementation that always returns true, and define a
method drawFromFile that, given a filename, retrieves a sprite and rotates it accordingly.


### Car and Truck
The Car and Truck classes both extend AVehicle, overriding the getWidth and getLength methods
according to their respective width and length. Both also override the getImage method by creating
an image based on a path string and modifying it based on the color, or choosing the selected sprite
if necessary. Finally, they implement registerClick by returning a new object with its active field
set based on the click location.

### PlayerCar
Finally, PlayerCar extends Car to represent the car that needs to reach an exit to win the game. It
overrides getImage to always return the player car image (unless selected), registerClick to return
a PlayerCar instead of a Car, and inWinningState to check that one of the exits is intersecting with
this.


---

## HELPER TYPES

### GridPosn
A GridPosn represents a coordinate location on the game board, in terms of x-coordinate and
y-coordinate of the grid-like cells on the game's board. Also referred to as the "grid position".

It has the following methods:
- samePosn(GridPosn other), which checks if this grid position refers to the same grid position
  referred to, by the other posn.
- offset(int dx, int dy), which changes the grid position coordinates. It changes the grid
  position's x-coordinate by dx, and the grid position's y-coordinate by dy.
- drawPositioned(WorldImage im, WorldScene scene), which draws the given image onto the provided
  scene, at this particular grid position.
- compareX(GridPosn other), which is used to compute the relative x-coordinate of this GridPosn,
  with respect to the other GridPosn.
- compareY(GridPosn other), which is used to compute the relative y-coordinate of this GridPosn,
  with respect to the other GridPosn.


### GridArea

A `GridArea` is an object that stores the location of an object by storing the `GridPosn`s of its
extreme points. This allows for predicates to determine intersection or containing between two 
`GridArea`s.

Both the horizontal and vertical intervals of a `GridArea` are semi-open; i.e., the top and left 
edges are inclusive, and the bottom and right edges are exclusive. This clarifies that  two 
side-by-side `IGameObject`s are not intersecting, while allowing for an `IGameObject` to contain 
its own `GridPosn`. 

It has the following methods:

- `intersects(GridArea that)`, which investigates both the `GridArea`s have any grid positions 
  that may be common and hence, in representation, might "intersect".
- `containsRect(GridArea that)`, which considers both the `GridRects` and indicates whether this
  `GridArea` have all the grid positions of the given `GridArea`, or in a sense, whether this 
  `GridArea` completely "contain" (is a superset of) the given `GridArea`.
- `containsPosn(GridPosn that)`, which checks if a `GridPosn` is one of the grid positions 
  represented by the `GridArea`.


### IList

An `IList` is a generic list of a given type. It is either an empty list (one that doesn't contain
anything), or it is a structure that contains one of those given elements and a list of more of
them. It is implemented to store a list of objects that are of some type (have some commonality).

We decided to keep `IList` rather than refactoring to `ArrayList` because we found that direct index 
access was not a large enough benefit to justify the change, and that many of our recursive 
list abstractions were easier and cleaner to use than loops. 

It has the following methods:

- `andmap(Function<T, Boolean> func)`, which goes over the entire list and checks whether every
  element in the list confers to a certain question/predicate/condition and if yes, it returns true.
  If not, it returns false.
- `ormap(Function<T, Boolean> func)`, which goes over the entire list and checks whether any element
  in the list confers to a certain question/predicate/condition and if yes, it returns true. If none
  of them meet this given criteria, it returns false. Else, it returns true.
- `restOrmap(BiFunction<T, IList<T>, Boolean> func)`, which is a method similar to ormap, but it also
  includes the rest of the list passed as a parameter in order to avoid making symmetrical
  comparisons twice, thereby reducing the number of comparisons by two.
- `foldr(BiFunction<T, U, U> func, U base)`, which goes over the list and accumulates all terms,
  combining them with some given operation, to return back the one combined term that constitutes
  all the members of list, combined in some fashion.
- `map(Function<T, U> func)`, which goes over the entire list and implements some operation on each
  member of the list, essentially changing the list in some manner (based on the operation). This
  also introduces the possibility that the list of results may not be the same as the list of
  inputs.

### TiledImage

A `TiledImage` represents a `WorldImage` (an in-built construct of the image library), that is drawn
or rendered on a specific tile (located at a specific position in the image of the game-board).

It has the following methods:

- `drawRow(int width)`, which renders the given image for the given length of the row.
- `drawRows(int height)`, which renders the given image for the given length of the column.
- `draw()`, which draws this `TiledImage` and renders it into an image.

### Functions

These functions refer to curated function objects used alongside the list abstraction methods.

The functions are as follows:

- `IntersectsPred<T extends IGameObject>`, which is a function object that constructs the condition
  used in list abstractions, such as `andmap` and `ormap`, given that this specific function object
  converts a generic type to a `boolean`. The condition here determines if this `IGameObject`  
  intersects with any `IGameObject`.
- `IntersectsAnyPred<T extends IGameObject>`, which is a function object that constructs the 
  condition used in list abstractions, such as andmap and ormap, given that this specific 
  function object converts a generic type to a `boolean`. The condition here determines if this 
  `IGameObject` intersects with any other `IGameObject`s given in the `String` during 
  Constructor call.
- `IntersectsAnyOtherPred<IList<U> otherObjects>`, which is a function object that constructs the
  condition used in list abstractions, such as `andmap`, `ormap` or `restOrmap`, given that this
  specific function object converts a generic type to a boolean. The condition here determines 
  if any of the vehicles or walls provided on construction overlap with that.
- `DrawToScene<T extends IGameObject>`, which is a function object that draws the provided 
  `IGameObject` to the provided WorldImage according to the IGameObject's position and draw method.
- `InRectPred<T extends IGameObject>`, which is a function object predicate that determines whether
  the provided `IGameObject`'s `GridArea` is fully contained in the `GridArea` passed during 
  construction.
- `InWinningStatePred`, which is a function object predicate that returns true if the provided
  `IVehicle` is in a winning state; i.e., if it, being a `PlayerCar` implies it is overlapping 
  an `Exit`.
- `OnClick`, which is a function object that maps a car before a click event to a car after a click
  event, selecting it if it contains the click position and deselecting it if it does not.