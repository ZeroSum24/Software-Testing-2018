import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;

import st.EntryMap;
import st.TemplateEngine;
import st.SimpleTemplateEngine;

// Colin Parrott (s1546623) and Stephen Waddell (s1346249)

public class Task2_Mutation {
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

	// MUTATION 2
	@Test
	public void UpdateDeletedEntryMapKey() {
		map.store("name", "Adam");
		map.delete("name");
		map.update("name", "Bobby");

		String result = engine.evaluate("Name is ${name}", map, TemplateEngine.DEFAULT);

		assertEquals("Name is ${name}", result);
	}

	// MUTATION 4
	@Test
	public void TemplateDoubleNested() {
		map.store("name", "Adam");
		map.store("things chocolate and bread", "cheeses");
		map.store("thing1 butter", "chocolate");
		map.store("thing2", "bread");
		map.store("thing3", "butter");

		String output = engine.evaluate("${name}'s favourite ${things ${thing1 ${thing3}} and ${thing2}}", map,
				TemplateEngine.DEFAULT);
		assertEquals("Adam's favourite cheeses", output);
	}

	// MUTATION 6
	@Test
	public void MutationSix() {
		map.store("a", "Adam");
		String result = engine.evaluate("${}", map, TemplateEngine.DEFAULT);

		assertEquals("${}", result);
	}

	// MUTATION 8
	@Test
	public void SimpleTemplateCaseSensitive() {
		String template = "Hi, my name is davId. DaViD is my forename. That's right, David being Davi again.";
		String expected = "Hi, my name is davId. DaViD is my forename. That's right, Peter being Davi again.";

		String result = simpleEngine.evaluate(template, "David", "Peter", SimpleTemplateEngine.CASE_SENSITIVE);
		assertEquals(result, expected);
	}

	// MUTATION 10
	@Test
	public void SimpleTemplateCaseNotWholeWord() {
		String template = "localVARIABLE int localId = local";
		String expected = "globalVARIABLE int globalId = global";

		String result = simpleEngine.evaluate(template, "local", "global", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

}