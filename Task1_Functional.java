import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
    
    @Before
    public void setUp() throws Exception 
    {
        map = new EntryMap();
        engine = new TemplateEngine();
        simpleEngine = new SimpleTemplateEngine();
    }

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

        String output = engine.evaluate("${name} is ${age} from ${city}", map, TemplateEngine.DELETE_UNMATCHED);
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

        String output = engine.evaluate("${name} is ${age} from ${city}", map, TemplateEngine.DELETE_UNMATCHED);
        assertEquals(output, "Adam is 19 from Edinburgh");
    }
    // -------------------------------------

}
