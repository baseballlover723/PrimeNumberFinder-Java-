import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class PrimeNumberFinderTests {

	private MultiThreadPrimeNumberFinder finder;

	@Before
	public void setUp() throws Exception {
		finder = new MultiThreadPrimeNumberFinder("src/Prime Number.txt", false);
	}

	@Test
	public void under2() {
		ArrayList<Long> list = new ArrayList<Long>();
		assertEquals(list, finder.findPrimesUnder(2));
	}

	@Test
	public void under5() {
		assertEquals(Arrays.asList(2L, 3L), finder.findPrimesUnder(5));
	}

	@Test
	public void under6() {
		assertEquals(Arrays.asList(2L, 3L, 5L), finder.findPrimesUnder(6));
	}

	@Test
	public void under9() {
		assertEquals(Arrays.asList(2L, 3L, 5L, 7L), finder.findPrimesUnder(9));
	}

	@Test
	public void under10() {
		assertEquals(Arrays.asList(2L, 3L, 5L, 7L), finder.findPrimesUnder(10));
	}

	@Test
	public void under15() {
		assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L, 13L), finder.findPrimesUnder(15));
	}

	@Test
	public void under19() {
		assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L, 13L, 17L), finder.findPrimesUnder(19));
	}

	@Test
	public void under24() {
		assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L), finder.findPrimesUnder(24));
	}

	@Test
	public void under28() {
		assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L), finder.findPrimesUnder(28));
	}

	@Test
	public void under31() {
		assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L, 29L), finder.findPrimesUnder(31));
	}

	@Test
	public void under35() {
		assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L, 29L, 31L),
				finder.findPrimesUnder(35));
	}

	@Test
	public void under39() {
		assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L, 29L, 31L, 37L),
				finder.findPrimesUnder(39));
	}

	@Test
	public void under42() {
		assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L, 29L, 31L, 37L, 41L),
				finder.findPrimesUnder(42));
	}

	@Test
	public void under100() {
		assertEquals(Arrays.asList(2L, 3L, 5L, 7L, 11L, 13L, 17L, 19L, 23L, 29L, 31L, 37L, 41L, 43L, 47L,
				53L, 59L, 61L, 67L, 71L, 73L, 79L, 83L, 89L, 97L), finder.findPrimesUnder(100));
	}
}
