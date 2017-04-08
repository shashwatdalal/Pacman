package jpacman;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import jpacman.concurrent.GameActor;
import jpacman.concurrent.PacmanActor;
import jpacman.concurrent.ghosts.BlinkyActor;
import jpacman.concurrent.ghosts.ClydeActor;
import jpacman.concurrent.ghosts.InkyActor;
import jpacman.concurrent.ghosts.PinkyActor;
import jpacman.engine.board.BoardFactory;
import jpacman.engine.game.Game;
import jpacman.engine.game.GameFactory;
import jpacman.engine.game.SinglePlayerGame;
import jpacman.engine.level.Level;
import jpacman.engine.level.LevelFactory;
import jpacman.engine.level.MapParser;
import jpacman.engine.level.PlayerFactory;
import jpacman.engine.npc.ghost.GhostFactory;
import jpacman.engine.sprite.PacManSprites;
import jpacman.engine.ui.Keypress;
import jpacman.engine.ui.PacManUI;
import jpacman.engine.ui.PacManUiBuilder;

/**
 * Creates and launches the JPacMan UI.
 *
 * @author Jeroen Roosen
 */
public class Launcher {

  public static final String DEFAULT_MAP = "/board.txt";
  private static final PacManSprites SPRITE_STORE = new PacManSprites();
  private String levelMap = DEFAULT_MAP;

  private PacManUI pacManUI;
  private Game game;

  // store threads in accessible fields to support testing

  private GameActor gameActor;
  private Thread gameStateThread;
  private Thread pacmanThread;
  private Thread pinkyThread;
  private Thread blinkyThread;
  private Thread inkyThread;
  private Thread clydeThread;

  /**
   * Main execution method for the Launcher.
   *
   * @param args The command line arguments - which are ignored.
   * @throws IOException When a resource could not be read.
   */
  public static void main(final String[] args) throws IOException {
    boolean withGui = true;
    if (args.length > 0 && args[0] != null) {
      withGui = !args[0].equals("true");
    }
    new Launcher().launch(withGui);
  }

  /**
   * @return The game object this launcher will start when {@link #launch(boolean)} is called.
   */
  public Game getGame() {
    return this.game;
  }

  /**
   * The map file used to populate the level.
   *
   * @return The name of the map file.
   */
  protected String getLevelMap() {
    return this.levelMap;
  }

  /**
   * Set the name of the file containing this level's map.
   *
   * @param fileName Map to be used.
   * @return Level corresponding to the given map.
   */
  public Launcher withMapFile(final String fileName) {
    this.levelMap = fileName;
    return this;
  }

  /**
   * Creates a new game using the level from {@link #makeLevel()}.
   *
   * @return a new Game.
   */
  public Game makeGame() {
    final GameFactory gf = getGameFactory();
    final Level level = makeLevel();
    return gf.createSinglePlayerGame(level);
  }

  /**
   * Creates a new level. By default this method will use the map parser to
   * parse the default board stored in the <code>board.txt</code> resource.
   *
   * @return A new level.
   */
  public Level makeLevel() {
    final MapParser parser = getMapParser();
    try (InputStream boardStream = Launcher.class.getResourceAsStream(getLevelMap())) {
      return parser.parseMap(boardStream);
    } catch (final IOException e) {
      throw new PacmanConfigurationException("Unable to create level.", e);
    }
  }

  /**
   * @return A new map parser object using the factories from {@link #getLevelFactory()} and {@link
   * #getBoardFactory()}.
   */
  protected MapParser getMapParser() {
    return new MapParser(getLevelFactory(), getBoardFactory());
  }

  /**
   * @return A new board factory using the sprite store from {@link #getSpriteStore()}.
   */
  protected BoardFactory getBoardFactory() {
    return new BoardFactory(getSpriteStore());
  }

  /**
   * @return The default {@link PacManSprites}.
   */
  protected PacManSprites getSpriteStore() {
    return SPRITE_STORE;
  }

  /**
   * @return A new factory using the sprites from {@link #getSpriteStore()} and the ghosts from
   * {@link #getGhostFactory()}.
   */
  protected LevelFactory getLevelFactory() {
    return new LevelFactory(getSpriteStore(), getGhostFactory());
  }

  /**
   * @return A new factory using the sprites from {@link #getSpriteStore()}.
   */
  protected GhostFactory getGhostFactory() {
    return new GhostFactory(getSpriteStore());
  }

  /**
   * @return A new factory using the players from {@link #getPlayerFactory()}.
   */
  protected GameFactory getGameFactory() {
    return new GameFactory(getPlayerFactory());
  }

  /**
   * @return A new factory using the sprites from {@link #getSpriteStore()}.
   */
  protected PlayerFactory getPlayerFactory() {
    return new PlayerFactory(getSpriteStore());
  }

  /**
   * Adds key events UP, DOWN, LEFT and RIGHT to a game.
   *
   * @param builder The {@link PacManUiBuilder} that will provide the UI.
   * @param game The game that will process the events.
   */
  protected void addSinglePlayerKeys(final PacManUiBuilder builder, final Game game) {
//    final Player p1 = game.getPlayer();

    builder.addKey(KeyEvent.VK_UP, () -> game.reportKeypress(Keypress.UP))
        .addKey(KeyEvent.VK_DOWN, () -> game.reportKeypress(Keypress.DOWN))
        .addKey(KeyEvent.VK_LEFT, () -> game.reportKeypress(Keypress.LEFT))
        .addKey(KeyEvent.VK_RIGHT, () -> game.reportKeypress(Keypress.RIGHT));
  }


  /**
   * Creates and starts a JPac-Man game.
   */
  public void launch(final boolean withGui) {
    this.game = makeGame();
    // Keep this object around so we can call reset()
    final GameActor gameActor = new GameActor((SinglePlayerGame) this.game);
    this.gameActor = gameActor;

    this.gameStateThread = new Thread(gameActor);
    this.pacmanThread = new Thread(new PacmanActor());
    this.pinkyThread = new Thread(new PinkyActor());
    this.blinkyThread = new Thread(new BlinkyActor());
    this.inkyThread = new Thread(new InkyActor());
    this.clydeThread = new Thread(new ClydeActor());

    if (withGui) {
      final PacManUiBuilder builder = new PacManUiBuilder().withDefaultButtons();
      addSinglePlayerKeys(builder, this.game);
      this.pacManUI = builder.build(this.game);
      this.pacManUI.start();
    } else { //headless
//      this.game.start();
    }

    // start threads only when the game starts
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(() -> {
      if (this.game.isInProgress()) {
        this.gameStateThread.start();
        this.pacmanThread.start();
        this.pinkyThread.start();
        this.blinkyThread.start();
        this.inkyThread.start();
        this.clydeThread.start();
        scheduler.shutdownNow();
      }
    }, 1, 1, TimeUnit.MILLISECONDS);
  }

  /**
   * Disposes of the UI. For more information see
   * {@link javax.swing.JFrame#dispose()}.
   */
  public void dispose() {
    if (this.pacManUI != null) {
      this.pacManUI.dispose();
    }
  }

  public Thread getGameStateThread() {
    return this.gameStateThread;
  }

  public Thread getPacmanThread() {
    return this.pacmanThread;
  }

  public Thread getPinkyThread() {
    return this.pinkyThread;
  }

  public Thread getBlinkyThread() {
    return this.blinkyThread;
  }

  public Thread getInkyThread() {
    return this.inkyThread;
  }

  public Thread getClydeThread() {
    return this.clydeThread;
  }

  public GameActor getGameActor() {
    return this.gameActor;
  }
}
