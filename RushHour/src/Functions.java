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

// A function that takes in an IGameObject and draws it to a WorldScene at a position dependent
// on its GridPosn
class DrawToScene<T extends IGameObject> implements BiFunction<T, WorldScene, WorldScene> {
  
  // Draws the provided IGameObject to the provided WorldImage according to the IGameObject's
  // position and draw method
  @Override
  public WorldScene apply(T obj, WorldScene scene) {
    return obj.drawTo(scene);
  }
}

// A predicate that determines whether the provided IGameObject's GridRect is fully contained in
// the GridRect passed during construction
class InRectPred<T extends IGameObject> implements Function<T, Boolean> {
  GridArea rect;
  
  InRectPred(GridArea rect) {
    this.rect = rect;
  }
  
  // Determines whether the provided IGameObject's GridRect is fully contained in the GridRect
  // passed during construction
  @Override
  public Boolean apply(T that) {
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
  
  // A predicate that returns true if the provided IVehicle is in a winning state; i.e., if it
  // being a PlayerCar implies it is overlapping an Exit.
  @Override
  public Boolean apply(IVehicle that) {
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
  
  // Maps a car before a click event to a car after a click event, selecting it if it contains
  // the click position and deselecting it if it does not.
  public IVehicle apply(IVehicle that) {
    return that.registerClick(this.clickedPosn);
  }
}

// A function that maps a car before a key event to a car after a key event, moving it if it is
// selected and the key represents an available movement direction.
class OnKey implements Function<IVehicle, IVehicle> {
  String key;
  IList<Wall> walls;
  IList<IVehicle> vehicles;
  
  
  OnKey(String key, IList<Wall> walls, IList<IVehicle> vehicles) {
    this.key = key;
    this.walls = walls;
    this.vehicles = vehicles;
  }
  
  // Maps a car before a click event to a car after a click event, selecting it if it contains
  // the click position and deselecting it if it does not.
  public IVehicle apply(IVehicle that) {
    return that.registerKey(key, walls, vehicles);
  }
}