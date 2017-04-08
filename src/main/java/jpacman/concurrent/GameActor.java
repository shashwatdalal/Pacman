package jpacman.concurrent;

import javax.lang.model.element.PackageElement;
import jpacman.concurrent.ghosts.Event;
import jpacman.concurrent.ghosts.GhostActor;
import jpacman.engine.board.Direction;
import jpacman.engine.game.SinglePlayerGame;
import jpacman.engine.level.Player;
import jpacman.engine.npc.ghost.Ghost;

/**
 * Actor to control the game state in a single player game
 */
public class GameActor implements Runnable {

  public static final int SLEEP_INTERVAL_FRAMERATE_CONTROL = 200;
  private final SinglePlayerGame game;
  PacmanMessageBus pmb = PacmanMessageBus.getInstance();

  /**
   * Tips:
   *
   * - From the 'game' parameter, you can obtain the Level (getLevel()) and the last Keypress
   * (getLastKeypress()).
   *
   * - The Level class has a deep copy constructor (see jpacman.engine.level.Level#Level(jpacman.engine.level.Level)).
   * You can use it to create a copy of (almost) the entire game state.
   *
   * - If you use the deep copy constructor for Level, you must use the cloned objects contained
   * inside it (e.g. use Level.getPlayer() instead of SinglePlayer.getPlayer()), or your program
   * might start crashing in confusing ways.
   */
  public GameActor(final SinglePlayerGame game) {
    this.game = game;
  }

  public void gameLoop() {
    while (game.isInProgress()) {
      pmb.setSinglePlayerGame(game);

      GameView gameView = new GameView(game.getLevel(), game.getLastKeypress());
      pmb.setGameView(gameView);

      try {
        Thread.sleep(SLEEP_INTERVAL_FRAMERATE_CONTROL);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      Event event = pmb.pollFromBlockingDeque();
      if (event != null) {
        if (event.getACTOR() instanceof GhostActor) {
          moveGhost(
              pmb.getSinglePlayerGame().getLevel().getGhost(
                  ((GhostActor) event.getACTOR()).getGhostType()),
              event.getDIRECTION());
        } else {
          movePlayer(game.getPlayer(), event.getDIRECTION());
        }
      } else {
        movePlayer(game.getPlayer(), game.getPlayer().getDirection());
      }

      try {
        Thread.sleep(SLEEP_INTERVAL_FRAMERATE_CONTROL);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
  /**
   * Clean up! To avoid problems during the automated tests, we'll invoke this
   * method (reset()) after each game.
   */
  public void reset() {
    pmb.resestBlockingDeque();
  }

  private void moveGhost(final Ghost ghost, final Direction dir) {
    this.game.getLevel().move(ghost, dir);
  }

  private void movePlayer(final Player player, final Direction dir) {
    this.game.getLevel().move(player, dir);
  }

  @Override
  public void run() {
    gameLoop();
  }
}
