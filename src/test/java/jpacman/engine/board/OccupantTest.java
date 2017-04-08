package jpacman.engine.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite to confirm that {@link Unit}s correctly (de)occupy squares.
 * 
 * @author Jeroen Roosen 
 * 
 */
public class OccupantTest {

	/**
	 * The unit under test.
	 */
	private Unit unit;

	/**
	 * Resets the unit under test.
	 */
	@Before
	public void setUp() {
		this.unit = new BasicUnit();
	}

	/**
	 * Asserts that a unit has no square to start with.
	 */
	@Test
	public void noStartSquare() {
		assertNull(this.unit.getSquare());
	}

	/**
	 * Tests that the unit indeed has the target square as its base after
	 * occupation.
	 */
	@Test
	public void testOccupy() {
		final Square target = new BasicSquare();
		this.unit.occupy(target);
		assertThat(this.unit.getSquare()).isEqualTo(target);
		assertThat(target.getOccupants()).contains(this.unit);
	}
	
    /**
     * Test that the unit indeed has the target square as its base after
     * double occupation.
     */
	@Test
	public void testReoccupy() {
		final Square target = new BasicSquare();
		this.unit.occupy(target);
		this.unit.occupy(target);
		assertThat(this.unit.getSquare()).isEqualTo(target);
		assertThat(target.getOccupants()).contains(this.unit);
	}
}
