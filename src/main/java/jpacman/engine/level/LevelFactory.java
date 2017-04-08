package jpacman.engine.level;

import java.util.List;
import java.util.Map;
import jpacman.engine.board.Board;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Square;
import jpacman.engine.npc.NPC;
import jpacman.engine.npc.ghost.Ghost;
import jpacman.engine.npc.ghost.GhostColor;
import jpacman.engine.npc.ghost.GhostFactory;
import jpacman.engine.sprite.PacManSprites;
import jpacman.engine.sprite.Sprite;

/**
 * Factory that creates levels and units.
 *
 * @author Jeroen Roosen
 */
public class LevelFactory {

  private static final int GHOSTS = 4;
  private static final int BLINKY = 0;
  private static final int INKY = 1;
  private static final int PINKY = 2;
  private static final int CLYDE = 3;

  /**
   * The default value of a pellet.
   */
  private static final int PELLET_VALUE = 10;

  /**
   * The sprite store that provides sprites for units.
   */
  private final PacManSprites sprites;
  /**
   * The factory providing ghosts.
   */
  private final GhostFactory ghostFact;
  /**
   * Used to cycle through the various ghost types.
   */
  private int ghostIndex;

  /**
   * Creates a new level factory.
   *
   * @param spriteStore The sprite store providing the sprites for units.
   * @param ghostFactory The factory providing ghosts.
   */
  public LevelFactory(final PacManSprites spriteStore, final GhostFactory ghostFactory) {
    this.sprites = spriteStore;
    this.ghostIndex = -1;
    this.ghostFact = ghostFactory;
  }

  /**
   * Creates a new level from the provided data.
   *
   * @param board The board with all ghosts and pellets occupying their squares.
   * @param ghosts A list of all ghosts on the board.
   * @param startPositions A list of squares from which players may start the game.
   * @return A new level for the board.
   */
  public Level createLevel(final Board board, final List<NPC> ghosts,
      final List<Square> startPositions) {

    // We'll adopt the simple collision map for now.
    final CollisionMap collisionMap = new PlayerCollisions();

    return new Level(board, ghosts, startPositions, collisionMap);
  }

  /**
   * Creates a new ghost.
   *
   * @return The new ghost.
   */
  NPC createGhost() {
    this.ghostIndex++;
    this.ghostIndex %= GHOSTS;
    switch (this.ghostIndex) {
      case BLINKY:
        return this.ghostFact.createBlinky();
      case INKY:
        return this.ghostFact.createInky();
      case PINKY:
        return this.ghostFact.createPinky();
      case CLYDE:
        return this.ghostFact.createClyde();
      default:
        return new RandomGhost(this.sprites.getGhostSprite(GhostColor.RED));
    }
  }

  /**
   * Creates a new pellet.
   *
   * @return The new pellet.
   */
  public Pellet createPellet() {
    return new Pellet(PELLET_VALUE, this.sprites.getPelletSprite());
  }

  /**
   * Implementation of an NPC that wanders around randomly.
   *
   * @author Jeroen Roosen
   */
  private static final class RandomGhost extends Ghost {

    /**
     * The suggested delay between moves.
     */
    private static final long DELAY = 175L;

    /**
     * Creates a new random ghost.
     *
     * @param ghostSprite The sprite for the ghost.
     */
    RandomGhost(final Map<Direction, Sprite> ghostSprite) {
      super(ghostSprite, (int) DELAY, 0);
    }

    @Override
    public Direction nextMove() {
      return randomMove();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
      return new RandomGhost(super.sprites);
    }
  }
}
