package jpacman.engine.game;

import jpacman.engine.level.Level;
import jpacman.engine.level.Player;

/**
 * A game with one player and a single level.
 *
 * @author Jeroen Roosen
 */
public class SinglePlayerGame extends Game {

  /**
   * The player of this game.
   */
  private final Player player;

  /**
   * The level of this game.
   */
  private final Level level;

  /**
   * Create a new single player game for the provided level and player.
   *
   * @param p The player.
   * @param l The level.
   */
  protected SinglePlayerGame(final Player p, final Level l) {
    super();
    assert p != null;
    assert l != null;
    this.player = p;
    this.level = l;
    this.level.registerPlayer(p);
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }

  @Override
  public Level getLevel() {
    return this.level;
  }

}
