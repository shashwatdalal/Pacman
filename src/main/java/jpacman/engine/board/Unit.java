package jpacman.engine.board;

import jpacman.engine.sprite.Sprite;

/**
 * A unit that can be placed on a {@link Square}.
 *
 * @author Jeroen Roosen
 */
public abstract class Unit implements Cloneable {

  /**
   * The square this unit is currently occupying.
   */
  private Square square;

  /**
   * The direction this unit is facing.
   */
  private Direction direction;

  /**
   * Creates a unit that is facing east.
   */
  protected Unit() {
    this.direction = Direction.EAST;
  }

  /**
   * Returns the current direction this unit is facing.
   *
   * @return The current direction this unit is facing.
   */
  public Direction getDirection() {
    return this.direction;
  }

  /**
   * Sets this unit to face the new direction.
   *
   * @param newDirection The new direction this unit is facing.
   */
  public void setDirection(final Direction newDirection) {
    this.direction = newDirection;
  }

  /**
   * Returns the square this unit is currently occupying.
   *
   * @return The square this unit is currently occupying, or <code>null</code> if this unit is not
   * on a square.
   */
  public Square getSquare() {
    assert invariant();
    return this.square;
  }

  /**
   * Occupies the target square iff this unit is allowed to as decided by
   * {@link Square#isAccessibleTo(Unit)}.
   *
   * @param target The square to occupy.
   */
  public void occupy(final Square target) {
    assert target != null;

    if (this.square != null) {
      this.square.remove(this);
    }
    this.square = target;
    target.put(this);
    assert invariant();
  }

  /**
   * Leaves the currently occupying square, thus removing this unit from the board.
   */
  public void leaveSquare() {
    if (this.square != null) {
      this.square.remove(this);
      this.square = null;
    }
    assert invariant();
  }

  /**
   * Tests whether the square this unit is occupying has this unit listed as
   * one of its occupiers.
   *
   * @return <code>true</code> if the square this unit is occupying has this unit listed as one of
   * its occupiers, or if this unit is currently not occupying any square.
   */
  protected boolean invariant() {
    return this.square == null || this.square.getOccupants().contains(this);
  }

  /**
   * Returns the sprite of this unit.
   *
   * @return The sprite of this unit.
   */
  public abstract Sprite getSprite();

  @Override
  protected abstract Object clone() throws CloneNotSupportedException;
}
