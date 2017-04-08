package jpacman.concurrent.ghosts;

import java.util.List;
import jpacman.concurrent.PacmanMessageBus;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Square;
import jpacman.engine.board.Unit;
import jpacman.engine.npc.ghost.Blinky;
import jpacman.engine.npc.ghost.Ghost;
import jpacman.engine.npc.ghost.Inky;
import jpacman.engine.npc.ghost.Navigation;

/**
 * Created by mateus on 20/02/17.
 *
 * /**
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

public class InkyActor extends GhostActor {

  public static final int SQUARES_AHEAD = 2;
  private final Class<? extends Ghost> GHOST_TYPE = Inky.class;

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
  @Override
  protected void nextMove() {

    final Unit blinky = pmb.getSinglePlayerGame().getLevel().getGhost(Blinky.class);
    final Unit player = pmb.getSinglePlayerGame().getPlayer();
    final Ghost me = pmb.getSinglePlayerGame().getLevel().getGhost(GHOST_TYPE);

    final Direction targetDirection = player.getDirection();
    Square playerDestination = player.getSquare();
    for (int i = 0; i < SQUARES_AHEAD; i++) {
      playerDestination = playerDestination.getSquareAt(targetDirection);
    }

    Square destination = playerDestination;
    Direction nextDirection = me.getDirection(); //default to last direction

    final List<Direction> firstHalf = Navigation.shortestPath(blinky.getSquare(),
        playerDestination, null);
    if (firstHalf != null) {
      for (final Direction d : firstHalf) {
        destination = playerDestination.getSquareAt(d);
      }
      final List<Direction> path = Navigation.shortestPath(me.getSquare(),
          destination, me);
      if (path != null && !path.isEmpty()) {
        nextDirection = path.get(0);
      }
    }

    nextDirection = chooseAnotherDirectionIfImpassable(me, nextDirection);
    PacmanMessageBus pmb = PacmanMessageBus.getInstance();
    pmb.pushToBlockingDeque(new Event(this,nextDirection));
  }

  @Override
  public Class<? extends Ghost> getGhostType() {
    return GHOST_TYPE;
  }
}
