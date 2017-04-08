package jpacman.engine.npc.ghost;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import jpacman.engine.board.Board;
import jpacman.engine.board.BoardFactory;
import jpacman.engine.board.Direction;
import jpacman.engine.board.Square;
import jpacman.engine.board.Unit;
import jpacman.engine.level.LevelFactory;
import jpacman.engine.level.MapParser;
import jpacman.engine.level.Pellet;
import jpacman.engine.sprite.PacManSprites;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the various methods provided by the {@link Navigation} class.
 * 
 * @author Jeroen Roosen 
 * 
 */
public class NavigationTest {

	/**
	 * Map parser used to construct boards.
	 */
	private MapParser parser;

	/**
	 * Set up the map parser.
	 */
	@Before
	public void setUp() {
		final PacManSprites sprites = new PacManSprites();
		this.parser = new MapParser(new LevelFactory(sprites, new GhostFactory(
				sprites)), new BoardFactory(sprites));
	}

	/**
	 * Verifies that the path to the same square is empty.
	 */
	@Test
	public void testShortestPathEmpty() {
		final Board b = this.parser.parseMap(Lists.newArrayList(" ")).getBoard();
		final Square s1 = b.squareAt(0, 0);
		final Square s2 = b.squareAt(0, 0);
		final List<Direction> path = Navigation
				.shortestPath(s1, s2, mock(Unit.class));
		assertEquals(0, path.size());
	}

	/**
	 * Verifies that if no path exists, the result is <code>null</code>.
	 */
	@Test
	public void testNoShortestPath() {
		final Board b = this.parser
				.parseMap(Lists.newArrayList("#####", "# # #", "#####"))
				.getBoard();
		final Square s1 = b.squareAt(1, 1);
		final Square s2 = b.squareAt(3, 1);
		final List<Direction> path = Navigation
				.shortestPath(s1, s2, mock(Unit.class));
		assertNull(path);
	}

	/**
	 * Verifies that having no traveller ignores terrain.
	 */
	@Test
	public void testNoTraveller() {
		final Board b = this.parser
				.parseMap(Lists.newArrayList("#####", "# # #", "#####"))
				.getBoard();
		final Square s1 = b.squareAt(1, 1);
		final Square s2 = b.squareAt(3, 1);
		final List<Direction> path = Navigation.shortestPath(s1, s2, null);
		assertArrayEquals(new Direction[] { Direction.EAST, Direction.EAST },
				path.toArray(new Direction[] {}));
	}

	/**
	 * Tests if the algorithm can find a path in a straight line.
	 */
	@Test
	public void testSimplePath() {
		final Board b = this.parser.parseMap(Lists.newArrayList("####", "#  #", "####"))
				.getBoard();
		final Square s1 = b.squareAt(1, 1);
		final Square s2 = b.squareAt(2, 1);
		final List<Direction> path = Navigation
				.shortestPath(s1, s2, mock(Unit.class));
		assertArrayEquals(new Direction[] { Direction.EAST },
				path.toArray(new Direction[] {}));
	}

	/**
	 * Verifies that the algorithm can find a path when it has to take corners.
	 */
	@Test
	public void testCornerPath() {
		final Board b = this.parser.parseMap(
				Lists.newArrayList("####", "#  #", "## #", "####")).getBoard();
		final Square s1 = b.squareAt(1, 1);
		final Square s2 = b.squareAt(2, 2);
		final List<Direction> path = Navigation
				.shortestPath(s1, s2, mock(Unit.class));
		assertArrayEquals(new Direction[] { Direction.EAST, Direction.SOUTH },
				path.toArray(new Direction[] {}));
	}

	/**
	 * Verifies that the nearest object is detected.
	 */
	@Test
	public void testNearestUnit() {
		final Board b = this.parser
				.parseMap(Lists.newArrayList("#####", "# ..#", "#####"))
				.getBoard();
		final Square s1 = b.squareAt(1, 1);
		final Square s2 = b.squareAt(2, 1);
		final Square result = Navigation.findNearest(Pellet.class, s1).getSquare();
		assertEquals(s2, result);
	}

	/**
	 * Verifies that there is no such location if there is no nearest object.
	 */
	@Test
	public void testNoNearestUnit() {
		final Board b = this.parser.parseMap(Lists.newArrayList(" ")).getBoard();
		final Square s1 = b.squareAt(0, 0);
		final Unit unit = Navigation.findNearest(Pellet.class, s1);
		assertNull(unit);
	}
	
	/**
	 * Verifies that there is ghost on the default board
	 * next to cell [1, 1].
	 *  
	 * @throws IOException if board reading fails.
	 */
	@Test
	public void testFullSizedLevel() throws IOException {
		try (InputStream i = getClass().getResourceAsStream("/board.txt")) {
			final Board b = this.parser.parseMap(i).getBoard();
			final Square s1 = b.squareAt(1, 1);
			final Unit unit = Navigation.findNearest(Ghost.class, s1);
			assertNotNull(unit);
		}
	}
}
