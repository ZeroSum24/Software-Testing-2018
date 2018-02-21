import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import st.EntryMap;
import st.TemplateEngine;
import st.SimpleTemplateEngine;

// Colin Parrott (s1546623) and Stephen Waddell (s1346249)

public class Task1_Functional {
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

	/**
	 * EntryMap Section 1.2
	 */

	// Spec 1 ------------Template Null/Empty

	@Test(expected = RuntimeException.class)
	public void EntryMapDeleteNull() {
		map.delete(null);
	}

	@Test(expected = RuntimeException.class)
	public void EntryMapDeleteEmpty() {
		map.delete("");
	}
	// -------------------------------------

	// Spec 2 ------------Consistent Order

	@Test
	public void EntryMapDeleteOrderCorrect() {
		map.store("age", "19");
		map.store("city", "Edinburgh");
		map.store("occupation", "Doctor");
		map.store("name", "Adam");

		map.delete("occupation");

		String output = engine.evaluate("${name} is ${age} from ${city}", map, TemplateEngine.DEFAULT);
		assertEquals(output, "Adam is 19 from Edinburgh");
	}
	// -------------------------------------

	// Spec 3 ------------Existing Value Pair

	@Test
	public void EntryMapDeleteChangesOutput() {
		map.store("age", "19");
		map.store("city", "Edinburgh");
		map.store("name", "Adam");

		map.delete("name");

		String output = engine.evaluate("${name} is ${age} from ${city}", map, TemplateEngine.DEFAULT);
		assertNotEquals(output, "Adam is 19 from Edinburgh");
	}
	// -------------------------------------

	@Test
	public void EntryMapDeleteExistingValueNothing() {
		map.store("age", "19");
		map.store("city", "Edinburgh");
		map.store("name", "Adam");

		map.delete("address");

		String output = engine.evaluate("${name} is ${age} from ${city}", map, TemplateEngine.DEFAULT);
		assertEquals(output, "Adam is 19 from Edinburgh");
	}
	// -------------------------------------

	/**
	 * EntryMap Section 1.3
	 */

	// Spec 1 ------------Template Null/Empty

	@Test(expected = RuntimeException.class)
	public void EntryMapUpdateTemplateNull() {
		map.update(null, "Adam");
	}

	@Test(expected = RuntimeException.class)
	public void EntryMapUpdateTemplateEmpty() {
		map.update("", "Adam");
	}

	// Spec 2 ------------Value Null

	@Test(expected = RuntimeException.class)
	public void EntryMapUpdateValueNull() {
		map.update("name", null);
	}
	// -------------------------------------

	// TODO: Using the same test set-up for order as Colin but unsure if it is
	// testing output or order
	// --> what does the question mean by order?
	// This is because the output is pulling output regardless of order

	// Spec 3 ------------Consistent Order

	@Test
	public void EntryMapUpdateOrderCorrect() {
		map.store("age", "19");
		map.store("city", "Edinburgh");
		map.store("name", "Adam");

		map.update("name", "Jimmy");
		map.update("city", "Aberdeen");

		String output = engine.evaluate("${name} is ${age} from ${city}", map, TemplateEngine.DEFAULT);
		assertEquals(output, "Jimmy is 19 from Aberdeen");
	}
	// -------------------------------------

	// Spec 4 ------------Existing Value Pair

	@Test
	public void EntryMapUpdateExistingValueNothing() {
		map.store("age", "19");
		map.store("city", "Edinburgh");
		map.store("name", "Adam");

		map.update("address", "Appleton Tower");

		String output = engine.evaluate("${name} is ${age} from ${city}", map, TemplateEngine.DEFAULT);
		assertEquals(output, "Adam is 19 from Edinburgh");
	}
	// -------------------------------------

	/**
	 * End of EntryMap section
	 */

	/**
	 * TemplateEngine section
	 */

	// Spec 8 ------------Different Template Length Ordering
	// TODO re-check these again
	
	@Test
	public void TemplateDifferentLength() {
		map.store("s", "19");
		map.store("dem", "Edinburgh");
		map.store("lm", "Adam");
		map.store("fgijkAdamnopqr", "value");

		String template = "abc}${dem}${fgijk${lm}nopqr}${s}uvw${xyz";
		String expected = "abc}Edinburghvalue19uvw${xyz";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}

	@Test
	public void TemplateSameLength() {
		map.store("s", "19");
		map.store("de", "Edinburgh");
		map.store("lm", "Adam");
		map.store("Edinburgh${lm}", "de occurs first");
		map.store("${de}Adam", "lm occurs first");

		String template = "abc}${${de}${lm}}${s}uvw${xyz";
		String expected = "abc}${EdinburghAdam}19uvw${xyz";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);

	}

	@Test
	public void TemplateExpectedLength() {
		map.store("s", "19");
		map.store("de", "Edinburgh");
		map.store("lm", "Adam");
		map.store("Edinburgh${lm}", "test1");
		map.store("${de}Adam", "test2");

		String template = "abc}${${de}${lm}}${s}uvw${xyz";
		String expected = "abc}${EdinburghAdam}19uvw${xyz";

		String output = engine.evaluate(template, map, TemplateEngine.DEFAULT);
		assertEquals(output, expected);
	}
	// -------------------------------------

	// Spec 9 ------------Template Match or Exhausted

	// --Match_Occurs

	// --1.
	@Test
	public void TemplateIsReplaced() {
		map.store("age", "19");
		map.store("city", "Edinburgh");
		map.store("name", "Adam");

		map.update("address", "Appleton Tower");

		String output = engine.evaluate("${name} is ${age} from ${city} who is ${name}", map, TemplateEngine.DEFAULT);
		assertEquals(output, "Adam is 19 from Edinburgh who is Adam");
	}

	// --2.
	@Test
	public void TemplateIsReplacedIncWrongTemplate() {
		map.store("age", "19");
		map.store("city", "Edinburgh is ${name}");
		map.store("name", "Adam");

		map.update("address", "Appleton Tower");

		String output = engine.evaluate("${name} is ${age} from ${city} who is ${name}", map, TemplateEngine.DEFAULT);
		assertEquals(output, "Adam is 19 from Edinburgh is ${name} who is Adam");
	}

	@Test
	public void TemplateIsReplacedIncTemplate() {
		map.store("age", "19");
		map.store("city", "Edinburgh");
		map.store("name", "Adam");
		map.store("age of Adam", "age");

		map.update("address", "Appleton Tower");

		String output = engine.evaluate("${name} is ${${age of ${name}}} from ${city} who is ${name}", map,
				TemplateEngine.DEFAULT);
		assertEquals(output, "Adam is 19 from Edinburgh who is Adam");
	}

	// --3.
	@Test
	public void TemplateIsReplacedandRepeats() {
		map.store("age", "19");
		map.store("city", "Edinburgh");
		map.store("name", "Adam");
		map.store("age of Adam", "age");
		map.store("city of Adam", "name");

		map.update("address", "Appleton Tower");

		String output = engine.evaluate("${name} is ${${age of ${name}}} from ${city} who is ${${city of ${name}}}",
				map, TemplateEngine.DEFAULT);
		assertEquals(output, "Adam is 19 from Edinburgh who is Adam");
	}
	// --List_Exhausted_and_No_Match

	// --1.
	@Test
	public void TemplateKeepMoveOn() {
		map.store("age", "19");
		map.store("city", "Edinburgh");
		map.store("name", "Adam");

		String output = engine.evaluate("${name} is ${age} from ${city} works as a ${occupation}", map,
				TemplateEngine.KEEP_UNMATCHED);
		assertEquals(output, "Adam is 19 from Edinburgh works as a ${occupation}");
	}

	// --2.
	@Test
	public void TemplateDeletesUnmatched() {
		map.store("age", "19");
		map.store("city", "Edinburgh");
		map.store("name", "Adam");

		String output = engine.evaluate("${name} is ${age} from ${city} works as a ${occupation}", map,
				TemplateEngine.DELETE_UNMATCHED);
		assertEquals(output, "Adam is 19 from Edinburgh works as a ");
	}// TODO does delete_unmatched need to be on?

	// -------------------------------------

	/**
	 * End of TemplateEngine section
	 */

	/**
	 * SimpleTemplateEngine section
	 */

	@Test
	public void SimpleTemplateCorrect() {
		String template = "Hi, my name is David. David is my forename.";

		String result = simpleEngine.evaluate(template, "David#2", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, "Hi, my name is David. Peter is my forename.");
	}

	// Spec 1 ------------Template Null/Empty

	@Test
	public void SimpleTemplateNull() {
		String template = null;

		String result = simpleEngine.evaluate(template, "David#2", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, null);
	}

	@Test
	public void SimpleTemplateEmpty() {
		String template = "";

		String result = simpleEngine.evaluate(template, "David#2", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, "");
	}
	// -------------------------------------

	// Spec 2 ------------Formatted Pattern Null/Empty

	@Test
	public void SimplePatternNull() {
		String template = "Hi, my name is David. David is my forename.";

		String result = simpleEngine.evaluate(template, null, "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, template);
	}

	@Test
	public void SimplePatternEmpty() {
		String template = "Hi, my name is David. David is my forename.";

		String result = simpleEngine.evaluate(template, "", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, template);
	}
	// -------------------------------------

	// Spec 3 ------------Value Null/Empty

	@Test
	public void SimpleValueNull() {
		String template = "Hi, my name is David. David is my forename.";

		String result = simpleEngine.evaluate(template, "David#2", null, SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, template);
	}

	@Test
	public void SimpleValueEmpty() {
		String template = "Hi, my name is David. David is my forename.";

		String result = simpleEngine.evaluate(template, "David#2", "", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, template);
	}
	// -------------------------------------

	// Spec 4 ------------Special Value #

	// --1.
	@Test
	public void SimpleValueOnewith2Values() {
		String template = "Hi, my name is David. David is my forename.";
		String expected = "Hi, my name is Peter. Peter is my forename.";

		String result = simpleEngine.evaluate(template, "David", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleValueOnewith4Values() {
		String template = "Hi, my name is David. David is my forename. That's right, David being David again.";
		String expected = "Hi, my name is Peter. Peter is my forename. That's right, Peter being Peter again.";

		String result = simpleEngine.evaluate(template, "David", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	// --2.
	@Test
	public void SimpleValueTwowithNot3Values() {
		String template = "Hi, my name is David. David is my forename.";
		String expected = template;

		String result = simpleEngine.evaluate(template, "David#3", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleValueTwowithlessthan3Values() {
		String template = "Hi, my name is David.";
		String expected = template;

		String result = simpleEngine.evaluate(template, "David#3", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	// TODO confirm the not or less interpretation with Colin at point 3
	// Tests word so **shrugs**
	@Test
	public void SimpleValueTwowithmorethan3Values() {
		String template = "Hi, my name is David. David is my forename. That's right, David being David again.";
		String expected = "Hi, my name is David. David is my forename. That's right, Peter being David again.";

		String result = simpleEngine.evaluate(template, "David#3", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleValueTwowith3Values() {
		String template = "Hi, my name is David. David is my forename. That's right, David.";
		String expected = "Hi, my name is David. David is my forename. That's right, Peter.";

		String result = simpleEngine.evaluate(template, "David#3", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	// --3.

	@Test
	public void SimpleValueThreewith1hashandnorm() {
		String template = "Hi, my name is David. David is my forename. That's right, David being David again.";
		String expected = template;

		String result = simpleEngine.evaluate(template, "David#", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleValueThreewith1hash() {
		String template = "Hi, my name is David#. David# is my forename. That's right, David being David# again.";
		String expected = template;

		String result = simpleEngine.evaluate(template, "David#", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleValueThreewith2hashes() {
		String template = "Hi, my name is David#. David# is my forename. That's right, David being David# again.";
		String expected = "Hi, my name is Peter. Peter is my forename. That's right, David being Peter again.";

		String result = simpleEngine.evaluate(template, "David##", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleValueTwowit2hashesandnorm() {
		String template = "Hi, my name is David##. David is my forename. That's right, David## is David#.";
		String expected = "Hi, my name is Peter#. David is my forename. That's right, Peter# is Peter.";

		String result = simpleEngine.evaluate(template, "David##", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleValueTwowith3hashes() {
		String template = "Hi, my name is David. David is my forename. That's right, David being David again.";
		String expected = template;

		String result = simpleEngine.evaluate(template, "David###", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleValueTwowith4hashes() {
		String template = "Hi, my name is David. David## is my forename. That's right, David## being David again.";
		String expected = "Hi, my name is David. Peter is my forename. That's right, Peter being David again.";

		String result = simpleEngine.evaluate(template, "David####", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleValueTwowith5hashes() {
		String template = "Hi, my name is David#. David## is my forename. That's right, David### being David#### again is David#####.";
		String expected = template;

		String result = simpleEngine.evaluate(template, "David#####", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	// --4.
	@Test
	public void SimpleValueFouronly2ndValue() {
		String template = "Hi, my name is David#. David# is my forename. That's right, David# being David# again.";
		String expected = "Hi, my name is David#. Peter is my forename. That's right, David# being David# again.";

		String result = simpleEngine.evaluate(template, "David###2", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleValueFouronly2ndValuemixedhash() {
		String template = "Hi, my name is David##. David# is my forename. That's right, David### being David again.";
		String expected = "Hi, my name is David##. Peter is my forename. That's right, David### being David again.";

		String result = simpleEngine.evaluate(template, "David###2", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	// --5.
	@Test
	public void SimpleValueFiveonly2ndValue() {
		String template = "Hi, my name is Davi. David# is my forename. That's right, David being David# again.";
		String expected = "Hi, my name is Davi. David# is my forename. That's right, Peterd being David# again.";

		String result = simpleEngine.evaluate(template, "Davi#3d", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}
	// -------------------------------------

	// Spec 5 ------------Case Sensitive

	// --Case_Sensitive
	@Test
	public void SimpleTemplateCaseSensitive() {
		String template = "Hi, my name is davId. DaViD is my forename. That's right, David being Davi again.";
		String expected = "Hi, my name is davId. DaViD is my forename. That's right, Peter being Davi again.";

		String result = simpleEngine.evaluate(template, "David", "Peter", SimpleTemplateEngine.CASE_SENSITIVE);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleTemplateCaseInsensitive() {
		String template = "Hi, my name is davId. DaViD is my forename. That's right, David being Davi again.";
		String expected = "Hi, my name is Peter. Peter is my forename. That's right, Peter being Davi again.";

		String result = simpleEngine.evaluate(template, "David", "Peter", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}
	// --Whole_Word_Search

	@Test
	public void SimpleTemplateCaseNotWholeWord() {
		String template = "localVARIABLE int localId = local";
		String expected = "globalVARIABLE int globalId = global";

		String result = simpleEngine.evaluate(template, "local", "global", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleTemplateCaseWholeWord() {
		String template = "localVARIABLE int localId = local";
		String expected = "localVARIABLE int localId = global";

		String result = simpleEngine.evaluate(template, "local", "global", SimpleTemplateEngine.WHOLE_WORLD_SEARCH);
		assertEquals(result, expected);
	}
	// --Word_Seperators

	// --Word_Seperators -- digits

	// TODO check interpretation with Colin -- All special characters other than
	// digits and letters are considered as word separators.

	@Test
	public void SimpleTemplateCaseWholeWordDigitsPattern() {
		String template = "localVARIABLE int localId = lo1cal";
		String expected = "localVARIABLE int localId = global";

		String result = simpleEngine.evaluate(template, "lo1cal", "global", SimpleTemplateEngine.WHOLE_WORLD_SEARCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleTemplateCaseWholeWordDigitsNonPattern() {
		String template = "localVARIABLE int localId = lo1cal";
		String expected = "localVARIABLE int localId = lo1cal";

		String result = simpleEngine.evaluate(template, "local", "global", SimpleTemplateEngine.WHOLE_WORLD_SEARCH);
		assertEquals(result, expected);
	}

	// --Word_Seperators -- letters
	@Test
	public void SimpleTemplateCaseWholeWordLettersPattern() {
		String template = "localVARIABLE int localId = lobcal";
		String expected = "localVARIABLE int localId = global";

		String result = simpleEngine.evaluate(template, "lobcal", "global", SimpleTemplateEngine.WHOLE_WORLD_SEARCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleTemplateCaseWholeWordLettersNonPattern() {
		String template = "localVARIABLE int localId = lobcal";
		String expected = "localVARIABLE int localId = lobcal";

		String result = simpleEngine.evaluate(template, "local", "global", SimpleTemplateEngine.WHOLE_WORLD_SEARCH);
		assertEquals(result, expected);
	}

	// --Word_Seperators -- special characters
	@Test
	public void SimpleTemplateCaseWholeWordSpecialsPattern() {
		String template = "localVARIABLE int localId = lo?cal";
		String expected = "localVARIABLE int localId = lo?cal";

		String result = simpleEngine.evaluate(template, "local", "global", SimpleTemplateEngine.WHOLE_WORLD_SEARCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleTemplateCaseWholeWordSpecialsNonPattern() {
		String template = "localVARIABLE int localId = lo?cal";
		String expected = "localVARIABLE int localId = global";

		String result = simpleEngine.evaluate(template, "lo?cal", "global", SimpleTemplateEngine.WHOLE_WORLD_SEARCH);
		assertEquals(result, expected);
	}

	// --Case_Sensitive && Whole_Word_Search
	@Test
	public void SimpleTemplateCaseSensitiveAndWholeWordCorrect() {
		String template = "localVARIABLE int localId = local is LOCaL";
		String expected = "localVARIABLE int localId = global is LOCaL";

		String result = simpleEngine.evaluate(template, "local", "global",
				SimpleTemplateEngine.CASE_SENSITIVE | SimpleTemplateEngine.WHOLE_WORLD_SEARCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleTemplateCaseandWholeWord() {
		String template = "loCalVARIABLE int localId = local";
		String expected = "loCalVARIABLE int localId = global";

		String result = simpleEngine.evaluate(template, "local", "global", SimpleTemplateEngine.WHOLE_WORLD_SEARCH);
		assertEquals(result, expected);
	}
	// -------------------------------------

	// Spec 6 ------------No Recursive Replacement

	@Test
	public void SimpleTemplateNoRecusive() {
		String template = "defabc";
		String expected = "defabcabc";

		String result = simpleEngine.evaluate(template, "abc", "abcabc", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}

	@Test
	public void SimpleTemplateNoRecusiveAltExample() {
		String template = "DavidisDavidDavid";
		String expected = "DavidisDavidisDavidisDavidDavidisDavid";

		String result = simpleEngine.evaluate(template, "David", "DavidisDavid", SimpleTemplateEngine.DEFAULT_MATCH);
		assertEquals(result, expected);
	}
	// -------------------------------------

	/**
	 * End of SimpleTemplateEngine section
	 */

	/**
	 * End of Part_1
	 */

}