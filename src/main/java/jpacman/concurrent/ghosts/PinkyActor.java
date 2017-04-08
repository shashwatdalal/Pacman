package jpacman.concurrent.ghosts;


import java.util.ArrayList;
import java.util.List;
import jpacman.concurrent.PacmanMessageBus;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Square;
import jpacman.engine.board.Unit;
import jpacman.engine.npc.ghost.Ghost;
import jpacman.engine.npc.ghost.Navigation;
import jpacman.engine.npc.ghost.Pinky;

/**
 * Created by mateus on 20/02/17.
 *
 * <p>
 * An implementation of the classic Pac-Man ghost Speedy.
 * </p>
 * <p>
 * Nickname: Pinky. Speedy gets his name for an unusual reason. Speedy appears
 * to try to outsmart Pac-Man and crash into Pac-Man from the opposite
 * direction. The truth behind this is that when Speedy isn't patrolling the
 * top-left corner of the maze, he tries to attack Pac-Man by moving to where he
 * is going to be (that is, a few spaces ahead of Pac-Man's current direction)
 * instead of right where he is, as Blinky does. It's difficult to use this to
 * your advantage, but it's possible. If Pinky is coming at you and you face a
 * different direction, even briefly, he may just turn away and attempt to cut
 * you off in the new direction while you return to your original direction. In
 * the original Japanese version, his name is Machibuse/Pinky.
 * </p>
 * <p>
 * <b>AI:</b> When the ghosts are not patrolling their home corners, Pinky wants
 * to go to the place that is four grid spaces ahead of Pac-Man in the direction
 * that Pac-Man is facing. If Pac-Man is facing down, Pinky wants to go to the
 * location exactly four spaces below Pac-Man. Moving towards this place uses
 * the same logic that Blinky uses to find Pac-Man's exact location. Pinky is
 * affected by a targeting bug if Pac-Man is facing up - when he moves or faces
 * up, Pinky tries moving towards a point up, and left, four spaces.
 * </p>
 * <p>
 * <i>Note: In the original arcade series, the ghosts' genders are unspecified
 * and assumed to be male. In 1999, the USA division of Namco and Namco Hometech
 * developed the Pac-Man World series and declared Pinky to be female.</i>
 * </p>
 * <p>
 * Source: http://strategywiki.org/wiki/Pac-Man/Getting_Started
 * </p>
 *
 * @author Jeroen Roosen
 */

public class PinkyActor extends GhostActor {

  private static final int SQUARES_AHEAD = 4;
  private Direction lastDirection;
  private final Class<? extends Ghost> GHOST_TYPE = Pinky.class;;

  /**
   * {@inheritDoc}
   *
   * <p>
   * When the ghosts are not patrolling their home corners, Pinky wants to go
   * to the place that is four grid spaces ahead of Pac-Man in the direction
   * that Pac-Man is facing. If Pac-Man is facing down, Pinky wants to go to
   * the location exactly four spaces below Pac-Man. Moving towards this place
   * uses the same logic that Blinky uses to find Pac-Man's exact location.
   * Pinky is affected by a targeting bug if Pac-Man is facing up - when he
   * moves or faces up, Pinky tries moving towards a point up, and left, four
   * spaces.
   * </p>
   */
  @Override
  public void nextMove() {
    final Unit player = pmb.getSinglePlayerGame().getPlayer();
    final Ghost me = pmb.getSinglePlayerGame().getLevel().getGhost(GHOST_TYPE);
    final Direction targetDirection = player.getDirection();

    Square destination = player.getSquare();
    for (int i = 0; i < SQUARES_AHEAD; i++) {
      destination = destination.getSquareAt(targetDirection);
    }
    final Direction facing = player.getDirection();
    final Square behind = player.getSquare().getSquareAt(facing.opposite());
    final List<Square> toIgnore = new ArrayList<>(1);
    toIgnore.add(behind);

    final List<Direction> path = Navigation.shortestPath(me.getSquare(),
        destination, me, toIgnore);
    if (path != null && !path.isEmpty()) {
      this.lastDirection = path.get(0);
    }
    this.lastDirection = chooseAnotherDirectionIfImpassable(me, this.lastDirection);
    PacmanMessageBus pmb = PacmanMessageBus.getInstance();
    pmb.pushToBlockingDeque(new Event(this, this.lastDirection));
  }

  @Override
  public Class<? extends Ghost> getGhostType() {
    return GHOST_TYPE;
  }
}

