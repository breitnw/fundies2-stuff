import javalib.funworld.WorldScene;
import java.util.function.BiFunction;
import java.util.function.Function;

// A predicate that determines whether the IGameObject applied to overlaps with the IGameObject
// provided on construction.
class IntersectsPred<T extends IGameObject> implements Function<T, Boolean> {
  IGameObject with;
  
  IntersectsPred(IGameObject with) {
    this.with = with;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.with ...       -- IGameObject
  METHODS:
  ... apply(T that) ...   -- Boolean
   */
  
  // Determines if the IGameObject provided on construction overlaps with that.
  @Override
  public Boolean apply(T that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                       -- T
    METHODS ON PARAMETERS:
    ... that.overlaps(this.with) ...   -- boolean
     */
    return that.intersects(this.with);
  }
}

// A predicate that determines whether the IGameObject applied to overlaps with any of the
// IGameObjects provided during construction.
class IntersectsAnyPred<T extends IGameObject, U extends IGameObject> implements
    Function<T, Boolean> {
  IList<U> otherObjects;
  
  IntersectsAnyPred(IList<U> otherObjects) {
    this.otherObjects = otherObjects;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.otherObjects ...                                    -- IList<U>
  METHODS:
  ... apply(T that) ...                                        -- Boolean
  METHODS ON FIELDS:
  ... this.otherObjects.ormap(Function<T, Boolean> func) ...   -- boolean
   */
  
  // Determines if any of the vehicles or walls provided on construction overlap with that.
  @Override
  public Boolean apply(T that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...   -- T
     */
    return this.otherObjects.ormap(new IntersectsPred<>(that));
  }
}

// A predicate that determines whether the IGameObject applied to overlaps with any of the
// IGameObjects in the rest of its list. To be used with restAndmap().
class IntersectsAnyOtherPred<T extends IGameObject>
    implements BiFunction<T, IList<T>, Boolean> {
  /* TEMPLATE
  METHODS:
  ... apply(T that) ...                                    -- Boolean
   */
  
  // Determines if any of the vehicles or walls in rest overlap with that.
  @Override
  public Boolean apply(T that, IList<T> rest) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                  -- T
    ... rest ...                                  -- IList<T>
    METHODS ON PARAMETERS:
    ... rest.ormap(Function<T, Boolean func>) ... -- Boolean
     */
    return rest.ormap(new IntersectsPred<>(that));
  }
}

// A function that takes in an IGameObject and draws it to a WorldScene at a position dependent
// on its GridPosn
class DrawToScene<T extends IGameObject> implements BiFunction<T, WorldScene, WorldScene> {
  
  int tileSize;
  
  DrawToScene(int tileSize) {
    this.tileSize = tileSize;
  }
  
  // Draws the provided IGameObject to the provided WorldImage according to the IGameObject's
  // position and draw method
  @Override
  public WorldScene apply(T obj, WorldScene scene) {
    /* TEMPLATE
    PARAMETERS:
    ... obj ...                            -- T
    ... scene ...                          -- WorldScene
    METHODS ON PARAMETERS:
    ... obj.drawTo(WorldScene scene) ...   -- WorldScene
     */
    return obj.drawTo(scene, tileSize);
  }
}

// A predicate that determines whether the provided IGameObject's GridRect is fully contained in
// the GridRect passed during construction
class InRectPred<T extends IGameObject> implements Function<T, Boolean> {
  GridRect rect;
  
  InRectPred(GridRect rect) {
    this.rect = rect;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.rect ...      -- GridRect
  METHODS:
  ... apply(T that) ...  -- Boolean
   */
  
  // Determines whether the provided IGameObject's GridRect is fully contained in the GridRect
  // passed during construction
  @Override
  public Boolean apply(T that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...            -- T
    METHODS ON PARAMETERS:
    ... that.getRect() ...  -- Boolean
     */
    return rect.containsRect(that.getRect());
  }
}

// A predicate that returns true if the provided IVehicle is in a winning state; i.e., if it
// being a PlayerCar implies it is overlapping an Exit.
class InWinningStatePred implements Function<IVehicle, Boolean> {
  IList<Exit> exits;
  
  InWinningStatePred(IList<Exit> exits) {
    this.exits = exits;
  }
  
  /* TEMPLATE
  FIELDS:
  ... exits ...                  -- IList<Exit>
  METHODS:
  ... apply(AVehicle that) ...   -- Boolean
   */
  
  // A predicate that returns true if the provided IVehicle is in a winning state; i.e., if it
  // being a PlayerCar implies it is overlapping an Exit.
  @Override
  public Boolean apply(IVehicle that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                      -- AVehicle
    METHODS ON PARAMETERS:
    ... that.inWinningState(IList<Exits> exits) ...   -- Boolean
     */
    return that.inWinningState(this.exits);
  }
}

// A function that maps a car before a click event to a car after a click event, selecting it if
// it contains the click position and deselecting it if it does not.
class OnClick implements Function<IVehicle, IVehicle> {
  GridPosn clickedPosn;
  
  OnClick(GridPosn clickedPosn) {
    this.clickedPosn = clickedPosn;
  }
  
  /* TEMPLATE
  FIELDS:
  ... this.clickedPosn ...       -- GridPosn
  METHODS:
  ... apply(AVehicle that) ...   -- AVehicle
   */
  
  // Maps a car before a click event to a car after a click event, selecting it if it contains
  // the click position and deselecting it if it does not.
  public IVehicle apply(IVehicle that) {
    /* TEMPLATE
    PARAMETERS:
    ... that ...                                   -- AVehicle
    METHODS ON PARAMETERS:
    ... that.registerClick(GridPosn clickPosn) ...   -- AVehicle
     */
    return that.registerClick(this.clickedPosn);
  }
}