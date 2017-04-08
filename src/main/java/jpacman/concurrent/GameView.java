package jpacman.concurrent;

import jpacman.engine.game.Game;
import jpacman.engine.level.Level;
import jpacman.engine.ui.Keypress;

/**
 * Created by Shashwat on 04-Mar-17.
 */
public class GameView {

  private final Keypress KEYPRESS;
  private final Level LEVEL;

  public GameView(Level level, Keypress keypress) {
    this.KEYPRESS = keypress;
    this.LEVEL = new Level(level);
  }

  public Keypress getKEYPRESS() {
    return KEYPRESS;
  }

  public Level getLEVEL() {
    return LEVEL;
  }
  
}
