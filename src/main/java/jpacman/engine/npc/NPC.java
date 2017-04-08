package jpacman.engine.npc;

import jpacman.engine.board.Direction;
import jpacman.engine.board.Unit;

/**
 * A non-player unit.
 *
 * @author Jeroen Roosen
 */
public abstract class NPC extends Unit {

  /**
   * The time that should be taken between moves.
   *
   * @return The suggested delay between moves in milliseconds.
   */
  public abstract long getInterval();

  /**
   * Calculates the next move for this unit and returns the direction to move
   * in.
   *
   * @return The direction to move in, or <code>null</code> if no move could be devised.
   */
  public abstract Direction nextMove();

}
