import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import st.EntryMap;
import st.TemplateEngine;
import st.SimpleTemplateEngine;

// Colin Parrott (s1546623) and Stephen Waddell (s1346249)

public class Task1_Functional
{
    private EntryMap map;
    private TemplateEngine engine;
    private SimpleTemplateEngine simpleEngine;

    private String templateEngineTestString;

    @Before
    public void setUp() throws Exception
    {
        map = new EntryMap();
        engine = new TemplateEngine();
        simpleEngine = new SimpleTemplateEngine();
        templateEngineTestString = "${NaMe} is ${ age  } from ${city} and likes ${like}";
    }


    /**
     * EntryMap Section
     */

    // Spec 1 ------------------------------

    @Test(expected = RuntimeException.class)
    public void EntryMapTemplateNull()
    {
    	map.store(null, "text");
    }

    @Test(expected = RuntimeException.class)
    public void EntryMapTemplateEmpty()
    {
    	map.store("", "text");
    }
    // -------------------------------------

    // Spec 2 ------------------------------

    @Test(expected = RuntimeException.class)
    public void EntryMapReplaceValueNull()
    {
        map.store("ff", null);
    }
    // --------------------------------------

    // Spec 3 ------------------------------
    @Test
    public void EntryMapOrderCorrect()
    {
        map.store("name", "Adam");
        map.store("age", "19");
        map.store("city", "Edinburgh");

        String output = engine.evaluate("${name} is ${age} from ${city}", map, TemplateEngine.DEFAULT);
        assertEquals(output, "Adam is 19 from Edinburgh");
    }

     // -------------------------------------

     // Spec 4 ------------------------------
    @Test
    public void DuplicateEntriesSize()
    {
        map.store("name", "Adam");
        map.store("name", "Bob");
        assertEquals(map.getEntries().size(), 1);
    }

    @Test
    public void DuplicateEntriesCorrect()
    {
        map.store("name", "Adam");
        map.store("name", "Bob");
        map.store("age", "19");
        map.store("city", "Edinburgh");
        map.store("city", "London");

        String output = engine.evaluate("${name} is ${age} from ${city}", map, TemplateEngine.DEFAULT);
        assertEquals(output, "Adam is 19 from Edinburgh");
    }

    @Test
    public void DuplicateEntriesCorrectTriple()
    {
        map.store("name", "Adam");
        map.store("name", "Bob");
        map.store("name", "Steve");
        map.store("age", "19");
        map.store("city", "Edinburgh");
        map.store("city", "Glasgow");
        map.store("city", "London");

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

    // Spec 1 ------------------------------
    @Test
    public void TemplateNull()
    {
       map.store("name", "Adam");
       String output = engine.evaluate(null, map, TemplateEngine.DEFAULT);
       assertEquals(output, null);
    }

    @Test
    public void TemplateEmpty()
    {
       map.store("name", "Adam");
       String output = engine.evaluate("", map, TemplateEngine.DEFAULT);
       assertEquals(output, "");
    }

    // Spec 2 ------------------------------
    @Test
    public void TemplateEntryMapNull()
    {
        String output = engine.evaluate("${name}", null, TemplateEngine.DEFAULT);
        assertEquals(output, "${name}");
    }

    // Spec 3 ------------------------------

    // Used by matching mode inputs that should run the default matching mode settings
    private boolean TemplateMatchingModeUseDefaults(Integer mode)
    {
        map.store("name", "Adam");
        map.store("age", "32");
        map.store("city", "London");

        String output = engine.evaluate(templateEngineTestString, map, mode);
        return output.equals("Adam is ${ age  } from London and likes ${like}");
    }

    @Test
    public void TemplateMatchingModeNull()
    {
        assertTrue(TemplateMatchingModeUseDefaults(null));
    }

     @Test
    public void TemplateMatchingModeZero()
    {
        assertTrue(TemplateMatchingModeUseDefaults(0));
    }

    @Test
    public void TemplateMatchingModeDefault()
    {
        assertTrue(TemplateMatchingModeUseDefaults(TemplateEngine.DEFAULT));
    }

    @Test
    public void TemplateMatchingModeUnsupported()
    {
        assertTrue(TemplateMatchingModeUseDefaults(-1));
        assertTrue(TemplateMatchingModeUseDefaults(999));
    }

    @Test
    public void TemplateConflictingUnmatched()
    {
        map.store("name", "Adam");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.KEEP_UNMATCHED | TemplateEngine.DELETE_UNMATCHED);
        assertEquals(output,"Adam is  from  and likes ");
    }

    @Test
    public void TemplateConflictingCase()
    {
        map.store("nAme", "Adam");
        map.store("city", "Edinburgh");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.CASE_SENSITIVE | TemplateEngine.CASE_INSENSITIVE);
        assertEquals(output,"${NaMe} is ${ age  } from Edinburgh and likes ${like}");
    }

    @Test
    public void TemplateConflictingSearch()
    {
        map.store("nAme", "Adam");
        map.store("city", "Edinburgh");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.BLUR_SEARCH | TemplateEngine.ACCURATE_SEARCH);
        assertEquals(output,"Adam is ${ age  } from Edinburgh and likes ${like}");
    }

    // Below are 9 tests that does pairwise combinations on the matching modes
    @Test
    public void TemplateAllDefault()
    {
        assertTrue(TemplateMatchingModeUseDefaults(TemplateEngine.KEEP_UNMATCHED | TemplateEngine.CASE_INSENSITIVE | TemplateEngine.ACCURATE_SEARCH));
    }

    @Test
    public void TemplateInsensitiveOnly()
    {
        map.store("name", "Adam");
        map.store("age", "19");
        map.store("city", "Edinburgh");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.CASE_INSENSITIVE);
        assertEquals(output,"Adam is ${ age  } from Edinburgh and likes ${like}");
    }

    @Test
    public void TemplateInsensitiveOthersNonDefault()
    {
        map.store("name", "Adam");
        map.store("age", "19");
        map.store("city", "Edinburgh");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.CASE_INSENSITIVE | TemplateEngine.DELETE_UNMATCHED | TemplateEngine.BLUR_SEARCH);
        assertEquals(output,"Adam is 19 from Edinburgh and likes ");
    }

    @Test
    public void TemplateKeepMatchedOnly()
    {
        map.store("name", "Adam");
        map.store("age", "19");
        map.store("city", "Edinburgh");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.KEEP_UNMATCHED);
        assertEquals(output,"Adam is ${ age  } from Edinburgh and likes ${like}");
    }

    @Test
    public void TemplateKeepUnmatchedOthersNonDefault()
    {
        map.store("name", "Adam");
        map.store("age", "19");
        map.store("city", "Edinburgh");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.KEEP_UNMATCHED | TemplateEngine.CASE_SENSITIVE | TemplateEngine.BLUR_SEARCH);
        assertEquals(output,"${NaMe} is 19 from Edinburgh and likes ${like}");
    }

    @Test
    public void TemplateDeleteAndCaseSensitiveOnly()
    {
        map.store("name", "Adam");
        map.store("age", "19");
        map.store("city", "Edinburgh");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.DELETE_UNMATCHED | TemplateEngine.CASE_SENSITIVE);
        assertEquals(output," is  from Edinburgh and likes ");
    }

    @Test
    public void TemplateDeleteUnmatchedAndAccurateSearchOnly()
    {
        map.store("name", "Adam");
        map.store("age", "19");
        map.store("city", "Edinburgh");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.DELETE_UNMATCHED | TemplateEngine.ACCURATE_SEARCH);
        assertEquals(output,"Adam is  from Edinburgh and likes ");
    }

    @Test
    public void TemplateCaseSensitiveAndAccurateSearchOnly()
    {
        map.store("name", "Adam");
        map.store("age", "19");
        map.store("city", "Edinburgh");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.CASE_SENSITIVE | TemplateEngine.ACCURATE_SEARCH);
        assertEquals(output,"${NaMe} is ${ age  } from Edinburgh and likes ${like}");
    }

    @Test
    public void TemplateBlurSearchOnly()
    {
        map.store("name", "Adam");
        map.store("age", "19");
        map.store("city", "Edinburgh");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.BLUR_SEARCH);
        assertEquals(output,"Adam is 19 from Edinburgh and likes ${like}");
    }

    // Spec 4 ------------------------------
    @Test
    public void TemplateStandardPattern()
    {
        map.store("name", "Adam");
        map.store("age", "19");

        String output = engine.evaluate("${name} is ${age}", map, TemplateEngine.DEFAULT);
        assertEquals(output,"Adam is 19");
    }

    @Test
    public void TemplateContainsDollarSigns()
    {
        map.store("$", "Adam");
        map.store("$age$", "19");

        String output = engine.evaluate("${$} is ${$age$}", map, TemplateEngine.DEFAULT);
        assertEquals(output, "Adam is 19");
    }

    @Test
    public void TemplateContainsOpeningBrace()
    {
        map.store("name{", "Adam");
        map.store("{age", "19");

        String output = engine.evaluate("${name{} is ${{age}", map, TemplateEngine.DEFAULT);
        assertEquals("Adam is 19", output);
    }

    // Spec 5 ------------------------------

    @Test
    public void TemplateBlurSearchSpaces()
    {
        map.store("  name  ", "Adam");
        map.store("a ge", "19");
        map.store(" c  i  t y", "Edinburgh");
        map.store("like ", "chocolate");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.BLUR_SEARCH);
        assertEquals("Adam is 19 from Edinburgh and likes chocolate", output);
    }

    @Test
    public void TemplateBlurSearchTabs()
    {
        map.store("\t\tname\t\t", "Adam");
        map.store("a\tge", "19");
        map.store("\tc\ti\tt\ty", "Edinburgh");
        map.store("like\t", "chocolate");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.BLUR_SEARCH);
        assertEquals("Adam is 19 from Edinburgh and likes chocolate", output);
    }

    @Test
    public void TemplateBlurSearchCarriageReturns()
    {
        map.store("\r\rname\r\r", "Adam");
        map.store("a\rge", "19");
        map.store("\rc\ri\rt\ry", "Edinburgh");
        map.store("like\r", "chocolate");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.BLUR_SEARCH);
        assertEquals("Adam is 19 from Edinburgh and likes chocolate", output);
    }

    @Test
    public void TemplateBlurSearchNewLines()
    {
        map.store("\n\nname\n\n", "Adam");
        map.store("a\nge", "19");
        map.store("\nc\ni\nt\ny", "Edinburgh");
        map.store("like\n", "chocolate");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.BLUR_SEARCH);
        assertEquals("Adam is 19 from Edinburgh and likes chocolate", output);
    }

    @Test
    public void TemplateBlurSearchFormFeeds()
    {
        map.store("\f\fname\f\f", "Adam");
        map.store("a\fge", "19");
        map.store("\fc\fi\ft\fy", "Edinburgh");
        map.store("like\f", "chocolate");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.BLUR_SEARCH);
        assertEquals("Adam is 19 from Edinburgh and likes chocolate", output);
    }

    @Test
    public void TemplateBlurSearchCarriageReturnPlusNewLine()
    {
        map.store("\r\n\r\nname\r\n\r\n", "Adam");
        map.store("a\r\nge", "19");
        map.store("\r\nc\r\ni\r\nt\r\ny", "Edinburgh");
        map.store("like\r\n", "chocolate");

        String output = engine.evaluate(templateEngineTestString, map, TemplateEngine.BLUR_SEARCH);
        assertEquals("Adam is 19 from Edinburgh and likes chocolate", output);
    }




}
