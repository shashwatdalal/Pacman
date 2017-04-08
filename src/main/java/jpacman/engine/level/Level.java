package jpacman.engine.level;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import jpacman.engine.board.Board;
import jpacman.engine.board.BoardFactory;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Square;
import jpacman.engine.board.Unit;
import jpacman.engine.npc.NPC;
import jpacman.engine.npc.ghost.Ghost;

/**
 * A level of Pac-Man. A level consists of the board with the players and the
 * AIs on it.
 *
 * @author Jeroen Roosen
 */
public class Level {

  /**
   * The board of this level.
   */
  private final Board board;

  /**
   * The squares from which players can start this game.
   */
  private final List<Square> startSquares;

  /**
   * The players on this level.
   */
  private final List<Player> players;
  /**
   * The table of possible collisions between units.
   */
  private final CollisionMap collisions;
  /**
   * The objects observing this level.
   */
  private final Set<LevelObserver> observers;

  /**
   * The ghosts roaming this level.
   */
  private final List<NPC> ghosts;
  /**
   * The start current selected starting square.
   */
  private int startSquareIndex;


  /**
   * Creates a new level for the board.
   *
   * @param b The board for the level.
   * @param ghosts The ghosts on the board.
   * @param startPositions The squares on which players start on this board.
   * @param collisionMap The collection of collisions that should be handled.
   */
  public Level(final Board b, final List<NPC> ghosts, final List<Square> startPositions,
      final CollisionMap collisionMap) {
    assert b != null;
    assert ghosts != null;
    assert startPositions != null;

    this.board = b;
    this.ghosts = ghosts;
    this.startSquares = startPositions;
    this.startSquareIndex = 0;
    this.players = new ArrayList<>();
    this.collisions = collisionMap;
    this.observers = new HashSet<>();
  }

  /**
   * Deep copy constructor. Doesn't copy observers!
   *
   * @param level the level to be copied
   */

  public Level(final Level level) {
    final Set<Square> startSquareSet = Sets.newHashSet(level.startSquares);
    final BoardFactory bf = new BoardFactory(null);
    this.board = bf.copyBoard(level.board, startSquareSet);
    this.ghosts = Lists.newArrayList(bf.getClonedGhosts());
    this.startSquares = bf.getClonedStartSquares();
    this.startSquareIndex = level.startSquareIndex;
    this.players = Lists.newArrayList(bf.getClonedPlayer());
    this.collisions = level.collisions;
    this.observers = new HashSet<>();
  }

  /**
   * Adds an observer that will be notified when the level is won or lost.
   *
   * @param observer The observer that will be notified.
   */
  public void addObserver(final LevelObserver observer) {
    this.observers.add(observer);
  }

  /**
   * Removes an observer if it was listed.
   *
   * @param observer The observer to be removed.
   */
  public void removeObserver(final LevelObserver observer) {
    this.observers.remove(observer);
  }

  /**
   * Registers a player on this level, assigning him to a starting position. A
   * player can only be registered once, registering a player again will have
   * no effect.
   *
   * @param p The player to register.
   */
  public void registerPlayer(final Player p) {
    assert p != null;
    assert !this.startSquares.isEmpty();

    if (this.players.contains(p)) {
      return;
    }
    this.players.add(p);
    final Square square = this.startSquares.get(this.startSquareIndex);
    p.occupy(square);
    this.startSquareIndex++;
    this.startSquareIndex %= this.startSquares.size();
  }

  /**
   * Returns the board of this level.
   *
   * @return The board of this level.
   */
  public Board getBoard() {
    return this.board;
  }

  /**
   * Moves the unit into the given direction if possible and handles all
   * collisions.
   *
   * @param unit The unit to move.
   * @param direction The direction to move the unit in.
   */
  public void move(final Unit unit, final Direction direction) {
    assert unit != null;
    assert direction != null;

    unit.setDirection(direction);
    final Square location = unit.getSquare();
    final Square destination = location.getSquareAt(direction);

    if (destination.isAccessibleTo(unit)) {
      final List<Unit> occupants = destination.getOccupants();
      unit.occupy(destination);
      for (final Unit occupant : occupants) {
        this.collisions.collide(unit, occupant);
      }
    }
    updateObservers();

  }

  /**
   * Updates the observers about the state of this level.
   */
  private void updateObservers() {
    if (!isAnyPlayerAlive()) {
      for (final LevelObserver o : this.observers) {
        o.levelLost();
      }
    }
    if (remainingPellets() == 0) {
      for (final LevelObserver o : this.observers) {
        o.levelWon();
      }
    }
  }

  /**
   * Returns <code>true</code> iff at least one of the players in this level
   * is alive.
   *
   * @return <code>true</code> if at least one of the registered players is alive.
   */
  public boolean isAnyPlayerAlive() {
    for (final Player p : this.players) {
      if (p.isAlive()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Counts the pellets remaining on the board.
   *
   * @return The amount of pellets remaining on the board.
   */
  public int remainingPellets() {
    final Board b = getBoard();
    int pellets = 0;
    for (int x = 0; x < b.getWidth(); x++) {
      for (int y = 0; y < b.getHeight(); y++) {
        for (final Unit u : b.squareAt(x, y).getOccupants()) {
          if (u instanceof Pellet) {
            pellets++;
          }
        }
      }
    }
    assert pellets >= 0;
    return pellets;
  }

  public List<Player> getPlayers() {
    return this.players;
  }

  public List<NPC> getGhosts() {
    return this.ghosts;
  }

  public Ghost getGhost(final Class<? extends Ghost> clazz) {
    final Level level = this;
    final Optional<NPC> result = level.getGhosts().stream()
        .filter(ghost -> ghost.getClass().equals(clazz)
        ).findFirst();
    return (Ghost) result.orElseThrow(() -> new RuntimeException("Missing ghost: " + clazz));
  }

  public Player getPlayer() {
    return getPlayers().get(0);
  }

  /**
   * An observer that will be notified when the level is won or lost.
   *
   * @author Jeroen Roosen
   */
  public interface LevelObserver {

    /**
     * The level has been won. Typically the level should be stopped when
     * this event is received.
     */
    void levelWon();

    /**
     * The level has been lost. Typically the level should be stopped when
     * this event is received.
     */
    void levelLost();
  }
}
