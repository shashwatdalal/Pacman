package jpacman.engine.board;

import jpacman.engine.sprite.Sprite;

/**
 * Basic implementation of unit.
 *
 * @author Jeroen Roosen
 */
class BasicUnit extends Unit {

  /**
   * Creates a new basic unit.
   */
  BasicUnit() {
    super();
  }

  @Override
  public Sprite getSprite() {
    return null;
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return new BasicUnit();
  }
}
