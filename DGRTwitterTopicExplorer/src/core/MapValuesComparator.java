package core;

import java.util.Comparator;
import java.util.Map;

public class MapValuesComparator implements Comparator<String> {
	
	Map<String, Integer> map;
	
	
	public MapValuesComparator(Map<String, Integer> map) {
        this.map = map;
    }
	
	// Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (map.get(a) > map.get(b))
        	return -1;
        else
        	if(map.get(a) < map.get(b))
        		return 1;
        else
        	return (a.compareTo(b));
        // returning 0 would merge keys
    }
    

}
