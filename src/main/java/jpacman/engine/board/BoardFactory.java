package jpacman.engine.board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jpacman.engine.level.Player;
import jpacman.engine.npc.ghost.Ghost;
import jpacman.engine.sprite.PacManSprites;
import jpacman.engine.sprite.Sprite;

/**
 * A factory that creates {@link Board} objects from 2-dimensional arrays of
 * {@link Square}s.
 *
 * @author Jeroen Roosen
 */
public class BoardFactory {

  /**
   * The sprite store providing the sprites for the background.
   */
  private final PacManSprites sprites;

  /**
   * Creates a new BoardFactory that will create a board with the provided
   * background sprites.
   *
   * @param spriteStore The sprite store providing the sprites for the background.
   */
  public BoardFactory(final PacManSprites spriteStore) {
    this.sprites = spriteStore;
  }

  /**
   * Creates a new board from a grid of cells and connects it.
   *
   * @param grid The square grid of cells, in which grid[x][y] corresponds to the square at position
   * x,y.
   * @return A new board, wrapping a grid of connected cells.
   */
  public Board createBoard(final Square[][] grid) {
    assert grid != null;

    final Board board = new Board(grid);

    final int width = board.getWidth();
    final int height = board.getHeight();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        final Square square = grid[x][y];
        for (final Direction dir : Direction.values()) {
          final int dirX = (width + x + dir.getDeltaX()) % width;
          final int dirY = (height + y + dir.getDeltaY()) % height;
          final Square neighbour = grid[dirX][dirY];
          square.link(neighbour, dir);
        }
      }
    }

    return board;
  }

  public Player getClonedPlayer() {
    return this.clonedPlayer;
  }

  public Set<Ghost> getClonedGhosts() {
    return this.clonedGhosts;
  }

  public List<Square> getClonedStartSquares() {
    return this.clonedStartSquares;
  }

  private Player clonedPlayer;
  private Set<Ghost> clonedGhosts;
  private List<Square> clonedStartSquares;

  public Board copyBoard(final Board toCopy, final Set<Square> startSquares) {
    assert toCopy != null;
    this.clonedPlayer = null;
    this.clonedGhosts = new HashSet<>();
    this.clonedStartSquares = new ArrayList<>();

    final Square[][] grid = new Square[toCopy.getWidth()][toCopy.getHeight()];
    //copy square...
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[j].length; j++) {
        final Square currentSquare = toCopy.squareAt(i, j);
        final Square copy = copySquare(currentSquare);
        grid[i][j] = copy;

        // check if square is a start space
        if (startSquares.contains(currentSquare)) {
          this.clonedStartSquares.add(copy);
        }

        // clear square
        for (final Unit occupant : currentSquare.getOccupants()) {
          copy.remove(occupant);
        }

        //clone occupants, link them
        for (final Unit occupant : currentSquare.getOccupants()) {
          try {
            final Unit clone = (Unit) occupant.clone();
            clone.leaveSquare();
            clone.occupy(copy);

            //store cloned players and ghosts
            if (clone instanceof Player) {
              if (this.clonedPlayer == null) {
                this.clonedPlayer = (Player) clone;
              } else {
                throw new RuntimeException("Duplicated pacman!");
              }
            } else if (clone instanceof Ghost) {
              if (!this.clonedGhosts.add((Ghost) clone)) {
                throw new RuntimeException("Duplicated ghost!");
              }
            }
          } catch (final CloneNotSupportedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }

    //and link the squares
    return createBoard(grid);
  }

  public Square copySquare(final Square toCopy) {
    Square copy = null;
    if (toCopy instanceof Wall) {
      copy = new Wall(toCopy.getSprite());
    } else if (toCopy instanceof Ground) {
      copy = new Ground(toCopy.getSprite());
    } else {
      throw new RuntimeException();
    }

    //This will not return the correct linked occupants - use createBoard for that

    return copy;
  }

  /**
   * Creates a new square that can be occupied by any unit.
   *
   * @return A new square that can be occupied by any unit.
   */
  public Square createGround() {
    return new Ground(this.sprites.getGroundSprite());
  }

  /**
   * Creates a new square that cannot be occupied by any unit.
   *
   * @return A new square that cannot be occupied by any unit.
   */
  public Square createWall() {
    return new Wall(this.sprites.getWallSprite());
  }

  /**
   * A wall is a square that is inaccessible to anyone.
   *
   * @author Jeroen Roosen
   */
  private static final class Wall extends Square {

    /**
     * The background for this square.
     */
    private final Sprite background;

    /**
     * Creates a new wall square.
     *
     * @param sprite The background for the square.
     */
    Wall(final Sprite sprite) {
      this.background = sprite;
    }

    @Override
    public boolean isAccessibleTo(final Unit unit) {
      return false;
    }

    @Override
    public Sprite getSprite() {
      return this.background;
    }
  }

  /**
   * A wall is a square that is accessible to anyone.
   *
   * @author Jeroen Roosen
   */
  private static final class Ground extends Square {

    /**
     * The background for this square.
     */
    private final Sprite background;

    /**
     * Creates a new ground square.
     *
     * @param sprite The background for the square.
     */
    Ground(final Sprite sprite) {
      this.background = sprite;
    }

    @Override
    public boolean isAccessibleTo(final Unit unit) {
      return true;
    }

    @Override
    public Sprite getSprite() {
      return this.background;
    }
  }
}
