package jpacman.concurrent;

import jpacman.concurrent.ghosts.Event;
import jpacman.engine.board.Direction;
import jpacman.engine.level.Player;
import jpacman.engine.ui.Keypress;

/**
 * Created by mateus on 16/02/17.
 */
public class PacmanActor extends BaseCharActor {

  public static final int SLEEP_INTERVAL = 100;

  public PacmanActor() {
    super(SLEEP_INTERVAL);
  }

  @Override
  protected void doAction() {
    final Keypress kp = pmb.getGameView().getKEYPRESS();
    if (kp != null) {
      Direction direction = kp.keypressToDirection();
      final Player player = pmb.getSinglePlayerGame().getPlayer();

      //continue forward if we can't go in the chosen direction
      if (!player.getSquare().getSquareAt(direction).isAccessibleTo(player)) {
        direction = player.getDirection();
      }
      pmb.pushToBlockingDeque(new Event(this,direction));
    }
  }
}
