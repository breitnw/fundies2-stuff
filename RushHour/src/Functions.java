import javalib.impworld.WorldScene;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

// PREDICATES -------------------------------------------------------------------------------------

// A predicate that determines whether the IGameObject applied to overlaps with the IGameObject
// provided on construction.
class IntersectsPred<T extends IGameObject> implements Function<T, Boolean> {
  IGameObject with;
  
  IntersectsPred(IGameObject with) {
    this.with = with;
  }
  
  // Determines if the IGameObject provided on construction overlaps with that.
  @Override
  public Boolean apply(T that) {
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
  
  // Determines if any of the vehicles or walls provided on construction overlap with that.
  @Override
  public Boolean apply(T that) {
    return this.otherObjects.ormap(new IntersectsPred<>(that));
  }
}

// A predicate that determines whether the IGameObject applied to overlaps with any of the
// IGameObjects in the rest of its list. To be used with restAndmap().
class IntersectsAnyOtherPred<T extends IGameObject> implements BiFunction<T, IList<T>, Boolean> {
  
  // Determines if any of the vehicles or walls in rest overlap with that.
  @Override
  public Boolean apply(T that, IList<T> rest) {
    return rest.ormap(new IntersectsPred<>(that));
  }
}

// A predicate that determines whether the provided IGameObject's GridArea is fully contained in
// the GridArea passed during construction
class InAreaPred<T extends IGameObject> implements Function<T, Boolean> {
  GridArea area;

  InAreaPred(GridArea area) {
    this.area = area;
  }

  // Determines whether the provided IGameObject's GridArea is fully contained in the GridArea
  // passed during construction
  @Override
  public Boolean apply(T that) {
    return this.area.containsArea(that.getArea());
  }
}

// A predicate that returns true if the provided IVehicle is in a winning state; i.e., if it
// being a PlayerCar implies it is overlapping an Exit.
class InWinningStatePred implements Function<IMovable, Boolean> {
  IList<Exit> exits;

  InWinningStatePred(IList<Exit> exits) {
    this.exits = exits;
  }

  // A predicate that returns true if the provided IVehicle is in a winning state; i.e., if it
  // being a PlayerCar implies it is overlapping an Exit.
  @Override
  public Boolean apply(IMovable that) {
    return that.inWinningState(this.exits);
  }
}

// Consumers --------------------------------------------------------------------------------------

// A consumer that takes in an IGameObject and draws it to a WorldScene at a position dependent
// on its GridPosn
class DrawToScene<T extends IGameObject> implements Consumer<T> {
  WorldScene scene;

  DrawToScene(WorldScene scene) {
    this.scene = scene;
  }
  
  // Draws the provided IGameObject to the provided WorldImage according to the IGameObject's
  // position and draw method
  // EFFECT: mutates the WorldScene to display the provided object
  @Override
  public void accept(T obj) {
    obj.drawTo(this.scene);
  }
}

// A consumer that mutates a movable according to a click event, selecting it if it contains the
// click position and deselecting it if it does not.
class OnClick implements Consumer<IMovable> {
  GridPosn clickedPosn;
  ScoreCounter sc;
  StateRecord record;
  
  OnClick(GridPosn clickedPosn, ScoreCounter sc, StateRecord record) {
    this.clickedPosn = clickedPosn;
    this.sc = sc;
    this.record = record;
  }
  
  // Mutates the provided movable according to a click event, selecting it if it contains the
  // click position and deselecting it if it does not.
  // EFFECT: mutates the provided IMovable to select or deselect it, and mutates the ScoreCounter
  // passed on construction to register new clicks if necessary.
  public void accept(IMovable that) {
    that.registerClick(this.clickedPosn, sc, record);
  }
}

// A consumer that mutates a movable according to a key event, moving it if it is selected and
// the key represents an available movement direction.
class OnKey implements Consumer<IMovable> {
  String key;
  IList<Wall> walls;
  IList<Exit> exits;
  IList<IMovable> movables;
  ScoreCounter sc;
  StateRecord record;

  OnKey(
      String key,
      IList<Wall> walls,
      IList<Exit> exits,
      IList<IMovable> movables,
      ScoreCounter sc,
      StateRecord record
  ) {
    this.key = key;
    this.walls = walls;
    this.exits = exits;
    this.movables = movables;
    this.sc = sc;
    this.record = record;
  }
  
  // Mutates a movable according to a key event, moving it if it is selected and the key
  // represents an available movement direction.
  // EFFECT: mutates the position of the IMovable according to the purpose statement, and mutates
  // the ScoreCounter passed on construction to register moves if necessary.
  public void accept(IMovable that) {
    that.registerKey(key, walls, exits, movables, sc, record);
  }
}

// A consumer that updates the selection state of the provided IMovable, updating the
// ScoreCounter if it results in a new selection.
class RegisterSelection<T extends IMovable> implements Consumer<T> {
  Boolean value;
  ScoreCounter sc;
  StateRecord record;

  RegisterSelection(boolean value, ScoreCounter sc, StateRecord record) {
    this.sc = sc;
    this.value = value;
    this.record = record;
  }

  // Registers a deselection on the provided IMovable.
  // EFFECT: Mutates the selection state of the provided object according to the value provided
  // on construction. Also registers a new selection on sc if there is one.

  @Override
  public void accept(T obj) {
    obj.registerSelectEvent(value, sc, record);
  }
}