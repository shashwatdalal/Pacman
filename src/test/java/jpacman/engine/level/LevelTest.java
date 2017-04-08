package jpacman.engine.level;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import jpacman.engine.board.Board;
import jpacman.engine.board.Square;
import jpacman.engine.npc.NPC;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests various aspects of level.
 * 
 * @author Jeroen Roosen 
 */
// The four suppresswarnings ignore the same rule, which results in 4 same string literals
public class LevelTest {

	/**
	 * The level under test.
	 */
	private Level level;

	/**
	 * An NPC on this level.
	 */
	private final NPC ghost = mock(NPC.class);

	/**
	 * Starting position 1.
	 */
	private final Square square1 = mock(Square.class);

	/**
	 * Starting position 2.
	 */
	private final Square square2 = mock(Square.class);

	/**
	 * The board for this level.
	 */
	private final Board board = mock(Board.class);
	
	/**
	 * The collision map.
	 */
	private final CollisionMap collisions = mock(CollisionMap.class);

	/**
	 * Sets up the level with the default board, a single NPC and a starting
	 * square.
	 */
	@Before
	public void setUp() {
		final long defaultInterval = 100L;
		this.level = new Level(this.board, Lists.newArrayList(this.ghost), Lists.newArrayList(
				this.square1, this.square2), this.collisions);
		when(this.ghost.getInterval()).thenReturn(defaultInterval);
	}

	/**
	 * Validates the state of the level when it isn't started yet.
	 */
//	@Test
//	public void noStart() {
//		assertFalse(level.isInProgress());
//	}

	/**
	 * Validates the state of the level when it is stopped without starting.
	 */
//	@Test
//	public void stop() {
//		this.level.stop();
//		assertFalse(this.level.isInProgress());
//	}

	/**
	 * Validates the state of the level when it is started.
	 */
//	@Test
//	public void start() {
//		this.level.start();
//		assertTrue(this.level.isInProgress());
//	}

	/**
	 * Validates the state of the level when it is started then stopped.
	 */
//	@Test
//	public void startStop() {
//		this.level.start();
//		this.level.stop();
//		assertFalse(this.level.isInProgress());
//	}

	/**
	 * Verifies registering a player puts the player on the correct starting
	 * square.
	 */
	@Test
	public void registerPlayer() {
		final Player p = mock(Player.class);
		this.level.registerPlayer(p);
		verify(p).occupy(this.square1);
	}

	/**
	 * Verifies registering a player twice does not do anything.
	 */
	@Test
	public void registerPlayerTwice() {
		final Player p = mock(Player.class);
		this.level.registerPlayer(p);
		this.level.registerPlayer(p);
		verify(p, times(1)).occupy(this.square1);
	}

	/**
	 * Verifies registering a second player puts that player on the correct
	 * starting square.
	 */
	@Test
	public void registerSecondPlayer() {
		final Player p1 = mock(Player.class);
		final Player p2 = mock(Player.class);
		this.level.registerPlayer(p1);
		this.level.registerPlayer(p2);
		verify(p2).occupy(this.square2);
	}

	/**
	 * Verifies registering a third player puts the player on the correct
	 * starting square.
	 */
	@Test
	public void registerThirdPlayer() {
		final Player p1 = mock(Player.class);
		final Player p2 = mock(Player.class);
		final Player p3 = mock(Player.class);
		this.level.registerPlayer(p1);
		this.level.registerPlayer(p2);
		this.level.registerPlayer(p3);
		verify(p3).occupy(this.square1);
	}
}
