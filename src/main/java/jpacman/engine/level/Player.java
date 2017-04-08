package jpacman.engine.level;

import java.util.Map;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Unit;
import jpacman.engine.sprite.AnimatedSprite;
import jpacman.engine.sprite.Sprite;

/**
 * A player operated unit in our game.
 *
 * @author Jeroen Roosen
 */
public class Player extends Unit {

  /**
   * The animations for every direction.
   */
  private final Map<Direction, Sprite> sprites;
  /**
   * The animation that is to be played when Pac-Man dies.
   */
  private final AnimatedSprite deathSprite;
  /**
   * The amount of points accumulated by this player.
   */
  private int score;
  /**
   * <code>true</code> iff this player is alive.
   */
  private boolean alive;

  /**
   * Creates a new player with a score of 0 points.
   *
   * @param spriteMap A map containing a sprite for this player for every direction.
   * @param deathAnimation The sprite to be shown when this player dies.
   */
  protected Player(final Map<Direction, Sprite> spriteMap, final AnimatedSprite deathAnimation) {
    this.score = 0;
    this.alive = true;
    this.sprites = spriteMap;
    this.deathSprite = deathAnimation;
    this.deathSprite.setAnimating(false);
  }

  /**
   * Returns whether this player is alive or not.
   *
   * @return <code>true</code> iff the player is alive.
   */
  public boolean isAlive() {
    return this.alive;
  }

  /**
   * Sets whether this player is alive or not.
   *
   * @param isAlive <code>true</code> iff this player is alive.
   */
  public void setAlive(final boolean isAlive) {
    if (isAlive) {
      this.deathSprite.setAnimating(false);
    }
    if (!isAlive) {
      this.deathSprite.restart();
    }
    this.alive = isAlive;
  }

  /**
   * Returns the amount of points accumulated by this player.
   *
   * @return The amount of points accumulated by this player.
   */
  public int getScore() {
    return this.score;
  }

  @Override
  public Sprite getSprite() {
    if (isAlive()) {
      return this.sprites.get(getDirection());
    }
    return this.deathSprite;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    final Player player = new Player(this.sprites, this.deathSprite);
    player.alive = this.alive;
    player.score = this.score;
    player.setDirection(this.getDirection());
    return player;
  }

  /**
   * Adds points to the score of this player.
   *
   * @param points The amount of points to add to the points this player already has.
   */
  public void addPoints(final int points) {
    this.score += points;
  }
}
