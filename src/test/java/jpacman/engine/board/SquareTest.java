package jpacman.engine.board;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite to confirm the public API of {@link Square} works as desired.
 * 
 * @author Jeroen Roosen 
 */
public class SquareTest {

	/**
	 * The square under test.
	 */
	private Square square;

	/**
	 * Resets the square under test.
	 */
	@Before
	public void setUp() {
		this.square = new BasicSquare();
	}

	/**
	 * Assert that the square holds the occupant once it has occupied it.
	 */
	@Test
	public void testOccupy() {
		final Unit occupant = mock(Unit.class);
		this.square.put(occupant);

		assertTrue(this.square.getOccupants().contains(occupant));
	}

	/**
	 * Assert that the square no longer holds the occupant after it has left the
	 * square.
	 */
	@Test
	public void testLeave() {
		final Unit occupant = mock(Unit.class);
		this.square.put(occupant);
		this.square.remove(occupant);

		assertFalse(this.square.getOccupants().contains(occupant));
	}

	/**
	 * Assert that the order in which units entered the square is preserved.
	 */
	@Test
	public void testOrder() {
		final Unit o1 = mock(Unit.class);
		final Unit o2 = mock(Unit.class);
		this.square.put(o1);
		this.square.put(o2);

		final Object[] occupantsAsArray = this.square.getOccupants().toArray();
		assertArrayEquals(new Object[] { o1, o2 }, occupantsAsArray);
	}
}
