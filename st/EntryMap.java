package st;

import java.util.ArrayList;
import java.util.HashSet;

public class EntryMap {

    private ArrayList<Entry> entries;

    private HashSet<Entry> uniqueEntries;

    public EntryMap(){
        entries = new ArrayList<>();
        uniqueEntries = new HashSet<>();
    }

    public void store(String pattern, String value) throws RuntimeException{
        Entry entry = new Entry(pattern, value);
        if (!isEntryValid(entry)){
            throw new RuntimeException();
        }

        if (isEntryUnique(entry)){
            addEntry(entry);
        }
    }

    private Boolean isEntryValid(Entry entry){
        if (entry.getPattern()== null)
            return Boolean.FALSE;
        if (entry.getPattern().isEmpty())
            return Boolean.FALSE;
        if (entry.getValue() == null)
            return Boolean.FALSE;
        return Boolean.TRUE;
    }

    private Boolean isEntryUnique(Entry entry){
        return !uniqueEntries.contains(entry);
    }

    private void addEntry(Entry entry){
        entries.add(entry);
        uniqueEntries.add(entry);
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }
    
    public void delete(String pattern) throws RuntimeException{
        if (pattern==null){
            throw new RuntimeException();
        }
        
        if (pattern.isEmpty()) {
        		throw new RuntimeException();
        }
        
        Entry entry = new Entry(pattern, null);
        if (uniqueEntries.contains(entry)) {
        		remove(entry);
        }
    }
    
    private void remove(Entry entry) {
	    	uniqueEntries.remove(entry);
	    	entries.remove(entry);
    }
    
    public void update(String pattern, String newValue) throws RuntimeException{
        Entry entry = new Entry(pattern, null);
        Entry newEntry = new Entry(pattern, newValue);
        if (!isEntryValid(newEntry)){
            throw new RuntimeException();
        }

        if (!isEntryUnique(entry)){
            replace(entry, newEntry);
        }
    }
    
    private void replace(Entry oldEntry, Entry newEntry) {
	    	uniqueEntries.remove(oldEntry);
	    	uniqueEntries.add(newEntry);
	    	entries.set(entries.indexOf(oldEntry), newEntry);
    }

    class Entry {
        String pattern;
        String value;

        public Entry(String pattern, String value) {
            this.pattern = pattern;
            this.value = value;
        }

        public String getPattern() {
            return pattern;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

            return this.getPattern().equals(entry.getPattern())? true: false;
        }

        @Override
        public int hashCode() {
            int result = getPattern().hashCode();
            return result;
        }
    }

}
