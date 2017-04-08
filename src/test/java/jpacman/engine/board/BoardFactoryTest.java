package jpacman.engine.board;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import jpacman.engine.sprite.PacManSprites;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the linking of squares done by the board factory.
 * 
 * @author Jeroen Roosen 
 */
public class BoardFactoryTest {

	/**
	 * The factory under test.
	 */
	private BoardFactory factory;
	
	/**
	 * Resets the factory under test.
	 */
	@Before
	public void setUp() {
		final PacManSprites sprites = mock(PacManSprites.class);
		this.factory = new BoardFactory(sprites);
	}
	
	/**
	 * Verifies that a single cell is connected to itself on all sides.
	 */
	@Test
	public void worldIsRound() {
		final Square s = new BasicSquare();
		final Square[][] grid = new Square[][]{{s}};
		this.factory.createBoard(grid);
		assertEquals(s, s.getSquareAt(Direction.NORTH));
		assertEquals(s, s.getSquareAt(Direction.SOUTH));
		assertEquals(s, s.getSquareAt(Direction.WEST));
		assertEquals(s, s.getSquareAt(Direction.EAST));
	}
	
	/**
	 * Verifies a chain of cells is connected to the east.
	 */
	@Test
	public void connectedEast() {
		final Square s1 = new BasicSquare();
		final Square s2 = new BasicSquare();
		final Square[][] grid = new Square[][]{{s1}, {s2}};
		this.factory.createBoard(grid);
		assertEquals(s2, s1.getSquareAt(Direction.EAST));
		assertEquals(s1, s2.getSquareAt(Direction.EAST));
	}
	
	/**
	 * Verifies a chain of cells is connected to the west.
	 */
	@Test
	public void connectedWest() {
		final Square s1 = new BasicSquare();
		final Square s2 = new BasicSquare();
		final Square[][] grid = new Square[][]{{s1}, {s2}};
		this.factory.createBoard(grid);
		assertEquals(s2, s1.getSquareAt(Direction.WEST));
		assertEquals(s1, s2.getSquareAt(Direction.WEST));
	}
	
	/**
	 * Verifies a chain of cells is connected to the north.
	 */
	@Test
	public void connectedNorth() {
		final Square s1 = new BasicSquare();
		final Square s2 = new BasicSquare();
		final Square[][] grid = new Square[][]{{s1, s2}};
		this.factory.createBoard(grid);
		assertEquals(s2, s1.getSquareAt(Direction.NORTH));
		assertEquals(s1, s2.getSquareAt(Direction.NORTH));
	}
	
	/**
	 * Verifies a chain of cells is connected to the south.
	 */
	@Test
	public void connectedSouth() {
		final Square s1 = new BasicSquare();
		final Square s2 = new BasicSquare();
		final Square[][] grid = new Square[][]{{s1, s2}};
		this.factory.createBoard(grid);
		assertEquals(s2, s1.getSquareAt(Direction.SOUTH));
		assertEquals(s1, s2.getSquareAt(Direction.SOUTH));
	}
}
