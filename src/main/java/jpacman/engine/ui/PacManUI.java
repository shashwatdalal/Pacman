package jpacman.engine.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import jpacman.engine.game.Game;
import jpacman.engine.ui.ScorePanel.ScoreFormatter;

/**
 * The default JPacMan UI frame. The PacManUI consists of the following
 * elements:
 *
 * <ul>
 * <li>A score panel at the top, displaying the score of the player(s).
 * <li>A board panel, displaying the current level, i.e. the board and all units
 * on it.
 * <li>A button panel, containing all buttons provided upon creation.
 * </ul>
 *
 * @author Jeroen Roosen
 */
public class PacManUI extends JFrame {

  /**
   * Default serialisation UID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The desired frame rate interval for the graphics in milliseconds, 40
   * being 25 fps.
   */
  private static final int FRAME_INTERVAL = 50;

  /**
   * The panel displaying the player scores.
   */
  private final ScorePanel scorePanel;

  /**
   * The panel displaying the game.
   */
  private final BoardPanel boardPanel;

  /**
   * Creates a new UI for a JPac-Man game.
   *
   * @param game The game to play.
   * @param buttons The map of caption-to-action entries that will appear as buttons on the
   * interface.
   * @param keyMappings The map of keyCode-to-action entries that will be added as key listeners to
   * the interface.
   * @param sf The formatter used to display the current score.
   */
  public PacManUI(final Game game, final Map<String, Action> buttons,
      final Map<Integer, Action> keyMappings, final ScoreFormatter sf) {
    super("JPac-Man");
    assert game != null;
    assert buttons != null;
    assert keyMappings != null;

    setDefaultCloseOperation(EXIT_ON_CLOSE);

    final PacKeyListener keys = new PacKeyListener(keyMappings);
    addKeyListener(keys);

    final JPanel buttonPanel = new ButtonPanel(buttons, this);

    this.scorePanel = new ScorePanel(game.getPlayer());
    if (sf != null) {
      this.scorePanel.setScoreFormatter(sf);
    }

    this.boardPanel = new BoardPanel(game);

    final Container contentPanel = getContentPane();
    contentPanel.setLayout(new BorderLayout());
    contentPanel.add(buttonPanel, BorderLayout.SOUTH);
    contentPanel.add(this.scorePanel, BorderLayout.NORTH);
    contentPanel.add(this.boardPanel, BorderLayout.CENTER);

    pack();
  }

  /**
   * Starts the "engine", the thread that redraws the interface at set
   * intervals.
   */
  public void start() {
    setVisible(true);

    final ScheduledExecutorService service = Executors
        .newSingleThreadScheduledExecutor();

    service.scheduleAtFixedRate(new Runnable() {

      @Override
      public void run() {
        nextFrame();
      }
    }, 0, FRAME_INTERVAL, TimeUnit.MILLISECONDS);

  }

  /**
   * Draws the next frame, i.e. refreshes the scores and game.
   */
  private void nextFrame() {
    this.boardPanel.repaint();
    this.scorePanel.refresh();
  }
}