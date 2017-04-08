package jpacman.engine.npc.ghost;

import java.util.List;
import java.util.Map;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Square;
import jpacman.engine.board.Unit;
import jpacman.engine.level.Player;
import jpacman.engine.sprite.Sprite;

/**
 * <p>
 * An implementation of the classic Pac-Man ghost Bashful.
 * </p>
 * <p>
 * Nickname: Inky. Bashful has truly earned his name. In a game of chicken
 * between Pac-Man and Bashful, Bashful might just run away. This isn't always
 * the case, but if Blinky is right behind you, it's a safe bet. He can't be
 * scared off while he patrols the southeast portion of the maze. In Japan, his
 * name is Kimagure/Aosuke.
 * </p>
 * <p>
 * <b>AI:</b> Bashful has the most complicated AI of all. When the ghosts are
 * not patrolling their home corners, Bashful considers two things: Shadow's
 * location, and the location two grid spaces ahead of Pac-Man. Bashful draws a
 * line from Shadow to the spot that is two squares in front of Pac-Man and
 * extends that line twice as far. Therefore, if Bashful is alongside Shadow
 * when they are behind Pac-Man, Bashful will usually follow Shadow the whole
 * time. But if Bashful is in front of Pac-Man when Shadow is far behind him,
 * Bashful tends to want to move away from Pac-Man (in reality, to a point very
 * far ahead of Pac-Man). Bashful is affected by a similar targeting bug that
 * affects Speedy. When Pac-Man is moving or facing up, the spot Bashful uses to
 * draw the line is two squares above and left of Pac-Man.
 * </p>
 * <p>
 * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
 * </p>
 *
 * @author Jeroen Roosen
 */
public final class Inky extends Ghost {

  private static final int SQUARES_AHEAD = 2;

  /**
   * The variation in intervals, this makes the ghosts look more dynamic and
   * less predictable.
   */
  private static final int INTERVAL_VARIATION = 50;

  /**
   * The base movement interval.
   */
  private static final int MOVE_INTERVAL = 250;

  /**
   * Creates a new "Inky", a.k.a. Bashful.
   *
   * @param spriteMap The sprites for this ghost.
   */
  public Inky(final Map<Direction, Sprite> spriteMap) {
    super(spriteMap, MOVE_INTERVAL, INTERVAL_VARIATION);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Bashful has the most complicated AI of all. When the ghosts are not
   * patrolling their home corners, Bashful considers two things: Shadow's
   * location, and the location two grid spaces ahead of Pac-Man. Bashful
   * draws a line from Shadow to the spot that is two squares in front of
   * Pac-Man and extends that line twice as far. Therefore, if Bashful is
   * alongside Shadow when they are behind Pac-Man, Bashful will usually
   * follow Shadow the whole time. But if Bashful is in front of Pac-Man when
   * Shadow is far behind him, Bashful tends to want to move away from Pac-Man
   * (in reality, to a point very far ahead of Pac-Man). Bashful is affected
   * by a similar targeting bug that affects Speedy. When Pac-Man is moving or
   * facing up, the spot Bashful uses to draw the line is two squares above
   * and left of Pac-Man.
   * </p>
   *
   * <p>
   * <b>Implementation:</b> by lack of a coordinate system there is a
   * workaround: first determine the square of Blinky (A) and the square 2
   * squares away from Pac-Man (B). Then determine the shortest path from A to
   * B regardless of terrain and walk that same path from B. This is the
   * destination.
   * </p>
   */
  // CHECKSTYLE:OFF To keep this more readable.
  @Override
  public Direction nextMove() {
    final Unit blinky = Navigation.findNearest(Blinky.class, getSquare());
    if (blinky == null) {
      return randomMove();
    }

    final Unit player = Navigation.findNearest(Player.class, getSquare());
    if (player == null) {
      return randomMove();
    }

    final Direction targetDirection = player.getDirection();
    Square playerDestination = player.getSquare();
    for (int i = 0; i < SQUARES_AHEAD; i++) {
      playerDestination = playerDestination.getSquareAt(targetDirection);
    }

    Square destination = playerDestination;
    final List<Direction> firstHalf = Navigation.shortestPath(blinky.getSquare(),
        playerDestination, null);
    if (firstHalf == null) {
      return randomMove();
    }

    for (final Direction d : firstHalf) {
      destination = playerDestination.getSquareAt(d);
    }

    final List<Direction> path = Navigation.shortestPath(getSquare(),
        destination, this);
    if (path != null && !path.isEmpty()) {
      return path.get(0);
    }
    return randomMove();
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return new Inky(super.sprites);
  }
  // CHECKSTYLE:ON

}
