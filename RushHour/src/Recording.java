import javalib.worldimages.FontStyle;
import javalib.worldimages.TextImage;
import javalib.worldimages.WorldImage;

import java.awt.*;
import java.util.Stack;

// Represents a record of the states of the board in the form of a stack of the actions taken.
class StateRecord {
  Stack<Action> record;

  StateRecord(Stack<Action> record) {
    this.record = record;
  }

  StateRecord() {
    this.record = new Stack<>();
  }

  // Removes the topmost element of the record stack and undoes it. If there are no elements in
  // the record stack, nothing is done.
  void undo(ScoreCounter sc, IList<IMovable> movables) {
    // deselect any selected movables
    movables.forEach(new RegisterSelection<>(false, sc, this));

    if (!this.record.empty()) {
      // pop one element from the record and undo it
      this.record.pop().undo(sc);
      // increment the score counter
      sc.increment();
    }

  }

  // Ends the current action and starts a new one
  void startAction(IMovable on) {
    this.record.push(new Action(on));
  }

  // Registers a move into the current action (i.e., the action at the top of the stack)
  void registerMove(int dx, int dy) {
    if (!this.record.empty()) {
      this.record.peek().registerMove(dx, dy);
    }
  }
}

// Represents a movement of an IMovable by dx horizontally and dy vertically.
class Action {
  IMovable on;
  int dx;
  int dy;

  Action(IMovable on) {
    this(on, 0, 0);
  }

  Action(IMovable on, int dx, int dy) {
    this.on = on;
    this.dx = dx;
    this.dy = dy;
  }

  // Undoes the action specified by this Action
  void undo(ScoreCounter sc) {
    this.on.moveUnchecked(-dx, -dy);
  }

  // Registers a move on this Action, offsetting its current dx and dy by the provided dx and dy.
  void registerMove(int dx, int dy) {
    this.dx += dx;
    this.dy += dy;
  }
}

// Represents a mutable counter that keeps track of the current score and whether the score
// should be incremented on the next move.
class ScoreCounter {
  int score;
  boolean hasNewSelection;

  ScoreCounter(int score, boolean hasNewSelection) {
    this.score = score;
    this.hasNewSelection = hasNewSelection;
  }

  ScoreCounter() {
    this(0, false);
  }

  // Registers a reselection on this ScoreCounter; i.e., a movable that wasn't previously
  // selected is selected.
  // EFFECT: mutates this ScoreCounter to specify that there is a new selection
  void registerReselect() {
    this.hasNewSelection = true;
  }

  // Registers a movement on this ScoreCounter; i.e., a selected movable is moved.
  // EFFECT: if this ScoreCounter has a new selection, then this ScoreCounter's score is
  // incremented by 1.
  void registerMove() {
    if (this.hasNewSelection) {
      this.hasNewSelection = false;
      this.increment();
    }
  }

  // Registers an increment on this ScoreCounter, adding 1 to the score
  // EFFECT: the score field is mutated to add 1.
  void increment() {
    this.score += 1;
  }

  // Renders this ScoreCounter to an image
  WorldImage toImage() {
    return new TextImage("score: " + this.score, 20, FontStyle.BOLD, Color.white)
            .movePinhole(-10, -10);
  }
}