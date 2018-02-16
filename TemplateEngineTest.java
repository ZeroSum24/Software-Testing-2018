import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import st.EntryMap;
import st.TemplateEngine;
import st.SimpleTemplateEngine;

public class TemplateEngineTest {

    private EntryMap map;

    private TemplateEngine engine;
    
    private SimpleTemplateEngine simpleEngine;
    
    @Before
    public void setUp() throws Exception {
        map = new EntryMap();
        engine = new TemplateEngine();
        simpleEngine = new SimpleTemplateEngine();
    }
    
    @Test
    public void Test1() {
        map.store("name", "Adam");
        map.store("surname", "Dykes");
        Integer matchingMode = TemplateEngine.DEFAULT;
        String result = engine.evaluate("Hello ${name} ${SURNAME}", map, matchingMode);
        assertEquals("Hello Adam Dykes", result);
    }
    
    @Test
    public void Test2() {
        map.store("name", "Adam");
        map.store("surname", "Dykes");
        map.store("age", "29");
        Integer matchingMode = TemplateEngine.DELETE_UNMATCHED | TemplateEngine.BLUR_SEARCH;
        String result = engine.evaluate("Hello ${name}, is your age ${age ${symbol}}", map, matchingMode);
        assertEquals("Hello Adam, is your age 29", result);
    }
    
    @Test
    public void Test3() {
        String template = "Hello, this is DAVID. David is 25 years old.";
        String pattern = "david";
        String value = "Tom";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("Hello, this is Tom. Tom is 25 years old.", result);
    }
    
    @Test
    public void Test4() {
        String template = "That is Arthur's Seat. Arthur's Seat is UK's highest mountain.";
        String pattern = "Arthur's Seat#2";
        String value = "Ben Nevis";
        Integer matchingMode = SimpleTemplateEngine.DEFAULT_MATCH;
        String result = simpleEngine.evaluate(template, pattern, value, matchingMode);
        assertEquals("That is Arthur's Seat. Ben Nevis is UK's highest mountain.", result);
    }
    
}