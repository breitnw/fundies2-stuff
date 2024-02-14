import javalib.funworld.WorldScene;
import javalib.worldimages.*;

import java.util.function.BiFunction;
import java.util.function.Function;

// A predicate that determines whether the IGameObject applied to overlaps with the IGameObject
// provided on construction. Returns false if the two are the same IGameObject (instance-wise)
class OverlappingPred<T extends IGameObject> implements Function<T, Boolean> {
  IGameObject with;
  
  OverlappingPred(IGameObject with) {
    this.with = with;
  }
  
  // Determines if the IGameObject provided on construction overlaps with that.
  @Override
  public Boolean apply(T that) {
    return that.overlaps(with) && that != with;
  }
}

// A predicate that determines whether the IGameObject applied to overlaps with any of the
// vehicles or walls provided during construction.
class OverlappingAnyPred<T extends IGameObject> implements Function<T, Boolean> {
  IList<AVehicle> vehicles;
  IList<Wall> walls;
  
  OverlappingAnyPred(IList<AVehicle> vehicles, IList<Wall> walls) {
    this.vehicles = vehicles;
    this.walls = walls;
  }
  
  // Determines if any of the vehicles or walls provided on construction overlap with that.
  @Override
  public Boolean apply(T that) {
    return vehicles.ormap(new OverlappingPred<>(that))
        || walls.ormap(new OverlappingPred<>(that));
  }
}

// A function that takes in an IGameObject and draws it to a WorldScene
class DrawToScene<T extends IGameObject> implements BiFunction<T, WorldScene, WorldScene> {
  // Draws the provided IGameObject to the provided WorldImage according to the IGameObject's
  // position and draw method
  @Override
  public WorldScene apply(T obj, WorldScene scene) {
    return obj.drawTo(scene);
  }
}

class InGridPred<T extends IGameObject> implements Function<T, Boolean> {
  int gridWidth;
  int gridHeight;
  
  InGridPred(int gridWidth, int gridHeight) {
    this.gridWidth = gridWidth;
    this.gridHeight = gridHeight;
  }
  
  @Override
  public Boolean apply(T that) {
    return that.inGrid(gridWidth, gridHeight);
  }
}

class InWinningStatePred implements Function<AVehicle, Boolean> {
  IList<Exit> exits;
  
  InWinningStatePred(IList<Exit> exits) {
    this.exits = exits;
  }
  
  @Override
  public Boolean apply(AVehicle that) {
    return that.inWinningState(this.exits);
  }
}

class MapClicked implements Function<AVehicle, AVehicle> {
  GridPosn clickedPosn;
  
  MapClicked(GridPosn clickedPosn) {
    this.clickedPosn = clickedPosn;
  }
  
  public AVehicle apply(AVehicle that) {
    return that.registerClick(this.clickedPosn);
  }
}