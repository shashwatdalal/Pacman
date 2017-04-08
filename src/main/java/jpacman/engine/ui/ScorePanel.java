package jpacman.engine.ui;

import java.awt.GridLayout;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jpacman.engine.level.Player;

/**
 * A panel consisting of a column for each player, with the numbered players on
 * top and their respective scores underneath.
 *
 * @author Jeroen Roosen
 */
public class ScorePanel extends JPanel {

  /**
   * The default way in which the score is shown.
   */
  public static final ScoreFormatter DEFAULT_SCORE_FORMATTER =
      // this lambda breaks cobertura 2.7 ...
      // player) -> String.format("Score: %3d", player.getScore());
      new ScoreFormatter() {
        @Override
        public String format(final Player p) {
          return String.format("Score: %3d", p.getScore());
        }
      };
  /**
   * Default serialisation ID.
   */
  private static final long serialVersionUID = 1L;
  /**
   * The map of players and the labels their scores are on.
   */
  private final Map<Player, JLabel> scoreLabels;
  /**
   * The way to format the score information.
   */
  private ScoreFormatter scoreFormatter = DEFAULT_SCORE_FORMATTER;

  /**
   * Creates a new score panel with a column for each player.
   *
   * @param players The players to display the scores of.
   */
  public ScorePanel(final List<Player> players) {
    super();
    assert players != null;

    setLayout(new GridLayout(2, players.size()));

    for (int i = 1; i <= players.size(); i++) {
      add(new JLabel("Player " + i, JLabel.CENTER));
    }
    this.scoreLabels = new LinkedHashMap<>();
    for (final Player p : players) {
      final JLabel scoreLabel = new JLabel("0", JLabel.CENTER);
      this.scoreLabels.put(p, scoreLabel);
      add(scoreLabel);
    }
  }

  public ScorePanel(final Player player) {
    this(Arrays.asList(player));
  }

  /**
   * Refreshes the scores of the players.
   */
  protected void refresh() {
    for (final Map.Entry<Player, JLabel> entry : this.scoreLabels.entrySet()) {
      final Player p = entry.getKey();
      String score = "";
      if (!p.isAlive()) {
        score = "You died. ";
      }
      score += this.scoreFormatter.format(p);
      entry.getValue().setText(score);
    }
  }

  /**
   * Let the score panel use a dedicated score formatter.
   *
   * @param sf Score formatter to be used.
   */
  public void setScoreFormatter(final ScoreFormatter sf) {
    assert sf != null;
    this.scoreFormatter = sf;
  }

  /**
   * Provide means to format the score for a given player.
   */
  public interface ScoreFormatter {

    /**
     * Format the score of a given player.
     *
     * @param p The player and its score
     * @return Formatted score.
     */
    String format(Player p);
  }
}
