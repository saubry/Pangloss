package pangloss.tmf;

import static org.junit.Assert.*

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import pangloss.tmf.Config;

class TestConfig {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public final void testSet() {
		def configSize = Config.config.size()
		Config.set("a", "k")
		assertEquals configSize+1, Config.config.size()
		Config.set("a", "k")
		assertEquals configSize+1, Config.config.size()
		Config.set("abc", "klm")
		assertEquals configSize+2, Config.config.size()
		Config.set("abc", "kl")
		assertEquals configSize+2, Config.config.size()
	}
	@Test
	public final void testGet() {
		assertEquals'k', Config.get('a')
		assertEquals 'kl', Config.get('abc')
		assertEquals ' > ', Config.get('pangloss_field_separator')
	}
}
