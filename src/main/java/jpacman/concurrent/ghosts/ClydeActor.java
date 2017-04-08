package jpacman.concurrent.ghosts;

import java.util.List;
import jpacman.concurrent.PacmanMessageBus;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Square;
import jpacman.engine.level.Player;
import jpacman.engine.npc.ghost.Clyde;
import jpacman.engine.npc.ghost.Ghost;
import jpacman.engine.npc.ghost.Navigation;

/**
 * Created by mateus on 20/02/17.
 *
 * /**
 * <p>
 * An implementation of the classic Pac-Man ghost Pokey.
 * </p>
 * <p>
 * Nickname: Clyde. Pokey needs a new nickname because out of all the ghosts,
 * Pokey is the least likely to "C'lyde" with Pac-Man. Pokey is always the last
 * ghost out of the regenerator, and the loner of the gang, usually off doing
 * his own thing when not patrolling the bottom-left corner of the maze. His
 * behavior is very random, so while he's not likely to be following you in hot
 * pursuit with the other ghosts, he is a little less predictable, and still a
 * danger. In Japan, his name is Otoboke/Guzuta.
 * </p>
 * <p>
 * <b>AI:</b> Pokey has two basic AIs, one for when he's far from Pac-Man, and
 * one for when he is near to Pac-Man. When the ghosts are not patrolling their
 * home corners, and Pokey is far away from Pac-Man (beyond eight grid spaces),
 * Pokey behaves very much like Blinky, trying to move to Pac-Man's exact
 * location. However, when Pokey gets within eight grid spaces of Pac-Man, he
 * automatically changes his behavior and goes to patrol his home corner in the
 * bottom-left section of the maze.
 * </p>
 * <p>
 * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
 * </p>
 *
 * @author Jeroen Roosen
 */

public class ClydeActor extends GhostActor {

  private final Class<? extends Ghost> GHOST_TYPE = Clyde.class;
  public static final int SHYNESS = 8;

  /**
   * {@inheritDoc}
   *
   * <p>
   * Pokey has two basic AIs, one for when he's far from Pac-Man, and one for
   * when he is near to Pac-Man. When the ghosts are not patrolling their home
   * corners, and Pokey is far away from Pac-Man (beyond eight grid spaces),
   * Pokey behaves very much like Blinky, trying to move to Pac-Man's exact
   * location. However, when Pokey gets within eight grid spaces of Pac-Man,
   * he automatically changes his behavior and goes to patrol his home corner
   * in the bottom-left section of the maze.
   * </p>
   * <p>
   * <b>Implementation:</b> Lacking a patrol function so far, Clyde will just
   * move in the opposite direction when he gets within 8 cells of Pac-Man.
   * </p>
   */
  @Override
  protected void nextMove() {
    final Player player = pmb.getSinglePlayerGame().getPlayer();
    final Ghost me = pmb.getSinglePlayerGame().getLevel().getGhost(GHOST_TYPE);

    final Square target = player.getSquare();

    Direction nextDirection = me.getDirection(); //default to last direction
    final List<Direction> path = Navigation.shortestPath(me.getSquare(), target, me);
    if (path != null && !path.isEmpty()) {
      if (path.size() <= SHYNESS) {
        nextDirection = nextDirection.opposite();
      } else {
        nextDirection = path.get(0);
      }
    }
    nextDirection = chooseAnotherDirectionIfImpassable(me, nextDirection);
    PacmanMessageBus pmb = PacmanMessageBus.getInstance();
    pmb.pushToBlockingDeque(new Event(this, nextDirection));
  }

  @Override
  public Class<? extends Ghost> getGhostType() {
    return GHOST_TYPE;
  }
}
