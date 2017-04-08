package jpacman.engine.level;

import jpacman.engine.board.Unit;
import jpacman.engine.npc.ghost.Ghost;

/**
 * A simple implementation of a collision map for the JPacman player.
 * <p>
 * It uses a number of instanceof checks to implement the multiple dispatch for the
 * collisionmap. For more realistic collision maps, this approach will not scale,
 * and the recommended approach is to use a {@link CollisionInteractionMap}.
 *
 * @author Arie van Deursen, 2014
 */

public class PlayerCollisions implements CollisionMap {

  @Override
  public void collide(final Unit mover, final Unit collidedOn) {

    if (mover instanceof Player) {
      playerColliding((Player) mover, collidedOn);
    } else if (mover instanceof Ghost) {
      ghostColliding((Ghost) mover, collidedOn);
    } else if (mover instanceof Pellet) {
      pelletColliding((Pellet) mover, collidedOn);
    }
  }

  private void playerColliding(final Player player, final Unit collidedOn) {
    if (collidedOn instanceof Ghost) {
      playerVersusGhost(player, (Ghost) collidedOn);
    }

    if (collidedOn instanceof Pellet) {
      playerVersusPellet(player, (Pellet) collidedOn);
    }
  }

  private void ghostColliding(final Ghost ghost, final Unit collidedOn) {
    if (collidedOn instanceof Player) {
      playerVersusGhost((Player) collidedOn, ghost);
    }
  }

  private void pelletColliding(final Pellet pellet, final Unit collidedOn) {
    if (collidedOn instanceof Player) {
      playerVersusPellet((Player) collidedOn, pellet);
    }
  }


  /**
   * Actual case of player bumping into ghost or vice versa.
   *
   * @param player The player involved in the collision.
   * @param ghost The ghost involved in the collision.
   */
  public void playerVersusGhost(final Player player, final Ghost ghost) {
    player.setAlive(false);
  }

  /**
   * Actual case of player consuming a pellet.
   *
   * @param player The player involved in the collision.
   * @param pellet The pellet involved in the collision.
   */
  public void playerVersusPellet(final Player player, final Pellet pellet) {
    pellet.leaveSquare();
    player.addPoints(pellet.getValue());
  }

}
