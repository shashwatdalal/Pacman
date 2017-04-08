package jpacman.engine.ui;

import jpacman.engine.board.Direction;

/**
 * Created by mateus on 16/02/17.
 */
public enum Keypress {
  UP, DOWN, LEFT, RIGHT;

  public Direction keypressToDirection() {
    switch (this) {
      case UP:
        return Direction.NORTH;
      case DOWN:
        return Direction.SOUTH;
      case LEFT:
        return Direction.WEST;
      case RIGHT:
        return Direction.EAST;
      default:
        throw new RuntimeException("Unhandled keypress: " + this);
    }
  }
}
