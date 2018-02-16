import static org.junit.Assert.assertEquals;

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
    
}
