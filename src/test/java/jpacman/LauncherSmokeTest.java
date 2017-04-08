package jpacman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import jpacman.concurrent.GameActor;
import jpacman.engine.board.Direction;
import jpacman.engine.game.Game;
import jpacman.engine.level.Player;
import jpacman.engine.ui.Keypress;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Smoke test launching the full game,
 * and attempting to make a number of typical moves.
 *
 * This is <strong>not</strong> a <em>unit</em> test -- it is an end-to-end test
 * trying to execute a large portion of the system's behavior directly from the
 * user interface. It uses the actual sprites and monster AI, and hence
 * has little control over what is happening in the game.
 *
 * Because it is an end-to-end test, it is somewhat longer
 * and has more assert statements than what would be good
 * for a small and focused <em>unit</em> test.
 *
 * @author Arie van Deursen, March 2014.
 */
public class LauncherSmokeTest {

  private Launcher launcher;

  private volatile Throwable threadException = null;

  /**
   * Launch the user interface.
   */
  @Before
  public void setUpPacman() {
//    this.launcher = new Launcher();
//    this.launcher.launch(false);
  }

  /**
   * Quit the user interface when we're done.
   */
  @After
  public void tearDown() {
//    this.launcher.dispose();
  }

  /**
   * Launch the game, and imitate what would happen in a typical game.
   * Teleports pacman around, bypassing the keyboard event handling
   * The test is only a smoke test, and not a focused small test.
   * Therefore it is OK that the method is a bit too long.
   *
   * @throws InterruptedException Since we're sleeping in this test.
   */
  @Test
  public void smokeTest() throws InterruptedException {

    for (int i = 0; i < 10; i++) {

      this.launcher = new Launcher();
      this.launcher.launch(false);

      final Game game = this.launcher.getGame();
      final Player player = game.getPlayer();

      // hook exception handlers to the threads
      // so we can capture exceptions thrown inside them
      hookExceptionHandlers(this.launcher);

      // start cleanly.
      assertFalse(game.isInProgress());
      game.start();
      assertTrue(game.isInProgress());
      assertEquals(0, player.getScore());

      Thread.sleep(100);
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      // get points

      game.move(player, Direction.EAST);
      assertEquals(10, player.getScore());
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      // now moving back does not change the score
      game.move(player, Direction.WEST);
      assertEquals(10, player.getScore());
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      // try to move as far as we can
      move(game, Direction.EAST, 7);
      assertEquals(60, player.getScore());
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      // move towards the monsters
      move(game, Direction.NORTH, 6);
      assertEquals(120, player.getScore());
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      // no more points to earn here.
      move(game, Direction.WEST, 2);
      assertEquals(120, player.getScore());
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      move(game, Direction.NORTH, 2);

      // Sleeping in tests is generally a bad idea.
      // Here we do it just to let the monsters move.
      Thread.sleep(500L);

      // we're close to monsters, this will get us killed.
      move(game, Direction.WEST, 10);
      move(game, Direction.EAST, 10);
      //assertFalse(player.isAlive());
      //assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      game.stop();
      assertFalse(game.isInProgress());

      this.launcher.dispose();
      Thread.sleep(2000); //wait until threads stop
      this.launcher.getGameActor().reset(); //cleanup for the next round
    }
  }

  /**
   * Just like the previous one, but this time we send keyboard
   * events to the pacman thread.
   */

  @Test
  public void smokierTest() throws InterruptedException {

    for (int i = 0; i < 10; i++) {

      this.launcher = new Launcher();
      this.launcher.launch(false);

      final Game game = this.launcher.getGame();
      final Player player = game.getPlayer();

      // hook exception handlers to the threads
      // so we can capture exceptions thrown inside them
      hookExceptionHandlers(this.launcher);

      // start cleanly.
      assertFalse(game.isInProgress());
      game.start();
      assertTrue(game.isInProgress());
      assertEquals(0, player.getScore());

      Thread.sleep(100);
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      // get points

      game.reportKeypress(Keypress.RIGHT);
      Thread.sleep(GameActor.SLEEP_INTERVAL_FRAMERATE_CONTROL * 2);
      assertTrue(10 <= player.getScore());
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      // now moving back does not change the score
      game.reportKeypress(Keypress.DOWN);
      Thread.sleep(GameActor.SLEEP_INTERVAL_FRAMERATE_CONTROL * 2);
      assertTrue(10 <= player.getScore());
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      // try to move as far as we can
      game.reportKeypress(Keypress.LEFT);
      Thread.sleep(GameActor.SLEEP_INTERVAL_FRAMERATE_CONTROL * 10);
      assertTrue(60 <= player.getScore());
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      // move away from the monsters
      game.reportKeypress(Keypress.DOWN);
      Thread.sleep(GameActor.SLEEP_INTERVAL_FRAMERATE_CONTROL * 3);
      assertTrue(100 <= player.getScore());
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      // no more points to earn here.
      game.reportKeypress(Keypress.LEFT);
      assertTrue(120 <= player.getScore());
      assertThreadsAreAliveAndNoExceptionWasThrown(this.launcher);

      // Sleeping in tests is generally a bad idea.
      // Here we do it just to let the monsters move.
      Thread.sleep(5000L);

      // we're close to monsters, this will get us killed.
      assertFalse(player.isAlive());
      game.stop();
      assertFalse(game.isInProgress());

      this.launcher.dispose();
      Thread.sleep(2000); //wait until threads stop
      this.launcher.getGameActor().reset(); //cleanup for the next round
    }
  }


  private void hookExceptionHandlers(final Launcher launcher) {
    launcher.getGameStateThread().setUncaughtExceptionHandler(((t, e) -> {
      this.threadException = e;
    }));
    launcher.getPacmanThread().setUncaughtExceptionHandler(((t, e) -> {
      this.threadException = e;
    }));
    launcher.getBlinkyThread().setUncaughtExceptionHandler(((t, e) -> {
      this.threadException = e;
    }));
    launcher.getInkyThread().setUncaughtExceptionHandler(((t, e) -> {
      this.threadException = e;
    }));
    launcher.getPinkyThread().setUncaughtExceptionHandler(((t, e) -> {
      this.threadException = e;
    }));
    launcher.getClydeThread().setUncaughtExceptionHandler(((t, e) -> {
      this.threadException = e;
    }));
  }

  private void assertThreadsAreAliveAndNoExceptionWasThrown(final Launcher launcher) {
    if (this.threadException != null) {
      this.threadException.printStackTrace();
    }
    assertTrue("game thread died", launcher.getGameStateThread().isAlive());
    assertTrue("pac thread died", launcher.getPacmanThread().isAlive());
    assertTrue("blinky thread died", launcher.getBlinkyThread().isAlive());
    assertTrue("clyde thread died", launcher.getClydeThread().isAlive());
    assertTrue("inky thread died", launcher.getInkyThread().isAlive());
    assertTrue("pinky thread died", launcher.getPinkyThread().isAlive());

    assertEquals(this.threadException, null);
  }

  /**
   * Make number of moves in given direction.
   *
   * @param game The game we're playing
   * @param dir The direction to be taken
   * @param numSteps The number of steps to take
   */
  public static void move(final Game game, final Direction dir, final int numSteps) {
    final Player player = game.getPlayer();
    for (int i = 0; i < numSteps; i++) {
      game.move(player, dir);
    }
  }
}
