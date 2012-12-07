package tmf;

import static org.junit.Assert.*

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import groovy.xml.MarkupBuilder
import org.custommonkey.xmlunit.Diff

import static org.easymock.EasyMock.*
import static org.easymock.IMocksControl.*
import org.freeplane.plugin.script.proxy.Proxy // Mocked
import org.freeplane.plugin.script.proxy.Convertible

public class LsFactory {
	public static final String ID = "123"
	public static Ls getTestLs(){
		Ls ls = new Ls(ID)
		return ls;
	}
 }

class TestLs {
	private Ls ls

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		ls = LsFactory.getTestLs()
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testAddTs() {
		ls.add(TsFactory.getTestTs())
		assertEquals(ls.children.size(), 1)
		assertEquals(ls.children.containsKey(TsFactory.ID), true)
		assertEquals(ls.children[TsFactory.ID].class.name, "tmf.Ts")
	}

	@Test
	public final void testToGmt() {
		def writer = new StringWriter()
		def xml = new MarkupBuilder(writer)
		ls.toGmt(xml)
		def xmlDiff = new Diff(writer.toString(), '<struct type="LS"/>')
		assert xmlDiff.similar()
	}

	@Test
	public final void testPopulateConcept() {
		Proxy.NodeRO nodeMock = createMock(Proxy.NodeRO.class)
		expect(nodeMock.getId()).andReturn("NodeID").anyTimes()
		expect(nodeMock.getPlainText()).andReturn("car").anyTimes()
		replay(nodeMock)
		ls.populate(nodeMock as Proxy.Node, ["objectLanguage":"en-GB"], Constants.LsMode.CONCEPT)
		assertEquals(ls.informationUnits.size(), 1)
		assertEquals(ls.informationUnits[0].class.name, "tmf.Iu")
		assertEquals(ls.informationUnits[0].dataCategory,"objectLanguage")
		assertEquals(ls.informationUnits[0].type,"string")
		assertEquals(ls.informationUnits[0].value,"en-GB")
	}
	@Test
	public final void testPopulateSource() {
		Proxy.NodeRO nodeMock = createMock(Proxy.NodeRO.class)
		expect(nodeMock.getId()).andReturn("NodeID").anyTimes()
		expect(nodeMock.getPlainText()).andReturn("car=voiture").anyTimes()
		expect(nodeMock.getNote()).andReturn(new Convertible("I have two cars.")).anyTimes()
		expect(nodeMock.getDetails()).andReturn(new Convertible("A car is an automotive vehicle.")).anyTimes()
		replay(nodeMock)
		ls.populate(nodeMock as Proxy.Node, ["sourceLanguage":"en"], Constants.LsMode.SOURCE)
		assertEquals(ls.informationUnits.size(), 1)
		assertEquals(ls.informationUnits[0].class.name, "tmf.Iu")
		assertEquals(ls.informationUnits[0].dataCategory,"objectLanguage")
		assertEquals(ls.informationUnits[0].type,"string")
		assertEquals(ls.informationUnits[0].value,"en")
	}
	@Test
	public final void testPopulateTarget() {
		Proxy.NodeRO nodeMock = createMock(Proxy.NodeRO.class)
		expect(nodeMock.getId()).andReturn("NodeID").anyTimes()
		expect(nodeMock.getPlainText()).andReturn("car=voiture").anyTimes()
		replay(nodeMock)
		ls.populate(nodeMock as Proxy.Node, ["targetLanguage":"fr"], Constants.LsMode.TARGET)
		assertEquals 1, ls.informationUnits.size()
		assertEquals "tmf.Iu", ls.informationUnits[0].class.name
		assertEquals "objectLanguage", ls.informationUnits[0].dataCategory
		assertEquals "string", ls.informationUnits[0].type
		assertEquals "fr", ls.informationUnits[0].value
	}

}
