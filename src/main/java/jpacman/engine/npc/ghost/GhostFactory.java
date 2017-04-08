package jpacman.engine.npc.ghost;

import jpacman.engine.sprite.PacManSprites;

/**
 * Factory that creates ghosts.
 *
 * @author Jeroen Roosen
 */
public class GhostFactory {

  /**
   * The sprite store containing the ghost sprites.
   */
  private final PacManSprites sprites;

  /**
   * Creates a new ghost factory.
   *
   * @param spriteStore The sprite provider.
   */
  public GhostFactory(final PacManSprites spriteStore) {
    this.sprites = spriteStore;
  }

  /**
   * Creates a new Blinky / Shadow, the red Ghost.
   *
   * @return A new Blinky.
   * @see Blinky
   */
  public Ghost createBlinky() {
    return new Blinky(this.sprites.getGhostSprite(GhostColor.RED));
  }

  /**
   * Creates a new Pinky / Speedy, the pink Ghost.
   *
   * @return A new Pinky.
   * @see Pinky
   */
  public Ghost createPinky() {
    return new Pinky(this.sprites.getGhostSprite(GhostColor.PINK));
  }

  /**
   * Creates a new Inky / Bashful, the cyan Ghost.
   *
   * @return A new Inky.
   * @see Inky
   */
  public Ghost createInky() {
    return new Inky(this.sprites.getGhostSprite(GhostColor.CYAN));
  }

  /**
   * Creates a new Clyde / Pokey, the orange Ghost.
   *
   * @return A new Clyde.
   * @see Clyde
   */
  public Ghost createClyde() {
    return new Clyde(this.sprites.getGhostSprite(GhostColor.ORANGE));
  }
}
