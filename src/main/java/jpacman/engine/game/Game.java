package jpacman.engine.game;

import jpacman.engine.board.Direction;
import jpacman.engine.level.Level;
import jpacman.engine.level.Level.LevelObserver;
import jpacman.engine.level.Player;
import jpacman.engine.ui.Keypress;

/**
 * A basic implementation of a Pac-Man game.
 *
 * @author Jeroen Roosen
 */
public abstract class Game implements LevelObserver {

  /**
   * <code>true</code> if the game is in progress.
   */
  private boolean inProgress;

  /**
   * Last keyboard event;
   */
  private Keypress lastKeypress;

  /**
   * Creates a new game.
   */
  protected Game() {
    this.inProgress = false;
    this.lastKeypress = null;
  }

  /**
   * Starts or resumes the game.
   */
  public void start() {
    if (isInProgress()) {
      return;
    }
    if (getLevel().isAnyPlayerAlive()
        && getLevel().remainingPellets() > 0) {
      this.inProgress = true;
      getLevel().addObserver(this);
    }
  }

  /**
   * Pauses the game.
   */
  public void stop() {
    if (!isInProgress()) {
      return;
    }
    this.inProgress = false;
  }

  /**
   * @return <code>true</code> iff the game is started and in progress.
   */
  public boolean isInProgress() {
    return this.inProgress;
  }

  /**
   * @return An immutable list of the participants of this game.
   */
  public abstract Player getPlayer();

  /**
   * @return The level currently being played.
   */
  public abstract Level getLevel();

  /**
   * Moves the specified player one square in the given direction.
   *
   * @param player The player to move.
   * @param direction The direction to move in.
   */
  public void move(final Player player, final Direction direction) {
    if (isInProgress()) {
      // execute player move.
      getLevel().move(player, direction);
    }
  }


  @Override
  public void levelWon() {
    stop();
  }

  @Override
  public void levelLost() {
    stop();
  }

  public void reportKeypress(final Keypress keypress) {
    this.lastKeypress = keypress;
  }

  public Keypress getLastKeypress() {
    return this.lastKeypress;
  }
}
