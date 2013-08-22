package core;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import domain.Tweet;

public class HashtagMapper {

	private HashMap<String, Integer> map;
	private TreeMap<String, Integer> sortedMap;
	private MapValuesComparator comparator;
	
	public HashtagMapper(){
		this.map = new HashMap<String, Integer>(); 
		this.comparator = new MapValuesComparator(this.map);
		this.sortedMap = new TreeMap<String, Integer>(this.comparator);
	}
	
	public void updateMap(List<Tweet> tweets){
		Integer occurr;
		for(Tweet t: tweets){
			for(String hashtag: t.getHashtags()){
				occurr = this.map.get(hashtag);
				if (occurr != null)
					this.map.put(hashtag, occurr+1);
				else
					this.map.put(hashtag, 1);
			}
		}
		this.sortedMap.putAll(this.map);
	}

	public TreeMap<String, Integer> getMap() {
		return this.sortedMap;
	}

}
