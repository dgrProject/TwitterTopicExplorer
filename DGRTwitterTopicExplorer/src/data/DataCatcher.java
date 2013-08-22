package data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

import core.MapValuesComparator;

import miscellaneous.Utility;

import domain.Pair;
import domain.Tweet;

public class DataCatcher {

	private TreeMap<String, Double> mainStreamData; // percentuali intersezioni dei termini della main query (per l'eliminazione)
	private TreeMap<String, Pair> completeSecondaryStreamsData; // mappa che contiene le percentuali finali (medie) dei candidati
	private LinkedList<TreeMap<String, Pair>> partialSecondaryStreamsData; // lista di mappe, una per ogni iterazione interna; ognuna contiene le percentuali dei candidati 
	private LinkedList<String[]> queryEvolution; // lista di due array: query prima dell'iterazione esterna e query dopo l'iterazione esterna
	private HashMap<String, Integer> hashtagToTotalTweets; // tweets totali delle candidate alla fine delle iterazioni interne
	private TreeMap<String, Integer> mainStreamFlatData; // mappa hashtag-integer dei tutti i tweet di tutti i main streams (sia quello singolo che quello multiplo)
	private int mainStreamTotalTweets; // totale di tutti i tweet di tutti i main streams (sia quello singolo che quello multiplo)
	private TreeMap<String, Integer> singleMainStreamMap; // mappa hastag-integer del main stream (quello singolo)
	private int tweetSingleMainStream; // numero tweet catturati nel main stream (quello singolo)
	private HashMap<String, LinkedList<Integer>> tweets2MultipleStreamMap; // numero dei tweet degli stream multilpli (sia quelli principali che secondari)

	

	public DataCatcher(){
		this.mainStreamData = new TreeMap<String, Double>();
		this.completeSecondaryStreamsData = new TreeMap<String, Pair>();
		this.partialSecondaryStreamsData = new LinkedList<TreeMap<String, Pair>>();
		this.queryEvolution = new LinkedList<String[]>();
		this.hashtagToTotalTweets = new HashMap<String, Integer>();
		this.mainStreamFlatData = new TreeMap<String, Integer>();
		this.tweets2MultipleStreamMap = new HashMap<String, LinkedList<Integer>>();
	}
	
	public void addTweets2MultipleStreamMap(String s, int tweets) {
		LinkedList<Integer> list;
		if (this.tweets2MultipleStreamMap.containsKey(s)) {
			list = this.tweets2MultipleStreamMap.get(s);
			list.add(tweets);
			this.tweets2MultipleStreamMap.put(s, list);
		}
		else {
			list = new LinkedList<Integer>();
			list.add(tweets);
			this.tweets2MultipleStreamMap.put(s, list);
		}
	}
	
	public HashMap<String, LinkedList<Integer>> getTweets2MultipleStreamMap() {
		return this.tweets2MultipleStreamMap;
	}
	
	
	public int getTweetSingleMainStream() {
		return tweetSingleMainStream;
	}

	public void setTweetSingleMainStream(int tweetSingleMainStream) {
		this.tweetSingleMainStream = tweetSingleMainStream;
	}

	public TreeMap<String, Integer> getSingleMainStreamMap() {
		return singleMainStreamMap;
	}
	
	public void setSingleMainStreamCandidates(TreeMap<String, Integer> singleMainStreamCandidates) {
		this.singleMainStreamMap = singleMainStreamCandidates;
	}
	
	public TreeMap<String, Double> getMainStreamData() {
		return mainStreamData;
	}

	public TreeMap<String, Pair> getCompleteSecondaryStreamsData() {
		return completeSecondaryStreamsData;
	}

	public LinkedList<TreeMap<String, Pair>> getPartialSecondaryStreamsData() {
		return partialSecondaryStreamsData;
	}

	public LinkedList<String[]> getQueryEvolution() {
		return queryEvolution;
	}

	public TreeMap<String, Integer> getHashtagToTotalTweets() {
		HashMap<String, Integer> unsoted = hashtagToTotalTweets;
		TreeMap<String, Integer> sorted = new TreeMap<String, Integer>(new MapValuesComparator(unsoted));;
		sorted.putAll(unsoted);
		return sorted;
	}

	public int getMainStreamTotalTweets() {
		return mainStreamTotalTweets;
	}

	public void addMainStreamData(String hashtag, double percent){
		this.mainStreamData.put(hashtag, Utility.fixDouble(percent));
	}
	
	public void addCompleteSecondaryStreamsData(String hashtag, Pair pair){
		this.completeSecondaryStreamsData.put(hashtag, pair.fixPair());
	}
	
	public void addPartialSecondaryStreamsData(TreeMap<String, Pair> map){
		for(String s: map.keySet())
			map.put(s, map.get(s).fixPair());
		this.partialSecondaryStreamsData.add(map);
	}
	
	public void addQueryEvolution(String[] query){
		this.queryEvolution.add(query);
	}
	
	public void addHashtagToTotalTweets(String hashtag, Integer totalTweets){
		if (!this.hashtagToTotalTweets.containsKey(hashtag)){
			this.hashtagToTotalTweets.put(hashtag, totalTweets);
		}else{
			int newTotal = this.hashtagToTotalTweets.get(hashtag) + totalTweets;
			this.hashtagToTotalTweets.put(hashtag, newTotal);
		}
	}
	
	public void setMainStreamTotalTweets(int totalTweets){
		this.mainStreamTotalTweets = totalTweets;
	}

	public TreeMap<String, Integer> getMainStreamFlatData() {
		return this.mainStreamFlatData;
	}

	public void calculateMainStreamFlatData(LinkedList<Tweet> mainStreamTweets) {
		setMainStreamTotalTweets(mainStreamTweets.size());
		String[] current = this.queryEvolution.getFirst();
		int cont;
		for(int i=0; i<current.length; i++){
			cont = 0;
			for(Tweet t: mainStreamTweets){
				if(t.hasHashtag(current[i]))
					cont++;
			}
			this.mainStreamFlatData.put(current[i], cont);
		}	
	}

}
