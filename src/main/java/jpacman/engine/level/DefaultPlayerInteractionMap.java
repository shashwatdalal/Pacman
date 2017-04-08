package jpacman.engine.level;

import jpacman.engine.board.Unit;
import jpacman.engine.level.CollisionInteractionMap.CollisionHandler;
import jpacman.engine.npc.ghost.Ghost;

/**
 * An extensible default interaction map for collisions caused by the player.
 *
 * The implementation makes use of the interactionmap, and as such can be easily
 * and declaratively extended when new types of units (ghosts, players, ...) are
 * added.
 *
 * @author Arie van Deursen
 * @author Jeroen Roosen
 */
public class DefaultPlayerInteractionMap implements CollisionMap {

  private final CollisionMap collisions = defaultCollisions();

  /**
   * Creates the default collisions Player-Ghost and Player-Pellet.
   *
   * @return The collision map containing collisions for Player-Ghost and Player-Pellet.
   */
  private static CollisionInteractionMap defaultCollisions() {
    final CollisionInteractionMap collisionMap = new CollisionInteractionMap();

    collisionMap.onCollision(Player.class, Ghost.class,
        new CollisionHandler<Player, Ghost>() {

          @Override
          public void handleCollision(final Player player, final Ghost ghost) {
            player.setAlive(false);
          }
        });

    collisionMap.onCollision(Player.class, Pellet.class,
        new CollisionHandler<Player, Pellet>() {

          @Override
          public void handleCollision(final Player player, final Pellet pellet) {
            pellet.leaveSquare();
            player.addPoints(pellet.getValue());
          }
        });
    return collisionMap;
  }

  @Override
  public void collide(final Unit mover, final Unit movedInto) {
    this.collisions.collide(mover, movedInto);
  }
}
