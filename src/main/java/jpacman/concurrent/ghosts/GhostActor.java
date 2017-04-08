package jpacman.concurrent.ghosts;

import java.util.LinkedHashSet;
import java.util.Set;
import jpacman.concurrent.BaseCharActor;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Square;
import jpacman.engine.npc.ghost.Ghost;

/**
 * Created by mateus on 20/02/17.
 */
public abstract class GhostActor extends BaseCharActor {

  public static final int GHOST_SLEEP_INTERVAL = 250;

  public GhostActor() {
    super(GHOST_SLEEP_INTERVAL);
  }

  @Override
  protected void doAction() {
    // TODO skeleton stub - remember that this extends BaseCharActor!
  }

  // TODO is void really the best return type for this method?
  protected abstract void nextMove();


  protected Direction chooseAnotherDirectionIfImpassable(final Ghost ghost,
      final Direction firstChoice) {
    final Square currentLocation = ghost.getSquare();
    if (currentLocation.getSquareAt(firstChoice).isAccessibleTo(ghost)) {
      return firstChoice;
    } else {
      final Set<Direction> candidates = new LinkedHashSet<>();
      candidates.add(ghost.getDirection()); //priority is to keep the same direction
      candidates.add(Direction.NORTH);
      candidates.add(Direction.EAST);
      candidates.add(Direction.SOUTH);
      candidates.add(Direction.WEST);

      candidates.remove(firstChoice);

      for (final Direction nextDirection : candidates) {
        if (currentLocation.getSquareAt(nextDirection).isAccessibleTo(ghost)) {
          return nextDirection;
        }
      }
      //locked in place?
      throw new RuntimeException("Unit is locked in place! " + ghost);
    }
  }

  public abstract Class<? extends Ghost> getGhostType();

  @Override
  public String toString() {
    return getGhostType().toString();
  }
}
