import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import st.EntryMap;
import st.TemplateEngine;
import st.SimpleTemplateEngine;

// Colin Parrott (s1546623) and Stephen Waddell (s1346249)

public class Task2_TDD_1 {
	private EntryMap map;
	private TemplateEngine engine;
	private SimpleTemplateEngine simpleEngine;

	private String templateEngineTestString;

	@Before
	public void setUp() throws Exception {
		map = new EntryMap();
		engine = new TemplateEngine();
		simpleEngine = new SimpleTemplateEngine();
		templateEngineTestString = "${NaMe} is ${ age  } from ${city} and likes ${like}";
	}
	

	// Spec 1 ------------Negative or Not a number	
	
	
	//Case: Not a number
	
	@Test
	public void NotANumberTestNonBaseBefore() {
		
		map.store("year", "Three years ago");

		String template = "I was born in ${year}";
		String expected = "I was born in Three years ago";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void NotANumberTestNonBaseAfter() {
		
		map.store("year", "in Three years");

		String template = "I was born in ${year}";
		String expected = "I was born in in Three years";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void NotANumberTestBaseNumBefore() {
		
		map.store("year", "Three years ago");
		map.store("base_year", "1990");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in Three years ago 1990";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void NotANumberTestBaseNonNumBefore() {
		
		map.store("year", "Three years ago");
		map.store("base_year", "won the cup");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in Three years ago won the cup";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void NotANumberTestBaseNumAfter() {
		
		map.store("year", "in Three years");
		map.store("base_year", "1990");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in in Three years 1990";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void NotANumberTestBaseNonNumAfter() {
		
		map.store("year", "in Three years");
		map.store("base_year", "won the cup");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in in Three years won the cup";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	//Case: Is Negative Number
	
	@Test
	public void IsANegativeNumberTestNonBaseBefore() {
		
		map.store("year", "-4 years ago");

		String template = "I was born in ${year}";
		String expected = "I was born in -4 years ago";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void IsANegativeNumberTestNonBaseAfter() {
		
		map.store("year", "in -4 years");

		String template = "I was born in ${year}";
		String expected = "I was born in in -4 years";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void IsANegativeNumberTestBaseNumBefore() {
		
		map.store("year", "-5 years ago");
		map.store("base_year", "1990");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in -5 years ago 1990";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void IsANegativeNumberTestBaseNonNumBefore() {
		
		map.store("year", "-5 years ago");
		map.store("base_year", "won the cup");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in -5 years ago won the cup";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void IsANegativeNumberTestBaseNumAfter() {
		
		map.store("year", "in -5 years");
		map.store("base_year", "1990");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in in -5 years 1990";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void IsANegativeNumberTestBaseNonNumAfter() {
		
		map.store("year", "in -5 years");
		map.store("base_year", "won the cup");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in in -5 years won the cup";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	// Spec 2 ------------X is zero	

	@Test
	public void XisZeroTestNonBaseBefore() {
		
		map.store("year", "0 years ago");

		String template = "I was born in ${year}";
		String expected = "I was born in 2018";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void XisZeroTestNonBaseAfter() {
		
		map.store("year", "in 0 years");

		String template = "I was born in ${year}";
		String expected = "I was born in 2018";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void XisZeroTestBaseBefore() {
		
		map.store("year", "0 years ago");
		map.store("base_year", "1990");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in 1990 1990";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void XisZeroTestBaseAfter() {
		
		map.store("year", "in 0 years");
		map.store("base_year", "1990");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in 1990 1990";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	
	// Spec 3 ------------Template ${base year} is contained
	
	@Test
	public void BaseYearTestBefore() {
		
		map.store("year", "5 years ago");
		map.store("base_year", "1990");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in 1985 1990";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void BaseYearTestAfter() {

		map.store("year", "in 2 years");
		map.store("base_year", "1990");

		String template = "I was born in ${year} ${base year}";
		String expected = "I was born in 1992 1990";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void BaseYearTestNonExpectedBefore() {

		map.store("year", "2 years ago");
		map.store("base_year", "1990");

		String template = "I was born in ${year}";
		String expected = "I was born in 1988";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void BaseYearTestNonExpectedAfter() {

		map.store("year", "in 2 years");
		map.store("base_year", "1990");

		String template = "I was born in ${year}";
		String expected = "I was born in 1992";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}

	// Spec: Cross Functional Tests ----------------------------
	
	@Test
	public void CrossFunctionalNonBaseTestBefore() {

		map.store("year", "2 years ago");
		map.store("team", "Scotland");

		String template = "I was born in ${team} in ${year}";
		String expected = "I was born in Scotland in 2016";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void CrossFunctionalNonBaseTestAfter() {

		map.store("year", "in 2 years");
		map.store("team", "Scotland");

		String template = "I was born in ${team} in ${year}";
		String expected = "I was born in Scotland in 2020";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void CrossFunctionalBaseTestBefore() {

		map.store("year", "2 years ago");
		map.store("base_year", "1990");
		map.store("team", "Scotland");

		String template = "I was born in ${team} in ${year}";
		String expected = "I was born in Scotland in 1988";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	
	@Test
	public void CrossFunctionalBaseTestAfter() {

		map.store("year", "in 2 years");
		map.store("base_year", "1990");
		map.store("team", "Scotland");

		String template = "I was born in ${team} in ${year}";
		String expected = "I was born in Scotland in 1992";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
}