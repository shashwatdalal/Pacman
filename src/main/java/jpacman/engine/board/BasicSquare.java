package jpacman.engine.board;

import jpacman.engine.sprite.Sprite;

/**
 * Basic implementation of square.
 *
 * @author Jeroen Roosen
 */
public class BasicSquare extends Square {

  /**
   * Creates a new basic square.
   */
  BasicSquare() {
    super();
  }

  public BasicSquare(final BasicSquare toCopy) {

  }

  @Override
  public boolean isAccessibleTo(final Unit unit) {
    return true;
  }

  @Override
  public Sprite getSprite() {
    return null;
  }
}
