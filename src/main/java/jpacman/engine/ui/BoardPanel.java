package jpacman.engine.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import jpacman.engine.board.Board;
import jpacman.engine.board.Square;
import jpacman.engine.board.Unit;
import jpacman.engine.game.Game;

/**
 * Panel displaying a game.
 *
 * @author Jeroen Roosen
 */
class BoardPanel extends JPanel {

  /**
   * Default serialisation ID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The background colour of the board.
   */
  private static final Color BACKGROUND_COLOR = Color.BLACK;

  /**
   * The size (in pixels) of a square on the board. The initial size of this
   * panel will scale to fit a board with square of this size.
   */
  private static final int SQUARE_SIZE = 16;

  /**
   * The game to display.
   */
  private final Game game;

  /**
   * Creates a new board panel that will display the provided game.
   *
   * @param game The game to display.
   */
  BoardPanel(final Game game) {
    super();
    assert game != null;
    this.game = game;

    final Board board = game.getLevel().getBoard();

    final int w = board.getWidth() * SQUARE_SIZE;
    final int h = board.getHeight() * SQUARE_SIZE;

    final Dimension size = new Dimension(w, h);
    setMinimumSize(size);
    setPreferredSize(size);
  }

  @Override
  public void paint(final Graphics g) {
    assert g != null;
    render(this.game.getLevel().getBoard(), g, getSize());
  }

  /**
   * Renders the board on the given graphics context to the given dimensions.
   *
   * @param board The board to render.
   * @param g The graphics context to draw on.
   * @param window The dimensions to scale the rendered board to.
   */
  private void render(final Board board, final Graphics g, final Dimension window) {
    final int cellW = window.width / board.getWidth();
    final int cellH = window.height / board.getHeight();

    g.setColor(BACKGROUND_COLOR);
    g.fillRect(0, 0, window.width, window.height);

    for (int y = 0; y < board.getHeight(); y++) {
      for (int x = 0; x < board.getWidth(); x++) {
        final int cellX = x * cellW;
        final int cellY = y * cellH;
        final Square square = board.squareAt(x, y);
        render(square, g, cellX, cellY, cellW, cellH);
      }
    }
  }

  /**
   * Renders a single square on the given graphics context on the specified
   * rectangle.
   *
   * @param square The square to render.
   * @param g The graphics context to draw on.
   * @param x The x position to start drawing.
   * @param y The y position to start drawing.
   * @param w The width of this square (in pixels.)
   * @param h The height of this square (in pixels.)
   */
  private void render(final Square square, final Graphics g, final int x, final int y, final int w,
      final int h) {
    square.getSprite().draw(g, x, y, w, h);
    for (final Unit unit : square.getOccupants()) {
      unit.getSprite().draw(g, x, y, w, h);
    }
  }
}