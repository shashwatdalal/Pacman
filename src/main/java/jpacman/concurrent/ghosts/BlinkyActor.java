package jpacman.concurrent.ghosts;

import java.util.ArrayList;
import java.util.List;
import jpacman.concurrent.PacmanMessageBus;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Square;
import jpacman.engine.board.Unit;
import jpacman.engine.npc.ghost.Blinky;
import jpacman.engine.npc.ghost.Ghost;
import jpacman.engine.npc.ghost.Navigation;

/**
 * Created by mateus on 20/02/17.
 */
public class BlinkyActor extends GhostActor {

  private final Class<? extends Ghost> GHOST_TYPE = Blinky.class;

  /**
   * {@inheritDoc}
   *
   * <p>
   * When the ghosts are not patrolling in their home corners (Blinky:
   * top-right, Pinky: top-left, Inky: bottom-right, Clyde: bottom-left),
   * Blinky will attempt to shorten the distance between Pac-Man and himself.
   * If he has to choose between shortening the horizontal or vertical
   * distance, he will choose to shorten whichever is greatest. For example,
   * if Pac-Man is four grid spaces to the left, and seven grid spaces above
   * Blinky, he'll try to move up towards Pac-Man before he moves to the left.
   * </p>
   */

  @Override
  public void nextMove() {
    final Unit player = pmb.getSinglePlayerGame().getPlayer();
    final Ghost me = pmb.getSinglePlayerGame().getLevel().getGhost(GHOST_TYPE);

    final Square destination = player.getSquare();
    final List<Square> toIgnore = new ArrayList<>(1);

    final List<Direction> path = Navigation.shortestPath(me.getSquare(),
        destination, me, toIgnore);
    Direction newDirection;
    if (path != null && !path.isEmpty()) {
      newDirection = path.get(0);
    } else {
      newDirection = player.getDirection();
    }
    newDirection = chooseAnotherDirectionIfImpassable(me, newDirection);
    pmb.pushToBlockingDeque(new Event(this,newDirection));
  }


  @Override
  public Class<? extends Ghost> getGhostType() {
    return GHOST_TYPE;
  }
}
