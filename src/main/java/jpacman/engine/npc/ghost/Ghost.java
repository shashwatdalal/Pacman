package jpacman.engine.npc.ghost;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Square;
import jpacman.engine.npc.NPC;
import jpacman.engine.sprite.Sprite;

/**
 * An antagonist in the game of Pac-Man, a ghost.
 *
 * @author Jeroen Roosen
 */
public abstract class Ghost extends NPC {

  /**
   * The sprite map, one sprite for each direction.
   */
  protected final Map<Direction, Sprite> sprites;

  /**
   * The base move interval of the ghost.
   */
  private final int moveInterval;

  /**
   * The random variation added to the {@link #moveInterval}.
   */
  private final int intervalVariation;

  /**
   * Creates a new ghost.
   *
   * @param spriteMap The sprites for every direction.
   * @param moveInterval The base interval of movement.
   * @param intervalVariation The variation of the interval.
   */
  protected Ghost(final Map<Direction, Sprite> spriteMap, final int moveInterval,
      final int intervalVariation) {
    this.sprites = spriteMap;
    this.intervalVariation = intervalVariation;
    this.moveInterval = moveInterval;
  }

  @Override
  public Sprite getSprite() {
    return this.sprites.get(getDirection());
  }

  @Override
  public long getInterval() {
    return this.moveInterval + new Random().nextInt(this.intervalVariation);
  }

  /**
   * Determines a possible move in a random direction.
   *
   * @return A direction in which the ghost can move, or <code>null</code> if the ghost is shut in
   * by inaccessible squares.
   */
  protected Direction randomMove() {
    final Square square = getSquare();
    final List<Direction> directions = new ArrayList<>();
    for (final Direction d : Direction.values()) {
      if (square.getSquareAt(d).isAccessibleTo(this)) {
        directions.add(d);
      }
    }
    if (directions.isEmpty()) {
      return null;
    }
    final int i = new Random().nextInt(directions.size());
    return directions.get(i);
  }
}
