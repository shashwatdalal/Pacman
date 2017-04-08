package jpacman.engine.level;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import jpacman.PacmanConfigurationException;
import jpacman.engine.board.Board;
import jpacman.engine.board.BoardFactory;
import jpacman.engine.board.Square;
import jpacman.engine.npc.NPC;

/**
 * Creates new {@link Level}s from text representations.
 *
 * @author Jeroen Roosen
 */
public class MapParser {

  /**
   * The factory that creates the levels.
   */
  private final LevelFactory levelCreator;

  /**
   * The factory that creates the squares and board.
   */
  private final BoardFactory boardCreator;

  /**
   * Creates a new map parser.
   *
   * @param levelFactory The factory providing the NPC objects and the level.
   * @param boardFactory The factory providing the Square objects and the board.
   */
  public MapParser(final LevelFactory levelFactory, final BoardFactory boardFactory) {
    this.levelCreator = levelFactory;
    this.boardCreator = boardFactory;
  }

  /**
   * Parses the text representation of the board into an actual level.
   *
   * <ul>
   * <li>Supported characters:
   * <li>' ' (space) an empty square.
   * <li>'#' (bracket) a wall.
   * <li>'.' (period) a square with a pellet.
   * <li>'P' (capital P) a starting square for players.
   * <li>'G' (capital G) a square with a ghost.
   * </ul>
   *
   * @param map The text representation of the board, with map[x][y] representing the square at
   * position x,y.
   * @return The level as represented by this text.
   */
  public Level parseMap(final char[][] map) {
    final int width = map.length;
    final int height = map[0].length;

    final Square[][] grid = new Square[width][height];

    final List<NPC> ghosts = new ArrayList<>();
    final List<Square> startPositions = new ArrayList<>();

    makeGrid(map, width, height, grid, ghosts, startPositions);

    final Board board = this.boardCreator.createBoard(grid);
    return this.levelCreator.createLevel(board, ghosts, startPositions);
  }

  private void makeGrid(final char[][] map, final int width, final int height,
      final Square[][] grid, final List<NPC> ghosts, final List<Square> startPositions) {
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        final char c = map[x][y];
        addSquare(grid, ghosts, startPositions, x, y, c);
      }
    }
  }

  private void addSquare(final Square[][] grid, final List<NPC> ghosts,
      final List<Square> startPositions, final int x, final int y, final char c) {
    switch (c) {
      case ' ':
        grid[x][y] = this.boardCreator.createGround();
        break;
      case '#':
        grid[x][y] = this.boardCreator.createWall();
        break;
      case '.':
        final Square pelletSquare = this.boardCreator.createGround();
        grid[x][y] = pelletSquare;
        this.levelCreator.createPellet().occupy(pelletSquare);
        break;
      case 'G':
        final Square ghostSquare = makeGhostSquare(ghosts);
        grid[x][y] = ghostSquare;
        break;
      case 'P':
        final Square playerSquare = this.boardCreator.createGround();
        grid[x][y] = playerSquare;
        startPositions.add(playerSquare);
        break;
      default:
        throw new PacmanConfigurationException("Invalid character at "
            + x + "," + y + ": " + c);
    }
  }

  private Square makeGhostSquare(final List<NPC> ghosts) {
    final Square ghostSquare = this.boardCreator.createGround();
    final NPC ghost = this.levelCreator.createGhost();
    ghosts.add(ghost);
    ghost.occupy(ghostSquare);
    return ghostSquare;
  }

  /**
   * Parses the list of strings into a 2-dimensional character array and
   * passes it on to {@link #parseMap(char[][])}.
   *
   * @param text The plain text, with every entry in the list being a equally sized row of squares
   * on the board and the first element being the top row.
   * @return The level as represented by the text.
   * @throws PacmanConfigurationException If text lines are not properly formatted.
   */
  public Level parseMap(final List<String> text) {

    checkMapFormat(text);

    final int height = text.size();
    final int width = text.get(0).length();

    final char[][] map = new char[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        map[x][y] = text.get(y).charAt(x);
      }
    }
    return parseMap(map);
  }

  /**
   * Check the correctness of the map lines in the text.
   *
   * @param text Map to be checked
   * @throws PacmanConfigurationException if map is not OK.
   */
  private void checkMapFormat(final List<String> text) {
    if (text == null) {
      throw new PacmanConfigurationException(
          "Input text cannot be null.");
    }

    if (text.isEmpty()) {
      throw new PacmanConfigurationException(
          "Input text must consist of at least 1 row.");
    }

    final int width = text.get(0).length();

    if (width == 0) {
      throw new PacmanConfigurationException(
          "Input text lines cannot be empty.");
    }

    for (final String line : text) {
      if (line.length() != width) {
        throw new PacmanConfigurationException(
            "Input text lines are not of equal width.");
      }
    }
  }

  /**
   * Parses the provided input stream as a character stream and passes it
   * result to {@link #parseMap(List)}.
   *
   * @param source The input stream that will be read.
   * @return The parsed level as represented by the text on the input stream.
   * @throws IOException when the source could not be read.
   */
  public Level parseMap(final InputStream source) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(
        source, "UTF-8"))) {
      final List<String> lines = new ArrayList<>();
      while (reader.ready()) {
        lines.add(reader.readLine());
      }
      return parseMap(lines);
    }
  }
}
